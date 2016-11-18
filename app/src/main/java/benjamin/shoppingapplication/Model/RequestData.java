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
    private String searchIndex;

    /**
     * If the UPC is the only available value then this constructor can be used.
     * @param upc - the UPC number from a barcode or product
     */
    public RequestData(String upc) {
        this.upc = upc;
        this.search = null;
        this.productNumber = null;
        this.searchIndex = null;
    }


    /**
     * Allows the user to initialize data if there is no product number
     * @param upc - The UPC number from a barcode
     * @param searchIndex - The search value to search through the API
     */
    public RequestData(String upc, String searchIndex) {
        this.upc = upc;
        this.search = null;
        this.productNumber = null;
        this.searchIndex = searchIndex;
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
        this.searchIndex = null;
    }

    /**
     * This will take the extra parameter of a search index to make sure that more accurate
     * results can be obtained
     * @param searchIndex - the index or category of items to be searched for
     * @param productNumber - the number of the product such as an ID
     * @param search - the search string to enter into the search
     * @param upc - the upc number of a specific product
     */
    public RequestData(String searchIndex, Integer productNumber, String search, String upc) {
        this.searchIndex = searchIndex;
        this.productNumber = productNumber;
        this.search = search;
        this.upc = upc;
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

    public boolean hasSearchIndex() {
        boolean eSearchIndex = false;
        if (searchIndex != null) {
            eSearchIndex = true;
        }
        return eSearchIndex;
    }

    public String getUpc() {
        return upc;
    }

    public String getSearch() {
        return search;

    }

    public String getSearchIndex () {
        return searchIndex;
    }

    public Integer getProductNumber() {
        return productNumber;
    }
}
