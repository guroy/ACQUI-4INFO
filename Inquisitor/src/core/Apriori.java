package core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Apriori implements ERAAlgorithm {
	//Stock tout les item-set et leur supports
	private Map<ArrayList<String>, Integer> result = new HashMap<ArrayList<String>, Integer>();
	
	//Stock les règles d'association
	private Map<Map<ArrayList<String>,String>, Double> associationRules = new HashMap<Map<ArrayList<String>,String>, Double>();
	private int nbArticle;
	private String output;
	private double suppMin;
	private double confMin;

	public Apriori(String output,double suppMin,double confMin){
		this.output=output;
		this.suppMin=suppMin;
		this.confMin=confMin;
	}
	
	
	public void process(List<String> mots, boolean[][] map) {
		Map<ArrayList<String>, ArrayList<Integer>> next = new HashMap<ArrayList<String>,  ArrayList<Integer>>();
		
		nbArticle = map.length;
		
		int index =0;
		for (String mot : mots) {
			ArrayList<String> nextMots = new ArrayList<String>();
			ArrayList<Integer> nextLines = new ArrayList<Integer>();
			nextMots.add(mot);
			int count = 0;
			
			//on compte combien de fois apparait le mot et mémorise ça
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
		
		System.out.println("fin");
		
		//On écrit la sortie
		this.writeOut();
		
		
	}

	// current = item-set de l'étape précédantes
	private void L(List<String> mots, boolean[][] map, Map<ArrayList<String>, ArrayList<Integer>> current) {

		//Va stocker les item-set ajoutés à cette étape et leurs lignes où ils apparraissent
		Map<ArrayList<String>, ArrayList<Integer>> next = new HashMap<ArrayList<String>,  ArrayList<Integer>>();

		//On récupère toutes les clefs pour itéré dessus
		List<ArrayList<String>> keys = new ArrayList<ArrayList<String>>(current.keySet());

			
		//Pour tout les ensembles de l'étape précédante
		for (int i = 0; i < keys.size(); i++) {
			ArrayList<String> a = keys.get(i);

			
			//On compare à tout les autres ensembles
			for (int j = i + 1; j < keys.size(); j++) {
				String add1="";
				String add2="";
		
				ArrayList<String> b = keys.get(j);
				
				ArrayList<String> temp = new ArrayList<String>();

				temp.addAll(a);				
				temp.removeAll(b);
				
				//S'il ont que 1 élément d'écart, on rajoute les mots différents add1 et add2 et on peut envisager de générer des règles d'associations et item-set
				if(temp.size()==1){
					add1=temp.get(0);
					temp.addAll(b);				
					temp.removeAll(a);
					add2=temp.get(0);
					temp.addAll(a);
								
					
					//trie par ordre alphabétique
					java.util.Collections.sort(temp);
					
	
					
					int res = 0;	
					//Intersection des lignes entre les deux item-set de départs, pour donner les lignes de l'item-set d'arrivé
					ArrayList<Integer> nextLines = intersection(current.get(a),current.get(b));
				
					res = nextLines.size();
					
					//S'il apparait assez fréquemment, on ajoute ce nouvel item set et on considère les règles d'associations associées
					double support = (((double)res)/nbArticle);
					if(support>suppMin){
						
						//Si on a pas déjà stocké cet item set on ajoute
						if (!result.containsKey(temp)) {
							next.put(temp,nextLines);
							result.put(temp, res);
						}
						
						
						//On ajoute les règles d'association si elles ont une assez bonne confiance
						double oldSupport1 = (((double)current.get(b).size())/nbArticle);
						double oldSupport2 = (((double)current.get(a).size())/nbArticle);
						
						Map<ArrayList<String>, String> rules1 = new HashMap<ArrayList<String>,  String>();
						Map<ArrayList<String>, String> rules2 = new HashMap<ArrayList<String>,  String>();
						if(support/oldSupport1>confMin){
							rules1.put(b, add1);
							associationRules.put(rules1, support/oldSupport1);
						}
						if(support/oldSupport2>confMin){
							rules2.put(a, add2);
							associationRules.put(rules2, support/oldSupport2);
						}
						
						
						
						
					}
				}
				

			}

		}
		
		//S'il y as eu au moins deux ensembles on passe à la suite
		if(next.size() > 1){
			System.out.println(next.size());
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
    
    
    
    
    public  void writeOut() {

		
		
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


	    // create your iterator for your map
	    Iterator<Entry<ArrayList<String>, Integer>> it = result.entrySet().iterator();

	    // then use the iterator to loop through the map, stopping when we reach the
	    // last record in the map or when we have printed enough records
	    while (it.hasNext()) {

	        // the key/value pair is stored here in pairs
	        Entry<ArrayList<String>, Integer> pairs = it.next();
	        // since you only want the value, we only care about pairs.getValue(), which is written to out
	        try {
				out.write(pairs.getKey() + " = " +pairs.getValue() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

	    }
	    
	    
	    Iterator<Entry<Map<ArrayList<String>, String>, Double>> it2 = associationRules.entrySet().iterator();
	    
	    while (it2.hasNext()) {

	        // the key/value pair is stored here in pairs
	        Entry<Map<ArrayList<String>, String>, Double> pairs = it2.next();
	        // since you only want the value, we only care about pairs.getValue(), which is written to out
	        try {
				out.write(pairs.getKey() + "|| Confidence = " +pairs.getValue() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

	    }
	    
	    // lastly, close the file and end
	    try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
    }
    
    

}
