package com.example.android.mashtags;

import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import InstagramOAuth.InstagramApp;
import InstagramOAuth.InstagramApp.OAuthAuthenticationListener;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**Rhine API Call**/
        new RhineAsyncTask().execute();


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static final String LOG_TAG = PlaceholderFragment.class.getSimpleName();

        InstagramApp mApp;
        Button btnConnect;
        TextView tvSummary;

        public PlaceholderFragment() {
        }

        OAuthAuthenticationListener listener = new OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                tvSummary.setText("Connected as " + mApp.getUserName());
                btnConnect.setText("Disconnect");
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        };

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            /**
             * Instagram OAuth Library Initialization
             **/
            mApp = new InstagramApp(getActivity(), apiKey.getInstagramId(),
                    apiKey.getInstagramSecret(), apiKey.getInstagramCallback());
            mApp.setListener(listener);

            tvSummary = (TextView) rootView.findViewById(R.id.tvSummary);

            btnConnect = (Button) rootView.findViewById(R.id.btnConnect);
            btnConnect.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    if (mApp.hasAccessToken()) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(
                                getActivity());
                        builder.setMessage("Disconnect from Instagram?")
                                .setCancelable(false)
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                mApp.resetAccessToken();
                                                btnConnect.setText("Connect");
                                                tvSummary.setText("Not connected");
                                            }
                                        })
                                .setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        final AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        mApp.authorize();
                    }
                }
            });

            if (mApp.hasAccessToken()) {
                tvSummary.setText("Connected as " + mApp.getUserName());
                btnConnect.setText("Disconnect");
            }

            return rootView;
        }
    }

    private class RhineAsyncTask extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... params) {
            String mApiKey = apiKey.getRhineKey();
            String mImageURL = "http://i.imgur.com/jaVav95.jpg";

            Rhine rhine = new Rhine(mApiKey);
            String result = rhine.run(rhine.extraction(rhine.image(mImageURL)));

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            TextView textView = (TextView) findViewById(R.id.example_text);
            textView.setText(result);
        }
    }
}