package benjamin.shoppingapplication.Controller;

import java.util.ArrayList;
import java.util.List;

import benjamin.shoppingapplication.Model.APIListData;
import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;
import benjamin.shoppingapplication.Model.RequestData;
import benjamin.shoppingapplication.Model.StoreProcesses.StoreProcess;
import benjamin.shoppingapplication.Model.StoreProcesses.StoreProcessFactory;
import benjamin.shoppingapplication.View.MainActivity;

/**
 * Created by Benjamin on 11/7/2016.
 */

public class MainController {
    private List<StoreProcess> stores = null;
    private static final MainController instance = new MainController();
    private MainActivity mainActivity;

    /**
     * This will loop through all the available stores and will get a reference to each for later
     * use, mostly in the queryAPI's function. This will allow for me to be able to ensure that this
     * section is completely decoupled from the model.
     */
    private MainController() {
        stores = new ArrayList<>();

        // loop through every string in the array of getListObjects and create one instance of each
        for (String process : StoreProcessFactory.getInstance().getListObjects()) {
            stores.add(StoreProcessFactory.getInstance().createObject(process));
        }

    }

    /**
     * QueryAPI's is the first point of contact within the main activity. for this reason it is also
     * used to initialize the mainActivities data which will be used later when the APIEnpoints need
     * to notify the main activity what is happening
     * @param request - The data that will be used int he request. Obtained from the UI
     * @param mainActivity - The reference to the main data so that communication can happen both
     *                     ways
     */
    public void queryAPIs(RequestData request, MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        APIListData.getInstance().resetData();
        //TODO flush out this function, remove all the contents once I am prepared to edit this section
        for (StoreProcess process : stores) {
            process.retrieveData(request); //TODO ensure that this is correct, I believe it will end up being like this in the end
        }
    }

    public List<APIData> getListData() {
        return APIListData.getInstance().getListData();
    }

    public void updateComparisonList() {
        mainActivity.updateComparisonList();
    }

    public static  MainController getInstance() {
        return instance;
    }
}
