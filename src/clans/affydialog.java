package clans;

import javax.swing.*;
import java.util.*;
import java.io.*;

public class affydialog extends javax.swing.JFrame {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4358800283763007044L;
    
    public affydialog(ClusteringWithGui parent) {
        this.parent=parent;
        initComponents();
        if(parent.avgfoldchange==true){
            avgcheckbox.setSelected(true);
        }else{
            avgcheckbox.setSelected(false);
        }
        if(parent.usefoldchange==true){
            foldchangecheckbox.setSelected(true);
        }else{
            foldchangecheckbox.setSelected(false);
        }
        if(parent.affyfiles!=null){
            datavec=parent.affyfiles;
            replicates myreplicate;
            for(int i=0;i<datavec.size();i++){
                myreplicate=datavec.elementAt(i);
                if(myreplicate.abbreviation!=null){
                    datanamesvec.addElement(myreplicate.abbreviation+":"+myreplicate.name+"/"+myreplicate.wtname);
                }else{
                    datanamesvec.addElement(myreplicate.name+"/"+myreplicate.wtname);
                }
            }//end for i
            datalist.setListData(datanamesvec);
            datalist.setSelectedIndices(new int[0]);
        }else{
            parent.affyfiles=datavec;
        }
        repaint();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        datpanel = new javax.swing.JPanel();
        buttonpanel = new javax.swing.JPanel();
        dbuttonpanel1 = new javax.swing.JPanel();
        adddatabutton = new javax.swing.JButton();
        namedatabutton = new javax.swing.JButton();
        rmdatabutton = new javax.swing.JButton();
        rbuttonpanel = new javax.swing.JPanel();
        addreplicatebutton = new javax.swing.JButton();
        rmreplicatebutton = new javax.swing.JButton();
        wtbuttonpanel = new javax.swing.JPanel();
        addwtbutton = new javax.swing.JButton();
        rmwtbutton = new javax.swing.JButton();
        fieldpanel = new javax.swing.JPanel();
        datascrollpanel = new javax.swing.JScrollPane();
        datalist = new javax.swing.JList();
        replicatescrollpanel = new javax.swing.JScrollPane();
        replicatelist = new javax.swing.JList();
        wtscrollpanel = new javax.swing.JScrollPane();
        wtreplicatelist = new javax.swing.JList();
        butpanel = new javax.swing.JPanel();
        loadbutton = new javax.swing.JButton();
        showbutton = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        filemenu = new javax.swing.JMenu();
        loadmenuitem = new javax.swing.JMenuItem();
        miscmenu = new javax.swing.JMenu();
        drawplotmenuitem = new javax.swing.JMenuItem();
        foldchangecheckbox = new javax.swing.JCheckBoxMenuItem();
        avgcheckbox = new javax.swing.JCheckBoxMenuItem();

        setTitle("Microarray Data");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        datpanel.setLayout(new java.awt.BorderLayout());

        buttonpanel.setLayout(new java.awt.GridLayout(0, 1));

        dbuttonpanel1.setLayout(new java.awt.GridLayout(0, 1));

        adddatabutton.setText("Add Data");
        adddatabutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adddatabuttonActionPerformed(evt);
            }
        });

        dbuttonpanel1.add(adddatabutton);

        namedatabutton.setText("Assign abbreviation");
        namedatabutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namedatabuttonActionPerformed(evt);
            }
        });

        dbuttonpanel1.add(namedatabutton);

        rmdatabutton.setText("Remove Data");
        rmdatabutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmdatabuttonActionPerformed(evt);
            }
        });

        dbuttonpanel1.add(rmdatabutton);

        buttonpanel.add(dbuttonpanel1);

        rbuttonpanel.setLayout(new java.awt.GridLayout(0, 1));

        addreplicatebutton.setText("Add Replicate");
        addreplicatebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addreplicatebuttonActionPerformed(evt);
            }
        });

        rbuttonpanel.add(addreplicatebutton);

        rmreplicatebutton.setText("Remove Replicate");
        rmreplicatebutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmreplicatebuttonActionPerformed(evt);
            }
        });

        rbuttonpanel.add(rmreplicatebutton);

        buttonpanel.add(rbuttonpanel);

        wtbuttonpanel.setLayout(new java.awt.GridLayout(0, 1));

        addwtbutton.setText("Add Reference (WT)");
        addwtbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addwtbuttonActionPerformed(evt);
            }
        });

        wtbuttonpanel.add(addwtbutton);

        rmwtbutton.setText("Remove Reference (WT)");
        rmwtbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rmwtbuttonActionPerformed(evt);
            }
        });

        wtbuttonpanel.add(rmwtbutton);

        buttonpanel.add(wtbuttonpanel);

        datpanel.add(buttonpanel, java.awt.BorderLayout.WEST);

        fieldpanel.setLayout(new java.awt.GridLayout(0, 1));

        datalist.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                datalistValueChanged(evt);
            }
        });

        datascrollpanel.setViewportView(datalist);

        fieldpanel.add(datascrollpanel);

        replicatescrollpanel.setViewportView(replicatelist);

        fieldpanel.add(replicatescrollpanel);

        wtscrollpanel.setPreferredSize(new java.awt.Dimension(250, 150));
        wtscrollpanel.setViewportView(wtreplicatelist);

        fieldpanel.add(wtscrollpanel);

        datpanel.add(fieldpanel, java.awt.BorderLayout.CENTER);

        butpanel.setLayout(new java.awt.GridLayout(1, 0));

        loadbutton.setText("Load Expression Data");
        loadbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadbuttonActionPerformed(evt);
            }
        });

        butpanel.add(loadbutton);

        showbutton.setText("Show selecteds");
        showbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showbuttonActionPerformed(evt);
            }
        });

        butpanel.add(showbutton);

        datpanel.add(butpanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(datpanel, java.awt.BorderLayout.CENTER);

        filemenu.setText("File");
        loadmenuitem.setText("Load");
        loadmenuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadmenuitemActionPerformed(evt);
            }
        });

        filemenu.add(loadmenuitem);

        jMenuBar.add(filemenu);

        miscmenu.setText("Misc");
        drawplotmenuitem.setText("Look for expression graph");
        drawplotmenuitem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                drawplotmenuitemActionPerformed(evt);
            }
        });

        miscmenu.add(drawplotmenuitem);

        foldchangecheckbox.setText("Use Foldchange");
        miscmenu.add(foldchangecheckbox);

        avgcheckbox.setText("Avg Foldchange");
        miscmenu.add(avgcheckbox);

        jMenuBar.add(miscmenu);

        setJMenuBar(jMenuBar);

        pack();
    }//GEN-END:initComponents
    
    private void namedatabuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namedatabuttonActionPerformed
        // assign a name to this data object
        int selected=datalist.getSelectedIndex();
        if(selected>-1){
            String newname=null;
            replicates myreplicate=(replicates) datavec.get(selected);
            if(myreplicate.abbreviation!=null){
                newname=javax.swing.JOptionPane.showInputDialog(this,"Set name",myreplicate.abbreviation);
            }else{
                newname=javax.swing.JOptionPane.showInputDialog(this,"Set name",String.valueOf(selected));
            }
            if(newname!=null){
                myreplicate.abbreviation=newname;
                datanamesvec.set(selected,myreplicate.abbreviation+":"+myreplicate.name+"/"+myreplicate.wtname);
                repaint();
            }
        }else{
            javax.swing.JOptionPane.showMessageDialog(this,"No dataset selected");
        }
    }//GEN-LAST:event_namedatabuttonActionPerformed
    
    private void drawplotmenuitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_drawplotmenuitemActionPerformed
        // have the user draw an expression graph and specify the correlation he wants (and corr-limit)
        //then set as selected all the sequences that have >=that correlation to the specified graph.
        if(valhash.size()>0){
            new affygetgraphcorrdialog(parent,this).setVisible(true);
        }else{
            javax.swing.JOptionPane.showMessageDialog(this,"Load the affymetrix data first (press the load-data button, bottom left)");
        }
    }//GEN-LAST:event_drawplotmenuitemActionPerformed
    
    private void showbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showbuttonActionPerformed
        // open a new window showing the expression levels for the selected datapoints
        //the currently selected elements are present in selectednames (int[])
        ArrayList<datapoint[]> tmplist=new ArrayList<datapoint[]>();
        String tmpname;
        if(parent.data.selectedSequencesIndices.length<=0){
            javax.swing.JOptionPane.showMessageDialog(this,"No sequences selected");
            return;
        }
        for(int i=parent.data.selectedSequencesIndices.length;--i>=0;){
            tmpname=(parent.data.sequence_names[parent.data.selectedSequencesIndices[i]]).trim();
            if(valhash.containsKey(tmpname)){
                tmplist.add(valhash.get(tmpname));
            }else{
                System.err.println("unknown name '"+tmpname+"'");
            }
        }//end for i
        if(tmplist.size()>0){
            new affyplotdialog(tmplist,globalrelstdev,datavec).setVisible(true);
        }
    }//GEN-LAST:event_showbuttonActionPerformed
    
    private void loadbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadbuttonActionPerformed
        //load the data from the files
        valhash.clear();
        Vector<datapoint[]> mydat=affyutils.readdata(datavec);
        //if I wan to use the fold-changes, convert the values
        if(foldchangecheckbox.isSelected()){
            parent.usefoldchange=true;
        }else{
            parent.usefoldchange=false;
        }
        if(avgcheckbox.isSelected()){
            parent.avgfoldchange=true;
        }else{
            parent.avgfoldchange=false;
        }
        if(parent.usefoldchange){
            System.out.println("Using foldchange; avgfoldchange="+parent.avgfoldchange);
            mydat=affyutils.getfoldchange(mydat,parent.avgfoldchange);
        }
        //now get the global relative stdev
        globalrelstdev=affyutils.getglobalrelstdev(mydat);
        System.err.println("globalrelstdev="+globalrelstdev);
        //now convert the Vector into a hash for easier lookup
        datapoint[] tmpdat;
        for(int i=mydat.size();--i>=0;){
            tmpdat=(datapoint[])mydat.elementAt(i);
            if(valhash.containsKey(tmpdat[0].name)){
                System.err.println(tmpdat[0].name+" already defined");
            }else{
                valhash.put(tmpdat[0].name,tmpdat);
            }
        }//end for i
        //now I have all the affydata in memory and can look it up by probe name
    }//GEN-LAST:event_loadbuttonActionPerformed
    
    private void loadmenuitemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadmenuitemActionPerformed
        // load the settings from a file
        fc.setMultiSelectionEnabled(false);
        fc.setDialogTitle("Select file to load from");
        fc.setApproveButtonText("OK");
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File savefile=fc.getSelectedFile();
            if(affyutils.load(savefile,this)<0){
                javax.swing.JOptionPane.showMessageDialog(this,"ERROR loading data from '"+savefile.getName()+"'");
            }else{
                javax.swing.JOptionPane.showMessageDialog(this,"DONE");
            }
            //and now update all the necessary values
            if(parent.avgfoldchange==true){
                avgcheckbox.setSelected(true);
            }else{
                avgcheckbox.setSelected(false);
            }
            if(parent.usefoldchange==true){
                foldchangecheckbox.setSelected(true);
            }else{
                foldchangecheckbox.setSelected(false);
            }
            replicates myreplicate;
            parent.affyfiles=datavec;
            datanamesvec.clear();
            for(int i=0;i<datavec.size();i++){
                myreplicate=(replicates)datavec.elementAt(i);
                if((myreplicate.abbreviation!=null)&&(myreplicate.abbreviation.equals("null")==false)){
                    datanamesvec.addElement(myreplicate.abbreviation+":"+myreplicate.name+"/"+myreplicate.wtname);
                }else{
                    datanamesvec.addElement(myreplicate.name+"/"+myreplicate.wtname);
                }
            }//end for i
            datalist.setListData(datanamesvec);
            datalist.setSelectedIndices(new int[0]);
        }
        repaint();
    }//GEN-LAST:event_loadmenuitemActionPerformed
    
    private void datalistValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_datalistValueChanged
        // update the replicatevec
        //System.out.println("value changed");
        int currelement=datalist.getSelectedIndex();
        if(currelement!=-1){//if I have a selected value
            replicatevec.clear();
            for(int j=0;j<((replicates)datavec.elementAt(currelement)).replicates;j++){
                replicatevec.addElement(((replicates)datavec.elementAt(currelement)).replicate[j]);
            }//end for j
            replicatelist.setListData(replicatevec);
            replicatelist.setSelectedIndices(new int[0]);
            //and now for the wildtype replicates
            wtreplicatevec.clear();
            for(int j=0;j<((replicates)datavec.elementAt(currelement)).wtreplicates;j++){
                wtreplicatevec.addElement(((replicates)datavec.elementAt(currelement)).wtreplicate[j]);
            }//end for j
            wtreplicatelist.setListData(wtreplicatevec);
            wtreplicatelist.setSelectedIndices(new int[0]);
            repaint();
        }
    }//GEN-LAST:event_datalistValueChanged
    
    private void rmwtbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmwtbuttonActionPerformed
        // remove the currently selected replicates from the currently selected wtdata element
        int datanum=datalist.getSelectedIndex();
        int[] currelements=replicatelist.getSelectedIndices();
        for(int i=currelements.length-1;i>=0;i--){
            ((replicates)datavec.elementAt(datanum)).wtreplicate[currelements[i]]=null;
        }//end for i
        File[] newreplicates=new File[((replicates)datavec.elementAt(datanum)).replicates-currelements.length];
        int counter=0;
        for(int i=0;i<newreplicates.length;i++){
            newreplicates[i]=((replicates)datavec.elementAt(datanum)).replicate[i+counter];
            if(newreplicates[i]==null){
                i--;
                counter++;
            }
        }//end for i
        ((replicates)datavec.elementAt(datanum)).wtreplicate=newreplicates;
        ((replicates)datavec.elementAt(datanum)).wtreplicates=newreplicates.length;
        wtreplicatevec.clear();
        for(int j=0;j<((replicates)datavec.elementAt(datanum)).replicates;j++){
            wtreplicatevec.addElement(((replicates)datavec.elementAt(datanum)).wtreplicate[j]);
        }//end for j
        replicatelist.setListData(wtreplicatevec);
        replicatelist.setSelectedIndices(new int[0]);
        repaint();
    }//GEN-LAST:event_rmwtbuttonActionPerformed
    
    private void addwtbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addwtbuttonActionPerformed
        //add more files to this wtdata element
        int datanum=datalist.getSelectedIndex();
        if(datanum>-1){//if I have an element selected
            fc.setMultiSelectionEnabled(true);
            fc.setDialogTitle("Select reference replicates (Wild-type)");
            fc.setApproveButtonText("ACCEPT REFERENCE (WT)");
            int returnval=fc.showOpenDialog(this);
            if(returnval == JFileChooser.APPROVE_OPTION) {
                File[] addfiles=fc.getSelectedFiles();
                replicates myreplicate=(replicates)datavec.elementAt(datanum);
                File[] newfiles=new File[myreplicate.replicates+addfiles.length];
                for(int i=0;i<myreplicate.replicates;i++){
                    newfiles[i]=myreplicate.replicate[i];
                }//end for i
                for(int i=0;i<addfiles.length;i++){
                    newfiles[i+myreplicate.replicates]=addfiles[i];
                }//end for i
                myreplicate.wtreplicate=newfiles;
                myreplicate.wtreplicates=myreplicate.wtreplicate.length;
                //now update with the new data
            }
            repaint();
        }
    }//GEN-LAST:event_addwtbuttonActionPerformed
    
    private void rmreplicatebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmreplicatebuttonActionPerformed
        // remove the currently selected replicates from the currently selected data element
        int datanum=datalist.getSelectedIndex();
        int[] currelements=replicatelist.getSelectedIndices();
        for(int i=currelements.length-1;i>=0;i--){
            ((replicates)datavec.elementAt(datanum)).replicate[currelements[i]]=null;
        }//end for i
        File[] newreplicates=new File[((replicates)datavec.elementAt(datanum)).replicates-currelements.length];
        int counter=0;
        for(int i=0;i<newreplicates.length;i++){
            newreplicates[i]=((replicates)datavec.elementAt(datanum)).replicate[i+counter];
            if(newreplicates[i]==null){
                i--;
                counter++;
            }
        }//end for i
        ((replicates)datavec.elementAt(datanum)).replicate=newreplicates;
        ((replicates)datavec.elementAt(datanum)).replicates=newreplicates.length;
        replicatevec.clear();
        for(int j=0;j<((replicates)datavec.elementAt(datanum)).replicates;j++){
            replicatevec.addElement(((replicates)datavec.elementAt(datanum)).replicate[j]);
        }//end for j
        replicatelist.setListData(replicatevec);
        replicatelist.setSelectedIndices(new int[0]);
        repaint();
    }//GEN-LAST:event_rmreplicatebuttonActionPerformed
    
    private void addreplicatebuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addreplicatebuttonActionPerformed
        //add more files to this data element
        int datanum=datalist.getSelectedIndex();
        if(datanum>-1){//if I have an element selected
            fc.setMultiSelectionEnabled(true);
            fc.setDialogTitle("Select Data replicates");
            fc.setApproveButtonText("ACCEPT DATA");
            int returnval=fc.showOpenDialog(this);
            if(returnval == JFileChooser.APPROVE_OPTION) {
                File[] addfiles=fc.getSelectedFiles();
                replicates myreplicate=(replicates)datavec.elementAt(datanum);
                File[] newfiles=new File[myreplicate.replicates+addfiles.length];
                for(int i=0;i<myreplicate.replicates;i++){
                    newfiles[i]=myreplicate.replicate[i];
                }//end for i
                for(int i=0;i<addfiles.length;i++){
                    newfiles[i+myreplicate.replicates]=addfiles[i];
                }//end for i
                myreplicate.replicate=newfiles;
                myreplicate.replicates=myreplicate.replicate.length;
                //now update with the new data
                
                
            }
            repaint();
        }
    }//GEN-LAST:event_addreplicatebuttonActionPerformed
    
    /**
     *  remove an element from the data vector
     * @param evt
     */
    private void rmdatabuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rmdatabuttonActionPerformed
        int[] currelements=datalist.getSelectedIndices();
        for(int i=currelements.length-1;i>=0;i--){
            datavec.removeElementAt(currelements[i]);
            datanamesvec.removeElementAt(currelements[i]);
        }
        
        datalist.setListData(datanamesvec);
        datalist.setSelectedIndices(new int[0]);
        
        replicatelist.setListData(new Vector<File>());
        replicatelist.setSelectedIndices(new int[0]);
        
        wtreplicatelist.setListData(new Vector<File>());
        wtreplicatelist.setSelectedIndices(new int[0]);
        
        repaint();
    }//GEN-LAST:event_rmdatabuttonActionPerformed
    
    private void adddatabuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adddatabuttonActionPerformed
        // add a list of files to datavec
        fc.setMultiSelectionEnabled(true);
        fc.setDialogTitle("Select Data replicates");
        fc.setApproveButtonText("ACCEPT DATA");
        int returnval=fc.showOpenDialog(this);
        if(returnval == JFileChooser.APPROVE_OPTION) {
            File[] addfiles=fc.getSelectedFiles();
            replicates myreplicate=new replicates();
            myreplicate.replicate=addfiles;
            myreplicate.replicates=addfiles.length;
            //now get an identifier for these files
            String myname=myreplicate.replicate[0].getName();
            String checkname;
            for(int i=0;i<myreplicate.replicates;i++){
                checkname=myreplicate.replicate[i].getName();
                while((checkname.startsWith(myname)==false)&&(myname.length()>0)){
                    myname=myname.substring(0,myname.length()-1);
                }
            }//end for i
            if(myname.length()<1){
                myname=String.valueOf(datavec.size());
            }
            //System.out.println("myname="+myname);
            myreplicate.name=myname;
            //now get the corresponding wild-type
            fc.setDialogTitle("Select reference replicates (Wild-type)");
            fc.setApproveButtonText("ACCEPT REFERENCE (WT)");
            if(wtfiles!=null){
                //set as preselected the last selected wild-type files
                fc.setSelectedFiles(wtfiles);
            }
            returnval=fc.showOpenDialog(this);
            if(returnval == JFileChooser.APPROVE_OPTION){
                //now get the correcponding wild-type data
                addfiles=fc.getSelectedFiles();
                wtfiles=addfiles;
                myreplicate.wtreplicate=addfiles;
                myreplicate.wtreplicates=addfiles.length;
                //now get an identifier for the wild-type files
                myname=myreplicate.wtreplicate[0].getName();
                for(int i=0;i<myreplicate.wtreplicates;i++){
                    checkname=myreplicate.wtreplicate[i].getName();
                    while((checkname.startsWith(myname)==false)&&(myname.length()>0)){
                        myname=myname.substring(0,myname.length()-1);
                    }
                }//end for i
                if(myname.length()<1){
                    myname=String.valueOf(datavec.size());
                }
                //System.out.println("myname="+myname);
                myreplicate.wtname=myname;
            }else{
                //user cancelled
                return;
            }
            //and now finally add the replicates element
            datavec.addElement(myreplicate);
            datanamesvec.addElement(myreplicate.name+"/"+myreplicate.wtname);
            datalist.setListData(datanamesvec);
            datalist.setSelectedIndices(new int[0]);
        }
        repaint();
    }//GEN-LAST:event_adddatabuttonActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
    //public static void main(String args[]) {
    //    new affydialog().show();
    //}
    
    ClusteringWithGui parent;
    JFileChooser fc=new JFileChooser(new File("."));
    File[] wtfiles=null;
    Vector<String> datanamesvec = new Vector<String>();// names for the data elements
    Vector<replicates> datavec = new Vector<replicates>();//the data+replicates
    Vector<File> replicatevec = new Vector<File>();// temporary replicate element Vector
    Vector<File> wtreplicatevec = new Vector<File>();// temporary vector for wt replicate data
    HashMap<String, datapoint[]> valhash=new HashMap<String, datapoint[]>();
    float globalrelstdev=0;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton adddatabutton;
    private javax.swing.JButton addreplicatebutton;
    private javax.swing.JButton addwtbutton;
    private javax.swing.JCheckBoxMenuItem avgcheckbox;
    private javax.swing.JPanel butpanel;
    private javax.swing.JPanel buttonpanel;
    private javax.swing.JList datalist;
    private javax.swing.JScrollPane datascrollpanel;
    private javax.swing.JPanel datpanel;
    private javax.swing.JPanel dbuttonpanel1;
    private javax.swing.JMenuItem drawplotmenuitem;
    private javax.swing.JPanel fieldpanel;
    private javax.swing.JMenu filemenu;
    private javax.swing.JCheckBoxMenuItem foldchangecheckbox;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JButton loadbutton;
    private javax.swing.JMenuItem loadmenuitem;
    private javax.swing.JMenu miscmenu;
    private javax.swing.JButton namedatabutton;
    private javax.swing.JPanel rbuttonpanel;
    private javax.swing.JList replicatelist;
    private javax.swing.JScrollPane replicatescrollpanel;
    private javax.swing.JButton rmdatabutton;
    private javax.swing.JButton rmreplicatebutton;
    private javax.swing.JButton rmwtbutton;
    private javax.swing.JButton showbutton;
    private javax.swing.JPanel wtbuttonpanel;
    private javax.swing.JList wtreplicatelist;
    private javax.swing.JScrollPane wtscrollpanel;
    // End of variables declaration//GEN-END:variables
    
}

class veclistcellrenderer extends javax.swing.JLabel implements javax.swing.ListCellRenderer {

    /**
     * 
     */
    private static final long serialVersionUID = 6805731310066182683L;

    /**
     * This is the only method defined by ListCellRenderer. We just reconfigure the JLabel each time we're called.
     */
    public java.awt.Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        String s = ((File)value).getAbsolutePath();
        setText(s);
        if (isSelected){
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        }else{
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setOpaque(true);
        return this;
    }
}

