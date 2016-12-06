package benjamin.shoppingapplication.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Controller.BarcodeScannerHelper;
import benjamin.shoppingapplication.Controller.MainController;
import benjamin.shoppingapplication.Controller.TextCommandController;
import benjamin.shoppingapplication.Model.APIListData;
import benjamin.shoppingapplication.Model.BaseDataObjects.APIData;
import benjamin.shoppingapplication.Model.RequestData;
import benjamin.shoppingapplication.R;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private BarcodeScannerHelper bsh;
    private List<Barcode> mBarcodes;
    private APIData mTempData;

    /**
     * constructor/start for the entire program
     * @param savedInstanceState - //TODO not sure exactly yet what this is
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // initialize variables that will be used later in the program
        final MainActivity mainActivity = this; // reference to this object
        bsh = new BarcodeScannerHelper();

        // here to ensure that the keys in the property file are available
        APIKeyAccess.getInstance().setPropertyFile(getBaseContext());

        // TODO set the focus to another element besides the text box
        // TODO ensure that the search button is positioned to not take up as much room perhaps an arrow to the side of the text box would be adequate


        // sets up the main activity and puts it in the main threads content view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        mainLayout.clearFocus();

        // code retrieved from:
        // http://www.android4devs.com/2014/12/how-to-make-material-design-app.html
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // set the listeners for the buttons
        setBarcodeScannerButtonOnClickListener();
        setSearchButtonOnClickListener(mainActivity);
    }

    /**
     * Sets the barcode scanners on click listener, this will require that there is code accessible
     * by the main activity so everything works correctly
     */
    public void setBarcodeScannerButtonOnClickListener() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                // the rest happens in the onActivityResult
            }
        });
    }


    /***
     * sets up the search buttons onClick listener so that items can be searched
     * @param mainActivity - this is a reference to the this object which needs to be accessed
     *                     from inside the listener, this is why it is final
     */
    public void setSearchButtonOnClickListener(final MainActivity mainActivity) {
        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText searchVal = (EditText) findViewById(R.id.search_text);

                /*********
                 * This code will hide the keyboard view from the user after they have clicked on
                 * the search button
                 * retrieved code from:
                 * http://stackoverflow.com/questions/2342620/how-to-hide-keyboard-after-typing-in-
                 * edittext-in-android*/
                InputMethodManager inputManager =
                        (InputMethodManager) mainActivity.
                                getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        mainActivity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                /***********/

                if (!searchVal.getText().toString().equals("")) {
                    String searchString = searchVal.getText().toString();
                    String command = TextCommandController
                            .getInstance()
                            .decideAction(searchString);

                    if (command.equals("camera")) {
                        dispatchTakePictureIntent();
                    } else {
                        if (command.equals("search")) {
                            searchString = TextCommandController.getInstance().generateNewSearchValue();
                        }

                        RequestData rd = new RequestData(null, searchString, null);
                        MainController.getInstance().queryAPIs(rd, mainActivity);
                    }
                }
            }
        });
    }


    /***
     * Acts as the listener for the APIEndpoints, once they have completed the requests and, parsed
     * the data they will call this function
     */
    public void updateComparisonList() {
        // makes the error message visible if there was an error in the parsing
        errorMessageVisibilityToggle();

        // reset the view so that it is clear for when cards are added later
        LinearLayout items = (LinearLayout) findViewById(R.id.items);
        items.removeAllViewsInLayout();
        
        List<APIData> compareItems = APIListData.getInstance().getListData();
        sortItemListByPrice(compareItems);
        buildItemCards(items, compareItems);
        items.requestFocus();

    }

    /*****
     * Sorts the list by price to ensure that the lowest priced items are at the top of the display
     * This also ensures that the lowest priced items are also not dependant on the store
     * @param itemsList - the items to be sorted
     */
    private void sortItemListByPrice (List<APIData> itemsList) {
        // perform this short sort to ensure the price is in the correct order
        Collections.sort(itemsList, new Comparator<APIData>() {
            @Override
            public int compare(APIData o1, APIData o2) {
                Double first = Double.valueOf(o1.getPrice().replaceAll("\\$", ""));
                Double two = Double.valueOf(o2.getPrice().replaceAll("\\$", ""));
                return first.compareTo(two);
            }
        });
    }


    /****
     * Builds the cards that will be inserted into the container, it needs the data so it can insert
     * the data accordingly
     * @param container - the container to store all the cards in
     * @param compareItems - the data that will be used to populate the added cards
     */
    private void buildItemCards (LinearLayout container, List<APIData> compareItems) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int i = 0;
        for (APIData item : compareItems) {
            View item_template = inflater.inflate(R.layout.item_comparison_template, null);


            if (!item.getProductURL().equals("")) {
                enableItemsOnClickLister(item_template, item);
            }

            // builds the text view to have the desired content information
            TextView text = (TextView) item_template.findViewById(R.id.item_text);
            text.setText("Title: " + item.getName() + "\nPrice: " + item.getPrice() + "\nStore: "
                    + item.getStoreName());

            // grab the picture based on the url so it can be displayed
            if (item.getPictureURL() != null) {
                ImageView image = (ImageView) item_template.findViewById(R.id.item_image);
                URLPictureQuery upq = new URLPictureQuery(item.getPictureURL(), image);
                upq.execute(null, null, null);
            }

            if (i % 2 == 1) {
                item_template.setBackgroundColor(getResources().getColor(R.color.white));
            }

            container.addView(item_template);
            i++;
        }
    }


    /*****
     * This will enable the on click listener for the view based on if the url is available or not
     * @param item_template - the template main view that needs to have the listener set
     * @param item - the data with the url that will be input into the hidden field so the lister
     *               will be able to access it
     */
    private void enableItemsOnClickLister (View item_template, APIData item) {
        // this will be used so when the listener is making the url request it will have access
        // to the correct url
        TextView hiddenURL = (TextView) item_template.findViewById(R.id.hiddenURL);
        hiddenURL.setText(item.getProductURL());

        // sets up the listener - this listener will use an intent to open up a browser with the url
        item_template.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView hiddenView = (TextView) v.findViewById(R.id.hiddenURL);
                String url = hiddenView.getText().toString();
                if (!url.equals("")) {
                    Log.i("MainActivity", "URL: " + url);
                    Uri uri = Uri.parse(url);
                    Intent openURL = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(openURL);
                }
            }
        });
    }

    /*****
     * This function will check to see if the error flag has been set to true and will make visible
     * the error message
     */
    private void errorMessageVisibilityToggle() {
        TextView errorMessage = (TextView) findViewById(R.id.error_message);
        if (APIListData.getInstance().getError()) {
            errorMessage.setVisibility(errorMessage.VISIBLE);
        } else {
            errorMessage.setVisibility(errorMessage.GONE);
        }
    }





    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String mCurrentPhotoPath;
    private Uri photoURI;

    /**
     * This function will dispatch the event that will start up the camera app to take the picture
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // create the new intent and ensure that the intent returns something
        Log.i("MainActivity", getPackageManager().toString());
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photo = null;

            // creates the photo file on the system
            try {
                photo = bsh.createPictureFile(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
                mCurrentPhotoPath = photo.getAbsolutePath();
                Log.i("Before Activity", "Created a new picture file: " + photo.getAbsolutePath());
            } catch (IOException e) {
                Log.e("PICTURE FILE FAILURE", "The picture file was not created");
                System.exit(-1);
            }


            // Ensure the photo is not null and then create the Intent for the camera
            if (photo != null) {
                // puts the photo into a photo Uri which will be used for the intent
                photoURI = FileProvider.getUriForFile(this,
                        "benjamin.shoppingapplication.android.fileprovider",
                        photo);
                Log.i("Before Activity", photoURI.toString());
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED && requestCode == REQUEST_IMAGE_CAPTURE) {

            Log.i("PHOTO", "Print Stack Trace: " + mCurrentPhotoPath);
            Log.i("PHOTO", "Photo URI: " + photoURI.toString());
            Log.i("PHOTO", "Photo URI Data: " + data);

            File imageFile = new File(photoURI.toString());


            //ImageView myImageView = (ImageView) findViewById(R.id.imageview);
            Bitmap photoBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
            //myImageView.setImageBitmap(photoBitmap);

            SparseArray<Barcode> barcodes = bsh.readBarcode(photoBitmap, getApplicationContext());

            // ensure that the result of read barcode is not null
            if (barcodes != null) {
                mBarcodes = bsh.validateBarcodes(barcodes);
            }

            Log.i("MainActivity", "Number of valid barcodes: " + mBarcodes.size());

            //TODO determine that this is the correct place for this hookin
            for (int i = 0; i < mBarcodes.size(); i++) {
                Log.i("MainActivity", "Calling the main controller!");
                RequestData rd = new RequestData(mBarcodes.get(i).rawValue);

                MainController.getInstance().queryAPIs(rd, this);
            }

            try {
                //wait(4000);
                bsh.deletePictureFile(mCurrentPhotoPath);
                Log.i("Delete Picture", "DELETED");
            } catch (Exception e)
            {
                Log.e("IMAGE ERROR", "An error occured with the image");
                e.printStackTrace();
            }

        }
    }
}
