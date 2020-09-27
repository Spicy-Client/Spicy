package spicy.user;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class User {
    /**
     * The spicy user real name.
     */
    private String name;

    /**
     * The spicy user display name.
     */
    private String displayName;

    /**
     * The spicy user rank.
     */
    private RankGroup rank;

    /**
     * The spicy user wings.
     */
    private boolean isHaveWings;

    /**
     * The spicy user crown.
     */
    private boolean isHaveCrown;

    /**
     * The spicy user halo.
     */
    private boolean isHaveHalo;

    /**
     * The spicy user headset.
     */
    private boolean isHaveHeadset;


    /**
     * The spicy user ears.
     */
    private boolean isHaveEars;



    public List<String> userList = new ArrayList<String>();


    /**
     * The spicy user cosmetics.
     * @param isHaveWings
     * @param isHaveCrown
     * @param isHaveHalo
     * @param isHaveHeadset
     */
    public void cosmetic(boolean isHaveWings, boolean isHaveCrown, boolean isHaveHalo, boolean isHaveHeadset, boolean isHaveEars) {
        this.isHaveWings = isHaveWings;
        this.isHaveCrown = isHaveCrown;
        this.isHaveHalo = isHaveHalo;
        this.isHaveHeadset = isHaveHeadset;
        this.isHaveEars = isHaveEars;
    }


    public boolean isHaveEars() {
        return isHaveEars;
    }

    public void setHaveEars(boolean haveEars) {
        isHaveEars = haveEars;
    }

    public boolean isHaveWings() {
        return isHaveWings;
    }

    public void setHaveWings(boolean haveWings) {
        isHaveWings = haveWings;
    }

    public boolean isHaveCrown() {
        return isHaveCrown;
    }

    public void setHaveCrown(boolean haveCrown) {
        isHaveCrown = haveCrown;
    }

    public boolean isHaveHalo() {
        return isHaveHalo;
    }

    public void setHaveHalo(boolean haveHalo) {
        isHaveHalo = haveHalo;
    }

    public boolean isHaveHeadset() {
        return isHaveHeadset;
    }

    public void setHaveHeadset(boolean haveHeadset) {
        isHaveHeadset = haveHeadset;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public RankGroup getRank() {
        return rank;
    }

    public void setRank(RankGroup rank) {
        this.rank = rank;
    }

    public String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "-";
            }
            ++i;
        }
        return s;
    }

    public enum RankGroup {
        User("User"),
        Vip("VIP"),
        Staff("Staff");

        /**
         * The spicy user rank.
         */
        private String rank;

        private RankGroup(String rank) {
           this.rank = rank;
        }
    }
}
