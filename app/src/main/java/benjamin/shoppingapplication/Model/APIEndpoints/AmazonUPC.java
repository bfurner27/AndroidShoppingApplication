package benjamin.shoppingapplication.Model.APIEndpoints;

import android.nfc.FormatException;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Model.RequestData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class AmazonUPC extends AsyncTask<Void, Integer, String> {
    private RequestData requestData;
    private String requestURL;

    public AmazonUPC(RequestData requestData) {
        this.requestData = requestData;
    }

    @Override
    protected void onPreExecute() {
        if (requestData == null || !requestData.hasUPC()) {
            Log.e("AmazonUPC", "There was no upc or request data passed into this endpoint");
            cancel(true);
        } else {
            requestURL = "Service=AWSECommerceService&" +
                    "Version=2013-08-01&" +
                    "Operation=ItemLookup&" +
                    "ItemId=" + requestData.getUpc() + "&" +
                    "IdType=UPC&" +
                    "SearchIndex=";
            if (requestData.hasSearchIndex()) {
                requestURL += requestData.getSearchIndex();
            } else {
                requestURL += "All";
            }


            AmazonSignatureGenerationHelper helper = new AmazonSignatureGenerationHelper();
            requestURL = helper.generateSignature(requestURL);
            Log.i("AmazonUPC", "Generated URL: " + requestURL);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.i("AmazonUPC", "Entering the background thread");
        RequestHelper rH = new RequestHelper();
        return rH.generateAPIResults(requestURL);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("Amazon", "The final result just blahed into the logs\n" + result);
        //TODO come up with a better log statement or no log statement at all!
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
