package spicy.alt;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import net.minecraft.util.Session;

import java.net.Proxy;
import java.util.ArrayList;

public class AltManager
{
    private ArrayList<Alt> alts;
    private Alt lastAlt;
    
    public AltManager() {
        this.alts = new ArrayList<Alt>();
    }
    
    public ArrayList<Alt> getAlts() {
        return this.alts;
    }
    
    public void filterEmails() {
        final ArrayList<Alt> tempList = new ArrayList<Alt>();
        for (final Alt alt : this.alts) {
            if (alt.getDisplay().contains("@")) {
                tempList.add(new Alt(this.resolveName(alt), alt.getUsername(), alt.getPassword()));
            }
            else {
                tempList.add(alt);
            }
        }
        this.alts = tempList;
    }
    
    public String resolveName(final Alt alt) {
        final Session session = this.createSession(alt.getUsername(), alt.getPassword());
        if (session != null) {
            return session.getUsername();
        }
        return "invalid";
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
    
    public void setLastAlt(final Alt lastAlt) {
        this.lastAlt = lastAlt;
    }
    
    public Alt getLastAlt() {
        return this.lastAlt;
    }
}
