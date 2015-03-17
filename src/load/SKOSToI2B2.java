package load;

import transco.OWLReader;

public class SKOSToI2B2 {

	
	public SKOSToI2B2 () {
		//I2B2 test
				// Chargement du fichier SKOS
				OWLReader reader = new OWLReader();
				reader.loadOntology("C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\skosSortiePrefix.owl");
				//reader.loadOntology("C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\skosSortiePrefixLight.owl");
				//SKOSToI2B2Builder loader = new SKOSToI2B2Builder(reader.getOntology());
				//loader.load();
				
				// On résonne
				Resonneur resonneur = new Resonneur(reader.getOntology());
				//resonneur.testUnsatisfiableClasses(loader.getOriginalOntology());
				//resonneur.testUnsatisfiableClasses(resonneur.findOnIndividual());
				SKOSToI2B2Builder loader = new SKOSToI2B2Builder(resonneur.findPropertyAssertion());
				
				loader.load();
				//WriteOntology fileOntoWriterOnto = new WriteOntology(
				//		resonneur.findPropertyAssertion());
				
				//fileOntoWriterOnto.writeFile("C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\inferred.owl", reader.getFormat());
	}
	
}
