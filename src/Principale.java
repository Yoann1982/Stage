import load.Resonneur;
import load.SKOSToI2B2;
import load.SKOSToI2B2Builder;

import org.semanticweb.owlapi.model.IRI;

import transco.ConceptSKOS;
import transco.Importer;
import transco.OWLOntologyBuilder;
import transco.OWLReader;
import transco.OWLToSKOS;
import transco.SKOSBuilder;
import transco.SKOSReader;
import transco.SKOSToOWL;
import transco.WriteOntology;
import transco.OWLOntologyBuilderSKOSAPI;

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
		OWLOntologyBuilderSKOSAPI builder = new OWLOntologyBuilderSKOSAPI();

		// On parcours la structure de données pour alimenter l'ontologie créée.
		for (ConceptSKOS conceptCur : reader.getListConceptSKOS()) {
			builder.createClass(conceptCur);
		}

		WriteOntology fileOntoWriter = new WriteOntology(builder.getOntology());
		fileOntoWriter.writeFile(output, reader.getFormat());
	}

	// END skosToOWL

	/**
	 * Cette méthode permet d'afficher un rappel sur les arguments à utiliser en
	 * entrée.
	 */
	public static void afficheMessageErreur() {
		System.err
				.println("Argument 1 obligatoire : type de transcodage : 1 : skos to owl ou 2 : owl to skos ou 3 : skos to i2b2");
		System.err.println("Argument 2 obligatoire : Nom du fichier en entrée");
		System.err.println("Argument 3 obligatoire : Nom du fichier en sortie");
		System.err.println("Argument 4 facultatif : IRI de l'ontologie");
		System.err.println("Argument 5 facultatif : Prefix de l'ontologie");
	}

	public static void checkArgs(String[] args) {
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
		}
	}

	/**
	 * La main prend trois argument: \n Argument 1 : type de transcodage : 1 :
	 * skostoowl, 2 : owltoskos ou 3 : skostoi2b2 \n Argument 2 : Nom du fichier
	 * en entrée Argument \n 3 : Nom du fichier en sortie \n 4 : Iri de
	 * l'ontologie \n 5 : Prefix de l'ontologie \n
	 * 
	 * @param args
	 * 
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.err.println("Erreur : Saisissez les arguments en entrée.");
			System.err
					.println("Vous devez saisir 3 arguments obligatoire et un argument facultatif :");
			afficheMessageErreur();
		} else {
			switch (args[0]) {
			case "1":
				checkArgs(args);
				// SKOS TO OWL
				SKOSToOWL transcoSKOSToOWL;
				if (args.length < 4)
					transcoSKOSToOWL = new SKOSToOWL(args[1], args[2]);
				else
					transcoSKOSToOWL = new SKOSToOWL(args[1], args[2], args[3]);
				break;
			case "2":
				checkArgs(args);
				// OWL TO SKOS
				OWLToSKOS transcoOWLToSKOS;
				if (args.length < 4)
					transcoOWLToSKOS = new OWLToSKOS(args[1], args[2]);
				else if (args.length < 5)
					transcoOWLToSKOS = new OWLToSKOS(args[1], args[2], args[3]);
				else
					transcoOWLToSKOS = new OWLToSKOS(args[1], args[2], args[3],
							args[4]);
				break;
			case "3":
				// SKOS TO I2B2
				SKOSToI2B2 loadI2B2;
				if (args.length < 2)
					System.err.println("Nombre d'argument incorrect.");
				else {
					loadI2B2 = new SKOSToI2B2(args[1]);
				}
				break;
			default:
				System.err
						.println("La valeur du premier argument est incorrect : ");
				afficheMessageErreur();
				break;

			}
		}
	}
}
