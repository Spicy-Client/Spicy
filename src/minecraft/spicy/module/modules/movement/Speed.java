package spicy.module.modules.movement;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.util.*;
import net.optifine.util.MathUtils;
import spicy.events.player.PlayerMotionUpdateEvent;
import spicy.events.player.PlayerMoveEvent;
import spicy.main.Spicy;
import spicy.main.Wrapper;
import spicy.module.Category;
import spicy.module.Module;
import spicy.settings.Setting;
import spicy.utils.RotationUtils;

import java.util.ArrayList;
import java.util.List;

public class Speed extends Module
{
    public static float modifier;
    private int ticks;
    private int tickadd;
    private double ticks2;
    private int turnTicks;
    private float prevYaw;
    private boolean turnCancel;
    private boolean prevStrafing;
    private double moveSpeed;
    boolean move;
    protected Minecraft mc;


    /**Bhop variables*/
    private double bhopMoveSpeed;
    private static double bhopLastDig;
    public static int bhopStage;


    /**Onground variables*/
    private double OngroundMoveSpeed;
    private double OngroundLastDig;
    private double OngroundStage;

    public Speed() {
        super("Speed", 0, Category.MOVEMENT);
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Bhop");
        modes.add("Onground");
        modes.add("Minez");
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("SpeedMode", this, "Bhop", modes));
        super.setup();
    }


    @Override
    public void onDisabled() {
        System.out.println("Speed disabled.");
        Timer.timerSpeed = 1.0f;
        super.onDisabled();
    }

    @Override
    public void onEnabled() {
        System.out.println("Speed enabled.");
        if (Wrapper.player() != null) {
            this.bhopMoveSpeed = Wrapper.getBaseMoveSpeed();
        }
        bhopLastDig = 0.0;
        Speed.bhopStage = 1;
        if (Wrapper.player() != null) {
            this.OngroundMoveSpeed = Wrapper.getBaseMoveSpeed();
        }
        this.OngroundLastDig = 0.0;
        this.OngroundStage = 2.0;
        super.onEnabled();
    }

    @SubscribeEvent
    private void onMoves(PlayerMoveEvent event) {
        final String valString = Spicy.INSTANCE.settingsManager.getSettingByName("SpeedMode").getValString();
        switch (valString) {
            case "Minez": {
                Wrapper.player();
                break;
            }
            case "Bhop": {
                if (MathUtils.roundToPlace(Wrapper.player().posY - (int)Wrapper.player().posY, 3) == MathUtils.roundToPlace(0.4, 3)) {
                    event.setY(Wrapper.player().motionY = 0.31);
                }
                else if (MathUtils.roundToPlace(Wrapper.player().posY - (int)Wrapper.player().posY, 3) == MathUtils.roundToPlace(0.71, 3)) {
                    event.setY(Wrapper.player().motionY = 0.04);
                }
                else if (MathUtils.roundToPlace(Wrapper.player().posY - (int)Wrapper.player().posY, 3) == MathUtils.roundToPlace(0.75, 3)) {
                    event.setY(Wrapper.player().motionY = -0.2);
                }
                else if (MathUtils.roundToPlace(Wrapper.player().posY - (int)Wrapper.player().posY, 3) == MathUtils.roundToPlace(0.55, 3)) {
                    event.setY(Wrapper.player().motionY = -0.14);
                }
                else if (MathUtils.roundToPlace(Wrapper.player().posY - (int)Wrapper.player().posY, 3) == MathUtils.roundToPlace(0.41, 3)) {
                    event.setY(Wrapper.player().motionY = -0.2);
                }
                if (Speed.bhopStage == 1 && (Wrapper.player().moveForward != 0.0f || Wrapper.player().moveStrafing != 0.0f)) {
                    this.bhopMoveSpeed = 1.35 * Wrapper.getBaseMoveSpeed() - 0.01;
                }
                else if (Speed.bhopStage == 2 && (Wrapper.player().moveForward != 0.0f || Wrapper.player().moveStrafing != 0.0f)) {
                    event.setY(Wrapper.player().motionY = 0.4);
                    this.bhopMoveSpeed *= 2.149;
                }
                else if (Speed.bhopStage == 3) {
                    final double difference = 0.66 * (Speed.bhopLastDig - Wrapper.getBaseMoveSpeed());
                    this.bhopMoveSpeed = Speed.bhopLastDig - difference;
                }
                else {
                    final List collidingList = Wrapper.world().getCollidingBlockBoundingBoxes(Wrapper.player(), Wrapper.player().boundingBox.offset(0.0, Wrapper.player().motionY, 0.0));
                    if ((collidingList.size() > 0 || Wrapper.player().isCollidedVertically) && Speed.bhopStage > 0) {
                        Speed.bhopStage = ((Wrapper.player().moveForward != 0.0f || Wrapper.player().moveStrafing != 0.0f) ? 1 : 0);
                    }
                    this.bhopMoveSpeed = Speed.bhopLastDig - Speed.bhopLastDig / 159.0;
                }
                Wrapper.setMoveSpeed(event, this.bhopMoveSpeed = Math.max(this.bhopMoveSpeed, Wrapper.getBaseMoveSpeed()));
                if (Wrapper.player().moveForward != 0.0f || Wrapper.player().moveStrafing != 0.0f) {
                    ++Speed.bhopStage;
                }
                break;
            }
            case "Onground": {
                if ((Wrapper.player().onGround || this.OngroundStage == 3.0)) {
                    if ((!Wrapper.player().isCollidedHorizontally && Wrapper.player().moveForward != 0.0f) || Wrapper.player().moveStrafing != 0.0f) {
                        if (this.OngroundStage == 2.0) {
                            this.OngroundMoveSpeed *= 2.149;
                            this.OngroundStage = 3.0;
                        }
                        else if (this.OngroundStage == 3.0) {
                            this.OngroundStage = 2.0;
                            final double difference = 0.66 * (this.OngroundLastDig - Wrapper.getBaseMoveSpeed());
                            this.OngroundMoveSpeed = this.OngroundLastDig - difference;
                        }
                        else {
                            final List collidingList = Wrapper.world().getCollidingBlockBoundingBoxes(Wrapper.player(), Wrapper.player().boundingBox.offset(0.0, Wrapper.player().motionY, 0.0));
                            if (collidingList.size() > 0 || Wrapper.player().isCollidedVertically) {
                                this.OngroundStage = 1.0;
                            }
                        }
                    }
                    else {
                        final Timer timer = Wrapper.mc().timer;
                        Timer.timerSpeed = 1.0f;
                    }
                    Wrapper.setMoveSpeed(event, this.OngroundMoveSpeed = Math.max(this.OngroundMoveSpeed, Wrapper.getBaseMoveSpeed()));
                }
                break;
            }
        }
    }

    @SubscribeEvent(0)
    private void onUpdate(final PlayerMotionUpdateEvent event) {
        if (event.getState() == PlayerMotionUpdateEvent.State.PRE) {
            this.setModuleSuffix(Spicy.INSTANCE.settingsManager.getSettingByName("SpeedMode").getValString());
            final String valString = Spicy.INSTANCE.settingsManager.getSettingByName("SpeedMode").getValString();
            switch (valString) {
                case "Minez": {
                    if (!this.mc.thePlayer.onGround) {
                        this.ticks = -5;
                        this.tickadd = -5;
                        this.ticks2 = Wrapper.getBaseMoveSpeed();
                        this.moveSpeed = Wrapper.getBaseMoveSpeed();
                        this.ticks2 = 0.0;
                        ++this.ticks2;
                    }
                    Timer.timerSpeed = 1.0f;
                    if (this.checks()) {
                        double speed;
                        double speedTick = 2.485;
                        double speedEmulsifier = 5.0;
                        double speedFix112 = 2.333;
                        double holyshit = 0.4;
                        speed = 2.5;
                        speedTick = 0.001664;
                        speedEmulsifier = 1.0E-4;
                        speedFix112 = 0.00123662;
                        holyshit = 0.003;
                        if (this.isStrafing() != this.prevStrafing) {
                            this.turnTicks = 1;
                            this.turnCancel = true;
                            ++this.ticks2;
                            this.moveSpeed = Wrapper.getBaseMoveSpeed();
                            final MovementInput movementInput = Minecraft.getMinecraft().thePlayer.movementInput;
                            float forward = Minecraft.getMinecraft().thePlayer.moveForward;
                            float strafe = Minecraft.getMinecraft().thePlayer.moveStrafing;
                            float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                            if (forward != 0.0f) {
                                if (strafe >= 1.0f) {
                                    yaw += ((forward > 0.0f) ? -45 : 45);
                                    strafe = 0.0f;
                                } else if (strafe <= -1.0f) {
                                    yaw += ((forward > 0.0f) ? 45 : -45);
                                    strafe = 0.0f;
                                }
                                if (forward > 0.0f) {
                                    forward = 1.0f;
                                } else if (forward < 0.0f) {
                                    forward = -1.0f;
                                }
                            }
                        }
                        if (this.ticks2 == 1.0) {
                            speed = 0.003;
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed);
                            }
                            this.ticks2 = 0.0;
                        }
                        if (this.ticks == 1) {
                            Timer.timerSpeed = 1.3f;
                            if (this.turnTicks <= 0) {
                                this.turnCancel = false;
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * holyshit, 0.0, this.mc.thePlayer.motionZ * holyshit))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * holyshit, 0.0, this.mc.thePlayer.motionZ * holyshit);
                            }
                            ++this.ticks2;
                            this.ticks2 = 1.0;
                        }
                        if (this.tickadd == 1) {
                            speed = 0.1;
                            Timer.timerSpeed = 1.3f;
                            if (this.turnTicks <= 0) {
                                this.turnCancel = false;
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * holyshit, 0.0, this.mc.thePlayer.motionZ * holyshit))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * holyshit, 0.0, this.mc.thePlayer.motionZ * holyshit);
                            }
                        } else if (this.ticks == 2) {
                            speed = 0.61;
                            speedTick = 0.001;
                            speedEmulsifier = 0.01;
                            speedFix112 = 0.01;
                            holyshit = -0.0;
                            final double lmx = Wrapper.mc().thePlayer.motionX;
                            final double lmz = Wrapper.mc().thePlayer.motionZ;
                            final MovementInput movementInput2 = Minecraft.getMinecraft().thePlayer.movementInput;
                            float forward2 = Minecraft.getMinecraft().thePlayer.moveForward;
                            float strafe2 = Minecraft.getMinecraft().thePlayer.moveStrafing;
                            float yaw2 = Minecraft.getMinecraft().thePlayer.rotationYaw;
                            if (forward2 != 0.0f) {
                                if (strafe2 >= 1.0f) {
                                    yaw2 += ((forward2 > 0.0f) ? -45 : 45);
                                    strafe2 = 0.0f;
                                } else if (strafe2 <= -1.0f) {
                                    yaw2 += ((forward2 > 0.0f) ? 45 : -45);
                                    strafe2 = 0.0f;
                                }
                                if (forward2 > 0.0f) {
                                    forward2 = 1.0f;
                                } else if (forward2 < 0.0f) {
                                    forward2 = -1.0f;
                                }
                            }
                            if (!this.turnCancel && !this.isTurning() && !this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112);
                            }
                        } else if (this.ticks == 3) {
                            speed = 0.59;
                            speedTick = 0.001;
                            speedEmulsifier = 0.01;
                            speedFix112 = 0.0010055235320003313;
                            final double lmx = Wrapper.mc().thePlayer.motionX;
                            final double lmz = Wrapper.mc().thePlayer.motionZ;
                            final MovementInput movementInput2 = Minecraft.getMinecraft().thePlayer.movementInput;
                            float forward2 = Minecraft.getMinecraft().thePlayer.moveForward;
                            float strafe2 = Minecraft.getMinecraft().thePlayer.moveStrafing;
                            float yaw2 = Minecraft.getMinecraft().thePlayer.rotationYaw;
                            if (forward2 != 0.0f) {
                                if (strafe2 >= 1.0f) {
                                    yaw2 += ((forward2 > 0.0f) ? -45 : 45);
                                    strafe2 = 0.0f;
                                } else if (strafe2 <= -1.0f) {
                                    yaw2 += ((forward2 > 0.0f) ? 45 : -45);
                                    strafe2 = 0.0f;
                                }
                                if (forward2 > 0.0f) {
                                    forward2 = 1.0f;
                                } else if (forward2 < 0.0f) {
                                    forward2 = -1.0f;
                                }
                            }
                            if (!this.turnCancel && !this.isTurning() && !this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112);
                            }
                        } else if (this.tickadd >= 3) {
                            speed = 0.69;
                            speedTick = 0.001;
                            speedEmulsifier = 0.01;
                            speedFix112 = 0.0010055235320003313;
                            final double lmx = Wrapper.mc().thePlayer.motionX;
                            final double lmz = Wrapper.mc().thePlayer.motionZ;
                            final MovementInput movementInput2 = Minecraft.getMinecraft().thePlayer.movementInput;
                            float forward2 = Minecraft.getMinecraft().thePlayer.moveForward;
                            float strafe2 = Minecraft.getMinecraft().thePlayer.moveStrafing;
                            float yaw2 = Minecraft.getMinecraft().thePlayer.rotationYaw;
                            if (forward2 != 0.0f) {
                                if (strafe2 >= 1.0f) {
                                    yaw2 += ((forward2 > 0.0f) ? -45 : 45);
                                    strafe2 = 0.0f;
                                } else if (strafe2 <= -1.0f) {
                                    yaw2 += ((forward2 > 0.0f) ? 45 : -45);
                                    strafe2 = 0.0f;
                                }
                                if (forward2 > 0.0f) {
                                    forward2 = 1.0f;
                                } else if (forward2 < 0.0f) {
                                    forward2 = -1.0f;
                                }
                            }
                            if (!this.turnCancel && !this.isTurning() && !this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedTick, 0.0, this.mc.thePlayer.motionZ * speedTick);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier);
                            }
                            if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112))) {
                                this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112);
                            }
                        } else if (this.ticks >= 4) {
                            if (this.ticks == 4) {
                                speed = 0.54;
                                speedTick = 0.0021;
                                speedEmulsifier = 0.02;
                                final double lmx = Wrapper.mc().thePlayer.motionX;
                                final double lmz = Wrapper.mc().thePlayer.motionZ;
                                final MovementInput movementInput2 = Minecraft.getMinecraft().thePlayer.movementInput;
                                float forward2 = Minecraft.getMinecraft().thePlayer.moveForward;
                                float strafe2 = Minecraft.getMinecraft().thePlayer.moveStrafing;
                                float yaw2 = Minecraft.getMinecraft().thePlayer.rotationYaw;
                                if (forward2 != 0.0f) {
                                    if (strafe2 >= 1.0f) {
                                        yaw2 += ((forward2 > 0.0f) ? -45 : 45);
                                        strafe2 = 1.1f;
                                    } else if (strafe2 <= -1.0f) {
                                        yaw2 += ((forward2 > 0.0f) ? 45 : -45);
                                        strafe2 = 0.6f;
                                    }
                                    if (forward2 > 0.0f) {
                                        forward2 = 14.0f;
                                    } else if (forward2 < 0.0f) {
                                        forward2 = 15.0f;
                                    }
                                }
                                if (!this.turnCancel && !this.isTurning() && !this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed))) {
                                    this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speed, 0.0, this.mc.thePlayer.motionZ * speed);
                                }
                                if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier))) {
                                    this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier);
                                }
                                if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier))) {
                                    this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedEmulsifier, 0.0, this.mc.thePlayer.motionZ * speedEmulsifier);
                                }
                                if (!this.isColliding(this.mc.thePlayer.boundingBox.offset(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112))) {
                                    this.mc.thePlayer.boundingBox.offsetAndUpdate(this.mc.thePlayer.motionX * speedFix112, 0.0, this.mc.thePlayer.motionZ * speedFix112);
                                }
                            }
                            this.ticks = 0;
                            this.tickadd = 0;
                        }
                        this.tickadd = 0;
                    }
                    this.prevYaw = this.mc.thePlayer.rotationYaw;
                    ++this.ticks;
                    --this.turnTicks;
                    this.prevStrafing = this.isStrafing();
                    break;
                }
                case "Bhop" : {
                    final double xDist = Wrapper.x() - Wrapper.player().prevPosX;
                    final double zDist = Wrapper.z() - Wrapper.player().prevPosZ;
                    bhopLastDig = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
                }
                case "Onground": {
                    if (this.OngroundStage == 3.0) {
                        event.setY(event.getY() + 0.4);
                    }
                    final double xDist = Wrapper.x() - Wrapper.player().prevPosX;
                    final double zDist = Wrapper.z() - Wrapper.player().prevPosZ;
                    this.OngroundLastDig = Math.sqrt(xDist * xDist + zDist * zDist);
                    break;
                }
            }
        }
    }

    @SubscribeEvent
    private void onMove(final PlayerMoveEvent event) {
        if (this.mc.thePlayer.moveStrafing != 0.0f && this.mc.thePlayer.moveForward != 0.0f) {
            event.setX(event.getX() * 0.95);
            event.setZ(event.getZ() * 0.95);
        }
    }

    private boolean isColliding(final AxisAlignedBB bb) {
        boolean colliding = false;
        for (final Object axisAlignedBB : Minecraft.getMinecraft().theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, bb)) {
            colliding = true;
        }
        if (this.getBlock(bb.offset(0.0, -0.1, 0.0)) instanceof BlockAir) {
            colliding = true;
        }
        return colliding;
    }

    private boolean isTurning() {
        return RotationUtils.getDistanceBetweenAngles(this.mc.thePlayer.rotationYaw, this.prevYaw) > 4.0f;
    }

    public Block getBlock(final AxisAlignedBB bb) {
        final int y = (int)bb.minY;
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                final Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null) {
                    return block;
                }
            }
        }
        return null;
    }



    @Override
    public String getModuleDesc() {
        return "Speed for walk fast";
    }

    private boolean isStrafing() {
        final MovementInput movementInput = this.mc.thePlayer.movementInput;
        return Minecraft.getMinecraft().thePlayer.moveStrafing != 0.0f && this.mc.thePlayer.moveForward != 0.0f;
    }

    public boolean checks() {
        return !this.mc.thePlayer.isSneaking() && !this.mc.thePlayer.isCollidedHorizontally && (this.mc.thePlayer.isCollidedHorizontally || this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && !this.mc.gameSettings.keyBindJump.isPressed() && !Wrapper.isOnLiquid() && !Wrapper.isInLiquid();
    }
}
