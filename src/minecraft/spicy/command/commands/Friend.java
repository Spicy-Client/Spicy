package spicy.command.commands;


import spicy.command.Command;
import spicy.friend.FriendManager;
import spicy.main.Wrapper;
import spicy.notification.NotificationPublisher;
import spicy.notification.NotificationType;

public class Friend extends Command
{
    @Override
    public String getNames() {
        return "friend";
    }
    
    @Override
    public String getDesc() {
        return "Add/remove friends.";
    }
    
    @Override
    public String getError() {
        return "$friend add/remove (name)";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        final String s = args[0];
        switch (s) {
            case "add": {
                FriendManager.addFriend(args[1]);
                Wrapper.addChatMessage(args[1] + " added friend.");
                NotificationPublisher.queue("Friend",  args[1] + " is your friend now", NotificationType.WARNING);
                break;
            }
            case "del":
            case "remove": {
                FriendManager.removeFriend(args[1]);
                Wrapper.addChatMessage(args[1] + " removed friend.");
                NotificationPublisher.queue("Friend",  args[1] + " is removed your friend list", NotificationType.WARNING);
                break;
            }
            default: {
                Wrapper.addChatMessage(this.getError());
                break;
            }
        }
    }
}
