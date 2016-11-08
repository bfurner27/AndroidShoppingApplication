package benjamin.shoppingapplication.Model.StoreProcesses;

import benjamin.shoppingapplication.Model.RequestData;

/**
 * Created by Benjamin on 11/3/2016.
 * This class will be used to ensure that the store process uses the same function
 * This will mostley be used so that one can abstract the StoreProcess process so that the
 * controller and the model can be further split
 */

public interface StoreProcess {
    /**
     * The data itself will actually be modified in a controller class that will maintain a list
     * of the data results
     * @param requestData - This will be an object that contains the possible request options
     */
    public void retrieveData(RequestData requestData);
}
