package load;

import transco.OWLReader;

public class SKOSToI2B2 {

	
	public SKOSToI2B2 (String inputFile) {
		//I2B2 test
				// Chargement du fichier SKOS
				OWLReader reader = new OWLReader();
				reader.loadOntology(inputFile);
								
				// On résonne
				Resonneur resonneur = new Resonneur(reader.getOntology());
				SKOSToI2B2Builder loader = new SKOSToI2B2Builder(resonneur.findPropertyAssertion());
				loader.setInputFile(inputFile);
				// On charge les données dans I2B2
				loader.load();
				
				// On exporte le fichier SQL
				
				//WriteOntology fileOntoWriterOnto = new WriteOntology(
				//		resonneur.findPropertyAssertion());
				
				//fileOntoWriterOnto.writeFile("C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\inferred.owl", reader.getFormat());
	}
	
}
