package benjamin.shoppingapplication.Model;

import java.util.List;

/**
 * Created by Benjamin on 11/3/2016.
 */

public interface Factory<T> {

    /**
     * This will return a list of the objects
     * @return returns a list of strings for the object
     */
    public List<String> getListObjects();

    /**
     * Will create the object for the specified factory
     * @param objectName - the name of the object to create
     * @return returns a reference to the object that was created
     */
    public T createObject(String objectName);
}
