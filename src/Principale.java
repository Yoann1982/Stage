import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import load.SKOSToI2B2;
import transco.OWLToSKOS;
import transco.SKOSToOWL;

/**
 * Cette classe contient la main et permet d'effectuer un transcodage SKOSToOWL
 * ou un transcodage OWLToSKOS.
 * 
 * @author Yoann Keravec Date: 09/03/2015<br>
 *         Institut Bergonié<br>
 */
public class Principale {

	/**
	 * Cet argument indique le nombre d'arguments attendu en entrée en fonction
	 * de l'action
	 */
	private static HashMap<Integer, Integer> mapArgObligatoire = new HashMap<Integer, Integer>();

	private static HashMap<Integer, Integer> mapArgFacultatif = new HashMap<Integer, Integer>();

	public static void initMapArgument() {
		// 1 : skos to owl
		mapArgObligatoire.put(1, 3);
		mapArgFacultatif.put(1, 2);

		// 2 : owl to skos
		mapArgObligatoire.put(2, 3);
		mapArgFacultatif.put(2, 2);

		// 3 : skos to csv
		mapArgObligatoire.put(3, 5);
		mapArgFacultatif.put(3, 0);

		// 4 : skos to SQL
		mapArgObligatoire.put(4, 5);
		mapArgFacultatif.put(4, 0);

		// 5 : skos to SQLLoader
		mapArgObligatoire.put(5, 7);
		mapArgFacultatif.put(5, 0);

	}

	/**
	 * Cette méthode permet d'afficher un rappel sur les arguments à utiliser en
	 * entrée.
	 */
	public static void afficheMessageErreur(String typeMessage) {

		switch (typeMessage) {

		case "1":
			System.err
					.println("Argument 1 obligatoire : type de transcodage : 1 : skos to owl ou 2 : owl to skos ou 3 : skos to i2b2");
			System.err
					.println("Argument 2 obligatoire : Nom du fichier en entrée");
			System.err
					.println("Argument 3 obligatoire : Nom du fichier en sortie");
			System.err.println("Argument 4 facultatif : IRI de l'ontologie");
			System.err.println("Argument 5 facultatif : Prefix de l'ontologie");
			break;
		case "2":
			System.err
					.println("Argument 1 obligatoire : type de transcodage : 1 : skos to owl ou 2 : owl to skos ou 3 : skos to i2b2");
			System.err
					.println("Argument 2 obligatoire : Nom du fichier en entrée");
			System.err
					.println("Argument 3 obligatoire : Nom du fichier en sortie");
			System.err.println("Argument 4 facultatif : IRI de l'ontologie");
			System.err.println("Argument 5 facultatif : Prefix de l'ontologie");
			break;
		case "3":
			System.err
					.println("Argument 1 obligatoire : type de transcodage : 1 : skos to owl ou 2 : owl to skos ou 3 : skos to i2b2");
			System.err
					.println("Argument 2 obligatoire : Nom du fichier en entrée");
			System.err
					.println("Argument 3 obligatoire : Nom du fichier en sortie");
			System.err.println("Argument 4 obligatoire : Caractère séparateur");
			System.err
					.println("Argument 5 obligatoire : Nom du fichier format table I2B2");
			break;
		case "4":
			System.err
					.println("Argument 1 obligatoire : type de transcodage : 1 : skos to owl ou 2 : owl to skos ou 3 : skos to i2b2");
			System.err
					.println("Argument 2 obligatoire : Nom du fichier en entrée");
			System.err
					.println("Argument 3 obligatoire : Nom du fichier en sortie");
			System.err.println("Argument 4 obligatoire : Nom de la table");
			System.err
					.println("Argument 5 obligatoire : Nom du fichier format table I2B2");
			break;
		case "5":
			System.err
					.println("Argument 1 obligatoire : type de transcodage : 1 : skos to owl ou 2 : owl to skos ou 3 : skos to i2b2");
			System.err
					.println("Argument 2 obligatoire : Nom du fichier en entrée");
			System.err
					.println("Argument 3 obligatoire : Nom du fichier en sortie");
			System.err.println("Argument 4 obligatoire : Nom du fichier CSV en sortie");
			
			System.err.println("Argument 5 obligatoire : Caractère séparateur");
			System.err.println("Argument 6 obligatoire : Nom de la table");
			System.err
					.println("Argument 7 obligatoire : Nom du fichier format table I2B2");
			break;
		case "6":
			break;
		default:
			break;
		}
	}

