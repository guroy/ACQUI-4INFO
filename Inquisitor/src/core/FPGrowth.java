package core;

import java.util.HashMap;
import java.util.List;

public class FPGrowth implements ERAAlgorithm{

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
		HashMap<String,Integer> headerTable = new HashMap<>();
		
		for(int i = 0 ; i < itemSetValue.length ; i++){
			if(itemSetValue[i] > minFrequency){
				headerTable.put(mots.get(i), itemSetValue[i]);
			}
		}

        // list of word in order and tab of there frequency
        String[] orderedWords = new String[headerTable.size()];
        Integer[] orderedInt = new Integer[headerTable.size()];
        for(int i = 0; i < headerTable.size() ; i++){
            orderedInt[i] = 0;
        }

		// Step 3 : sorted item by frequency
		headerTable.forEach( (k,v) -> {
            int current = 0;
            while( v < orderedInt[current]) current++;
            for (int i = orderedInt.length - 1; i > current ; i--){
                orderedInt[i] = orderedInt[i-1];
                orderedWords[i] = orderedWords[i-1];
            }
            orderedInt[current] = v;
            orderedWords[current] = k;
        });

        showThreeStep(orderedWords,orderedInt);

	}

    public void showThreeStep(String[] k, Integer[] v){
        for (int i =0;i<k.length;i++) {
            System.out.println("couple (mots, frequence) : (" + k[i] + ", " + v[i] + ")");
        }
    }
}
