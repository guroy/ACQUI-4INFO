package core.fpGrowth;

import core.ERAAlgorithm;
import core.fpGrowth.itemSetUtil.Item;
import core.fpGrowth.itemSetUtil.KItemSetFPG;

import java.util.*;

public class FPGrowth implements ERAAlgorithm {

    private double support;
    private int minFrequency;
    private List<KItemSetFPG> kItemSetFPGList = new ArrayList<>();

    public FPGrowth(double support){
        this.support = support;
    }


	/**
	 * process the calculus of frequent Item-set
	 * @param mots list of the words we wanted to match
	 * @param map [x][y] if the words in mots[y] is in the article x
     */
	public void process(List<String> mots, boolean[][] map){

		// tab for the frequency
		int[] itemSetValue = new int[mots.size()];

        KItemSetFPG first = new KItemSetFPG(1);
        kItemSetFPGList.add(first);

		for(int i = 0; i< itemSetValue.length; i++){
			itemSetValue[i] = 0;
		}
		
		// minimum frequency = 0
		minFrequency = (int) (support * map.length) ;

		// Step 1 : determined frequency
        for (boolean[] aMap1 : map) {
            for (int j = 0; j < aMap1.length; j++) {
                if (aMap1[j]) {
                    (itemSetValue[j])++;
                }
            }
        }

        System.out.println("step 1 done");

		// Step 2 : conserved item with support desired
		// HashMap contains the <words, <support,index>>
        // index in the map
		HashMap<String, Pair<Integer,Integer>> headerTable = new HashMap<>();

		for(int i = 0 ; i < itemSetValue.length ; i++){
			if(itemSetValue[i] > minFrequency){
				headerTable.put(mots.get(i), new Pair<>(itemSetValue[i], i));

                ArrayList<String> oneWord = new ArrayList<>();
                oneWord.add(mots.get(i));

                Item current = new Item(oneWord, itemSetValue[i]);
                first.addItem(current);
			}
		}

        // list of word in order and tab of there frequency
        ArrayList<String> orderedWords = new ArrayList<>(headerTable.size());
        Integer[] orderedInt = new Integer[headerTable.size()];
        for(int i = 0; i < headerTable.size() ; i++){
            orderedWords.add("");
            orderedInt[i] = 0;
        }

        System.out.println("step 2 done");

		// Step 3 : sorted item by frequency in orderedWords and OrderedInt
		headerTable.forEach( (k,v) -> {
            int current = 0;
            while( v.getLeft() < orderedInt[current]) current++;
            for (int i = orderedInt.length - 1; i > current ; i--){
                orderedInt[i] = orderedInt[i-1];
                orderedWords.set(i, orderedWords.get(i - 1));
            }
            orderedInt[current] = v.getLeft();
            orderedWords.set(current, k);
        });

        System.out.println("step 3 done");

		// Step 4 : construction of the FP-Tree and the item-tree link
        HashMap<String, List<Node>> itemToTree = new HashMap<>();
		FPTree tree = new FPTree();
		Node root = tree.getRoot();
        Node current = root;

        // add elements to the tree
        // for each article
		for (boolean[] aMap : map) {
            // for each word
            for (String orderedWord : orderedWords) {
                if (aMap[headerTable.get(orderedWord).getRight()]) {
                    if (current.containDirectSon(orderedWord)) {
                        current = current.takeDirectSun(orderedWord);
                        current.getValue().setRight(current.getValue().getRight() + 1);
                    } else {
                        current.addSun(orderedWord);
                        current = current.takeDirectSun(orderedWord);
                        if (itemToTree.containsKey(orderedWord)) {
                            itemToTree.get(orderedWord).add(current);
                        } else {
                            itemToTree.put(orderedWord, new ArrayList<>());
                            itemToTree.get(orderedWord).add(current);
                        }
                    }
                }
            }
            current = root;
		}

        System.out.println("step 4 done");

        // Calculus of the frequent item-set
        frequentItemSet(tree, orderedWords, itemToTree);

        // print all k-item set
        showKItemSet();
	}

    // method used to get the frequent item-set
    private void frequentItemSet(FPTree tree, ArrayList<String> orderedWords, Map<String, List<Node>> itemToTree){

        boolean noMoreKItemSet = false;
        int k = 2;

        KItemSetFPG old = new KItemSetFPG(k);
        kItemSetFPGList.add(old);

        // creation of the 2-ItemSet
        for (int i = orderedWords.size() - 1 ; i > 0 ; i--  ) {
            for (int j = i - 1; j >= 0; j--) {
                int counter = 0;
                for (Node node : itemToTree.get(orderedWords.get(i))){
                    if(node.hasParent(orderedWords.get(j))) counter+=node.getValue().getRight();
                }
                if(counter != 0 && counter > minFrequency){
                    Item toAdd = new Item(new ArrayList<>(), counter);
                    toAdd.addStringToItem(orderedWords.get(i));
                    toAdd.addStringToItem(orderedWords.get(j));
                    old.addItem(toAdd);
                }

            }
        }

        System.out.println("2-itemSet done");

        // creation of the k-ItemSet from (k-1)ItemSet
        while(!noMoreKItemSet){
            k++;
            KItemSetFPG kItemCurrent = new KItemSetFPG(k);

            // make the calculus of k+1 item-set from old
            // for each item already present
            for(Item item : old.getkItemSet()){
                int first = orderedWords.size() + 1;
                int last = -1;

                for(String current : item.getItemSet()){
                    if(orderedWords.indexOf(current) > last) last = orderedWords.indexOf(current);
                    if(orderedWords.indexOf(current) < first) first = orderedWords.indexOf(current);
                }

                // The process
                // for each words not in the current set
                for (int i=0 ; i < first; i++){

                    int counter = 0;
                    boolean somethingFailed = false;
                    for (Node node : itemToTree.get(orderedWords.get(last))){
                        if (node.hasParent(orderedWords.get(i))){
                            for (String element : item.getItemSet()){
                                if (!node.hasParent(element) && !node.getValue().getLeft().equals(orderedWords.get(last))){
                                    somethingFailed = true;
                                }
                            }
                            if(!somethingFailed){
                                counter += node.getValue().getRight();
                            }
                        }else{
                            somethingFailed = true;
                        }
                    }
                    if (counter >= minFrequency){
                        Item newItem = new Item(new ArrayList<>(), counter);
                        for(String string : item.getItemSet()){
                            newItem.addStringToItem(string);
                        }
                        newItem.addStringToItem(orderedWords.get(i));
                        kItemCurrent.addItem(newItem);
                    }
                }
            }

            old = kItemCurrent;

            if (kItemCurrent.getkItemSet().size() != 0){
                kItemSetFPGList.add(kItemCurrent);
                System.out.println(k+"-itemSet done");
            }else{
                noMoreKItemSet = true;
            }
        }

    }

    private void showKItemSet(){
        for(KItemSetFPG kItemSetFPG : kItemSetFPGList){
            String string = "";
            string += kItemSetFPG.getK()+"-item-set : ";
            for (Item item : kItemSetFPG.getkItemSet()){
                for(String it : item.getItemSet()){
                    string += it+" , ";
                }
                string += item.getNbOccur()+" ; ";
            }
            System.out.println(string);
        }
    }
}
