package core.fpg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import core.ERAAlgorithm;
import core.fpg.itemSetUtil.Item;
import core.fpg.itemSetUtil.KItemSetFPG;


/**
 * Class FP-Growth, used to managed FP-Growth algorithm
 */
public class FPGrowth implements ERAAlgorithm {

    // instance variables
    private double support;
    private double confidence;
    private int minFrequency;

    // List of KItemSetFPG used to stock itemSets
    private List<KItemSetFPG> kItemSetFPGList = new ArrayList<>();

    // List of AssociationRules
    private ArrayList<AssociationRules> ar = new ArrayList<>();

    // output path to save data after the process
    private String output;


    // constructor
    public FPGrowth(String output, double support, double confidence){
        this.support = support;
        this.confidence = confidence;
        this.output = output;
    }


	/**
	 * process the calculus of frequent Item-set
	 * @param mots list of the words we wanted to match
	 * @param map [x][y] if the words in mots[y] is in the article x
     */
	public void process(List<String> mots, boolean[][] map){

		// tab for the frequency
		int[] itemSetValue = new int[mots.size()];

        // creation of a first itemSet to save 1-itemSets
        KItemSetFPG first = new KItemSetFPG(1);
        kItemSetFPGList.add(first);

        // initialisation of the tab
		for(int i = 0; i< itemSetValue.length; i++){
			itemSetValue[i] = 0;
		}
		
		// calculus minimum frequency
        // will be used to determined if we accept itemSets or not
		minFrequency = (int) (support * map.length) ;

		// Step 1 : determined frequency of each words
        for (boolean[] aMap1 : map) {
            for (int j = 0; j < aMap1.length; j++) {
                if (aMap1[j]) {
                    (itemSetValue[j])++;
                }
            }
        }

		// Step 2 : conserved item with support desired
		// HashMap contains the <words, <support,index>>
        // index in the map
		HashMap<String, Pair<Integer,Integer>> headerTable = new HashMap<>();

        // for each words
		for(int i = 0 ; i < itemSetValue.length ; i++){
            // we check they are enough occur to accept the word
			if(itemSetValue[i] > minFrequency){
                // we put the word in the header table
				headerTable.put(mots.get(i), new Pair<>(itemSetValue[i], i));

                // we construct a list of one item, inefficient for one word
                // but it's the data structure choose for all itemSets
                ArrayList<String> oneWord = new ArrayList<>();
                oneWord.add(mots.get(i));

                // we create the item for the current words and add-it to the 1-itemSet List
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

		// Step 4 : construction of the FP-Tree and the item-tree link
        // itemToTree will be the link between a word and all of the nodes which contains the word
        HashMap<String, List<Node>> itemToTree = new HashMap<>();

        // FP-Tree creation, root of the tree and the set of a current on root to begin
		FPTree tree = new FPTree();
		Node root = tree.getRoot();
        Node current = root;

        // add elements to the tree
        // for each article
		for (boolean[] aMap : map) {
            // for each word
            for (String orderedWord : orderedWords) {
                // if the words appears in the article
                if (aMap[headerTable.get(orderedWord).getRight()]) {
                    // and if the current node has a sun node for the current word
                    if (current.containDirectSon(orderedWord)) {
                        // We increment the value of the sun node
                        current = current.takeDirectSun(orderedWord);
                        current.getValue().setRight(current.getValue().getRight() + 1);
                    } else {
                        // We created a new node
                        current.addSun(orderedWord);
                        current = current.takeDirectSun(orderedWord);
                        if (itemToTree.containsKey(orderedWord)) {
                            // and added the link to the words in itemToTree if the words is already present
                            itemToTree.get(orderedWord).add(current);
                        } else {
                            // or we added the word and the link into itemToTree
                            itemToTree.put(orderedWord, new ArrayList<>());
                            itemToTree.get(orderedWord).add(current);
                        }
                    }
                }
            }
            // after a article is on the tree, we return to the root node
            // and redo the process with the following article
            current = root;
		}

        // Calculus of the frequent item-set
        frequentItemSet(orderedWords, itemToTree);

        // call to the method which search for assocaition rules
        findAssociation();

        // print all k-item set
        // showKItemSet();
        // showAssociationRules();
        writeInFile();
	}

    /**
     * This method process the calculus of the k-itemSets
     * @param orderedWords
     * List of the words sorted by frequency
     * @param itemToTree
     * Link between each words and there occurs in the tree
     */
    private void frequentItemSet(ArrayList<String> orderedWords, Map<String, List<Node>> itemToTree){

        // boolean used to know if we have to continue to process
        boolean noMoreKItemSet = false;

        // current k-itemSet generating
        int k = 2;

        // new itemSets collection, named old because it will be the old for the generation of the (k+1)-itemSet
        KItemSetFPG old = new KItemSetFPG(k);
        kItemSetFPGList.add(old);

        // creation of the 2-ItemSet
        // for each words from the last to the first
        for (int i = orderedWords.size() - 1 ; i > 0 ; i--  ) {
            // for each words from the current to the first
            for (int j = i - 1; j >= 0; j--) {
                // frequency counter
                int counter = 0;
                // for each node for the current words we search if the precedent words is a parent
                for (Node node : itemToTree.get(orderedWords.get(i))){
                    if(node.hasParent(orderedWords.get(j))) counter+=node.getValue().getRight();
                }
                // and if the counter is above the min frequency, we add the 2-itemSet generated
                if(counter > minFrequency){
                    Item toAdd = new Item(new ArrayList<>(), counter);
                    toAdd.addStringToItem(orderedWords.get(i));
                    toAdd.addStringToItem(orderedWords.get(j));
                    old.addItem(toAdd);
                }

            }
        }

        // creation of the k-ItemSet from (k-1)ItemSet
        while(!noMoreKItemSet){
            k++;
            KItemSetFPG kItemCurrent = new KItemSetFPG(k);

            // make the calculus of k+1 item-set from old
            // for each item already present
            for(Item item : old.getkItemSet()){
                int first = orderedWords.size() + 1;
                int last = -1;

                // we take the most frequent words and the less present
                for(String current : item.getItemSet()){
                    if(orderedWords.indexOf(current) > last) last = orderedWords.indexOf(current);
                    if(orderedWords.indexOf(current) < first) first = orderedWords.indexOf(current);
                }

                // The process
                // complex

                // we take all the words parents to the first
                Set<String> parentWords = new HashSet<>();
                for (Node node : itemToTree.get(orderedWords.get(first))){
                    node.getAllAscendant(parentWords);
                }

                // and for each of those words, we try to create a new ItemSet from the current item and the added words
                // PS : je pense qu'il y'a mieux pour choisir les mots candidats à un ajout dans l'itemSet
                // je crois qu'il serait même possible de générer directement les itemsets sans passer
                // par l'étape listage en parcourrant l'arbre et en calculant en fesant
                // un passage par la table des liens, mais pas eu le temps de tester
                // une implémentation
                for (String toSearch : parentWords){

                    // counter of the frequency of the new itemSet
                    int counter = 0;

                    boolean somethingFailed = false;

                    // for each node associate to the less frequent words of the probable new item
                    for (Node node : itemToTree.get(orderedWords.get(last))){

                        // we search if the candidate word has a node which is a parent to the current node
                        if (node.hasParent(toSearch)){

                            // and if it's okay, we test each words from the base item
                            for (String element : item.getItemSet()){
                                if (!node.hasParent(element) && !node.getValue().getLeft().equals(element)){
                                    somethingFailed = true;
                                }
                            }
                            // if all is okay, we added the value in the tree to the counter
                            if(!somethingFailed){
                                counter += node.getValue().getRight();
                            }
                        }
                        somethingFailed = false;
                    }
                    // we added the new itemSets if the frequency is above the minFrequency
                    if (counter >= minFrequency){
                        Item newItem = new Item(new ArrayList<>(), counter);
                        for(String string : item.getItemSet()){
                            newItem.addStringToItem(string);
                        }
                        newItem.addStringToItem(toSearch);
                        kItemCurrent.addItem(newItem);
                    }
                }
            }

            // the current list of itemSet became the old one and he is added to the list of k-itemSets collection
            old = kItemCurrent;

            if (kItemCurrent.getkItemSet().size() != 0){
                kItemSetFPGList.add(kItemCurrent);
                System.out.println(k+"-itemSet done");
            }else{
                noMoreKItemSet = true;
            }
        }

    }

    // Method to find association, post process
    private void findAssociation(){

        // we do the process k-1 times because no association with 1-itemSets
        for(int i = 0; i<kItemSetFPGList.size()-1; i++){
            // we set the base itemSets collection and the itemSets collection we search on
            KItemSetFPG base = kItemSetFPGList.get(i);
            KItemSetFPG onSearch = kItemSetFPGList.get(i+1);

            // for each base itemSet
            for (Item itemBase : base.getkItemSet()){
                // for each on search itemSet
                for (Item itemOnSearch : onSearch.getkItemSet()){
                    // if all item from the base is on the itemSet we search on
                    // and if the confidence is above a threshold we choose
                    // a new rule is generated
                    if(itemOnSearch.getItemSet().containsAll(itemBase.getItemSet()) && itemOnSearch.getNbOccur()/(double)itemBase.getNbOccur() > confidence){
                        ArrayList<String> arrayList = new ArrayList(itemOnSearch.getItemSet());
                        arrayList.removeAll(itemBase.getItemSet());
                        ar.add(new AssociationRules(itemBase.getItemSet(), itemBase.getNbOccur(), arrayList.get(0),itemOnSearch.getNbOccur()));
                    }
                }

            }
        }
    }

    // debug method
    private void showKItemSet(){
        for(KItemSetFPG kItemSetFPG : kItemSetFPGList){
            String string = "";
            System.out.println();
            string += kItemSetFPG.getK()+"-item-set : ";
            System.out.println(string);
            for (Item item : kItemSetFPG.getkItemSet()){
                string = "";
                for(String it : item.getItemSet()){
                    string += it+" , ";
                }
                string += item.getNbOccur()+" ; ";
                System.out.println(string);
            }
        }
    }

    // debug method
    private void showAssociationRules(){
        for (AssociationRules a : ar){
            System.out.println(a.toString());
        }
    }

    // method to save data generated in a file
    private void writeInFile(){
        FileWriter fstream = null;
        BufferedWriter out;

        // create your filewriter and bufferedreader
        try {
            fstream = new FileWriter(output);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        out = new BufferedWriter(fstream);

        try {
            out.write("ItemSets : \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(KItemSetFPG kItemSetFPG : kItemSetFPGList){
            String string = "\n";
            try {
                out.write(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
            string += kItemSetFPG.getK()+"-item-set : \n";
            try {
                out.write(string);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (Item item : kItemSetFPG.getkItemSet()){
                string = "";
                for(String it : item.getItemSet()){
                    string += it+" , ";
                }
                string += item.getNbOccur()+" \n";
                try {
                    out.write(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            out.write("\nAssociation rules : \n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (AssociationRules a : ar){
            try {
                out.write(a.toString()+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
