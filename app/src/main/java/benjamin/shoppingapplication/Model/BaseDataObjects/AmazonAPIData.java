package benjamin.shoppingapplication.Model.BaseDataObjects;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class AmazonAPIData extends APIData {

    /*****************************************************
     * Constructors
     *****************************************************/
    public AmazonAPIData() {
        super("", "", "", "", "", "Amazon", "");
    }

    /****************************************
     * The non default constructor, mostly used so that the super classes constructor will be
     * called with the correct non-default values.
     *
     * @param price - the price of the product
     * @param name - the name of the product
     * @param description - a description of the product
     * @param pictureURL - what he product looks like
     * @param productURL - where the product can be located
     * @param productNumber - the number of the product if applicable
     */
    public AmazonAPIData(String price, String name, String description, String pictureURL,
                         String productURL, String productNumber) {
        super(price, name, description, pictureURL, productURL, "Amazon", productNumber);
    }

    public AmazonAPIData(String dataToParse) {
        parse(dataToParse);
    }




    @Override
    public void parse(String data) {
        Log.i ("AmazonAPIData", "data: " + data);
        try {
            DocumentBuilderFactory xmlBuildFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder xmlBuilder = xmlBuildFactory.newDocumentBuilder();

            InputStream inStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
            Document xml = xmlBuilder.parse(inStream);

            Element item = xml.getDocumentElement();   // gets the root element

            NodeList tagList =  item.getElementsByTagName("ASIN");
            if  (tagList.getLength() > 0) {
                Node asin = tagList.item(0);
                setProductNumber(asin.getTextContent());
            } else {
                Log.e ("AmazonAPIData", "Parse: failed to find the ASIN number");
            }

            tagList = item.getElementsByTagName("DetailPageURL");
            if (tagList.getLength() > 0) {
                Node detailPageURL = tagList.item(0);
                setProductURL(detailPageURL.getTextContent());
            } else {
                Log.e ("AmazonAPIData", "Parse: failed to find the DetailPageURL");
            }

            tagList = item.getElementsByTagName("Title");
            if (tagList.getLength() > 0) {
                Node productTitle = tagList.item(0);
                setName(productTitle.getTextContent());
            }else {
                Log.e ("AmazonAPIData", "Parse: failed to find the Title/name");
            }

            tagList = item.getElementsByTagName("FormattedPrice");
            if (tagList.getLength() > 0) {
                Node price = tagList.item(0);
                setPrice(price.getTextContent());
            }else {
                Log.e ("AmazonAPIData", "Parse: failed to find the price");
            }

            Log.i ("AmazonAPIData", "DataParsedResult: " + toString());


            tagList = item.getElementsByTagName("SmallImage");
            if (tagList.getLength() > 0) {
                Node smallImage = tagList.item(0);
                Node url = smallImage.getFirstChild();
                if (url != null && url.getNodeName().equals("URL")) {
                    setPictureURL(url.getTextContent());
                }
            }else {
                Log.e ("AmazonAPIData", "Parse: failed to find the pictureURL");
            }

            Log.i ("AmazonAPIData", "DataParsedResult: " + toString());




        } catch (ParserConfigurationException e) {
            Log.e("AmazonAPIData", "message: " + e.getMessage());
        } catch (IOException e) {
            Log.e("AmazonAPIData", "message: " + e.getMessage());
        } catch (SAXException e) {
            Log.e("AmazonAPIData", "message: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        String data = getName() + " - " + getProductNumber() + ": " + getPrice() + "Description: "
                + getDescription() + "\nproductURL: " + getProductURL() + "\npictureURL: "
                + getPictureURL();

        return data;
    }
}
