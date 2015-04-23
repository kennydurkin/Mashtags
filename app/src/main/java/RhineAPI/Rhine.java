package RhineAPI;

import java.util.List;
import java.net.URLConnection;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.DataOutputStream;

public class Rhine {
    public Rhine(String apikey) {
        this.url = "http://api.rhine.io/";
        this.apikey = apikey;
    }

    public String run (String request) {
        String k = request.substring(request.indexOf("\"") + 1, request.indexOf(":") - 1);
        //System.out.println(k);
        request = "{\"request\": {\"method\": " + request + ", \"key\": \"" + this.apikey + "\"}}";
        //System.out.println(request);
        HttpURLConnection conn = null;
        try {
            URL url = new URL(this.url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            DataOutputStream d = new DataOutputStream(conn.getOutputStream());
            d.writeBytes(request);
            d.flush();
            d.close();

            InputStream i = conn.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(i));
            return r.readLine();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        finally {
            if (conn != null) conn.disconnect();
        }
    }

    private String apikey;
    private String url;

    public static final String entity (String e) { return "{\"entity\": \"" + e + "\"}"; }
    public static final String article (String url) { return "{\"article\": \"fromurl\": \"" + url + "\"}"; }
    public static final String image (String url) { return "{\"image\":{\"fromurl\": \"" + url + "\"}}"; }
    public static final String text (String t) { return "{\"text\": \"" + t + "\"}"; }

    public static final String distance (String x, String y) { return "{\"distance\": [" + x + "," + y + "]}"; }
    public static final String extraction (String x) { return "{\"extraction\": " + x + "}"; }
    public static final String equivalence (String x, String y) { return "{\"equivalance\": [" + x + "," + y + "]}"; }
    public static final String subclass (String x, String y) { return "{\"subclass\": [" + x + "," + y + "]}"; }
    public static final String clustering (List<String> x) { String b = "{\"clustering\": ["; for (String e : x) b += e + ","; if (x.size() > 1) b = b.substring(0, b.length() - 1); return b + "]}"; }

    public static void main(String[] args) {
        Rhine x = new Rhine("NEKEMBIPUXPJYZCZIMAXXZJVK");
        System.out.println(x.run(extraction(image("http://i.imgur.com/jaVav95.jpg"))));
        //System.out.println(x.run(subclass(entity("Mango"), entity("Tasty"))));
    }
}
