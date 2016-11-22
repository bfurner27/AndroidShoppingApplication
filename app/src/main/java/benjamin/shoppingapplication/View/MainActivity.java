package benjamin.shoppingapplication.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.Properties;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Controller.MainController;
import benjamin.shoppingapplication.Model.APIEndpoints.AmazonSearch;
import benjamin.shoppingapplication.Model.RequestData;
import benjamin.shoppingapplication.R;

public class MainActivity extends AppCompatActivity {

    /**
     * constructor/start for the entire program
     * @param savedInstanceState - //TODO not sure exactly yet what this is
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // here to ensure that the keys in the property file are available
        APIKeyAccess.getInstance().setPropertyFile(getBaseContext());
        Log.i("MainActivity", "Context: " + getBaseContext().toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO flush out the interface and remove the following line of code, this is just to get things rolling
        RequestData requestData = new RequestData("035000521019"); // adds in the upc number
        MainController.getInstance().queryAPIs(requestData);
    }


    private void createSearchBox() {

    }
}
