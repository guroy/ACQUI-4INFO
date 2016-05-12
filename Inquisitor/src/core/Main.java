package core;

import java.io.IOException;

import core.fpg.FPGrowth;

public class Main {

	public static void main(String[] args) throws IOException {
		// -t = étude du ticket
		// -a = étude de l'article
		if(args.length == 0){
			System.out.println("Utilisation : ");
			System.out.println("inquisitor -a articles.txt mots.txt fichier.out");
			System.out.println("ou");
			System.out.println("inquisitor -t ticket.txt fichier.out");
		}
		else if (args[0].equals("-a")) {
			if (args.length != 4 && args.length != 5) {
				System.out.println("Utilisation : ");
				System.out.println("inquisitor -a articles.txt mots.txt fichier.out");
			} else {
				Parser parser = new Parser();
				parser.run(args[1], args[2]);
				if (args.length == 5 && args[4].equals("-fpg")){
					FPGrowth fp = new FPGrowth(0.05, 0.75);
					fp.process(parser.getWords(), parser.getTab());

				}else {
					Apriori ap = new Apriori(args[3],0.05,0.5);
					ap.process(parser.getWords(), parser.getTab());
				}
			}
		} else if (args[0].equals("-t")) {
			if (args.length != 4 && args.length != 3) {
				System.out.println("Utilisation : ");
				System.out.println("inquisitor -t ticket.txt fichier.out");
			} else {
				ParserTicket parser2 = new ParserTicket();
				parser2.run(args[1]);

				if (args[3].equals("-fpg")){
					FPGrowth fp = new FPGrowth(0.05, 0.75);
					fp.process(parser2.getWords(), parser2.getTab());

				}else {
					Apriori ap = new Apriori(args[3],0.05,0.5);
					ap.process(parser2.getWords(), parser2.getTab());
				}
			}
		} else {
			System.out.println("Utilisation : ");
			System.out.println("inquisitor -a articles.txt mots.txt fichier.out [option : -fpg]");
			System.out.println("ou");
			System.out.println("inquisitor -t ticket.txt fichier.out [option : -fpg]");
		}
	}

}
