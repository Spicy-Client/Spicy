package spicy.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import spicy.main.Spicy;
import spicy.utils.ChatColor;

import java.net.Proxy;

public class LoginThread extends Thread
{
    private final Minecraft mc;
    private String status;
    private final String username;
    private final String password;
    
    public LoginThread(final String username, final String password) {
        super("Login Thread");
        this.mc = Minecraft.getMinecraft();
        this.username = username;
        this.password = password;
        this.status = "Idle...";
    }
    
    private final Session createSession(final String username, final String password) {
        final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public String getStatus() {
        return this.status;
    }
    
    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = ChatColor.GREEN + "Logged in. (" + this.username + " - offline name)";
            return;
        }
        this.status = ChatColor.YELLOW + "Logging in...";
        final Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
            this.status = ChatColor.RED + "Login failed!";
        }
        else {
            Spicy.INSTANCE.altManager.setLastAlt(new Alt(this.username, this.password));
            this.status = ChatColor.GREEN + "Logged in. (" + auth.getUsername() + ")";
            this.mc.session = auth;
        }
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
}
