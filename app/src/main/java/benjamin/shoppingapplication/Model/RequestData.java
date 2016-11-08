package benjamin.shoppingapplication.Model;

/**
 * Created by Benjamin on 11/3/2016.
 *
 * This class is used for passing the data of the request to the actual request. There is no
 * default constructors or setters because the data is controlled in such a way that if the
 * value is null then it cannot be passed to a request.
 */

public class RequestData {
    private String upc;
    private String search;
    private Integer productNumber;

    /**
     * If the UPC is the only available value then this constructor can be used.
     * @param upc - the UPC number from a barcode or product
     */
    public RequestData(String upc) {
        this.upc = upc;
        this.search = null;
        this.productNumber = null;
    }

    /**
     * Allows the user to initialize data if there is no product number
     * @param upc - The UPC number from a barcode
     * @param search - The search value to search through the API
     */
    public RequestData(String upc, String search) {
        this.upc = upc;
        this.search = search;
        this.productNumber = null;
    }

    /**
     * Ensure that there is the option to set any of the variables. This function can also be used
     * with putting null values in for the params that do not exist and it will ensure they are
     * not initialized.
     * @param upc - The UPC number from a barcode
     * @param search - The search value to send with the API request
     * @param productNumber - The product number if it is known
     */
    public RequestData(String upc, String search, Integer productNumber) {
        this.upc = upc;
        this.search = search;
        this.productNumber = productNumber;
    }

    /**

     * checks if the UPC number exists or has been initialized
     * @return false if the upc value is null
     */
    public boolean hasUPC() {
        boolean isUpc = false;
        if (upc != null) {
            isUpc = true;
        }
        return isUpc;
    }

    /**
     * checks if the search variable has been given a value
     * @return false if the search value is null
     */
    public boolean hasSearch() {
        boolean isSearch = false;
        if (search != null) {
            isSearch = true;
        }
        return isSearch;
    }

    /**
     * Checks that the class has a product number
     * @return false if the product number is null
     */
    public boolean hasProductNumber () {
        boolean eProductNumber = false;
        if (productNumber != null) {
            eProductNumber = true;
        }
        return eProductNumber;
    }

    public String getUpc() {
        return upc;
    }

    public String getSearch() {
        return search;

    }

    public Integer getProductNumber() {
        return productNumber;
    }
}
