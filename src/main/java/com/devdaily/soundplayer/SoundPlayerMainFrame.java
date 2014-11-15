package com.devdaily.soundplayer;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import com.sun.awt.AWTUtilities;

public class SoundPlayerMainFrame extends JFrame {
  
  // our controller
  SoundPlayerMain controller;
  
  private SoundPlayerPanel meditationPanel;
  JMenuBar menuBar = new JMenuBar();
  JMenu fileMenu = new JMenu("File");

  // supported actions
  private Action quitAction;

  // keystrokes for actions
  private KeyStroke quitKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.META_MASK);
  
  /**
   * Use this constructor for re-creating existing windows (and therefore you don't need to 
   * set a background color, it should already have one).
   */
  public SoundPlayerMainFrame(SoundPlayerMain controller, SoundPlayerPanel meditationPanel) {
    this.controller = controller;
    this.meditationPanel = meditationPanel;
    configureActionsAndKeystrokes();
    menuBar = initializeMenuBar();
    setJMenuBar(menuBar);
    //AWTUtilities.setWindowOpacity(SoundPlayerMainFrame.this, 0.98f);
    buildFrame();
  }
  
  private void buildFrame() {
    getContentPane().add(meditationPanel,BorderLayout.CENTER);
    pack();
  }
  
  private JMenuBar initializeMenuBar()
  {
    menuBar.add(fileMenu);
    return menuBar;
  }
  
  void setFileMenuItems(java.util.List<String> items) {
    fileMenu.removeAll();
    for (String s: items) {
      JMenuItem jmi = new JMenuItem(s);
      jmi.setActionCommand(s);
      jmi.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          doFileMenuItemSelected(event);
        }
      });
      fileMenu.add(jmi);
    }
  }

  private void doFileMenuItemSelected(ActionEvent event) {
    controller.handleFileMenuItemSelected(event);
  }

  private void configureActionsAndKeystrokes()
  {
//    quitAction = new QuitApplicationAction(controller, this, "Quit", quitKeystroke.getKeyCode());
//    getEditorPane().getInputMap().put(quitKeystroke, "quitKeystroke");
//    getEditorPane().getActionMap().put("quitKeystroke", quitAction);
  }



}
















