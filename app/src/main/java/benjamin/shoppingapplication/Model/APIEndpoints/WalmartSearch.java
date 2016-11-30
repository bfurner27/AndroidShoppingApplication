package benjamin.shoppingapplication.Model.APIEndpoints;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Controller.MainController;
import benjamin.shoppingapplication.Model.APIListData;
import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;
import benjamin.shoppingapplication.Model.BaseDataObjects.WalmartAPIData;
import benjamin.shoppingapplication.Model.RequestData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class WalmartSearch extends AsyncTask<Void, Integer, String> {
    private RequestData requestData;
    private String requestURL;

    public WalmartSearch(RequestData requestData) {
        this.requestData = requestData;
    }


    @Override
    protected void onPreExecute() {
        Log.i("WalmartSearch", "Entered the pre-execute section");
        if (requestData == null || !requestData.hasSearch()) {
            cancel(true);
        } else {
            requestURL = "http://api.walmartlabs.com/v1/search?"
                    + "query=" + formatSearchString(requestData.getSearch())
                    + "&format=json"
                    + "&apiKey=" + APIKeyAccess.getInstance().getAPIKey("walmart_key");

            Log.i("WalmartSearch", "url:" + requestURL);
        }
    }

    /**
     * some code retrieved from
     * http://www.androidauthority.com/use-remote-web-api-within-android-app-617869/
     */
    @Override
    protected String doInBackground(Void... params) {
        Log.i("WalmartSearch", "entered background");
        RequestHelper rH = new RequestHelper();
        return rH.generateAPIResults(requestURL);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("WalmartSearch", "Entering the Post Execute Section");

        Gson jsonParser = new Gson();
        JsonObject res = jsonParser.fromJson(result, new TypeToken<JsonObject>(){}.getType());

        // parse the object into an array
        JsonArray items = res.get("items").getAsJsonArray();

        List<APIData> walmartItems = new ArrayList<>();

        for (int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            String jsonItem = jsonParser.toJson(item);
            walmartItems.add(new WalmartAPIData(jsonItem));

        }

        APIListData.getInstance().updateListData(walmartItems);
        MainController.getInstance().updateComparisonList();


    }

    @Override
    protected void onProgressUpdate(Integer... progress) {

    }

    @Override
    protected void onCancelled(String result) {
        Log.e("ASYNCERROR", "The request data was not provided or the request failed");
    }


    private String formatSearchString(String uSearchString) {

        return uSearchString.replace(" ", "+");
    }
}
