package clans.gui;

import java.util.*;
import java.awt.*;

import clans.model.proteins.AminoAcidSequence;
import clans.model.proteins.HighScoringSegmentPair;

public class WindowShowBlastHitsOnSequence extends javax.swing.JDialog {
    
    /**
     * 
     */
    private static final long serialVersionUID = -5554164100601368922L;

    public WindowShowBlastHitsOnSequence(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public WindowShowBlastHitsOnSequence(ProgramWindow parent, HighScoringSegmentPair[] hsparr, int referenceseqnum, String[] namearr,
            AminoAcidSequence refseq, HashMap<String, Integer> nameshash) {
        //super(parent, false);
        this.parent=parent;
        this.hsparr=hsparr;
        this.referenceseqnum=referenceseqnum;
        this.referenceseq=parent.data.sequences[referenceseqnum];
        this.namearr=namearr;
        this.refseq=refseq;
        this.charseq=refseq.seq.toCharArray();
        this.seqlength=refseq.seq.length();
        this.nameshash=nameshash;
        this.setTitle(namearr[referenceseqnum]);
        makedata();//
        draw1=new drawdetail();
        draw2=new drawoverview();
        initComponents();
        resize();
        repaint();
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        mainpanel = new javax.swing.JPanel();
        overviewpanel = new javax.swing.JPanel();
        overviewpanel.add(draw2);
        detailpanel = new javax.swing.JPanel();
        detailpanel.add(draw1);
        xscrollbar = new javax.swing.JScrollBar();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        mainpanel.setLayout(new java.awt.GridLayout(0, 1));

        mainpanel.setPreferredSize(new java.awt.Dimension(640, 150));
        overviewpanel.setLayout(new javax.swing.BoxLayout(overviewpanel, javax.swing.BoxLayout.X_AXIS));

        overviewpanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        overviewpanel.setPreferredSize(new java.awt.Dimension(640, 240));
        overviewpanel.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                overviewpanelAncestorResized(evt);
            }
        });
        overviewpanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                overviewpanelMousePressed(evt);
            }
        });
        overviewpanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                overviewpanelMouseDragged(evt);
            }
        });

        mainpanel.add(overviewpanel);

        detailpanel.setLayout(new javax.swing.BoxLayout(detailpanel, javax.swing.BoxLayout.X_AXIS));

        detailpanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0)));
        detailpanel.setPreferredSize(new java.awt.Dimension(640, 240));
        mainpanel.add(detailpanel);

        getContentPane().add(mainpanel, java.awt.BorderLayout.CENTER);

        xscrollbar.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        xscrollbar.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                xscrollbarAdjustmentValueChanged(evt);
            }
        });

        getContentPane().add(xscrollbar, java.awt.BorderLayout.SOUTH);

        pack();
    }//GEN-END:initComponents
    
    private void overviewpanelAncestorResized(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_overviewpanelAncestorResized
        resize();
        repaint();
    }//GEN-LAST:event_overviewpanelAncestorResized
    
    private void xscrollbarAdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_xscrollbarAdjustmentValueChanged
        repaint();
    }//GEN-LAST:event_xscrollbarAdjustmentValueChanged
    
    private void overviewpanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_overviewpanelMouseDragged
        // Add your handling code here:
        draw2.selectednum=(int)((evt.getX()-(draw2.xoffset))/draw2.unitwidth);
        if(draw2.selectednum<0){
            draw2.selectednum=0;
        }else if(draw2.selectednum>=seqlength){
            draw2.selectednum=seqlength-1;
        }
        xscrollbar.setValue(draw2.selectednum);
        parent.blastselectseqs = hitsvec.elementAt(draw2.selectednum);
        repaint();
        parent.repaint();        
    }//GEN-LAST:event_overviewpanelMouseDragged
    
    private void overviewpanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_overviewpanelMousePressed
        // if the mouse is pressed I want to draw the selectedcolumn at the curren tmouse position
        draw2.selectednum=(int)((evt.getX()-(draw2.xoffset))/draw2.unitwidth);
        xscrollbar.setValue(draw2.selectednum);
        parent.blastselectseqs = hitsvec.elementAt(draw2.selectednum);
        repaint();
        parent.repaint();
    }//GEN-LAST:event_overviewpanelMousePressed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        parent.blastselectseqs=new int[0];
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
    //public static void main(String args[]) {
    //    new viewblasthits(new javax.swing.JFrame(), true).show();
    //}
    
    ProgramWindow parent;
    HighScoringSegmentPair[] hsparr;
    int referenceseqnum;
    AminoAcidSequence referenceseq;
    String[] namearr;
    HashMap<String, Integer> nameshash;
    drawdetail draw1;
    drawoverview draw2;
    AminoAcidSequence refseq;
    int seqlength;
    char[] charseq;
    int[] hitsarr=new int[0];
    Vector<int[]> hitsvec = new Vector<int[]>();
    int maxhsps=0;
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel detailpanel;
    private javax.swing.JPanel mainpanel;
    private javax.swing.JPanel overviewpanel;
    private javax.swing.JScrollBar xscrollbar;
    // End of variables declaration//GEN-END:variables
    
    void makedata(){
        //create the data needed for drawing the overview and details
        //a character array (sequence)
        //a int array (number of hsp's for each residue)
        //a vector containing integer arrays with the hit sequence numbers
        //always add the query number as first element!
        hitsarr=new int[seqlength];
        int hspnum=hsparr.length;
        //now loop through the hsp's and remember how many hits which residue had
        int start;
        int end;
        int j;
        int[][] tmparr=new int[seqlength][hspnum+1];
        for(int i=0;i<seqlength;i++){
            hitsarr[i]=-1;
            tmparr[i][0]=referenceseqnum;
            for(j=1;j<=hspnum;j++){
                tmparr[i][j]=-1;
            }//end for j
        }//end for i
        if (hsparr.length == 0) {
            System.out.println("no blast hits found");
        }else{
            System.out.println("blast hits for "+hsparr[0].qname);
            for(int i=0;i<hspnum;i++){
                //System.out.println(hsparr[i].hname);
                start=hsparr[i].qstart;
                end=hsparr[i].qend;
                for(j=start;j<end;j++){
                    hitsarr[j]++;
                    tmparr[j][i + 1] = nameshash.get(hsparr[i].hname).intValue();
                }
            }//end for i
        }
        //now copy the tmparr data into sth. more easily usable
        int[] posarr;
        int counter;
        for(int i=0;i<seqlength;i++){
            counter=0;
            for(j=0;j<=hspnum;j++){
                if(tmparr[i][j]!=-1){
                    counter++;
                }
            }//end for j
            if(counter>maxhsps){
                maxhsps=counter;
            }
            posarr=new int[counter];
            counter=0;
            for(j=0;j<=hspnum;j++){
                if(tmparr[i][j]!=-1){
                    posarr[counter]=tmparr[i][j];
                    counter++;
                }
            }//end for j
            hitsvec.addElement(posarr);
        }//end for i
    }//end makedata
    
    //--------------------------------------------------------------------------
    
    void resize(){
        int drawwidth=overviewpanel.getWidth()-2*draw1.xoffset;
        int drawheight=overviewpanel.getHeight()-2*draw1.yoffset;
        draw1.drawwidth=drawwidth;
        draw1.drawheight=drawheight;
        draw2.drawwidth=drawwidth;
        draw2.drawheight=drawheight;
        draw1.drawelements=draw1.drawwidth/draw1.fontwidth;
        xscrollbar.setVisibleAmount(0);
        xscrollbar.setMaximum(seqlength);
        if(xscrollbar.getValue()>seqlength-draw1.drawelements){
            xscrollbar.setValue(seqlength-draw1.drawelements);
        }
        xscrollbar.setVisibleAmount(draw1.drawelements);
     }//end resize
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    class drawoverview extends javax.swing.JPanel{
        
        /**
         * 
         */
        private static final long serialVersionUID = -2946625860881426993L;

        public drawoverview(){
            this.setBackground(Color.white);
        }
        
        int drawwidth;
        int drawheight;
        float unitwidth;
        int drawunitwidth;
        float unitheight;
        int xoffset=2;
        int yoffset=2;
        int seqheight=5;
        int selectednum=0;
        Color selectedcolor=new Color(1,0,0,0.4f);
        
        public void paintComponent(java.awt.Graphics g){
            super.paintComponent(g);
            //draw an overview of the complete sequence
            drawwidth=overviewpanel.getWidth()-2*xoffset;
            drawheight=overviewpanel.getHeight()-2*yoffset;
            unitwidth=((float)drawwidth/(float)seqlength);
            unitheight=(float)drawheight/(float)maxhsps;
            drawunitwidth=(int)unitwidth+1;
            g.setColor(Color.lightGray);
            for(int i=0;i<seqlength;i++){
                g.fillRect((int)(i*unitwidth)+xoffset, (int)(drawheight-yoffset-(hitsarr[i]*unitheight)), drawunitwidth, (int)(hitsarr[i]*unitheight));
            }//end for i
            g.fillRect(xoffset,drawheight-yoffset-1,drawwidth,yoffset+1);
            g.setColor(selectedcolor);
            g.fillRect((int)(selectednum*unitwidth)+xoffset, yoffset, drawunitwidth, drawheight);
        }//end paintcomponent
        
    }//end class drawme
    
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    
    class drawdetail extends javax.swing.JPanel{
        
        /**
         * 
         */
        private static final long serialVersionUID = 7997168794734721075L;

        public drawdetail(){
            this.setBackground(Color.white);
        }
        
        int drawwidth;
        int drawheight;
        int fontsize=10;
        int fontwidth=fontsize;
        Font myfont=new java.awt.Font("Monospace",Font.PLAIN, fontsize);
        float unitwidth;
        float unitheight;
        int xoffset=2;
        int yoffset=2;
        int seqheight=0;
        int selectednum=0;
        int drawelements=1;
        int drawstart=0;
        Color selectedcolor=new Color(1,0,0,0.4f);
        
        public void paintComponent(java.awt.Graphics g){
            super.paintComponent(g);
            fontwidth=myfont.getSize();
            drawwidth=detailpanel.getWidth()-2*xoffset;
            drawheight=detailpanel.getHeight()-2*yoffset-fontsize;
            drawelements=drawwidth/fontwidth;
            drawstart=xscrollbar.getValue();
            unitheight=(float)drawheight/(float)maxhsps;
            for(int i=0;(i<drawelements)&&((i+drawstart)<seqlength);i++){
                g.setColor(Color.black);
                g.drawString(String.valueOf(charseq[drawstart+i]),(int)(i*fontwidth)+xoffset, drawheight-yoffset);
                g.setColor(Color.lightGray);
                g.fillRect((int)(i*fontwidth)+xoffset, (int)(drawheight-yoffset-fontsize-(unitheight*hitsarr[drawstart+i])), fontwidth, (int)(unitheight*hitsarr[drawstart+i]));
            }//end for i
            g.setColor(Color.lightGray);
            g.fillRect(xoffset,drawheight-2*yoffset-fontsize-1,drawwidth,yoffset+1);
            g.setColor(selectedcolor);
            g.fillRect((int)((draw2.selectednum-drawstart)*fontwidth), yoffset, fontwidth, drawheight);
        }//end paintcomponent
        
    }//end class drawme
    
}//end class
