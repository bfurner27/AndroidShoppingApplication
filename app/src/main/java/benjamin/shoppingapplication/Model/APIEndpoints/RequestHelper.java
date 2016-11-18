package benjamin.shoppingapplication.Model.APIEndpoints;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Benjamin on 11/17/2016.
 */

public class RequestHelper {

    /***
     * This is to help in generating the URL requests. I made this after I realized I was
     * duplicating my code over and over again for the request.
     * @param requestURL - the url of the request to make
     * @return
     */
    public String generateAPIResults (String requestURL) {
        String result = null;

        try {
            // build the url for the request
            URL url = new URL(requestURL);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

            try {
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();

                // Read in the entire request information and parse it into a string
                String line = "";
                // if the line returns null then it is the end of the input
                while ((line = bufferedReader.readLine()) != null) {
                    // append the line with a newline at the end to ensure it maintains
                    // the json format
                    stringBuilder.append(line).append("\n");
                }

                result = stringBuilder.toString();

            } finally {
                httpURLConnection.disconnect();
            }

        } catch (MalformedURLException e) {
            //TODO remove the stack trace once satisfied it is working
            e.printStackTrace();
            Log.e("RequestHelper", "Failed to create the URL as expected");
        } catch (IOException e) {
            //TODO remove the stack trace once satisfied it is working
            e.printStackTrace();
            Log.e("RequestHelper", "Failed to send the url through the connection and the file failed");
        }

        return result;
    }
}
