package spicy.alt;

public class Alt
{
    private String display;
    private String username;
    private String password;
    private String resolvedUsername;
    
    public Alt(final String username, final String password) {
        this.display = username;
        this.username = username;
        this.password = password;
    }
    
    public Alt(final String display, final String username, final String password) {
        this.display = display;
        this.username = username;
        this.password = password;
    }
    
    public String getDisplay() {
        return this.display;
    }
    
    public String getUsername() {
        return this.username;
    }
    
    public String getPassword() {
        return this.password;
    }
}
