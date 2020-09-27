package spicy.utils;

public class Expand
{
    private float x;
    private float y;
    private float expandX;
    private float expandY;
    private long lastMS;
    
    public Expand(final float x, final float y, final float expandX, final float expandY) {
        this.x = x;
        this.y = y;
        this.expandX = expandX;
        this.expandY = expandY;
    }
    
    public void hardInterpolate(final float targetX, final float targetY, final int xSpeed, final int ySpeed) {
        final long currentMS = System.currentTimeMillis();
        final long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.expandX = AnimationUtil.calculateCompensation(targetX, this.expandX, delta, xSpeed);
        this.expandY = AnimationUtil.calculateCompensation(targetY, this.expandY, delta, ySpeed);
    }
    
    public void interpolate(final float targetX, final float targetY, final int xSpeed, final int ySpeed) {
        final long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        if (delta > 60L) {
            delta = 16L;
        }
        this.lastMS = currentMS;
        final int deltaX = (int)(Math.abs(targetX - this.expandX) * 1.1f);
        final int deltaY = (int)(Math.abs(targetY - this.expandY) * 0.5f);
        this.expandX = AnimationUtil.calculateCompensation(targetX, this.expandX, delta, deltaX);
        this.expandY = AnimationUtil.calculateCompensation(targetY, this.expandY, delta, deltaY);
    }
    
    public float getExpandX() {
        return this.expandX;
    }
    
    public float getExpandY() {
        return this.expandY;
    }
    
    public void setExpandX(final float expandX) {
        this.expandX = expandX;
    }
    
    public void setExpandY(final float expandY) {
        this.expandY = expandY;
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
}
