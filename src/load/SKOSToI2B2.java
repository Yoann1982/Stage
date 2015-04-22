package load;

import java.io.File;
import java.io.IOException;

import transco.OWLReader;

public class SKOSToI2B2 {

	private SKOSToI2B2Builder loader;

	public SKOSToI2B2(String inputFile, String conceptStart, int niveau) {
		// I2B2 test
		// Chargement du fichier SKOS

		OWLReader reader = new OWLReader();
		reader.loadOntology(inputFile);

		// On résonne
		Resonneur resonneur = new Resonneur(reader.getOntology());
		loader = new SKOSToI2B2Builder(resonneur.findPropertyAssertion());
		loader.setInputFile(inputFile);
		// On charge les données dans I2B2
		loader.load(conceptStart, niveau);
	}

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

	public void createCSV(String fichierCSV, String separateur,
			String fichierFormat) {
		new MetadataToCSV(fichierCSV, loader.getListeMetadata(), separateur,
				fichierFormat);
	}

	public void createCSV(String fichierCSV, String fichierFormat) {
		new MetadataToCSV(fichierCSV, loader.getListeMetadata(), fichierFormat);
	}

	public void createSQL(String[] parametre) {
		if (parametre.length == 3 || parametre.length == 4 )
			new MetadataToSQL(parametre[0], loader.getListeMetadata(),
					parametre[1], parametre[2]);
		else
			System.err.println("Erreur : nombre de paramètres incorrect.");
	}

	public void createSQL(String fichierSortie, String nomTable,
			String fichierFormat) {
		new MetadataToSQL(fichierSortie, loader.getListeMetadata(), nomTable,
				fichierFormat);
	}

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

	public void createSQLLoader(String fichierSQL, String fichierCSV,
			String separateur, String nomTable, String fichierFormat) {
		new MetadataToSQLLoader(fichierSQL, fichierCSV, separateur,
				loader.getListeMetadata(), nomTable, fichierFormat);
	}

	public void createSQLLoader(String fichierSQL, String fichierCSV,
			String nomTable, String fichierFormat) {
		new MetadataToSQLLoader(fichierSQL, fichierCSV,
				loader.getListeMetadata(), nomTable, fichierFormat);
	}

	
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
