package core.fpGrowth.itemSetUtil;

import java.util.ArrayList;

/**
 * Class used to managed item on fpGrowth algorithm
 * Created by klamblot on 22/04/2016.
 */
public class Item {
    public ArrayList<String> getItemSet() {
        return itemSet;
    }

    private ArrayList<String> itemSet;
    private int nbOccur;

    public Item(ArrayList<String> itemSet, int first){
        this.itemSet = itemSet;
        nbOccur = first;
    }

    public void addNbOccurs(int sup){
        nbOccur+=sup;
    }

    public void addStringToItem(String string){
        itemSet.add(string);
    }
}
