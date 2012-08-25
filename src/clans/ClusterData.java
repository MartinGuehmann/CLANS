package clans;

/**
 *
 * @author tancred
 */
public class ClusterData {

    //variables initialized on creation
    minhsp[] blasthits=null;
    AminoAcidSequence[] sequences=null;
    String[] namearr=null;
    java.util.HashMap nameshash=null;
    double eval=-1;
    double pval=-1;
    float scval=-1;
    int verbose=-1;
    int cpu=-1;
    boolean savepos=false;
    String cmd=null;
    String blastpath=null;
    boolean addblastvbparam=false;
    String formatdbpath=null;
    String[] referencedb=null;
    StringBuffer errbuff=null;
    String input_filename=null;

    //variables initialized later on
    boolean nographics=false;
    boolean complexatt=true;
    int seqnum=0;
    float[] seqlengths=null;
    getmovethread[] movethreads=null;
    boolean usescval=false;
    minattvals[] myattvals=null;
    
    //variables I use as part of the clustering
    int rounds=0;
    float[][] myposarr=null;
    float[][] posarr=null;
    boolean cluster2d=false;
    float maxmove=0.1f;
    double minpval=1;
    double mineval=1;
    float hidebelow=0;
    float hidebelowold=0;
    double cooling=1;
    double currcool=1;
    saverunobject saveddata=null;
    float attfactor=10f;
    float repfactor=0.5f;
    int attvalpow=1;
    int repvalpow=1;
    float dampening=0.2f;
    double minattract=1;
    float[] weights=null;
    java.util.ArrayList<String> mapfiles=null;
    java.util.ArrayList<String> lookupfiles=null;
    java.util.Vector<String> affyfiles=null;
    boolean usefoldchange=false;
    boolean avgfoldchange=false;
    String namesdmp_file="not_spcified";
    String nodesdmp_file="not_specified";
    float zoomfactor=1;
    boolean showinfo=true;
    int[] selectednames=new int[0];
    int[] selectnames=new int[0];
    int selnamenum=0;
    float[][] lastmovearr=null;
    float[][] mymovearr=null;
    float[][] posarrtmp=null;
    int[][] drawarrtmp=null;
    java.util.ArrayList[] draworder=null;
    static int dimensions=3;
    int elements=-1;
    double[][] rotmtx={{1,0,0},{0,1,0},{0,0,1}};//the performed rotations
    double[][] myrotmtx={{1,0,0},{0,1,0},{0,0,1}};//new double[3][3];//both of the above together
    minattvals[] orgattvals=null;
    boolean attvalsimple=false;
    boolean rescalepvalues=false;
    double maxvalfound=0;
    float p2attfactor=1;
    float p2attoffset=0;
    int ovalsize=4;
    int dotsize=2;
    int groupsize=4;
    java.util.Vector seqgroupsvec=new java.util.Vector();
    java.util.ArrayList <int[][]>polygons=null;
    boolean showseqgroups=false;
    boolean changedvals=false;
    java.awt.Color[] colorarr=null;
    float[] colorcutoffs=null;
    int roundsdone=0;
    int roundslimit=-1;
    boolean moveselectedonly=false;

    public ClusterData(minhsp[] blasthits,AminoAcidSequence[] inaln, String[] namearr,java.util.HashMap nameshash,double eval,double pval,float scval,int verbose,int cpu,boolean savepos, String cmd, String blastpath,boolean addblastvbparam, String formatdbpath,String[] referencedb,StringBuffer errbuff,String loadsaved) {
        this.blasthits=blasthits;
        this.sequences=ClusterMethods.remove_gaps_from_sequences(inaln);
        this.namearr=namearr;
        this.nameshash=nameshash;
        this.eval=eval;
        this.pval=pval;
        this.scval=scval;
        this.verbose=verbose;
        this.cpu=cpu;
        this.savepos=savepos;
        this.cmd=cmd;
        this.blastpath=blastpath;
        this.addblastvbparam=addblastvbparam;
        this.formatdbpath=formatdbpath;
        this.referencedb=referencedb;
        this.errbuff=errbuff;
        this.input_filename=loadsaved;
        this.seqnum=java.lang.reflect.Array.getLength(namearr);
        this.movethreads=new getmovethread[cpu];
    }
    
    public void save_to_file(java.io.File output_file){
    	saverunobject myrun=new saverunobject();
        myrun.file=output_file;
        myrun.inaln=sequences;
        myrun.blasthits=blasthits;
        myrun.attvals=myattvals;
        myrun.posarr=myposarr;
        myrun.maxmove=maxmove;
        myrun.pval=minpval;
        myrun.usescval=usescval;
        if(attvalsimple){
            myrun.complexatt=false;
        }else{
            myrun.complexatt=true;
        }
        myrun.rotmtx=rotmtx;
        myrun.seqgroupsvec=seqgroupsvec;
        myrun.cooling=cooling;
        myrun.currcool=currcool;
        myrun.attfactor=attfactor;
        myrun.attvalpow=attvalpow;
        myrun.repfactor=repfactor;
        myrun.repvalpow=repvalpow;
        myrun.dampening=dampening;
        myrun.minattract=minattract;
        myrun.blastpath=blastpath;
        myrun.formatdbpath=formatdbpath;
        myrun.dotsize=dotsize;
        myrun.ovalsize=ovalsize;
        myrun.groupsize=groupsize;
        myrun.mapfiles=mapfiles;
        myrun.lookupfiles=lookupfiles;
        myrun.usefoldchange=usefoldchange;
        myrun.avgfoldchange=avgfoldchange;
        myrun.affyfiles=affyfiles;
        myrun.namesdmp_file=namesdmp_file;
        myrun.nodesdmp_file=nodesdmp_file;
        if(cluster2d){
            myrun.cluster2d=true;
        }else{
            myrun.cluster2d=false;
        }
        myrun.colorarr=colorarr;
        myrun.colorcutoffs=colorcutoffs;
        
        myrun.rounds = rounds;
        
        CustomUtils.saverun(myrun, namearr, nographics);
        myrun=null;
    }
    
}