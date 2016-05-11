package core;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Parser {
	private ArrayList<String> words = new ArrayList<String>();
	private boolean[][] tab;

	public ArrayList<String> getWords() {
		return words;
	}

	public void setWords(ArrayList<String> words) {
		this.words = words;
	}

	public boolean[][] getTab() {
		return tab;
	}

	public void setTab(boolean[][] tab) {
		this.tab = tab;
	}

	/*
	 * Récupère sur l'entrée standart les mots dans words, puis les articles et
	 * analyse la présence de mots dans les articles , le résultat est dans tab
	 * et en retour
	 */
	public void run(String entry_articles, String entry_words) throws FileNotFoundException {

        // read line by line input words

        try{
            InputStream ips=new FileInputStream(entry_words);
            InputStreamReader ipsr=new InputStreamReader(ips, "ISO-8859-1");
            BufferedReader br=new BufferedReader(ipsr);
            String line;
            while ((line=br.readLine()) != null){
                words.add(line);
            }
            br.close();

			// Créer une liste de contenu unique basée sur les éléments de ArrayList
			Set<String> mySet = new HashSet<String>(words);

			// Créer une Nouvelle ArrayList à partir de Set
			words = new ArrayList<String>(mySet);
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

        // read line by line input article
		ArrayList<String> articles = new ArrayList<>();
        try{
            InputStream ips=new FileInputStream(entry_articles);
            InputStreamReader ipsr=new InputStreamReader(ips, "ISO-8859-1");
            BufferedReader br=new BufferedReader(ipsr);
            String line;
            while ((line=br.readLine()) != null){
				line = " "+line;
                articles.add(line);
            }
            br.close();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }

		tab = new boolean[articles.size()][words.size()];

		int i = 0;
		for (String article : articles) {
			int j = 0;
			for (String word : words) {
				if (article.contains(" "+word+" ")) {
					tab[i][j] = true;
				} else {
					tab[i][j] = false;
				}
				j++;
			}
			i++;
		}


	}

}
