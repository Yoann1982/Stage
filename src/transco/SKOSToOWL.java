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
	 * Ce constructeur permet d'effectuer le transcodage d'un fichier SKOS en
	 * OWL.
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

		buildOnto(reader, output, null);
	}

	public SKOSToOWL(String[] parametre) {

		switch (parametre.length) {

		case 2:
			new SKOSToOWL(parametre[0], parametre[1]);
			break;
		case 3:
			new SKOSToOWL(parametre[0], parametre[1], parametre[2]);
			break;
		case 4:
			new SKOSToOWL(parametre[0], parametre[1], parametre[2],
					parametre[3]);
			break;
		default:
			System.err.println("Erreur : nombre de paramètres incorrect.");
			break;

		}

	}

	/**
	 * Ce constructeur permet d'effectuer le transcodage d'un fichier SKOS en
	 * OWL. Cette méthode contient dans sa signature, une chaîne de caractères
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
		buildOnto(reader, output, iriOnto);
	}

	public SKOSToOWL(String input, String output, String iriOnto, String prefix) {

		OWLReader reader = new OWLReader();
		reader.loadOntology(input, iriOnto, prefix);
		buildOnto(reader, output, iriOnto);
	}

	public void buildOnto(OWLReader reader, String output, String iriProject) {

		// On crée une instance de l'objet qui va permettre de créer une
		// ontologie à partir de la structure de données.
		OWLOntologyBuilder builder = new OWLOntologyBuilder(
				reader.getOntology());
		if (iriProject == null)
			builder.createOntology();
		else
			builder.createOntology(IRI.create(iriProject));
		WriteOntology fileOntoWriter = new WriteOntology(
				builder.getTargetOntology());
		fileOntoWriter.writeFile(output, reader.getFormat());
	}

}
