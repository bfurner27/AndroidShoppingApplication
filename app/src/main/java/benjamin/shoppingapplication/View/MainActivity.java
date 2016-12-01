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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
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
        final MainActivity mainActivity = this;       // used to ensure the controller has the necessary
                                                // data


        bsh = new BarcodeScannerHelper();

        // here to ensure that the keys in the property file are available
        APIKeyAccess.getInstance().setPropertyFile(getBaseContext());
        Log.i("MainActivity", "Context: " + getBaseContext().toString());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // code retrieved from:
        // http://www.android4devs.com/2014/12/how-to-make-material-design-app.html
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);


        // pass in all the things that are only accessible from the MainActivity so they can be used
        // elsewhere
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                // the rest happens in the onActivityResult
            }
        });


        Button searchButton = (Button) findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //TODO split this into smaller code fragments
                EditText searchVal = (EditText) findViewById(R.id.search_text);

                if (!searchVal.getText().toString().equals("")) {
                    RequestData rd = new RequestData(null, searchVal.getText().toString(), null);
                    MainController.getInstance().queryAPIs(rd, mainActivity);
                }
            }
        });



    }


    public void updateComparisonList() {
        LinearLayout items = (LinearLayout) findViewById(R.id.items);
        List<APIData> compareItems = APIListData.getInstance().getListData();


        // perform this short sort to ensure the price is in the correct order
        Collections.sort(compareItems, new Comparator<APIData>() {
            @Override
            public int compare(APIData o1, APIData o2) {
                Double first = Double.valueOf(o1.getPrice().replaceAll("\\$", ""));
                Double two = Double.valueOf(o2.getPrice().replaceAll("\\$", ""));
                return first.compareTo(two);
            }
        });

        items.removeAllViewsInLayout();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        int i = 0;
        for (APIData item : compareItems) {
            View item_template = inflater.inflate(R.layout.item_comparison_template, null);

            TextView hiddenURL = (TextView) item_template.findViewById(R.id.hiddenURL);

            if (!item.getProductURL().equals("")) {
                hiddenURL.setText(item.getProductURL());
            }

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

            TextView text = (TextView) item_template.findViewById(R.id.item_text);
            text.setText("Title: " + item.getName() + "\nPrice: " + item.getPrice() + "\nStore: " + item.getStoreName());

            // grab the picture based on the url so it can be displayed
            if (item.getPictureURL() != null) {
                ImageView image = (ImageView) item_template.findViewById(R.id.item_image);
                URLPictureQuery upq = new URLPictureQuery(item.getPictureURL(), image);
                upq.execute(null, null, null);
            }

            if (i % 2 == 1) {
                item_template.setBackgroundColor(getResources().getColor(R.color.white));
            }

            items.addView(item_template);
            i++;
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
            mBarcodes = bsh.validateBarcodes(barcodes);

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
