package load;


import transco.OWLReader;

public class SKOSToI2B2 {

	private SKOSToI2B2Builder loader;
	
	public SKOSToI2B2(String inputFile) {
		// I2B2 test
		// Chargement du fichier SKOS
		OWLReader reader = new OWLReader();
		reader.loadOntology(inputFile);

		// On résonne
		Resonneur resonneur = new Resonneur(reader.getOntology());
		loader = new SKOSToI2B2Builder(
				resonneur.findPropertyAssertion());
		loader.setInputFile(inputFile);
		// On charge les données dans I2B2
		loader.load();

				
		// On exporte le résultat dans un SQLLoader
		
		new MetadataToSQLLoader(
		"C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\metadata.ldr",
		"C:\\\\Users\\\\y.keravec\\\\Documents\\\\BERGONIE\\\\OUT\\\\metadataLoader.csv",
		";",
		loader.getListeMetadata(),
		"Metadata",
		"C:\\Users\\y.keravec\\Documents\\BERGONIE\\SPECIFICATIONS\\formatMetadataTable.csv");

		
		// On exporte le fichier SQL
		
		// WriteOntology fileOntoWriterOnto = new WriteOntology(
		// resonneur.findPropertyAssertion());

		// fileOntoWriterOnto.writeFile("C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\inferred.owl",
		// reader.getFormat());
	}
	
	public void createCSV(String fichierCSV, String separateur, String fichierFormat) {
		new MetadataToCSV(
				fichierCSV,
				loader.getListeMetadata(),
				separateur,
				fichierFormat);
	}
	
	public void createSQL(String fichierEntree, String nomTable, String fichierFormat) {
		new MetadataToSQL(
				fichierEntree,
				loader.getListeMetadata(),
				nomTable,
				fichierFormat);
	}
	
	public void createSQLLoader(String fichierSQL, String fichierCSV, String separateur, String nomTable, String fichierFormat) {
		new MetadataToSQLLoader(
				fichierSQL,
				fichierCSV,
				separateur,
				loader.getListeMetadata(),
				nomTable,
				fichierFormat);
	}
	
}
