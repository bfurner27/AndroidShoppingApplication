package benjamin.shoppingapplication.View;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Benjamin on 11/29/2016.
 */

public class URLPictureQuery extends AsyncTask<Void, Void, Bitmap> {
    private String url;
    private ImageView imageView;

    public URLPictureQuery(String url, ImageView picture) {
        this.url = url;
        this.imageView = picture;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        Log.i("URLPictureQuery", "Background process");
        Bitmap responsePicture = null;
        try {
            // code from:
            // http://stackoverflow.com/questions/6407324/how-to-get-image-from-url-in-android

            responsePicture = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responsePicture;
    }

    @Override
    protected void onCancelled() {
        Log.e("URLPictureQuery", "The picture failed to load");
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            Log.e("URLPictureQuery", "Failed to load the resource");
        }
    }
}
