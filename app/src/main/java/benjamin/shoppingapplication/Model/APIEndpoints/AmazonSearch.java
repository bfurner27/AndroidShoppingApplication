package benjamin.shoppingapplication.Model.APIEndpoints;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import benjamin.shoppingapplication.Controller.MainController;
import benjamin.shoppingapplication.Model.APIListData;
import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;
import benjamin.shoppingapplication.Model.BaseDataObjects.AmazonAPIData;
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
                    "ResponseGroup=Offers,Images,ItemAttributes&" +
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

        Log.i("AmazonSearch", "Exiting the thread");
        return rh.generateAPIResults(requestURL);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.i("AmazonSearch", "Entering the Post Execute");
        //TODO replace this with a log statement that actually has information not just the result
        try {
            DocumentBuilderFactory xmlBuildFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = xmlBuildFactory.newDocumentBuilder();

            InputStream inStream = new ByteArrayInputStream(result.getBytes("UTF-8"));
            Document xml = xmlBuilder.parse(inStream);

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

            APIListData.getInstance().updateListData(itemList);
            MainController.getInstance().updateComparisonList();
        } catch (SAXException e) {
            Log.e("AmazonSearch", e.getMessage());
        } catch (IOException e) {
            Log.e("AmazonSearch", e.getMessage());
        } catch (ParserConfigurationException e) {
            Log.e("AmazonSearch", e.getMessage());
        } catch (TransformerException e) {
            Log.e("AmazonSearch", e.getMessage());
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
