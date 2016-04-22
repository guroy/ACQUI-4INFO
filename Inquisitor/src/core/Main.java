package core;

import core.fpGrowth.FPGrowth;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		Parser parser = new Parser();
		FPGrowth fp = new FPGrowth();
		
		
		// -t = étude du ticket
		// -a = étude de l'article
		if(args.length == 0){
			System.out.println("Utilisation : ");
			System.out.println("inquisitor -a articles.txt mots.txt fichier.out");
			System.out.println("ou");
			System.out.println("inquisitor -t ticket.txt fichier.out");
		}
		else if (args[0].equals("-a")) {
			if (args.length != 4) {
				System.out.println("Utilisation : ");
				System.out.println("inquisitor -a articles.txt mots.txt fichier.out");
			} else {
				// TODO : Parser le fichier articles avec les mots discriminants
				// TODO : Faire l'étude des articles
				parser.run(args[1], args[2]);

				fp.process(parser.getWords(), parser.getTab());
			}
		} else if (args[1].equals("-t")) {
			if (args.length != 3) {
				System.out.println("Utilisation : ");
				System.out.println("inquisitor -t ticket.txt fichier.out");
			} else {
				// TODO : Étude du ticket de caisse
			}
		} else {
			System.out.println("Utilisation : ");
			System.out.println("inquisitor -a articles.txt mots.txt fichier.out");
			System.out.println("ou");
			System.out.println("inquisitor -t ticket.txt fichier.out");
		}
	}

}
