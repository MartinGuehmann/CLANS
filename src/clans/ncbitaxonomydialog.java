package clans;

import java.io.*;
import java.util.*;

public class ncbitaxonomydialog extends javax.swing.JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 8114400353377055870L;

    public ncbitaxonomydialog(ClusteringWithGui parent) {
        this.parent=parent;
        initComponents();
        if(parent.namesdmp_file!=null){
            namestextfield.setText(parent.namesdmp_file);
        }
        if(parent.nodesdmp_file!=null){
            nodestextfield.setText(parent.nodesdmp_file);
        }
        childlist.setModel(childmodel);
        parentlist.setModel(parentmodel);
        parentlist.addMouseListener(mouseListenerparent);
        childlist.addMouseListener(mouseListenerchild);
        repaint();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        mainpanel = new javax.swing.JPanel();
        buttonpanel = new javax.swing.JPanel();
        namesbutton = new javax.swing.JButton();
        namestextfield = new javax.swing.JTextField();
        nodesbutton = new javax.swing.JButton();
        nodestextfield = new javax.swing.JTextField();
        loadbutton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        selectedspanel = new javax.swing.JPanel();
        centerpanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textarea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        calcbutton = new javax.swing.JButton();
        taxonomypanel = new javax.swing.JPanel();
        findpanel = new javax.swing.JPanel();
        findlabel = new javax.swing.JLabel();
        findtextfield = new javax.swing.JTextField();
        findtextbutton = new javax.swing.JButton();
        updownpanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        parentlist = new javax.swing.JList();
        jScrollPane3 = new javax.swing.JScrollPane();
        childlist = new javax.swing.JList();
        showpanel = new javax.swing.JPanel();
        showbutton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        mainpanel.setLayout(new java.awt.BorderLayout());

        buttonpanel.setLayout(new java.awt.GridLayout());

        namesbutton.setText("names.dmp");
        namesbutton.setToolTipText("select the path to the names.dmp file");
        namesbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namesbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(namesbutton);

        namestextfield.setEditable(false);
        namestextfield.setText("UNDEFINED");
        buttonpanel.add(namestextfield);

        nodesbutton.setText("nodes.dmp");
        nodesbutton.setToolTipText("select the path to the nodes.dmp file");
        nodesbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nodesbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(nodesbutton);

        nodestextfield.setEditable(false);
        nodestextfield.setText("UNDEFINED");
        buttonpanel.add(nodestextfield);

        loadbutton.setText("Load Data");
        loadbutton.setToolTipText("load the data");
        loadbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadbuttonActionPerformed(evt);
            }
        });
        buttonpanel.add(loadbutton);

        mainpanel.add(buttonpanel, java.awt.BorderLayout.NORTH);

        getContentPane().add(mainpanel, java.awt.BorderLayout.NORTH);

        selectedspanel.setLayout(new java.awt.BorderLayout());

        centerpanel.setLayout(new java.awt.BorderLayout());

        textarea.setColumns(20);
        textarea.setEditable(false);
        textarea.setRows(5);
        jScrollPane1.setViewportView(textarea);

        centerpanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        selectedspanel.add(centerpanel, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.BorderLayout());

        calcbutton.setText("Get Taxonomic Info for Selected");
        calcbutton.setToolTipText("display the taxonomic groups for the selected sequences");
        calcbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcbuttonActionPerformed(evt);
            }
        });
        jPanel2.add(calcbutton, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.SOUTH);

        selectedspanel.add(jPanel1, java.awt.BorderLayout.SOUTH);

        jTabbedPane1.addTab("Selected sequences", selectedspanel);

        taxonomypanel.setLayout(new java.awt.BorderLayout());

        findpanel.setLayout(new java.awt.GridLayout());

        findlabel.setText("Find Taxonomy");
        findpanel.add(findlabel);

        findtextfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findtextfieldActionPerformed(evt);
            }
        });
        findpanel.add(findtextfield);

        findtextbutton.setText("Check");
        findtextbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findtextbuttonActionPerformed(evt);
            }
        });
        findpanel.add(findtextbutton);

        taxonomypanel.add(findpanel, java.awt.BorderLayout.PAGE_START);

        updownpanel.setLayout(new javax.swing.BoxLayout(updownpanel, javax.swing.BoxLayout.LINE_AXIS));

        parentlist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(parentlist);

        jSplitPane1.setLeftComponent(jScrollPane2);

        childlist.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane3.setViewportView(childlist);

        jSplitPane1.setRightComponent(jScrollPane3);

        updownpanel.add(jSplitPane1);

        taxonomypanel.add(updownpanel, java.awt.BorderLayout.CENTER);

        showpanel.setLayout(new java.awt.BorderLayout());

        showbutton.setText("Select sequences");
        showbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showbuttonActionPerformed(evt);
            }
        });
        showpanel.add(showbutton, java.awt.BorderLayout.CENTER);

        taxonomypanel.add(showpanel, java.awt.BorderLayout.PAGE_END);

        jTabbedPane1.addTab("By Taxonomy", taxonomypanel);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void namesbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namesbuttonActionPerformed
        // open the file chooser and get the ncbi names.dmp file
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            namestextfield.setText(fc.getSelectedFile().getAbsolutePath());
            repaint();
        }
    }//GEN-LAST:event_namesbuttonActionPerformed

    private void nodesbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nodesbuttonActionPerformed
        // open the file chooser and get the ncbi nodes.dmp file
        int returnVal = fc.showOpenDialog(this);
        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            nodestextfield.setText(fc.getSelectedFile().getAbsolutePath());
            repaint();
        }
    }//GEN-LAST:event_nodesbuttonActionPerformed

    private void loadbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadbuttonActionPerformed
        // open both the files specified in the names and nodes textfileds and read the data into memory
        String namesfile=namestextfield.getText();
        String nodesfile=nodestextfield.getText();
        if(namesfile.length()>0 && (namesfile.equalsIgnoreCase("undefined")==false) && namesfile.length()>0 && (namesfile.equalsIgnoreCase("undefined")==false)){
            try{
                BufferedReader namesread=new BufferedReader(new FileReader(namesfile));
                BufferedReader nodesread=new BufferedReader(new FileReader(nodesfile));
                System.out.println("Loading data from '"+namesfile+"' and '"+nodesfile+"'");
                String inline;
                //read the names file line by line
                String[] tmparr;
                String nodenum;
                String specname;
                String desc;
                String pnode;
                String[] datarr=null;
                java.util.regex.Pattern patt = java.util.regex.Pattern.compile("\t\\|\t?");
                while((inline=namesread.readLine())!=null){
                    //tmparr=inline.split("\\s+|\\s+");
                    tmparr=patt.split(inline,0);
                    if(tmparr.length<4){
                        System.err.println("WARNING: line '"+inline+"' does not split into >=4 parts; skipping entry");
                    }else{
                        nodenum=tmparr[0];
                        specname=tmparr[1];
                        desc=tmparr[3];
                        //System.out.println("num='"+nodenum+"' spec='"+specname+"' desc='"+desc+"'");
                        if(nameshash.containsKey(nodenum)){
                            datarr=nameshash.get(nodenum);
                        }else{
                            datarr=new String[4];
                            datarr[0]=specname;
                            datarr[1]=nodenum;
                            datarr[2]=null;//parent is undefined for the moment
                            datarr[3]=null;//node description is undefined for the moment
                            nameshash.put(nodenum, datarr);
                            //System.out.println("adding as NEW name:'"+specname+"'");
                        }
                        if(desc.equals("scientific name")){
                            //then overwrite any species name there was before in the array
                            datarr[0]=specname;
                            //System.out.println("adding as scientific name:'"+specname+"'");
                        }
                        //now add all of this data to the nameshash
                        nameshash.put(specname,datarr);
                    }
                }
                System.out.println("read "+nameshash.size()+" node types");
                //read the nodes file line by line
                while((inline=nodesread.readLine())!=null){
                    //tmparr=inline.split("\\s+|\\s+",3);
                    tmparr=patt.split(inline,4);
                    if(tmparr.length<4){
                        System.err.println("WARNING: line '"+inline+"' does not split into >=4 parts; skipping entry");
                    }else{
                        nodenum=tmparr[0];
                        pnode=tmparr[1];
                        desc=tmparr[2];
                        if(nameshash.containsKey(nodenum)){
                            datarr=nameshash.get(nodenum);
                            datarr[2]=pnode;
                            datarr[3]=desc;
                        }else{
                            System.err.println("WARNING: nameshash doen not contain '"+nodenum+" as key!, skipping entry");
                        }
                    }
                }
                namesread.close();
                nodesread.close();
            }catch (IOException ioe){
                javax.swing.JOptionPane.showMessageDialog(this,"ERROR, unable to read from either '"+namesfile+"' or '"+nodesfile+"'");
                didload=false;
            }
            javax.swing.JOptionPane.showMessageDialog(this,"DONE reading taxonomy data");
            didload=true;
            parent.namesdmp_file=namesfile;
            parent.nodesdmp_file=nodesfile;
        }else{
            javax.swing.JOptionPane.showMessageDialog(this,"NOTE: you need to specify the paths to the NCBI names.dmp and nodes.dmp taxonomy files before trying to load data!");
            didload=false;
        }
    }//GEN-LAST:event_loadbuttonActionPerformed

    private void calcbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcbuttonActionPerformed
        //take the selected sequences and read the taxonomic id's from their names (square brackets only)
        //then count how often each ID occurred and what the whole taxonomic path for each is
        if(didload==false){
            javax.swing.JOptionPane.showMessageDialog(this,"ERROR, you need to load some taxonomy data first! (select the files and press the \"Load data\" button");
            return;
        }
        int seqnum=parent.data.selectedSequencesIndices.length;
        if(seqnum<1){
            javax.swing.JOptionPane.showMessageDialog(this,"Please select some sequences in the main window");
        }else{
            ArrayList<String> speclist=new ArrayList<String>();
            java.util.regex.Pattern patt = java.util.regex.Pattern.compile("\\[(.+?)\\]");
            java.util.regex.Matcher mym=null;
            for(int i=0;i<seqnum;i++){
                //System.out.println("looking at '"+parent.namearr[parent.selectednames[i]]+"'");
                mym=patt.matcher(parent.data.sequence_names[parent.data.selectedSequencesIndices[i]]);
                while(mym.find()){
                    //System.out.println("\tspecname is:'"+mym.group(1)+"'");
                    speclist.add(mym.group(1));
                }
            }//end for i
            //now go through the list of species names I found and see how often each species was present
            HashMap<String,namecount> myhash=new HashMap<String,namecount>();
            String checkname="";
            String nodename="";
            String[] datarr;
            namecount mync=null;
            for(int i=speclist.size();--i>=0;){
                checkname=speclist.get(i);
                //System.out.println("checking '"+checkname+"'");
                if(nameshash.containsKey(checkname)){
                    datarr=nameshash.get(checkname);
                    nodename=datarr[1];
                    //System.out.println("\tconverts to nodename:'"+nodename+"'");
                    while((nodename.equals("1")==false) && nameshash.containsKey(nodename)){
                        if(myhash.containsKey(nodename)){
                            mync=myhash.get(nodename);
                            mync.count++;
                        }else{
                            mync=new namecount();
                            mync.name=datarr[0];
                            mync.count=1;
                            mync.desc=datarr[3];//taxonomic "level"
                            myhash.put(nodename, mync);
                        }
                        datarr=nameshash.get(nodename);
                        nodename=datarr[2];//the parent node
                        //System.out.println("\t\t--> parent="+nodename);
                    }
                    if(nodename.equals("1")==false){
                        System.err.println("WARNING: stopped taxonomy traceback for '"+checkname+"' before root at node '"+nodename+"'");
                    }
                }else{
                    System.err.println("WARNING: species name '"+checkname+"' is unknown; skipping entry");
                }
            }//end for i
            //now I have all of the taxonomic levels in myhash
            //now sort the entries by their counts and print out the text in descending order
            namecount[] ncarr=myhash.values().toArray(new namecount[0]);
            java.util.Arrays.sort(ncarr,new namecountcomparator());//and then by abundance of identifier
            //and now output the data to the textarea
            int size=ncarr.length;
            StringBuffer mybuff=new StringBuffer();
            float insize=speclist.size();
            for(int i=0;i<size;i++){
                mybuff.append(ncarr[i].name+" ("+ncarr[i].desc+") #:"+ncarr[i].count+" ("+((float)(int)((ncarr[i].count/insize)*100000))/1000+"%)"+newline);
            }//end for i
            textarea.setText(mybuff.toString());
            textarea.setCaretPosition(0);
        }
        repaint();
    }//GEN-LAST:event_calcbuttonActionPerformed

    private void findtextfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findtextfieldActionPerformed
        // I have entered a taxonomic name, see whether it is unique.
        String taxname=findtextfield.getText();
        if(nameshash.containsKey(taxname)==false){
            System.out.println("WARNING: no exact match found, searching through all taxonomic ID's");
            javax.swing.JOptionPane.showMessageDialog(this,"WARNING: no exact match found for '"+taxname+"', searching through all taxonomic ID's");
            //if the name is NOT found in nameshash
            String[] tmpnames=nameshash.keySet().toArray(new String[0]);
            //now go through all possible names and see whether the entered name matches any of them
            ArrayList<String>tmplist=new ArrayList<String>();
            ArrayList<String>tmplist2=new ArrayList<String>();
            java.util.regex.Pattern patt = java.util.regex.Pattern.compile(".*\\s+"+taxname+"\\s+.*");
            java.util.regex.Matcher mym=null;
            for(int i=tmpnames.length;--i>=0;){
                mym=patt.matcher(tmpnames[i]);
                if(mym.matches()){
                    tmplist.add(tmpnames[i]);//the name that matched
                    tmplist2.add(nameshash.get(tmpnames[i])[0]);//the scientific name
                }
            }//end for i
            if(tmplist.size()==0){
                javax.swing.JOptionPane.showMessageDialog(this,"ERROR, No taxonomic identifier matching ' "+taxname+"' could be found");
                return;
            }else if(tmplist.size()==1){
                //then only one MATCH was present
                taxname=tmplist.get(0);
            }else{
                //now call a blocking dialog that queries which of the species names is meant
                taxname=selecttaxdialog.gettax(this,tmplist,tmplist2);
                if(taxname==null){
                    return;
                }
            }
        }
        //if I get here I have a valid taxname!
        System.out.println("got taxname '"+taxname+"'");
        findtextfield.setText(taxname);
        String nodename=nameshash.get(taxname)[1];
        System.out.println("got nodename '"+nodename+"'");
        //now find the parents and kids of this node
        //first find the kids
        childmodel.clear();
        String[][] tmpvals=nameshash.values().toArray(new String[0][2]);
        HashMap<String[],Boolean> didhash=new HashMap<String[],Boolean>();//as many names can map to the same array, make sure I don't add the same array multiple times
        System.out.println("hashsize="+tmpvals.length);
        for(int i=tmpvals.length;--i>=0;){
            if((tmpvals[i][2]!=null) && (tmpvals[i][2].equals(nodename))){
                if(didhash.containsKey(tmpvals[i])==false){
                    childmodel.addElement(tmpvals[i][0]+" ("+tmpvals[i][1]+")");
                    didhash.put(tmpvals[i], null);
                }
            }
        }//end for i
        //then find the parents
        parentmodel.clear();
        while((nodename.equals("1")==false) && nameshash.containsKey(nodename)){
            if(nameshash.get(nodename)[2] !=null){
                nodename=nameshash.get(nodename)[2];//the parent node
                parentmodel.addElement(nameshash.get(nodename)[0]+" ("+nameshash.get(nodename)[1]+")");//the species name of the new node
            }else{
                System.out.println("GOT NULL FOR PARENT NODE ON '"+nodename+"'");
            }
        }
        if(nodename.equals("1")==false){
            System.err.println("WARNING: stopped taxonomy traceback for '"+taxname+"' before root at node '"+nodename+"'");
        }
        
        repaint();
    }//GEN-LAST:event_findtextfieldActionPerformed

    private void showbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showbuttonActionPerformed
        // now select all sequences in the map containing the current keyword
        String taxname=findtextfield.getText();
        if(taxname.length()<1){
            javax.swing.JOptionPane.showMessageDialog(this,"Please enter a taxonomic identifier to search for");
            return;
        }
        if(didload==false){
            javax.swing.JOptionPane.showMessageDialog(this,"ERROR, you need to load some taxonomy data first! (select the files and press the \"Load data\" button");
            return;
        }
        if (nameshash.get(taxname) == null || nameshash.get(taxname).length == 0) {
        	javax.swing.JOptionPane.showMessageDialog(this,"Taxonomic identifier has not yet been validated. please press the \"Check\" button first");
            return;        	
        }
        //it is probably fastest to get the full tax for my current ID and then to search all of the names from root--> species
        //only those that match my full selection length are valid hits
        ArrayList<String> mynodes=new ArrayList<String>();
        String myname, mytax;
        mytax=nameshash.get(taxname)[1];
        mynodes.add(mytax);
        while((mytax.equals("1")==false) && nameshash.containsKey(mytax)){
            if(nameshash.get(mytax)[2] !=null){
                mytax=nameshash.get(mytax)[2];//the parent node
                mynodes.add(mytax);
            }else{
                System.out.println("GOT NULL FOR PARENT NODE ON '"+mytax+"'");
            }
        }
        if(mytax.equals("1")==false){
            System.err.println("WARNING: stopped taxonomy traceback for '"+taxname+"' before root at node '"+mytax+"'");
        }
        //now I have the list of node elements I want to check for
        String[] checkarr=mynodes.toArray(new String[0]);
        //now go through all of the sequences in parent and see whether they have a valid TAXID, and then whether they match the tax-class I am looking for
        //also check whether currently I have any sequences selected in the cluster view, then base the search only on these
        int seqnum=parent.data.selectedSequencesIndices.length;
        String[] alttax;//a single ID may contain multiple taxid's
        ArrayList<String> speclist=new ArrayList<String>();
        java.util.regex.Pattern patt = java.util.regex.Pattern.compile("\\[(.+?)\\]");
        java.util.regex.Matcher mym=null;
        int checkindex;
        ArrayList<Integer> poslist=new ArrayList<Integer>();
        boolean useselected=false;
        if(seqnum>0){
        	//if I have a set of selected sequences
        	useselected=true;
        }else{
        	//if none are selected, use all
        	seqnum=parent.data.sequence_names.length;
        }
        for(int i=seqnum;--i>=0;){
        	//now get each name and see whether this name matches my given tax identifier
        	if(useselected){
        		myname=parent.data.sequence_names[parent.data.selectedSequencesIndices[i]];
        	}else{
        		myname=parent.data.sequence_names[i];
        	}
            //now get all of the taxonomic id's from this name
            mym=patt.matcher(myname);
            speclist.clear();
            while(mym.find()){
                //System.out.println("\tspecname is:'"+mym.group(1)+"'");
                speclist.add(mym.group(1));
            }
            if(speclist.size()<1){
                System.err.println("WARNING: no valid taxonomy found for sequence name '"+myname+"'");
            }else{
                alttax=speclist.toArray(new String[0]);
                //now check each of these in turn
                for(int j=speclist.size();--j>=0;){
                    mynodes.clear();
                    if(nameshash.containsKey(alttax[j])){
                        mytax=nameshash.get(alttax[j])[1];
                        mynodes.add(mytax);
                        while((mytax.equals("1")==false) && nameshash.containsKey(mytax)){
                            if(nameshash.get(mytax)[2] !=null){
                                mytax=nameshash.get(mytax)[2];//the parent node
                                mynodes.add(mytax);
                            }else{
                                System.out.println("GOT NULL FOR PARENT NODE ON '"+mytax+"'");
                            }
                        }
                        if(mytax.equals("1")==false){
                            System.err.println("WARNING: stopped taxonomy traceback for '"+taxname+"' before root at node '"+mytax+"'");
                        }
                        checkindex=checkarr.length-1;
                        //System.out.println("checking '"+alttax[j]+"'");
                        for(int k=mynodes.size();--k>=0;){
                            //by reading from the end I start at "root" and work my way towards species level
                            //System.out.println("\t"+checkarr[checkindex]+" ("+checkindex+")\t"+mynodes.get(k)+" ("+k+")");
                            if(checkarr[checkindex].equals(mynodes.get(k))){
                                checkindex--;
                                if(checkindex<0){
                                    //matched all, stop looking
                                    k=-1;
                                }
                            }else{//stop looking
                                k=-1;
                            }
                        }//end for j
                        if(checkindex<0){
                            //then all elements matched, set this sequence as true and look for the next one
                        	if(useselected){
                        		poslist.add(new Integer(parent.data.selectedSequencesIndices[i]));                        		
                        	}else{
                        		poslist.add(new Integer(i));
                        	}
                            j=-1;
                        }
                    }else{
                        System.err.println("WARNING: unknow taxonomic identifier '"+alttax[j]+"', skipping this taxid");
                    }
                }//end for j
            }
        }//end for i
        //now update the parent selectednames
        if(poslist.size()>0){
            parent.data.selectedSequencesIndices=new int[poslist.size()];
            for(int i=poslist.size();--i>=0;){
                parent.data.selectedSequencesIndices[i]=poslist.get(i).intValue();
            }//end for i
            parent.repaint();
        }else{
            javax.swing.JOptionPane.showMessageDialog(this,"No species matching this taxonomic listing found in main plot");
        }
    }//GEN-LAST:event_showbuttonActionPerformed

    private void findtextbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findtextbuttonActionPerformed
        //just sth. for people who don't like to (or can't) press "enter" (sometimes happens on terminal emulations))
        findtextfieldActionPerformed(evt);
    }//GEN-LAST:event_findtextbuttonActionPerformed

    /**
    * @param args the command line arguments
    */
    //public static void main(String args[]) {
    //    java.awt.EventQueue.invokeLater(new Runnable() {
    //        public void run() {
    //            new ncbitaxonomydialog().setVisible(true);
    //        }
    //    });
    //}

    ClusteringWithGui parent=null;
    static final javax.swing.JFileChooser fc=new javax.swing.JFileChooser(new File("."));
    HashMap <String,String[]> nameshash=new HashMap<String,String[]>();//lookup names--> nodes and nodes--> nodes and nodes--> names
    boolean didload=false;
    String newline = System.getProperty("line.separator");
    javax.swing.DefaultListModel parentmodel=new javax.swing.DefaultListModel();
    javax.swing.DefaultListModel childmodel=new javax.swing.DefaultListModel();


    java.awt.event.MouseListener mouseListenerparent = new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = parentlist.locationToIndex(e.getPoint());
                String taxname=(String)parentmodel.getElementAt(index);
                taxname=taxname.substring(0,taxname.indexOf("(")).trim();
                findtextfield.setText(taxname);
                findtextfieldActionPerformed(null);
             }
        }
    };
    java.awt.event.MouseListener mouseListenerchild = new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = childlist.locationToIndex(e.getPoint());
                String taxname=(String)childmodel.getElementAt(index);
                taxname=taxname.substring(0,taxname.indexOf("(")).trim();
                findtextfield.setText(taxname);
                findtextfieldActionPerformed(null);
             }
        }
    };


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonpanel;
    private javax.swing.JButton calcbutton;
    private javax.swing.JPanel centerpanel;
    private javax.swing.JList childlist;
    private javax.swing.JLabel findlabel;
    private javax.swing.JPanel findpanel;
    private javax.swing.JButton findtextbutton;
    private javax.swing.JTextField findtextfield;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loadbutton;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JButton namesbutton;
    private javax.swing.JTextField namestextfield;
    private javax.swing.JButton nodesbutton;
    private javax.swing.JTextField nodestextfield;
    private javax.swing.JList parentlist;
    private javax.swing.JPanel selectedspanel;
    private javax.swing.JButton showbutton;
    private javax.swing.JPanel showpanel;
    private javax.swing.JPanel taxonomypanel;
    private javax.swing.JTextArea textarea;
    private javax.swing.JPanel updownpanel;
    // End of variables declaration//GEN-END:variables


    class namecountcomparator implements Comparator<namecount> {
        //sort largest first
        public int compare(namecount nc1, namecount nc2) {
            int num1 = nc1.count;
            int num2 = nc2.count;
            return (num1 < num2 ? 1 : (num1 == num2 ? 0 : -1));
        }//end compare
    }//end comparator class



    class namecount{
        public namecount(){}
        int count=0;
        String name=null;
        String desc=null;
    }

}//end class
