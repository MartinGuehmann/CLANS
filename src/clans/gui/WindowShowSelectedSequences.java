package clans.gui;

import java.util.*;

public class WindowShowSelectedSequences extends javax.swing.JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = -4171611623198735444L;

    public WindowShowSelectedSequences(String[] namesarr, ProgramWindow parent) {
        this.parent = parent;
        this.namesarr = numberarr(namesarr);
        initComponents();
        if (parent.data.selectedSequencesIndices.length > 0) {
            showall = false;
            //show only the selected seqs
            //globalselected=parent.selectednames;
            selectednames = new String[parent.data.selectedSequencesIndices.length];
            for (int i = 0; i < selectednames.length; i++) {
                selectednames[i] = this.namesarr[parent.data.selectedSequencesIndices[i]];
            }//end for i
            seqnamelist.setListData(selectednames);
            seqnamelist.setSelectedIndices(new int[0]);
            selectednamesbutton.setText("show all names");
        } else {
            showall = true;
        }
        this.jScrollPane2.getVerticalScrollBar().setUnitIncrement((int) (jPanel1.getHeight() / namesarr.length));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        seqnamelist = new javax.swing.JList(namesarr);
        buttonpanel = new javax.swing.JPanel();
        backbutton = new javax.swing.JButton();
        okbutton = new javax.swing.JButton();
        clearbutton = new javax.swing.JButton();
        searchbutton = new javax.swing.JButton();
        selectednamesbutton = new javax.swing.JButton();
        closebutton = new javax.swing.JButton();
        savebutton = new javax.swing.JButton();

        setTitle("Selected Sequences (3D)");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jScrollPane2.setPreferredSize(new java.awt.Dimension(100, 500));

        jPanel1.setDoubleBuffered(false);
        jPanel1.setLayout(new java.awt.BorderLayout());

        seqnamelist.setFont(new java.awt.Font("Monospaced", 0, 10));
        jPanel1.add(seqnamelist, java.awt.BorderLayout.CENTER);

        jScrollPane2.setViewportView(jPanel1);

        getContentPane().add(jScrollPane2, java.awt.BorderLayout.CENTER);

        buttonpanel.setDoubleBuffered(false);
        buttonpanel.setLayout(new java.awt.GridLayout(1, 0));

        backbutton.setText("Back (0)");
        backbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(backbutton);

        okbutton.setText("OK");
        okbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(okbutton);

        clearbutton.setText("Clear");
        clearbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(clearbutton);

        searchbutton.setText("Find");
        searchbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(searchbutton);

        selectednamesbutton.setText("Show selected names");
        selectednamesbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectednamesbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(selectednamesbutton);

        closebutton.setText("Close");
        closebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closebuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(closebutton);

        savebutton.setText("Save to file");
        savebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                savebuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(savebutton);

        getContentPane().add(buttonpanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void savebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_savebuttonActionPerformed
        // save the current list to a file
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser(".");
        int returnVal = fc.showSaveDialog(this);
        String tmpstr;
        if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            try {
                java.io.PrintWriter outwrite = new java.io.PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(fc.getSelectedFile())));
                for (int i = 0; i < seqnamelist.getModel().getSize(); i++) {
                    tmpstr = (String) seqnamelist.getModel().getElementAt(i);
                    outwrite.println(tmpstr.substring(tmpstr.indexOf(" ")).trim());
                }
                outwrite.close();
            } catch (java.io.IOException ioe) {
                javax.swing.JOptionPane.showMessageDialog(this, "ERROR, unable to write to " + fc.getSelectedFile().getAbsolutePath());
            }
        }
    }//GEN-LAST:event_savebuttonActionPerformed

    private void backbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbuttonActionPerformed
        // assign the former selection as current
        if (selectedvec.size() == 0) {
            return;
        }
        if (showall) {
            globalselected = (int[]) selectedvec.remove(selectedvec.size() - 1);
            parent.data.selectedSequencesIndices = globalselected;
            seqnamelist.setListData(namesarr);
            seqnamelist.setSelectedIndices(globalselected);
        } else {
            globalselected = (int[]) selectedvec.remove(selectedvec.size() - 1);
            parent.data.selectedSequencesIndices = globalselected;
            selectednames = new String[globalselected.length];
            int[] myselected = new int[globalselected.length];
            for (int i = 0; i < selectednames.length; i++) {
                selectednames[i] = namesarr[globalselected[i]];
                myselected[i] = i;
            }//end for i
            seqnamelist.setListData(selectednames);
            seqnamelist.setSelectedIndices(myselected);
        }
        backbutton.setText("Back (" + selectedvec.size() + ")");
        parent.repaint();
    }//GEN-LAST:event_backbuttonActionPerformed

    private void closebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closebuttonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closebuttonActionPerformed

    private void selectednamesbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectednamesbuttonActionPerformed
        // show only the selected names
        if (showall == true) {
            showall = false;
            //show only the selected seqs
            globalselected = seqnamelist.getSelectedIndices();
            selectednames = new String[parent.data.selectedSequencesIndices.length];
            for (int i = 0; i < selectednames.length; i++) {
                selectednames[i] = namesarr[parent.data.selectedSequencesIndices[i]];
            }//end for i
            seqnamelist.setListData(selectednames);
            seqnamelist.setSelectedIndices(new int[0]);
            selectednamesbutton.setText("show all names");
        } else {
            showall = true;
            //show all sequences
            seqnamelist.setListData(namesarr);
            seqnamelist.setSelectedIndices(globalselected);
            selectednamesbutton.setText("show selected names");
        }
    }//GEN-LAST:event_selectednamesbuttonActionPerformed

    /**
     * 
     * @param evt
     */
    private void searchbuttonActionPerformed(java.awt.event.ActionEvent evt) {

        int[] selected_indices;
        
        String query = DialogSearchQueryEntry.get(this);
        
        if (query == null) { // the query entry dialog has been quit via the cancel button
            return;
        }
        
        if (query.length() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "no query was entered", "ERROR",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (showall) {
            selected_indices = getmatches(query, namesarr);

        } else {
            selected_indices = getmatches(query, selectednames);
        }
        
        if (selected_indices.length == 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "No sequences found for '" + query + "'", "ERROR",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        seqnamelist.setSelectedIndices(selected_indices);
    }

    
    private void clearbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearbuttonActionPerformed
        seqnamelist.setSelectedIndices(new int[0]);
        if (showall == false) {
            showall = true;
            selectednamesbutton.setText("show selected names");
        }
        selectedvec = new Vector<int[]>();
        backbutton.setText("Back (0)");
    }//GEN-LAST:event_clearbuttonActionPerformed

    private void okbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okbuttonActionPerformed
        //don't forget to update globalselected!
        int[] myselected = seqnamelist.getSelectedIndices();
        if (myselected.length > 0) {
            if (showall) {
                parent.data.selectedSequencesIndices = myselected;
                parent.updateSelectionButtonLabel();
                parent.repaint();
            } else {//If I am only showing a subset of all sequences I need to reassign the correct indices
                globalselected = parent.data.selectedSequencesIndices;
                for (int i = myselected.length; --i >= 0;) {
                    myselected[i] = globalselected[myselected[i]];//reassign the sequence number globally
                }//end for i
                if (myselected.length < globalselected.length) {
                    if (parent.data.selectedSequencesIndices.length > 0) {//save the former data
                        selectedvec.addElement(parent.data.selectedSequencesIndices);
                        backbutton.setText("Back (" + selectedvec.size() + ")");
                    }
                }
                parent.data.selectedSequencesIndices = myselected;
                globalselected = myselected;
                //now update this window view
                selectednames = new String[parent.data.selectedSequencesIndices.length];
                for (int i = 0; i < selectednames.length; i++) {
                    selectednames[i] = namesarr[parent.data.selectedSequencesIndices[i]];
                }//end for i
                seqnamelist.setListData(selectednames);
                seqnamelist.setSelectedIndices(new int[0]);
                //now update the parent view
                parent.updateSelectionButtonLabel();
                parent.repaint();
            }
        }
    }//GEN-LAST:event_okbuttonActionPerformed

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog



    String[] namesarr;
    ProgramWindow parent;
    boolean showall = true;
    String[] selectednames;
    int[] globalselected = new int[0];
    Vector<int[]> selectedvec = new Vector<int[]>();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backbutton;
    private javax.swing.JPanel buttonpanel;
    private javax.swing.JButton clearbutton;
    private javax.swing.JButton closebutton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton okbutton;
    private javax.swing.JButton savebutton;
    private javax.swing.JButton searchbutton;
    private javax.swing.JButton selectednamesbutton;
    public javax.swing.JList seqnamelist;
    // End of variables declaration//GEN-END:variables


    /**
     * 
     * @param query a regular expression
     * @param namesarr the strings against which the query is checked 
     * @return the indices of all strings matched by the query
     */
    int[] getmatches(String query, String[] namesarr) {
        
        if (query != null && query.length() > 0) {
            String[] tmparr = query.split("\\s\\|\\|\\s");
            if (namesarr == null) {
                javax.swing.JOptionPane.showMessageDialog(this, "Namesarr=null; close this window and open a new one");
                return new int[0];
            }
            int namesnum = namesarr.length;
            Vector<Integer> tmpvec = new Vector<Integer>();
            
            if (tmparr[0].equals("^exact^")) {
                for (int j = tmparr.length; --j > 0;) {//skip element 0
                    tmparr[j] = tmparr[j].replaceAll("\\|", "\\\\\\|");
                    tmparr[j] = tmparr[j].replaceAll("\\[", "\\\\\\[");
                    tmparr[j] = tmparr[j].replaceAll("\\]", "\\\\\\]");
                    tmparr[j] = tmparr[j].replaceAll("\\(", "\\\\\\(");
                    tmparr[j] = tmparr[j].replaceAll("\\)", "\\\\\\)");
                    query = "^\\d+\\s+"+tmparr[j]+"\\s*$";
                    System.out.println("query='" + query + "'");

                    for (int i = 0; i < namesnum; i++) {
                        if (namesarr[i].matches(query)) {
                            tmpvec.addElement(new Integer(i));
                        }
                    }
                }
                
            } else {//non-exact matching
                for (int j = tmparr.length; --j >= 0;) {
                    tmparr[j] = tmparr[j].replaceAll("\\|", "\\\\\\|");
                    tmparr[j] = tmparr[j].replaceAll("\\[", "\\\\\\[");
                    tmparr[j] = tmparr[j].replaceAll("\\]", "\\\\\\]");
                    tmparr[j] = tmparr[j].replaceAll("\\(", "\\\\\\(");
                    tmparr[j] = tmparr[j].replaceAll("\\)", "\\\\\\)");
                    query = "(?i).*" + tmparr[j] + ".*";
                    System.out.println("query='" + query + "'");
                    for (int i = 0; i < namesnum; i++) {
                        if (namesarr[i].matches(query)) {
                            tmpvec.addElement(new Integer(i));
                        }
                    }//end for i
                }
            }
            int[] retarr = new int[tmpvec.size()];
            for (int i = 0; i < tmpvec.size(); i++) {
                retarr[i] = ((Integer) tmpvec.elementAt(i)).intValue();
            }
            return retarr;
        } else {
            return new int[0];
        }
    }

    
    /**
     * 
     * @param innames
     * @return
     */
    String[] numberarr(String[] innames) {
        int elements = innames.length;
        String[] retarr = new String[elements];
        int numlength = (String.valueOf(elements).length()) + 1;
        StringBuffer tmpstrbuff = new StringBuffer();
        
        for (int i = 0; i < elements; i++) {
            tmpstrbuff.setLength(0);
            tmpstrbuff.append(i);
            for (int j = tmpstrbuff.length(); j < numlength; j++) {
                tmpstrbuff.append(" ");
            }
            retarr[i] = tmpstrbuff + innames[i];
        }
        
        return retarr;
    }

    
    /**
     * Sets the selected names
     * @param selectednamesint indices of the selected entries
     */
    void setselected(int[] selectednamesint) {
        seqnamelist.setSelectedIndices(selectednamesint);
    }

    /**
     * Sets the selected names and decides whether to show all or only the selected entries
     * @param selectednamesint
     * @param showonly if true, only show the selected entries
     */
    void setselected(int[] selectednamesint, boolean showonly) {
        setselected(selectednamesint);
        
        if (showonly) {//if I only want to see the selected names
        
            selectednames = new String[selectednamesint.length];
            
            for (int i = 0; i < selectednames.length; i++) {
                selectednames[i] = namesarr[selectednamesint[i]];
            }
            
            seqnamelist.setListData(selectednames);
            seqnamelist.setSelectedIndices(new int[0]);
            
            showall = false;
            selectednamesbutton.setText("show all names");
        }
    }
}

