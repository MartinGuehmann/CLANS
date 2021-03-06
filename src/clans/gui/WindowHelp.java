package clans.gui;

public class WindowHelp extends javax.swing.JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6837885059895247704L;

	private javax.swing.JTextArea helptextarea;
	private javax.swing.JButton jButton1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField toptextfield;

	public WindowHelp(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
	}

	private void initComponents() {
		jPanel1 = new javax.swing.JPanel();
		toptextfield = new javax.swing.JTextField();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		helptextarea = new javax.swing.JTextArea();
		helptextarea.setText(helptext);
		jPanel3 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

		toptextfield.setBackground(new java.awt.Color(255, 255, 255));
		toptextfield.setEditable(false);
		toptextfield.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		toptextfield.setText("Help");
		jPanel1.add(toptextfield);

		getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

		jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.X_AXIS));

		jPanel2.setPreferredSize(new java.awt.Dimension(600, 400));
		jScrollPane1.setViewportView(helptextarea);

		jPanel2.add(jScrollPane1);

		getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

		jPanel3.setLayout(new java.awt.GridLayout());

		jButton1.setText("OK (Close)");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jPanel3.add(jButton1);

		getContentPane().add(jPanel3, java.awt.BorderLayout.SOUTH);

		pack();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed
		this.setVisible(false);
		dispose();
	}

	static String helptext = "Short help for CLANS\n"
			+ "For more help see the README.txt and clans.conf files\n"
			+ "Always also take a look at the command line as most error messages (still) appear there.\n"
			+ "\n"
			+ "    Graphical User Interface (GUI) Options\n"
			+ "\n"
			+ "MOUSE:\n"
			+ "   Press on gray outline & drag to rotate around an axis perpendicular to the screen.\n"
			+ "   Press & drag inside 3d graph widow to rotate around a horizontal or vertical axis.\n"
			+ "   If BUTTON Select/Move is highlighted: Press & drag to select all sequences in a region.\n"
			+ "   Holding \"shift\" and dragging the mouse pans the view\n"
			+ "   Holding \"alt\" or \"ctrl\" or \"meta\" and dragging the mouse either deselects all sequences\n"
			+ "      in selected region (if in selection mode)\n"
			+ "      or moves the selected sequence according to the mouse movement (if in movement mode).\n"
			+ "\n"
			+ "BUTTONS:\n"
			+ "   -Initialize: randomize the sequence positions prior to clustering.\n"
			+ "   -Start run/Stop/Resume: Start, Stop or Resume a clustering run.\n"
			+ "   -Select/Move: toggle between selecting sequences & rotating the 3d-space using the mouse.\n"
			+ "   -Show selected sequences: open a window showing the names of the currently selected sequences.\n"
			+ "   -Zoom on selected: Focus on the selected sequences. NOTE: the other sequences are still displayed\n"
			+ "       and part of the dataset. To actually remove unselected sequences from the dataset \n"
			+ "       use: Menu:Misc:Use selected subset.\n"
			+ "   -Select all/Clear selection: Select all/none of the sequences\n"
			+ "   -Use P-values better than (text field): remove all connections between sequences with P-values \n"
			+ "       worse than specified (text field next to it) from the graph and ignore them in the calculations.\n"
			+ "   -Maxmove (when using x-forwarding on a solaris machine, the \"return\" key is problematic. \n"
			+ "       Pressing this button causes the program to read the data from all text fields (except \"use P-values\")\n"
			+ "       and updates the program accordingly)\n"
			+ "\n"
			+ "CHECKBOXES:\n"
			+ "   -Show names:display the sequence names in the graph\n"
			+ "   -Show numbers: display the sequence numbers in the graph\n"
			+ "   -Show blast hits: Draw connections between all sequences with BLAST hits. Color of the line reflects the \n"
			+ "       P-value for this connection.\n"
			+ "\n"
			+ "TEXTFIELDS:\n"
			+ "   NOTE: The program disregards any changes made to the text fields until \"return/enter\" is pressed.\n"
			+ "   -Cooling:A multiplier for maxmove (see below). if set to <1, maxmove slowly converges to 0 (no further movement);\n"
			+ "       if >1, Maxmove ->> infinity.\n"
			+ "   -Dampening:If dampening=0, the movement vector this sequence had in the last round has no influence on this\n"
			+ "       round's movement. Dampening=1; last round's movement counts as much as this round's force vector\n"
			+ "       (they are simply added);(0<x<1; how much does movement last round's movement influence this round's?)\n"
			+ "   -Maxmove:maximum distance a point is allowed to travel per round.\n"
			+ "   -Attval value :Multiplier for the attractive forces between two sequences.\n"
			+ "   -Attval exponent :determines how the attractive force scales with distance\n"
			+ "       (default=2, attraction increases with distance squared)\n"
			+ "   -Repval value :Multiplier for the repulsive forces between two sequences.\n"
			+ "   -Repval exponent :determines how the repulsive force scales with distance\n"
			+ "       (default=2, attraction increases with 1/(distance squared))\n"
			+ "   -Min. attraction:A minimal force that attracts each sequence towards the origin of the graph\n"
			+ "       (to keep unconnected clusters/sequences from drifting apart indefinitely).(scales linearly with distance)\n"
			+ "\n"
			+ "NOTE: using the Attval&Repval value and exponent you can influence the compactness of the clusters and their relative distances.\n"
			+ "\n"
			+ "\n"
			+ "MENU:\n"
			+ "   File:\n"
			+ "           -Load run: Load a CLANS savefile (or a BioLayout input file).\n"
			+ "           -Save run: Save current clustering to file.\n"
			+ "           -Add sequences: Do that from the command line via -olddata & -newseqs (see above).\n"
			+ "           -Save matrix values: Save only the matrix containing attraction values for the sequences.\n"
			+ "           -Save 2d graph data: Save the current position of all sequences (the 2d representation) & the sequence names to a file.\n"
			+ "           -Print: print the current view (scaled to fit on one page)\n"
			+ "           -Load matrix data: Load data from a file containing precomputed attracion values.\n"
			+ "\n"
			+ "   Misc:\n"
			+ "           -Extract selected sequences: write the full length sequence & name for the currently selected sequences (red dots)\n"
			+ "                to a separate file.\n"
			+ "           -Hide singletons: remove all sequences with no BLAST hits to any other sequence from the graph.\n"
			+ "           -Use selected subset: Remove all but the selected sequences from the graph.\n"
			+ "           -Use parent group: Undo the last (or multiple) \"use selected subset\" or \"hide singletons\" steps.\n"
			+ "           -Set rotation values:Set the values for the current rotation matrix (9 values).\n"
			+ "           -Complex attraction: use only the best P-value to calculate attractive forces between two sequences (simple)\n"
			+ "               or multiply the probabilities for all HSP's connecting these two sequences (complex).\n"
			+ "           -Optimize only selected sequences: cluster only the selected sequences; leave the rest static.\n"
			+ "               (attraction & repulsion is still computed using ALL sequences).\n"
			+ "           -Cluster in 2D: cluster in 2 dimentions only\n"
			+ "           -Rescale attraction values: set the worst P-value as \"no attraction\"(0), the best as \"best attraction\"(1)\n"
			+ "               and rescale all other values in between (can be useful after having changed the P-value cutoff)\n"
			+ "           -approximation of matrix: plots the pairwise 3d-distance of points over the \"attraction\" values in the matrix.\n"
			+ "               A straight line from top-left to bottom-right would indicate a perfect representation of the matrix values in 3d.\n"
			+ "               However, what is expected, is for all homologous sequences to pull together closer \n"
			+ "               (due to the high number of shared connections) and the graph to distort to sth. resembling a 1/x curve\n"
			+ "               with low 3d-distances for a wide range of good pP-values (attraction values) and a wide range of 3d-distances \n"
			+ "               for bad pP-values.\n"
			+ "\n"
			+ "   Draw:\n"
			+ "           -Change Font: Change the font used in the graph display\n"
			+ "           -Set sequence dot size: sets the size of the dots representing a sequence in 3D-space.\n"
			+ "           -Set selected circle size: sets the size of the circle highlighting selected sequences and sequence groups.\n"
			+ "           -Change color (BLAST hit connections): change the coloring of the lines connecting vertices.\n"
			+ "           -Color dots by sequence length: Colors the sequences according to their length\n"
			+ "               (yellow=shortest, blue=longest, gradient=in-between)\n"
			+ "           -Color edges by \"frustration\": Colors the edges according to wether they are stretched(red) or squashed(blue)\n"
			+ "               compared to what the pairwise attraction value would predict. Stretching is caused by the repulsive effect all\n"
			+ "               vertices have on all others, squashing is caused by \"indirect\" attractive forces (A attracts B, B attracts C,\n"
			+ "               causes A attracts C edge to be squashed a bit.)\n"
			+ "           -Change color (Foreground): change the foreground color (default: black);\n"
			+ "           -Change color (Background): change the background color (default: white);\n"
			+ "           -Change color (Selected sequences): change the color of the ovals highlighting selected sequences.\n"
			+ "           -Change color(BLAST hit numbers): change the color of the sequence numbers for those with BLASt hits\n"
			+ "               (only if windows-> show BLAST hits for sequence: was used)\n"
			+ "           -Change color(BLAST hit circles): change the color of the circles highlighting sequences with BLAST hits\n"
			+ "               (only if widows-> show BLAST hits for sequence: was used)\n"
			+ "           -Show origin:Draw a red \"X\" at the origin (0,0,0)\n"
			+ "           -Show info:Show information about the current clustering \n"
			+ "               (bad->good blast HSP coloring, BLAST command line used, maximum x&y coordinates, current rotation matrix)\n"
			+ "           -Show names while selecting:While selecting, pop up a window showing the names of the currently\n"
			+ "               selected set of sequences.\n"
			+ "           -Show HSP sequence numbers:Show the sequence numbers for those sequences that were found via the current blast search\n"
			+ "               (also see Windows,show blast hits for sequence)\n"
			+ "           -Zoom: set a zoom factor for the view (default: 100%; fits all vetices on to he screen)\n"
			+ "           -Center graph: set the current view on the center of the graph.\n"
			+ "           -antialiasing: enable antialiasing (nicer graphics, but slower)\n"
			+ "\n"
			+ "   Windows:\n"
			+ "           -Sequences: Opens a window showing the sequence numbers and names (more, see below)\n"
			+ "           -P-value plot: Opens a window showing the distribution of P-values (or attraction values) for this dataset.\n"
			+ "           -Show BLAST hits for sequence: Select a sequence and re-BLAST against the current dataset.\n"
			+ "               Opens a window showing the distribution of HSP's throughout the selected sequence. Clicking with the mouse\n"
			+ "               inside this new window highlights the sequences that had HSP's in the 3d-graph\n"
			+ "               (green circles)(also see show HSP sequence numbers).\n"
			+ "           -NJ-phylogeny: opens a window that lets you see the all against all attraction value matrix,\n"
			+ "               a matrix showing the distance separating each sequence from each other and the possibility to calculate \n"
			+ "               NJ phylogenies from either of the two matrices (just a feature, don't trust these phylogenies!).\n"
			+ "           -Find Clusters: let the program determine what clusters exist in the dataset. "
			+ "               Clustering is either done by \"N-linkage clustering\" (user defines the number of connections necessary),\n"
			+ "               by \"convex clustering\" (roughly: all sequences with average sequence<->cluster attractive forces \n"
			+ "               better than X*stdev of the average attraction for the dataset are grouped (User defines \"X\")) \n"
			+ "               or a \"network approach\" (roughly: each sequence forms a node of the input layer for a network.\n"
			+ "               These nodes emit the number of the cluster the sequence belongs to \n"
			+ "               (at the beginning: number of clusters=number of sequences.)(note, the \"weight\" of each value is proportional to \n"
			+ "               the -log(P-value_ of the blast hit). The second layer integrates all these inputs and emits the cluster number \n"
			+ "               with the highest sum of entries for each sequence. This value is then fed back as the new \"cluster assignment\"\n"
			+ "               for the sequence to the input layer. The above steps are repeated until no cluster assignment changes \n"
			+ "               (generally 5 ot 6 rounds)). \n"
			+ "           -A jacknife test is possible. Two parameters are necessary: 1. the number of replicates to perform, \n"
			+ "               2. the amount of data to disregard in each replicate. Confidence values are calculated for \n"
			+ "               a)each cluster (how often does each cluster appear EXACTLY the way it is in the replicates \n"
			+ "               (not less sequences and not more))\n"
			+ "               b)for each sequence (how often is each sequence assigned to the same cluster).\n"
			+ "               Cluster confidences appear in the cluster-window, sequence confidences are displayed in the graph \n"
			+ "               (black=low-confidence, red=high confidence)."
			+ "           -Get sequences with hits to/from selected: highlight the sequences with BLAST/PSIBLAST hits to the current selection\n"
			+ "               (opens a dialog with two lists containing the sequence names for 1. the selected sequences and 2. the sequences \n"
			+ "               with blast hits to or from the selected set).\n"
			+ "           -Edit Sequence Groups: assign a color scheme to the currently selected sequences (and remember these colors and sequences).\n"
			+ "               used to highlight different groups or clusters in a graph.\n"
			+ "\n"
			+ "\n"
			+ "-----------------------------\n"
			+ "SEQUENCES WINDOW:\n"
			+ "\n"
			+ "Mouse/keyboard: select/deselect sequences from the list.\n"
			+ "\n"
			+ "BUTTONS:\n"
			+ "   OK: Re-draw 3d-graph according to the current selection.\n"
			+ "   CLEAR: De-select all sequences.\n"
			+ "   SEARCH: Select all sequences that contain (prompts for text).\n"
			+ "   SHOW SELECTED NAMES: if sequences are/were selected via the mouse in the 3d-graph, this removes the names of all \n"
			+ "       unselected sequences from this window.\n" + "   CLOSE: close the window.\n" + "\n"
			+ "----------------------------------------------------------\n"
			+ "----------------------------------------------------------\n";
}
