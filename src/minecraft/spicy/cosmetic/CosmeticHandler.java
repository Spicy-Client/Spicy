package spicy.cosmetic;

import net.minecraft.client.entity.AbstractClientPlayer;
import spicy.main.Spicy;


public class CosmeticHandler {

    public static boolean shouldRenderHalo(AbstractClientPlayer player) {
        return Spicy.INSTANCE.user.isHaveHalo();
    }

    public static boolean shouldRenderWings(AbstractClientPlayer player) {
        return Spicy.INSTANCE.user.isHaveWings();
    }

    public static boolean shouldRenderCrown(AbstractClientPlayer player) {
        return Spicy.INSTANCE.user.isHaveCrown();
    }

    public static boolean shouldRenderHeadset(AbstractClientPlayer player) {
        return Spicy.INSTANCE.user.isHaveHeadset();
    }

    public static float[] getHaloColor(AbstractClientPlayer player) {
        return new float[] {255,255,0};
    }

    public static float[] getAngelWhiteWingsColor(AbstractClientPlayer player) {
        return new float[] {255,1,1};
    }

    public static float[] getCrownColor(AbstractClientPlayer player) {
        return new float[] {1,1,1};
    }

    public static float[] getHeadsetColor(AbstractClientPlayer player) {
        return new float[] {1,1,1};
    }

}
