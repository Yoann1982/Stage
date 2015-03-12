import org.semanticweb.owlapi.model.IRI;

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
		fileOntoWriter.writeFile(output, reader.getFormat());
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
		fileOntoWriter.writeFileRDFOWL(output, reader.getFormat());
	}

	/**
	 * Cette méthode permet d'effectuer le transcodage d'un fichier SKOS en OWL.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void skosToOWL2(String input, String output, String iriOnto) {

		// On lit le fichier qui alimente une structure de données interne
		OWLReader reader = new OWLReader();
		reader.loadOntology(input, iriOnto);

		// On crée une instance de l'objet qui va permettre de créer une
		// ontologie à partir de la structure de données.
		OWLOntologyBuilderNoSKOSAPI builder = new OWLOntologyBuilderNoSKOSAPI(
				reader.getOntology());
		builder.createOntology(IRI.create(iriOnto));
		WriteOntology fileOntoWriter = new WriteOntology(
				builder.getTargetOntology());
		fileOntoWriter.writeFile(output, reader.getFormat());
	}

	// END skosToOWL2

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
		fileOntoWriterOnto.writeFileRDFOWL(output, skosBuilder.getFormat());
	}

	/**
	 * Cette méthode permet de transcoder un fichier OWL en SKOS.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void OWLToSkos(String input, String output, String iriOnto,
			String prefix) {

		OWLReader reader = new OWLReader();
		reader.loadOntology(input, iriOnto, prefix);
		buildOnto(reader, output);
	}

	/**
	 * Cette méthode permet de transcoder un fichier OWL en SKOS.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void OWLToSkos(String input, String output, String iriOnto) {

		OWLReader reader = new OWLReader();
		reader.loadOntology(input, iriOnto);
		buildOnto(reader, output);
	}

	/**
	 * Cette méthode permet de transcoder un fichier OWL en SKOS.
	 * 
	 * @param input
	 *            Fichier en entrée.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void OWLToSkos(String input, String output) {

		OWLReader reader = new OWLReader();
		reader.loadOntology(input);
		buildOnto(reader, output);

	}

	/**
	 * Cette méthode permet d'afficher un rappel sur les arguments à utiliser en
	 * entrée.
	 */
	public static void afficheMessageErreur() {
		System.err
				.println("Argument 1 obligatoire : type de transcodage : 1 : skostoowl ou 2 : owltoskos");
		System.err.println("Argument 2 obligatoire : Nom du fichier en entrée");
		System.err.println("Argument 3 obligatoire : Nom du fichier en sortie");
		System.err.println("Argument 4 facultatif : IRI de l'ontologie");
		System.err.println("Argument 5 facultatif : Prefix de l'ontologie");
	}

	/*
	 * La main prend trois argument: Argument 1 : type de transcodage : 1 :
	 * skostoowl ou 2 : owltoskos Argument 2 : Nom du fichier en entrée Argument
	 * 3 : Nom du fichier en sortie 4 : Iri de l'ontologie 5 : Prefix de
	 * l'ontologie
	 * 
	 * @param args
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// On vérifie que l'on a le bon nombre d'argument
		if (args.length < 3) {
			System.err
					.println("Erreur : Nombre d'arguments en entrée incorrect.");
			System.err
					.println("Vous devez saisir 3 arguments obligatoire et un argument facultatif :");
			afficheMessageErreur();
			System.exit(1);
		} else {

			if (args.length > 4) {
				System.out.println("Type de transcodage : " + args[0]);
				System.out.println("Fichier en entrée : " + args[1]);
				System.out.println("Fichier en sortie : " + args[2]);
				System.out.println("IRI de l'ontologie : " + args[3]);
				System.out.println("Prefix de l'ontologie : " + args[4]);
			} else {
				if (args.length > 3) {
					System.out.println("Type de transcodage : " + args[0]);
					System.out.println("Fichier en entrée : " + args[1]);
					System.out.println("Fichier en sortie : " + args[2]);
					System.out.println("IRI de l'ontologie : " + args[3]);
				} else {
					System.out.println("Type de transcodage : " + args[0]);
					System.out.println("Fichier en entrée : " + args[1]);
					System.out.println("Fichier en sortie : " + args[2]);
				}
			}
			if (!(args[0].equals("1") || args[0].equals("2"))) {
				System.err
						.println("La valeur du premier argument est incorrect : ");
				afficheMessageErreur();
			} else {
				if (args[0].equals("1")) {
					if (args.length < 4)
						skosToOWL2(args[1], args[2]);
					else
						skosToOWL2(args[1], args[2], args[3]);

				} else {
					if (args.length < 4)
						OWLToSkos(args[1], args[2]);
					else if (args.length < 5)
						OWLToSkos(args[1], args[2], args[3]);
					else
						OWLToSkos(args[1], args[2], args[3], args[4]);
				}
			}
		}
	}

}
