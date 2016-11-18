package benjamin.shoppingapplication.Model.APIEndpoints;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.StringBuilderPrinter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Model.RequestData;
import benjamin.shoppingapplication.View.MainActivity;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class WalmartUPC extends AsyncTask<Void, Integer, String> {
    private RequestData requestData;
    private String requestURL;

    public WalmartUPC() {
        this.requestData = null;
    }

    @Override
    protected void onPreExecute() {
        Log.i("WalmartUPC", "Entered the pre-execute section");
        if (requestData == null || !requestData.hasUPC()) {
            cancel(true);
        } else {
            requestURL = "http://api.walmartlabs.com/v1/items?" + "apiKey="
                    + APIKeyAccess.getInstance().getAPIKey("walmart_key") + "&upc="
                    + requestData.getUpc();
        }
    }

    /**
     * some code retrieved from
     * http://www.androidauthority.com/use-remote-web-api-within-android-app-617869/
     */
    @Override
    protected String doInBackground(Void... params) {
        Log.i("WalmartUPC", "entered background");
        RequestHelper rH = new RequestHelper();
        return rH.generateAPIResults(requestURL);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("WalmartUPC", "The final result just blahed into the logs\n" + result);
        //TODO come up with a better log statement or no log statement at all!
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onCancelled(String result) {
        Log.e("ASYNCERROR", "The request data was not provided or the request failed");
    }


    /**
     * TODO determine if the request should be set every time before it is set or if this class should be created multiple times
     * @param requestData
     */
    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }
}
