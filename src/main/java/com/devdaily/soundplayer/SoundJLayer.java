package com.devdaily.soundplayer;

import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.JavaSoundAudioDevice;

public class SoundJLayer extends JLayerPlayerPausable.PlaybackListener implements Runnable
{
  private String filePath;
  private JLayerPlayerPausable player;
  private Thread playerThread;  

  public SoundJLayer(String filePath)
  {
    this.filePath = filePath;
    // AJA - added
    playerInitialize();
  }

  public void pause()
  {
    this.player.pause();

    this.playerThread.stop();
    this.playerThread = null;
  }

  public void pauseToggle()
  {
    if (this.player.isPaused == true)
    {
      this.play();
    }
    else
    {
      this.pause();
    }
  }

  public void play()
  {
    if (this.player == null)
    {
      this.playerInitialize();
    }

    this.playerThread = new Thread(this, "AudioPlayerThread");
    this.playerThread.start();
  }

  // AJA - can only call after playerInitialize
  public JavaSoundAudioDevice getAudioDevice() {
    return this.player.getAudioDevice();
  }

  // AJA - made this public
  public void playerInitialize()
  {
    try
    {
      // AJA
      String urlAsString = new java.io.File(filePath).toURL().toString();
      this.player = new JLayerPlayerPausable
      (
        new java.net.URL(urlAsString),
        this 
      );
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  // PlaybackListener members

  public void playbackStarted(JLayerPlayerPausable.PlaybackEvent playbackEvent)
  {
    System.out.println("playbackStarted()");
  }

  public void playbackFinished(JLayerPlayerPausable.PlaybackEvent playbackEvent)
  {
    System.out.println("playbackEnded()");
  } 

  // IRunnable members

  public void run()
  {
    try
    {
      this.player.resume();
    }
    catch (javazoom.jl.decoder.JavaLayerException ex)
    {
      ex.printStackTrace();
    }

  }
}



