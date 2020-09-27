package spicy.cosmetic.adapters;

public interface MathAdapter
{
    int clamp_int(int var1, int var2, int var3);

    double clamp_double(double var1, double var3, double var5);

    int floor_float(float var1);

    int ceiling_float_int(float var1);

    int ceiling_double_int(double var1);

    float sin(float var1);

    float cos(float var1);

    float clamp_float(float var1, float var2, float var3);

    float sqrt_float(float var1);

    float abs(float var1);

    int hsvToRGB(float var1, float var2, float var3);

    float wrapAngleTo180_float(float var1);

    int floor_double(double var1);
}
