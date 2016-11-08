package benjamin.shoppingapplication.Model.BaseDataObjects;

import java.util.ArrayList;
import java.util.List;

import benjamin.shoppingapplication.Model.Factory;

/**
 * Created by Benjamin on 11/3/2016.
 *
 * This class is thread safe because it may be required by separate threads
 */

public class APIDataFactory implements Factory {
    // found portions of the singleton code at http://www.javaworld.com/article/2073352/core-java/simply-singleton.html
    private final static APIDataFactory instance = new APIDataFactory();

    private APIDataFactory() {
        // ensure this class is not instantiated anywhere
    }

    public APIDataFactory getInstance() {
        return instance;
    }

    /**
     * generates a list of the items that this factory can create
     * @return a list of strings with the names of the comparison values for creating an object
     */
    @Override
    public List<String> getListObjects() {
        List<String> names = new ArrayList<>();
        names.add("AmazonAPIData");
        names.add("TargetAPIData");
        names.add("WalmartAPIData");
        return names;
    }

    /**
     *
     * @param objectName - the name of the object to create cased ignored
     *                   {amazonapidata, targetapidata, walmartapidata}
     * @return null if nothing was created based on the string input, object otherwise
     */
    @Override
    public APIData createObject(String objectName) {
        APIData apiData = null;

        if (objectName.equalsIgnoreCase("AmazonAPIData")) {
            apiData = new AmazonAPIData();
        } else if (objectName.equalsIgnoreCase("TargetAPIData")) {
            apiData = new TargetAPIData();
        } else if (objectName.equalsIgnoreCase("WalmartAPIData")) {
            apiData = new WalmartAPIData();
        }

        return apiData;
    }
}
