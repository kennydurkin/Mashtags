package RhineAPI;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.mashtags.R;
import com.example.android.mashtags.apiKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by ken on 2/15/15.
 */

public class RhineAsyncTask extends AsyncTask<String, String, String>
{
    private static final String LOG_TAG = "Rhine Log";

    public static String queryTag = "";
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

        try {
            JSONObject jsonObj = (JSONObject) new JSONTokener(result).nextValue();
            JSONObject success = jsonObj.getJSONObject("success");
            JSONArray extraction = success.getJSONArray("extraction");
            JSONObject primary = extraction.getJSONObject(0);
            queryTag = primary.getString("entity");

            Log.i(LOG_TAG,"Best hashtag was " + queryTag);
            Log.i(LOG_TAG,"Relevancy of " + primary.getString("relevance"));
        } catch(JSONException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        TextView textView = (TextView) rootView.findViewById(R.id.hashtag_text);
        textView.setText("Finding hashtags similar to: " + queryTag);
    }
}