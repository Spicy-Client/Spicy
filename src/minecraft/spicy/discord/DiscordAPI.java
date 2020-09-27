package spicy.discord;

public class DiscordAPI {

	private static final DiscordAPI INSTANCE = new DiscordAPI();
	public static final DiscordAPI getInstance() {
		return INSTANCE;
	}
	
	private DiscordRP discordRP = new DiscordRP();


	public void init() {
		discordRP.start();
	}

	public void shutdown() {
		discordRP.shutdown();
		
	}
	public DiscordRP getDiscordRP() {
		return discordRP;
	 }
	}
