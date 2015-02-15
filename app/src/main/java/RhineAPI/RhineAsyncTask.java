package RhineAPI;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import com.example.android.mashtags.R;
import com.example.android.mashtags.apiKey;

/**
 * Created by ken on 2/15/15.
 */

public class RhineAsyncTask extends AsyncTask<String, String, String>
{
    private Context mContext;
    private View rootView;

    public RhineAsyncTask(Context context, View rootView){
        this.mContext = context;
        this.rootView = rootView;
    }

    @Override
    protected String doInBackground(String... arg0) {
        String mApiKey = apiKey.getRhineKey();
        String mImageURL = "http://i.imgur.com/jaVav95.jpg";

        Rhine rhine = new Rhine(mApiKey);
        String result = rhine.run(rhine.extraction(rhine.image(mImageURL)));

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        TextView textView = (TextView) rootView.findViewById(R.id.example_text);
        textView.setText(result);
    }
}