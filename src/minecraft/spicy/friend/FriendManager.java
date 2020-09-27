package spicy.friend;

import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import spicy.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FriendManager
{
    private static final File FRIEND_DIR;
    public static ArrayList<Friend> friendsList;
    
    static {
        FRIEND_DIR = FileUtils.getConfigFile("friends");
        FriendManager.friendsList = new ArrayList<Friend>();
    }
    
    public static void start() {
        load();
        save();
    }
    
    public static void addFriend(final String name) {
        FriendManager.friendsList.add(new Friend(name));
        save();
    }
    
    public static String getAliasName(final String name) {
        String alias = "";
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                alias = friend.name;
                break;
            }
        }
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
            return name;
        }
        return alias;
    }
    
    public static void removeFriend(final String name) {
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(name)) {
                FriendManager.friendsList.remove(friend);
                break;
            }
        }
        save();
    }
    
    public static String replaceText(String text) {
        for (final Friend friend : FriendManager.friendsList) {
            if (text.contains(friend.name)) {
                text = friend.name;
            }
        }
        return text;
    }
    
    public static boolean isFriend(final String name) {
        boolean isFriend = false;
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                isFriend = true;
                break;
            }
        }
        if (Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
            isFriend = true;
        }
        return isFriend;
    }
    
    public static void load() {
        FriendManager.friendsList.clear();
        final List<String> fileContent = FileUtils.read(FriendManager.FRIEND_DIR);
        for (final String line : fileContent) {
            try {
                final String[] split = line.split(":");
                final String name = split[0];
                final String alias = split[1];
                addFriend(name);
            }
            catch (Exception ex) {}
        }
    }
    
    public static void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Friend friend : FriendManager.friendsList) {
            final String alias = getAliasName(friend.name);
            fileContent.add(String.format("%s:%s", friend.name, alias));
        }
        FileUtils.write(FriendManager.FRIEND_DIR, fileContent, true);
    }
}
