package spicy.command.commands;


import spicy.command.Command;
import spicy.main.Spicy;

public class Cosmetic extends Command
{
    public static boolean crown = false;
    public static boolean halo = false;
    public static boolean headset = false;

    public static String name;

    @Override
    public String getNames() {
        return "cosmetic";
    }

    @Override
    public String getDesc() {
        return "Cosmetics for player.";
    }

    @Override
    public String getError() {
        return "cosmetic {wing/crown/halo/headset/ears} true/false";
    }

    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        boolean b = Boolean.parseBoolean(args[1]);
        switch (args[0]) {
            case "wing": {
                Spicy.INSTANCE.user.setHaveWings(b);
                break;
            }
            case "crown": {
                Spicy.INSTANCE.user.setHaveCrown(b);
                break;
            }
            case "halo": {
                Spicy.INSTANCE.user.setHaveHalo(b);
                break;
            }
            case "headset": {
                Spicy.INSTANCE.user.setHaveHeadset(b);
                break;
            }
            case "ears": {
                Spicy.INSTANCE.user.setHaveEars(b);
            }
        }
    }
}
