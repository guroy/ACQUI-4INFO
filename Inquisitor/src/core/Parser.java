package core;

import java.util.ArrayList;
import java.util.Scanner;

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

	/*Récupère sur l'entrée standart les mots dans words, puis les articles et analyse la présence de mots dans les articles
	, le résultat est dans tab et en retour*/
	public void run() {
		Scanner sc = new Scanner(System.in);

		while (sc.hasNextLine()) {
			String s = sc.nextLine();
			if (!words.contains(s)) {
				words.add(s);
			}
		}

		ArrayList<String> articles = new ArrayList<String>();
		while (sc.hasNextLine()) {

			articles.add(sc.nextLine());
		}
		tab = new boolean[articles.size()][words.size()];

		int i = 0;
		for (String article : articles) {
			int j = 0;
			for (String word : words) {
				if (article.contains(word)) {
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
