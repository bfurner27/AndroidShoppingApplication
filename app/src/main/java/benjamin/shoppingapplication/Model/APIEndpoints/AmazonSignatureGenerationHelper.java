package benjamin.shoppingapplication.Model.APIEndpoints;

import android.util.Log;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import benjamin.shoppingapplication.Controller.APIKeyAccess;

/**
 * Created by Benjamin on 11/15/2016.
 * Source Code Retrieved from:
 * http://docs.aws.amazon.com/AWSECommerceService/latest/DG/AuthJavaSampleSig2.html
 * some minor modifications were made by me to make sure that it conformed to what I needed
 */

public class AmazonSignatureGenerationHelper {
    //TODO this does not work for all the requests so change it to the sample code that amazon provides

    private static final String UTF8_CHARSET = "UTF-8";
    private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
    private static final String REQUEST_URI = "/onca/xml";
    private static final String REQUEST_METHOD = "GET";

    private String endpoint = "webservices.amazon.com"; // must be lowercase
    private String awsAccessKeyId = APIKeyAccess.getInstance().getAPIKey("amazon_id");
    private String associateId = APIKeyAccess.getInstance().getAPIKey("amazon_associate_id");
    private String awsSecretKey = APIKeyAccess.getInstance().getAPIKey("amazon_private_key");

    private SecretKeySpec secretKeySpec = null;
    private Mac mac = null;

    public AmazonSignatureGenerationHelper() {
        try {
            byte[] secretyKeyBytes = awsSecretKey.getBytes(UTF8_CHARSET);
            secretKeySpec =
                    new SecretKeySpec(secretyKeyBytes, HMAC_SHA256_ALGORITHM);
            mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
            mac.init(secretKeySpec);
        } catch (UnsupportedEncodingException e) {
            Log.e("ASGH", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("ASGH", e.getMessage());
        } catch (InvalidKeyException e) {
            Log.e("ASGH", e.getMessage());
        }
    }

    public String generateSignature(String paramList) {
        String[] params = paramList.split("&");

        Map<String, String> paramMap = new TreeMap<>();

        for (int i = 0; i < params.length; i++) {
            String[] keyValue = params[i].split("=");
            if (keyValue.length > 1) {
                paramMap.put(keyValue[0], keyValue[1]);
            } else {
                throw new IndexOutOfBoundsException("The param list was incorrect, " +
                        "it must be in the form of key=value&");
            }
        }

        String result = sign(paramMap);
        Log.i("ASGH", "Results of the sign: " + result);

        return result;
    }

    public String sign(Map<String, String> params) {
        params.put("AWSAccessKeyId", awsAccessKeyId);
        params.put("AssociateTag", associateId);
        params.put("Timestamp", timestamp());

        SortedMap<String, String> sortedParamMap =
                new TreeMap<String, String>(params);
        String canonicalQS = canonicalize(sortedParamMap);
        String toSign =
                REQUEST_METHOD + "\n"
                        + endpoint + "\n"
                        + REQUEST_URI + "\n"
                        + canonicalQS;

        String hmac = hmac(toSign);
        String sig = percentEncodeRfc3986(hmac);
        String url = "http://" + endpoint + REQUEST_URI + "?" +
                canonicalQS + "&Signature=" + sig;

        return url;
    }

    private String hmac(String stringToSign) {
        String signature = null;
        byte[] data;
        byte[] rawHmac;
        try {
            data = stringToSign.getBytes(UTF8_CHARSET);
            rawHmac = mac.doFinal(data);
            Base64 encoder = new Base64();
            signature = new String(encoder.encode(rawHmac));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(UTF8_CHARSET + " is unsupported!", e);
        }
        return signature;
    }

    private String timestamp() {
        String timestamp = null;
        Calendar cal = Calendar.getInstance();
        DateFormat dfm = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dfm.setTimeZone(TimeZone.getTimeZone("GMT"));
        timestamp = dfm.format(cal.getTime());
        return timestamp;
    }

    private String canonicalize(SortedMap<String, String> sortedParamMap) {
        if (sortedParamMap.isEmpty()) {
            return "";
        }

        StringBuffer buffer = new StringBuffer();
        Iterator<Map.Entry<String, String>> iter =
                sortedParamMap.entrySet().iterator();

        while (iter.hasNext()) {
            Map.Entry<String, String> kvpair = iter.next();
            buffer.append(percentEncodeRfc3986(kvpair.getKey()));
            buffer.append("=");
            buffer.append(percentEncodeRfc3986(kvpair.getValue()));
            if (iter.hasNext()) {
                buffer.append("&");
            }
        }
        String canonical = buffer.toString();
        return canonical;
    }

    private String percentEncodeRfc3986(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, UTF8_CHARSET)
                    .replace("+", "%20")
                    .replace("*", "%2A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }

}
