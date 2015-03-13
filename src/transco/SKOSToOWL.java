package transco;

import org.semanticweb.owlapi.model.IRI;

/**
 * Cette classe gère le transcodage SKOS vers OWL
 * 
 * @author Yoann Keravec Date: 13/03/2015<br>
 *         Institut Bergonié<br>
 */
public class SKOSToOWL {

	/**
	 * Ce constructeur permet d'effectuer le transcodage d'un fichier SKOS en OWL.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public SKOSToOWL(String input, String output) {

		// On lit le fichier qui alimente une structure de données interne
		OWLReader reader = new OWLReader();
		reader.loadOntology(input);

		// On crée une instance de l'objet qui va permettre de créer une
		// ontologie à partir de la structure de données.
		OWLOntologyBuilder builder = new OWLOntologyBuilder(
				reader.getOntology());
		builder.createOntology();
		WriteOntology fileOntoWriter = new WriteOntology(
				builder.getTargetOntology());
		fileOntoWriter.writeFileRDFOWL(output, reader.getFormat());
	}

	/**
	 * Ce constructeur permet d'effectuer le transcodage d'un fichier SKOS en OWL.
	 * Cette méthode contient dans sa signature, une chaîne de caractères
	 * correspondant à l'IRI de l'ontologie à constuire.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 * @param iriOnto
	 *            Chaîne de caractères correspondant à l'IRI de l'ontologie à
	 *            construire. Exemple : http://bcbsarcoma/
	 * 
	 */
	public SKOSToOWL(String input, String output, String iriOnto) {

		// On lit le fichier qui alimente une structure de données interne
		OWLReader reader = new OWLReader();
		reader.loadOntology(input, iriOnto);

		// On crée une instance de l'objet qui va permettre de créer une
		// ontologie à partir de la structure de données.
		OWLOntologyBuilder builder = new OWLOntologyBuilder(
				reader.getOntology());
		builder.createOntology(IRI.create(iriOnto));
		WriteOntology fileOntoWriter = new WriteOntology(
				builder.getTargetOntology());
		fileOntoWriter.writeFile(output, reader.getFormat());
	}	
}
