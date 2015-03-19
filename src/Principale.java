import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import load.SKOSToI2B2;


import transco.ConceptSKOS;
import transco.OWLToSKOS;
import transco.SKOSReader;
import transco.SKOSToOWL;
import transco.WriteOntology;
import transco.OWLOntologyBuilderSKOSAPI;

/**
 * Cette classe contient la main et permet d'effectuer un transcodage SKOSToOWL
 * ou un transcodage OWLToSKOS.
 * 
 * @author Yoann Keravec Date: 09/03/2015<br>
 *         Institut Bergonié<br>
 */
public class Principale {

	/**
	 * Cette méthode permet d'effectuer le transcodage d'un fichier SKOS en OWL.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */

	public static void skosToOWL(String input, String output) {

		// On lit le fichier qui alimente une structure de données interne
		SKOSReader reader = new SKOSReader();
		reader.loadFile(input);

		// On crée une instance de l'objet qui va permettre de créer une
		// ontologie à partir de la structure de données.
		OWLOntologyBuilderSKOSAPI builder = new OWLOntologyBuilderSKOSAPI();

		// On parcours la structure de données pour alimenter l'ontologie créée.
		for (ConceptSKOS conceptCur : reader.getListConceptSKOS()) {
			builder.createClass(conceptCur);
		}

		WriteOntology fileOntoWriter = new WriteOntology(builder.getOntology());
		fileOntoWriter.writeFile(output, reader.getFormat());
	}

	// END skosToOWL

	/**
	 * Cette méthode permet d'afficher un rappel sur les arguments à utiliser en
	 * entrée.
	 */
	public static void afficheMessageErreur() {
		System.err
				.println("Argument 1 obligatoire : type de transcodage : 1 : skos to owl ou 2 : owl to skos ou 3 : skos to i2b2");
		System.err.println("Argument 2 obligatoire : Nom du fichier en entrée");
		System.err.println("Argument 3 obligatoire : Nom du fichier en sortie");
		System.err.println("Argument 4 facultatif : IRI de l'ontologie");
		System.err.println("Argument 5 facultatif : Prefix de l'ontologie");
	}

	public static void checkArgs(String[] args) {
		// On vérifie que l'on a le bon nombre d'argument
		if (args.length < 3) {
			System.err
					.println("Erreur : Nombre d'arguments en entrée incorrect.");
			System.err
					.println("Vous devez saisir 3 arguments obligatoire et un argument facultatif :");
			afficheMessageErreur();
			System.exit(1);
		} else {
			if (args.length > 4) {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier en sortie : " + args[2]);
				System.out.println("IRI de l'ontologie : " + args[3]);
				System.out.println("Prefix de l'ontologie : " + args[4]);
			} else {
				if (args.length > 3) {
					System.out.println("Type de transcodage : " + args[0]);
					System.out.println("Fichier en entrée : " + args[1]);
					System.out.println("Fichier en sortie : " + args[2]);
					System.out.println("IRI de l'ontologie : " + args[3]);
				} else {
					System.out.println("Type de transcodage : " + args[0]);
					System.out.println("Fichier en entrée : " + args[1]);
					System.out.println("Fichier en sortie : " + args[2]);
				}
			}
		}
	}

