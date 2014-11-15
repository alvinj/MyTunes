package com.devdaily.soundplayer.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import com.devdaily.soundplayer.SoundPlayerMain;
import com.devdaily.soundplayer.SoundPlayerMainFrame;

public class QuitApplicationAction extends AbstractAction
{
  SoundPlayerMainFrame mainFrame;
  SoundPlayerMain controller;
  
  public QuitApplicationAction(SoundPlayerMain controller, SoundPlayerMainFrame frame, String name, Integer mnemonic)
  {
    super(name, null);
    this.controller = controller;
    this.mainFrame = frame;
    putValue(MNEMONIC_KEY, mnemonic);
  }

  public void actionPerformed(ActionEvent e)
  {
    controller.doQuitAction();
  }
}


