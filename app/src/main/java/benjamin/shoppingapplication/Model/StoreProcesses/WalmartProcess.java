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
        RequestData rD = new RequestData("035000521019");
        //TODO remove above item
        WalmartUPC walmartUPC = new WalmartUPC();
        walmartUPC.setRequestData(rD); //TODO replace rD with requestData which will be passed in
        walmartUPC.execute(null, null, null);
    }
}
