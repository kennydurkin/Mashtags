package com.example.android.mashtags;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import InstagramOAuth.InstagramApp;
import RhineAPI.RhineAsyncTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MashFragment extends Fragment {
    private static final String LOG_TAG = MashFragment.class.getSimpleName();

    InstagramApp mApp;
    Button btnConnect;
    TextView tvSummary;
    TextView hashtagView;
    TextView countView;

    public MashFragment() {
    }

    InstagramApp.OAuthAuthenticationListener listener = new InstagramApp.OAuthAuthenticationListener() {
        @Override
        public void onSuccess() {
            /**
             * Sorting algorithm of the hashmap
             **/
            SortedMap mHashtags = MapValueSort.getValueSortedMap(mApp.hashtags);
            //Iterator tagItr = mHashtags.keySet().iterator();
            //Iterator cntItr = mHashtags.values().iterator();

            /*Log.i(LOG_TAG, "BEGIN SORTED LIST");
            while (tagItr.hasNext() && cntItr.hasNext()) {
                Log.e(LOG_TAG,"The tag #" + tagItr.next() + " had " + cntItr.next() + " matches.");
            }
            Log.i(LOG_TAG,"END SORTED LIST");*/

            Set keys = mHashtags.keySet();
            Iterator itr = keys.iterator();

            for (int i=0; i<5; i++)
            {
                String key = (String) itr.next();
                Integer value = (Integer) mHashtags.get(key);
                hashtagView.append("\n"+key+"\n");
                countView.append("\n"+value.toString()+"\n");
            }

            //MapAdapter adapter = new MapAdapter(mHashtags);
            //list.setAdapter(adapter);

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


        /**Rhine API Call**/
        new RhineAsyncTask(getActivity(),rootView).execute();

        /**
         * Instagram OAuth Library Initialization
         **/
        mApp = new InstagramApp(getActivity(), apiKey.getInstagramId(),
                apiKey.getInstagramSecret(), apiKey.getInstagramCallback());
        mApp.setListener(listener);

        hashtagView = (TextView) rootView.findViewById(R.id.text1);
        countView = (TextView) rootView.findViewById(R.id.text2);


        tvSummary = (TextView) rootView.findViewById(R.id.tvSummary);

        btnConnect = (Button) rootView.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new View.OnClickListener() {

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