package clans;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

/**
 * Creates and manages the CLANS graphical user interface (GUI).
 * 
 * TODO: Currently this class also contains computational code that should eventually be refactored to its own classes.
 */
public class ClusteringWithGui extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1310615823184297259L;

	/**
	 * The default labels for the start/stop/resume button so that they are consistent.
	 */
	enum ButtonStartStopMessage {
		START("Start run"), RESUME("Resume"), STOP("Stop");

		private String value;

		private ButtonStartStopMessage(String value) {
			this.value = value;
		}

		protected String get() {
			return value;
		}
	}

	/**
	 * Invokes an autosave every {@code data.getAutosaveIntervalMinutes())} minutes, if autosaving is enabled.
	 */
	private Timer autosaveTimer; // if autosaving is enabled this timer executes the autosave after the set interval
	/**
	 * If true, postpones the autosave setup to after the first successful save operation. This is necessary as
	 * sometimes, no filename is associated with {@data}, e.g. when coming directly from parsing BLAST result.
	 */
	private boolean autosaveAutoReenable;

	/**
	 * Runs the load/save operations in their own background thread while keeping the GUI responsive.
	 */
	private SwingWorker<Void, Integer> saveLoadWorker;
	
	/**
	 * If true, disables almost all mouse events as their listeners check this variable. This is used in @{code
	 * disableUserControls()} and {@code enableUserControls()} to block and reenable user input during, e.g., loading
	 * and saving.
	 */
	private boolean mouseEventsDisabled;
	/**
	 * Listens for escape key presses during long running operations and cancels them if the key is pressed. Currently
	 * used during loading and saving.
	 */
	private KeyListener cancelWorkInProgress;

	/**
	 * Creates the GUI.
	 * 
	 * @param data
	 *            The input data. Can be "empty" (i.e. without real data) to start the GUI without immediately loading a
	 *            file.
	 */
	public ClusteringWithGui(ClusterData data) {

		this.data = data;
		
		// closing events are handled in a WindowListener with custom windowClosing method
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		draw1 = new drawpanel();
		
		initializeComputationThread();

		initComponents();

		consumeDataAtConstruction();

		textfield_threshold_value.setText(String.valueOf(data.pvalue_threshold));
		textfield_info_min_blast_evalue.setText(String.valueOf(data.pvalue_threshold));

		// now initialize the stuff
		mousestart[0] = 0;
		mousestart[1] = 0;
		mousemove[0] = 0;
		mousemove[1] = 0;
		draw1.init();

		// and now initialize a run
		resetGraph();

		set_selection_button_label();

		if (data.errbuff.length() > 0) {
			// If I have had errors up to this point
			new errwindow(this, true, data.errbuff.toString()).setVisible(true);
		}

		if (data.getAbsoluteInputfileName() != null) {
			loadDataClansFormatInBackground(data.getAbsoluteInputfileName());
		}

		if (new File(data.getIntermediateResultfileName()).canRead()) {
			System.out.println("reading former data");
			readsave.parse_intermediate_results(data);
			data.posarr = data.myposarr;
		}
	}
	
	/**
	 * Creates a new instance of the computation thread and names it "computation thread".
	 * <p>
	 * Note: Named threads are easier to debug! On a command line run command "jps -l" to get the programs process id
	 * (first column). Then run "jstack <process id>" to see all threads of your process.
	 */
	private void initializeComputationThread() {
		mythread = new computethread(this);
	}

	/**
	 * Initializes the GUI elements. This is called from the constructor.
	 */ 
	private void initComponents() {
		graphpanel = new javax.swing.JPanel();
		buttonpanel = new javax.swing.JPanel();
		drawbuttonpanel = new javax.swing.JPanel();
		button_initialize = new javax.swing.JButton();
		button_start_stop_resume = new javax.swing.JButton();
		button_show_selected = new javax.swing.JButton();
		button_select_move = new javax.swing.JToggleButton();
		button_cutoff_value = new javax.swing.JButton();
		textfield_threshold_value = new javax.swing.JTextField();
		textfield_info_min_blast_evalue = new javax.swing.JTextField();
		button_select_all_or_clear = new javax.swing.JButton();
		checkbox_show_names = new javax.swing.JCheckBox();
		checkbox_show_numbers = new javax.swing.JCheckBox();
		checkbox_show_connections = new javax.swing.JCheckBox();
		button_zoom_on_selected = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		menu_file = new javax.swing.JMenu();
		loadmenuitem = new javax.swing.JMenuItem();
		savemenuitem = new javax.swing.JMenuItem();
		saveasmenuitem = new javax.swing.JMenuItem();
		saveattvalsmenuitem = new javax.swing.JMenuItem();
		addseqsmenuitem = new javax.swing.JMenuItem();
		savemtxmenuitem = new javax.swing.JMenuItem();
		save2dmenuitem = new javax.swing.JMenuItem();
		printmenuitem = new javax.swing.JMenuItem();
		loadalternatemenuitem = new javax.swing.JMenuItem();
		loadtabsmenuitem = new javax.swing.JMenuItem();
		loadgroupsmenuitem = new javax.swing.JMenuItem();
		menu_misc = new javax.swing.JMenu();
		getseqsmenuitem = new javax.swing.JMenuItem();
		hidesingletonsmenuitem = new javax.swing.JMenuItem();
		remove_selected_sequences_menu_item = new javax.swing.JMenuItem();
		getchildmenuitem = new javax.swing.JMenuItem();
		getparentmenuitem = new javax.swing.JMenuItem();
		setrotmenuitem = new javax.swing.JMenuItem();
		attvalcompcheckbox = new javax.swing.JCheckBoxMenuItem();
		moveselectedonly = new javax.swing.JCheckBoxMenuItem();
		cluster2dbutton = new javax.swing.JCheckBoxMenuItem();
		rescalepvaluescheckbox = new javax.swing.JCheckBoxMenuItem();
		messageOverlayActiveCheckbox = new javax.swing.JCheckBoxMenuItem();
		autosaveSetup = new javax.swing.JMenuItem();
		skipdrawingrounds = new javax.swing.JMenuItem();
		menu_draw = new javax.swing.JMenu();
		changefontmenuitem = new javax.swing.JMenuItem();
		getdotsizemenuitem = new javax.swing.JMenuItem();
		getovalsizemenuitem = new javax.swing.JMenuItem();
		changecolormenuitem = new javax.swing.JMenuItem();
		changefgcolormenuitem = new javax.swing.JMenuItem();
		changebgcolormenuitem = new javax.swing.JMenuItem();
		changeselectcolormenuitem = new javax.swing.JMenuItem();
		changenumbercolor = new javax.swing.JMenuItem();
		changeblastcolor = new javax.swing.JMenuItem();
		lengthcolormenuitem = new javax.swing.JCheckBoxMenuItem();
		colorfrustrationcheckbox = new javax.swing.JCheckBoxMenuItem();
		showorigcheckbox = new javax.swing.JCheckBoxMenuItem();
		showinfocheckbox = new javax.swing.JCheckBoxMenuItem();
		shownamesselectcheckbox = new javax.swing.JCheckBoxMenuItem();
		showblasthitnamescheckbox = new javax.swing.JCheckBoxMenuItem();
		zoommenuitem = new javax.swing.JMenuItem();
		centermenuitem = new javax.swing.JMenuItem();
		antialiasingcheckboxmenuitem = new javax.swing.JCheckBoxMenuItem();
		stereocheckboxmenuitem = new javax.swing.JCheckBoxMenuItem();
		stereoanglemenuitem = new javax.swing.JMenuItem();
		menu_windows = new javax.swing.JMenu();
		showoptionsmenuitem = new javax.swing.JMenuItem();
		sequencesitem = new javax.swing.JMenuItem();
		evalueitem = new javax.swing.JMenuItem();
		getblasthitsmenuitem = new javax.swing.JMenuItem();
		clustermenuitem = new javax.swing.JMenuItem();
		getseqsforselectedhits = new javax.swing.JMenuItem();
		seqscoloring = new javax.swing.JMenuItem();
		showseqsmenuitem = new javax.swing.JMenuItem();
		rotationmenuitem = new javax.swing.JMenuItem();
		affymenuitem = new javax.swing.JMenuItem();
		mapmanmenuitem = new javax.swing.JMenuItem();
		taxonomymenuitem = new javax.swing.JMenuItem();
		menu_help = new javax.swing.JMenu();
		aboutmenuitem = new javax.swing.JMenuItem();
		helpmenuitem = new javax.swing.JMenuItem();

		setTitle("3D-View");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				openReallyExitSafetyDialog();
			}
		});

		graphpanel.setPreferredSize(new java.awt.Dimension(640, 480));
		graphpanel.add(draw1);
		graphpanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				graphpanelMouseDragged(evt);
			}
		});
		graphpanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				graphpanelMousePressed(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				graphpanelMouseReleased(evt);
			}
		});
		graphpanel.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
			public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
			}

			public void ancestorResized(java.awt.event.HierarchyEvent evt) {
				requestRepaint();
			}
		});
		graphpanel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				zoomUsingMouseWheel(evt);
			}
		});
		graphpanel.setLayout(new javax.swing.BoxLayout(graphpanel, javax.swing.BoxLayout.LINE_AXIS));
		getContentPane().add(graphpanel, java.awt.BorderLayout.CENTER);

		buttonpanel.setLayout(new java.awt.GridLayout(1, 0));

		drawbuttonpanel.setLayout(new java.awt.GridLayout(0, 4));

		button_initialize.setText("Initialize");
		button_initialize.setToolTipText("Initialize the graph positions");
		button_initialize.setMnemonic(KeyEvent.VK_I);
		button_initialize.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				initializeGraphPositions();
			}
		});
		drawbuttonpanel.add(button_initialize);

		modifyButtonStartStopResume(ButtonStartStopMessage.STOP, true);
		button_start_stop_resume.setToolTipText("start/resume/stop the current run");
		button_start_stop_resume.setMnemonic(KeyEvent.VK_S);
		button_start_stop_resume.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				toggleComputationRunning();
			}
		});
		drawbuttonpanel.add(button_start_stop_resume);

		button_show_selected.setText("Show selected");
		button_show_selected.setMnemonic(KeyEvent.VK_O);
		button_show_selected.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openShowSelectedSequencesWindow();
			}
		});
		drawbuttonpanel.add(button_show_selected);

		button_select_move.setToolTipText("Toggles between moving the coordinate system and selecting sequences");
		button_select_move.setMnemonic(KeyEvent.VK_V);
		button_select_move.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateSelectMoveButtonLabel();
			}
		});
		updateSelectMoveButtonLabel();
		drawbuttonpanel.add(button_select_move);

		button_cutoff_value.setText("Use p-values better than:");
		button_cutoff_value.setMnemonic(KeyEvent.VK_B);
		button_cutoff_value.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				buttonSetThresholdPressed();
			}
		});
		drawbuttonpanel.add(button_cutoff_value);

		textfield_threshold_value.setText("1");
		textfield_threshold_value.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				confirmedThresholdTextfieldValue();
			}
		});
		drawbuttonpanel.add(textfield_threshold_value);

		textfield_info_min_blast_evalue.setEditable(false);
		textfield_info_min_blast_evalue.setText("1");
		textfield_info_min_blast_evalue.setToolTipText("evalue limit used for blast");
		drawbuttonpanel.add(textfield_info_min_blast_evalue);

		set_selection_button_label();
		button_select_all_or_clear.setMnemonic(KeyEvent.VK_A);
		button_select_all_or_clear.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				button_clear_selectionActionPerformed();
			}
		});
		drawbuttonpanel.add(button_select_all_or_clear);

		checkbox_show_names.setText("show names");
		checkbox_show_names.setToolTipText("show sequence names in graph");
		checkbox_show_names.setMnemonic(KeyEvent.VK_N);
		checkbox_show_names.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				requestRepaint();
			}
		});
		drawbuttonpanel.add(checkbox_show_names);

		checkbox_show_numbers.setText("show numbers");
		checkbox_show_numbers.setToolTipText("show sequence numbers in graph");
		checkbox_show_numbers.setMnemonic(KeyEvent.VK_U);
		checkbox_show_numbers.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				requestRepaint();
			}
		});
		drawbuttonpanel.add(checkbox_show_numbers);

		checkbox_show_connections.setText("show connections");
		checkbox_show_connections.setToolTipText("draw lines for all connections better than the selected cutoff");
		checkbox_show_connections.setMnemonic(KeyEvent.VK_T);
		checkbox_show_connections.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(java.awt.event.ItemEvent evt) {
				requestRepaint();
			}
		});
		drawbuttonpanel.add(checkbox_show_connections);

		button_zoom_on_selected.setText("Zoom on selected");
		button_zoom_on_selected.setMnemonic(KeyEvent.VK_Z);
		button_zoom_on_selected.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				button_zoom_on_selectedActionPerformed();
			}
		});
		drawbuttonpanel.add(button_zoom_on_selected);

		buttonpanel.add(drawbuttonpanel);

		getContentPane().add(buttonpanel, java.awt.BorderLayout.SOUTH);

		menu_file.setText("File");
		menu_file.setMnemonic(KeyEvent.VK_F);

		loadmenuitem.setText("Load Run");
		loadmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadDataClansFormat();
			}
		});
		menu_file.add(loadmenuitem);

		savemenuitem.setText("Save Run");
		savemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveRun();
			}
		});
		menu_file.add(savemenuitem);

		saveasmenuitem.setText("Save Run As");
		saveasmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveRunAs();
			}
		});
		menu_file.add(saveasmenuitem);

		saveattvalsmenuitem.setText("Save attraction values to file");
		saveattvalsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveattvalsmenuitemActionPerformed();
			}
		});
		menu_file.add(saveattvalsmenuitem);

		addseqsmenuitem.setText("Add Sequences");
		addseqsmenuitem.setToolTipText("You have to do that from the command line");
		addseqsmenuitem.setEnabled(false);
		addseqsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addNewSequences();
			}
		});
		menu_file.add(addseqsmenuitem);

		savemtxmenuitem.setText("Save blast matrix p-values");
		savemtxmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveBlastMatrixPValuesAs();
			}
		});
		menu_file.add(savemtxmenuitem);

		save2dmenuitem.setText("Save 2d graph data");
		save2dmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				save2dGraphDataAs();
			}
		});
		menu_file.add(save2dmenuitem);

		printmenuitem.setText("Print view");
		printmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				printDrawArea();
			}
		});
		menu_file.add(printmenuitem);

		loadalternatemenuitem.setText("Load data in matrix format");
		loadalternatemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadDataMatrixFormat();
			}
		});
		menu_file.add(loadalternatemenuitem);

		loadtabsmenuitem.setText("Load tabular data");
		loadtabsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadDataTabularFormat();
			}
		});
		menu_file.add(loadtabsmenuitem);

		loadgroupsmenuitem.setText("Append sequence groups from file");
		loadgroupsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loadSequenceGroupsToAppend();
			}
		});
		menu_file.add(loadgroupsmenuitem);

		jMenuBar1.add(menu_file);

		menu_misc.setText("Misc");
		menu_misc.setMnemonic(KeyEvent.VK_M);

		getseqsmenuitem.setText("Extract selected sequences");
		getseqsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openSaveSequenceAsFastaDialog();
			}
		});
		menu_misc.add(getseqsmenuitem);

		remove_selected_sequences_menu_item.setText("Remove selected sequences");
		remove_selected_sequences_menu_item.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				removeAllSelectedSequences();
			}
		});
		menu_misc.add(remove_selected_sequences_menu_item);

		hidesingletonsmenuitem.setText("Hide singletons");
		hidesingletonsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hideSingletonsMenuItemActionPerformed();
			}
		});
		menu_misc.add(hidesingletonsmenuitem);

		getchildmenuitem.setText("Use selected subset");
		getchildmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				useGraphSubset();
			}
		});
		menu_misc.add(getchildmenuitem);

		getparentmenuitem.setText("Use parent group (0)");
		getparentmenuitem.setEnabled(false);
		getparentmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				useGraphSuperset();
			}
		});
		menu_misc.add(getparentmenuitem);

		setrotmenuitem.setText("Set rotation values");
		setrotmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openRotationMatrixInputDialog();
			}
		});
		menu_misc.add(setrotmenuitem);

		attvalcompcheckbox.setSelected(true);
		attvalcompcheckbox.setText("Complex attraction");
		attvalcompcheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateUseComplexAttractionValuesState();
			}
		});
		menu_misc.add(attvalcompcheckbox);

		moveselectedonly.setText("Optimize only selected sequences");
		moveselectedonly.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateOptimizeOnlySelectedSequencesState();
			}
		});
		menu_misc.add(moveselectedonly);

		cluster2dbutton.setText("Cluster in 2D");
		cluster2dbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cluster2dbuttonActionPerformed();
			}
		});
		menu_misc.add(cluster2dbutton);

		rescalepvaluescheckbox.setText("Rescale attraction values");
		rescalepvaluescheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateRescaleAttractionValuesState();
			}
		});
		menu_misc.add(rescalepvaluescheckbox);

		autosaveSetup.setText("setup autosaving");
		autosaveSetup.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openAutosaveSetupDialog();
			}
		});
		menu_misc.add(autosaveSetup);
		
		messageOverlayActiveCheckbox.setText("activate message overlay");
		messageOverlayActive = true;
		messageOverlayActiveCheckbox.setSelected(true);
		messageOverlayActiveCheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				toggleMessageOverlayActive();
			}
		});
		menu_misc.add(messageOverlayActiveCheckbox);

		skipdrawingrounds.setText("Only draw every Nth round (speedup)");
		skipdrawingrounds.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				skipdrawingroundsActionPerformed();
			}
		});
		menu_misc.add(skipdrawingrounds);

		jMenuBar1.add(menu_misc);

		menu_draw.setText("Draw");
		menu_draw.setMnemonic(KeyEvent.VK_D);

		changefontmenuitem.setText("Change Font");
		changefontmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openChangeFontDialog();
			}
		});
		menu_draw.add(changefontmenuitem);

		getdotsizemenuitem.setText("Set dot size");
		getdotsizemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openChaneDotSizeDialog();
			}
		});
		menu_draw.add(getdotsizemenuitem);

		getovalsizemenuitem.setText("Set selected circle size");
		getovalsizemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openChangeSelectedCircleSizeDialog();
			}
		});
		menu_draw.add(getovalsizemenuitem);

		changecolormenuitem.setText("Change color (dot connections)");
		changecolormenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openChangeDotConnectionColorDialog();
			}
		});
		menu_draw.add(changecolormenuitem);

		changefgcolormenuitem.setText("Change color (Foreground)");
		changefgcolormenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openForegroundColorChangeDialog();
			}
		});
		menu_draw.add(changefgcolormenuitem);

		changebgcolormenuitem.setText("Change color (Background)");
		changebgcolormenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openBackgroundColorChangeDialog();
			}
		});
		menu_draw.add(changebgcolormenuitem);

		changeselectcolormenuitem.setText("Change color (Selecteds)");
		changeselectcolormenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openSelectedColorChangeDialog();
			}
		});
		menu_draw.add(changeselectcolormenuitem);

		changenumbercolor.setText("Change color (BLAST hit numbers)");
		changenumbercolor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openNumberColorChangeDialog();
			}
		});
		menu_draw.add(changenumbercolor);

		changeblastcolor.setText("Change color (BLAST hit circles)");
		changeblastcolor.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openBlastColorChangeDialog();
			}
		});
		menu_draw.add(changeblastcolor);

		lengthcolormenuitem.setText("Color dots by sequence length (yellow=short, blue=long)");
		menu_draw.add(lengthcolormenuitem);

		colorfrustrationcheckbox.setText("Color by edge \"frustration\" (red=too long, blue=too short)");
		colorfrustrationcheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				requestRepaint();
			}
		});
		menu_draw.add(colorfrustrationcheckbox);

		showorigcheckbox.setText("Show origin");
		menu_draw.add(showorigcheckbox);

		if (data == null) {
			showinfocheckbox.setSelected(false);
		} else {
			showinfocheckbox.setSelected(data.showinfo);
		}
		showinfocheckbox.setText("Show info");
		showinfocheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				requestRepaint();
			}
		});
		menu_draw.add(showinfocheckbox);

		shownamesselectcheckbox.setText("Show names while selecting");
		menu_draw.add(shownamesselectcheckbox);

		showblasthitnamescheckbox.setText("Show hsp sequence numbers");
		menu_draw.add(showblasthitnamescheckbox);

		zoommenuitem.setText("Zoom");
		zoommenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				zoommenuitemActionPerformed();
			}
		});
		menu_draw.add(zoommenuitem);

		centermenuitem.setText("Center graph");
		centermenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				centerGraph();
			}
		});
		menu_draw.add(centermenuitem);

		antialiasingcheckboxmenuitem.setText("Antialiasing (slow !)");
		antialiasingcheckboxmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				requestRepaint();
			}
		});
		menu_draw.add(antialiasingcheckboxmenuitem);

		stereocheckboxmenuitem.setText("Stereo");
		stereocheckboxmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				requestRepaint();
			}
		});
		menu_draw.add(stereocheckboxmenuitem);

		stereoanglemenuitem.setText("Change stereo angle (0-360)");
		stereoanglemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openChangeStereoVisionAngleDialog();
			}
		});
		menu_draw.add(stereoanglemenuitem);

		jMenuBar1.add(menu_draw);

		menu_windows.setText("Windows");
		menu_windows.setMnemonic(KeyEvent.VK_W);

		showoptionsmenuitem.setText("Show options window");
		showoptionsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openOptionsWindow();
			}
		});
		menu_windows.add(showoptionsmenuitem);

		sequencesitem.setText("Selecteds");
		sequencesitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openShowSelectedSequencesWindow();
			}
		});
		menu_windows.add(sequencesitem);

		evalueitem.setText("P-value plot");
		evalueitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openScoreDistributionPlot();
			}
		});
		menu_windows.add(evalueitem);

		getblasthitsmenuitem.setText("Show blast hits for sequence:");
		getblasthitsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openShowBlastHitsForSequenceDialog();
			}
		});
		menu_windows.add(getblasthitsmenuitem);

		clustermenuitem.setText("find clusters");
		clustermenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openFindClustersWindow();
			}
		});
		menu_windows.add(clustermenuitem);

		getseqsforselectedhits.setText("Get sequence with hits from/to selected");
		getseqsforselectedhits.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openGetSequencesConnectedToSelectedsWindow();
			}
		});
		menu_windows.add(getseqsforselectedhits);

		seqscoloring.setText("Edit Groups");
		seqscoloring.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openEditGroupsWindow();
			}
		});
		menu_windows.add(seqscoloring);

		showseqsmenuitem.setText("Show selected sequences as text (copy/pastable)");
		showseqsmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openShowCopyPasteableSequencesWindow();
			}
		});
		menu_windows.add(showseqsmenuitem);

		rotationmenuitem.setText("Rotation");
		rotationmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openRotationWindow();
			}
		});
		menu_windows.add(rotationmenuitem);

		affymenuitem.setText("Microarray_data");
		affymenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				affymenuitemActionPerformed();
			}
		});
		menu_windows.add(affymenuitem);

		mapmanmenuitem.setText("Functional mapping");
		mapmanmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openFunctionalMappingWindow();
			}
		});
		menu_windows.add(mapmanmenuitem);

		taxonomymenuitem.setText("Taxonomy");
		taxonomymenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openTaxonomyWindow();
			}
		});
		menu_windows.add(taxonomymenuitem);

		jMenuBar1.add(menu_windows);

		menu_help.setText("Help");
		menu_help.setMnemonic(KeyEvent.VK_H);

		aboutmenuitem.setText("About");
		aboutmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openAboutWindow();
			}
		});
		menu_help.add(aboutmenuitem);

		helpmenuitem.setText("Help");
		helpmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openHelpWindow();
			}
		});
		menu_help.add(helpmenuitem);

		jMenuBar1.add(menu_help);

		setJMenuBar(jMenuBar1);

		originalGlassPane = this.getGlassPane();
		activateGuiMessageOverlay();
		addGuiMessageOverlayResizeListener();
		
		createLoadSaveCancelKeyListener();

		pack();
	}

	/**
	 * Adds listener to inform the message overlay of main window size changes.
	 */
	private void addGuiMessageOverlayResizeListener() {
		graphpanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent arg0) {
				if (message_overlay != null) {
					message_overlay.updateGlassSize(graphpanel.getHeight());
				}
			}
		});
	}
	
	/**
	 * Creates the KeyListener that is added/removed when the escape key is activated/deactivated for canceling
	 * long-running operations. Currently used during file loading and saving.
	 */
	private void createLoadSaveCancelKeyListener() {
		cancelWorkInProgress = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					if (saveLoadWorker != null) {
						saveLoadWorker.cancel(true);
					}
				}
			}
		};
	}
	
	/**
	 * Consumes the data and sets some parts of theGUI up.
	 */
	private void consumeDataAtConstruction() {
		data.nographics = false;

		data.mineval = data.eval;
		data.pvalue_threshold = data.pval;

		if (data.scval >= 0) { // in that case use a score cutoff
			data.usescval = true;

			data.pvalue_threshold = data.scval;
			button_cutoff_value.setText("Use SC-vals better than");
			textfield_threshold_value.setText("0");
			evalueitem.setText("SC-value plot");
			attvalcompcheckbox.setSelected(false);

		} else {
			data.usescval = false;
		}

		data.seqlengths = new float[data.seqnum];
		float maxlength = 0;
		for (int i = 0; i < data.seqnum; i++) {
			data.seqlengths[i] = data.sequences[i].length();
			if (data.seqlengths[i] > maxlength) {
				maxlength = data.seqlengths[i];
			}
		}

		for (int i = 0; i < data.seqnum; i++) {
			data.seqlengths[i] /= maxlength;
		}
	}

	private void loadDataTabularFormat() {
		boolean restart_computation = stopComputation(true);

		groupseqs = null;
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			// update loadtabdata(fc.getSelectedFile().getAbsolutePath());
			if (myseqgroupwindow != null) {
				myseqgroupwindow.setVisible(false);
				myseqgroupwindow.dispose();
			}
			if (mymapfunctiondialog != null) {
				mymapfunctiondialog.setVisible(false);
				mymapfunctiondialog.dispose();
			}
			repaint();
		}

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Opens a dialog that lets users enter a new stereo vision angle.
	 */
	private void openChangeStereoVisionAngleDialog() {
		String tmpstr = "";
		try {
			tmpstr = JOptionPane.showInputDialog(this, "Enter the new angle (int):", String.valueOf(draw1.stereoangle));
			if (tmpstr != null) {
				draw1.stereoangle = (int) (Float.parseFloat(tmpstr));// someone might enter a float and it's not time
																		// critical
			}
		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to parse integer from '" + tmpstr + "'");
		}
		repaint();
	}

	/**
	 * Opens a windows that shows functional mapping data.
	 */
	private void openFunctionalMappingWindow() {
		if (mymapfunctiondialog != null) {
			mymapfunctiondialog.setVisible(false);
			mymapfunctiondialog.dispose();
		}
		mymapfunctiondialog = new mapfunctiondialog_tab(this);
		mymapfunctiondialog.setVisible(true);
	}

	/**
	 * Enables zoom with mousewheel+CTRL (coarse grained) or mousewheel+CTRL+SHIFT (fine grained)
	 * 
	 * @param evt
	 *            The mouse wheel event.
	 */
	private void zoomUsingMouseWheel(java.awt.event.MouseWheelEvent evt) {
	
		if (mouseEventsDisabled) {
			return;
		}

		if (!evt.isControlDown()) {
			return;
		}

		float oldzoom = data.zoomfactor;

		if (evt.isShiftDown()) {
			data.zoomfactor += ((float) -evt.getWheelRotation()) / 100;
		} else {
			data.zoomfactor += ((float) -evt.getWheelRotation()) / 10;
		}

		this.updateZoom(oldzoom);
	}

	/**
	 * Opens a windows to show microarray data.
	 */
	private void affymenuitemActionPerformed() {
		if (myaffydialog != null) {
			myaffydialog.setVisible(false);
			myaffydialog.dispose();
		}
		myaffydialog = new affydialog(this);
		myaffydialog.setVisible(true);
	}

	/**
	 * Opens a window that lets users specify rotations of the graph.
	 */
	private void openRotationWindow() {
		if (myrotationdialog != null) {
			myrotationdialog.setVisible(false);
			myrotationdialog.dispose();
		}
		
		myrotationdialog = new rotationdialog(this);
		myrotationdialog.setVisible(true);
	}

	/**
	 * Loads additional sequence groups from a file.
	 */
	private void loadSequenceGroupsToAppend() {
		
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		
			data.append_groups_or_clusters_from_file(fc.getSelectedFile());
			
			if (myseqgroupwindow != null) {
				myseqgroupwindow.setVisible(false);
				myseqgroupwindow.dispose();
			}
			repaint();
		}
	}

	private void addNewSequences() {
		javax.swing.JOptionPane.showMessageDialog(this, "You currently have to do that from the command line");
	}

	/**
	 * Opens the options window.
	 */
	private void openOptionsWindow() {
		if (options_window != null) {
			options_window.setVisible(false);
			options_window.dispose();
		}
		options_window = new WindowOptions(this);
		options_window.setVisible(true);
	}

	/**
	 * Opens the "setup autosave" dialog and handles the user's interaction with it. The dialog lets users decide
	 * whether to save automatically and how often to do it.
	 */
	private void openAutosaveSetupDialog() {

		String user_input = (String) JOptionPane.showInputDialog(this,
				"autosave this file every N minutes (0 = disable autosave)", "setup autosaving for this file",
				JOptionPane.PLAIN_MESSAGE, null, null, data.getAutosaveIntervalMinutes());

		if (user_input == null) { // dialog was canceled -> no change
			return;
		}

		try {
			int new_interval = Integer.parseInt(user_input);

			if (new_interval < 0) { // time intervals cannot be negative
				throw new NumberFormatException();
			}

			data.setAutosaveIntervalMinutes(new_interval);
		} catch (NumberFormatException e) {
			javax.swing.JOptionPane.showMessageDialog(this, "unable to parse positive integer number from \""
					+ user_input + "\"");
			return;
		}

		if (data.getAutosaveIntervalMinutes() > 0) {
			enableAutosave(true);

		} else {
			disableAutosave(true);
			return;
		}
	}
	
	/**
	 * Enables autosaving if it is set up to occur. For loaded data without autosave parameters, a reasonably long
	 * autosave interval is automatically set up and the user is informed about it in a pop-up dialog.
	 */
	private void initializeAutosave() {
		if (!data.knowsAutosave()) {
			data.makeAutosaveAware();
			javax.swing.JOptionPane.showMessageDialog(this, "Autosaving every " + data.getAutosaveIntervalMinutes()
					+ " minutes has been enabled for this file." + "\nTo change this go to Menu->Misc->"
					+ autosaveSetupLabel + ".", "autosaving enabled", JOptionPane.INFORMATION_MESSAGE);
		}

		if (data.getAutosaveIntervalMinutes() > 0) {
			enableAutosave(false);
		}
		
	}

	/**
	 * Enables autosaving if an autosave interval is set. Does nothing if the autosave interval is 0.
	 * 
	 * @param show_overlay_message
	 *            If true and if the message overlay is active, the user is informed about the change in an overlay
	 *            message.
	 */
	private void enableAutosave(boolean show_overlay_message) {

		if (data.getAutosaveIntervalMinutes() == 0) {
			return;
		}

		if (autosaveTimer != null && autosaveTimer.isRunning()) {
			autosaveTimer.stop();
		}

		// convert minutes to milliseconds for time
		autosaveTimer = new Timer(data.getAutosaveIntervalMinutes() * 60 * 1000, new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				autosave();
			}
		});
		autosaveTimer.start();

		if (messageOverlayActive && show_overlay_message) {
			message_overlay.setCustomMessage("autosaving ENABLED", "every " + data.getAutosaveIntervalMinutes()
					+ " minutes", message_overlay.getColorDefault(), GuiMessageOverlay.Duration.INFO, true, false);
		}
	}

	/**
	 * Disables autosaving.
	 * 
	 * @param show_overlay_message
	 *            If true and if the message overlay is active, the user is informed about the change in an overlay
	 *            message.
	 */
	private void disableAutosave(boolean show_overlay_message) {

		data.setAutosaveIntervalMinutes(0);

		if (autosaveTimer != null && autosaveTimer.isRunning()) {
			autosaveTimer.stop();
		}
		autosaveTimer = null;
		

		if (messageOverlayActive && show_overlay_message) {
			message_overlay.setCustomMessage("autosaving DISABLED", null, message_overlay.getColorDefault(),
					GuiMessageOverlay.Duration.WARNING, true, false);
		}
	}
	
	/**
	 * Stops the autosave timer.
	 * 
	 * @return true if a timer exists and it was running, false else.
	 */
	private boolean pauseAutosave() {
		if (autosaveTimer == null || !autosaveTimer.isRunning()){
			return false;
		}
		
		autosaveTimer.stop();
		return true;
	}
	
	/**
	 * Restarts the autosave timer. This resets the time until the next autosave to the autosave interval.
	 */
	private void restartAutosave() {
		if (autosaveTimer != null) {
			autosaveTimer.restart();
		}
	}
	
	/**
	 * Toggles whether the message overlay is shown or disabled.
	 */
	private void toggleMessageOverlayActive() {
		
		if (messageOverlayActive) {
			deactivateMessageOverlay();
		} else {
			activateGuiMessageOverlay();
		}
		
		messageOverlayActive = !messageOverlayActive;
	}
	
	/**
	 * Activates the message overlay.
	 * <p>
	 * TODO: remember: production versions MUST use GuiMessageOverlay instead of the debugging class
	 * GuiMessageOverlayLogged!
	 */
	private void activateGuiMessageOverlay() {
		message_overlay = new GuiMessageOverlay();
		message_overlay.updateGlassSize(graphpanel.getHeight());

		this.setGlassPane(message_overlay);
	}
	
	/**
	 * Deactivates the message overlay.
	 */
	private void deactivateMessageOverlay() {
		this.setGlassPane(originalGlassPane);
		message_overlay = null;
	}
	
	/**
	 * Changes the "enabled" state of all menus in the menubar.
	 * 
	 * @param enabled
	 *            If true all menus will be enabled, if false disabled.
	 */
	private void modifyMenusEnabled(boolean enabled) {
		for (int i=0; i < jMenuBar1.getMenuCount(); i++) {
			jMenuBar1.getMenu(i).setEnabled(enabled);
		}
	}

	/**
	 * Enables all menus in the menubar.
	 */
	private void enableMenus() {
		modifyMenusEnabled(true);
	}

	/**
	 * Disables all menus in the menubar.
	 */
	private void disableMenus() {
		modifyMenusEnabled(false);
	}

	/**
	 * Changes the "enabled" state of all buttons in the control bar (bottom of the CLANS window).
	 * 
	 * @param enabled
	 *            If true all buttons will be enabled, if false disabled.
	 */
	private void modifyControlButtonsEnabled(boolean enabled) {
		for (int i = 0; i < drawbuttonpanel.getComponentCount(); i++) {
			drawbuttonpanel.getComponent(i).setEnabled(enabled);
		}
	}

	/**
	 * Enables all buttons in the control bar (bottom of the CLANS window).
	 */
	private void enableControlButtons() {
		modifyControlButtonsEnabled(true);
	}

	/**
	 * Disables all buttons in the control bar (bottom of the CLANS window).
	 */
	private void disableControlButtons() {
		modifyControlButtonsEnabled(false);
	}

	/**
	 * Enables all mouse event handling in event listeners.
	 */
	private void enableMouseEventHandling() {
		mouseEventsDisabled = false;
	}
	
	/**
	 * Disables all mouse event handling in event listeners.
	 */
	private void disableMouseEventHandling() {
		mouseEventsDisabled = true;
	}
	
	/**
	 * Enables detecting escape key presses to cancel operations.
	 */
	private void enableCancelWorkInProgressKey() {
		addKeyListener(cancelWorkInProgress);
	}
	
	/**
	 * Disables detecting escape key presses to cancel operations.
	 */
	private void disableCancelWorkInProgressKey() {
		removeKeyListener(cancelWorkInProgress);
	}
	
	/**
	 * Enables user contols.
	 */
	private void enableUserControls() {
		enableMenus();
		enableControlButtons();
		enableMouseEventHandling();
		disableCancelWorkInProgressKey();
	}

	/**
	 * Disables user contols. This is useful to block the user from interacting with the GUI in long running operations
	 * like saving and loading, where he could break s.th. with his input.
	 */
	private void disableUserControls() {
		disableMenus();
		disableControlButtons();
		disableMouseEventHandling();
		enableCancelWorkInProgressKey();
	}

	/**
	 * Opens an input dialog where users can choose to only repaint the GUI after every N rounds of calculation.
	 */
	private void skipdrawingroundsActionPerformed() {
		String tmpstr;
		tmpstr = javax.swing.JOptionPane.showInputDialog(this, "Draw each Nth round. N=", String.valueOf(skiprounds));
		try {
			if (tmpstr != null) {
				skiprounds = Integer.parseInt(tmpstr);
			}
		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to parse integer from '" + tmpstr + "'");
		}
	}

	/**
	 * Opens the "Help" window.
	 */
	private void openHelpWindow() {
		new WindowHelp(this, true).setVisible(true);
	}

	/**
	 * Opens the "About" window.
	 */
	private void openAboutWindow() {
		new WindowAbout(this, true).setVisible(true);
	}

	/**
	 * Opens a window that offers the currently selected sequences in FASTA format as copy & pastable text.
	 */
	private void openShowCopyPasteableSequencesWindow() {
		int seqnum = data.selectednames.length;
		if (seqnum < 1) {
			javax.swing.JOptionPane.showMessageDialog(this, "Please select some sequences");
			return;
		}
		
		StringBuffer outbuff = new StringBuffer();
		for (int i = 0; i < seqnum; i++) {
			outbuff.append(">" + data.sequence_names[data.selectednames[i]] + " " + data.selectednames[i] + "\n");
			outbuff.append(data.sequences[data.selectednames[i]].seq + "\n");
		}

		new ShowCopyPasteableSequences(new javax.swing.JFrame(), outbuff).setVisible(true);
	}

	/**
	 * Updates the zoom of the view to the currently set zoom value while keeping the same area centered.
	 * 
	 * @param old_zoom
	 *            The previous zoom value.
	 */
	private void updateZoom(float old_zoom) {

		if (old_zoom == -1) { // not zoomed before, simply center 
			centerGraph();
			return;
		}
		
		int panelwidth = graphpanel.getWidth() - 2 * draw1.xadd;
		int panelheight = graphpanel.getHeight() - 2 * draw1.yadd;

		int imagecenterx = (int) ((panelwidth / 2 - draw1.xtranslate) / old_zoom);
		int imagecentery = (int) ((panelheight / 2 - draw1.ytranslate) / old_zoom);

		draw1.xtranslate = (int) (-(imagecenterx * data.zoomfactor) + panelwidth / 2);
		draw1.ytranslate = (int) (-(imagecentery * data.zoomfactor) + panelheight / 2);

		repaint();
	}

	/**
	 * Centers the graph while maintaining the current zoom level.
	 */
	private void centerGraph() {
		int panelwidth = graphpanel.getWidth() - 2 * draw1.xadd;
		int panelheight = graphpanel.getHeight() - 2 * draw1.yadd;

		draw1.xtranslate = (int) (panelwidth / 2 * (1 - data.zoomfactor));
		draw1.ytranslate = (int) (panelheight / 2 * (1 - data.zoomfactor));

		repaint();
	}
	
	private void zoommenuitemActionPerformed() {
		String tmpstr = "";
		float oldzoom = data.zoomfactor;
		try {
			tmpstr = javax.swing.JOptionPane.showInputDialog(this, "New zoom factor (in percent)",
					String.valueOf((int) (data.zoomfactor * 100)));
			if (tmpstr != null) {
				data.zoomfactor = ((float) (Integer.parseInt(tmpstr))) / 100;
			}
		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "Error; cannot parse float from '" + tmpstr + "'");
			return;
		}

		updateZoom(oldzoom);
	}

	/**
	 * Opens a dialog to let the user change the GUI font.
	 */
	private void openChangeFontDialog() {
		draw1.myfont = fontchooserdialog.getfont("Select Font", draw1.myfont);
		repaint();
	}
	
	/**
	 * Opens a window in which the user can create and manage custom groups of sequences. These group definitions are
	 * used to draw differently colored shapes in the draw area instead of simple dots with identical colors.
	 */
	private void openEditGroupsWindow() {

		if (myseqgroupwindow != null) {
			myseqgroupwindow.setVisible(false);
			myseqgroupwindow.dispose();
		}
		
		myseqgroupwindow = new WindowEditGroups(this);
		myseqgroupwindow.setVisible(true);
	}

	/**
	 * Opens a window that lets users see all sequences with connections to the currently selected sequences. Further
	 * options like the selection of those connected sequences are available to the user.
	 */
	private void openGetSequencesConnectedToSelectedsWindow() {
		int[] blasthitsarr = showblasthitsforselected.getblasthits(data.myattvals, data.selectednames,
				data.sequence_names);
		new showblasthitsforselected(this, blasthitsarr, data.selectednames).setVisible(true);
	}

	/**
	 * Opens the "find clusters" window to let the user perform automatic cluster detection on the data.
	 */
	private void openFindClustersWindow() {
		// don't define optionsvec as it contains both strings and numbers in a defined order
		Vector<String> optionsvec = new Vector<String>();
		new DialogClusterOptions(this, optionsvec).setVisible(true);
		if (optionsvec.size() == 0) {// if I canceled
			return;
		}
		boolean didbootstrap = false;
		String tmpstr = (String) optionsvec.remove(0);
		if (tmpstr.equals("convex")) {
			tmpstr = (String) optionsvec.remove(0);
			try {
				float sigmafac = Float.parseFloat(tmpstr);
				tmpstr = (String) optionsvec.remove(0);
				int minseqnum = Integer.parseInt(tmpstr);
				System.out.println("searching for convex clusters");
				Vector<cluster> clustervec = findclusters.getconvex(data.myattvals, sigmafac, minseqnum, data.elements);
				System.out.println("done searching for clusters; opening window");
				if (((String) optionsvec.remove(0)).equalsIgnoreCase("true")) {// if do bootstrap
					didbootstrap = true;
					tmpstr = (String) optionsvec.remove(0);
					int replicates = Integer.parseInt(tmpstr);
					tmpstr = (String) optionsvec.remove(0);
					float remove = Float.parseFloat(tmpstr);
					if (remove > 1) {
						remove /= 100;
					}
					if (bootstrapcluster.bootstrapconvex(data.myattvals, clustervec, "convex", replicates, remove,
							sigmafac, minseqnum, data.elements) == false) {
						javax.swing.JOptionPane.showMessageDialog(this, "ERROR while bootstrapping");
						return;
					}
				}
				new ClusterWindow(this, clustervec, "convex: " + minseqnum + ";" + sigmafac, didbootstrap)
						.setVisible(true);
			} catch (NumberFormatException ne) {
				javax.swing.JOptionPane.showMessageDialog(this, "Unable to parse float from " + tmpstr);
			}
		} else if (tmpstr.equals("linkage")) {
			tmpstr = (String) optionsvec.remove(0);
			try {
				int minlinkage = Integer.parseInt(tmpstr);
				tmpstr = (String) optionsvec.remove(0);
				int minseqnum = Integer.parseInt(tmpstr);
				System.out.println("searching for linkage clusters");
				Vector<cluster> clustervec = findclusters.getlinkage(data.myattvals, minlinkage, minseqnum,
						data.elements);
				System.out.println("done searching for clusters; opening window");
				if (((String) optionsvec.remove(0)).equalsIgnoreCase("true")) {// if do bootstrap
					didbootstrap = true;
					tmpstr = (String) optionsvec.remove(0);
					int replicates = Integer.parseInt(tmpstr);
					tmpstr = (String) optionsvec.remove(0);
					float remove = Float.parseFloat(tmpstr);
					if (remove > 1) {
						remove /= 100;
					}
					if (bootstrapcluster.bootstraplinkage(data.myattvals, clustervec, "linkage", replicates, remove,
							minlinkage, minseqnum, data.elements) == false) {
						javax.swing.JOptionPane.showMessageDialog(this, "Error while bootstrapping");
						return;
					}
				}
				new ClusterWindow(this, clustervec, "linkage: " + minseqnum + ";" + minlinkage, didbootstrap)
						.setVisible(true);
			} catch (NumberFormatException ne) {
				javax.swing.JOptionPane.showMessageDialog(this, "Unable to parse int from " + tmpstr);
			}
		} else if (tmpstr.equals("network")) {
			tmpstr = (String) optionsvec.remove(0);
			try {
				int minseqnum = Integer.parseInt(tmpstr);
				boolean dooffset = false;
				boolean globalaverage = false;
				tmpstr = (String) optionsvec.remove(0);
				if (tmpstr.equalsIgnoreCase("true")) {
					dooffset = true;
				}
				tmpstr = (String) optionsvec.remove(0);
				if (tmpstr.equalsIgnoreCase("true")) {
					globalaverage = true;
				}
				int maxrounds = Integer.parseInt(optionsvec.remove(optionsvec.size() - 1));
				System.out.println("searching for network clusters, maxrounds=" + maxrounds);
				Vector<cluster> clustervec = findclusters.getnetwork(data.myattvals, minseqnum, dooffset,
						globalaverage, data.elements, maxrounds);
				System.out.println("done searching for clusters; opening window");
				if (((String) optionsvec.remove(0)).equalsIgnoreCase("true")) {// if do bootstrap
					didbootstrap = true;
					tmpstr = (String) optionsvec.remove(0);
					int replicates = Integer.parseInt(tmpstr);
					tmpstr = (String) optionsvec.remove(0);
					float remove = Float.parseFloat(tmpstr);
					if (remove > 1) {
						remove /= 100;
					}
					if (bootstrapcluster.bootstrapnetwork(data.myattvals, clustervec, "network", replicates, remove,
							minseqnum, dooffset, globalaverage, data.elements, maxrounds) == false) {
						javax.swing.JOptionPane.showMessageDialog(this, "Error while bootstrapping");
						return;
					}
				}
				new ClusterWindow(this, clustervec, "network:" + minseqnum + ";" + dooffset + ";" + globalaverage,
						didbootstrap).setVisible(true);
			} catch (NumberFormatException ne) {
				javax.swing.JOptionPane.showMessageDialog(this, "Unable to parse int from " + tmpstr);
			}
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "Error in selecting clustering method: " + tmpstr);
		}
	}

	/**
	 * Opens file choice dialog and load data the selected file with matrix info (only one value per pair).
	 */
	private void loadDataMatrixFormat() {
		boolean restart_computation = stopComputation(true);

		groupseqs = null;
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			saverunobject saveddata = ClusterData.load_matrix_file(fc.getSelectedFile());
			if (saveddata.file != null) {// if the data was read all right
				repaint = "Error Loading Data";
				data.sequences = ClusterMethods.remove_gaps_from_sequences(saveddata.inaln);
				int seqs = data.sequences.length;

				// data.nameshash holds info about which name is which array number
				data.nameshash = new HashMap<String, Integer>((int) (seqs / 0.75) + 1, (float) 0.75);

				data.sequence_names = new String[seqs];
				data.myposarr = new float[seqs][3];
				Random rand = ClusterMethods.rand;
				for (int i = 0; i < seqs; i++) {
					data.sequence_names[i] = data.sequences[i].name.trim();
					data.sequences[i].name = new String("sequence" + i);
					data.nameshash.put(data.sequences[i].name, new Integer(i));
					data.myposarr[i][0] = rand.nextFloat();
					data.myposarr[i][1] = rand.nextFloat();
					data.myposarr[i][2] = rand.nextFloat();
				}
				data.blasthits = null;
				data.orgattvals = null;
				data.myattvals = saveddata.attvals;
				data.myattvals = saveddata.attvals;
				button_cutoff_value.setText("Use Attraction values better than");
				textfield_threshold_value.setText("0");
				data.elements = data.sequence_names.length;
				// now symmetrize and normalize the attvals to range from -1 to +1
				float minval = 0;
				float maxval = 0;
				for (int i = data.myattvals.length - 1; i >= 0; i--) {
					if (data.myattvals[i].att > maxval) {
						maxval = data.myattvals[i].att;
					} else if (data.myattvals[i].att < minval) {
						minval = data.myattvals[i].att;
					}
				}// end for i
				if (-minval > maxval) {// decide wether to divide by maxval or -minval
					maxval = -minval;
				}
				for (int i = data.myattvals.length - 1; i >= 0; i--) {
					// now normalize the values
					data.myattvals[i].att /= maxval;
				}// end for i
				data.p2attfactor = maxval;
				data.selectednames = new int[0];
				data.seqgroupsvec = saveddata.seqgroupsvec;
				data.posarr = data.myposarr;
				data.lastmovearr = new float[data.elements][ClusterData.dimensions];
				data.mymovearr = new float[data.elements][ClusterData.dimensions];
				data.posarrtmp = new float[data.elements][ClusterData.dimensions];
				data.drawarrtmp = new int[data.elements][ClusterData.dimensions];
				data.resetDrawOrder();
				data.attvalsimple = true;
				repaint = null;
				data.pvalue_threshold = 1;
				textfield_threshold_value.setText("1");
				textfield_info_min_blast_evalue.setText("1");
			} else {// if the data had errors
				JOptionPane.showMessageDialog(this, "Error reading data", "Error reading", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
		int seqnum = data.sequence_names.length;
		System.out.println("seqnum=" + seqnum);
		data.seqlengths = new float[seqnum];
		float maxlength = 0;
		for (int i = 0; i < seqnum; i++) {
			data.seqlengths[i] = data.sequences[i].seq.replaceAll("-", "").length();
			if (data.seqlengths[i] > maxlength) {
				maxlength = data.seqlengths[i];
			}
		}// end for i
		if (maxlength > 0) {
			for (int i = 0; i < seqnum; i++) {
				data.seqlengths[i] /= maxlength;
			}// end for i
		}

		if (restart_computation) {
			startComputation();
		}

		repaint();
	}

	/**
	 * Toggles between 2D and 3D clustering mode.
	 */
	private void cluster2dbuttonActionPerformed() {
		// get rid of all z-axis information and set the rotation matrices to 1,1,1 (x,y,z)
		// set the rotation matrices to 1,1,1
		data.rotmtx[0][0] = 1;
		data.rotmtx[0][1] = 0;
		data.rotmtx[0][2] = 0;
		data.rotmtx[1][0] = 0;
		data.rotmtx[1][1] = 1;
		data.rotmtx[1][2] = 0;
		data.rotmtx[2][0] = 0;
		data.rotmtx[2][1] = 0;
		data.rotmtx[2][2] = 1;
		draw1.tmprotmtx[0][0] = 1;
		draw1.tmprotmtx[0][1] = 0;
		draw1.tmprotmtx[0][2] = 0;
		draw1.tmprotmtx[1][0] = 0;
		draw1.tmprotmtx[1][1] = 1;
		draw1.tmprotmtx[1][2] = 0;
		draw1.tmprotmtx[2][0] = 0;
		draw1.tmprotmtx[2][1] = 0;
		draw1.tmprotmtx[2][2] = 1;
		data.myrotmtx[0][0] = 1;
		data.myrotmtx[0][1] = 0;
		data.myrotmtx[0][2] = 0;
		data.myrotmtx[1][0] = 0;
		data.myrotmtx[1][1] = 1;
		data.myrotmtx[1][2] = 0;
		data.myrotmtx[2][0] = 0;
		data.myrotmtx[2][1] = 0;
		data.myrotmtx[2][2] = 1;
		// now set all the z-values to "0"
		for (int i = data.myposarr.length; --i >= 0;) {
			data.myposarr[i][2] = 0;
		}// end for i
		if (cluster2dbutton.isSelected()) {
			data.cluster2d = true;
		} else {
			data.cluster2d = false;
		}
		repaint();
	}

	private void printDrawArea() {
		boolean restart_computation = stopComputation(true);

		java.awt.print.PrinterJob printJob = java.awt.print.PrinterJob.getPrinterJob();
		printJob.setPrintable(draw1);
		if (printJob.printDialog()) {
			try {
				printJob.print();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Opens a color choice dialog that is safe in keeping the original color if canceled.
	 * 
	 * @param title
	 *            The title of the dialog.
	 * @param current_color
	 *            The current color, which is used as starting color.
	 * @return The picked color or {@code current_color} if the dialog is closed/canceled.
	 */
	protected java.awt.Color safe_change_color_dialog(String title, java.awt.Color current_color) {

		java.awt.Color new_color = null;

		try {
			new_color = JColorChooser.showDialog(this, title, current_color);
		} catch (java.awt.HeadlessException e) {
			System.err.println("HeadlessException!");
		}

		if (new_color == null) {
			return current_color;
		}
		return new_color;
	}

	/**
	 * Opens a color chooser dialog to change the color of selected sequence circles.
	 */
	private void openSelectedColorChangeDialog() {
		draw1.selectedcolor = safe_change_color_dialog("Choose a new foreground color", draw1.selectedcolor);
		repaint();
	}

	/**
	 * Opens a color chooser dialog to change the color of blast hit circles.
	 */
	private void openBlastColorChangeDialog() {
		draw1.blastcirclecolor = safe_change_color_dialog("Choose a new foreground color", draw1.blastcirclecolor);
		repaint();
	}

	/**
	 * Opens the "selected sequences" windows that displays the names of all selected sequences or of all sequences if
	 * none are selected.
	 */
	private void openShowSelectedSequencesWindow() {
		
		if (!this.contains_data(true)) {
			return;
		}
		
		if (shownames != null) {
			shownames.setVisible(false);
			shownames.dispose();
		}
		
		shownames = new WindowShowSelectedSequences(data.sequence_names, this);
		shownames.setVisible(true);
	}

	/**
	 * Opens a color chooser dialog to change the foreground color.
	 */
	private void openForegroundColorChangeDialog() {
		draw1.fgcolor = safe_change_color_dialog("Choose a new foreground color", draw1.fgcolor);
		repaint();
	}

	/**
	 * Opens a color chooser dialog to change the background color.
	 */
	private void openBackgroundColorChangeDialog() {
		draw1.bgcolor = safe_change_color_dialog("Choose a new background color", draw1.bgcolor);
		repaint();
	}

	private void save2dGraphDataAs() {
		boolean restart_computation = stopComputation(true);

		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File savefile = fc.getSelectedFile();
			try {
				PrintWriter outwrite = new PrintWriter(new BufferedWriter(new FileWriter(savefile)));

				int namenum = data.sequence_names.length;
				outwrite.println("ID\tNAME\tX\tY");

				for (int i = 0; i < namenum; i++) {
					outwrite.println(i + "\t" + data.sequence_names[i] + "\t" + (data.posarrtmp[i][0] - draw1.xadd)
							/ draw1.drawwidth + "\t" + (data.posarrtmp[i][1] - draw1.yadd) / draw1.drawheight);
				}

				outwrite.close();
			} catch (IOException ioe) {
				javax.swing.JOptionPane.showMessageDialog(this, "IOERROR writing to '" + savefile.getName() + "'");
			}
		}

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Opens a color chooser dialog to change the background color.
	 */
	private void openNumberColorChangeDialog() {
		draw1.blasthitcolor = safe_change_color_dialog("Select New Color", draw1.blasthitcolor);
		repaint();
	}

	/**
	 * Sets the threshold according to the threshold textfield content when the return key is pressed in the textfield.
	 */
	private void confirmedThresholdTextfieldValue() {
		double new_threshold;
		try {
			new_threshold = Double.parseDouble(textfield_threshold_value.getText());
		} catch (NumberFormatException e) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR; unable to parse double from '"
					+ textfield_threshold_value.getText() + "'");
			return;
		}
		set_threshold(new_threshold);
	}

	/**
	 * Sets the threshold according to the threshold textfield content when the "use values better than" button is
	 * pressed.
	 */
	private void buttonSetThresholdPressed() {
		confirmedThresholdTextfieldValue();
	}

	/**
	 * Sets the threshold used for clustering. In case the clustering is running, it is temporarily stopped then
	 * resumed.
	 * 
	 * @param threshold
	 *            The new threshold.
	 */
	private void set_threshold(double threshold) {

		if (!this.contains_data(true)) {
			return;
		}

		boolean restart_computation = stopComputation(true);

		data.pvalue_threshold = threshold;

		if (data.blasthits != null) {
			synchronized (data.myattvals) {
				data.compute_attraction_values();
			}
		} else if (data.myattvals != null) {// remove all attvals below the specified value
			if (data.orgattvals == null) {
				data.orgattvals = data.myattvals;
			}
			data.myattvals = ClusterMethods.filter_attraction_values(data.orgattvals, data.pvalue_threshold);
		}

		data.resetDrawOrder();
		repaint();

		if (restart_computation) {
			startComputation();
		}
	}

	private void saveBlastMatrixPValuesAs() {

		boolean restart_computation = stopComputation(true);

		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File savefile = fc.getSelectedFile();
			int elementsl = data.sequence_names.length;
			try {
				PrintWriter outwrite = new PrintWriter(new BufferedWriter(new FileWriter(savefile)));

				outwrite.println("sequences=" + elementsl);
				outwrite.println("<seqs>");

				for (int i = 0; i < elementsl; i++) {
					outwrite.println(">" + data.sequence_names[i]);
				}

				outwrite.println();
				outwrite.println("</seqs>");

				if (data.seqgroupsvec.size() > 0) {

					outwrite.println("#user defined sequence groups");
					outwrite.println("<seqgroups>");

					SequenceGroup mygroup;
					for (int i = data.seqgroupsvec.size() - 1; i >= 0; i--) {
						mygroup = (SequenceGroup) data.seqgroupsvec.elementAt(i);
						outwrite.println("name=" + mygroup.name);
						outwrite.println("color=" + mygroup.color.getRed() + ";" + mygroup.color.getGreen() + ";"
								+ mygroup.color.getBlue());
						outwrite.print("numbers=");

						for (int j = mygroup.sequences.length - 1; j >= 0; j--) {
							outwrite.print(mygroup.sequences[j] + ";");
						}

						outwrite.println();
					}

					outwrite.println("</seqgroups>");
				}

				outwrite.println("<att>");
				minattvals[] myattvals = data.myattvals;
				int datnum = myattvals.length;

				for (int i = 0; i < datnum; i++) {
					outwrite.println(myattvals[i].query + " " + myattvals[i].hit + " :" + myattvals[i].att);
				}

				outwrite.println("</att>");
				outwrite.close();
				System.out.println("done");
			} catch (IOException e) {
				javax.swing.JOptionPane.showMessageDialog(this, "IOERROR writing to '" + savefile.getAbsolutePath()
						+ "'");
			}
		}

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Opens a dialog that lets users change the size of all dots.
	 */
	private void openChaneDotSizeDialog() {
		String user_input = "";
		try {
			user_input = JOptionPane.showInputDialog(this, "Enter the new size (int):", String.valueOf(data.dotsize));
			if (user_input != null) {
				data.dotsize = (int) (Float.parseFloat(user_input));
			}
		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to parse integer from '" + user_input + "'");
		}
		repaint();
	}

	/**
	 * Opens a dialog that lets users enter specific rotation matrix values.
	 */
	private void openRotationMatrixInputDialog() {
		String tmpstr = "";
		try {
			tmpstr = JOptionPane.showInputDialog(this, "Enter the new rotation values: x , y , z (9 values total)");
			if (tmpstr == null) {
				return;
			}
			String[] tmparr = tmpstr.split(",");
			if (tmparr.length != 9) {
				JOptionPane.showMessageDialog(this, "You have to enter nine values separated by commas ','", "Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					data.myrotmtx[i][j] = Double.parseDouble(tmparr[i * 3 + j]);
				}
			}// end for i
			data.rotmtx[0][0] = data.myrotmtx[0][0];
			data.rotmtx[0][1] = data.myrotmtx[0][1];
			data.rotmtx[0][2] = data.myrotmtx[0][2];
			data.rotmtx[1][0] = data.myrotmtx[1][0];
			data.rotmtx[1][1] = data.myrotmtx[1][1];
			data.rotmtx[1][2] = data.myrotmtx[1][2];
			data.rotmtx[2][0] = data.myrotmtx[2][0];
			data.rotmtx[2][1] = data.myrotmtx[2][1];
			data.rotmtx[2][2] = data.myrotmtx[2][2];
		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to parse double from '" + tmpstr + "'");
		}
		repaint();
	}

	/**
	 * Opens a dialog that lets users set the size for circles representing selected sequences.
	 */
	private void openChangeSelectedCircleSizeDialog() {
		// set the size for the circles for selected sequences
		String tmpstr = "";
		try {
			tmpstr = JOptionPane.showInputDialog(this, "Enter the new size(int):", String.valueOf(data.ovalsize));
			if (tmpstr != null) {
				data.ovalsize = Integer.parseInt(tmpstr);
			}
		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to parse int from '" + tmpstr + "'");
		}
		repaint();
	}

	/**
	 * Opens a dialog that lets users pick a sequence for which BLAST is run against the present database. The region
	 * and e-values of the HSPs are then shown mapped to this sequence.
	 * <p>
	 * i.e. region 1-200 hits cluster A, region 210-300 cluster b, ergo 2 domains
	 */
	private void openShowBlastHitsForSequenceDialog() {
		int referenceseqnum = getsinglenamedialog.getrefseq(data.sequence_names);
		if (referenceseqnum == -1) {
			javax.swing.JOptionPane.showMessageDialog(this, "Please select a sequence");
			return;
		}

		// get the blast hits to this sequence
		hsp[] thishsp = (new viewblasthitsutils()).gethsps(referenceseqnum, data.sequences, data.cmd,
				data.formatdbpath, data.blastpath, data.addblastvbparam, data.referencedb, data.mineval,
				data.pvalue_threshold);
		
		// plot these blast hits on to the sequence
		viewblasthits myview = new viewblasthits(this, thishsp, referenceseqnum, data.sequence_names,
				data.sequences[referenceseqnum], data.nameshash);
		viewblasthitsvec.addElement(myview);
		
		myview.setVisible(true);
	}

	/**
	 * Opens a file chooser dialog that asks the user whether a selected file should be overwritten if it exists
	 * 
	 * @return The selected filename or "" if the dialog was canceled or closed.
	 */
	private String safe_output_file_chooser() {
		// this line makes using TAB possible to go from Yes to No in the FileChooser. Otherwise pressing Enter ALWAYS
		// selected YES, irrespective of the currently selected button
		UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

		JFileChooser jFileChooser = new JFileChooser() {
			/**
			 * For existing files, ask whether to overwrite or not or cancel the save.
			 * 
			 * This code snippet stems from: http://stackoverflow.com/a/3729157/454402
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void approveSelection() {
				File f = getSelectedFile();
				if (f.exists() && getDialogType() == SAVE_DIALOG) {
					int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
							JOptionPane.YES_NO_CANCEL_OPTION);
					switch (result) {
					case JOptionPane.YES_OPTION:
						super.approveSelection();
						return;
					case JOptionPane.NO_OPTION:
						return;
					case JOptionPane.CLOSED_OPTION:
						return;
					case JOptionPane.CANCEL_OPTION:
						cancelSelection();
						return;
					}
				}
				super.approveSelection();
			}
		};

		// set input filename as default output filename
		if (data.getAbsoluteInputfileName() != null) {
			jFileChooser.setSelectedFile(new File(data.getAbsoluteInputfileName()));
		}

		int returnVal = jFileChooser.showSaveDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			return jFileChooser.getSelectedFile().getPath();
		}

		return null; // signal "no file was selected" to caller
	}

	/**
	 * Saves the run to a CLANS format file and informs the user about progress and success in overlay and STDERR
	 * messages.
	 * 
	 * @param output_filename
	 *            The file to which the output is written.
	 */
	private void threaded_save_run(final String output_filename) {

		if (messageOverlayActive) {
			message_overlay.setSaving();	
		} else {
			System.out.println("Note: saving can be canceled by pressing escape");
		}

		if (output_filename == null) {
			if (messageOverlayActive) {
				message_overlay.setCanceled();
			}
			return;
		}
		
		final boolean restart_computation = stopComputation(true);
		final boolean restart_autosave = pauseAutosave();
		
		disableUserControls();

		if (saveLoadWorker != null) {
			saveLoadWorker.cancel(true); // kill potentially running old worker
		}
		
		saveLoadWorker = new SwingWorker<Void, Integer>() {

			@Override
			protected Void doInBackground() throws Exception {
				Thread.currentThread().setName("save in background");
				data.safer_save_to_file(output_filename, this);
				return null;
			}

			protected void done() {
				String error_message = "";
				boolean saving_successful = true;
				boolean saving_canceled = false;

				try {
					// we query the output of doInBackground only to get to the catch blocks in case of failures
					get();

				} catch (CancellationException e) {
					saving_successful = false;
					saving_canceled = true;
					error_message = "saving canceled";

				} catch (InterruptedException e) {
					saving_successful = false;
					error_message = e.getMessage();

				} catch (ExecutionException e) {
					saving_successful = false;
					
					Throwable exception = e.getCause();
					if (exception instanceof IOException) {
						error_message = exception.getMessage();

					} else if (exception instanceof IllegalStateException) {
						error_message = exception.getMessage();

					} else {
						error_message = "unexpected exception: " + e.getMessage();
					}

					System.err.println(error_message);
					
					if (messageOverlayActive){
						message_overlay.setFailed(error_message);
					}
				}

				if (saving_successful) {

					if (autosaveAutoReenable) {
						data.makeAutosaveAware();
						enableAutosave(true);
						autosaveAutoReenable = false;
					}

					setTitle("Clustering of " + data.getBaseInputfileName());

					if (messageOverlayActive) {
						message_overlay.setCompleted();
					}

					if (restart_computation) {
						startComputation();
					}

				} else {
					System.err.println(error_message);

					if (messageOverlayActive) {
						if (saving_canceled) {
							message_overlay.setCanceled();
						} else {
							message_overlay.setFailed(error_message);
						}
					}
					
					if (restart_autosave) {
						restartAutosave();
					}
				}

				enableUserControls();
			}
		};

		saveLoadWorker.execute();
	}

	/**
	 * Gets the filename associated with the currently open data.
	 * 
	 * @return The filename or null if the data is not associated with a filename yet.
	 */
	private String getSaveFilename() {
		return data.getAbsoluteInputfileName();
	}

	/**
	 * Opens a file chooser dialog to let the user pick an output file. If the file exists, the user will be asked
	 * whether to use it or select a different one or cancel.
	 * 
	 * @return The filename or null if the user closed/canceled the input dialog.
	 */
	private String getSaveAsFilename() {
		return safe_output_file_chooser();
	}

	/**
	 * Saves the data in CLANS format to the filename associated with it. Used by menu entry "File->Save Run".
	 * <p>
	 * If the data is not associated with a filename (e.g. CLANS parsed BLAST results), this falls back to
	 * {@code saveRunAs} behavior of showing the user a file choice dialog.
	 */
	private void saveRun() {
		String output_filename = getSaveFilename();

		if (output_filename == null) {
			output_filename = getSaveAsFilename();

			if (output_filename == null) {
				return;
			}
		}

		threaded_save_run(output_filename);
	}

	/**
	 * Saves the data in CLANS format to a user chosen file. Used by menu entry "File->Save Run As". If the file
	 * selection dialog is canceled/closed, no save file is created.
	 */
	private void saveRunAs() {
		String output_filename = getSaveAsFilename();

		if (output_filename == null) {
			return;
		}

		threaded_save_run(output_filename);
	}

	/**
	 * Saves the data in CLANS format to its associated file. Used by the autosave feature to save data on a regular
	 * basis. If the data is not associated with a filename (e.g. CLANS parsed BLAST results), activating the autosave
	 * feature is postponed until a manual save is successfully performed.
	 */
	private void autosave() {
		String output_filename = getSaveFilename();

		if (output_filename == null) {
			autosaveAutoReenable = true;
			disableAutosave(false);
			return;
		}

		threaded_save_run(output_filename);
	}

	private void loadDataClansFormat() {

		groupseqs = null;

		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			String filename = fc.getSelectedFile().getAbsolutePath();
			loadDataClansFormatInBackground(filename);

			if (myseqgroupwindow != null) {
				myseqgroupwindow.setVisible(false);
				myseqgroupwindow.dispose();
			}
			if (mymapfunctiondialog != null) {
				mymapfunctiondialog.setVisible(false);
				mymapfunctiondialog.dispose();
			}
			repaint();

		} else if (returnVal == JFileChooser.CANCEL_OPTION) {
			if (messageOverlayActive) {
				message_overlay.setCanceled();
			}
		}
	}

	/**
	 * Permanently deletes sequences based on a selection mask.
	 * 
	 * @param keep_mask
	 *            An array with true at indices of sequences that should be kept and false at indices of sequences that
	 *            should be deleted.
	 */
	private void deleteSequences(boolean[] keep_mask) {
		int counter = 0;
		// shift for sequence ids in groups
		int[] sequence_index_shift = new int[keep_mask.length];
		for (int i = 0; i < keep_mask.length; i++) {
			sequence_index_shift[i] = i - counter;
			if (keep_mask[i]) {
				counter++;
			}
		}

		if (data.selectednames.length < 2) {
			return;
		}

		if (shownames != null) {
			shownames.setVisible(false);
			shownames.dispose();
			shownames = null;
		}

		if (myseqgroupwindow != null) {
			myseqgroupwindow.setVisible(false);
			myseqgroupwindow.dispose();
			myseqgroupwindow = null;
		}

		if (viewblasthitsvec.size() > 0) {
			for (int i = 0; i < viewblasthitsvec.size(); i++) {
				blastselectseqs = new int[0];
				viewblasthitsvec.elementAt(i).setVisible(false);
				viewblasthitsvec.elementAt(i).dispose();
			}
			viewblasthitsvec.setSize(0);
		}

		level++;
		getparentmenuitem.setEnabled(true);
		getparentmenuitem.setText("use parent group (" + level + ")");
		parentnameshash.addElement(data.nameshash);

		// holds info about which name is which array number
		data.nameshash = new HashMap<String, Integer>((int) (data.selectednames.length / 0.75) + 1, (float) 0.75);
		for (int i = 0; i < data.selectednames.length; i++) {
			data.nameshash.put(data.sequences[data.selectednames[i]].name, new Integer(i));
		}

		if (data.blasthits != null) {
			parentblasthits.addElement(data.blasthits);
			data.blasthits = SelectedSubsetHandling.get_blasthits(data.blasthits, data.selectednames);
		} else {
			if (data.orgattvals == null) {
				// System.out.println("setting orgattval=myattvals");
				data.orgattvals = data.myattvals;
			}
			// System.out.println("rmsingletons: newattvals calculation");
			parent_orgattvals.addElement(data.orgattvals);
			data.orgattvals = SelectedSubsetHandling.get_myattvals(data.orgattvals, data.selectednames);
			data.compute_attraction_values();
		}

		parentmovearr.addElement(data.mymovearr);
		data.mymovearr = SelectedSubsetHandling.get_mymovearr(data.mymovearr, data.selectednames);
		if (data.mymovearr.length > 0) {
			// lastmovearr == previous_movements
			data.lastmovearr = new float[data.mymovearr.length][data.mymovearr[0].length];
		}

		// myposarr == positions
		parentposarr.addElement(data.myposarr);
		data.myposarr = SelectedSubsetHandling.get_myposarr(data.myposarr, data.selectednames);
		parentaln.addElement(data.sequences);
		data.sequences = SelectedSubsetHandling.get_sequences(data.sequences, data.selectednames);
		parentnamearr.addElement(data.sequence_names);
		data.sequence_names = SelectedSubsetHandling.get_namearr(data.sequence_names, data.selectednames);
		data.selectednames = new int[0];
		if (data.blasthits != null) {
			synchronized (data.myattvals) {
				data.compute_attraction_values();
			}
		}

		// fix the sequence ids stored in groups
		SequenceGroup current_seqgroup;
		Vector<Integer> keep_group_sequences;
		for (int i = 0; i < data.seqgroupsvec.size(); i++) {
			current_seqgroup = data.seqgroupsvec.elementAt(i);

			// collect and fix the sequence ids to keep for this group
			keep_group_sequences = new Vector<Integer>();
			for (int j = 0; j < current_seqgroup.sequences.length; j++) {
				if (keep_mask[current_seqgroup.sequences[j]]) {
					keep_group_sequences.add(current_seqgroup.sequences[j]
							- sequence_index_shift[current_seqgroup.sequences[j]]);
				}
			}

			// copy the ids over to the seqgroup
			current_seqgroup.sequences = new int[keep_group_sequences.size()];
			for (int j = 0; j < keep_group_sequences.size(); j++) {
				current_seqgroup.sequences[j] = keep_group_sequences.elementAt(j);
			}

		}

		data.elements = data.sequence_names.length;
		data.posarr = data.myposarr;
		data.posarrtmp = new float[data.elements][ClusterData.dimensions];
		data.drawarrtmp = new int[data.elements][ClusterData.dimensions];

		data.resetDrawOrder();
		repaint();
	}

	/**
	 * Permanently remove all currently selected sequences from the data.
	 */
	private void removeAllSelectedSequences() {

		boolean restart_computation = stopComputation(true);

		// selected_group_sequences == groupseqs
		groupseqs = null;

		int sequences = data.sequence_names.length;
		int[] sequences_to_remove = data.selectednames.clone();
		data.selectednames = new int[sequences - data.selectednames.length];
		boolean[] sequences_to_keep = new boolean[sequences];
		for (int i = 0; i < sequences; i++) {
			sequences_to_keep[i] = true;
		}

		for (int i = 0; i < sequences_to_remove.length; i++) {
			sequences_to_keep[sequences_to_remove[i]] = false;
		}

		int counter = 0;
		for (int i = 0; i < sequences; i++) {
			if (sequences_to_keep[i]) {
				data.selectednames[counter] = i;
				counter++;
			}
		}

		deleteSequences(sequences_to_keep);

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Permanently removes all sequences without connections at the currently selected p-value cutoff.
	 */
	private void hideSingletonsMenuItemActionPerformed() {

		boolean restart_computation = stopComputation(true);

		groupseqs = null;
		int sequence_number = data.sequence_names.length;
		boolean[] sequences_to_keep = new boolean[sequence_number];

		for (int i = 0; i < data.myattvals.length; i++) {
			if (data.myattvals[i].att != 0) {
				sequences_to_keep[data.myattvals[i].query] = true;
				sequences_to_keep[data.myattvals[i].hit] = true;
			}
		}

		int counter = 0;
		// shift for sequence ids in groups
		int[] sequence_index_shift = new int[sequence_number];
		for (int i = 0; i < sequence_number; i++) {
			sequence_index_shift[i] = i - counter;
			if (sequences_to_keep[i]) {
				counter++;
			}
		}

		data.selectednames = new int[counter];
		counter = 0;
		for (int i = 0; i < sequence_number; i++) {
			if (sequences_to_keep[i]) {
				data.selectednames[counter] = i;
				counter++;
			}
		}

		deleteSequences(sequences_to_keep);

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Go to super-set of currently shown subset (use all sequences from the level before).
	 */
	private void useGraphSuperset() {

		boolean restart_computation = stopComputation(true);

		groupseqs = null;
		if (shownames != null) {
			shownames.setVisible(false);
			shownames.dispose();
			shownames = null;
		}
		if (myseqgroupwindow != null) {
			myseqgroupwindow.setVisible(false);
			myseqgroupwindow.dispose();
			myseqgroupwindow = null;
		}
		if (viewblasthitsvec.size() > 0) {
			for (int i = 0; i < viewblasthitsvec.size(); i++) {
				blastselectseqs = new int[0];
				((viewblasthits) viewblasthitsvec.elementAt(i)).setVisible(false);
				((viewblasthits) viewblasthitsvec.elementAt(i)).dispose();
			}
			viewblasthitsvec.setSize(0);
		}
		level--;
		if (level == 0) {
			getparentmenuitem.setEnabled(false);
		}
		getparentmenuitem.setText("use parent group (" + level + ")");
		if (data.blasthits != null) {
			data.blasthits = (MinimalHsp[]) parentblasthits.elementAt(level);
			parentblasthits.removeElementAt(level);
		} else {
			data.orgattvals = (minattvals[]) parent_orgattvals.elementAt(level);
			parentblasthits.removeElementAt(level);
			data.myattvals = ClusterMethods.filter_attraction_values(data.orgattvals, data.pvalue_threshold);
		}
		data.mymovearr = (float[][]) parentmovearr.elementAt(level);
		if (data.mymovearr.length > 0) {
			data.lastmovearr = new float[data.mymovearr.length][data.mymovearr[0].length];
		}
		parentmovearr.removeElementAt(level);
		data.myposarr = (float[][]) parentposarr.elementAt(level);
		parentposarr.removeElementAt(level);
		data.sequences = (AminoAcidSequence[]) parentaln.elementAt(level);
		parentaln.removeElementAt(level);
		data.sequence_names = (String[]) parentnamearr.elementAt(level);
		parentnamearr.removeElementAt(level);
		data.nameshash = parentnameshash.elementAt(level);
		parentnameshash.removeElementAt(level);
		data.weights = (float[]) parentweights.elementAt(level);
		parentweights.removeElementAt(level);
		data.selectednames = new int[0];
		data.elements = data.sequence_names.length;
		if (data.blasthits != null) {
			synchronized (data.myattvals) {
				data.compute_attraction_values();
			}
		}
		if (myseqgroupwindow != null) {
			myseqgroupwindow.setVisible(false);
			myseqgroupwindow.dispose();
		}
		if (parentseqgroups.size() > level) {
			data.seqgroupsvec = parentseqgroups.remove(level);
		}
		if (mymapfunctiondialog != null) {
			mymapfunctiondialog.makenameshash();
		}
		data.posarr = data.myposarr;
		data.posarrtmp = new float[data.elements][ClusterData.dimensions];
		data.drawarrtmp = new int[data.elements][ClusterData.dimensions];
		data.resetDrawOrder();
		repaint();

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Go to sub-set of currently shown sequences (use all sequences from the level before).
	 */
	private void useGraphSubset() {

		boolean restart_computation = stopComputation(true);

		groupseqs = null;
		int selectednum = data.selectednames.length;
		if (selectednum < 2) {
			modifyButtonStartStopResume(null, true);
			return;
		}
		if (shownames != null) {
			shownames.setVisible(false);
			shownames.dispose();
			shownames = null;
		}
		if (myseqgroupwindow != null) {
			myseqgroupwindow.setVisible(false);
			myseqgroupwindow.dispose();
			myseqgroupwindow = null;
		}
		if (myseqgroupwindow != null) {
			myseqgroupwindow.setVisible(false);
			myseqgroupwindow.dispose();
			data.showseqgroups = false;
		}
		selectvec.clear();
		if (viewblasthitsvec.size() > 0) {
			for (int i = 0; i < viewblasthitsvec.size(); i++) {
				blastselectseqs = new int[0];
				((viewblasthits) viewblasthitsvec.elementAt(i)).setVisible(false);
				((viewblasthits) viewblasthitsvec.elementAt(i)).dispose();
			}
			viewblasthitsvec.setSize(0);
		}
		level++;
		getparentmenuitem.setEnabled(true);
		getparentmenuitem.setText("use parent group (" + level + ")");
		parentnameshash.addElement(data.nameshash);
		data.nameshash = new HashMap<String, Integer>((int) (selectednum / 0.75) + 1, (float) 0.75);// holds info about
																									// which name is
																									// which array
																									// number
		for (int i = 0; i < selectednum; i++) {
			data.nameshash.put(data.sequences[data.selectednames[i]].name, new Integer(i));
		}
		if (data.blasthits != null) {
			parentblasthits.addElement(data.blasthits);
			data.blasthits = SelectedSubsetHandling.get_blasthits(data.blasthits, data.selectednames);
		}
		parentmovearr.addElement(data.mymovearr);
		data.mymovearr = SelectedSubsetHandling.get_mymovearr(data.mymovearr, data.selectednames);
		parentposarr.addElement(data.myposarr);
		data.myposarr = SelectedSubsetHandling.get_myposarr(data.myposarr, data.selectednames);
		parentaln.addElement(data.sequences);
		data.sequences = SelectedSubsetHandling.get_sequences(data.sequences, data.selectednames);
		parentnamearr.addElement(data.sequence_names);
		data.sequence_names = SelectedSubsetHandling.get_namearr(data.sequence_names, data.selectednames);
		parentweights.addElement(data.weights);
		data.weights = SelectedSubsetHandling.get_weights(data.weights, data.selectednames);
		data.elements = data.sequence_names.length;
		if (data.blasthits == null) {
			if (data.orgattvals == null) {
				data.orgattvals = data.myattvals;
			}
			parent_orgattvals.addElement(data.orgattvals);
			data.orgattvals = SelectedSubsetHandling.get_myattvals(data.orgattvals, data.selectednames);
			data.myattvals = ClusterMethods.filter_attraction_values(data.orgattvals, data.pvalue_threshold);
		}
		parentseqgroups.addElement(data.seqgroupsvec);
		data.seqgroupsvec = new Vector<SequenceGroup>();
		data.selectednames = new int[0];

		synchronized (data.myattvals) {
			data.compute_attraction_values();
		}

		data.posarr = data.myposarr;
		data.posarrtmp = new float[data.elements][ClusterData.dimensions];
		data.drawarrtmp = new int[data.elements][ClusterData.dimensions];
		data.resetDrawOrder();
		if (mymapfunctiondialog != null) {
			mymapfunctiondialog.makenameshash();
		}

		repaint();

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Opens a windows with a score distribution plot. The plot differs for similarity data given as scores, p-values,
	 * or attraction values.
	 */
	private void openScoreDistributionPlot() {
		if (data.blasthits != null) {
			if (data.usescval) { // score mode
				eplotdialog eplot = new eplotdialog(data.blasthits, data.pvalue_threshold, true);
				eplot.setVisible(true);

			} else { // p-value mode
				eplotdialog eplot = new eplotdialog(data.blasthits, data.pvalue_threshold, false);
				eplot.setVisible(true);
			}
		} else { // attraction value mode
			attplotdialog attplot = new attplotdialog(data.myattvals, data.pvalue_threshold);
			attplot.setVisible(true);
		}
	}

	/**
	 * Saves the currently selected sequences in FASTA format to a user-chosen file.
	 */
	private void openSaveSequenceAsFastaDialog() {

		boolean restart_computation = stopComputation(true);

		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			AminoAcidSequence[] selectedseqs = getselectedseqs();
			if (selectedseqs.length == 0) {
				javax.swing.JOptionPane.showMessageDialog(this, "Please select some sequences");
			} else {
				printout.printfasta(selectedseqs, fc.getSelectedFile());
			}
		}

		if (restart_computation) {
			startComputation();
		}
	}

	/**
	 * Opens a dialog for changing connection colors.
	 */
	private void openChangeDotConnectionColorDialog() {
		DialogChangeConnectionColors.changecolor(this, data.colorarr);
		repaint();
	}

	/**
	 * Deselects everything if sequences are currently selected or selects all sequences if none are currently selected.
	 */
	private void button_clear_selectionActionPerformed() {

		if (!this.contains_data(true)) {
			return;
		}

		if (data.selectednames.length > 0) {// if sth. is selected clear the selection

			deselect_all_entries();

			if (shownames != null) {
				shownames.seqnamelist.setSelectedIndices(data.selectednames);// clear all selected
			}
			if (zoom) {
				zoom = false;
				button_zoom_on_selected.setText("Zoom on selected");
			}

		} else {// if nothing is selected, select all
			ArrayList<Integer> all_indices = new ArrayList<Integer>();
			for (int i = 0; i < data.sequences.length; i++) {
				all_indices.add(i);
			}

			set_selected(all_indices);

			if (shownames != null) {
				shownames.seqnamelist.setSelectedIndices(data.selectednames);
			}
		}

		repaint();
	}

	/**
	 * Zooms on selected sequences or resets zoom if no sequences are selected.
	 */
	private void button_zoom_on_selectedActionPerformed() {

		if (!this.contains_data(true)) {
			return;
		}

		if (zoom) {
			button_zoom_on_selected.setText("Zoom on selected");
			zoom = false;

		} else {
			button_zoom_on_selected.setText("Show all");
			zoom = true;
			if (data.selectednames.length < 1) {
				zoom = false;
				JOptionPane.showMessageDialog(null, "Please select some sequences", "Message",
						JOptionPane.INFORMATION_MESSAGE);
				button_zoom_on_selected.setText("Zoom on selected");
			}
		}

		data.zoomfactor = 1;
		this.updateZoom(-1);
	}

	
	/**
	 * Based on the GUI state, updates the select/move button label.
	 */
	private void updateSelectMoveButtonLabel() {
		if (button_select_move.isSelected()) {
			button_select_move.setText("SELECT/move");
		} else {
			button_select_move.setText("select/MOVE");
		}
	}

	/**
	 * Handles starting mouse dragging events in the draw area, i.e. the mouse button has just been pressed and the
	 * mouse started to move so it's not a click any more but a drag.
	 * 
	 * @param evt
	 *            The mouse event.
	 */
	private void graphpanelMousePressed(java.awt.event.MouseEvent evt) {
	
		if (mouseEventsDisabled) {
			return;
		}
	
		mouse_is_pressed = true;
		if (button_select_move.isSelected() == false) {
			if (evt.isAltDown() || evt.isControlDown() || evt.isMetaDown()) {// if I want to drag a sequence in 2d
				// move all selected sequences a certain amount
				moveseqs = true;
				mousestart[0] = evt.getX();
				mousestart[1] = evt.getY();
			} else if (evt.isShiftDown()) {
				mousestart[0] = evt.getX();
				mousestart[1] = evt.getY();
				translate[0] = draw1.xtranslate;
				translate[1] = draw1.ytranslate;
			} else {
				draw1.drawbox = true;
				mousestart[0] = evt.getX();
				mousestart[1] = evt.getY();
				// if mouse is inside a certain area rotate x,y; outside rotate z
				if (stereocheckboxmenuitem.isSelected() == false) {
					if ((mousestart[0] < draw1.xadd) || (mousestart[0] > draw1.drawwidth + draw1.xadd)
							|| (mousestart[1] < draw1.yadd) || (mousestart[1] > draw1.drawheight + draw1.yadd)) {
						draw1.rotatez = true;
					} else {
						draw1.rotatez = false;
					}
				} else {// if in stereo mode
					if ((mousestart[0] < draw1.xadd) || (mousestart[0] > draw1.drawwidth * 2 + draw1.xadd)
							|| (mousestart[1] < draw1.yadd) || (mousestart[1] > draw1.drawheight + draw1.yadd)) {
						draw1.rotatez = true;
					} else {
						draw1.rotatez = false;
					}
				}
				mousemove[0] = 0;
				mousemove[1] = 0;
			}
		} else {
			mousemove[0] = 0;
			mousemove[1] = 0;
			selectstart[0] = evt.getX() - draw1.xtranslate;
			selectstart[1] = evt.getY() - draw1.ytranslate;
			currmousepos[0] = selectstart[0];
			currmousepos[1] = selectstart[1];
		}
		repaint();
	}

	/**
	 * Handles mouse dragging events in the draw area during the dragging.
	 * 
	 * @param evt
	 *            The mouse event.
	 */
	private void graphpanelMouseDragged(java.awt.event.MouseEvent evt) {
	
		if (mouseEventsDisabled) {
			return;
		}
	
		if (button_select_move.isSelected() == false) {
			if (evt.isShiftDown()) {
				draw1.xtranslate = evt.getX() - mousestart[0] + translate[0];
				draw1.ytranslate = evt.getY() - mousestart[1] + translate[1];
			} else if (moveseqs) {

			} else {
				mousemove[0] = evt.getX() - mousestart[0];
				mousemove[1] = evt.getY() - mousestart[1];
			}
		} else {
			if (shownamesselectcheckbox.isSelected()) {
				if (shownames == null) {
					shownames = new WindowShowSelectedSequences(data.sequence_names, this);
					shownames.setVisible(true);
				}
				int[] tmpreg = new int[4];
				tmpreg[0] = selectstart[0];
				tmpreg[1] = selectstart[1];
				tmpreg[2] = evt.getX() - draw1.xtranslate;
				tmpreg[3] = evt.getY() - draw1.ytranslate;
				updatetmpselected(tmpreg);
			}
			mousemove[0] = 0;
			mousemove[1] = 0;
			currmousepos[0] = evt.getX() - draw1.xtranslate;
			currmousepos[1] = evt.getY() - draw1.ytranslate;
		}
		repaint();
	}

	/**
	 * Repaints the draw area.
	 */
	private void requestRepaint() {
		mousemove[0] = 0;
		mousemove[1] = 0;
		repaint();
	}

	/**
	 * Starts stopped or stops running computations when the start/stop/resume button is activated.
	 */
	private void toggleComputationRunning() {
		if (!this.contains_data(true)) {
			return;
		}

		if (isComputing()) {
			stopComputation(false);
		} else {
			startComputation();
		}

		repaint();
	}

	private void initializeGraphPositions() {
		if (!this.contains_data(true)) {
			return;
		}
		
		if (isComputing()) {
			stopComputation(true);
		}

		resetGraph();

		repaint();
	}

	/**
	 * Handles complete mouse dragging events in the draw area, i.e. the mouse button has been released after dragging.
	 * 
	 * @param evt
	 */
	private void graphpanelMouseReleased(java.awt.event.MouseEvent evt) {
		
		if (mouseEventsDisabled) {
			return;
		}
		
		draw1.drawbox = false;
		mousemove[0] = 0;
		mousemove[1] = 0;
		mouse_is_pressed = false;

		if (moveseqs) {
			moveseqs = false;
			int movex = evt.getX() - mousestart[0];
			int movey = evt.getY() - mousestart[1];
			moveselected(movex, movey);

		} else {
			if (button_select_move.isSelected()) {
				int[] tmpreg = new int[4];
				tmpreg[0] = selectstart[0];
				tmpreg[1] = selectstart[1];
				tmpreg[2] = evt.getX() - draw1.xtranslate;
				tmpreg[3] = evt.getY() - draw1.ytranslate;
				if (evt.isAltDown() || evt.isControlDown() || evt.isShiftDown()) {
					// modifier keys indicate deselection instead of selection
					updateselected(tmpreg, true);
				} else {
					updateselected(tmpreg, false);
				}
				clusterconf = null;
			}

			// deepcopy the myrotmtx to rotmtx (do NOT assign reference)
			data.rotmtx[0][0] = data.myrotmtx[0][0];
			data.rotmtx[0][1] = data.myrotmtx[0][1];
			data.rotmtx[0][2] = data.myrotmtx[0][2];
			data.rotmtx[1][0] = data.myrotmtx[1][0];
			data.rotmtx[1][1] = data.myrotmtx[1][1];
			data.rotmtx[1][2] = data.myrotmtx[1][2];
			data.rotmtx[2][0] = data.myrotmtx[2][0];
			data.rotmtx[2][1] = data.myrotmtx[2][1];
			data.rotmtx[2][2] = data.myrotmtx[2][2];
			draw1.tmprotmtx = new double[3][3];
		}
		repaint();
	}

	/**
	 * Shows a prompt whether the user really wants to exit and do so if approved.
	 */
	private void openReallyExitSafetyDialog() {

		Object[] options = { "Yes", "No"};
		int result = JOptionPane.showOptionDialog(this, "Really exit CLANS?\nUnsaved changes will be lost!",
				"Really exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[1]);

		switch (result) {
		case 0:
			System.exit(0);
		default:
			return;
		}
	}

	/**
	 * Opens a window that lets users add taxonomy information to their data.
	 * <p>
	 * This feature depends on files from the NCBI taxonomy database that users must supply themselves.
	 */
	private void openTaxonomyWindow() {
		if (taxonomydialog != null) {
			taxonomydialog.setVisible(false);
			taxonomydialog.dispose();
		}
		taxonomydialog = new ncbitaxonomydialog(this);
		taxonomydialog.setVisible(true);
	}

	/**
	 * Based on the GUI state, updates the internal state correctly to use complex or simple attraction values.
	 */
	private void updateUseComplexAttractionValuesState() {
		if (attvalcompcheckbox.isSelected()) {
			data.attvalsimple = false;
		} else {
			data.attvalsimple = true;
		}
	}

	/**
	 * Based on the GUI state, updates the internal state correctly to rescale or not attraction values.
	 */
	private void updateRescaleAttractionValuesState() {
		if (rescalepvaluescheckbox.isSelected()) {
			data.rescalepvalues = true;
		} else {
			data.rescalepvalues = false;
		}
	}

	/**
	 * Based on the GUI state, updates the internal state correctly to optimize only the selected subset of sequences.
	 */
	private void updateOptimizeOnlySelectedSequencesState() {
		if (moveselectedonly.isSelected()) {
			data.moveselectedonly = true;
		} else {
			data.moveselectedonly = false;
		}
	}

	/**
	 * Saves the attraction values to a user-chosen file.
	 */
	private void saveattvalsmenuitemActionPerformed() {

		boolean restart_computation = stopComputation(true);

		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			this.setTitle("Clustering of " + fc.getSelectedFile().getName());
			data.save_attraction_values_to_file(fc.getSelectedFile());
		}

		if (restart_computation) {
			startComputation();
		}
	}

	int level = 0;
	Vector<float[][]> parentmovearr = new Vector<float[][]>();
	Vector<float[][]> parentposarr = new Vector<float[][]>();
	Vector<AminoAcidSequence[]> parentaln = new Vector<AminoAcidSequence[]>();
	Vector<String[]> parentnamearr = new Vector<String[]>();
	Vector<HashMap<String, Integer>> parentnameshash = new Vector<HashMap<String, Integer>>();

	Vector<MinimalHsp[]> parentblasthits = new Vector<MinimalHsp[]>();
	Vector<minattvals[]> parent_orgattvals = new Vector<minattvals[]>();
	Vector<Vector<SequenceGroup>> parentseqgroups = new Vector<Vector<SequenceGroup>>();
	Vector<float[]> parentweights = new Vector<float[]>();

	Vector<viewblasthits> viewblasthitsvec = new Vector<viewblasthits>();
	Vector<selectclass> selectvec = new Vector<selectclass>();

	boolean zoom = false;
	boolean mouse_is_pressed = false;
	boolean moveseqs = false;// flag to set if I want to draw sequences in 3d-space
	boolean recalc = true;// synchronize drawing and calculating
	drawpanel draw1;

	/**
	 * dataLock prevents the GUI from drawing data that is currently chaning, e.g. while adjusting to new data after
	 * loading.
	 */
	final public Object dataLock = new Object();

	WindowShowSelectedSequences shownames;
	WindowEditGroups myseqgroupwindow;
	computethread mythread;

	int skiprounds = 1;
	int[] mousemove = new int[2];
	int[] translate = new int[2];
	int[] mousestart = new int[2];
	int[] selectstart = new int[2];
	int[] currmousepos = new int[2];

	int[] groupseqs = null;// used by seqgroupsdialog to show which group is currently selected
	java.awt.Color groupseqscolor = new java.awt.Color(0, 0, 0);
	int[] blastselectseqs = new int[0];
	float[] clusterconf = null;// confidence values for the selected sequences

	static final JFileChooser fc = new JFileChooser(new File("."));
	String repaint = null;

	WindowOptions options_window = null;
	rotationdialog myrotationdialog = null;// clans rotation values
	affydialog myaffydialog = null;// loads/shows affymetrix data
	mapfunctiondialog_tab mymapfunctiondialog = null;// loads/shows metabolic/functional mapping
	ncbitaxonomydialog taxonomydialog = null;// show the NCBI taxonomic keywords
	String namesdmp_file = null;// "names.dmp";
	String nodesdmp_file = null;// "nodes.dmp";
	ArrayList<java.io.File> mapfiles = new ArrayList<java.io.File>();
	ArrayList<java.io.File> lookupfiles = new ArrayList<java.io.File>();
	Vector<replicates> affyfiles = null;
	boolean usefoldchange = false;
	boolean avgfoldchange = false;
	boolean dotsfirst = false;

	ClusterData data = null;

	private javax.swing.JMenu menu_misc;
	private javax.swing.JMenuItem aboutmenuitem;
	private javax.swing.JMenuItem addseqsmenuitem;
	private javax.swing.JMenuItem affymenuitem;
	private javax.swing.JCheckBoxMenuItem antialiasingcheckboxmenuitem;
	private javax.swing.JCheckBoxMenuItem attvalcompcheckbox;
	private javax.swing.JPanel buttonpanel;
	private javax.swing.JMenuItem centermenuitem;
	private javax.swing.JMenuItem changebgcolormenuitem;
	private javax.swing.JMenuItem changeblastcolor;
	private javax.swing.JMenuItem changecolormenuitem;
	private javax.swing.JMenuItem changefgcolormenuitem;
	private javax.swing.JMenuItem changefontmenuitem;
	private javax.swing.JMenuItem changenumbercolor;
	private javax.swing.JMenuItem changeselectcolormenuitem;
	public javax.swing.JButton button_select_all_or_clear;
	private javax.swing.JCheckBoxMenuItem cluster2dbutton;
	private javax.swing.JMenuItem clustermenuitem;
	private javax.swing.JCheckBoxMenuItem colorfrustrationcheckbox;
	private javax.swing.JPanel drawbuttonpanel;
	private javax.swing.JMenu menu_draw;
	private javax.swing.JMenuItem evalueitem;
	private javax.swing.JMenu menu_file;
	private javax.swing.JMenuItem getblasthitsmenuitem;
	private javax.swing.JMenuItem getchildmenuitem;
	private javax.swing.JMenuItem getdotsizemenuitem;
	private javax.swing.JMenuItem getovalsizemenuitem;
	private javax.swing.JMenuItem getparentmenuitem;
	private javax.swing.JMenuItem getseqsforselectedhits;
	private javax.swing.JMenuItem getseqsmenuitem;
	private GuiMessageOverlay message_overlay;
	private Component originalGlassPane;
	private boolean messageOverlayActive;
	private javax.swing.JPanel graphpanel;
	private javax.swing.JMenu menu_help;
	private javax.swing.JMenuItem helpmenuitem;
	private javax.swing.JMenuItem remove_selected_sequences_menu_item;
	private javax.swing.JMenuItem hidesingletonsmenuitem;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JCheckBoxMenuItem lengthcolormenuitem;
	private javax.swing.JMenuItem loadalternatemenuitem;
	private javax.swing.JMenuItem loadgroupsmenuitem;
	private javax.swing.JMenuItem loadmenuitem;
	private javax.swing.JMenuItem loadtabsmenuitem;
	private javax.swing.JMenuItem mapmanmenuitem;
	private javax.swing.JButton button_cutoff_value;
	private javax.swing.JTextField textfield_threshold_value;
	private javax.swing.JTextField textfield_info_min_blast_evalue;
	private javax.swing.JCheckBoxMenuItem moveselectedonly;
	private javax.swing.JMenuItem printmenuitem;
	private javax.swing.JCheckBoxMenuItem rescalepvaluescheckbox;
	private javax.swing.JCheckBoxMenuItem messageOverlayActiveCheckbox;
	private javax.swing.JMenuItem rotationmenuitem;
	private javax.swing.JMenuItem save2dmenuitem;
	private javax.swing.JMenuItem saveattvalsmenuitem;
	private javax.swing.JMenuItem savemenuitem;
	private javax.swing.JMenuItem saveasmenuitem;
	private javax.swing.JMenuItem savemtxmenuitem;
	private javax.swing.JToggleButton button_select_move;
	private javax.swing.JMenuItem seqscoloring;
	private javax.swing.JMenuItem sequencesitem;
	private javax.swing.JMenuItem setrotmenuitem;
	private javax.swing.JCheckBoxMenuItem showblasthitnamescheckbox;
	private javax.swing.JCheckBox checkbox_show_connections;
	private javax.swing.JCheckBoxMenuItem showinfocheckbox;
	private javax.swing.JCheckBox checkbox_show_names;
	private javax.swing.JCheckBoxMenuItem shownamesselectcheckbox;
	private javax.swing.JCheckBox checkbox_show_numbers;
	private javax.swing.JMenuItem showoptionsmenuitem;
	private javax.swing.JCheckBoxMenuItem showorigcheckbox;
	private javax.swing.JButton button_show_selected;
	private javax.swing.JMenuItem showseqsmenuitem;
	private final String autosaveSetupLabel = "setup autosaving";
	private javax.swing.JMenuItem autosaveSetup;
	private javax.swing.JMenuItem skipdrawingrounds;
	private javax.swing.JButton button_initialize;
	private javax.swing.JMenuItem stereoanglemenuitem;
	private javax.swing.JCheckBoxMenuItem stereocheckboxmenuitem;
	private javax.swing.JButton button_start_stop_resume;
	private javax.swing.JMenuItem taxonomymenuitem;
	private javax.swing.JMenu menu_windows;
	private javax.swing.JButton button_zoom_on_selected;
	private javax.swing.JMenuItem zoommenuitem;

	/**
	 * Changes label and availability state of the start/resume/stop button.
	 * 
	 * @param label
	 *            The new label or null if the old label should be kept.
	 * @param enabled
	 *            True if the button should be enabled, false if disabled.
	 */
	private void modifyButtonStartStopResume(Object label, boolean enabled) {
		if (label != null) {
			if (label instanceof ButtonStartStopMessage) {
				button_start_stop_resume.setText(((ButtonStartStopMessage) label).get());
			} else {
				button_start_stop_resume.setText(label.toString());
			}
		}

		button_start_stop_resume.setEnabled(enabled);
	}

	/**
	 * Checks if we are currently computing new iterations of the clustering.
	 * 
	 * @return true iff the clustering is running
	 */
	protected boolean isComputing() {
		return mythread != null && mythread.isRunning();
	}

	/**
	 * Starts the computation.
	 * 
	 * @return true iff the computation was already running before the call.
	 */
	private boolean startComputation() {
		if (isComputing()) {
			return true;
		}

		if (!contains_data(true)) {
			return false;
		}

		updateOptionValuesFromOptionsWindow();

		initializeComputationThread(); // thread cannot be restarted
		mythread.start();

		modifyButtonStartStopResume(ButtonStartStopMessage.STOP, true);

		return false;
	}

	/**
	 * Stops the computation.
	 * 
	 * @return true iff the computation was running.
	 */
	private boolean stopComputation(boolean keep_button_disabled) {
		if (!isComputing()) {
			return false;
		}

		modifyButtonStartStopResume(null, false);

		mythread.interrupt(); // tell the thread to stop whenver possible next
		try {
			mythread.join(); // wait for thread to stop
		} catch (InterruptedException e) {
			/**
			 * thrown if the GUI thread is interrupted during the join, which makes the Exception meaningless as the
			 * whole program dies in that case.
			 */
		}

		if (!keep_button_disabled) {
			modifyButtonStartStopResume(ButtonStartStopMessage.RESUME, true);
		}

		return true;
	}

	/**
	 * Resets the graph to random sequence positions. This is used when and equal to just having loaded data off BLAST
	 * results.
	 */
	void resetGraph() {
		data.changedvals = false;

		updateOptionValuesFromOptionsWindow();

		if (!isComputing()) {
			repaint();
		}

		data.initialize();

		if (options_window != null) {
			options_window.currcoolfield.setText(String.valueOf(data.currcool));
		}
		modifyButtonStartStopResume(ButtonStartStopMessage.START, true);
		mousemove[0] = 0;
		mousemove[1] = 0;

		this.updateZoom(-1);
	}

	/**
	 * Starts a new thread that loads a CLANS file in the background to keep the GUI responsive. Once the loading is
	 * done, the GUI is briefly blocked from redrawing (read: it is frozen) to inject the new values into it.
	 * 
	 * @param filename
	 *            The CLANS file to be loaded.
	 */
	private void loadDataClansFormatInBackground(final String filename) {

		if (messageOverlayActive) {
			message_overlay.setLoading();
		} else {
			System.out.println("Note: loading can be canceled by pressing escape");
		}
		
		final boolean restart_computation = stopComputation(true);
		final boolean restart_autosave = pauseAutosave();
		
		disableUserControls();
	
		if (saveLoadWorker != null) {
			saveLoadWorker.cancel(true); // kill potentially running old worker
		}
		
		final saverunobject[] new_data = new saverunobject[1];
		final long start_time = System.currentTimeMillis();
		
		saveLoadWorker = new SwingWorker<Void, Integer>() {

			@Override
			protected Void doInBackground() throws Exception {
				Thread.currentThread().setName("load in background");
				new_data[0] = ClusterData.load_run_from_file(new java.io.File(filename), saveLoadWorker);
				return null;
			}

			@Override
			protected void done() {
				String error_message = "";
				boolean loading_successful = true;
				boolean loading_canceled = false;
				
				try {
					// we query the output of doInBackground only to get to the catch blocks in case of failures
					get();

				} catch (CancellationException e) {
					loading_successful = false;
					loading_canceled = true;
					error_message = "loading canceled";

				} catch (InterruptedException e) {
					loading_successful = false;
					error_message = e.getMessage();

				} catch (ExecutionException e) {
					loading_successful = false;

					Throwable exception = e.getCause();
					if (exception instanceof FileNotFoundException) {
						error_message = exception.getMessage();

					} else if (exception instanceof ParseException) {
						error_message = "line " + ((ParseException) exception).getErrorOffset() + ": "
								+ exception.getMessage();

					} else if (exception instanceof IOException) {
						error_message = exception.getMessage();

					} else {
						error_message = "unexpected exception: " + e.getMessage();
					}
				}

				// completion_time cannot be recorded here as loading_successful triggers further actions
				long completion_time;
				
				if (loading_successful) {

					synchronized (dataLock) {
						data.injectLoadedDataIntoExisting(filename, new_data[0]);
					}
					injectLoadedSetupIntoGui();
					
					// completion_time is slightly off as we must capture it before initializeAutosave to avoid wait
					// time during user interaction with a dialog opened in that method
					completion_time = System.currentTimeMillis();
					
					initializeAutosave();

					if (messageOverlayActive) {
						message_overlay.setCompleted();
					}

					if (restart_computation) {
						startComputation();
					}

				} else {
					completion_time = System.currentTimeMillis();
					
					System.err.println(error_message);

					if (messageOverlayActive) {
						if (loading_canceled) {
							message_overlay.setCanceled();
						} else {
							message_overlay.setFailed(error_message);
						}
					}
					
					if (restart_autosave) {
						restartAutosave();
					}
				}

				System.out.println("loading took " + String.format("%s", (completion_time - start_time) / 1000f)
						+ " seconds.\n");

				enableUserControls();
			}
		};

		saveLoadWorker.execute();
	}

	/**
	 * Loads clans run from file.
	 * 
	 * @param filename
	 *            The input file.
	 * @return true iff loading succeeded.
	 * @throws IOException
	 * @throws ParseException
	 * @throws FileNotFoundException
	 */
	void injectLoadedSetupIntoGui() {

		textfield_info_min_blast_evalue.setText(String.valueOf(data.maxvalfound));
		if (data.blasthits == null) {
			button_cutoff_value.setText("Use Attraction values better than");
			textfield_threshold_value.setText("0");
			savemtxmenuitem.setText("Save Attraction values as matrix");
			evalueitem.setText("Attraction value plot");
		} else {
			if (data.usescval) {
				button_cutoff_value.setText("Use SC-vals better than");
				textfield_threshold_value.setText("0");
				evalueitem.setText("SC-value plot");
			} else {
				button_cutoff_value.setText("Use P-values better than");
				textfield_threshold_value.setText("1");
				evalueitem.setText("P-value plot");
			}
		}

		if (data.complexatt == true) {
			attvalcompcheckbox.setSelected(true);
		} else {
			attvalcompcheckbox.setSelected(false);
		}
		if (data.cluster2d) {
			cluster2dbutton.setSelected(true);
		} else {
			cluster2dbutton.setSelected(false);
		}
		if (data.showinfo) {
			showinfocheckbox.setSelected(true);
		} else {
			showinfocheckbox.setSelected(false);
		}
		textfield_threshold_value.setText(String.valueOf(data.pvalue_threshold));

		if (options_window != null) {
			options_window.initialize_textfields();
		}

		textfield_info_min_blast_evalue.setText(String.valueOf(data.maxvalfound));
		this.setTitle("Clustering of " + data.getBaseInputfileName());

		this.updateZoom(-1);
	}

	boolean contains_data() {
		return this.data.mymovearr != null;
	}

	boolean contains_data(boolean showinfo) {
		if (this.contains_data()) {
			return true;
		}

		if (showinfo) {
			javax.swing.JOptionPane.showMessageDialog(this, "you have to load data first", "no data loaded",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return false;
	}

	void initaddedseqs(MinimalHsp[] blastvec, AminoAcidSequence[] allaln, String[] allnamearr,
			HashMap<String, Integer> allnameshash, int[] newnumarr, float[][] allposarr, float maxmove, double pval,
			boolean useselectedonly) {
		// initialize the necessary variable for the case where new sequences are added to an existing run.
		data.sequences = allaln;
		data.myposarr = allposarr;
		data.blasthits = blastvec;
		data.maxmove = maxmove;
		data.pvalue_threshold = pval;
		textfield_threshold_value.setText(String.valueOf(data.pvalue_threshold));
		textfield_info_min_blast_evalue.setText(String.valueOf(data.pvalue_threshold));
		data.selectednames = newnumarr;
		data.nameshash = allnameshash;
		data.sequence_names = allnamearr;
		data.elements = data.sequence_names.length;
		data.posarr = data.myposarr;
		data.lastmovearr = new float[data.elements][ClusterData.dimensions];
		data.mymovearr = new float[data.elements][ClusterData.dimensions];
		data.posarrtmp = new float[data.elements][ClusterData.dimensions];
		data.drawarrtmp = new int[data.elements][ClusterData.dimensions];
		data.resetDrawOrder();

		data.compute_attraction_values();

		moveselectedonly.setSelected(useselectedonly);
		int seqnum = data.sequences.length;
		System.out.println("seqnum=" + seqnum);
		data.seqlengths = new float[seqnum];
		float maxlength = 0;
		for (int i = 0; i < seqnum; i++) {
			data.seqlengths[i] = data.sequences[i].seq.replaceAll("-", "").length();
			if (data.seqlengths[i] > maxlength) {
				maxlength = data.seqlengths[i];
			}
		}// end for i
		for (int i = 0; i < seqnum; i++) {
			data.seqlengths[i] /= maxlength;
		}// end for i

	}// end initaddedseqs

	// --------------------------------------------------------------------------

	AminoAcidSequence[] getselectedseqs() {
		// get the currently selected sequences
		ArrayList<AminoAcidSequence> tmpvec = new ArrayList<AminoAcidSequence>();
		int sequences = data.selectednames.length;
		AminoAcidSequence curraaseq;
		for (int i = 0; i < sequences; i++) {
			curraaseq = new AminoAcidSequence();
			curraaseq.name = data.sequence_names[data.selectednames[i]];
			curraaseq.seq = data.sequences[data.selectednames[i]].seq;
			tmpvec.add(curraaseq);
		}
		AminoAcidSequence[] retarr = (AminoAcidSequence[]) tmpvec.toArray(new AminoAcidSequence[0]);
		return retarr;
	}// end getselectedseqs

	/**
	 * Updates the data with the values of all available fields of the options window.
	 */
	void updateOptionValuesFromOptionsWindow() {
		if (options_window == null) {
			return;
		}

		String tmpstr = "";

		try {
			tmpstr = options_window.attfield.getText();
			data.attfactor = Float.parseFloat(tmpstr);

			tmpstr = options_window.repfield.getText();
			data.repfactor = Float.parseFloat(tmpstr);

			tmpstr = options_window.dampfield.getText();
			data.dampening = Float.parseFloat(tmpstr);

			tmpstr = options_window.coolfield.getText();
			data.cooling = Double.parseDouble(tmpstr);

			tmpstr = options_window.currcoolfield.getText();
			data.currcool = Double.parseDouble(tmpstr);

			tmpstr = options_window.minattfield.getText();
			data.minattract = Double.parseDouble(tmpstr);

			tmpstr = options_window.maxmovefield.getText();
			data.maxmove = Float.parseFloat(tmpstr);

			tmpstr = options_window.attvalpowtextfield.getText();
			data.attvalpow = Integer.parseInt(tmpstr);

			tmpstr = options_window.repvalpowtextfield.getText();
			data.repvalpow = Integer.parseInt(tmpstr);

			tmpstr = options_window.roundstextfield.getText();
			data.roundslimit = Integer.parseInt(tmpstr);

		} catch (NumberFormatException e) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to parse number from '" + tmpstr + "'");
		}
	}

	/**
	 * Updates the selection given a selected area of the window. *
	 * 
	 * @param selected_region_corners
	 *            the selected region as {min X, max X, min Y, max Y}
	 * @param deselect
	 *            if true, selected points in the region will be deselected, otherwise the selection of all points will
	 *            be toggled
	 */
	void updateselected(int[] selected_region_corners, boolean deselect) {

		ArrayList<Integer> tmpselect = new ArrayList<Integer>();
		int minx = selected_region_corners[0];
		int maxx = selected_region_corners[2];
		int miny = selected_region_corners[1];
		int maxy = selected_region_corners[3];
		int tmpint;
		if (minx > maxx) {
			tmpint = maxx;
			maxx = minx;
			minx = tmpint;
		}
		if (miny > maxy) {
			tmpint = maxy;
			maxy = miny;
			miny = tmpint;
		}
		// now get all sequences that are within those 4 coords
		float[][] currpos = data.posarrtmp;
		for (int i = 0; i < data.elements; i++) {
			// see if this object is within the selected region
			if ((currpos[i][0] > minx) && (currpos[i][0] < maxx) && (currpos[i][1] > miny) && (currpos[i][1] < maxy)) {
				tmpselect.add(new Integer(i));
			}
		}// end for i
			// now select those that are not present in selectednames and deselect those that are
		int tmparrsize = tmpselect.size();
		Integer[] tmparr = (Integer[]) tmpselect.toArray(new Integer[0]);
		tmpselect.clear();
		int j;
		int selectedelements = data.selectednames.length;
		boolean isnew;
		for (int i = 0; i < tmparrsize; i++) {
			tmpint = tmparr[i].intValue();
			isnew = true;
			for (j = 0; j < selectedelements; j++) {
				if (data.selectednames[j] == tmpint) {// if this element was already selected
					isnew = false;// do not add it to the new Vector
					data.selectednames[j] = -1;// mark this as unselected
				}
			}// end for j
			if (isnew && (deselect == false)) {// if this element should be selected
				tmpselect.add(tmparr[i]);
			}
		}// end for i
		for (int i = 0; i < selectedelements; i++) {
			// now add those that were selected before
			if (data.selectednames[i] > -1) {
				tmpselect.add(new Integer(data.selectednames[i]));
			}
		}// end for i

		// now assign the new selecteds to selectelements and update shownamedialog
		set_selected(tmpselect);

		if (shownames != null) {
			shownames.setselected(data.selectednames, !shownames.showall);
		}
	}

	/**
	 * sets the selected entries and does some housekeeping for that.
	 * 
	 * @param new_selecteds
	 *            the indices of the selected entries
	 */
	protected void set_selected(ArrayList<Integer> new_selecteds) {
		data.selectednames = new int[new_selecteds.size()];
		for (int i = 0; i < new_selecteds.size(); i++) {
			data.selectednames[i] = new_selecteds.get(i).intValue();
		}

		if (myseqgroupwindow != null) { // update the highlighting of groups with selected sequences
			myseqgroupwindow.repaint();
		}

		set_selection_button_label();
	}

	/**
	 * deselects all entries.
	 */
	protected void deselect_all_entries() {
		set_selected(new ArrayList<Integer>());
	}

	void updatetmpselected(int[] tmpreg) {
		// as input this has 4 coordinates
		// what this does is to take all sequences that are within those coordinates
		// and select/deselect them. It also needs to update the selection in shownamedialog
		ArrayList<Integer> tmpselect = new ArrayList<Integer>();
		int minx = tmpreg[0];
		int maxx = tmpreg[2];
		int miny = tmpreg[1];
		int maxy = tmpreg[3];
		int tmpint;
		if (minx > maxx) {
			tmpint = maxx;
			maxx = minx;
			minx = tmpint;
		}
		if (miny > maxy) {
			tmpint = maxy;
			maxy = miny;
			miny = tmpint;
		}
		// now get all sequences that are within those 4 coords
		float[][] currpos = data.posarrtmp;
		for (int i = 0; i < data.elements; i++) {
			// see if this object is within the selected region
			if ((currpos[i][0] > minx) && (currpos[i][0] < maxx) && (currpos[i][1] > miny) && (currpos[i][1] < maxy)) {
				tmpselect.add(new Integer(i));
			}
		}// end for i
			// now select those that are not present in selectednames and deselect those that are
		int j;
		int selectedelements = data.selectednames.length;
		int[] tmpdeselected = new int[selectedelements];
		boolean isnew;
		for (int i = 0; i < tmpselect.size(); i++) {
			tmpint = ((Integer) tmpselect.get(i)).intValue();
			isnew = true;
			for (j = 0; j < selectedelements; j++) {
				if (data.selectednames[j] == tmpint) {
					isnew = false;
					tmpdeselected[j] = 1;
					break;
				}
			}// end for j
			if (isnew == false) {
				tmpselect.remove(i);
				i--;
			}
		}// end for loop through vector
			// now add the already selected names
		for (int i = selectedelements - 1; i >= 0; i--) {
			// now add those that were selected before
			if (tmpdeselected[i] != 1) {
				tmpselect.add(0, new Integer(data.selectednames[i]));
			}
		}// end for i
			// now assign the new selecteds to tmpselectelements and update shownamedialog
		int[] tmpselectednames = new int[tmpselect.size()];
		for (int i = tmpselect.size() - 1; i >= 0; i--) {
			tmpselectednames[i] = ((Integer) tmpselect.get(i)).intValue();
		}// end for i
		if (shownames != null) {
			shownames.setselected(tmpselectednames, true);
		}
	}// end updatetmpselected

	void moveselected(int xint, int yint) {
		// move selected sequences by movex and movy in 3d space
		// use the rotmtx to compute the x,y and z coord to move them by
		float x = (float) ((float) xint / draw1.xscale);
		float y = (float) ((float) yint / draw1.yscale);
		double[][] invrot = getinvrot(data.rotmtx);
		float[] move = new float[3];
		move[0] = (float) (invrot[0][0] * x + invrot[0][1] * y);
		move[1] = (float) (invrot[1][0] * x + invrot[1][1] * y);
		move[2] = (float) (invrot[2][0] * x + invrot[2][1] * y);
		// now move each of the selected seqs by that amount
		int selectedelements = data.selectednames.length;
		// for each of the selected sequences shift it's position in posarr by x,y,z coords.
		// selectednames is a int[] with the selected sequences indices
		for (int i = 0; i < selectedelements; i++) {
			data.myposarr[data.selectednames[i]][0] += move[0];
			data.myposarr[data.selectednames[i]][1] += move[1];
			data.myposarr[data.selectednames[i]][2] += move[2];
		}// end for i
	}// end moveselected

	static double[][] getinvrot(double[][] m) {
		// get the inverse of a 3x3 matrix inmtx
		double[][] retmtx = {
				{ ((m[1][1] * m[2][2]) - (m[2][1] * m[1][2])), -((m[0][1] * m[2][2]) - (m[2][1] * m[0][2])),
						((m[0][1] * m[1][2]) - (m[1][1] * m[0][2])) },
				{ -((m[1][0] * m[2][2]) - (m[2][0] * m[1][2])), ((m[0][0] * m[2][2]) - (m[2][0] * m[0][2])),
						-((m[0][0] * m[1][2]) - (m[1][0] * m[0][2])) },
				{ ((m[1][0] * m[2][1]) - (m[2][0] * m[1][1])), -((m[0][0] * m[2][1]) - (m[2][0] * m[0][1])),
						((m[0][0] * m[1][1]) - (m[1][0] * m[0][1])) } };
		return retmtx;
	}

	/**
	 * labels the selection button "Clear Selection" or "Select All" depending on whether sequences are selected.
	 */
	void set_selection_button_label() {
		if (data != null && data.selectednames != null && data.selectednames.length > 0) {
			button_select_all_or_clear.setText("Clear Selection");
		} else {
			button_select_all_or_clear.setText("Select All");
		}
	}

	class drawpanel extends JPanel implements java.awt.print.Printable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -9084230540557021638L;

		public drawpanel() {
			this.setOpaque(true);// should be set by default, but can't hurt to re-set
			this.setDoubleBuffered(true);// should be set by default, but can't hurt to re-set
		}

		void init() {
			setSize(graphpanel.getWidth(), graphpanel.getHeight());
			setBackground(new java.awt.Color(0.95f, 0.95f, 0.95f));
			data.posarr = new float[0][0];
			data.colorarr = makecolors(colornum);
			data.colorcutoffs = new float[colornum];
			if (data.usescval == true) {
				for (int i = 0; i < colornum; i++) {
					data.colorcutoffs[i] = (float) (((float) i / (float) (colornum)) * data.p2attfactor);
				}
			} else {
				for (int i = 0; i < colornum; i++) {
					data.colorcutoffs[i] = ((float) i / (float) (colornum));
				}
			}
			refdbstring = "";
			for (int i = 0; i < data.referencedb.length; i++) {
				refdbstring += data.referencedb[i] + "; ";
			}// end for i
				// set the drawbox coordinates (a box of size 1 in x,y,z space)
			box = new double[8][3];
			box[0][0] = 0;
			box[0][1] = 0;
			box[0][2] = 0;

			box[1][0] = 1;
			box[1][1] = 0;
			box[1][2] = 0;

			box[2][0] = 1;
			box[2][1] = 1;
			box[2][2] = 0;

			box[3][0] = 0;
			box[3][1] = 1;
			box[3][2] = 0;

			box[4][0] = 0;
			box[4][1] = 0;
			box[4][2] = 1;

			box[5][0] = 1;
			box[5][1] = 0;
			box[5][2] = 1;

			box[6][0] = 1;
			box[6][1] = 1;
			box[6][2] = 1;

			box[7][0] = 0;
			box[7][1] = 1;
			box[7][2] = 1;

			boxdata = new double[8][3];
			originpos[0] = 0;
			originpos[1] = 0;
		}

		int[] originpos = new int[2];
		int colornum = 10;

		boolean drawbox = false;

		HashMap<String, minattvals> frustration;// color code for the frustration of connections
		java.awt.Color[] frustcolorarr = new java.awt.Color[0];
		java.awt.Color bgcolor = new java.awt.Color(1f, 1f, 1f);
		java.awt.Color fgcolor = new java.awt.Color(0, 0, 0);
		java.awt.Color blastcirclecolor = new java.awt.Color(0, 1f, 0);

		int xadd = 25;
		int yadd = 25;
		int drawwidth;
		int drawheight;
		int stereoangle = 4;
		double[][] box;
		double[][] boxdata;
		double sinxz;
		double cosxz;
		double sinyz;
		double cosyz;
		double sinxy;
		double cosxy;
		double alphayz;
		double alphaxz;
		double alphaxy;
		double xscale = 1;
		double yscale = 1;
		boolean rotatez = false;
		double maxx = 1;
		double maxy = 1;
		double minx = 1;
		double miny = 1;
		double xmove = 0;
		double ymove = 0;
		double zmove = 0;

		int[] draw = new int[4];// the coordinates of the field selected by the mouse
		java.awt.Color selectedcolor = new java.awt.Color(1f, 0f, 0f);
		java.awt.Font myfont = this.getFont();
		java.awt.Font smallfont = new java.awt.Font("Monospace", 0, 9);
		int fontsize = myfont.getSize();
		int fontwidth = (int) (fontsize / 2);

		String refdbstring = "";
		java.awt.Color blasthitcolor = new java.awt.Color(0.4f, 0.4f, 0.4f);
		selectclass tmpselectclass;
		int xtranslate = 0;
		int ytranslate = 0;
		int[][] tmpposarr;
		int[] xposarr;
		int[] yposarr;
		int posnum;

		double[][] tmprotmtx = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };// new double[3][3];//the current mouse
																			// rotation

		java.awt.Color[] makecolors(int num) {
			java.awt.Color[] retarr = new java.awt.Color[num];// was num+1
			int worstred = 230;
			int worstgreen = 230;
			int worstblue = 230;
			int bestred = 0;
			int bestgreen = 0;
			int bestblue = 0;
			// now compute the stepwise gradient
			float redstep = ((float) (bestred - worstred)) / ((float) num);// were all three num+1
			float greenstep = ((float) (bestgreen - worstgreen)) / ((float) num);
			float bluestep = ((float) (bestblue - worstblue)) / ((float) num);
			for (int i = 0; i < num; i++) {
				retarr[i] = new java.awt.Color((int) (worstred + (i * redstep)), (int) (worstgreen + (i * greenstep)),
						(int) (worstblue + (i * bluestep)));
			}

			return retarr;
		}// end makecolors

		// ----------------------------------------------------------------------

		public int print(java.awt.Graphics g, java.awt.print.PageFormat pf, int pi)
				throws java.awt.print.PrinterException {
			double xscalel = pf.getImageableWidth() / ((double) graphpanel.getWidth());
			double yscalel = pf.getImageableHeight() / ((double) graphpanel.getHeight());
			if (pi >= 1) {
				return java.awt.print.Printable.NO_SUCH_PAGE;
			}
			java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
			g2d.translate(pf.getImageableX(), pf.getImageableY());
			g2d.scale(xscalel, yscalel);
			graphpanel.setDoubleBuffered(false);
			paintComponent(g2d);
			graphpanel.setDoubleBuffered(true);
			return java.awt.print.Printable.PAGE_EXISTS;
		}

		// ----------------------------------------------------------------------

		public void paintComponent(java.awt.Graphics g) {

			// the overlay has to be done first as otherwise the dataLock might prevent it from showing
			if (messageOverlayActive) {
				message_overlay.activate_overlay();
			}

			g.setFont(myfont);
			if (stereocheckboxmenuitem.isSelected()) {
				// draw the graph in stereo vision
				// draw the first picture as normal
				drawwidth = (int) ((graphpanel.getWidth() - xadd - xadd) / 2);
				drawheight = (int) (graphpanel.getHeight() - yadd - yadd);
				g.translate(xtranslate, ytranslate);
				g.setColor(bgcolor);
				g.fillRect(xadd - xtranslate, yadd - ytranslate, drawwidth + drawwidth, drawheight);
				drawwidth *= data.zoomfactor;
				drawheight *= data.zoomfactor;
				g.setColor(fgcolor);
				if (repaint != null) {// error loadign or similar
					g.drawString(repaint, (drawwidth / 2) - xtranslate, (drawheight / 2) - ytranslate);
				} else {
					// now rotate the graph by the stereoangle along the x-axis
					mousemove[0] += ((float) stereoangle / 360f) * drawwidth;
					getangles();

					if (contains_data()) {
						drawdata(g);
					}

					if (drawbox) {
						drawbox(g);
					}

					// now rotate back to what it was before
					mousemove[0] -= ((float) stereoangle / 360f) * (drawwidth);
				}
				if (showorigcheckbox.isSelected()) {
					g.setColor(java.awt.Color.red);
					g.drawString("X(0,0,0)", originpos[0], originpos[1]);
				}
				g.setColor(java.awt.Color.black);
				if (showinfocheckbox.isSelected()) {
					g.drawString(" INFO: blast=" + data.blastpath + " refdb=" + refdbstring, (colornum + 9) * fontwidth
							- xtranslate, fontsize - ytranslate);
					g.setFont(smallfont);
					g.drawString("x:" + minx + " to " + maxx, fontwidth - xtranslate, graphpanel.getHeight() - fontsize
							- 3 - ytranslate);
					g.drawString("y:" + miny + " to " + maxy, fontwidth - xtranslate, graphpanel.getHeight()
							- ytranslate - 3);
					g.drawString((float) ((int) (data.myrotmtx[0][0] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[0][1] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[0][2] * 100)) / 100, xadd - xtranslate, yadd + fontsize
							- ytranslate);
					g.drawString((float) ((int) (data.myrotmtx[1][0] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[1][1] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[1][2] * 100)) / 100, xadd - xtranslate, yadd + 2 * fontsize
							- ytranslate);
					g.drawString((float) ((int) (data.myrotmtx[2][0] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[2][1] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[2][2] * 100)) / 100, xadd - xtranslate, yadd + 3 * fontsize
							- ytranslate);
				}

				recalc = true;
				// and draw the second image
				drawwidth = (int) ((graphpanel.getWidth() - xadd - xadd) / 2);
				g.translate(xtranslate + drawwidth, ytranslate);
				drawwidth *= data.zoomfactor;
				g.setColor(fgcolor);
				if (repaint != null) {

				} else {
					getangles();

					if (contains_data()) {
						drawdata(g);
					}

					if (drawbox) {
						drawbox(g);
					}
				}
			} else {// don't draw in stereo
				drawwidth = (int) (graphpanel.getWidth() - xadd - xadd);
				drawheight = (int) (graphpanel.getHeight() - yadd - yadd);
				g.translate(xtranslate, ytranslate);
				g.setColor(bgcolor);
				g.fillRect(xadd - xtranslate, yadd - ytranslate, drawwidth, drawheight);
				drawwidth *= data.zoomfactor;
				drawheight *= data.zoomfactor;
				g.setColor(fgcolor);
				if (repaint != null) {
					g.drawString(repaint, (drawwidth / 2) - xtranslate, (drawheight / 2) - ytranslate);
				} else {
					getangles();
					if (contains_data()) {
						drawdata(g);
					}

					if (drawbox) {
						drawbox(g);
					}
				}
				if (showorigcheckbox.isSelected()) {
					g.setColor(java.awt.Color.red);
					g.drawString("X(0,0,0)", originpos[0], originpos[1]);
				}
				g.setColor(java.awt.Color.black);
				if (showinfocheckbox.isSelected()) {
					g.drawString(" INFO: blast=" + data.blastpath + " refdb=" + refdbstring, (colornum + 9) * fontwidth
							- xtranslate, fontsize - ytranslate);
					g.setFont(smallfont);
					g.drawString("x:" + minx + " to " + maxx, fontwidth - xtranslate, graphpanel.getHeight() - fontsize
							- 3 - ytranslate);
					g.drawString("y:" + miny + " to " + maxy, fontwidth - xtranslate, graphpanel.getHeight()
							- ytranslate - 3);
					g.drawString((float) ((int) (data.myrotmtx[0][0] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[0][1] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[0][2] * 100)) / 100, xadd - xtranslate, yadd + fontsize
							- ytranslate);
					g.drawString((float) ((int) (data.myrotmtx[1][0] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[1][1] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[1][2] * 100)) / 100, xadd - xtranslate, yadd + 2 * fontsize
							- ytranslate);
					g.drawString((float) ((int) (data.myrotmtx[2][0] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[2][1] * 100)) / 100 + ","
							+ (float) ((int) (data.myrotmtx[2][2] * 100)) / 100, xadd - xtranslate, yadd + 3 * fontsize
							- ytranslate);
				}
				recalc = true;
			}

			g.drawString("Round: " + data.rounds, (int) (graphpanel.getWidth() / 2) - xtranslate,
					graphpanel.getHeight() - fontsize - ytranslate);
		}

		void getangles() {
			// set alphaxy alphaxz and alphayz and their sin and cos (the rotation angles)
			// first compute the angle xy by taking the last rotaion angles and the last mouse movement
			// tmpvec contains movement of x,y,and z by the mouse)
			double[] tmpvec = { java.lang.Math.PI / 2 * ((double) mousemove[0] / (drawwidth / 2)),
					java.lang.Math.PI / 2 * ((double) mousemove[1] / (drawwidth / 2)), 0 };
			xmove = tmpvec[0] + 0.001;// avoid div0 errors
			ymove = tmpvec[1] + 0.001;
			// if rotatez the calculate the rotation angle for z axis
			if ((rotatez == true) || (cluster2dbutton.isSelected())) {
				zmove = ymove + xmove;
				tmprotmtx[0][0] = java.lang.Math.cos(zmove);
				tmprotmtx[0][1] = -java.lang.Math.sin(zmove);
				tmprotmtx[0][2] = 0;
				tmprotmtx[1][0] = java.lang.Math.sin(zmove);
				tmprotmtx[1][1] = java.lang.Math.cos(zmove);
				tmprotmtx[1][2] = 0;
				tmprotmtx[2][0] = 0;
				tmprotmtx[2][1] = 0;
				tmprotmtx[2][2] = 1;
			} else {// get the parameters for the xy rotation
				if (xmove == 0 && ymove == 0) {
					return;
				}
				double rotz = java.lang.Math.atan(ymove / xmove) - java.lang.Math.PI / 2;// the amount I have to rotate
																							// in xy
				double rotx = java.lang.Math.sqrt(xmove * xmove + ymove * ymove);// the amount to rotate by
				if ((xmove < 0)) {
					rotx = -rotx;
				}
				double cosrotz = java.lang.Math.cos(rotz);
				double sinrotz = java.lang.Math.sin(rotz);
				double cosrotx = java.lang.Math.cos(rotx);
				double sinrotx = java.lang.Math.sin(rotx);
				tmprotmtx[0][0] = ((cosrotz * cosrotz) + (-sinrotz * cosrotx * -sinrotz));
				tmprotmtx[0][1] = ((cosrotz * sinrotz) + (-sinrotz * cosrotx * cosrotz));
				tmprotmtx[0][2] = (-sinrotz * -sinrotx);
				tmprotmtx[1][0] = ((sinrotz * cosrotz) + (cosrotz * cosrotx * -sinrotz));
				tmprotmtx[1][1] = ((sinrotz * sinrotz) + (cosrotz * cosrotx * cosrotz));
				tmprotmtx[1][2] = (cosrotz * -sinrotx);
				tmprotmtx[2][0] = (sinrotx * -sinrotz);
				tmprotmtx[2][1] = (sinrotx * cosrotz);
				tmprotmtx[2][2] = cosrotx;
			}// end xy rotation
			xmove = 0;
			ymove = 0;
			zmove = 0;
			// now multiply tmprotmtx to rotmtx and save in myrotmtx
			data.myrotmtx[0][0] = (tmprotmtx[0][0] * data.rotmtx[0][0]) + (tmprotmtx[0][1] * data.rotmtx[1][0])
					+ (tmprotmtx[0][2] * data.rotmtx[2][0]);
			data.myrotmtx[0][1] = (tmprotmtx[0][0] * data.rotmtx[0][1]) + (tmprotmtx[0][1] * data.rotmtx[1][1])
					+ (tmprotmtx[0][2] * data.rotmtx[2][1]);
			data.myrotmtx[0][2] = (tmprotmtx[0][0] * data.rotmtx[0][2]) + (tmprotmtx[0][1] * data.rotmtx[1][2])
					+ (tmprotmtx[0][2] * data.rotmtx[2][2]);
			data.myrotmtx[1][0] = (tmprotmtx[1][0] * data.rotmtx[0][0]) + (tmprotmtx[1][1] * data.rotmtx[1][0])
					+ (tmprotmtx[1][2] * data.rotmtx[2][0]);
			data.myrotmtx[1][1] = (tmprotmtx[1][0] * data.rotmtx[0][1]) + (tmprotmtx[1][1] * data.rotmtx[1][1])
					+ (tmprotmtx[1][2] * data.rotmtx[2][1]);
			data.myrotmtx[1][2] = (tmprotmtx[1][0] * data.rotmtx[0][2]) + (tmprotmtx[1][1] * data.rotmtx[1][2])
					+ (tmprotmtx[1][2] * data.rotmtx[2][2]);
			data.myrotmtx[2][0] = (tmprotmtx[2][0] * data.rotmtx[0][0]) + (tmprotmtx[2][1] * data.rotmtx[1][0])
					+ (tmprotmtx[2][2] * data.rotmtx[2][0]);
			data.myrotmtx[2][1] = (tmprotmtx[2][0] * data.rotmtx[0][1]) + (tmprotmtx[2][1] * data.rotmtx[1][1])
					+ (tmprotmtx[2][2] * data.rotmtx[2][1]);
			data.myrotmtx[2][2] = (tmprotmtx[2][0] * data.rotmtx[0][2]) + (tmprotmtx[2][1] * data.rotmtx[1][2])
					+ (tmprotmtx[2][2] * data.rotmtx[2][2]);
		}// end getangles

		// -----------------------------------

		void drawbox(java.awt.Graphics g) {
			// draw a box onto the screen showing the current orientation of worldspace
			// do the xz rotation
			double minxl = data.myrotmtx[0][0] * box[0][0] + data.myrotmtx[0][1] * box[0][1] + data.myrotmtx[0][2]
					* box[0][2];
			double maxxl = minxl;
			double minyl = data.myrotmtx[1][0] * box[0][0] + data.myrotmtx[1][1] * box[0][1] + data.myrotmtx[1][2]
					* box[0][2];
			double maxyl = minyl;
			boxdata[3][0] = data.myrotmtx[0][0] * box[3][0] + data.myrotmtx[0][1] * box[3][1] + data.myrotmtx[0][2]
					* box[3][2];
			boxdata[3][1] = data.myrotmtx[1][0] * box[3][0] + data.myrotmtx[1][1] * box[3][1] + data.myrotmtx[1][2]
					* box[3][2];
			boxdata[3][2] = data.myrotmtx[2][0] * box[3][0] + data.myrotmtx[2][1] * box[3][1] + data.myrotmtx[2][2]
					* box[3][2];
			if (boxdata[3][0] < minxl) {
				minxl = boxdata[3][0];
			} else if (boxdata[3][0] > maxxl) {
				maxxl = boxdata[3][0];
			}
			if (boxdata[3][1] < minyl) {
				minyl = boxdata[3][1];
			} else if (boxdata[3][1] > maxyl) {
				maxyl = boxdata[3][1];
			}
			boxdata[2][0] = data.myrotmtx[0][0] * box[2][0] + data.myrotmtx[0][1] * box[2][1] + data.myrotmtx[0][2]
					* box[2][2];
			boxdata[2][1] = data.myrotmtx[1][0] * box[2][0] + data.myrotmtx[1][1] * box[2][1] + data.myrotmtx[1][2]
					* box[2][2];
			boxdata[2][2] = data.myrotmtx[2][0] * box[2][0] + data.myrotmtx[2][1] * box[2][1] + data.myrotmtx[2][2]
					* box[2][2];
			if (boxdata[2][0] < minxl) {
				minxl = boxdata[2][0];
			} else if (boxdata[2][0] > maxxl) {
				maxxl = boxdata[2][0];
			}
			if (boxdata[2][1] < minyl) {
				minyl = boxdata[2][1];
			} else if (boxdata[2][1] > maxyl) {
				maxyl = boxdata[2][1];
			}
			boxdata[7][0] = data.myrotmtx[0][0] * box[7][0] + data.myrotmtx[0][1] * box[7][1] + data.myrotmtx[0][2]
					* box[7][2];
			boxdata[7][1] = data.myrotmtx[1][0] * box[7][0] + data.myrotmtx[1][1] * box[7][1] + data.myrotmtx[1][2]
					* box[7][2];
			boxdata[7][2] = data.myrotmtx[2][0] * box[7][0] + data.myrotmtx[2][1] * box[7][1] + data.myrotmtx[2][2]
					* box[7][2];
			if (boxdata[7][0] < minxl) {
				minxl = boxdata[7][0];
			} else if (boxdata[7][0] > maxxl) {
				maxxl = boxdata[7][0];
			}
			if (boxdata[7][1] < minyl) {
				minyl = boxdata[7][1];
			} else if (boxdata[7][1] > maxyl) {
				maxyl = boxdata[7][1];
			}
			double xoffset = -minxl;
			double yoffset = -minyl;
			double xfac = 50;
			double yfac = 50;
			g.setColor(bgcolor);
			g.fillRect(xadd, yadd, (int) (xfac * 1.5), (int) (yfac * 1.5));
			g.setColor(fgcolor);
			// now I have the coordinates; now draw the box
			g.drawLine((int) ((boxdata[0][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[0][1] + yoffset) * yfac + yadd), (int) ((boxdata[3][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[3][1] + yoffset) * yfac + yadd));
			g.drawLine((int) ((boxdata[2][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[2][1] + yoffset) * yfac + yadd), (int) ((boxdata[3][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[3][1] + yoffset) * yfac + yadd));
			g.drawLine((int) ((boxdata[3][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[3][1] + yoffset) * yfac + yadd), (int) ((boxdata[7][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[7][1] + yoffset) * yfac + yadd));
			g.setColor(java.awt.Color.red);
			g.drawString("Y", (int) ((boxdata[0][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[0][1] + yoffset) * yfac + yadd));
			g.drawString("X", (int) ((boxdata[2][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[2][1] + yoffset) * yfac + yadd));
			g.drawString("0,0,0", (int) ((boxdata[3][0] + xoffset) * xfac + xadd), (int) ((boxdata[3][1] + yoffset)
					* yfac + yadd));
			g.drawString("Z", (int) ((boxdata[7][0] + xoffset) * xfac + xadd),
					(int) ((boxdata[7][1] + yoffset) * yfac + yadd));
		}// end drawbox

		// ---------------------------------------

		void makedrawdata() {
			// use the data from posarr and the rotation angles xz and yz to
			double xfac = 1;
			double yfac = 1;
			double xoffset;
			double yoffset;
			float[][] tposarrtmp = data.posarrtmp;
			float[][] posarr = data.posarr;
			double[][] myrotmtx = data.myrotmtx;
			// compute the new positions and save them to posarrtmp
			for (int i = data.elements; --i >= 0;) {
				// same as for boxdata
				tposarrtmp[i][0] = (float) (myrotmtx[0][0] * posarr[i][0] + myrotmtx[0][1] * posarr[i][1] + myrotmtx[0][2]
						* posarr[i][2]);
				tposarrtmp[i][1] = (float) (myrotmtx[1][0] * posarr[i][0] + myrotmtx[1][1] * posarr[i][1] + myrotmtx[1][2]
						* posarr[i][2]);
				data.drawarrtmp[i][0] = (int) tposarrtmp[i][0];
				data.drawarrtmp[i][1] = (int) tposarrtmp[i][1];
				// I don't need to calculate the 3rd dimention
				// tposarrtmp[i][2]=(float)(myrotmtx[2][0]*posarr[i][0]+myrotmtx[2][1]*posarr[i][1]+myrotmtx[2][2]*posarr[i][2]);
				// now add perspective using posarrtmp[i][2]
				// maxdiff is the maximum value for any coordinate in the system
			}// end for i
			int selectedelements = data.selectednames.length;
			if ((zoom) && (selectedelements > 0)) {// if zoom=true
				// only get maxx and maxy and minx and miny from selected sequences
				// I know I have at least one selected element (checked at zoombuttonactionperformed)
				maxx = tposarrtmp[data.selectednames[0]][0];
				maxy = tposarrtmp[data.selectednames[0]][1];
				minx = maxx;
				miny = maxy;
				for (int i = selectedelements; --i >= 0;) {// for (int i=1;i<selectedelements;i++){
					if (maxx < tposarrtmp[data.selectednames[i]][0]) {
						maxx = tposarrtmp[data.selectednames[i]][0];
					} else if (minx > tposarrtmp[data.selectednames[i]][0]) {
						minx = tposarrtmp[data.selectednames[i]][0];
					}
					if (maxy < tposarrtmp[data.selectednames[i]][1]) {
						maxy = tposarrtmp[data.selectednames[i]][1];
					} else if (miny > tposarrtmp[data.selectednames[i]][1]) {
						miny = tposarrtmp[data.selectednames[i]][1];
					}
				}// end for i
				xfac = drawwidth / (maxx - minx);
				yfac = drawheight / (maxy - miny);
				xoffset = (-minx);
				yoffset = (-miny);
				if (maxx - minx == 0) {
					System.out.println("isZero (in Zoom)!");
				}
				for (int i = 0; i < data.elements; i++) {
					tposarrtmp[i][0] = (float) (((tposarrtmp[i][0] + xoffset) * xfac) + xadd);
					tposarrtmp[i][1] = (float) (((tposarrtmp[i][1] + yoffset) * yfac) + yadd);
					data.drawarrtmp[i][0] = (int) tposarrtmp[i][0];
					data.drawarrtmp[i][1] = (int) tposarrtmp[i][1];
					// I don't need to calculate the 3rd dimention
				}// end for i
				originpos[0] = (int) ((xoffset * xfac) + xadd);
				originpos[1] = (int) ((yoffset * yfac) + yadd);
			} else {// if I don't want to zoom in on selected sequences
				maxx = tposarrtmp[0][0];
				maxy = tposarrtmp[0][1];
				minx = maxx;
				miny = maxy;
				for (int i = data.elements; --i >= 0;) {// for (int i=1;i<edelements;i++){
					if (maxx < tposarrtmp[i][0]) {
						maxx = tposarrtmp[i][0];
					} else if (minx > tposarrtmp[i][0]) {
						minx = tposarrtmp[i][0];
					}
					if (maxy < tposarrtmp[i][1]) {
						maxy = tposarrtmp[i][1];
					} else if (miny > tposarrtmp[i][1]) {
						miny = tposarrtmp[i][1];
					}
				}// end for i
				xfac = drawwidth / (maxx - minx);
				yfac = drawheight / (maxy - miny);
				xoffset = (-minx);
				yoffset = (-miny);
				for (int i = data.elements; --i >= 0;) {
					tposarrtmp[i][0] = (float) (((tposarrtmp[i][0] + xoffset) * xfac) + xadd);
					tposarrtmp[i][1] = (float) (((tposarrtmp[i][1] + yoffset) * yfac) + yadd);
					data.drawarrtmp[i][0] = (int) tposarrtmp[i][0];
					data.drawarrtmp[i][1] = (int) tposarrtmp[i][1];
					// I don't need to calculate the 3rd dimention
				}// end for i
				originpos[0] = (int) ((xoffset * xfac) + xadd);
				originpos[1] = (int) ((yoffset * yfac) + yadd);
			}// end if zoom==false
			xscale = xfac;
			yscale = yfac;
		}// end makedrawdata

		// ---------------------------------------

		void drawdata(java.awt.Graphics g1d) {
			synchronized (dataLock) {
				drawdata_synchronized(g1d);
			}
		}

		void drawdata_synchronized(java.awt.Graphics g1d) {

			java.awt.Graphics2D g = (java.awt.Graphics2D) g1d;
			if (antialiasingcheckboxmenuitem.isSelected()) {
				g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
			} else {
				g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
						java.awt.RenderingHints.VALUE_ANTIALIAS_OFF);
			}
			float halfdot = (float) (data.dotsize / 2);
			float halfoval = (float) (data.ovalsize / 2);
			float halfgroup = (float) (data.groupsize / 2);
			// old, slow for many elements
			int[][] tposarrtmp = data.drawarrtmp;
			int elements = data.elements;// tposarrtmp.length;//was posarr 19.1.04
			int dotsize = data.dotsize;
			int ovalsize = data.ovalsize;

			if (elements > 0) {
				makedrawdata();
				g.setColor(fgcolor);
				g.drawRect(xadd - xtranslate, yadd - ytranslate, graphpanel.getWidth() - (2 * xadd),
						graphpanel.getHeight() - (2 * yadd));
				if (checkbox_show_connections.isSelected()) {
					// here i am looking for a speedup by eliminating the multiple "for" loops
					// to this effect the drawing order is computed in an earlier step. here I just have to loop through
					// the 2d array and draw the elements in the according color
					// int colornum=colorarr.length;
					if (colorfrustrationcheckbox.isSelected() == false) {
						// color the lines by their blast P-value
						int j;
						int[] currdraw;
						int vecsize;
						if (data.draworder == null || data.draworder.size() < 1) {
							data.draworder = get_line_draw_order(data.myattvals, colornum);
						}
						for (int i = 0; i < colornum; i++) {
							vecsize = data.draworder.get(i).size();
							g.setColor(data.colorarr[i]);
							g.drawString("-", (5 + i) * fontwidth - xtranslate, fontsize - ytranslate);
							// draw all the vector elements
							for (j = vecsize; --j >= 0;) {
								currdraw = data.draworder.get(i).get(j);
								// g.drawLine((int)tposarrtmp[currdraw[0]][0],(int)tposarrtmp[currdraw[0]][1],(int)tposarrtmp[currdraw[1]][0],(int)tposarrtmp[currdraw[1]][1]);
								g.drawLine(tposarrtmp[currdraw[0]][0], tposarrtmp[currdraw[0]][1],
										tposarrtmp[currdraw[1]][0], tposarrtmp[currdraw[1]][1]);
							}// end for j
						}// end for i
						g.setColor(fgcolor);
						g.drawString("Worst", -xtranslate, fontsize - ytranslate);
						g.drawString("Best", ((colornum + 5) * fontwidth) - xtranslate, fontsize - ytranslate);
					} else {// if colorfrustrationcheckbox.isSelected()
							// color the lines by their "frustration" (i.e. too long or too short for P-value)
						frustration = getfrustration(data.myattvals, data.posarr);// get the frustration for each line
						int j;

						int vecsize;
						String key;
						int[] tmparr;
						minattvals currfrust;
						if (data.draworder.size() < 1) {
							data.draworder = get_line_draw_order(data.myattvals, colornum);// draw the lines in the same
																							// order as before
						}
						for (int i = 0; i < colornum; i++) {
							vecsize = data.draworder.get(i).size();
							// draw all the vector elements
							for (j = 0; j < vecsize; j++) {
								tmparr = (int[]) data.draworder.get(i).get(j);
								key = tmparr[0] + "_" + tmparr[1];

								if (frustration.containsKey(key)) {
									currfrust = (minattvals) frustration.get(key);
									if (currfrust.att > 0) {
										// then the coloring should be in blue(i.e. too short for attval)
										g.setColor(new java.awt.Color((1 - currfrust.att), (1 - currfrust.att), 1));
									} else {
										// then the coloring should be in red(i.e. too long for attval)
										g.setColor(new java.awt.Color(1, (1 + currfrust.att), (1 + currfrust.att)));
									}
									g.drawLine(tposarrtmp[currfrust.query][0], tposarrtmp[currfrust.query][1],
											tposarrtmp[currfrust.hit][0], tposarrtmp[currfrust.hit][1]);
								} else {
									System.err.println("no value for frustration key '" + key + "'");
								}
							}// end for j
						}// end for i
						g.setColor(fgcolor);
						g.drawString("Worst", -xtranslate, fontsize - ytranslate);
						g.drawString("Best", ((colornum + 5) * fontwidth) - xtranslate, fontsize - ytranslate);
					}
				}
				if (checkbox_show_names.isSelected()) {
					int selectednamesnum = data.selectednames.length;
					String[] namearr = data.sequence_names;
					if (selectednamesnum == 0) {
						for (int i = 0; i < elements; i++) {
							g.drawString(String.valueOf(i) + "-" + namearr[i], (int) tposarrtmp[i][0],
									(int) tposarrtmp[i][1]);
						}// end for i
					} else {
						for (int i = 0; i < selectednamesnum; i++) {
							g.drawString(String.valueOf(i) + "-" + namearr[data.selectednames[i]],
									(int) tposarrtmp[data.selectednames[i]][0],
									(int) tposarrtmp[data.selectednames[i]][1]);
						}// end for i
					}
				} else if (checkbox_show_numbers.isSelected()) {
					for (int i = 0; i < elements; i++) {
						g.drawString(String.valueOf(i), (int) tposarrtmp[i][0], (int) tposarrtmp[i][1]);
					}// end for i
				}
				if (dotsfirst == true) {
					// now draw the sequence dots
					if (lengthcolormenuitem.isSelected()) {
						float[] seqlengths = data.seqlengths;
						for (int i = 0; i < elements; i++) {
							g.setColor(new java.awt.Color(1 - seqlengths[i], 1 - seqlengths[i], seqlengths[i]));
							g.fillOval((int) (tposarrtmp[i][0] - halfdot), (int) (tposarrtmp[i][1] - halfdot), dotsize,
									dotsize);
						}
					} else {
						g.setColor(fgcolor);
						for (int i = 0; i < elements; i++) {
							g.fillOval((int) (tposarrtmp[i][0] - halfdot), (int) (tposarrtmp[i][1] - halfdot), dotsize,
									dotsize);
						}
					}
				}
				// draw the sequences from older selection steps
				// now draw the elements in the selectvec
				for (int i = selectvec.size() - 1; i >= 0; i--) {
					tmpselectclass = (selectclass) selectvec.elementAt(i);
					g.setColor(tmpselectclass.color);
					int tmpelements = tmpselectclass.selectednames.length;
					for (int j = 0; j < tmpelements; j++) {
						g.fillOval((int) (tposarrtmp[tmpselectclass.selectednames[j]][0] - halfoval),
								(int) (tposarrtmp[tmpselectclass.selectednames[j]][1] - halfoval), ovalsize, ovalsize);
					}
				}// end for i

				// draw the sequence groups
				if (data.showseqgroups) {
					SequenceGroup mygroup;
					for (int i = data.seqgroupsvec.size() - 1; i >= 0; i--) {
						mygroup = data.seqgroupsvec.elementAt(i);

						if (mygroup.hide == true) {
							continue;
						}

						g.setColor(mygroup.color);

						if (mygroup.type == 0) {
							halfgroup = (float) mygroup.size / 2;
							for (int j = mygroup.sequences.length - 1; j >= 0; j--) {

								if (mygroup.sequences[j] < elements) {
									g.fillOval((int) (tposarrtmp[mygroup.sequences[j]][0] - halfgroup),
											(int) (tposarrtmp[mygroup.sequences[j]][1] - halfgroup), mygroup.size,
											mygroup.size);

								} else {
									System.err.println("sequence number " + mygroup.sequences[j]
											+ " is not present in file; removing entry from group");
									mygroup.remove(j);
								}
							}// end for j

						} else {
							tmpposarr = mygroup.polygon;
							posnum = tmpposarr[2][0];// the number of points
							xposarr = new int[posnum];
							yposarr = new int[posnum];
							for (int j = mygroup.sequences.length - 1; j >= 0; j--) {

								if (mygroup.sequences[j] < elements) {
									for (int k = 0; k < posnum; k++) {
										xposarr[k] = (int) (tmpposarr[0][k] + tposarrtmp[mygroup.sequences[j]][0]);
										yposarr[k] = (int) (tmpposarr[1][k] + tposarrtmp[mygroup.sequences[j]][1]);
									}
									g.fillPolygon(xposarr, yposarr, posnum);

								} else {
									System.err.println("sequence number " + mygroup.sequences[j]
											+ " is not present in file; removing entry from group");
									mygroup.remove(j);
								}
							}
						}
					}
				}

				if (groupseqs != null) {// draw the sequences from the currently selected group
					g.setColor(groupseqscolor);
					for (int i = groupseqs.length; --i >= 0;) {
						g.fillOval((int) (tposarrtmp[groupseqs[i]][0] - halfoval),
								(int) (tposarrtmp[groupseqs[i]][1] - halfoval), ovalsize, ovalsize);
					}// end for i
				}
				// draw the current selection
				if (clusterconf == null) {
					g.setColor(selectedcolor);
					for (int i = data.selectednames.length - 1; i >= 0; i--) {
						g.fillOval((int) (tposarrtmp[data.selectednames[i]][0] - halfoval),
								(int) (tposarrtmp[data.selectednames[i]][1] - halfoval), ovalsize, ovalsize);
					}// end for selected
				} else {
					for (int i = data.selectednames.length - 1; i >= 0; i--) {
						if (clusterconf[i] < 0) {
							clusterconf[i] = 0;
						}
						g.setColor(new java.awt.Color((int) (selectedcolor.getRed() * clusterconf[i]),
								(int) (selectedcolor.getGreen() * clusterconf[i]),
								(int) (selectedcolor.getBlue() * clusterconf[i])));
						g.fillOval((int) (tposarrtmp[data.selectednames[i]][0] - halfoval),
								(int) (tposarrtmp[data.selectednames[i]][1] - halfoval), ovalsize, ovalsize);
					}// end for selected
				}
				g.setColor(fgcolor);
				for (int i = data.selectednames.length - 1; i >= 0; i--) {
					g.drawOval((int) (tposarrtmp[data.selectednames[i]][0] - halfoval),
							(int) (tposarrtmp[data.selectednames[i]][1] - halfoval), ovalsize, ovalsize);
				}// end for selected
				if (blastselectseqs.length > 0) {
					// draw these sequences as green dots
					g.setColor(blasthitcolor);
					if (showblasthitnamescheckbox.isSelected()) {
						// write the names to screen
						for (int i = blastselectseqs.length - 1; i >= 0; i--) {
							g.drawString(String.valueOf(blastselectseqs[i]), (int) tposarrtmp[blastselectseqs[i]][0],
									(int) tposarrtmp[blastselectseqs[i]][1]);
						}// end for i
					}// end if show names for blasthits
					g.setColor(blastcirclecolor);
					for (int i = blastselectseqs.length - 1; i >= 0; i--) {
						g.fillOval((int) (tposarrtmp[blastselectseqs[i]][0] - halfoval),
								(int) (tposarrtmp[blastselectseqs[i]][1] - halfoval), ovalsize, ovalsize);
					}// end for selected
						// now draw the blast query
					g.setColor(java.awt.Color.magenta);
					g.fillOval((int) (tposarrtmp[blastselectseqs[0]][0] - halfoval),
							(int) (tposarrtmp[blastselectseqs[0]][1] - halfoval), ovalsize, ovalsize);
					// now draw the black outline
					g.setColor(fgcolor);
					for (int i = blastselectseqs.length - 1; i >= 0; i--) {
						g.drawOval((int) (tposarrtmp[blastselectseqs[i]][0] - halfoval),
								(int) (tposarrtmp[blastselectseqs[i]][1] - halfoval), ovalsize, ovalsize);
					}// end for selected
				}
				if (dotsfirst == false) {
					// now draw the sequence dots
					if (lengthcolormenuitem.isSelected()) {
						float[] seqlengths = data.seqlengths;
						for (int i = 0; i < elements; i++) {
							g.setColor(new java.awt.Color(1 - seqlengths[i], 1 - seqlengths[i], seqlengths[i]));
							g.fillOval((int) (tposarrtmp[i][0] - halfdot), (int) (tposarrtmp[i][1] - halfdot), dotsize,
									dotsize);
						}
					} else {
						g.setColor(fgcolor);
						for (int i = 0; i < elements; i++) {
							g.fillOval((int) (tposarrtmp[i][0] - halfdot), (int) (tposarrtmp[i][1] - halfdot), dotsize,
									dotsize);
						}
					}
				}
			}// end if elements>0
			if (button_select_move.isSelected() && mouse_is_pressed) {
				g.setColor(java.awt.Color.orange);
				if (currmousepos[0] < selectstart[0]) {
					draw[0] = currmousepos[0];
					draw[2] = selectstart[0];
				} else {
					draw[0] = selectstart[0];
					draw[2] = currmousepos[0];
				}
				if (currmousepos[1] < selectstart[1]) {
					draw[1] = currmousepos[1];
					draw[3] = selectstart[1];
				} else {
					draw[1] = selectstart[1];
					draw[3] = currmousepos[1];
				}
				g.drawRect(draw[0], draw[1], (draw[2] - draw[0]), (draw[3] - draw[1]));
			}
		}

		/**
		 * bin the lines connecting datapoints by the color they are assigned (makes subsequent drawing quicker)
		 * 
		 * @param myattvals
		 * @param colornum
		 * @return
		 */
		ArrayList<ArrayList<int[]>> get_line_draw_order(minattvals[] myattvals, int colornum) {

			ArrayList<ArrayList<int[]>> retarr = new ArrayList<ArrayList<int[]>>();
			for (int i = 0; i < colornum; i++) {
				retarr.add(new ArrayList<int[]>());
			}
			int[] mydraw = new int[2];// connection between sequences i & j

			float[] colorcutoffs = data.colorcutoffs;
			for (int i = myattvals.length; --i >= 0;) {

				if ((myattvals[i].att == 0) || (myattvals[i].att <= colorcutoffs[0])) {// if I am below the lowest to
																						// draw
					continue;
				}

				for (int j = 0; j < 10; j++) {
					if (j < 9) {
						if (myattvals[i].att <= colorcutoffs[j + 1]) { // from j to j + 1
							mydraw = new int[2];
							mydraw[0] = myattvals[i].query;
							mydraw[1] = myattvals[i].hit;
							retarr.get(j).add(mydraw);
							break;
						}
					} else { // j == 9
						if (myattvals[i].att > colorcutoffs[j]) { // better than value at j
							mydraw = new int[2];
							mydraw[0] = myattvals[i].query;
							mydraw[1] = myattvals[i].hit;
							retarr.get(j).add(mydraw);
							break;
						}
					}
				}
			}

			return retarr;
		}// end getdraworder

		// ----------------------------------------------------------------------

		HashMap<String, minattvals> getfrustration(minattvals[] attvals, float[][] posarr) {
			// get wether each connection is longer or shorter than expected based on the attraction value
			int seqnum = attvals.length;
			HashMap<String, minattvals> retarr = new HashMap<String, minattvals>((int) (seqnum / 0.8) + 1, 0.8f);
			minattvals[] minattarr = new minattvals[seqnum];
			int q, h;
			float tmpfloat;
			float att;
			float tmpx, tmpy, tmpz;
			float avgatt = 0;
			float avgdist = 0;
			float maxval = 0;
			int counter = 0;
			float tmpval;
			String key;
			for (int i = 0; i < seqnum; i++) {
				att = attvals[i].att;
				q = attvals[i].query;
				h = attvals[i].hit;
				key = q + "_" + h;
				if (att > 0) {// else don't do the calculations as I won't be drawing a line
					counter++;
					tmpx = posarr[q][0] - posarr[h][0];
					tmpy = posarr[q][1] - posarr[h][1];
					tmpz = posarr[q][2] - posarr[h][2];
					tmpval = (float) java.lang.Math.sqrt(tmpx * tmpx + tmpy * tmpy + tmpz * tmpz);
					minattarr[i] = new minattvals(q, h, tmpval);
					retarr.put(key, minattarr[i]);
					avgdist += tmpval;
					avgatt += att;
				}
			}// end for i
			avgdist /= counter;
			avgatt /= counter;
			tmpfloat = avgatt * avgdist;
			// note: the higher the attval, the smaller the dist!
			for (int i = 0; i < seqnum; i++) {
				att = attvals[i].att;
				q = attvals[i].query;
				h = attvals[i].hit;
				if (att > 0) {
					minattarr[i].att = (tmpfloat - (att * minattarr[i].att));
					if (maxval < minattarr[i].att) {
						maxval = minattarr[i].att;
					}
					if (maxval < -minattarr[i].att) {
						maxval = -minattarr[i].att;
					}
				}
			}// end for i

			for (int i = 0; i < seqnum; i++) {
				if (attvals[i].att > 0) {
					minattarr[i].att /= maxval;
				}// else I have a null entry
			}// end fo ri
			return retarr;
		}// end getfrustration
	}// end class drawpanel

	/**
	 * Class that handles the actual iteration computation.
	 */
	class computethread extends java.lang.Thread {

		/**
		 * Creates a new thread instance for computing iterations of the algorithm with custom thread name.
		 * <p>
		 * Note: Named threads are easier to debug! On a command line run command "jps -l" to get the programs process
		 * id (first column). Then run "jstack <process id>" to see all threads of your process.
		 * 
		 * @param parent
		 *            The GUI to which this thread belongs.
		 * @param name
		 *            The name of the thread.
		 */
		public computethread(ClusteringWithGui parent, String name) {
			this.parent = parent;
			this.didrun = false;
			this.stop = false;
			this.setName(name);
		}
		
		/**
		 * Creates a new thread instance for computing iterations of the algorithm with thead name "computation thread".
		 * <p>
		 * Note: Named threads are easier to debug! On a command line run command "jps -l" to get the programs process
		 * id (first column). Then run "jstack <process id>" to see all threads of your process.
		 * 
		 * @param parent
		 *            The GUI to which this thread belongs.
		 */
		public computethread(ClusteringWithGui parent) {
			this(parent, "computation thread");
		}

		boolean stop = true;
		boolean didrun = false;
		String tmpstr = "";
		float tmpcool = 1;
		final Object syncon = new Object(); // dummy object to sync on
		ClusteringWithGui parent;

		protected boolean isRunning() {
			return this.isAlive();
		}

		@Override
		public void run() {
			this.didrun = true;
			data.roundsdone = 0;
			while (!Thread.currentThread().isInterrupted()) {
				data.rounds++;
				if (data.roundslimit != -1) {
					data.roundsdone++;

					modifyButtonStartStopResume("STOP (" + data.roundsdone + "/" + data.roundslimit + ")", true);

					if (data.roundsdone >= data.roundslimit) {
						stop = true;
						synchronized (parent) {
							parent.notify();
						}
					}
				}
				// first see whether the main window has the focus
				if (graphpanel.isFocusOwner()) {
					// mainwindow has the focus, then see whether I am done drawing
					try {
						while (recalc == false) {
							Thread.sleep(100);
						}
					} catch (InterruptedException ie) {
						System.err.println("Interrupted sleep in computethread");
					}
				}

				if (data.changedvals) {
					updateOptionValuesFromOptionsWindow();
					data.changedvals = false;
				}

				ClusterMethods.recluster3d(parent.data);

				if (level == 0 && data.save_intermediate_results) {
					try {
						PrintWriter outwriter = new PrintWriter(new BufferedWriter(new FileWriter(
								data.getIntermediateResultfileName())));

						outwriter.println("sequences: " + data.myposarr.length);
						outwriter.println("values: ");
						outwriter.println("minattract " + data.minattract);
						outwriter.println("maxmove " + data.maxmove);
						outwriter.println("cooling " + data.cooling);
						outwriter.println("currcool " + data.currcool);
						outwriter.println("mineval " + data.mineval);
						outwriter.println("minpval " + data.pvalue_threshold);
						outwriter.println("repfactor " + data.repfactor);
						outwriter.println("attfactor " + data.attfactor);
						outwriter.println("dampening " + data.dampening);
						outwriter.println("rounds " + data.rounds);

						printout.save_semicolon_delimited_positions(outwriter, data.myposarr);

						outwriter.flush();
						outwriter.close();

					} catch (IOException e) {
						System.err.println("unable to save positions to " + data.getIntermediateResultfileName());
						e.printStackTrace();
					}
				}

				data.posarr = data.myposarr;
				tmpcool = (((float) ((int) (data.currcool * 100000))) / 100000);
				if (options_window != null) {
					options_window.currcoolfield.setText(String.valueOf(tmpcool));
				}
				if (tmpcool <= 1e-5) {
					stop = true;
					modifyButtonStartStopResume("DONE (absolute zero)", true);
				}
				if (data.rounds % skiprounds == 0 && data.nographics == false) {
					recalc = false;
					repaint();
				}
			}// end while
			synchronized (syncon) {
				modifyButtonStartStopResume(ButtonStartStopMessage.RESUME, true);
			}
			parent.repaint();
		}// end run

	}
}
