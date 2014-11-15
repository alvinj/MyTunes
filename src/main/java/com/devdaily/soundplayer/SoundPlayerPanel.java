package com.devdaily.soundplayer;

import java.awt.Color;
import java.awt.Font;
import java.util.Hashtable;

import javax.swing.*;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

public class SoundPlayerPanel
extends JPanel
{
  private JLabel headerLabel = new JLabel();
  private JLabel helpTextLabel = new JLabel();
  private JLabel filenameLabel = new JLabel();
  private JLabel timeCountdownLabel = new JLabel();
  
  private JTextField filenameField=new JTextField(20);
  private JButton startStopButton = new JButton("Start");
  private JButton browseForFileButton = new JButton("Browse ...");

  private JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 50);
  private SoundPlayerMainController controller;
  
  public SoundPlayerPanel(SoundPlayerMainController controller)
  {
    this.controller = controller;
    headerLabel.setText("Al's Sound Player");
    headerLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
    helpTextLabel.setText("Select a file, then click the Play button.");
    filenameLabel.setText("Filename:");
    filenameField.setText("");
    timeCountdownLabel.setForeground(Color.GRAY);
    configureVolumeSliderControl();
    buildMeditationPanel();
  }

  private void configureVolumeSliderControl() {
    volumeSlider.setToolTipText("Volume control");
    volumeSlider.addChangeListener(controller);
    volumeSlider.setMajorTickSpacing(10);
    volumeSlider.setMinorTickSpacing(2);
    volumeSlider.setPaintTicks(true);
    volumeSlider.setPaintLabels(true);
    //volumeSlider.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
    
    Hashtable labelTable = new Hashtable();
    labelTable.put( new Integer( 0 ), new JLabel("Quiet") );
    labelTable.put( new Integer( 100 ), new JLabel("Loud") );
    volumeSlider.setLabelTable( labelTable );
  }
  
  public JSlider getVolumeSlider() {
    return volumeSlider;
  }

  public JTextField getFilenameField()
  {
    return filenameField;
  }
  
  public JButton getStartStopButton()
  {
    return startStopButton;
  }
  
  public JButton getBrowseForFileButton()
  {
    return browseForFileButton;
  }
  
  public JLabel getTimeCountdownField()
  {
    return timeCountdownLabel;
  }
  
  /**
   * Use the Jgoodies FormLayout to create the panel.
   */
  private void buildMeditationPanel()
  {
//    FormLayout layout = new FormLayout("pref, 2dlu, pref, 2dlu, pref:grow",                      // columns
//                        "p,3dlu, p,9dlu, p,3dlu, p,9dlu, p,3dlu, fill:pref:grow,3dlu, p,9dlu");  // rows
    FormLayout layout = new FormLayout("pref, 2dlu, pref, 2dlu, pref:grow",
    "p,3dlu, p,9dlu, p,3dlu, p,9dlu, p,3dlu, fill:pref:grow,3dlu, p,9dlu");
    PanelBuilder builder = new PanelBuilder(layout, this);
    builder.setDefaultDialogBorder();

    CellConstraints cc = new CellConstraints();

    // row 1
    builder.add(headerLabel, cc.xyw(1,1,3));

    // row 3
    builder.add(helpTextLabel, cc.xyw(1,3,3,"left,top"));

    // row 5
    builder.add(filenameLabel, cc.xy(1,5));
    builder.add(filenameField, cc.xy(3,5));
    builder.add(browseForFileButton, cc.xy(5,5));
    filenameLabel.setLabelFor(filenameField);

    // row 7 (buttons)
    JButton[] buttons = {startStopButton};
    JPanel buttonPanel = ButtonBarFactory.buildLeftAlignedBar(buttons);
    builder.add(buttonPanel, cc.xy(3,7));
    
    // row 9 (slider control) looks better if i start in column 2.
    builder.add(volumeSlider, cc.xyw(2,9,4));
  }

}














