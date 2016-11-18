package benjamin.shoppingapplication.Model.APIEndpoints;

import android.os.AsyncTask;
import android.util.Log;

import benjamin.shoppingapplication.Model.RequestData;


/**
 * Created by Benjamin on 11/3/2016.
 * This class is meant to send a request to amazon based on a search value
 * The search string can be passed in through the RequestData object and a search
 * will be performed which will return several results
 */

public class AmazonSearch extends AsyncTask<Void, Integer, String> {
    private RequestData requestData;
    private String requestURL;

    public AmazonSearch(RequestData requestData) {
        this.requestData = requestData;
    }

    @Override
    protected void onPreExecute() {
        if (requestData == null || !requestData.hasSearch()) {
            cancel(true);
        } else {
            String searchParams = "http://webservices.amazon.com/onca/xml?" +
                    "Service=AWSECommerceService&" +
                    "Operation=ItemSearch&" +
                    "Keywords=" + requestData.getSearch() + "&" +
                    "SearchIndex=";

            if (requestData.hasSearchIndex()) {
                searchParams += requestData.getSearchIndex();
            } else {
                searchParams += "All";
            }

            AmazonSignatureGenerationHelper asgh = new AmazonSignatureGenerationHelper();
            requestURL = asgh.generateSignature(searchParams);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.i("AmazonSearch", "Entering the thread");
        RequestHelper rh = new RequestHelper();


        return rh.generateAPIResults(requestURL);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("AmazonSearch", "Data: " + result);
        //TODO replace this with a log statement that actually has information not just the result
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
