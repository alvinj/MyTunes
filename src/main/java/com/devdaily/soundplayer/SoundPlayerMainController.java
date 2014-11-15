package com.devdaily.soundplayer;

import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javazoom.jlgui.basicplayer.BasicPlayerException;

import com.devdaily.swingutils.SwingUtils;
import com.devdaily.utils.DDFileUtils;

public class SoundPlayerMainController implements ChangeListener {
  
  SoundPlayerMain mainController;
  
  // soundplayer panel references
  SoundPlayerPanel soundPlayerPanel;
  private JButton startStopButton;
  private JButton browseForFileButton;
  private JTextField filenameTextField;
  
  private static final String BUTTON_START_TEXT = "Play";
  private static final String BUTTON_STOP_TEXT  = "Stop";
  
  ThreadedCountdownField tcf;
  
  // file chooser stuff
  String currentDirectory = "";
  String currentFilename; // full path with filename. null means new/untitled.
  JFileChooser jFileChooser1 = new JFileChooser();
  
  ActualSoundPlayer actualSoundPlayer;
  
  // recent files stuff
  private static final String RECENT_FILE_LIST_FILENAME = ".soundPlayerRecentFilelist";
  private SortedSet<String> recentFiles = new TreeSet<String>();
  

  //----------------------------- RECENT FILES ------------------------------
  
  private String getRecentlyOpenedFilesFilename() {
    String homeDir = System.getProperty("user.home");
    String fileSep = System.getProperty("file.separator");
    return homeDir + fileSep + RECENT_FILE_LIST_FILENAME;
  }
  
  private void addToListOfRecentlyOpenedFiles(String newCanonFilename) {
    recentFiles.add(newCanonFilename);
    updateRecentFilesList();
  }

  /**
   * Write our recent files list out to disk.
   */
  private void updateRecentFilesList() {
    List<String> files = new ArrayList<String>(recentFiles);
    String output = convertStringListToStringsOnSeparateLines(files);
    try {
      DDFileUtils.writeFile(getRecentlyOpenedFilesFilename(), output);
      populateRecentFilesListFromDisk();
    } catch (IOException e) {
      // TODO ignoring this for now
    }
  }
  
  private String convertStringListToStringsOnSeparateLines(List<String> stringsAsList) {
    StringBuilder sb = new StringBuilder();
    for (String s: stringsAsList) {
      sb.append(s + "\n");
    }
    return sb.toString();
  }
  
  /**
   * Reads the list of recently-opened files that we have stored on the user's disk.
   */
  private void populateRecentFilesListFromDisk() {
    List<String> recentFileList = new ArrayList<String>(); 
    try {
      recentFileList = DDFileUtils.readFileAsListOfStrings(getRecentlyOpenedFilesFilename());
      recentFiles.addAll(recentFileList);
      //System.err.println(recentFileList);
      updateUiWithRecentlyOpenedFileList(recentFileList);
    } catch (Exception e) {
      // TODO ignoring this for now
    }
  }

  private void updateUiWithRecentlyOpenedFileList(final List<String> recentFileListForUI) {
    SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          mainController.addItemsToFileMenu(recentFileListForUI);
        }
        catch (Exception exception)
        {
          // ignore
        }
      }
    });
  }

  //----------------------------- MAIN CONTROLLER STUFF ------------------------------
  
  public SoundPlayerMainController(SoundPlayerMain mainController) {
    this.mainController = mainController;
  }

  public SoundPlayerPanel createSoundPlayerPanel()
  {
    soundPlayerPanel = new SoundPlayerPanel(this);
    browseForFileButton = soundPlayerPanel.getBrowseForFileButton();
    startStopButton = soundPlayerPanel.getStartStopButton();
    filenameTextField = soundPlayerPanel.getFilenameField();
    
    // make sure the button starts with our start text
    startStopButton.setText(BUTTON_START_TEXT);
    
    addListenersToWidgets();
    return soundPlayerPanel;
  }
  
  private void addListenersToWidgets()
  {
    // [enter] on this field should be just like clicking the start button
    filenameTextField.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doStartButtonClickedAction();
      } 
    });

    browseForFileButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        doBrowseForFileAction();
      } 
    });

    // handle start/stop actions
    startStopButton.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        String currentButtonText = startStopButton.getText();
        if (currentButtonText.toUpperCase().equals(BUTTON_START_TEXT.toUpperCase()))
        {
          doStartButtonClickedAction();
        }
        else
        {
          doStopButtonClickedAction();
        }
      } 
    });
  }

  /**
   * What we do when the user clicks the "browse" button.
   */
  private void doBrowseForFileAction() {
    String tmpFilename = getUserSelectedFilename();
    if (tmpFilename ==null) return;
    currentFilename = tmpFilename;
    filenameTextField.setText(currentFilename);
  }
  
  private String getUserSelectedFilename() {
    FileDialog fileDialog = SwingUtils.letUserChooseFile(mainController.getMainFrame(), currentDirectory);
    return SwingUtils.getCanonicalFilenameFromFileDialog(fileDialog);
  }
  
  /**
   * Play the sound file when the start button is clicked.
   */
  private void doStartButtonClickedAction()
  {
    if (currentFilename == null || currentFilename.trim().equals("")) {
      JOptionPane.showMessageDialog(mainController.getMainFrame(), "Please select a filename.");
      return;
    }
    addToListOfRecentlyOpenedFiles(currentFilename);
    startStopButton.setText(BUTTON_STOP_TEXT);
    actualSoundPlayer = new ActualSoundPlayer(this, currentFilename);
    try {
      actualSoundPlayer.play();
    } catch (Exception e) {
      getDebugFileWriter().append(e.getMessage());
      SwingUtils.displayErrorMessage(mainController.getMainFrame(), e.getMessage());
    }
  }
  
  /**
   * Things to do when the Stop button is clicked.
   */
  void doStopButtonClickedAction()
  {
    startStopButton.setText(BUTTON_START_TEXT);
    try {
      actualSoundPlayer.pause();
    } catch (Throwable t) {
      getDebugFileWriter().append(t.getMessage());
      SwingUtils.displayErrorMessage(mainController.getMainFrame(), t.getMessage());
    }
    filenameTextField.requestFocus();
  }
  
  public PrintWriter getDebugFileWriter()
  {
    return mainController.getDebugFileWriter();
  }

  //------------------------------ volume slider control ----------------------------
  
  /**
   * This method gets callbacks whenever the JSlider control is adjusted.
   */
  @Override
  public void stateChanged(ChangeEvent event) {
    JSlider source = (JSlider)event.getSource();
    if (!source.getValueIsAdjusting()) {
      double gain = 0;
      int volume = (int)source.getValue();
      if (volume == 0) {
        gain = 0;
      } else {
        gain = volume / 100.0;
      }
      setGain(gain);
    }
  }
  
  private void setGain(double gain) {
    if (actualSoundPlayer != null) {
      try {
        actualSoundPlayer.setGain(gain);
      } catch (BasicPlayerException e) {
        // ignoring this
      }
    }
  }

  public void setFilenameTextField(String filename) {
    filenameTextField.setText(filename);
    currentFilename = filename;
  }

  public void initializeFileMenu() {
    populateRecentFilesListFromDisk();
  }

  
}
























