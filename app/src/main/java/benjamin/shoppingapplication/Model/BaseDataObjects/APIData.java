package benjamin.shoppingapplication.Model.BaseDataObjects;

/**
 * Created by Benjamin on 11/3/2016.
 */

public abstract class APIData {
    private String price;
    private String name;
    private String description;
    private String pictureURL;
    private String productURL;
    private String storeName;
    private String productNumber;

    /**
     * This will take the data as a string and will parse the JSON accordingly
     */
    public abstract void parse(String data);

    @Override
    public abstract String toString();


    /****************************************
     * Constructors
     ****************************************/

    public APIData(String price, String name, String description, String pictureURL,
                   String productURL, String storeName, String productNumber) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.pictureURL = pictureURL;
        this.productURL = productURL;
        this.storeName = storeName;
        this.productNumber = productNumber;
    }

    public APIData() {
        this.price = "999999999999999999999.99";
        this.name = "";
        this.description = "";
        this.pictureURL = "";
        this.productURL = "";
        this.storeName = "";
        this.productNumber = "";
    }


    /****************************************
     * Getters/Setters
     ****************************************/

    public String getProductNumber() { return productNumber; }

    public void setProductNumber(String productNumber) { this.productNumber = productNumber; }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getProductURL() {
        return productURL;
    }

    public void setProductURL(String productURL) {
        this.productURL = productURL;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
}
