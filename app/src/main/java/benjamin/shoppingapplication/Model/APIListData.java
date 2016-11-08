package benjamin.shoppingapplication.Model;

import java.util.ArrayList;
import java.util.List;

import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;

/**
 * Created by Benjamin on 11/7/2016.
 */

public class APIListData {
    private List<APIData> listData;
    private static final APIListData instance = new APIListData();

    private APIListData() {
        // defies creating an instance
        listData = new ArrayList<>();
    }

    public static final APIListData getInstance() {
        return instance;
    }

    public List<APIData> getListData() {
        return listData;
    }

    /**
     * This will add the elements passed in the update list to the internal list
     * @param updateList - A list of elements to add to the internal list
     */
    public void updateListData(List<APIData> updateList) {
        for (APIData aD : updateList) {
            listData.add(aD);
        }
    }

    public void resetData() {
        listData.clear();
    }
}
