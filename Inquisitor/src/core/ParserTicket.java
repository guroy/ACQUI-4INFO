package core;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ParserTicket {
	// Les noms des attributs
	private ArrayList<String> words = new ArrayList<String>();
	// Tableau de présence (true si présent)
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

	public void run(String entry_tickets) throws FileNotFoundException {

		Scanner scan = new Scanner(entry_tickets);

		String[] entete = scan.nextLine().split("\t");

		int nbParam = entete.length;

		// Lecture de l'entete
		ArrayList<String[]> body = new ArrayList<String[]>();

		// Lecture de tout
		while (scan.hasNextLine()) {
			body.add(scan.nextLine().split("\t"));
		}

		// REMPLISSAGE DE word

		// colonne 1 : cardid
		// ignore

		// colonne 2 :  prix
		// Calcul de min et max de prix
		double minP = min(body, 1);
		double maxP = max(body, 1);
		double longueurIntervalePrix = (maxP - minP) / 4;
		
		double prixFirstLimit = minP + longueurIntervalePrix;
		double prixSecondLimit = minP + 2*longueurIntervalePrix;
		double prixThirdLimit = minP + 3*longueurIntervalePrix;
		
		words.add("prix_0-" + Math.round(prixFirstLimit));
		words.add("prix_" + Math.round(prixFirstLimit) + "-" + Math.round(prixSecondLimit));
		words.add("prix_" + Math.round(prixSecondLimit) + "-" + Math.round(prixThirdLimit));
		words.add("prix_" + Math.round(prixThirdLimit) + "-inf");
		
		// colonne 3 : pmethod
		words.add("pmethod_cheque");
		words.add("pmethod_cash");
		words.add("pmethod_card");
		
		// colonne 4 : sex
		words.add("sex_M");
		words.add("sex_F");
		
		// colonne 5 : proprio
		words.add("proprio_YES");
		words.add("proprio_NO");
		
		// colonne 6 : revenu
		// Calcul de min et max de revenu
		double minR = min(body, 5);
		double maxR = max(body, 5);
		double longueurIntervaleRevenu = (maxR - minR) / 4;

		double revFirstLimit = minR + longueurIntervaleRevenu;
		double revSecondLimit = minR + 2*longueurIntervaleRevenu;
		double revThirdLimit = minR + 3*longueurIntervaleRevenu;
		
		words.add("prix_0-" + Math.round(revFirstLimit));
		words.add("prix_" + Math.round(revFirstLimit) + "-" + Math.round(revSecondLimit));
		words.add("prix_" + Math.round(revSecondLimit) + "-" + Math.round(revThirdLimit));
		words.add("prix_" + Math.round(revThirdLimit) + "-inf");
		
		// colonne 7 : age
		words.add("age_0-18");
		words.add("age_19-25");
		words.add("age_26-40");
		words.add("age_40+");
	
		// colonnes des produits :
		for(int i=7;i<nbParam;i++){
			words.add(entete[i]);
		}

		// REMPLISSAGE DE tab

	}

	public double min(ArrayList<String[]> body, int colonne) {

		double min = Double.MAX_VALUE;

		// TIPS : "double Double.parseDouble(String str)"
		for (String[] tab : body) {
			if (Double.parseDouble(tab[colonne]) < min) {
				min = Double.parseDouble(tab[colonne]);
			}
		}
		return min;
	}

	public double max(ArrayList<String[]> body, int colonne) {

		double max = Double.MIN_VALUE;

		for (String[] tab : body) {
			if (Double.parseDouble(tab[colonne]) > max) {
				max = Double.parseDouble(tab[colonne]);
			}
		}
		return max;

	}
}
