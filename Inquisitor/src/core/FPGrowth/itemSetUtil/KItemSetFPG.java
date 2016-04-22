package core.fpGrowth.itemSetUtil;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used to managed a k-itemset
 * Created by klamblot on 22/04/2016.
 */
public class KItemSetFPG {
    private int k;
    private ArrayList<Item> kItemSet;

    public KItemSetFPG(int k){
        this.k = k;
        kItemSet = new ArrayList<>();
    }

    public void addItem(Item item){
        kItemSet.add(item);
    }

    public void incrementItem(Item item){
        kItemSet.indexOf(item);
    }
}
