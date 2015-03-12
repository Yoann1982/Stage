import transco.ConceptSKOS;
import transco.Importer;
import transco.OWLOntologyBuilderNoSKOSAPI;
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
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void skosToOWL(String input, String output) {

		// On lit le fichier qui alimente une structure de données interne
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
	 * Cette méthode permet d'effectuer le transcodage d'un fichier SKOS en OWL.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void skosToOWL2(String input, String output) {

		// On lit le fichier qui alimente une structure de données interne
		OWLReader reader = new OWLReader();
		reader.loadOntology(input);

		// On crée une instance de l'objet qui va permettre de créer une
		// ontologie à partir de la structure de données.
		OWLOntologyBuilderNoSKOSAPI builder = new OWLOntologyBuilderNoSKOSAPI(
				reader.getOntology());
		builder.createOntology();
		WriteOntology fileOntoWriter = new WriteOntology(
				builder.getTargetOntology());
		fileOntoWriter.writeFile(output);
	}

	// END skosToOWL2

	/**
	 * Cette méthode permet de transcoder un fichier OWL en SKOS.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void OWLToSkos(String input, String output) {

		OWLReader fileOntoRead = new OWLReader();
		fileOntoRead.loadOntology(input);

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
				importerSKOS.getOntology());
		fileOntoWriterOnto.writeFile(output);

	}

	/**
	 * Cette méthode permet d'afficher un rappel sur les arguments à utiliser en
	 * entrée.
	 */
	public static void afficheMessageErreur() {
		System.err
				.println("Argument 1 : type de transcodage : 1 : skostoowl ou 2 : owltoskos");
		System.err.println("Argument 2 : Nom du fichier en entrée");
		System.err.println("Argument 3 : Nom du fichier en sortie");
	}

	/*
	 * La main prend trois argument: Argument 1 : type de transcodage : 1 :
	 * skostoowl ou 2 : owltoskos Argument 2 : Nom du fichier en entrée Argument
	 * 3 : Nom du fichier en sortie
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// On vérifie que l'on a le bon nombre d'argument
		if (args.length != 3) {
			System.err
					.println("Erreur : Nombre d'arguments en entrée incorrect.");
			System.err.println("Vous devez saisir 3 arguments :");
			afficheMessageErreur();
			System.exit(1);
		} else {

			System.out.println("Type de transcodage : " + args[0]);
			System.out.println("Fichier en entrée : " + args[1]);
			System.out.println("Fichier en sortie : " + args[2]);
			if (!(args[0].equals("1") || args[0].equals("2"))) {
				System.err
						.println("La valeur du premier argument est incorrect : ");
				afficheMessageErreur();
			} else {
				if (args[0].equals("1")) {
					skosToOWL2(args[1], args[2]);

				} else {
					OWLToSkos(args[1], args[2]);
				}
			}
		}
	}

}
