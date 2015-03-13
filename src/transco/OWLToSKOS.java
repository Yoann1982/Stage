package transco;

/**
 * Cette classe gère le transcodage OWL vers SKOS
 * 
 * @author Yoann Keravec Date: 13/03/2015<br>
 *         Institut Bergonié<br>
 */
public class OWLToSKOS {

	/**
	 * Cette méthode permet de construire une ontologie à partir d'un OWLReader
	 * et une chaîne de caractères correspondant au chemin absolu du fichier de
	 * sortie.
	 * 
	 * @param reader
	 * @see OWLReader. Objet contenant le contenu de l'ontologie à transcrire
	 * @param output
	 *            Chaîne de caractères correspondant au chemin absolu du fichier
	 *            de sortie.
	 */
	private static void buildOnto(OWLReader reader, String output) {
		String ontoExterne = "http://www.w3.org/TR/skos-reference/skos-owl1-dl.rdf";
		Importer importer = new Importer(reader.getOntology());
		importer.importOnto(ontoExterne);

		// On crée les objets SKOS
		SKOSBuilder skosBuilder = new SKOSBuilder(reader.getOntology());
		skosBuilder.setFormat(reader.getFormat());
		skosBuilder.createSKOSOntologie();
		// On importe l'ontologie SKOS dans l'ontologie cible
		Importer importerSKOS = new Importer(skosBuilder.getTargetOntology());
		importerSKOS.importOnto(ontoExterne);

		WriteOntology fileOntoWriterOnto = new WriteOntology(
				importerSKOS.getOntology());
		fileOntoWriterOnto.writeFile(output, skosBuilder.getFormat());
	}

	/**
	 * Ce constructeur permet de transcoder un fichier OWL en SKOS. Elle contient
	 * en paramètre d'entrée une chaîne de caractère correspondant à l'IRI de
	 * l'ontologie à constuire (ex : http://bcbsarcoma/) et le prefixe
	 * correspondant à cette ontologie (ex: bcb).
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 * @param iriOnto
	 *            Chaîne de caractères correspondant à l'IRI de l'ontologie à
	 *            constuire.
	 * @param prefix
	 *            Chaîne de caractères contenant la valeur du prefix
	 *            correspondant à l'IRI.
	 */
	public OWLToSKOS(String input, String output, String iriOnto, String prefix) {

		OWLReader reader = new OWLReader();
		reader.loadOntology(input, iriOnto, prefix);
		buildOnto(reader, output);
	}

	/**
	 * Ce constructeur permet de transcoder un fichier OWL en SKOS. Elle contient
	 * en paramètre d'entrée une chaîne de caractère correspondant à l'IRI de
	 * l'ontologie à constuire (ex : http://bcbsarcoma/).
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 * @param iriOnto
	 *            Chaîne de caractères correspondant à l'IRI de l'ontologie à
	 *            constuire.
	 */
	public OWLToSKOS(String input, String output, String iriOnto) {

		OWLReader reader = new OWLReader();
		reader.loadOntology(input, iriOnto);
		buildOnto(reader, output);
	}

	/**
	 * Ce constructeur permet de transcoder un fichier OWL en SKOS.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public OWLToSKOS(String input, String output) {

		OWLReader reader = new OWLReader();
		reader.loadOntology(input);
		buildOnto(reader, output);

	}

	
}