	/**
	 * La main prend trois argument: \n Argument 1 : type de transcodage : 1 :
	 * skostoowl, 2 : owltoskos ou 3 : skostoi2b2 \n Argument 2 : Nom du fichier
	 * en entrée Argument \n 3 : Nom du fichier en sortie \n 4 : Iri de
	 * l'ontologie \n 5 : Prefix de l'ontologie \n
	 * 
	 * @param args
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			// On ouvre le menu
			menu();
		} else {
			switch (args[0]) {
			case "1":
				checkArgs(args);
				if (args.length < 4)
					new SKOSToOWL(args[1], args[2]);
				else
					new SKOSToOWL(args[1], args[2], args[3]);
				break;
			case "2":
				checkArgs(args);
				if (args.length < 4)
					new OWLToSKOS(args[1], args[2]);
				else if (args.length < 5)
					new OWLToSKOS(args[1], args[2], args[3]);
				else
					new OWLToSKOS(args[1], args[2], args[3],
							args[4]);
				break;
			case "3":
				if (args.length < 2)
					System.err.println("Nombre d'argument incorrect.");
				else {
					new SKOSToI2B2(args[1]);
				}
				break;
			default:
				System.err
						.println("La valeur du premier argument est incorrect : ");
				afficheMessageErreur();
				break;
			}
		}
	}

	public static void menu() {

		String swValue;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out
					.println("===================[     MENU     ]======================");
			System.out
					.println("| Quelle opération souhaitez-vous effectuer ?           |");
			System.out
					.println("| 1 - Transcodage SKOS to OWL                           |");
			System.out
					.println("| 2 - Transcodage OWL to SKOS                           |");
			System.out
					.println("| 3 - Chargement SKOS to I2B2                           |");
			System.out
					.println("| 4 - Quitter                                           |");
			System.out
					.println("=========================================================");

			try {
				swValue = br.readLine();

				switch (swValue) {
				case "1":
					menuSKOSToOWL();
					break;
				case "2":
					menuOWLToSKOS();
					break;
				case "3":
					menuSKOSToI2B2();
					break;
				case "4":
					System.exit(0);
				default:
					System.out
							.println("Votre choix est invalide, recommencez!");
					break;
				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * Cette méthode gère le menu correspondant à la transcodification SKOS TO
	 * OWL
	 */
	public static void menuSKOSToOWL() {
		menuTransco(1);
	}

	/**
	 * Cette méthode gère le menu correspondant à la transcodification OWL TO
	 * SKOS
	 */
	public static void menuOWLToSKOS() {
		menuTransco(2);
	}

	/**
	 * Cette méthode gère le menu correspondant au chargement d'un fichier SKOS
	 * vers I2B2
	 */
	public static void menuSKOSToI2B2() {

	}

	public static void menuTransco(int typeTransco) {
		if (typeTransco == 1)
			System.out
					.println("===================[     MENU SKOS TO OWL    ]======================");
		else
			System.out
					.println("===================[     MENU OWL TO SKOS    ]======================");
		System.out.println("Nom du fichier en entrée :");
		String fichierEntree = choixUtilisateur();
		checkFile(fichierEntree, true);
		System.out.println("Nom du fichier en sortie :");
		String fichierSortie = choixUtilisateur();
		checkFile(fichierEntree, false);
		System.out.println("Nom de l'IRI (facultatif) :");
		String iri = choixUtilisateur();
		System.out.println("Nom du prefixe (facultatif) :");
		String prefix = choixUtilisateur();
		System.out.println("Transcodage en cours ...");
		if (typeTransco == 1) {
			if (iri == null || iri.isEmpty())
				new SKOSToOWL(fichierEntree, fichierSortie);
			else if (prefix == null || prefix.isEmpty())
				new SKOSToOWL(fichierEntree, fichierSortie,
						iri);
			else
				new SKOSToOWL(fichierEntree, fichierSortie,
						iri, prefix);
		} else {
			if (iri == null || iri.isEmpty())
				new OWLToSKOS(fichierEntree, fichierSortie);
			else if (prefix == null || prefix.isEmpty())
				new OWLToSKOS(fichierEntree, fichierSortie,
						iri);
			else
				new OWLToSKOS(fichierEntree, fichierSortie,
						iri, prefix);
		}

	}

	/**
	 * Cette méthode permet de vérifier si le nom de fichier saisie est bien
	 * saisie et permet d'effectuer un test d'existence au besoin Le deuxième
	 * paramètre prend true pour faire le test d'existence, false sinon.
	 * 
	 * @param fichierEntree
	 * @param existence
	 */
	public static void checkFile(String fichierEntree, boolean existence) {
		if (existence) {
			if (!new File(fichierEntree).exists()) {
				System.out.println("Le fichier saisie n'existe pas.");
				System.out
						.println("Veuillez-saisir de nouveau le nom du fichier.");
				fichierEntree = null;
			}
		}
		while (fichierEntree == null || fichierEntree.trim().isEmpty()) {
			System.out.println("Veuillez saisir un fichier à lire.");
			System.out.println("Nom du fichier en entrée :");
			fichierEntree = choixUtilisateur();
			if (existence) {
				if (!new File(fichierEntree).exists()) {
					System.out.println("Le fichier saisie n'existe pas.");
					System.out
							.println("Veuillez-saisir de nouveau le nom du fichier.");
					fichierEntree = null;
				}
			}
		}
	}

	public static String choixUtilisateur() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String choixUtilisateur = null;

		try {
			choixUtilisateur = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return choixUtilisateur;
	}

}
