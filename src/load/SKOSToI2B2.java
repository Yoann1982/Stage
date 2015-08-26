package load;

import java.io.File;
import java.io.IOException;

import transco.OWLReader;

/**
 * Cette classe contient les méthodes permettant un chargement de fichier SKOS vers une structur I2B2, soit par fichier CSV, fichier SQL, SQLLoader ou chargement direct en table.
 * 
 * @author Yoann Keravec <br>
 *         Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class SKOSToI2B2 {

	private SKOSToI2B2Builder loader;

	/**
	 * Constructeur
	 * @param inputFile Fichier SKOS à charger.
	 * @param conceptStart Concept de départ.
	 * @param niveau Niveau de départ.
	 */
	public SKOSToI2B2(String inputFile, String conceptStart, int niveau) {
		// I2B2 test
		// Chargement du fichier SKOS

		OWLReader reader = new OWLReader();
		reader.loadOntology(inputFile);

		// On résonne
		Raisonneur resonneur = new Raisonneur(reader.getOntology());
		loader = new SKOSToI2B2Builder(resonneur.findPropertyAssertion());
		loader.setInputFile(inputFile);
		// On charge les données dans I2B2
		loader.load(conceptStart, niveau);
	}

	/**
	 * Cette méthode crée un fichier CSV à partir d'un tableau de paramètre.
	 * @param parametre
	 */
	public void createCSV(String[] parametre) {

		switch (parametre.length) {
		case 2:
			new MetadataToCSV(parametre[0], loader.getListeMetadata(),
					parametre[1]);
			break;
		case 3:
			new MetadataToCSV(parametre[0], loader.getListeMetadata(),
					parametre[1]);
			break;
		case 4 :
			new MetadataToCSV(parametre[0], loader.getListeMetadata(),
					parametre[3], parametre[1]);
			break;
		default:
			System.err.println("Erreur : nombre de paramètres incorrect.");
			break;
		}
	}

	/**
	 * Cette méthode crée un fichier CSV à partir d'une liste de Metadata. Le caractère séparateur est paramétrable.
	 * @param fichierCSV Fichier à générer.
	 * @param separateur Caractère séparateur du fichier CSV.
	 * @param fichierFormat Fichier décrivant le format de la table I2B2.
	 */
	public void createCSV(String fichierCSV, String separateur,
			String fichierFormat) {
		new MetadataToCSV(fichierCSV, loader.getListeMetadata(), separateur,
				fichierFormat);
	}

	/**
	 * Cette méthode crée un fichier CSV à partir d'une liste de Metadata. Le caractère séparateur est ";".
	 * @param fichierCSV
	 * @param fichierFormat
	 */
	public void createCSV(String fichierCSV, String fichierFormat) {
		new MetadataToCSV(fichierCSV, loader.getListeMetadata(), fichierFormat);
	}

	/**
	 * Cette méthode crée un fichier SQL (insert) à partir d'une liste de paramètre et d'une liste de Metadata.
	 * @param parametre
	 */
	public void createSQL(String[] parametre) {
		if (parametre.length == 3 || parametre.length == 4 )
			new MetadataToSQL(parametre[0], loader.getListeMetadata(),
					parametre[1], parametre[2]);
		else
			System.err.println("Erreur : nombre de paramètres incorrect.");
	}

	/**
	 * Cette méthode crée un fichier SQL (insert) à partir de paramètres d'entrée.
	 * @param fichierSortie Fichier à générer.
	 * @param nomTable Nom de la table à générer indiquer dans les ordres d'insert.
	 * @param fichierFormat Format de la table à alimenter.
	 */
	public void createSQL(String fichierSortie, String nomTable,
			String fichierFormat) {
		new MetadataToSQL(fichierSortie, loader.getListeMetadata(), nomTable,
				fichierFormat);
	}

	/**
	 * Cette méthode crée un fichier SQLLoader à partir d'une liste de paramètres.
	 * @param parametre
	 */
	public void createSQLLoader(String[] parametre) {
		switch (parametre.length) {

		case 4:
			new MetadataToSQLLoader(parametre[0], parametre[1],
					loader.getListeMetadata(), parametre[2], parametre[3]);
			break;
		case 5:
			new MetadataToSQLLoader(parametre[0], parametre[1],
					loader.getListeMetadata(), parametre[2], parametre[3]);
			break;
		case 6:
			new MetadataToSQLLoader(parametre[0], parametre[1], parametre[4],
					loader.getListeMetadata(), parametre[2], parametre[3]);
			break;
		default:
			System.err.println("Erreur : nombre de paramètres incorrect.");
			break;
		}
	}

	/**
	 * Cette méthode crée un fichier un fichier SQLLoader et son fichier CSV associé. Le caractère séparateur du fichier CSV est paramétrable.
	 * @param fichierSQL Fichier de contrôle à générer.
	 * @param fichierCSV Fichier CSV de chargement à générer.
	 * @param separateur Caractère séparateur du fichier CSV.
	 * @param nomTable Nom de la table à charger.
	 * @param fichierFormat Format de la table à charger.
	 */
	public void createSQLLoader(String fichierSQL, String fichierCSV,
			String separateur, String nomTable, String fichierFormat) {
		new MetadataToSQLLoader(fichierSQL, fichierCSV, separateur,
				loader.getListeMetadata(), nomTable, fichierFormat);
	}

	/**
	 * Cette méthode crée un fichier SQLLoader et son fichier CSV associé. Le caractère séparateur du fichier CSV est ";".
	 * @param fichierSQL
	 * @param fichierCSV
	 * @param nomTable
	 * @param fichierFormat
	 */
	public void createSQLLoader(String fichierSQL, String fichierCSV,
			String nomTable, String fichierFormat) {
		new MetadataToSQLLoader(fichierSQL, fichierCSV,
				loader.getListeMetadata(), nomTable, fichierFormat);
	}

	/**
	 * Cette méthode charge une table I2B2 à partir de Métadata.
	 * @param parametre
	 */
	public void loadSQL(String[] parametre) {
		loadSQL(Integer.decode(parametre[0]), parametre[1]);
	}
	public void loadSQL(int methodeChargement, String fichierFormat) {
		// Si methodeChargement == 1 : insert
		// Si methodeChargement == 2 : SQLLoader

		switch (methodeChargement) {

		case 1:
			// Methode par Insert
			// 1 On crée le fichier temporaire
			String name = "fichier";
			String ext = ".tmp";
			File tempFile = null;
			try {
				// Creer un fichier temporaire
				tempFile = File.createTempFile(name, ext);
				// Supprimer automatiquement
				tempFile.deleteOnExit();
				System.out.println("Ficher temporaire: ");
				System.out.println(tempFile.getCanonicalFile());
			} catch (IOException ex) {
				System.err
						.println("Erreur lors de la génération du fichier temporaire");
				ex.printStackTrace();
			}


			// Génération des données à inserer
			new MetadataToSQL(tempFile, loader.getListeMetadata(), "Metadata",
					fichierFormat);
			// Chargement en base de données
			new MetadataToDatabase(tempFile);
			break;
		case 2:
			String nameCTL = "control";
			String extCTL = ".ctl";
			File tempFileCTL = null;
			String nameCSV = "data";
			String extCSV = ".csv";
			File tempFileCSV = null;

			try {
				// Creer un fichier temporaire
				tempFileCTL = File.createTempFile(nameCTL, extCTL);
				// Supprimer automatiquement
				tempFileCTL.deleteOnExit();
				System.out.println("Ficher temporaire CTL : ");
				System.out.println(tempFileCTL.getCanonicalFile());
				// Creer un fichier temporaire
				tempFileCSV = File.createTempFile(nameCSV, extCSV);
				// Supprimer automatiquement
				tempFileCSV.deleteOnExit();
				System.out.println("Ficher temporaire CSV : ");
				System.out.println(tempFileCSV.getCanonicalFile());
			} catch (IOException ex) {
				System.err
						.println("Erreur lors de la génération du fichier temporaire");
				ex.printStackTrace();
			}


			// Génération des données à inserer
			new MetadataToSQLLoader(tempFileCTL, tempFileCSV, loader.getListeMetadata(),
					"Metadata", fichierFormat);
			
			// Chargement en base de données
			new MetadataToDatabase(nameCTL+extCTL);
			break;
		default:
			System.err.println("Erreur : Methode de chargement inconnue.");
			break;

		}
	}

}