	public static void checkArgs(String[] args) {
		// On vérifie que l'on a le bon nombre d'argument en fonction de
		// l'action choisie

		Integer nbArgObligatoire = mapArgObligatoire.get(Integer
				.decode(args[0]));
		Integer nbArgFacultatif = mapArgFacultatif.get(Integer.decode(args[0]));

		if (args.length < nbArgObligatoire) {
			System.err
					.println("Erreur : Nombre d'arguments en entrée incorrect.");
			System.err.println("Vous devez saisir " + nbArgObligatoire
					+ " arguments obligatoires et " + nbArgFacultatif
					+ " arguments facultatifs :");
			afficheMessageErreur(args[0]);
			System.exit(1);
		} else {
			afficheListeArgument(args, nbArgObligatoire, nbArgFacultatif);
			lanceTransco(args, nbArgObligatoire, nbArgFacultatif);
		}
	}

	public static void afficheListeArgument(String[] args,
			Integer nbArgObligatoire, Integer nbArgFacultatif) {

		switch (args[0]) {

		case "1":

			if (args.length == nbArgObligatoire + nbArgFacultatif) {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier en sortie : " + args[2]);
				System.out.println("IRI de l'ontologie : " + args[3]);
				System.out.println("Prefix de l'ontologie : " + args[4]);
			} else {
				if (args.length > nbArgObligatoire) {
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
			break;
		case "2":
			if (args.length == nbArgObligatoire + nbArgFacultatif) {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier en sortie : " + args[2]);
				System.out.println("IRI de l'ontologie : " + args[3]);
				System.out.println("Prefix de l'ontologie : " + args[4]);
			} else {
				if (args.length > nbArgObligatoire) {
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
			break;
		case "3":
			if (args.length == nbArgObligatoire + nbArgFacultatif) {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier en sortie : " + args[2]);
				System.out.println("Caractère séparateur : " + args[3]);
				System.out.println("Fichier Format : " + args[4]);
			} else {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier en sortie : " + args[2]);
				System.out.println("Fichier Format : " + args[3]);
			}
			break;
		case "4":
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier en sortie : " + args[2]);
				System.out.println("Nom de la table : " + args[3]);
				System.out.println("Fichier Format : " + args[4]);
			break;
		case "5":
			if (args.length == nbArgObligatoire + nbArgFacultatif) {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier SQLLoader en sortie : " + args[2]);
				System.out.println("Fichier CSV en sortie : " + args[3]);
				System.out.println("Caractère séparateur : " + args[4]);
				System.out.println("Nom de la table : " + args[5]);
				System.out.println("Fichier Format : " + args[6]);
			} else {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier SQLLoader en sortie : " + args[2]);
				System.out.println("Fichier CSV en sortie : " + args[3]);
				System.out.println("Nom de la table : " + args[4]);
				System.out.println("Fichier Format : " + args[5]);
			}
			break;
		case "6":
			break;
		default:
			break;
		}
	}

	public static void lanceTransco(String[] args, Integer nbArgObligatoire,
			Integer nbArgFacultatif) {
		String[] parametre = null;

		switch (args[0]) {

		case "1":
			parametre = new String[args.length - 1];
			for (int i = 0; i < args.length - 1; i++) {
				parametre[i] = args[i + 1];
			}
			new SKOSToOWL(parametre);
			break;
		case "2":
			parametre = new String[args.length - 1];
			for (int i = 0; i < args.length - 1; i++) {
				parametre[i] = args[i + 1];
			}
			new OWLToSKOS(parametre);
		case "3":
			SKOSToI2B2 loaderCSV = new SKOSToI2B2(args[1]);
			loaderCSV.createCSV(args[2], args[3], args[4]);
			break;
		case "4":
			SKOSToI2B2 loaderSQL = new SKOSToI2B2(args[1]);
			loaderSQL.createSQL(args[2], args[3], args[4]);
			break;
		case "5":
			SKOSToI2B2 loaderSQLLoader = new SKOSToI2B2(args[1]);
			loaderSQLLoader.createSQLLoader(args[2], args[3], args[4], args[5], args[6]);
			break;
		case "6":
			break;
		default:
			System.err.println("Choix d'action inconnu.");
			break;
		}
	}

	/**
	 * La main prend trois argument: \n Argument 1 : type de transcodage : \t 1
	 * : SKOS TO OWL \n \t 2 : OWL TO SKOS \n \t 3 : SKOS TO CSV \n \t 4 : SKOS
	 * TO SQL (Insert) \n \t 5 : SKOS TO SQLLoader \n \t 6 : SKOS TO I2B2
	 * Argument 2 : Nom du fichier en entrée \n Argument 3 : Nom du fichier en
	 * sortie \n Argument 4 : Iri de l'ontologie \n Argument 5 : Prefix de
	 * l'ontologie \n
	 * 
	 * @param args
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		initMapArgument();
		if (args.length == 0) {
			// On ouvre le menu
			menu();
		} else {
			checkArgs(args);
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
		String valeurSaisie;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out
				.println("===================[     MENU SKOS TO I2B2    ]======================");
		System.out.println("Que voulez-vous faire ?");
		System.out.println("1 - Générer un fichier CSV");
		System.out.println("2 - Générer un fichier SQL (insert into)");
		System.out
				.println("3 - Générer un fichier SQLLoader et son fichier CSV associé");
		System.out.println("4 - Alimenter la table d'I2B2");
		System.out.println("5 - Quitter");
		try {
			valeurSaisie = br.readLine();
			if (valeurSaisie.equals("5")) {
				System.exit(0);
			} else
				genererSortie(Integer.parseInt(valeurSaisie));
		} catch (IOException e2) {
			e2.printStackTrace();
		}

	}

	public static void genererSortie(int typeFichier) {
		String fichierEntree = null;
		String fichierSortie = null;
		String fichierFormat = null;
		String fichierCSV = null;
		String separateur = null;
		String nomTable = null;

		System.out.println("Nom du fichier en entrée :");
		fichierEntree = choixUtilisateur();
		checkFile(fichierEntree, true);
		System.out.println("Nom du fichier en sortie :");
		fichierSortie = choixUtilisateur();
		checkFile(fichierSortie, false);
		if (typeFichier == 3) {
			System.out.println("Nom du fichier CSV à générer :");
			fichierCSV = choixUtilisateur();
			checkFile(fichierCSV, false);
		}
		if (typeFichier == 1 || typeFichier == 3) {
			System.out.println("Caractère de séparation :");
			separateur = choixUtilisateur();
		}
		if (typeFichier == 2 || typeFichier == 3) {
			System.out.println("Nom de la table SQL à charger :");
			nomTable = choixUtilisateur();
		}
		System.out.println("Nom du fichier format Metadata :");
		fichierFormat = choixUtilisateur();
		checkFile(fichierFormat, true);
		SKOSToI2B2 loaderI2B2 = new SKOSToI2B2(fichierEntree);
		switch (typeFichier) {
		case 1:
			loaderI2B2.createCSV(fichierSortie, separateur, fichierFormat);
			break;
		case 2:
			loaderI2B2.createSQL(fichierSortie, nomTable, fichierFormat);
			break;
		case 3:
			loaderI2B2.createSQLLoader(fichierSortie, fichierCSV, separateur,
					nomTable, fichierFormat);
			break;
		case 4:
			break;
		default:
			System.err.println("Erreur : Type de sortie à générer inconnu.");
			break;
		}
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
		checkFile(fichierSortie, false);
		System.out.println("Nom de l'IRI (facultatif) :");
		String iri = choixUtilisateur();
		System.out.println("Nom du prefixe (facultatif) :");
		String prefix = choixUtilisateur();
		System.out.println("Transcodage en cours ...");
		if (typeTransco == 1) {
			if (iri == null || iri.isEmpty())
				new SKOSToOWL(fichierEntree, fichierSortie);
			else if (prefix == null || prefix.isEmpty())
				new SKOSToOWL(fichierEntree, fichierSortie, iri);
			else
				new SKOSToOWL(fichierEntree, fichierSortie, iri, prefix);
		} else {
			if (iri == null || iri.isEmpty())
				new OWLToSKOS(fichierEntree, fichierSortie);
			else if (prefix == null || prefix.isEmpty())
				new OWLToSKOS(fichierEntree, fichierSortie, iri);
			else
				new OWLToSKOS(fichierEntree, fichierSortie, iri, prefix);
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
