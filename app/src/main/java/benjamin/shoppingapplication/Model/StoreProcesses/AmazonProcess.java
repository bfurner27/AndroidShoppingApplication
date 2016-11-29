package benjamin.shoppingapplication.Model.StoreProcesses;

import benjamin.shoppingapplication.Model.APIEndpoints.AmazonSearch;
import benjamin.shoppingapplication.Model.APIEndpoints.AmazonUPC;
import benjamin.shoppingapplication.Model.RequestData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class AmazonProcess implements StoreProcess {
    @Override
    public void retrieveData(RequestData requestData) {
        if (requestData.hasUPC()) {
            AmazonUPC amazonUPC = new AmazonUPC(requestData);
            amazonUPC.execute(null, null, null);
        } else if (requestData.hasSearch()) {
            // TODO make sure that this is actually flushed out instead of just dummy data
            AmazonSearch amazonSearch = new AmazonSearch(requestData);
            amazonSearch.execute(null, null, null);
        }
    }
}
