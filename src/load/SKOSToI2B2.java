package load;

import java.util.List;

import transco.OWLReader;

public class SKOSToI2B2 {

	public SKOSToI2B2(String inputFile) {
		// I2B2 test
		// Chargement du fichier SKOS
		OWLReader reader = new OWLReader();
		reader.loadOntology(inputFile);

		// On résonne
		Resonneur resonneur = new Resonneur(reader.getOntology());
		SKOSToI2B2Builder loader = new SKOSToI2B2Builder(
				resonneur.findPropertyAssertion());
		loader.setInputFile(inputFile);
		// On charge les données dans I2B2
		loader.load();

		// On exporte le résultat dans un CSV
		MetadataToCSV fichierCSV = new MetadataToCSV(
				"C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\metadata.csv",
				loader.getListeMetadata(),
				";",
				"C:\\Users\\y.keravec\\Documents\\BERGONIE\\SPECIFICATIONS\\formatMetadataTable.csv");

		// On exporte le résultat dans un SQL
		MetadataToSQL fichierSQL = new MetadataToSQL(
				"C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\metadata.sql",
				loader.getListeMetadata(),
				"Metadata",
				"C:\\Users\\y.keravec\\Documents\\BERGONIE\\SPECIFICATIONS\\formatMetadataTable.csv");

		// On exporte le résultat dans un SQLLoader
		
		MetadataToSQLLoader sqlLoader = new MetadataToSQLLoader(
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
}
