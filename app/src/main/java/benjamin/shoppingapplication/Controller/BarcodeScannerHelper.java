package benjamin.shoppingapplication.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Benjamin on 11/28/2016.
 * This is created as a basic helper to the barcode scanner. It will carry all the other operations
 * that don't need to be carried out on the main thread
 */

public class BarcodeScannerHelper extends AppCompatActivity {
    /**
     * Creates a picture file on the device that can then be
     * sent to the photo app for when the picture is taken
     *
     * @param  externalFileDir - getExternalFilesDir(Environment.DIRECTORY_PICTURES) main activity
     * @return - returns a file reference
     * @throws IOException
     */
    public File createPictureFile(File externalFileDir) throws IOException {
        File imageFile;
        String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String imageFileName = "barcode_" + timestamp;

        imageFile = File.createTempFile(imageFileName, ".jpg", externalFileDir);

        return imageFile;
    }


    /**
     * Reads the barcode from the picture and returns the array of Barcode's
     *
     * @param barcodeBitmap - The bitmap of the picture to be decoded
     * @param applicationContext - getApplicationContext() - main activity
     * @return - an array of barcode's
     */
    public SparseArray<Barcode> readBarcode(Bitmap barcodeBitmap, Context applicationContext) {
        BarcodeDetector detector = new BarcodeDetector.Builder(applicationContext)
                .setBarcodeFormats(Barcode.PRODUCT | Barcode.UPC_E | Barcode.UPC_A)
                .build();

        if (!detector.isOperational()) {
            Log.e("DETECTOR", "The app needs to be refreshed");
            return null;
        }

        Frame frame = new Frame.Builder().setBitmap(barcodeBitmap).build();
        SparseArray<Barcode> barcodes = detector.detect(frame);

        return barcodes;
    }


    /**
     * Finds the valid barcode's and return them in an array list
     *
     * @param barcodes - the list of all recognized barcode's
     * @return - a list of valid barcode's
     */
    public List<Barcode> validateBarcodes(SparseArray<Barcode> barcodes) {
        Log.i("VALIDATEBARCODES:", " NumBarcodes: " + barcodes.size());
        List<Barcode> barcodeValues = new ArrayList<>();
        for (int i = 0; i < barcodes.size(); i++) {
            if (validateBarcode(barcodes.valueAt(i))) {
                Log.i("VALIDATEBARCODES", "Valid Barcode: " + barcodes.valueAt(i).rawValue);
                barcodeValues.add(barcodes.valueAt(i));
            }
        }

        return barcodeValues;
    }



    /**
     * Checks the barcode to ensure that it is a correct value
     *
     * @param barcode - the barcode to be checked
     * @return - true if the barcode is valid false otherwise
     */
    private boolean validateBarcode(Barcode barcode) {
        boolean isValid = true;
        String sBarcode = barcode.rawValue;


        // sum odd and even values and multiply the odds by 3
        Integer sumOdd = 0;
        Integer sumEven = 0;
        String check = "";
        for (int i = 0; i < sBarcode.length() - 1; i++) {
            check += sBarcode.charAt(i);
            if (i % 2 == 0) {
                sumEven += Character.getNumericValue(sBarcode.charAt(i));
            } else {
                sumOdd += Character.getNumericValue(sBarcode.charAt(i));
            }
        }
        Log.i("CHECKBARCODE", "check: " + check);
        sumOdd = sumOdd * 3;

        // sum the even and odd values together
        Integer total = sumOdd + sumEven;
        total = total % 10;
        total = 10 - total;

        Integer checkDigit = Character.getNumericValue(sBarcode.charAt(sBarcode.length() - 2));
        Log.i("CHECKBARCODE", "Check Digit: " + checkDigit.toString());

        // compare against the checksum
        if (total.equals(checkDigit)) {
            if (total.equals(10)) {
                isValid = false;
            }
        }

        return isValid;
    }


    /**
     * deletePictureFile, does not currently seem to be deleting the picture on the public space
     * @throws IOException
     */
    public void deletePictureFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.delete()) {
                Log.i("PhotoDelete", "Deleted the photo");
            } else {
                Log.e("PHOTO DELETE", "Failed to delete the photo");
            }
        } else {
            Log.e("PHOTO DELETE", "The photo does not exist");
        }
    }

}
