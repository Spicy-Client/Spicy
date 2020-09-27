package spicy.cosmetic;


import spicy.cosmetic.adapters.CoreAdapter;
import spicy.cosmetic.adapters.MathAdapter;

public class CosmeticCore
{
    private static CoreAdapter coreAdapter;


    public static MathAdapter getMath()
    {
        return coreAdapter.getMathImplementation();
    }
}
