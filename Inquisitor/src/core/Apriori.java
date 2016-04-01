package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Apriori implements ERAAlgorithm {
	private Map<ArrayList<String>, Integer> result = new HashMap<ArrayList<String>, Integer>();

	public void process(List<String> mots, boolean[][] map) {
		ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();

		int index = 0;
		for (String mot : mots) {
			array.add(new ArrayList<String>());
			array.get(index).add(mot);
			result.put(array.get(index), 0);
		}

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j]) {
					int value = result.get(array.get(index));
					result.put(array.get(index), value + 1);
				}
			}
		}

		L(mots, map, array, 1);
	}

	
	// current = item de l'étape précédantes, length taille des trucs de l'étape précédante
	private Map<ArrayList<String>, Integer> L(List<String> mots,
			boolean[][] map, ArrayList<ArrayList<String>> current, int length) {
			
		
		
		return null;

	}

}
