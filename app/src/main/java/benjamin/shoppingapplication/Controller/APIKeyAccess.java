package benjamin.shoppingapplication.Controller;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by Benjamin on 11/7/2016.
 */
public class APIKeyAccess {
    private static APIKeyAccess ourInstance = new APIKeyAccess();
    private Properties keyProperties = null;

    public static APIKeyAccess getInstance() {
        return ourInstance;
    }

    private APIKeyAccess() {
    }

    /**
     * The main function that will return a key based on the property given
     */
    public String getAPIKey(String key) {
        if (keyProperties == null) {
            Log.e("APIKeyAccess", "The resource has not been loaded previously call the " +
                    "setPropertyFile() first");
            return null;
        }

        return keyProperties.getProperty(key);
    }

    public void setPropertyFile(Context context) {
        loadPropertyFile(context);
    }

    /**
     *
     * @param context - the context of the base in memory
     */
    private void loadPropertyFile (Context context) {
        keyProperties = new Properties();
        try {
            keyProperties.load(context.getAssets().open("keys.properties"));
        } catch (IOException e) {
            Log.e("APIKeyAccess", "Failed to open the properties resource specified");
            keyProperties = null;
        }
    }

}
