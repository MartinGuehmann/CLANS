/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package clans.model.proteins;

/**
 *
 * @author tancred
 */
public class HighScoringSegmentPair {
/** Creates a new instance of hsp */
    public HighScoringSegmentPair() {
        qname=new String("");
        qseq=new String("");
        hname=new String("");
        hseq=new String("");
        qstart=-1;
        qend=-1;
        hstart=-1;
        hend=-1;
        value=-1;
        
    }
    public String qname;
    public String qseq;
    public String hname;
    public String hseq;
    public int qstart;
    public int qend;
    public int hstart;
    public int hend;
    public double value;
    
}
