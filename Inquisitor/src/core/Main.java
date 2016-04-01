package core;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// -t = étude du ticket
		// -a = étude de l'article
		if (args[0].equals("-a")) {
			if (args.length != 4) {
				System.out.println("Utilisation : ");
				System.out.println("inquisitor -a articles.txt mots.txt fichier.out");
			} else {
				// TODO
			}
		} else if (args[1].equals("-t")) {
			if (args.length != 3) {
				System.out.println("Utilisation : ");
				System.out.println("inquisitor -t ticket.txt fichier.out");
			} else {
				// TODO
			}
		} else {
			System.out.println("Utilisation : ");
			System.out.println("inquisitor -a articles.txt mots.txt fichier.out");
			System.out.println("ou");
			System.out.println("inquisitor -t ticket.txt fichier.out");
		}
	}

}
