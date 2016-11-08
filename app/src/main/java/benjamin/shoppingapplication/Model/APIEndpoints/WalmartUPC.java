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
    private MainActivity mainActivity;

    public WalmartUPC() {
        this.requestData = null;
    }

    @Override
    protected void onPreExecute() {
        Log.i("WalmartUPC", "Entered the pre-execute section");
        if (requestData == null) {
            cancel(true);
        }

    }

    /**
     * some code retrieved from
     * http://www.androidauthority.com/use-remote-web-api-within-android-app-617869/
     */
    @Override
    protected String doInBackground(Void... params) {
        Log.i("WalmartUPC", "entered background");
        String result = null;

        // get the key from the properties file
        String API_KEY = APIKeyAccess.getInstance().getAPIKey("walmart_key");

        if (requestData.hasUPC()) {
            try {
                // build the url for the request
                URL url = new URL("http://api.walmartlabs.com/v1/items?" + "apiKey="
                        + API_KEY + "&upc=" + requestData.getUpc());

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(httpURLConnection.getInputStream()));

                    StringBuilder stringBuilder = new StringBuilder();

                    // Read in the entire request information and parse it into a string
                    String line = "";
                    // if the line returns null then it is the end of the input
                    while ((line = bufferedReader.readLine()) != null) {
                        // append the line with a newline at the end to ensure it maintains
                        // the json format
                        stringBuilder.append(line).append("\n");
                    }

                    result = stringBuilder.toString();

                } finally {
                    httpURLConnection.disconnect();
                }

            } catch (MalformedURLException e) {
                //TODO remove the stack trace once satisfied it is working
                e.printStackTrace();
                Log.e("WalmartUPC", "Failed to create the URL as expected");
            } catch (IOException e) {
                //TODO remove the stack trace once satisfied it is working
                e.printStackTrace();
                Log.e("WalmartUPC", "Failed to send the url through the connection and the file failed");
            }
        } else {
            Log.e("WalmartUPC", "Unable to find the UPC number to send with the request");
        }

        return result;
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
