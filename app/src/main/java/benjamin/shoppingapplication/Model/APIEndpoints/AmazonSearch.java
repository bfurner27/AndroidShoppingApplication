package benjamin.shoppingapplication.Model.APIEndpoints;

import android.os.AsyncTask;
import android.util.Log;

import benjamin.shoppingapplication.Model.RequestData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class AmazonSearch extends AsyncTask<Void, Integer, String> {
    private RequestData requestData;

    public AmazonSearch() {
        this.requestData = null;
    }

    @Override
    protected void onPreExecute() {
        if (requestData == null) {
            cancel(true);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String result) {

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onCancelled(String result) {
        Log.e("ASYNCERROR", "The request data was not provided or the request failed");
    }


    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }
}
