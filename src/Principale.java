import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import Param.Param;
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

	public static final String RETOUR_CHARIOT = Param.RETOUR_CHARIOT;
	public static final String FILE_SEPARATOR = Param.FILE_SEPARATOR;

	private static final String repertoireOutput = Param.REPERTOIRE_OUTPUT;

	private static final String repertoireErreur = Param.REPERTOIRE_ERREUR;

	private static final String repertoireParam = Param.REPETOIRE_PARAM;

	private static final String fichierParamOracle = Param.FICHIER_PARAM_ORACLE;
	private static final String fichierParamMySQL = Param.FICHIER_PARAM_MYSQL;
	private static final String fichierParamPostgreSQL = Param.FICHIER_PARAM_POSTGRESQSL;

	public static void initMapArgument() {
		// 1 : skos to owl
		mapArgObligatoire.put(1, 3);
		mapArgFacultatif.put(1, 2);

		// 2 : owl to skos
		mapArgObligatoire.put(2, 3);
		mapArgFacultatif.put(2, 2);

		// 3 : skos to csv
		mapArgObligatoire.put(3, 4);
		mapArgFacultatif.put(3, 1);

		// 4 : skos to SQL
		mapArgObligatoire.put(4, 5);
		mapArgFacultatif.put(4, 0);

		// 5 : skos to SQLLoader
		mapArgObligatoire.put(5, 6);
		mapArgFacultatif.put(5, 1);

		// 6 : skos to i2b2
		mapArgObligatoire.put(6, 4);
		mapArgFacultatif.put(6, 0);

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
			System.err.println("Argument 4 facultatif : Caractère séparateur");
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
			System.err
					.println("Argument 4 obligatoire : Nom du fichier CSV en sortie");

			System.err.println("Argument 5 facultatif : Caractère séparateur");
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

	/**
	 * Cette méthode permet de vérifier les paramètres en entrée.
	 * Elle vérifie le nombre de paramètres en fonction du type d'action indiquée (le premier argument).
	 * Si le nombre est incorrect, elle affiche sur la sortie standard des messages d'erreurs.
	 * Si le nombre de paramètres est OK, elle affiche sur la sortie standard les paramètres d'entrée et lance les transco (méthode lanceTransco)
	 * @param args La liste d'arguments du lancement de l'application.
	 */
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

	/**
	 * Cette méthode affiche la liste des arguments en entrée.
	 * @param args La liste des arguments.
	 * @param nbArgObligatoire Le nombre d'arguments obligatoires.
	 * @param nbArgFacultatif Le nombre d'arguments facultatifs.
	 */
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
			System.out.println("Type de transcodage : " + args[0]);
			System.out.println("Fichier en entrée : " + args[1]);
			System.out.println("Méthode de chargement : " + args[2]);
			System.out.println("Fichier Format : " + args[3]);
			break;
		default:
			break;
		}
	}

	/**
	 * Cette méthode effectue le lancement des traitements de transcodage.
	 * @param args La liste des paramètres.
	 * @param nbArgObligatoire Le nombre d'arguments obligatoire.
	 * @param nbArgFacultatif Le nombre d'arguments facultatifs.
	 */
	public static void lanceTransco(String[] args, Integer nbArgObligatoire,
			Integer nbArgFacultatif) {
		String[] parametre = null;
		if (!new File(args[1]).exists()) {
			System.err.println("Erreur : Le fichier indiqué en entrée ("
					+ args[1] + ") n'existe pas.");
			System.exit(1);
		} else {
			switch (args[0]) {

			case "1":
				args[2] = verifFichierSortie(args[2], 1);
				if (args.length > 3) {
					args[3] = verifIRI(args[3]);
				}
				parametre = new String[args.length - 1];
				for (int i = 0; i < args.length - 1; i++) {
					parametre[i] = args[i + 1];
				}
				new SKOSToOWL(parametre);
				break;
			case "2":
				args[2] = verifFichierSortie(args[2], 1);
				if (args.length > 3) {
					args[3] = verifIRI(args[3]);
				}
				parametre = new String[args.length - 1];
				for (int i = 0; i < args.length - 1; i++) {
					parametre[i] = args[i + 1];
				}
				new OWLToSKOS(parametre);
				break;
			case "3":
				args[2] = verifFichierSortie(args[2], 1);
				args[4] = verifFichierFormat(args[4], 1);
				SKOSToI2B2 loaderCSV = new SKOSToI2B2(args[1]);
				parametre = new String[args.length - 2];
				for (int i = 0; i < args.length - 2; i++) {
					parametre[i] = args[i + 2];
				}
				loaderCSV.createCSV(parametre);
				break;
			case "4":
				args[2] = verifFichierSortie(args[2], 1);
				args[4] = verifFichierFormat(args[4], 1);
				SKOSToI2B2 loaderSQL = new SKOSToI2B2(args[1]);
				parametre = new String[args.length - 2];
				for (int i = 0; i < args.length - 2; i++) {
					parametre[i] = args[i + 2];
				}
				loaderSQL.createSQL(parametre);
				break;
			case "5":
				args[6] = verifFichierFormat(args[6], 1);
				args[2] = verifFichierSortie(args[2], 1);
				args[3] = verifFichierSortie(args[3], 1);
				SKOSToI2B2 loaderSQLLoader = new SKOSToI2B2(args[1]);
				parametre = new String[args.length - 2];
				for (int i = 0; i < args.length - 2; i++) {
					parametre[i] = args[i + 2];
				}
				loaderSQLLoader.createSQLLoader(parametre);
				break;
			case "6":
				if (!(args[2].trim().equals("1") || args[2].trim().equals("2"))) {
					System.err.println("Erreur : Le mode de chargement saisi ("
							+ args[2] + ") est incorrect.");
					System.exit(1);
				} else {
					args[3] = verifFichierFormat(args[3], 1);
					SKOSToI2B2 loaderTable = new SKOSToI2B2(args[1]);
					parametre = new String[args.length - 2];
					for (int i = 0; i < args.length - 2; i++) {
						parametre[i] = args[i + 2];
					}
					loaderTable.loadSQL(parametre);
				}
				break;
			default:
				System.err.println("Choix d'action inconnu.");
				break;
			}
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

		// Vérification de la présence des répertoires de l'application
		checkRepertoire(repertoireOutput);
		checkRepertoire(repertoireErreur);
		checkRepertoire(repertoireParam);

		if (args.length == 0)
			menu(); // On ouvre le menu
		else
			checkArgs(args);
	}

	/**
	 * Cette méthode gère l'affichage du menu qui s'affiche lorsque l'application est lancée sans aucun paramètre.
	 */
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
	 * OWL.
	 */
	public static void menuSKOSToOWL() {
		menuTransco(1);
	}

	/**
	 * Cette méthode gère le menu correspondant à la transcodification OWL TO
	 * SKOS.
	 */
	public static void menuOWLToSKOS() {
		menuTransco(2);
	}

	/**
	 * Cette méthode gère le menu correspondant au chargement d'un fichier SKOS
	 * vers I2B2.
	 */
	public static void menuSKOSToI2B2() {
		String valeurSaisie;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
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
				if (valeurSaisie.trim().equals("5")) {
					System.exit(0);
				} else {
					if (valeurSaisie.trim().equals("1")
							|| valeurSaisie.trim().equals("2")
							|| valeurSaisie.trim().equals("3")
							|| valeurSaisie.trim().equals("4")) {
						genererSortie(Integer.parseInt(valeurSaisie));
						break;
					} else {
						System.out.println("Valeur saisie incorrecte.");
					}

				}
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * Cette méthode gère les générations de fichiers à partir de SKOS ou le chargement en base.
	 * @param typeFichier 1 : fichier CSV, 2 : fichier SQL, 3 : SQLLoader, 4 : Alimentation table SQL
	 */
	public static void genererSortie(int typeFichier) {
		String fichierEntree = null;
		String fichierSortie = null;
		String fichierFormat = null;
		String fichierCSV = null;
		String separateur = null;
		String nomTable = null;
		String methodeChargement = null;

		System.out.println("Nom du fichier en entrée :");
		fichierEntree = checkFile(1);
		if (typeFichier != 4) {
			System.out.println("Nom du fichier en sortie :");
			fichierSortie = checkFile(2);
		}
		if (typeFichier == 3) {
			System.out.println("Nom du fichier CSV à générer :");
			fichierCSV = checkFile(2);
		}
		if (typeFichier == 1 || typeFichier == 3) {
			System.out.println("Caractère de séparation (facultatif) :");
			separateur = choixUtilisateur();
		}
		if (typeFichier == 2 || typeFichier == 3) {
			System.out.println("Nom de la table SQL à charger :");
			nomTable = choixUtilisateur();
		}
		System.out.println("Nom du fichier format Metadata :");
		fichierFormat = checkFile(3);
		if (typeFichier == 4) {
			System.out
					.println("Méthode de chargement (1 : Insert / 2 : SQLLoader) :");
			methodeChargement = checkMethodeChargement();
		}
		SKOSToI2B2 loaderI2B2 = new SKOSToI2B2(fichierEntree);
		switch (typeFichier) {
		case 1:
			if (separateur == null || separateur.isEmpty())
				loaderI2B2.createCSV(fichierSortie, fichierFormat);
			else
				loaderI2B2.createCSV(fichierSortie, separateur, fichierFormat);
			break;
		case 2:
			loaderI2B2.createSQL(fichierSortie, nomTable, fichierFormat);
			break;
		case 3:
			if (separateur == null || separateur.isEmpty())
				loaderI2B2.createSQLLoader(fichierSortie, fichierCSV, nomTable,
						fichierFormat);
			else
				loaderI2B2.createSQLLoader(fichierSortie, fichierCSV,
						separateur, nomTable, fichierFormat);
			break;
		case 4:
			loaderI2B2
					.loadSQL(Integer.decode(methodeChargement), fichierFormat);
			break;
		default:
			System.err.println("Erreur : Type de sortie à générer inconnu.");
			break;
		}
	}

	/**
	 * Cette méthode permet d'afficher le menu de saisie des informations de transcodage
	 * @param typeTransco 1 : SKOS To OWL 2 : OWL To SKOS
	 */
	public static void menuTransco(int typeTransco) {
		if (typeTransco == 1)
			System.out
					.println("===================[     MENU SKOS TO OWL    ]======================");
		else
			System.out
					.println("===================[     MENU OWL TO SKOS    ]======================");
		System.out.println("Nom du fichier en entrée :");
		String fichierEntree = checkFile(1);
		System.out.println("Nom du fichier en sortie :");
		String fichierSortie = checkFile(2);
		System.out.println("Nom de l'IRI (facultatif) :");
		String iri = choixUtilisateur();
		iri = verifIRI(iri);
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
	 * @param typeFichier
	 *            Type de fichier traité : 1 - fichier d'entrée, 2 - Fichier de
	 *            sortie, 3 - Fichier de paramétrage
	 */
	public static String checkFile(int typeFichier) {
		String fichierEntree = null;
		do {
			fichierEntree = choixUtilisateur();
			if (fichierEntree == null || fichierEntree.trim().isEmpty()) {
				System.out.println("Veuillez saisir un fichier à lire.");
				System.out.println("Nom du fichier :");
			} else {
				if (typeFichier == 1) { // fichier entrée
					if (!new File(fichierEntree).exists()) {
						System.out.println("Le fichier saisie n'existe pas.");
						System.out
								.println("Veuillez-saisir de nouveau le nom du fichier.");
						fichierEntree = null;
					}
				} else {
					if (typeFichier == 2) { // fichier Sortie
						// Vérifie si le fichier est écrit en chemin absolu et
						// si le
						// chemin
						// existe sinon on écrit dans HOME/I2B2/OUTPUT/
						fichierEntree = verifFichierSortie(fichierEntree, 2);
					} else { // fichier param
						fichierEntree = verifFichierFormat(fichierEntree, 2);
					}
				}
			}
		} while (fichierEntree == null || fichierEntree.trim().isEmpty());
		return fichierEntree;
	}

	/**
	 * Cette méthode effectue la récupération du choix de l'utilisateur
	 * concernant la méthode de chargement et vérifie que le mode de chargement
	 * saisie par l'utilisateur vaut bien 1 ou 2. Si ce n'est pas le cas, un
	 * message indique l'erreur à l'utilisateur et lui demande de saisie de
	 * nouveau une valeur jusqu'à avoir une valeur correcte.
	 * 
	 * @return la valeur saisie par l'utilisateur.
	 */
	public static String checkMethodeChargement() {
		String valeur = choixUtilisateur();
		// Vérifie que la valeur vaut 1 ou 2
		while (!(valeur.trim().equals("1") || valeur.trim().equals("2"))) {
			System.out
					.println("Valeur saisie incorrecte. Valeur possible : 1 - Insert / 2 - SQL Loader.");
			valeur = choixUtilisateur();
		}
		return valeur;
	}

	/**
	 * Cette méthode gère la récupération des données saisies par l'utilisateur
	 * (prompt).
	 * 
	 * @return La valeur saisie par l'utilisateur.
	 */
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

	/**
	 * Cette méthode vérifie que l'IRI fini par un caractère /. Si ce n'est pas
	 * le cas, le caractère est ajouté.
	 * 
	 * @return L'IRI éventuellement commplétée de /
	 */
	public static String verifIRI(String iri) {
		String sortie = "/";

		if (!(iri == null || iri.isEmpty())
				&& !iri.substring(iri.length() - 1, iri.length()).equals("/")) {
			return iri + sortie;
		} else
			return iri;
	}

	/**
	 * Cette méthode permet de vérifier la cohérence du fichier saisie Si le
	 * chemin est incorrect, une erreur est remontée. Si seul le nom d'un
	 * fichier est indiqué, le fichier sera créé dans le home del'utilsiateur
	 * dans le répertoire I2B2/OUTPUT/
	 * 
	 * @param fichierSortie
	 *            Nom absolu du fichier de sortie typeAppel : 1 - ligne de
	 *            commande, 2 - menu
	 * @return Le chemin absolu du fichier. Soit celui saisie par l'utilisateur
	 *         si correcte, soit celui correspondant à
	 *         HOME/I2B2/OUTPUT/fichierSortie si seul le nom du fichier a été
	 *         saisie, soit arrêt du programme si chemin absolu incorrecte.
	 */
	public static String verifFichierSortie(String fichierSortie, int typeAppel) {

		String sortie = "";
		// On regarde si le nom comporte des caractères séparateur
		if (fichierSortie.contains(FILE_SEPARATOR)) {
			// L'utilisateur a entré un chemin absolu
			// On vérifie si le chemin exist
			// String nomFichier =
			// fichierSortie.substring(fichierSortie.lastIndexOf(System.getProperty("file.separator"))+1);
			String cheminFichier = fichierSortie.substring(0,
					fichierSortie.lastIndexOf(FILE_SEPARATOR) + 1);
			// Si le chemin n'existe pas, on remonte une erreur
			if (!new File(cheminFichier).exists()) {
				if (typeAppel == 1) {
					System.err
							.println("Le chemin du fichier de sortie indiqué ("
									+ fichierSortie + ") n'existe pas.");
					System.exit(1);
				} else {
					System.out
							.println("Le chemin du fichier de sortie indiqué ("
									+ fichierSortie + ") n'existe pas.");
					System.out
							.println("Saisissez de nouveau le nom du fichier : ");
					sortie = checkFile(2);
				}
			} else {
				sortie = fichierSortie;
			}
		} else {
			// Ce n'est pas un chemin absolu
			// On crée le chemin absolu pour écrire dans le répertoire
			// HOME/I2B2/OUTPUT/
			checkRepertoire(repertoireOutput);
			sortie = repertoireOutput + fichierSortie;
		}
		return sortie;
	}

	/**
	 * Cette méthode vérifie que le répertoire en entrée existe. Si ce n'est pas
	 * le cas, il est créée.
	 * 
	 * @param repertoire
	 */
	public static void checkRepertoire(String repertoire) {
		if (!new File(repertoire).exists()) {
			// Créer le dossier avec tous ses parents
			new File(repertoire).mkdirs();
		}
	}

	/**
	 * Cette méthode vérifie si la variable fichierFormat contient un fichier ou
	 * un type de base de données (Oracle, PostgreSQL, MySQL). Si le type d'une
	 * base est en entrée, fichierFormat sera écrasé par le nom du fichier de
	 * paramétrage associé par défaut.
	 * 
	 * @param fichierFormat
	 *            : nom de fichier ou type de base de données. typeAppel : 1 -
	 *            ligne de commande, 2 - menu
	 * @return Nom du fichier Format
	 */
	public static String verifFichierFormat(String fichierFormat, int typeAppel) {

		if (fichierFormat.equalsIgnoreCase("Oracle")) {
			fichierFormat = fichierParamOracle;
		} else if (fichierFormat.equalsIgnoreCase("MySQL")) {
			fichierFormat = fichierParamMySQL;
		} else if (fichierFormat.equalsIgnoreCase("PostgreSQL")) {
			fichierFormat = fichierParamPostgreSQL;
		}

		if (!new File(fichierFormat).exists()) {
			if (typeAppel == 1) {
				System.err.println("Erreur : Le fichier indiqué en entrée ("
						+ fichierFormat + ") n'existe pas.");

				System.exit(1);
			} else {
				System.out.println("Erreur : Le fichier indiqué en entrée ("
						+ fichierFormat + ") n'existe pas.");
				System.out
						.println("Veuillez saisir de nouveau le format (fichier de paramétrage ou type de BDD).");
				fichierFormat = checkFile(3);
			}
		}
		return fichierFormat;
	}
}
