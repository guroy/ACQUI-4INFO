package core.fpg;

import java.util.ArrayList;

/**
 * Created by klamblot on 11/05/2016.
 * class to conserve association rules
 */
public class AssociationRules {
    private ArrayList<String> set;
    private int setFreq;
    private String implie;
    private int implieFreq;
    private double confidence;

    AssociationRules(ArrayList newSet, int newSetFreq, String newImplie, int newImplieFreq){
        set = newSet;
        setFreq = newSetFreq;
        implie = newImplie;
        implieFreq = newImplieFreq;
        confidence = implieFreq/(double)setFreq;
    }

    // unique methods to string for create a super nice string which will be used in the generation of th result file
    public String toString(){
        return set+" : "+setFreq+" ==> "+implie+" : "+implieFreq+" <conf : "+confidence+">";
    }
}
