package load;

import transco.OWLReader;
import transco.SKOSToOWL;

public class SKOSToI2B2 {

	private SKOSToI2B2Builder loader;

	public SKOSToI2B2(String inputFile) {
		// I2B2 test
		// Chargement du fichier SKOS
		
		OWLReader reader = new OWLReader();
		reader.loadOntology(inputFile);

		// On résonne
		Resonneur resonneur = new Resonneur(reader.getOntology());
		loader = new SKOSToI2B2Builder(resonneur.findPropertyAssertion());
		loader.setInputFile(inputFile);
		// On charge les données dans I2B2
		loader.load();
	}

	public void createCSV(String[] parametre) {

		switch (parametre.length) {
		case 2:
			new MetadataToCSV(parametre[0], loader.getListeMetadata(), parametre[1]);
			break;
		case 3:
			new MetadataToCSV(parametre[0], loader.getListeMetadata(), parametre[1],
					parametre[2]);
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
		if (parametre.length == 3) 
			new MetadataToSQL(parametre[0], loader.getListeMetadata(), parametre[1],
					parametre[2]);
		else System.err.println("Erreur : nombre de paramètres incorrect.");
	}
	
	public void createSQL(String fichierEntree, String nomTable,
			String fichierFormat) {
		new MetadataToSQL(fichierEntree, loader.getListeMetadata(), nomTable,
				fichierFormat);
	}

	public void createSQLLoader(String[] parametre) {
		switch (parametre.length) {

		case 4:
			new MetadataToSQLLoader(parametre[0], parametre[1],
					loader.getListeMetadata(), parametre[2], parametre[3]);
			break;
		case 5:
			new MetadataToSQLLoader(parametre[0], parametre[1], parametre[2],
					loader.getListeMetadata(), parametre[3], parametre[4]);
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
		new MetadataToSQLLoader(fichierSQL, fichierCSV, loader.getListeMetadata(), nomTable, fichierFormat);
	}

}
