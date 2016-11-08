package benjamin.shoppingapplication.Model.StoreProcesses;

import java.util.ArrayList;
import java.util.List;

import benjamin.shoppingapplication.Model.Factory;

/**
 * Created by Benjamin on 11/3/2016.
 */

public class StoreProcessFactory implements Factory {
   private static final StoreProcessFactory instance = new StoreProcessFactory();


    private StoreProcessFactory () {
        // ensure no instantiations of this object are made exept through get instance
    }


    /**
     * Singleton pattern to ensure there is only one instance of this class
     * @return - returns the reference to this class
     */
    public static final StoreProcessFactory getInstance() {
        return instance;
    }

    /**
     * This will return a list of the objects
     *
     * @return returns a list of strings for the object
     */
    @Override
    public List<String> getListObjects() {
        List<String> names = new ArrayList<>();
        names.add("AmazonProcess");
        names.add("TargetProcess");
        names.add("WalmartProcess");
        return names;
    }

    /**
     * Will create the object for the specified factory
     *
     * @param objectName - the name of the object to create
     * @return returns - a reference to the object that was created, if null it failed to create an
     *                   instance of an object
     */
    @Override
    public StoreProcess createObject(String objectName) {
        StoreProcess sP = null;

        if (objectName.equalsIgnoreCase("AmazonProcess")) {
            sP = new AmazonProcess();
        } else if (objectName.equalsIgnoreCase("TargetProcess")) {
            sP = new TargetProcess();
        } else if (objectName.equalsIgnoreCase("WalmartProcess")) {
            sP = new WalmartProcess();
        }

        return sP;
    }
}
