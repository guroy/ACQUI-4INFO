package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Apriori implements ERAAlgorithm {
	private Map<ArrayList<String>, Integer> result = new HashMap<ArrayList<String>, Integer>();

	public void process(List<String> mots, boolean[][] map) {
		Map<ArrayList<String>, ArrayList<Integer>> next = new HashMap<ArrayList<String>,  ArrayList<Integer>>();

		int index =0;
		for (String mot : mots) {
			ArrayList<String> nextMots = new ArrayList<String>();
			ArrayList<Integer> nextLines = new ArrayList<Integer>();
			nextMots.add(mot);
			int count = 0;
			
			//on compte combien de fois apparais le mot et m�morise o�
			for (int i = 0; i < map.length; i++) {
				if (map[i][index]) {
					count++;
					nextLines.add(i);
				}
			}
			
			//on stock le 1-itemset
			result.put(nextMots, count);
			next.put(nextMots, nextLines);
			index++;
		}

		

		L(mots, map, next);
		System.out.println(result);
	}

	// current = item de l'étape précédantes, length taille des trucs de l'étape
	// précédante
	private void L(List<String> mots, boolean[][] map, Map<ArrayList<String>, ArrayList<Integer>> current) {

		//Va stocker les ensembles ajoutés à cette étape
		Map<ArrayList<String>, ArrayList<Integer>> next = new HashMap<ArrayList<String>,  ArrayList<Integer>>();

		//On r�cup�re toutes les clefs pour it�r� dessus
		List<ArrayList<String>> keys = new ArrayList<ArrayList<String>>(current.keySet());

			
		//Pour tout les ensembles de l'étape précédante
		for (int i = 0; i < keys.size(); i++) {
			ArrayList<String> a = keys.get(i);

			
			//On compare à tout les autres ensembles
			for (int j = i + 1; j < keys.size(); j++) {
				ArrayList<String> b = keys.get(j);
				
				ArrayList<String> temp = new ArrayList<String>();

				for (String s : a) {
					temp.add(s);
				}
				temp.removeAll(b);
				if(temp.size()==1){
					temp.addAll(b);
				}				
				java.util.Collections.sort(temp);
				if (!result.containsKey(temp)) {
					int res = 0;

				
					//Intersection des lignes o�les deux item-set de d�parts apparaissent ensembles
					ArrayList<Integer> nextLines = intersection(current.get(a),current.get(b));

					
					res = nextLines.size();
					
					//S'ils apparraissent au moins une fois et assez fréquent, on les ajoutent
					if(res > 0){
						double support = (((double)res)/map.length)*100;
						if(support>0.9){
							next.put(temp,nextLines);
							result.put(temp, res);
					
							
						}
					}
				}
			}

		}
		
		//S'il y as eu au moins deux ensembles on passe à la suite
		if(next.size() > 1){
			L(mots, map, next);
		}

	}
	
    public static ArrayList<Integer> intersection(ArrayList<Integer> list1, ArrayList<Integer> list2) {
    	ArrayList<Integer> list = new ArrayList<Integer>();

        for (Integer t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

}
