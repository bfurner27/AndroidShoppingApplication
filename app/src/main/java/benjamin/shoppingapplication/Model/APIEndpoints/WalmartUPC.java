package benjamin.shoppingapplication.Model.APIEndpoints;

import android.app.DownloadManager;
import android.os.AsyncTask;
import android.util.Log;
import android.util.StringBuilderPrinter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Controller.MainController;
import benjamin.shoppingapplication.Model.APIListData;
import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;
import benjamin.shoppingapplication.Model.BaseDataObjects.WalmartAPIData;
import benjamin.shoppingapplication.Model.RequestData;
import benjamin.shoppingapplication.View.MainActivity;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class WalmartUPC extends AsyncTask<Void, Integer, String> {
    private RequestData requestData;
    private String requestURL;

    public WalmartUPC(RequestData requestData) {
        this.requestData = requestData;
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
        try {
            return rH.generateAPIResults(requestURL);
        } catch (Exception e) {
            Log.e("AmazonUPC", e.getStackTrace().toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("WalmartUPC", "Entering the Post Execute Section");

        if (result == null) {
            APIListData.getInstance().setErrorFlag();
        } else {

            Gson jsonParser = new Gson();
            JsonObject res = jsonParser.fromJson(result, new TypeToken<JsonObject>() {
            }.getType());

            // parse the object into an array
            JsonArray items = res.get("items").getAsJsonArray();

            List<APIData> walmartItems = new ArrayList<>();

            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                String jsonItem = jsonParser.toJson(item);
                walmartItems.add(new WalmartAPIData(jsonItem));

            }


            APIListData.getInstance().updateListData(walmartItems);
        }   // end else

        MainController.getInstance().updateComparisonList();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onCancelled(String result) {
        Log.e("ASYNCERROR", "The request data was not provided or the request failed");
    }
}
