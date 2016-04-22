package core.fpGrowth.itemSetUtil;

import java.util.ArrayList;

/**
 * Class used to managed item on FPGrowth algorithm
 * Created by klamblot on 22/04/2016.
 */
public class Item {
    public ArrayList<String> itemSet;
    public int nbOccur;

    public Item(ArrayList<String> itemSet, int first){
        this.itemSet = itemSet;
        nbOccur = first;
    }

    public void addNbOccurs(int sup){
        nbOccur+=sup;
    }
}
