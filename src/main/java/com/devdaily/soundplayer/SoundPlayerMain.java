package com.devdaily.soundplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.apple.eawt.Application;
import com.devdaily.swingutils.SwingUtils;

/**
 * This is the "Main" class for your application.
 */
public class SoundPlayerMain implements MacOSXApplicationInterface
{
  SoundPlayerMainFrame mainFrame;
  Application macApplication;
  MacOSXApplicationAdapter macAdapter;
  DockBarAdapter dockBarAdapter;
  
  // the ui
  private SoundPlayerMainController soundPlayerPanelController;
  SoundPlayerPanel soundPlayerPanel;

  // for debugging
  private static final String DEBUG_FILENAME = "SoundPlayerDebug.txt";
  private String homeDir;
  private String canonDebugFilename;
  private File debugFile;
  private PrintWriter debugFileWriter;

  public static void main(String[] args)
  {
    new SoundPlayerMain();
  }
  
  public SoundPlayerMain()
  {
    System.setProperty("apple.awt.graphics.EnableQ2DX","true");
    createDebugFileWriter();
    getDebugFileWriter().print("APP WAS STARTED");
    getDebugFileWriter().flush();
    configureMacOsXStuff();
    createTheUI();
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          mainFrame.setLocationRelativeTo(null);
          mainFrame.setResizable(false);
          mainFrame.setVisible(true);
        }
        catch (Exception exception)
        {
          exception.printStackTrace();
        }
      }
    });
  }

  private void configureMacOsXStuff() {
    macApplication = Application.getApplication();
    configureMacDockAdapter(macApplication);
    configureOSXAboutPreferencesAndQuit(macApplication);
  }

  private void createTheUI() {
    soundPlayerPanelController = new SoundPlayerMainController(this);
    soundPlayerPanel = soundPlayerPanelController.createSoundPlayerPanel();
    mainFrame = new SoundPlayerMainFrame(this, soundPlayerPanel);
    soundPlayerPanelController.initializeFileMenu();
  }

  private void createDebugFileWriter()
  {
    homeDir = System.getProperty("user.home");
    canonDebugFilename = homeDir + System.getProperty("file.separator") + DEBUG_FILENAME;
    debugFile = new File(canonDebugFilename);
    try
    {
      debugFileWriter = new PrintWriter(debugFile);
    }
    catch (FileNotFoundException e)
    {
      // just going to ignore this one; if we can't write a debug file,
      // we can't write.
    }
  }
  
  public PrintWriter getDebugFileWriter()
  {
    return this.debugFileWriter;
  }
  
  public JFrame getMainFrame() {
    return mainFrame;
  }

  //--------------------------- Generic OS X stuff -----------------------------

  private void configureOSXAboutPreferencesAndQuit(Application theApplication) {
    macAdapter = new MacOSXApplicationAdapter(this);
    theApplication.addApplicationListener(macAdapter);
    // must enable the preferences option manually
    //theApplication.setEnabledPreferencesMenu(true);
  }

  private void configureMacDockAdapter(Application theApplication) {
    dockBarAdapter = new DockBarAdapter(this);
    theApplication.addApplicationListener(dockBarAdapter);
  }

  public void doAboutAction()
  {
    // TODO implement (or not)
  }

  public void doPreferencesAction()
  {
    // TODO implement (or not)
    // TODO if you implement this, you need to un-comment the "setEnabledPreferencesMenu" line in the main method
    //JOptionPane.showMessageDialog(null, "Sorry, no preferences at this time.");
  }

  public void doQuitAction()
  {
    SwingUtils.sleep(250);
    System.exit(0);
  }

  /**
   * Handle the case where the user has selected a recently-opened file.
   * The ActionEvent should have the filename in it.
   * @param event
   */
  public void handleFileMenuItemSelected(ActionEvent event) {
    String filename = event.getActionCommand();
    soundPlayerPanelController.setFilenameTextField(filename);
  }
  
  // a pass-thru method
  void addItemsToFileMenu(java.util.List<String> items) {
    mainFrame.setFileMenuItems(items);
  }

    

}















