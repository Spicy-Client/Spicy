package spicy.music;

import com.google.gson.Gson;
import com.youtube.search.Item;
import com.youtube.WebUtils;
import com.youtube.search.Search;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import spicy.gui.music.MusicScreen;

import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MusicManager {
    private static MusicManager instance;
    private HashMap<Integer, ResourceLocation> artsLocations = new HashMap<>();
    public Item currentItem = null;
    public String api_key = "AIzaSyDWNpBQGALJR6Ny3JuQc86BRr_-LXlWEmo";
    private ArrayList<Item> lastResearch = null;
    public MediaPlayer mediaPlayer;
    public File musicFolder;
    public Thread loadingThread = null;
    private ArrayList<Item> playlist = new ArrayList<>();
    private Process currentProc;

    public MusicManager() {
        musicFolder = new File(Minecraft.getMinecraft().mcDataDir.toString() + File.separator + "Spicy" + File.separator + "music");
        if (!musicFolder.exists()) {
            musicFolder.mkdirs();
        } /*TODO stream ile çalınca bunu tamamen sil else {
            for (File f : musicFolder.listFiles()) {
                if (f.getName().endsWith(".mp3") || f.getName().endsWith(".mp3.part")) {
                    f.delete();
                }
            }
        }*/

        try {
            File downloader = new File(musicFolder, "youtube-dl.exe");
            if (!downloader.exists()) {
                InputStream link = (getClass().getResourceAsStream("/assets/minecraft/spicy/youtube-dl.exe"));
                Files.copy(link, downloader.getAbsoluteFile().toPath());
            }
        } catch (Exception ignored){}

        final CountDownLatch latch = new CountDownLatch(1);
        SwingUtilities.invokeLater(() -> {
            new JFXPanel(); // initializes JavaFX environment
            latch.countDown();
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void addToPlaylist(Item track) {
        for (Item t : playlist) {
            if (t.getId().getVideoId().equals(track.getId().getVideoId())) {
                return;
            }
        }
        playlist.add(track);
    }

    public void removeFromPlaylist(Item track) {
        playlist.remove(track);
    }
    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public List<Item> search(String q, String limit) {
        Gson gson = new Gson();
        Search request = gson.fromJson(WebUtils.visitSite("https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&q=" + q.replace(" ", "%20") + "&type=video&key="+api_key), Search.class);

        ArrayList<Item> tracks = new ArrayList<>(request.getItems());

        this.lastResearch = tracks;
        return tracks;
    }

    public void play(Item track) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        File file = new File(musicFolder, track.getId().getVideoId() + ".m4a");
        if (!file.exists()) {
            if(this.currentItem != null && this.currentItem.getId().getVideoId().equals(track.getId().getVideoId())){
                System.out.println("asd");
                return;
            }
            if (loadingThread != null) {
                loadingThread.interrupt();
            }
            this.currentItem = track;
            (loadingThread = new LoadingThread(track)).start();

            /*loadingThread = new Thread(() -> {
                AudioInputStream din = null;
                try {
                    /*File downloader = new File(musicFolder, "youtube-dl.exe");
                    if (!downloader.exists()) {
                        InputStream link = (getClass().getResourceAsStream("/assets/minecraft/lctr/youtube-dl.exe"));
                        Files.copy(link, downloader.getAbsoluteFile().toPath());
                    }
                    // youtube-dl.exe artık wra misali
                    Runtime rt = Runtime.getRuntime();
                    Process proc = rt.exec("\""+downloader.getAbsolutePath()+"\" -ci -f \"bestaudio[ext=m4a]\" https://www.youtube.com/watch?v="+track.getId().getVideoId() + " -o -", null, new File(downloader.getParent()));

                        // sungglas

                        BufferedReader stdInput = new BufferedReader(new
                                InputStreamReader(proc.getInputStream()));

                        String s = null;
                        while ((s = stdInput.readLine()) != null && (s.contains("[youtube]") || s.contains("[info]"))) {
                            System.out.println(s);
                        }*/
                    /*String s = WebUtils.visitSite("https://www.convertmp3.io/fetch/?format=text&video=https://www.youtube.com/watch?v="+track.getId().getVideoId());
                    System.out.println(s);
                    if(s.contains("meta")) return;
                    String url = s.split("Link: ")[1];
                    System.out.println(url);

                    InputStream stream = new URL(url).openStream();
                    InputStream bufferedStream = new BufferedInputStream(stream);
                    AudioInputStream in = AudioSystem.getAudioInputStream(bufferedStream);
                    AudioFormat baseFormat = in.getFormat();
                    AudioFormat decodedFormat = new AudioFormat(
                            AudioFormat.Encoding.PCM_SIGNED,
                            baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                            baseFormat.getChannels() * 2, baseFormat.getSampleRate(),
                            false);
                    din = AudioSystem.getAudioInputStream(decodedFormat, in);
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
                    SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                    if(line != null) {
                        int bufferSize = 4096;
                        line.open(decodedFormat, bufferSize);
                        line.start();

                        ((FloatControl) line.getControl(FloatControl.Type.MASTER_GAIN)).setValue(1.f);

                        byte[] buffer = new byte[bufferSize];
                        java.util.Timer timer = new java.util.Timer(true);
                        AudioInputStream finalDin = din;
                        timer.scheduleAtFixedRate(new TimerTask() {
                            int nBytesRead;
                            @Override
                            public void run() {
                                //if (this.) {
                                //    line.stop();
                                //    return;
                                //}
                                line.start();
                                try {
                                    if ((nBytesRead = finalDin.read(buffer, 0, buffer.length)) != -1) {
                                        line.write(buffer, 0, nBytesRead);
                                    }

                                    if (line.getLongFramePosition() == in.getFrameLength()) {
                                        line.drain();
                                        line.stop();
                                        line.close();
                                        finalDin.close();
                                        next();
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }, 0, 1);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
                loadingThread = null;
            });

            loadingThread.start();*/
                    /*
            loadingThread = new Thread(() -> {
                try {
                    File downloader = new File(musicFolder, "youtube-dl.exe");
                    if (!downloader.exists()) {
                        InputStream link = (getClass().getResourceAsStream("/assets/minecraft/lctr/youtube-dl.exe"));
                        Files.copy(link, downloader.getAbsoluteFile().toPath());
                    }
                    // youtube-dl.exe artık wra misali

                    Runtime rt = Runtime.getRuntime();
                    currentProc = rt.exec("\""+downloader.getAbsolutePath()+"\"  -ci -f \"worstaudio[ext=m4a]\" --audio-quality 1337 -o %(id)s.%(ext)s https://www.youtube.com/watch?v="+track.getId().getVideoId(), null, new File(downloader.getParent()));

                    // sungglas

                    BufferedReader stdInput = new BufferedReader(new
                            InputStreamReader(currentProc.getInputStream()));

                    String s = null;
                    while ((s = stdInput.readLine()) != null) {
                        System.out.println(s);
                    }

                    loadingThread = null;
                    play(track);

                } catch (Exception ex){
                    ex.printStackTrace();
                }
                loadingThread = null;
            });
            loadingThread.start();*/
            /*
            if (loadingThread != null) {
                loadingThread.interrupt();
            }
            loadingThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String s = WebUtils.visitSite("https://www.convertmp3.io/fetch/?format=text&video=https://www.youtube.com/watch?v="+track.getId().getVideoId());
                    System.out.println(s);
                    if(s.contains("meta")) return;
                    String url = s.split("Link: ")[1];
                    System.out.println(url);
                    File tempFile = new File(musicFolder, track.getId().getVideoId() + ".mp3.part");
                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                        HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                        byte[] buff = new byte[1024];
                        int readed = 0;
                        while ((readed = con.getInputStream().read(buff)) > 0) {
                            fos.write(buff, 0, readed);
                        }
                        fos.close();
                        Files.copy(tempFile.toPath(), file.toPath());
                        tempFile.delete();
                        play(track);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    loadingThread = null;
                }
            });
            loadingThread.start();
            /*loadingThread = new Thread(() -> {
                try {
                    String line;
                    String s = WebUtils.visitSite("https://www.convertmp3.io/fetch/?format=text&video=https://www.youtube.com/watch?v="+track.getId().getVideoId());
                    if(s.contains("meta")){
                        return;
                    }
                    System.out.println(s.split("Link: ")[1]);
                    URL urlobj = new URL(s.split("Link: ")[1]);
                    String length = s.split("Length: ")[1];
                    String realLength = length.split(" <br")[0];
                    //ystem.out.println("Length: " + realLength);
                    //MusicScreen.currentSongLength = Integer.valueOf(realLength);


                    HttpURLConnection connection = (HttpURLConnection)urlobj.openConnection();
                    connection.addRequestProperty(agent1, agent2);
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    String url = connection.getURL().toString();
                    File tempFile = new File(musicFolder, track.getId().getVideoId() + ".mp3.part");
                    FileOutputStream fos = new FileOutputStream(tempFile);

                    HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();
                    byte[] buff = new byte[1024];
                    int readed = 0;
                    while ((readed = con.getInputStream().read(buff)) > 0) {
                        fos.write(buff, 0, readed);
                    }
                    fos.close();

                    Files.copy(tempFile.toPath(), file.toPath());
                    tempFile.delete();
                    play(track);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    loadingThread = null;
                    return;
                }
                loadingThread = null;
            });
            loadingThread.start();*/

        } else {
            this.currentItem = track;
            Media hit = new Media(file.toURI().toString());
            mediaPlayer = new MediaPlayer(hit);
            mediaPlayer.setVolume(MusicScreen.volumeControl);
            mediaPlayer.setAutoPlay(true);
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
            mediaPlayer.setOnEndOfMedia(this::next);
        }
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public Thread getLoadingThread() {
        return loadingThread;
    }

    public void next() {
        if (!playlist.isEmpty()) {
            if (currentItem == null) {
                play(playlist.get(0));
                return;
            } else {
                boolean playNext = false;
                for (Item t : playlist) {
                    if (playNext) {
                        play(t);
                        return;
                    } else if (t.getId().getVideoId() == currentItem.getId().getVideoId()) {
                        playNext = true;
                    }
                }
            }
        }
        if (lastResearch != null && !lastResearch.isEmpty()) {
            if (currentItem == null) {
                play(lastResearch.get(0));
            } else {
                boolean playNext = false;
                for (Item t : lastResearch) {
                    if (playNext) {
                        play(t);
                        break;
                    } else if (t.getId().getVideoId() == currentItem.getId().getVideoId()) {
                        playNext = true;
                    }
                }
            }
        }
    }

    public void prev() {
        if (!playlist.isEmpty()) {
            if (currentItem == null) {
                play(playlist.get(0));
                return;
            } else {
                Item lastItem = null;
                for (Item t : playlist) {
                    if (!(t.getId().getVideoId() == currentItem.getId().getVideoId())) {
                        lastItem = t;
                    } else if(lastItem != null)play(lastItem);
                }
            }
        }
        if (lastResearch != null && !lastResearch.isEmpty()) {
            if (currentItem == null) {
                play(lastResearch.get(0));
            } else {
                Item lastItem = null;
                for (Item t : lastResearch) {
                    if (!(t.getId().getVideoId() == currentItem.getId().getVideoId())) {
                        lastItem = t;
                    } else if(lastItem != null)play(lastItem);
                }
            }
        }
    }

    public List<Item> getPlaylist() {
        return playlist;
    }
}
