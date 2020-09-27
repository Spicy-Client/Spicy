package spicy.gui.music;

import com.youtube.search.Item;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.TextureImpl;
import spicy.discord.DiscordAPI;
import spicy.font.TTFFontRenderer;
import spicy.main.Spicy;
import spicy.music.MusicManager;
import spicy.music.image.Resources;
import spicy.music.image.TextureImage;
import spicy.utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


public class MusicScreen extends GuiScreen {
    public static int arrayOffsetY;
    private Expand expand = new Expand(0, 0, 0, 0);
    private Expand scroller = new Expand(0, 0, 0, 0);
    private List<Item> displayedTracks = new ArrayList<>();
    private int yOffset = 0;
    private int yOffsetTarget = 0;
    private MusicTextField search = null;
    public static float volumeControl = 1;

    @Override
    public void initGui() {
        Minecraft m = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(m);
        float x = scaledResolution.getScaledWidth() / 2;
        float y = scaledResolution.getScaledHeight() / 2;
        search = new MusicTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 83, (int) y - 123, 240, 20);
        search.setFocused(true);
    }

    @Override
    public boolean doesGuiPauseGame(){
        return true;
    }

    private Translate hoverAnim = new Translate(0, 0);
    private boolean hover;

    public float smoothTrans(double current, double last){
        return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if(MusicManager.getInstance().getCurrentItem() != null) {
            DiscordAPI.getInstance().getDiscordRP().update("Listening " + MusicManager.getInstance().getCurrentItem().getSnippet().getTitle(), "");
        }        scroller.interpolate(0, yOffsetTarget,0, 10);
        yOffset = (int) scroller.getExpandY();
        GlStateManager.disableAlpha();


        hoverAnim.interpolate(4, 0, 0.5f);

        Minecraft m = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(m);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        int s = res.getScaleFactor();

        RenderingUtil.drawRect(x - 210, y - 140, x + 185, y + 140, Colors.getColor(25, 25, 20, 225));
        RenderingUtil.drawRect(x - 210, y - 140, x - 85, y + 140, Colors.getColor(19, 19, 14));
        RenderingUtil.drawRect(x - 210, y - 140, x - 85, y - 110, Colors.getColor(24, 24, 19));

        TTFFontRenderer headerFont = Spicy.INSTANCE.fontManager.getFont("FONT 15");
        TTFFontRenderer headerFont2 = Spicy.INSTANCE.fontManager.getFont("FONT 25");
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("spicy/music/musicbiggest.png"));
        GlStateManager.enableBlend();
        GlStateManager.color(255,255,255);
        drawModalRectWithCustomSizedTexture(
                (int)x - 150,
                (((int)y - 140) + ((int)y - 110)) / 2 - (int)7.55f, 0, 0, 16, 16, 16, 16);
        GlStateManager.disableBlend();
        GlStateManager.color(1, 1, 1, 255);
        headerFont2.drawCenteredString("Spicy",
                (x - (210 - 16)) + 17.5f,
                (((y - 150) + (y - 110)) / 2 - 7.55f) + 4f, Colors.getColor(255));
        headerFont.drawCenteredString("Music",
                (x - (210 - 16)) + 17.5f,
                (((y - 120) + (y - 110)) / 2 - 7.55f) + 4f, Colors.getColor(255));


        search.drawTextBox();

        MultipleGLScissor sc1 = new MultipleGLScissor((int) (x - 89) * s, (int) (y - 137) * s, 270 * s, 237 * s);

        GlStateManager.color(1, 1, 1, 255);

        if (displayedTracks != null && !displayedTracks.isEmpty()) {
            int rowIndex = 0;
            int row = 0;
            int i = 0;
            for (Item track : displayedTracks) {
                if(track.image == null || track.image.texture == null){
                    track.image = Resources.downloadTexture("https://img.youtube.com/vi/"+ track.getId().getVideoId() +"/0.jpg");
                }
                TextureImage artLocation = track.image;
                boolean hoverCheck = (mouseY >= y - 140 && mouseY <= y + 140 &&
                        mouseX >= x - 80 + 85 * rowIndex &&
                        mouseX <= x - 80 + 85 * rowIndex + 65 &&
                        mouseY >= y - 85 + i * 88 + yOffset &&
                        mouseY <= y - 85 + i * 88 + yOffset + 65);
                if (artLocation != null) {
                    GlStateManager.color(1, 1, 1, 1);
                    TextureImpl.bindNone();
                    GlStateManager.color(1, 1, 1, 1);
                    GlStateManager.color(255, 255, 255, 255);
                    if (hoverCheck) {
                        artLocation.rectTextureMasked(((int) x - 80 + 85 * rowIndex) - (int) hoverAnim.getX(), ((int) y - 85 + i * 88 + yOffset) - (int) hoverAnim.getX(), 65 + ((int) hoverAnim.getX() * 2), 65 + ((int) hoverAnim.getX() * 2), 255, 0.001f);
                    } else {
                        artLocation.rectTextureMasked(((int) x - 80 + 85 * rowIndex), ((int) y - 85 + i * 88 + yOffset), 65, 65, 255, 0.001f);
                    }
                } else {
                    if (hoverCheck)
                        RenderingUtil.drawRect(
                                (x - 80 + 85 * rowIndex) - (int) hoverAnim.getX(),
                                (y - 85 + i * 88 + yOffset) - (int) hoverAnim.getX(),
                                (x - 80 + 85 * rowIndex) + 65 + (int) hoverAnim.getX(),
                                (y - 85 + i * 88 + yOffset) + 65 + (int) hoverAnim.getX(),
                                Colors.getColor(25, 25, 20, 235));
                    else
                        RenderingUtil.drawRect(
                                (x - 80 + 85 * rowIndex),
                                (y - 85 + i * 88 + yOffset),
                                (x - 80 + 85 * rowIndex) + 65,
                                (y - 85 + i * 88 + yOffset) + 65,
                                Colors.getColor(25, 25, 20, 235));
                //    RenderingUtil.drawRect(x - 80 + 85 * rowIndex, y - 85 + i * 88 + yOffset, x - 80 + 85 * rowIndex + 65, y - 85 + i * 88 + yOffset + 65, Colors.getColor(25, 25, 20, 235));
                    GlStateManager.enableBlend();
                    Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("spicy/music/musicbig.png"));
                    drawModalRectWithCustomSizedTexture((int) x - 65 + 85 * rowIndex, (int) y - 67 + i * 88 + yOffset, 0, 0, 28, 28, 28, 28);
                    GlStateManager.disableBlend();
                }
                if (hoverCheck){
                    RenderingUtil.drawRects((int)((x - 79 + 85 * rowIndex) - hoverAnim.getX()), (int)(y - 85 + i * 88 + yOffset) - (int)hoverAnim.getX(), 65 + ((int)hoverAnim.getX()*2), 65 + ((int)hoverAnim.getX()*2), Colors.getColor(0, 150));
                    GlStateManager.enableBlend();
                    Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("spicy/music/playbiggest.png"));
                    drawModalRectWithCustomSizedTexture((((((x - 80 + 85 * rowIndex) + (x - 80 + 85 * rowIndex + 65)) / 2)) - 6f) - (int)hoverAnim.getX(), (y - 65 + i * 88 + yOffset) + 3.5f - (int)hoverAnim.getX(), 0, 0, 14 + hoverAnim.getX(), 14 + hoverAnim.getX(), 14 + hoverAnim.getX(), 14 + hoverAnim.getX());
                    GlStateManager.disableBlend();
                    if(!hover) {
                        hoverAnim.setX(0);
                        hover = true;
                    }
                } else {
                    hover = false;
                }
                String
                        trackNameSplitted = track.getSnippet().getTitle().split(Pattern.quote("("))[0].split(Pattern.quote("["))[0],

                        trackName = StringUtils.abbreviate((trackNameSplitted.contains(" - ") && trackNameSplitted.split(" - ").length >= 2 && trackNameSplitted.split(" - ")[1] != null) ? trackNameSplitted.split(" - ")[1] : trackNameSplitted, 20),

                        trackAuthor = StringUtils.abbreviate(trackNameSplitted.contains(" - ") ? trackNameSplitted.split(" - ")[0] : "", 20);

                GlStateManager.color(1, 1, 1, 255);
                Spicy.INSTANCE.fontManager.getFont("FONT 15").drawCenteredString(trackName.replaceAll("ı", "i").replaceAll("ğ", "g").replaceAll("Ğ", "G").replaceAll("İ", "I").replaceAll("ş", "s").replaceAll("Ş", "S"),
                        ((x - 80 + 85 * rowIndex) + (x - 80 + 85 * rowIndex + 65)) / 2,
                        y - 85 + i * 88 + yOffset + 65 + 5, Colors.getColor(255));
                GlStateManager.color(1, 1, 1, 255);
                Spicy.INSTANCE.fontManager.getFont("FONT 10").drawCenteredString(trackAuthor.replaceAll("ı", "i").replaceAll("ğ", "g").replaceAll("Ğ", "G").replaceAll("İ", "I").replaceAll("ş", "s").replaceAll("Ş", "S"),
                        ((x - 80 + 85 * rowIndex) + (x - 80 + 85 * rowIndex + 65)) / 2,
                        y - 85 + i * 88 + yOffset + 65 + 5 + 1.3f + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, Colors.getColor(255));
                GlStateManager.color(1, 1, 1, 255);

                if(rowIndex == 2){
                    rowIndex = 0;
                    i++;
                } else {
                    rowIndex++;
                }
            }
            float progress = -(float) yOffset / ((i * 65) - 244);
            int height = (int) (244 * (244f / Math.max(244, i * 65)));
            int progressPos = (int) (progress * (244 - height));
        } else {
            Spicy.INSTANCE.fontManager.getFont("FONT 20").drawCenteredString("No results", x + 33, y + 10, Colors.getColor(255));
        }



        sc1.destroy();

        if(MusicManager.getInstance().getCurrentItem() != null){
            Item track = MusicManager.getInstance().getCurrentItem();
            TextureImage artLocation = track.image;
            if (artLocation != null && false) {//TODO
                //artLocation.texture.bind();
                //artLocation.rectTextureMasked(x - 210, y + 130,);
                TextureImpl.bindNone();
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.color(1, 1, 1, 255);
                artLocation.rectTextureMasked(x - 210, y + 130, 395, 40, 255, 0.001f);

            //    RenderingUtil.drawRect(x - 210, y + 130, x + 185, y + 170, Colors.getColor(25, 25, 20, 225));

                RenderingUtil.drawRects((int)x - 210, (int)y + 130, 395, 40, 32);
            } else if (artLocation != null){
                artLocation.rectTextureMaskedBanner(x - 210, y + 130, 395, 40, 0, 0.001f);
                RenderingUtil.drawRect(x - 210, y + 130, x + 185, y + 170, new java.awt.Color(15,15,15,125  ).getRGB());
                //artLocation.texture.bind();
                GlStateManager.color(1, 1, 1, 1);
                TextureImpl.bindNone();
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.color(255, 255, 255, 255);
                artLocation.rectTextureMasked(x - 180, y + 130 - 65 + 15, 65, 65, 255, 0.001f);

                String trackName = StringUtils.abbreviate(track.getSnippet().getTitle().split(Pattern.quote("("))[0].split(Pattern.quote("["))[0], 22);
                GlStateManager.color(1, 1, 1, 255);
                GlStateManager.disableBlend();
                Spicy.INSTANCE.fontManager.getFont("FONT 15").drawCenteredString(trackName,
                        ((x - 180) + ((x - 180) + 75)) / 2
                        , y + 130 - 65 + 15 + 65 + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, Colors.getColor(255));
                GlStateManager.enableBlend();

            } else {
                RenderingUtil.drawRect(x - 210, y + 130, x + 185, y + 170, Colors.getColor(25, 25, 20, 255));


            }
        } else {
            RenderingUtil.drawRect(x - 210, y + 130, x + 185, y + 170, Colors.getColor(25, 25, 20, 255));
            GlStateManager.color(255,255,255);
        }

        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        if((MusicManager.getInstance().getMediaPlayer() == null || MusicManager.getInstance().getMediaPlayer().getStatus() != MediaPlayer.Status.PLAYING))
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("spicy/music/playbiggest.png"));
        else
            Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("spicy/music/pausebiggest.png"));

        drawModalRectWithCustomSizedTexture(((x - 85) + (x + 185)) / 2 - 17, y + 142, 0, 0, 13, 13, 13, 13);

        GlStateManager.disableBlend();
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("spicy/music/backwardbiggest.png"));
        drawModalRectWithCustomSizedTexture((((x - 85) + (x + 185)) / 2) - 17 - 63, y + 142, 0, 0, 13, 13, 13, 13);

        GlStateManager.disableBlend();
        GlStateManager.enableBlend();

        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(new ResourceLocation("spicy/music/forwardbiggest.png"));
        drawModalRectWithCustomSizedTexture((((x - 85) + (x + 185)) / 2) - 18 + 63, y + 142, 0, 0, 13, 13, 13, 13);
        GlStateManager.disableBlend();
        float volume = this.volumeControl;
        float x1 = (((x - 210) + (x + 185)) / 2) - 40;
        float x2 = (((x - 210) + (x + 185)) / 2) + 40;
        Gui.drawRect(x + 173, y + 137, x + 173 + 2, y + 20 + 137, new java.awt.Color(125, 117, 117,155).getRGB());
        Gui.drawRect(x + 173, (y - (-1+volume)*20) + 137, x  + 173 + 2, y + 20 + 137, new java.awt.Color(255, 255, 255,155).getRGB());
        if(Mouse.isButtonDown(0) && this.isMouseHoveringRect2(x + 173, y + 137, x + 173 + 2, y + 20 + 137, mouseX, mouseY)) {
            System.out.println(1);
            volumeControl = Math.min(20, Math.max(0,((y + 20 + 137)-mouseY)))/20f;
            MusicManager.getInstance().getMediaPlayer().setVolume(MusicScreen.volumeControl);
        }
        int readedSeconds = 0, totalSeconds = 0;

        if (MusicManager.getInstance().getMediaPlayer() != null) {
            readedSeconds = (int) MusicManager.getInstance().getMediaPlayer().getCurrentTime().toSeconds();
            totalSeconds = (int) MusicManager.getInstance().getMediaPlayer().getStopTime().toSeconds();
        }

        TTFFontRenderer sfl9 = Spicy.INSTANCE.fontManager.getFont("FONT 15");
        float xdd1 = x - 85;
        float xdd2 = x + 185;

        GlStateManager.color(1, 1, 1, 255);
        sfl9.drawString(formatSeconds(readedSeconds), xdd1, y + 167 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT,
                Colors.getColor(255, 222));
        sfl9.drawString(formatSeconds(totalSeconds), xdd2 - sfl9.getWidth(formatSeconds(totalSeconds)) + 0.6f, y + 168 - Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT,
                Colors.getColor(255, 222));

        drawRect((int)xdd1, (int)y + 168, (int)xdd2, (int)y + 170, Colors.getColor(145, 200));
       if(readedSeconds > 0 || totalSeconds > 0) {
           drawRect((int) xdd1, (int) y + 168, (int) xdd1 - 1 + (267) * readedSeconds / totalSeconds, (int) y + 170, Colors.getColor(255, 230));
       }



        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                yOffsetTarget -= 45;
            } else if (wheel > 0) {
                if(!(yOffsetTarget >= displayedTracks.size() * 70)) {
                    yOffsetTarget += 45;
                    if (yOffsetTarget > 0) {
                        yOffsetTarget = 0;
                    }
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            Minecraft.getMinecraft().entityRenderer.theShaderGroup = null;
        }
        switch (keyCode) {
            case Keyboard.KEY_DOWN:
                yOffsetTarget -= 160;
                break;
            case Keyboard.KEY_UP:
                yOffsetTarget += 160;
                if (yOffsetTarget > 0) {
                    yOffsetTarget = 0;
                }
                break;
            case Keyboard.KEY_RETURN:
                if (search.isFocused()) {
                    String[] splittedSearch = search.getText().split(" limit: ");
                    displayedTracks = MusicManager.getInstance().search(splittedSearch[0],
                            Optional.ofNullable(splittedSearch.length >= 2 ? splittedSearch[1] : "9").orElse("9"));
                }
                break;
        }
        search.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    public void drawTexturedRect(float x, float y, float width, float height, String image) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("spicy/"+image+".png"));
        Gui.drawModalRectWithCustomSizedTexture(x,  y, 0, 0, width, height, width, height);
    }

    public boolean isMouseHoveringRect2(float x, float y, float x2, float y2, int mouseX, int mouseY){
        return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }

    public boolean isHoveringCoords(float x, float y, float width, float height, int mouseX, int mouseY){
        return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height-0.5f;
    }

    @Override
    protected void mouseClicked(int clicX, int clicY, int button) {
        try {
            super.mouseClicked(clicX, clicY, button);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Minecraft m = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(m);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;

        if ( // PLAY & PAUSE
                clicX >= (((x - 85) + (x + 185)) / 2) - 17 &&
                        clicX <= ((((x - 85) + (x + 185)) / 2) - 17 + 13) &&
                        clicY >= y + 142 &&
                        clicY <= y + 142 + 13
        ){
            if(MusicManager.getInstance().getMediaPlayer() == null)
                return;
            if (MusicManager.getInstance().getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                MusicManager.getInstance().getMediaPlayer().pause();
                return;
            } else {
                MusicManager.getInstance().getMediaPlayer().play();
                return;
            }
        } else if ( // PLAY & PAUSE
                clicX >= (((x - 85) + (x + 185)) / 2) - 18 + 63 &&
                        clicX <= ((((x - 85) + (x + 185)) / 2) - 18 + 63 + 13) &&
                        clicY >= y + 142 &&
                        clicY <= y + 142 + 13
        ) {
            if (MusicManager.getInstance().getMediaPlayer() == null)
                return;
            else {
                MusicManager.getInstance().next();
                return;
            }
        } else if ( // PLAY & PAUSE
                clicX >= (((x - 85) + (x + 185)) / 2) - 17 - 63 &&
                        clicX <= ((((x - 85) + (x + 185)) / 2) - 17 - 63 + 13) &&
                        clicY >= y + 142 &&
                        clicY <= y + 142 + 13
        ) {
            if (MusicManager.getInstance().getMediaPlayer() == null)
                return;
            else {
                MusicManager.getInstance().prev();
                return;
            }
        }


        if (displayedTracks != null) {
            int i = 0;
            int rowIndex = 0;
            for (Item track : displayedTracks) {
                if (clicX >= x - 80 + 85 * rowIndex &&
                        clicX <= x - 80 + 85 * rowIndex + 65 &&
                        clicY >= y - 85 + i * 88 + yOffset &&
                        clicY <= y - 85 + i * 88 + yOffset + 65){
                    MusicManager.getInstance().play(track);
                    break;
                }

                if(rowIndex == 2){
                    rowIndex = 0;
                    i++;
                } else {
                    rowIndex++;
                }
            }
        }

        search.mouseClicked(clicX, clicY, button);
    }

    private String formatSeconds(int seconds) {
        String rstl = "";
        int mins = seconds / 60;
        if (mins < 10) {
            rstl += "0";
        }
        rstl += mins + ":";
        seconds %= 60;
        if (seconds < 10) {
            rstl += "0";
        }
        rstl += seconds;
        return rstl;
    }


}


