package com.devdaily.soundplayer;

import javax.swing.JLabel;

public class ThreadedCountdownField extends Thread
{
  private SoundPlayerMainController ourController;
  private JLabel timeRemainingLabel;
  private long timeRemaining;

  /**
   * @param meditation Our controller class, needed for a callback to doEndOfTimerAction().
   * @param timeLabel The JLabel we will update.
   * @param timeToSleep Time to sleep, in seconds.
   */
  ThreadedCountdownField(SoundPlayerMainController meditation, JLabel timeLabel, long timeToSleep)
  {
    this.ourController = meditation;
    this.timeRemainingLabel = timeLabel;
    timeRemaining = timeToSleep;
  }

  public void run()
  {
    // TODO test, is this running on the EDT? should be, it is triggered from the start button
    
    while (timeRemaining > 0)
    {
      timeRemainingLabel.setText("time to sleep: " + Long.toString(timeRemaining) + " seconds");
      try
      {
        Thread.sleep(1000);
      }
      catch (InterruptedException e)
      {
        if (ourController.getDebugFileWriter()!=null) e.printStackTrace(ourController.getDebugFileWriter());
      }
      timeRemaining--;
    }

    timeRemainingLabel.setText("");
    //ourController.doEndOfTimerAction();
  }

}



