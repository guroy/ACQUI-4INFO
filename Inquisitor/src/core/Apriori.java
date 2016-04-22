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
					result.put(array.get(index), value + 1);
				}
			}
		}

		L(mots, map, array, 1);
		
	}

	// current = item de l'étape précédantes, length taille des trucs de l'étape
	// précédante
	private void L(List<String> mots, boolean[][] map, ArrayList<ArrayList<String>> current,
			int length) {

		ArrayList<ArrayList<String>> next = new ArrayList<ArrayList<String>>();

		for (int i = 0; i < current.size(); i++) {
			ArrayList<String> a = current.get(i);

			for (int j = i + 1; j < current.size(); j++) {
				int test = 0;
				ArrayList<String> b = current.get(j);
				String toAdd = null;
				for (String s : a) {
					if (!b.contains(s)) {
						test++;
						if (test > 1) {
							break;
						}

						toAdd = s;

					}
				}

				if (test == 1) {
					ArrayList<String> temp = new ArrayList<String>();
					for (String s : a) {
						temp.add(s);
					}
					temp.add(toAdd);
					java.util.Collections.sort(temp);

					if (!result.containsKey(temp)) {
						int res = 0;
						next.add(temp);
						boolean valide = true;

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

						result.put(temp, res);
					}
				}

			}
		}

		
		L(mots, map, next, length++);

	}

}
