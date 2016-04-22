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
			index++;
		}

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				if (map[i][j]) {
					int value = result.get(array.get(j));
					result.put(array.get(j), value + 1);

				}
			}
		}

		L(mots, map, array);
		System.out.println(result);
	}

	// current = item de l'étape précédantes, length taille des trucs de l'étape
	// précédante
	private void L(List<String> mots, boolean[][] map, ArrayList<ArrayList<String>> current) {

		//Va stocker les ensembles ajoutés à cette étape
		ArrayList<ArrayList<String>> next = new ArrayList<ArrayList<String>>();

		//Pour tout les ensembles de l'étape précédante
		for (int i = 0; i < current.size(); i++) {
			ArrayList<String> a = current.get(i);

			
			//On compare à tout les autres ensembles
			for (int j = i + 1; j < current.size(); j++) {
				int test = 0;
				ArrayList<String> b = current.get(j);
				String toAdd = null;
				
				//S'ils ont un seul Element de différence, on stock ce mot dans toAdd
				for (String s : a) {
					if (!b.contains(s)) {
						test++;
						if (test > 1) {
							break;
						}else{
							toAdd = s;
						}
					}
				}

				
				//S'il y avais un mot de diff
				if (test == 1) {
					ArrayList<String> temp = new ArrayList<String>();
					//on copie a
					for (String s : b) {
						temp.add(s);
					}
					temp.add(toAdd);
					//trie par ordre alphabbétique de la copie
					java.util.Collections.sort(temp);

					//S'il n'existe pas déjà
					if (!result.containsKey(temp)) {
						int res = 0;
						boolean valide = true;

						//On compte le nombre de fois que ces mots apparaissent ensembles
						for (int w = 0; w < map.length; w++) {
							valide = true;
							for (String s : temp) {
								int index = mots.indexOf(s);
								valide = map[w][index];
								if (!valide) {
									break;
								}
							}
							if (valide) {
								res++;
							}
						}
						
						//S'ils apparraissent au moins une fois et assez fréquent, on les ajoutent
						if(res > 0){
							double support = (((double)res)/map.length)*100;
							if(support>0.9){
								next.add(temp);
								result.put(temp, res);
							}
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

}
