package core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Scanner;

public class ParserTicket {
	////////////////////////////////
	// Les noms des attributs
	private ArrayList<String> words = new ArrayList<String>();
	// Tableau de présence (true si présent)
	private boolean[][] tab;
	////////////////////////////////
	
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

		InputStream ips=new FileInputStream(entry_tickets);
		InputStreamReader ipsr = null;
		try {
			ipsr = new InputStreamReader(ips, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
         
		
		Scanner scan = new Scanner(ipsr);
		
		// Lecture de l'entete
		String[] entete = scan.nextLine().split("\t");

		// Lecture de tout
		ArrayList<String[]> body = new ArrayList<String[]>();
		while (scan.hasNextLine()) {
			body.add(scan.nextLine().split("\t"));
		}
		////////////////////////////
		//// REMPLISSAGE DE word////
		////////////////////////////
		
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
		
		words.add("revenus_0-" + Math.round(revFirstLimit));
		words.add("revenus_" + Math.round(revFirstLimit) + "-" + Math.round(revSecondLimit));
		words.add("revenus_" + Math.round(revSecondLimit) + "-" + Math.round(revThirdLimit));
		words.add("revenus_" + Math.round(revThirdLimit) + "-inf");
		
		// colonne 7 : age
		words.add("age_0-18");
		words.add("age_19-25");
		words.add("age_26-40");
		words.add("age_41+");
	
		// colonnes des produits :
		for(int i=7;i<entete.length;i++){
			words.add(entete[i]);
		}

		////////////////////////////
		///// CREATION DE tab //////
		////////////////////////////
		int nbEntry = body.size();
		int nbParam = words.size();
		boolean[][] tabRes = new boolean[nbEntry][nbParam];
		
		////////////////////////////
		//// REMPLISSAGE DE tab ////
		////////////////////////////
		
		for (int i=0; i<nbEntry;i++){
			//Colonne 2 : prix [0 - 3]
			double prix = Double.parseDouble((body.get(i))[1]);

			if(prix <= prixFirstLimit){
				tabRes[i][0] = true;
			}else if(prix <= prixSecondLimit){
				tabRes[i][1] = true;
			}else if(prix <= prixThirdLimit){
				tabRes[i][2] = true;
			}else{
				tabRes[i][3] = true;
			}
			
			
			//Colonne 3 : pmethod [4 - 6]
			if((body.get(i))[2]=="CHEQUE"){
				tabRes[i][4] = true;
			}else if((body.get(i))[2]=="CASH"){
				tabRes[i][5] = true;
			}else if((body.get(i))[2]=="CARD"){
				tabRes[i][6] = true;
			}
			
			//Colonne 4 : sex [7 - 8]
			if((body.get(i))[3]=="M"){
				tabRes[i][7] = true;
			}else if((body.get(i))[3]=="F"){
				tabRes[i][8] = true;
			}
			
			//Colonne 5 : proprietaire [9 - 10]
			if((body.get(i))[4]=="YES"){
				tabRes[i][9] = true;
			}else 
			if((body.get(i))[4]=="NO"){
				tabRes[i][10] = true;
			}
			 
			//Colonne 6 : revenus [11 - 14]
			double rev = Double.parseDouble((body.get(i))[5]);
			if(rev <= revFirstLimit){
				tabRes[i][11] = true;
			}else
			if(rev <= revSecondLimit){
				tabRes[i][12] = true;
			}else
			if(rev <= revThirdLimit){
				tabRes[i][13] = true;
			}else{
				tabRes[i][14] = true;
			}
			
			//Colonne 7 : age [15 - 18]
			double age = Double.parseDouble((body.get(i))[6]);
			if(age < 19){
				tabRes[i][15] = true;
			}else
			if(age < 26){
				tabRes[i][16] = true;
			}else
			if(age < 41){
				tabRes[i][17] = true;
			}else{
				tabRes[i][18] = true;
			}
			
			//Colonnes produits
			for (int j=7; j<entete.length ; j++){
				if((body.get(i))[j]=="T"){
					tabRes[i][j+11]=true;
				}
			}
			
		}
		
		setTab(tabRes);
	}//end run(...)

	
	
	
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
