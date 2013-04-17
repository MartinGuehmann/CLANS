package clans;

import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.JColorChooser;

/**
 * 
 * @author tancred
 */
public class WindowEditGroups extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -408308359066941525L;

	/**
	 * 
	 * @param parent
	 */
	public WindowEditGroups(ClusteringWithGui parent) {
		this.parent = parent;

		myfont = parent.draw1.myfont;
		mybackground = parent.draw1.bgcolor;
		myforeground = parent.draw1.fgcolor;

		initComponents();

		showinparent.setSelected(parent.data.showseqgroups);

		largerbutton.setText("^ (" + (parent.data.groupsize + 1) + ")");
		smallerbutton.setText("v (" + (parent.data.groupsize - 1) + ")");

		typepanel.add(draw1);
		repaint();
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
	 * content of this method is always regenerated by the Form Editor.
	 */
	private void initComponents() {// GEN-BEGIN:initComponents

		buttonpanel = new javax.swing.JPanel();
		showinparent = new javax.swing.JCheckBox();
		colornamescheckbox = new javax.swing.JCheckBox();
		jPanel3 = new javax.swing.JPanel();
		sizelabel = new javax.swing.JLabel();
		sizetextfield = new javax.swing.JTextField();
		hidebutton = new javax.swing.JButton();
		namebutton = new javax.swing.JButton();
		colorbutton = new javax.swing.JButton();
		addbutton = new javax.swing.JButton();
		setasselectedbutton = new javax.swing.JButton();
		upbutton = new javax.swing.JButton();
		downbutton = new javax.swing.JButton();
		okbutton = new javax.swing.JButton();
		delbutton = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel();
		listpanel = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		groupslist = new javax.swing.JList(parent.data.seqgroupsvec);
		groupslist.setCellRenderer(new MyCellRenderer());
		jPanel2 = new javax.swing.JPanel();
		typepanel = new javax.swing.JPanel();
		smallerbutton = new javax.swing.JButton();
		largerbutton = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		sortnamemenuitem = new javax.swing.JMenuItem();
		sortselnamemenuitem = new javax.swing.JMenuItem();
		sortcolormenuitem = new javax.swing.JMenuItem();
		sortselcolormenuitem = new javax.swing.JMenuItem();
		groupcohesionmenuitem = new javax.swing.JMenuItem();
		randcolormenuitem = new javax.swing.JMenuItem();
		extracttofilemenuitem = new javax.swing.JMenuItem();

		setTitle("Edit Groups");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

		buttonpanel.setLayout(new java.awt.GridLayout(0, 2));

		showinparent.setText("Draw groups");
		showinparent.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showinparentActionPerformed(evt);
			}
		});
		buttonpanel.add(showinparent);

		colornamescheckbox.setSelected(true);
		colornamescheckbox.setText("Color group names");
		colornamescheckbox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				colornamescheckboxActionPerformed(evt);
			}
		});
		buttonpanel.add(colornamescheckbox);

		jPanel3.setLayout(new java.awt.GridLayout(1, 0));

		sizelabel.setText("Default size:");
		jPanel3.add(sizelabel);

		sizetextfield.setText("5");
		sizetextfield.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sizetextfieldActionPerformed(evt);
			}
		});
		jPanel3.add(sizetextfield);

		buttonpanel.add(jPanel3);

		hidebutton.setText("Hide/Show");
		hidebutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				hidebuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(hidebutton);

		namebutton.setText("Change name");
		namebutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				namebuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(namebutton);

		colorbutton.setText("Change color");
		colorbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				colorbuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(colorbutton);

		addbutton.setText("Add selected");
		addbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				addbuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(addbutton);

		setasselectedbutton.setText("Set as selected");
		setasselectedbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				setasselectedbuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(setasselectedbutton);

		upbutton.setText("Move up");
		upbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				upbuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(upbutton);

		downbutton.setText("Move down");
		downbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				downbuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(downbutton);

		okbutton.setText("Update");
		okbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okbuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(okbutton);

		delbutton.setText("Delete");
		delbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				delbuttonActionPerformed(evt);
			}
		});
		buttonpanel.add(delbutton);

		getContentPane().add(buttonpanel, java.awt.BorderLayout.SOUTH);

		jPanel1.setLayout(new java.awt.BorderLayout());

		listpanel.setPreferredSize(new java.awt.Dimension(100, 100));
		listpanel.setLayout(new java.awt.BorderLayout());

		jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 0));

		groupslist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
				groupslistValueChanged(evt);
			}
		});
		jScrollPane1.setViewportView(groupslist);

		listpanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jPanel1.add(listpanel, java.awt.BorderLayout.CENTER);

		jPanel2.setLayout(new java.awt.BorderLayout());

		typepanel.setPreferredSize(new java.awt.Dimension(50, 50));
		typepanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				typepanelMouseClicked(evt);
			}
		});
		typepanel.setLayout(new javax.swing.BoxLayout(typepanel, javax.swing.BoxLayout.LINE_AXIS));
		jPanel2.add(typepanel, java.awt.BorderLayout.CENTER);

		smallerbutton.setText("v");
		smallerbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				smallerbuttonActionPerformed(evt);
			}
		});
		jPanel2.add(smallerbutton, java.awt.BorderLayout.SOUTH);

		largerbutton.setText("^");
		largerbutton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				largerbuttonActionPerformed(evt);
			}
		});
		jPanel2.add(largerbutton, java.awt.BorderLayout.NORTH);

		jPanel1.add(jPanel2, java.awt.BorderLayout.EAST);

		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

		jMenu1.setText("Menu");

		sortnamemenuitem.setText("Sort all by name");
		sortnamemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sortnamemenuitemActionPerformed(evt);
			}
		});
		jMenu1.add(sortnamemenuitem);

		sortselnamemenuitem.setText("Sort selected by name");
		sortselnamemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sortselnamemenuitemActionPerformed(evt);
			}
		});
		jMenu1.add(sortselnamemenuitem);

		sortcolormenuitem.setText("Sort all by color");
		sortcolormenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sortcolormenuitemActionPerformed(evt);
			}
		});
		jMenu1.add(sortcolormenuitem);

		sortselcolormenuitem.setText("Sort selected by color");
		sortselcolormenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sortselcolormenuitemActionPerformed(evt);
			}
		});
		jMenu1.add(sortselcolormenuitem);

		groupcohesionmenuitem.setText("Calculate group cohesion");
		groupcohesionmenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				groupcohesionmenuitemActionPerformed(evt);
			}
		});
		jMenu1.add(groupcohesionmenuitem);

		randcolormenuitem.setText("Assign random colors");
		randcolormenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				randcolormenuitemActionPerformed(evt);
			}
		});
		jMenu1.add(randcolormenuitem);

		extracttofilemenuitem.setText("Extract group sequences to file");
		extracttofilemenuitem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				extracttofilemenuitemActionPerformed(evt);
			}
		});
		jMenu1.add(extracttofilemenuitem);

		jMenuBar1.add(jMenu1);

		setJMenuBar(jMenuBar1);

		pack();
	}// GEN-END:initComponents

	private void hidebuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_hidebuttonActionPerformed
		int reference = groupslist.getSelectedIndex();
		if (reference == -1) {
			return;// none selected
		}
		if ((reference > -1) && (reference <= parent.data.seqgroupsvec.size())) {
			boolean refbool = parent.data.seqgroupsvec.elementAt(reference).hide;
			if (refbool == true) {
				refbool = false;
			} else {
				refbool = true;
			}
			int[] currsel = groupslist.getSelectedIndices();
			if (java.lang.reflect.Array.getLength(currsel) > 0) {
				int i;
				for (i = java.lang.reflect.Array.getLength(currsel); --i >= 0;) {
					if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
						parent.data.seqgroupsvec.elementAt(currsel[i]).hide = refbool;
					}
				}// end for i
			}
		}
		repaint();
	}// GEN-LAST:event_hidebuttonActionPerformed

	private void sortselcolormenuitemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sortselcolormenuitemActionPerformed
		// sort the selected sequence groups by their color
		int[] currsel = groupslist.getSelectedIndices();
		if (java.lang.reflect.Array.getLength(currsel) > 0) {
			seqgroup[] tmparr = new seqgroup[java.lang.reflect.Array.getLength(currsel)];
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
					tmparr[i] = parent.data.seqgroupsvec.elementAt(currsel[i]);
				}
			}// end for i
				// now sort tmparr
			java.util.Arrays.sort(tmparr, new seqgroupscolorcomparator());
			// and now re-add them to the seqgroupsvec
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
					parent.data.seqgroupsvec.set(currsel[i], tmparr[i]);
				}
			}// end for i
			repaint();
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "You need to select at least one group");
		}
	}// GEN-LAST:event_sortselcolormenuitemActionPerformed

	private void sortselnamemenuitemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sortselnamemenuitemActionPerformed
		// sort the selected sequence groups by their name
		int[] currsel = groupslist.getSelectedIndices();
		if (java.lang.reflect.Array.getLength(currsel) > 0) {
			seqgroup[] tmparr = new seqgroup[java.lang.reflect.Array.getLength(currsel)];
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
					tmparr[i] = parent.data.seqgroupsvec.elementAt(currsel[i]);
				}
			}// end for i
				// now sort tmparr
			java.util.Arrays.sort(tmparr, new seqgroupsnamecomparator());
			// and now re-add them to the seqgroupsvec
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
					parent.data.seqgroupsvec.set(currsel[i], tmparr[i]);
				}
			}// end for i
			repaint();
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "You need to select at least one group");
		}
	}// GEN-LAST:event_sortselnamemenuitemActionPerformed

	private void sortcolormenuitemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sortcolormenuitemActionPerformed
		// sort the groups by their color
		int num = parent.data.seqgroupsvec.size();
		seqgroup[] tmparr = new seqgroup[num];
		for (int i = 0; i < num; i++) {
			tmparr[i] = parent.data.seqgroupsvec.elementAt(i);
		}// end for i
		java.util.Arrays.sort(tmparr, new seqgroupscolorcomparator());
		for (int i = 0; i < num; i++) {
			parent.data.seqgroupsvec.setElementAt(tmparr[i], i);
		}// end for i
		groupslist.setListData(parent.data.seqgroupsvec);
		repaint();
	}// GEN-LAST:event_sortcolormenuitemActionPerformed

	private void sortnamemenuitemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sortnamemenuitemActionPerformed
		// sort the sequence groups by name
		int num = parent.data.seqgroupsvec.size();
		seqgroup[] tmparr = new seqgroup[num];
		for (int i = 0; i < num; i++) {
			tmparr[i] = parent.data.seqgroupsvec.elementAt(i);
		}// end for i
		java.util.Arrays.sort(tmparr, new seqgroupsnamecomparator());
		for (int i = 0; i < num; i++) {
			parent.data.seqgroupsvec.setElementAt(tmparr[i], i);
		}// end for i
		groupslist.setListData(parent.data.seqgroupsvec);
		repaint();
	}// GEN-LAST:event_sortnamemenuitemActionPerformed

	private void randcolormenuitemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_randcolormenuitemActionPerformed
		// I want to assign a new random color to all selected groups
		int[] currsel = groupslist.getSelectedIndices();
		if (java.lang.reflect.Array.getLength(currsel) > 0) {
			seqgroup mygroup;
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
					mygroup = parent.data.seqgroupsvec.elementAt(currsel[i]);
					mygroup.color = new java.awt.Color(ClusterMethods.rand.nextInt(16777216));
				}
			}
			repaint();
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "You need to select at least one group");
		}
	}// GEN-LAST:event_randcolormenuitemActionPerformed

	private void groupcohesionmenuitemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_groupcohesionmenuitemActionPerformed
		// calculate the inner cluster average and variance and the same value to the
		// sequences outside the cluster.
		int currsel = groupslist.getSelectedIndex();
		if ((currsel > -1) && (currsel <= parent.data.seqgroupsvec.size())) {
			// get the avg and variance within the cluster
			int[] sequences = parent.data.seqgroupsvec.elementAt(currsel).sequences;
			float avgval = getavgatt(sequences, parent.data.myattvals);
			float varval = getvaratt(sequences, parent.data.myattvals, avgval);
			float outavg = getoutavgatt(sequences, parent.data.myattvals);
			float outvar = getoutvaratt(sequences, parent.data.myattvals, outavg);
			parent.data.seqgroupsvec.elementAt(currsel).confvals = "avg:" + avgval + " var:" + varval + " outavg:"
					+ outavg + " outvar:" + outvar;
		} else {
			System.err.println("no group selected or group outside of range");
		}
		repaint();
	}// GEN-LAST:event_groupcohesionmenuitemActionPerformed

	private void extracttofilemenuitemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_extracttofilemenuitemActionPerformed
		// I want to write all of the selected groups to separate files (named after the clusters)
		// first get the directory to write to
		int[] currsel = groupslist.getSelectedIndices();
		if (java.lang.reflect.Array.getLength(currsel) > 0) {
			ClusteringWithGui.fc.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
			int returnVal = ClusteringWithGui.fc.showOpenDialog(this);
			if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
				java.io.File currdir = (ClusteringWithGui.fc.getSelectedFile());
				seqgroup mygroup;
				for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
					if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
						mygroup = parent.data.seqgroupsvec.elementAt(currsel[i]);
						int[] sequences = mygroup.sequences;
						String outfile = currdir.getAbsolutePath() + java.io.File.separatorChar + mygroup.name;
						try {
							java.io.PrintWriter outwrite = new java.io.PrintWriter(new java.io.BufferedWriter(
									new java.io.FileWriter(outfile)));
							// now write each of the sequences to the file
							for (int j = java.lang.reflect.Array.getLength(sequences); --j >= 0;) {
								outwrite.println(">" + parent.data.sequence_names[sequences[j]] + " " + sequences[j]);
								outwrite.println(parent.data.sequences[sequences[j]].seq);
							}// end for j
							outwrite.close();
						} catch (java.io.IOException ioe) {
							System.err.println("IOERROR writing to " + outfile);
						}
					}
				}
			}
			ClusteringWithGui.fc.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		} else {
			javax.swing.JOptionPane.showMessageDialog(this, "You need to select at least one group");
		}
	}// GEN-LAST:event_extracttofilemenuitemActionPerformed

	private void sizetextfieldActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_sizetextfieldActionPerformed
		// change the size setting
		try {
			parent.data.groupsize = Integer.parseInt(sizetextfield.getText());
		} catch (NumberFormatException ne) {
			javax.swing.JOptionPane.showMessageDialog(this, "ERROR parsing int from '" + sizetextfield.getText() + "'");
		}
	}// GEN-LAST:event_sizetextfieldActionPerformed

	private void largerbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_largerbuttonActionPerformed
		// increase the polygon size for all
		int reference = groupslist.getSelectedIndex();
		if (reference == -1) {
			return;// none selected
		}
		if ((reference > -1) && (reference <= parent.data.seqgroupsvec.size())) {
			int refsize = parent.data.seqgroupsvec.elementAt(reference).size + 1;
			int[] currsel = groupslist.getSelectedIndices();
			if (java.lang.reflect.Array.getLength(currsel) > 0) {
				int i;
				for (i = java.lang.reflect.Array.getLength(currsel); --i >= 0;) {
					if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
						parent.data.seqgroupsvec.elementAt(currsel[i]).size = refsize;
						parent.data.seqgroupsvec.elementAt(currsel[i]).polygon = makepolygons.get(
								parent.data.seqgroupsvec.elementAt(currsel[i]).type,
								parent.data.seqgroupsvec.elementAt(currsel[i]).size);
					}
				}// end for i
				largerbutton.setText("^ (" + (parent.data.seqgroupsvec.elementAt(reference).size + 1) + ")");
				smallerbutton.setText("v (" + (parent.data.seqgroupsvec.elementAt(reference).size - 1) + ")");
			}
		}
		repaint();
	}// GEN-LAST:event_largerbuttonActionPerformed

	private void smallerbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_smallerbuttonActionPerformed
		// decrease the polygon size for all
		int reference = groupslist.getSelectedIndex();
		if (reference == -1) {
			return;// none selected
		}
		if ((reference > -1) && (reference <= parent.data.seqgroupsvec.size())) {
			int refsize = parent.data.seqgroupsvec.elementAt(reference).size - 1;
			int[] currsel = groupslist.getSelectedIndices();
			if (java.lang.reflect.Array.getLength(currsel) > 0) {
				int i;
				for (i = java.lang.reflect.Array.getLength(currsel); --i >= 0;) {
					if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
						parent.data.seqgroupsvec.elementAt(currsel[i]).size = refsize;
						parent.data.seqgroupsvec.elementAt(currsel[i]).polygon = makepolygons.get(
								parent.data.seqgroupsvec.elementAt(currsel[i]).type,
								parent.data.seqgroupsvec.elementAt(currsel[i]).size);
					}
				}// end for i
				largerbutton.setText("^ (" + (parent.data.seqgroupsvec.elementAt(reference).size + 1) + ")");
				smallerbutton.setText("v (" + (parent.data.seqgroupsvec.elementAt(reference).size - 1) + ")");
			}
		}
		repaint();
	}// GEN-LAST:event_smallerbuttonActionPerformed

	private void typepanelMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_typepanelMouseClicked
		// increase the type counter by one
		if (evt.getButton() == MouseEvent.BUTTON1) {
			// increase counter
			draw1.type++;
			if (draw1.type >= draw1.polygons.size()) {
				draw1.type = 0;
			}
		} else {
			// decrease counter
			draw1.type--;
			if (draw1.type < 0) {
				draw1.type = draw1.polygons.size() - 1;
			}
		}

		int[] currsel = groupslist.getSelectedIndices();
		if (java.lang.reflect.Array.getLength(currsel) > 0) {
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
					parent.data.seqgroupsvec.elementAt(currsel[i]).type = draw1.type;
					parent.data.seqgroupsvec.elementAt(currsel[i]).polygon = makepolygons.get(draw1.type,
							parent.data.seqgroupsvec.elementAt(currsel[i]).size);
				}
			}// end for i
		}
		repaint();
	}// GEN-LAST:event_typepanelMouseClicked

	private void colornamescheckboxActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_colornamescheckboxActionPerformed
		repaint();
	}// GEN-LAST:event_colornamescheckboxActionPerformed

	private void downbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_downbuttonActionPerformed
		// move the currently selected object down by one in the vector
		int currsel = groupslist.getSelectedIndex();
		if ((currsel > -1) && (currsel < parent.data.seqgroupsvec.size() - 1)) {
			parent.data.seqgroupsvec.insertElementAt(parent.data.seqgroupsvec.remove(currsel), currsel + 1);
			groupslist.setSelectedIndex(currsel + 1);
		}
		repaint();
	}// GEN-LAST:event_downbuttonActionPerformed

	private void upbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_upbuttonActionPerformed
		// move the currently selected object up by one in the vector
		int currsel = groupslist.getSelectedIndex();
		if ((currsel > 0) && (currsel <= parent.data.seqgroupsvec.size())) {
			parent.data.seqgroupsvec.insertElementAt(parent.data.seqgroupsvec.remove(currsel), currsel - 1);
			groupslist.setSelectedIndex(currsel - 1);
		}
		repaint();
	}// GEN-LAST:event_upbuttonActionPerformed

	private void setasselectedbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_setasselectedbuttonActionPerformed
		// replace the current sequence selection with this group
		int[] currsel = groupslist.getSelectedIndices();
		if (java.lang.reflect.Array.getLength(currsel) > 0) {
			int counter = 0;
			seqgroup mygroup;
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
					counter += parent.data.seqgroupsvec.elementAt(currsel[i]).sequences.length;
				}
			}
			int[] selecteds = new int[counter];
			counter = 0;
			for (int i = java.lang.reflect.Array.getLength(currsel) - 1; i >= 0; i--) {
				mygroup = parent.data.seqgroupsvec.elementAt(currsel[i]);
				for (int j = java.lang.reflect.Array.getLength(mygroup.sequences) - 1; j >= 0; j--) {
					selecteds[counter] = mygroup.sequences[j];
					counter++;
				}// end for j
			}// end for i
			parent.data.selectednames = selecteds;
			parent.button_select_all_or_clear.setText("Clear Selection");
			if (java.lang.reflect.Array.getLength(currsel) == 1) {
				if (parent.data.seqgroupsvec.elementAt(currsel[0]).seqconf != null) {
					parent.clusterconf = parent.data.seqgroupsvec.elementAt(currsel[0]).seqconf;
				} else {
					parent.clusterconf = null;
				}
			} else {
				parent.clusterconf = null;
			}
		} else {
			parent.button_select_all_or_clear.setText("Select All");
		}
	}// GEN-LAST:event_setasselectedbuttonActionPerformed

	private void showinparentActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_showinparentActionPerformed
		// show or don't show the groups
		if (showinparent.isSelected()) {
			parent.data.showseqgroups = true;
		} else {
			parent.data.showseqgroups = false;
		}
		parent.repaint();
	}// GEN-LAST:event_showinparentActionPerformed

	private void groupslistValueChanged(javax.swing.event.ListSelectionEvent evt) {// GEN-FIRST:event_groupslistValueChanged
		// Update screen
		int currsel = groupslist.getSelectedIndex();
		if (currsel > -1) {
			colorbutton.setBackground(parent.data.seqgroupsvec.elementAt(currsel).color);
			draw1.type = parent.data.seqgroupsvec.elementAt(currsel).type;
			largerbutton.setText("^ (" + (parent.data.seqgroupsvec.elementAt(currsel).size + 1) + ")");
			smallerbutton.setText("v (" + (parent.data.seqgroupsvec.elementAt(currsel).size - 1) + ")");
			this.repaint();
			// and now highlight the currently selected groups in the map
			int[] currselarr = groupslist.getSelectedIndices();
			int counter = 0;
			for (int i = java.lang.reflect.Array.getLength(currselarr) - 1; i >= 0; i--) {
				if ((currselarr[i] > -1) && (currselarr[i] <= parent.data.seqgroupsvec.size())) {
					counter += parent.data.seqgroupsvec.elementAt(currselarr[i]).sequences.length;
				}
			}
			int[] selecteds = new int[counter];
			counter = 0;
			seqgroup mygroup;
			for (int i = java.lang.reflect.Array.getLength(currselarr) - 1; i >= 0; i--) {
				mygroup = parent.data.seqgroupsvec.elementAt(currselarr[i]);
				for (int j = java.lang.reflect.Array.getLength(mygroup.sequences) - 1; j >= 0; j--) {
					selecteds[counter] = mygroup.sequences[j];
					counter++;
				}// end for j
			}// end for i
			parent.groupseqs = selecteds;
			parent.groupseqscolor = parent.data.seqgroupsvec.elementAt(currsel).color;
			parent.repaint();
		}
	}// GEN-LAST:event_groupslistValueChanged

	private void delbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_delbuttonActionPerformed
		// remove currently selected sequence group
		int[] currsel = groupslist.getSelectedIndices();
		for (int i = java.lang.reflect.Array.getLength(currsel); --i >= 0;) {
			if ((currsel[i] > -1) && (currsel[i] <= parent.data.seqgroupsvec.size())) {
				parent.data.seqgroupsvec.removeElementAt(currsel[i]);
			}
		}
		groupslist.setListData(parent.data.seqgroupsvec);
		repaint();
	}// GEN-LAST:event_delbuttonActionPerformed

	private void addbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_addbuttonActionPerformed
		// add the current sequence selection as a new sequence group
		if (java.lang.reflect.Array.getLength(parent.data.selectednames) == 0) {
			// don't add a new group
			javax.swing.JOptionPane.showMessageDialog(this, "No sequences selected", "Error",
					javax.swing.JOptionPane.ERROR_MESSAGE);
			return;
		}
		seqgroup newgroup = new seqgroup();
		String newname = "selected sequences";
		newname = javax.swing.JOptionPane.showInputDialog("Group name:", newname);
		if (newname == null || newname.length() < 1) {
			// don't add the unnamed group
			javax.swing.JOptionPane.showMessageDialog(this, "Warning:Unnamed group; skipping");
			return;
		}
		newgroup.name = newname;
		newgroup.color = java.awt.Color.red;
		newgroup.sequences = parent.data.selectednames;
		newgroup.type = 0;
		newgroup.size = parent.data.groupsize;
		parent.data.seqgroupsvec.addElement(newgroup);
		// removed as this gives me problem with the "show selected sequences" window
		// parent.selectednames=new int[0];
		// parent.clearselectbutton.setText("Select All");
		groupslist.setListData(parent.data.seqgroupsvec);
		repaint();
	}// GEN-LAST:event_addbuttonActionPerformed

	/**
	 * Opens a color chooser to let the user pick a new color for all currently selected groups.
	 * @param evt
	 */
	private void colorbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_colorbuttonActionPerformed
	    int[] selected_groups = groupslist.getSelectedIndices();
		if (selected_groups.length > 0) {
			if ((selected_groups[0] > -1) && (selected_groups[0] <= parent.data.seqgroupsvec.size())) {
				
			    java.awt.Color current_color = parent.data.seqgroupsvec.elementAt(selected_groups[0]).color;
				java.awt.Color new_color = null;
				
				try {
					new_color = JColorChooser.showDialog(this, "Select New Color", current_color);
				} catch (java.awt.HeadlessException e) {
					System.err.println("HeadlessException!");
				}
				
				if (new_color != null) {
				    colorbutton.setBackground(new_color);
	                for (int i = selected_groups.length - 1; i >= 0; i--) {
	                    parent.data.seqgroupsvec.elementAt(selected_groups[i]).color = new_color;
	                }
				}
			}
			
			this.repaint();
			parent.groupseqscolor = parent.data.seqgroupsvec.elementAt(selected_groups[0]).color;
			parent.repaint();
		}
	}// GEN-LAST:event_colorbuttonActionPerformed

	private void namebuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_namebuttonActionPerformed
		// edit the name for the currently selected sequence group
		int currsel = groupslist.getSelectedIndex();
		if ((currsel > -1) && (currsel <= parent.data.seqgroupsvec.size())) {
			String newname = parent.data.seqgroupsvec.elementAt(currsel).name;
			newname = javax.swing.JOptionPane.showInputDialog(this, "Group name:", newname);
			parent.data.seqgroupsvec.elementAt(currsel).name = newname;
			repaint();
		}
	}// GEN-LAST:event_namebuttonActionPerformed

	private void okbuttonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okbuttonActionPerformed
		// update the view for parent
		parent.draw1.repaint();
	}// GEN-LAST:event_okbuttonActionPerformed

	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_closeDialog
		parent.groupseqs = null;
		setVisible(false);
		dispose();
	}// GEN-LAST:event_closeDialog

	/**
	 * @param args
	 *            the command line arguments
	 */
	// public static void main(String args[]) {
	// new seqgroupwindow(new javax.swing.JFrame(), true).show();
	// }

	javax.swing.JColorChooser colorchooser = new javax.swing.JColorChooser();
	String[] groupnames;
	ClusteringWithGui parent;
	java.awt.Font myfont;
	java.awt.Color mybackground;
	java.awt.Color myforeground;
	graphpanel draw1 = new graphpanel();
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton addbutton;
	private javax.swing.JPanel buttonpanel;
	private javax.swing.JButton colorbutton;
	private javax.swing.JCheckBox colornamescheckbox;
	private javax.swing.JButton delbutton;
	private javax.swing.JButton downbutton;
	private javax.swing.JMenuItem extracttofilemenuitem;
	private javax.swing.JMenuItem groupcohesionmenuitem;
	private javax.swing.JList groupslist;
	private javax.swing.JButton hidebutton;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JButton largerbutton;
	private javax.swing.JPanel listpanel;
	private javax.swing.JButton namebutton;
	private javax.swing.JButton okbutton;
	private javax.swing.JMenuItem randcolormenuitem;
	private javax.swing.JButton setasselectedbutton;
	private javax.swing.JCheckBox showinparent;
	private javax.swing.JLabel sizelabel;
	private javax.swing.JTextField sizetextfield;
	private javax.swing.JButton smallerbutton;
	private javax.swing.JMenuItem sortcolormenuitem;
	private javax.swing.JMenuItem sortnamemenuitem;
	private javax.swing.JMenuItem sortselcolormenuitem;
	private javax.swing.JMenuItem sortselnamemenuitem;
	private javax.swing.JPanel typepanel;
	private javax.swing.JButton upbutton;

	// End of variables declaration//GEN-END:variables

	// --------------------------------------------------------------------------

	float getavgatt(int[] seqs, minattvals[] attvals) {
		// get the average attractive force of sequences in this group
		float sumval = 0;
		int seqnum = java.lang.reflect.Array.getLength(seqs);
		int attnum = java.lang.reflect.Array.getLength(attvals);
		int q, h, j;
		for (int i = 0; i < attnum; i++) {
			q = attvals[i].query;
			h = attvals[i].hit;
			for (j = 0; j < seqnum; j++) {
				if (seqs[j] == q) {
					q = -1;
				}
				if (seqs[j] == h) {
					h = -1;
				}
			}// end for j
			if ((q == -1) && (h == -1)) {
				sumval += attvals[i].att;
			}
		}// end for i
		return sumval / attnum;
	}// end getavgatt

	// --------------------------------------------------------------------------

	float getvaratt(int[] seqs, minattvals[] attvals, float avg) {
		// get the variance of the attvals in this group
		float varval = 0;
		float diff;
		int q, h, j;
		int seqnum = java.lang.reflect.Array.getLength(seqs);
		int attnum = java.lang.reflect.Array.getLength(attvals);
		for (int i = 0; i < attnum; i++) {
			q = attvals[i].query;
			h = attvals[i].hit;
			for (j = 0; j < seqnum; j++) {
				if (seqs[j] == q) {
					q = -1;
				}
				if (seqs[j] == h) {
					h = -1;
				}
			}// end for j
			if ((q == -1) && (h == -1)) {
				diff = attvals[i].att - avg;
				varval += (diff * diff);
			}
		}// end for i
		return varval / attnum;
	}// end getvaratt

	// --------------------------------------------------------------------------

	float getoutavgatt(int[] seqs, minattvals[] attvals) {
		// get the average attractive force of sequences in this group to the non-group seqs
		int elements = java.lang.reflect.Array.getLength(attvals);
		int seqnum = java.lang.reflect.Array.getLength(seqs);
		int attnum = java.lang.reflect.Array.getLength(attvals);
		if (seqnum == elements) {
			return 0;
		}
		int j;
		float sumval = 0;
		int counter = 0;
		int q, h;
		for (int i = 0; i < attnum; i++) {
			q = attvals[i].query;
			h = attvals[i].hit;
			for (j = 0; j < seqnum; j++) {
				if (seqs[j] == q) {
					q = -1;
				}
				if (seqs[j] == h) {
					h = -1;
				}
			}// end for j
			if ((q == -1) || (h == -1) && (q != -1 || h != -1)) {
				// if either query or hit are part of this group but not both
				sumval += attvals[i].att;
				counter++;
			}
		}// end for i
		return sumval / counter;
	}// end getoutavgatt

	// --------------------------------------------------------------------------

	float getoutvaratt(int[] seqs, minattvals[] attvals, float avg) {
		// get the variance of the attvals in this group to the non-group seqs
		int elements = java.lang.reflect.Array.getLength(attvals);
		int seqnum = java.lang.reflect.Array.getLength(seqs);
		int attnum = java.lang.reflect.Array.getLength(attvals);
		if (seqnum == elements) {
			return 0;
		}
		int j;
		float varval = 0;
		float diff;
		int counter = 0;
		int q, h;
		for (int i = 0; i < attnum; i++) {
			q = attvals[i].query;
			h = attvals[i].hit;
			for (j = 0; j < seqnum; j++) {
				if (seqs[j] == q) {
					q = -1;
				}
				if (seqs[j] == h) {
					h = -1;
				}
			}// end for j
			if ((q == -1) || (h == -1) && (q != -1 || h != -1)) {
				// if either query or hit are part of this group but not both
				diff = attvals[i].att - avg;
				varval += (diff * diff);
				counter++;
			}
		}// end for i
		return varval / counter;
	}// end getoutvaratt

	// --------------------------------------------------------------------------

	class seqgroupscolorcomparator implements java.util.Comparator<seqgroup> {

		/**
		 * compares the colors of two seqgroups
		 */
		public int compare(seqgroup o1, seqgroup o2) {
			float h1 = gethue(o1.color);
			float h2 = gethue(o2.color);
			return (h1 > h2 ? 1 : (h1 == h2 ? 0 : -1));
		}

		float gethue(java.awt.Color c) {
			return java.awt.Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null)[0];
		}

	}

	class seqgroupsnamecomparator implements java.util.Comparator<seqgroup> {

		/**
		 * compares the name of two seqgroups
		 */
		public int compare(seqgroup o1, seqgroup o2) {
			String h1 = o1.name;
			String h2 = o2.name;
			return h1.compareTo(h2);
		}

	}

	// set what the JList is supposed to draw
	class MyCellRenderer extends javax.swing.JLabel implements javax.swing.ListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4398839558096028331L;

		public java.awt.Component getListCellRendererComponent(javax.swing.JList list, Object value, int index,
				boolean isSelected, boolean cellHasFocus) {
			if (index == 0) {
				myfont = parent.draw1.myfont;
				mybackground = parent.draw1.bgcolor;
				myforeground = parent.draw1.fgcolor;
			}
			
			seqgroup group = (seqgroup) value;
			
			String s = group.name;

			s += "(" + group.sequences.length + ")";

			if (group.confvals != null) {
				s += " (cohesion: " + group.confvals + ")";
			}
			if (group.hide) {
				s += " (hidden)";
			}
			setText(s);
			if (colornamescheckbox.isSelected()) {
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(group.color);
				} else {
					setBackground(mybackground);
					setForeground(group.color);
				}
			} else {
				if (isSelected) {
					setBackground(list.getSelectionBackground());
					setForeground(myforeground);
				} else {
					setBackground(mybackground);
					setForeground(myforeground);
				}
			}
			setEnabled(list.isEnabled());
			setFont(myfont);
			setOpaque(true);
			return this;
		}
	}// end class cellrenderer

	// --------------------------------------------------------------------------

	class graphpanel extends javax.swing.JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2444671386154267112L;

		public graphpanel() {
		}

		int type = 0;
		int framewidth = 50;
		int frameheight = 50;
		int size = 25;
		ArrayList<int[][]> polygons = makepolygons.get(size);
		int[][] tmpposarr;
		int[] xposarr, yposarr;
		int posnum;
		int xoffset = (int) (framewidth / 2);
		int yoffset = (int) (frameheight / 2);

		public void paintComponent(java.awt.Graphics g) {
			g.setColor(java.awt.Color.white);
			framewidth = typepanel.getWidth();
			frameheight = typepanel.getHeight();
			xoffset = (int) (framewidth / 2);
			yoffset = (int) (frameheight / 2);
			g.fillRect(0, 0, framewidth, frameheight);
			tmpposarr = ((int[][]) polygons.get(type));
			g.setColor(colorbutton.getBackground());
			if (type != 0) {
				posnum = tmpposarr[2][0];
				xposarr = new int[posnum];
				yposarr = new int[posnum];
				for (int i = 0; i < posnum; i++) {
					xposarr[i] = tmpposarr[0][i] + xoffset;
					yposarr[i] = tmpposarr[1][i] + yoffset;
				}// end for i
				g.fillPolygon(xposarr, yposarr, posnum);
			} else {
				g.fillOval((int) (xoffset - size / 2), (int) (yoffset - size / 2), size, size);
			}
		}// end paintComponent

	}// end class graphpanel

}// end class
