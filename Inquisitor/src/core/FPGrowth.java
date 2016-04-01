package core;

import java.util.HashMap;
import java.util.List;

public class FPGrowth implements ERAAlgorithm{

	public void process(List<String> mots, boolean[][] map){
		
		// boolean used to know if all the itemset are done
		boolean finish = false;
		
		// tab for the frequence
		int[] itemSetValue = new int[mots.size()];
		
		// minimum frequence = 0.25
		int minsup = map.length / 4;
		
		// nombre de 1-itemset frequent
		int count = 0;
		
		for(int i = 0; i < mots.size() ; i++){
			for(int j = 0; j < map[i].length; j++){
				if(map[i][j]){
					(itemSetValue[i])++;
				}
			}
		}
		
		// Step 2
		HashMap<Integer,Integer> headerTable = new HashMap<Integer, Integer>();
		
		for(int i = 0 ; itemSetValue.length < 0 ; i++){
			if(itemSetValue[i] > minsup){
				headerTable.put(i, itemSetValue[i]);
			}
		}
		
	}
}
