package benjamin.shoppingapplication.Model.APIEndpoints;

import android.nfc.FormatException;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.util.Base64;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Model.APIListData;
import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;
import benjamin.shoppingapplication.Model.BaseDataObjects.AmazonAPIData;
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
                    "ResponseGroup=Offers,Images,ItemAttributes&" +
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
        Log.i("AmazonUPC", "Leaving the background thread");
        return rH.generateAPIResults(requestURL);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("Amazon", "Entered post execute section");
        //TODO come up with a better log statement or no log statement at all!

        // parse the data into the separate amazon data objects
        try {
            DocumentBuilderFactory xmlBuildFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = xmlBuildFactory.newDocumentBuilder();

            InputStream inStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document xml = xmlBuilder.parse(inStream);

           // Element root = xml.getDocumentElement();

            NodeList listItems = xml.getElementsByTagName("Item");

            List<APIData> itemList = new ArrayList<>();

            for (int i = 0; i < listItems.getLength(); i++) {
                Node item = listItems.item(i);

                // found this code at:
                // http://stackoverflow.com/questions/2223020/convert-an-org-w3c-dom-node-into-a-string
                StringWriter writer = new StringWriter();
                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(item), new StreamResult(writer));
                String output = writer.toString();
                // end of code

                AmazonAPIData aAD = new AmazonAPIData(output);

                itemList.add(aAD);
            }


            // this line updates the interface go between.
            APIListData.getInstance().updateListData(itemList);

        } catch (ParserConfigurationException e) {
            Log.e("AmazonUPC", "message: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("AmazonUPC", "message: " + e.getMessage());
            e.printStackTrace();
        } catch (SAXException e) {
            Log.e("AmazonUPC", "message: " + e.getMessage());
            e.printStackTrace();
        } catch (TransformerException e) {
            Log.e("AmazonUPC", "message: " + e.getMessage());
            e.printStackTrace();
        }

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
