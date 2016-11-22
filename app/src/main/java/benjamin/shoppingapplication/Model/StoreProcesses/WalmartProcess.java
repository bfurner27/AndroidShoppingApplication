package benjamin.shoppingapplication.Model.StoreProcesses;

import benjamin.shoppingapplication.Model.APIEndpoints.WalmartSearch;
import benjamin.shoppingapplication.Model.APIEndpoints.WalmartUPC;
import benjamin.shoppingapplication.Model.RequestData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class WalmartProcess implements StoreProcess {

    @Override
    public void retrieveData(RequestData requestData) {
        WalmartUPC walmartUPC = new WalmartUPC(requestData);
        walmartUPC.execute(null, null, null);

        // TODO actually hook this up to the system by ensureing that the search parameter is called if necessary or not called otherwise
        WalmartSearch walmartSearch = new WalmartSearch(new RequestData(null, "Colgate Toothpaste", null));
        walmartSearch.execute(null, null, null);

    }
}
