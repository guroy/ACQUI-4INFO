package core.FPGrowth;

import core.ERAAlgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FPGrowth implements ERAAlgorithm {

	/**
	 * process the calculus of frequent Item-set
	 * @param mots list of the words we wanted to match
	 * @param map [x][y] if the words in mots[y] is in the article x
     */
	public void process(List<String> mots, boolean[][] map){

		// tab for the frequency
		int[] itemSetValue = new int[mots.size()];

		for(int i = 0; i< itemSetValue.length; i++){
			itemSetValue[i] = 0;
		}
		
		// minimum frequency = 0
		int minFrequency = map.length / 100 ;

		// Step 1 : determined frequency
		for(int i = 0; i < map.length ; i++){
			for(int j = 0; j < map[i].length; j++){
				if(map[i][j]){
					(itemSetValue[j])++;
				}
			}
		}


		// Step 2 : conserved item with support desired
		// HashMap contains the <words, <support,index>>
        // index in the map
		HashMap<String, Pair<Integer,Integer>> headerTable = new HashMap<>();

		for(int i = 0 ; i < itemSetValue.length ; i++){
			if(itemSetValue[i] > minFrequency){
				headerTable.put(mots.get(i), new Pair<>(itemSetValue[i], i));
			}
		}

        // list of word in order and tab of there frequency
        String[] orderedWords = new String[headerTable.size()];
        Integer[] orderedInt = new Integer[headerTable.size()];
        for(int i = 0; i < headerTable.size() ; i++){
            orderedInt[i] = 0;
        }

		// Step 3 : sorted item by frequency in orderedWords and OrderedInt
		headerTable.forEach( (k,v) -> {
            int current = 0;
            while( v.getLeft() < orderedInt[current]) current++;
            for (int i = orderedInt.length - 1; i > current ; i--){
                orderedInt[i] = orderedInt[i-1];
                orderedWords[i] = orderedWords[i-1];
            }
            orderedInt[current] = v.getLeft();
            orderedWords[current] = k;
        });


		// Step 4 : construction of the FP-Tree and the item-tree link
        HashMap<String, List<Node>> itemToTree = new HashMap<>();
		FPTree tree = new FPTree();
		Node root = tree.getRoot();
        Node current = root;
		for (boolean[] aMap : map) {
            for (String orderedWord : orderedWords) {
                if (aMap[headerTable.get(orderedWord).getRight()]) {
                    if (current.containsSon(orderedWord)) {
                        current = current.takeSun(orderedWord);
                        current.getValue().setRight(current.getValue().getRight() + 1);
                    } else {
                        current.addSun(orderedWord);
                        current = current.takeSun(orderedWord);
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
	}
}
