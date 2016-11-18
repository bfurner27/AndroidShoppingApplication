package benjamin.shoppingapplication.Model.APIEndpoints;

import android.content.ContextWrapper;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import benjamin.shoppingapplication.Controller.APIKeyAccess;
import benjamin.shoppingapplication.Model.RequestData;
import benjamin.shoppingapplication.View.MainActivity;

/**
 * Created by Benjamin on 11/9/2016.
 */

public class AmazonSearchTest {
    @Test
    public void test_paramsortAscii() {
        String [] sortArray = { "benjamin=sad83918234", "Samuel=funness", "Benjamin=happiness",  "samuel=goodness" };
        String [] compare = { "Benjamin=happiness", "Samuel=funness", "benjamin=sad83918234", "samuel=goodness" };
        Arrays.sort(sortArray);
        for (int i = 0; i < compare.length; i++) {
            Assert.assertEquals(compare[i], sortArray[i]);
        }


        String [] sortArray1 = { "A", "a", "B",  "b", "c", "D", "F" };
        String [] compare1 = { "A", "B", "D", "F", "a", "b", "c"  };
        Arrays.sort(sortArray1);
        for (int i = 0; i < compare.length; i++) {
            Assert.assertEquals(compare1[i], sortArray1[i]);
        }
    }
}
