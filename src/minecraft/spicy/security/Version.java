package spicy.security;

import spicy.main.Spicy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Version {

    public static boolean ok=false;
    public static void CheckVersionFromWebsite() {

        try {
            String asd = readContentFromUrl(new URL("https://spicy.wtf/version"));
                if (asd.contains(Spicy.INSTANCE.VERSION)) {
                    ok = true;
                } else {
                    ok = false;
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String readContentFromUrl(URL url) throws Exception {
        String line;
        StringBuilder content = new StringBuilder();
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "NING/1.0");
        urlConnection.setConnectTimeout(7500);
        urlConnection.setReadTimeout(7500);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        while ((line = bufferedReader.readLine()) != null) {
            content.append(line + "\n");
        }
        bufferedReader.close();
        return content.toString();
    }
}
