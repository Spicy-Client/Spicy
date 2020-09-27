package spicy.music;

/*
    Copyright (C) 2014-2023 Efe Zimmerlay

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

import com.youtube.search.Item;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import spicy.gui.music.MusicScreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 * Created by EfeZmrly on 16.06.2020.
 *
 * @author Efe Zimmerlay
 **/
public class LoadingThread extends Thread {

    private final Item sunglas;
    private Process currentProc;
    
    public LoadingThread(Item sunglas) {
        this.sunglas = sunglas;
    }

    @Override
    public void run() {
        try {
            MusicManager mgr = MusicManager.getInstance();
            Runtime rt = Runtime.getRuntime();
            File downloader = new File(MusicManager.getInstance().musicFolder, "youtube-dl.exe");
            System.out.println(String.format("%s indiriyom glas", sunglas.getSnippet().getTitle()));
            currentProc = rt.exec("\""+downloader.getAbsolutePath()+"\"  -ci -f \"worstaudio[ext=m4a]\" --audio-quality 1337 -o %(id)s.%(ext)s https://www.youtube.com/watch?v="+sunglas.getId().getVideoId(), null, new File(downloader.getParent()));

            // sungglas

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(currentProc.getInputStream()));

            String s = null;
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            //MusicManager.getInstance().loadingThread = null;
            //MusicManager.getInstance().play(track);

            mgr.currentItem = this.sunglas;
            Media hit = new Media(new File(mgr.musicFolder, sunglas.getId().getVideoId() + ".m4a").toURI().toString());
            mgr.mediaPlayer = new MediaPlayer(hit);
            mgr.mediaPlayer.setVolume(MusicScreen.volumeControl*20);
            mgr.mediaPlayer.setAutoPlay(true);
            /*mediaPlayer.setOnStopped(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Stopped");
                }
            });
            mediaPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Ready");
                }
            });
            mediaPlayer.setOnError(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Error");
                }
            });*/
            mgr.mediaPlayer.setOnEndOfMedia(mgr::next);

        } catch (Exception ex){
            ex.printStackTrace();
        }
        super.run();
    }

    @Override
    public void interrupt() {
        try {
            currentProc.destroy();
            currentProc.destroyForcibly();
            System.out.println("munglas: "+currentProc.isAlive());
        } catch (Exception ex){
            System.out.println(String.format("[SUNGLAS] %s", ex.getStackTrace()));
        }
        super.interrupt();
    }
}
