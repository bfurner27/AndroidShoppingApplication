package benjamin.shoppingapplication.Model.StoreProcesses;

import benjamin.shoppingapplication.Model.APIEndpoints.WalmartUPC;
import benjamin.shoppingapplication.Model.RequestData;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class WalmartProcess implements StoreProcess {

    @Override
    public void retrieveData(RequestData requestData) {
        //TODO request data will be sent in from the variable above but for now a temp variable will be added which needs to be removed

        //TODO remove above item
        WalmartUPC walmartUPC = new WalmartUPC();
        walmartUPC.setRequestData(requestData);
        walmartUPC.execute(null, null, null);
    }
}
