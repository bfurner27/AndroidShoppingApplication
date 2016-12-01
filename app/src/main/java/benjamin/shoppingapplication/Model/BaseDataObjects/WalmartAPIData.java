package benjamin.shoppingapplication.Model.BaseDataObjects;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class WalmartAPIData extends APIData {

    // private data variables
    private Boolean online;

    /*****************************************************
     * Constructors
     *****************************************************/
    public WalmartAPIData() {
        super("", "", "", "", "", "Walmart", "");
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
    public WalmartAPIData(String price, String name, String description, String pictureURL,
                         String productURL, String productNumber) {
        super(price, name, description, pictureURL, productURL, "Walmart", productNumber);
        online = null;
    }

    public WalmartAPIData (String jsonObject) {
        super("9999999999999.99", "", "", "", "", "Walmart", "");
        online = false;
        parse(jsonObject);
    }


    @Override
    public void parse(String data) {
        Gson parser = new Gson();
        JsonObject item = parser.fromJson(data, new TypeToken<JsonObject>(){}.getType());

        // parse the json into the data objects set aside
        // set up the checks to ensure that the data does exist in the json object
        if (item.get("name") != null) {
            setName(item.get("name").getAsString());
        }

        if (item.get("salePrice") != null) {
            setPrice(item.get("salePrice").getAsString());
        }

        if (item.get("thumbnailImage") != null) {
            setPictureURL(item.get("thumbnailImage").getAsString());
        }

        if (item.get("itemId") != null) {
            setProductNumber(item.get("itemId").getAsString());
        }

        if (item.get("productUrl") != null) {
            setProductURL(item.get("productUrl").getAsString());
        }

        if (item.get("availableOnline") != null) {
            online = item.get("availableOnline").getAsBoolean();
        }

        Log.i("WalmartAPIData", "ParseData: \n-----------------\n" + toString());
    }

    @Override
    public String toString() {
        String data =
                "company: " + getStoreName() + "\n" +
                "name:    " + getName() + "\n" +
                "price:   " + getPrice() + "\n" +
                "image:   " + getPictureURL() + "\n" +
                "url:     " + getProductURL() + "\n" +
                "num:     " + getProductNumber() + "\n" +
                "online:  " + online + "\n";
        return data;
    }
}
