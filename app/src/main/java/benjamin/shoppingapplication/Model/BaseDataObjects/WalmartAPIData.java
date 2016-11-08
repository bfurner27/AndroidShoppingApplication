package benjamin.shoppingapplication.Model.BaseDataObjects;

import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class WalmartAPIData extends APIData {
    /*****************************************************
     * Constructors
     *****************************************************/
    public WalmartAPIData() {
        super("", "", "", "", "", "Walmart", 0);
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
                         String productURL, Integer productNumber) {
        super(price, name, description, pictureURL, productURL, "Walmart", productNumber);
    }


    @Override
    public void parseJSON(String json) {

    }

    @Override
    public String toString() {
        String data = "";

        return data;
    }
}
