package benjamin.shoppingapplication.Model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import benjamin.shoppingapplication.Model.APIListData;
import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;
import benjamin.shoppingapplication.Model.BaseDataObjects.AmazonAPIData;
import benjamin.shoppingapplication.Model.BaseDataObjects.TargetAPIData;
import benjamin.shoppingapplication.Model.BaseDataObjects.WalmartAPIData;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class APIListDataTest {
    /**
     * Tests the updateListData function to ensure that if data is added to the list it is
     * added correctly
     */
    @Test
    public void updateListData_listAdded_correct() {
        List<APIData> updateData = new ArrayList<>();

        // add a couple objects to the ListData object
        WalmartAPIData wAPID = new WalmartAPIData();
        updateData.add(wAPID);
        AmazonAPIData aAPID = new AmazonAPIData();
        updateData.add(aAPID);
        TargetAPIData tAPID = new TargetAPIData();
        updateData.add(tAPID);

        APIListData.getInstance().updateListData(updateData);

        List<APIData> result = APIListData.getInstance().getListData();

        assertArrayEquals(updateData.toArray(), result.toArray());

    }

    /*****
     * Tests the reset function. This is just to ensure that when the list needs to be reset that
     * it can be reset.
     */
    @Test
    public void reset_listReset_correct () {
        List<APIData> updateData = new ArrayList<>();

        // add a couple objects to the ListData object
        WalmartAPIData wAPID = new WalmartAPIData();
        updateData.add(wAPID);
        AmazonAPIData aAPID = new AmazonAPIData();
        updateData.add(aAPID);
        TargetAPIData tAPID = new TargetAPIData();
        updateData.add(tAPID);

        APIListData.getInstance().updateListData(updateData);

        APIListData.getInstance().resetData();

        assertEquals("Testing the reset", APIListData.getInstance().getListData().size(), 0);
    }
}