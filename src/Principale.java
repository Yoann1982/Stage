import transco.ConceptSKOS;
import transco.Importer;
import transco.OWLReader;
import transco.SKOSBuilder;
import transco.SKOSReader;
import transco.WriteOntology;
import transco.OWLOntologyBuilder;

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
	 * @param input Fichier en entrée.
	 * @param output Fichier en sortie.
	 */
	public static void skosToOWL(String input, String output) {

		// On lit le fichier qui alimente une structure de donnÃ©es interne
		SKOSReader reader = new SKOSReader();
		reader.loadFile(input);

		// On crée une instance de l'objet qui va permettre de créer une
		// ontologie à partir de la structure de données.
		OWLOntologyBuilder builder = new OWLOntologyBuilder();

		// On parcours la structure de données pour alimenter l'ontologie créée.
		for (ConceptSKOS conceptCur : reader.getListConceptSKOS()) {
			builder.createClass(conceptCur);
		}

		WriteOntology fileOntoWriter = new WriteOntology(builder.getOntology());
		fileOntoWriter.writeFile(output);
	}
	// END skosToOWL

	/**
	 * Cette méthode permet de transcoder un fichier OWL en SKOS.
	 * @param input Fichier en entrée.
	 * @param output Fichier en sortie.
	 */
	public static void OWLToSkos(String input, String output) {
		
		OWLReader fileOntoRead = new OWLReader();
		fileOntoRead.chargeOntology(input);

		String ontoExterne = "http://www.w3.org/TR/skos-reference/skos-owl1-dl.rdf";
		Importer importer = new Importer(fileOntoRead.getOntology());
		importer.importOnto(ontoExterne);

		// On crée les objets SKOS
		SKOSBuilder skosBuilder = new SKOSBuilder(fileOntoRead.getOntology());
		skosBuilder.creeSKOSOntologie();
		// On importe l'ontologie SKOS dans l'ontologie cible
		Importer importerSKOS = new Importer(skosBuilder.getTargetOntology());
		importerSKOS.importOnto(ontoExterne);

		WriteOntology fileOntoWriterOnto = new WriteOntology(
				skosBuilder.getTargetOntology());
		fileOntoWriterOnto.writeFile(output);

	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Version Linux
		// String nomFichier = "file:/home/yoann/BERGONIE/canals.skos";
		// Version Windows
		String nomFichier = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\canals.skos";

		// Version Linux
		// String nomFichierSortie = "/home/yoann/BERGONIE/OUT/ontologie.owl";
		// Version Windows
		String nomFichierSortie = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\ontologie.owl";

		skosToOWL(nomFichier, nomFichierSortie);

		// Partie chargement ontologie, import & écriture

		String nomFichierOnto = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OWL\\bcbsarcoma_v3.owl";
		String fichierSKOSOutput = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\skosOutput.owl";
		OWLToSkos(nomFichierOnto, fichierSKOSOutput);
	}

}
