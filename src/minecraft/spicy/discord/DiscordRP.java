package spicy.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;
import spicy.main.Spicy;
import spicy.security.HWID;
import spicy.user.User;

public class DiscordRP {
 
	private boolean running = true;
	private long created = 0;
	public void start() {
		this.created = System.currentTimeMillis();
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
			@Override
			public void apply(DiscordUser user) {
				System.out.println("Websome " + user.username + "#" + user.discriminator + ".");
				update("Booting...", "");
			}
		}).build();
		
		DiscordRPC.discordInitialize("726036596754743367", handlers, true);
		
		new Thread("Discord RPC Callback" ) {
			@Override
			public void run() { 
				while(running) {
					DiscordRPC.discordRunCallbacks();
					
				}
			}
		}.start();

		}
		public void shutdown() {
			running = false;
			DiscordRPC.discordShutdown();
			
		}
        public void update(String firstLine, String secondLine) {
        	DiscordRichPresence.Builder b = new DiscordRichPresence.Builder(secondLine);
        	DiscordRichPresence p = new DiscordRichPresence();
        	b.setBigImage("big", "");
        	b.setSmallImage("small", "");
        	b.build().largeImageText = "Spicy";
        	if(Spicy.INSTANCE.user.getRank() == User.RankGroup.User) {
				b.build().smallImageText = "User";
			} else if(Spicy.INSTANCE.user.getRank() == User.RankGroup.Vip) {
				b.build().smallImageText = "VIP";
			} else {
				b.build().smallImageText = "Staff";
			}
        	b.setDetails(firstLine);
        	b.build().state = "User: " + HWID.displayRealName;
        	b.setStartTimestamps(created);
        	DiscordRPC.discordUpdatePresence(b.build());
             }
        }
