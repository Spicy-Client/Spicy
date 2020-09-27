package spicy.utils;

import net.minecraft.network.Packet;

public class NetUtil implements MinecraftUtil {
    public static void sendPacketNoEvents(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendNewPacket(packet);
    }

    public static void sendPacket(Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
