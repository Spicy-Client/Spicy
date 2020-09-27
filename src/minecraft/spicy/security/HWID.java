package spicy.security;

import net.minecraft.client.Minecraft;
import spicy.gui.hwid.GuiUnknownHWID;
import spicy.main.Spicy;
import spicy.user.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HWID {

    public static boolean ok=false;
    public static String displayRealName;
    public static void CheckHWIDfromWebsite() {

        try {
            String asd = readContentFromUrl(new URL("https://spicy.wtf/hwid"));
            String[] splitted = asd.split(":"); // by
             for(int i=0; i<splitted.length; i++){
                if (splitted[i].contains(Spicy.INSTANCE.user.getHWID())) {
                     ok = true;
                    Spicy.INSTANCE.user.setRank(User.RankGroup.valueOf(splitted[1+i]));
                    Spicy.INSTANCE.user.setName(splitted[2+i]);
                    Spicy.INSTANCE.user.setDisplayName(splitted[3+i]);
                    Minecraft.getMinecraft().session = new net.minecraft.util.Session(Spicy.INSTANCE.user.getName(), "", "", "mojang");
                    displayRealName=Spicy.INSTANCE.user.getDisplayName().substring(0,splitted[3+i].length());
                }
             }
            if (!ok) {
                Minecraft.getMinecraft().displayGuiScreen(new GuiUnknownHWID());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Minecraft.getMinecraft().displayGuiScreen(new GuiUnknownHWID());
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
