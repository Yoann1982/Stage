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
 *         Institut Bergoni�<br>
 */
public class Principale {

	/**
	 * Cette m�thode permet d'effectuer le transcodage d'un fichier SKOS en OWL.
	 * 
	 * @param input
	 *            Fichier en entr�e.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void skosToOWL(String input, String output) {

		// On lit le fichier qui alimente une structure de données interne
		SKOSReader reader = new SKOSReader();
		reader.loadFile(input);

		// On cr�e une instance de l'objet qui va permettre de cr�er une
		// ontologie � partir de la structure de donn�es.
		OWLOntologyBuilder builder = new OWLOntologyBuilder();

		// On parcours la structure de donn�es pour alimenter l'ontologie cr��e.
		for (ConceptSKOS conceptCur : reader.getListConceptSKOS()) {
			builder.createClass(conceptCur);
		}

		WriteOntology fileOntoWriter = new WriteOntology(builder.getOntology());
		fileOntoWriter.writeFile(output);
	}

	// END skosToOWL

	/**
	 * Cette m�thode permet de transcoder un fichier OWL en SKOS.
	 * 
	 * @param input
	 *            Fichier en entr�e.
	 * @param output
	 *            Fichier en sortie.
	 */
	public static void OWLToSkos(String input, String output) {

		OWLReader fileOntoRead = new OWLReader();
		fileOntoRead.chargeOntology(input);

		String ontoExterne = "http://www.w3.org/TR/skos-reference/skos-owl1-dl.rdf";
		Importer importer = new Importer(fileOntoRead.getOntology());
		importer.importOnto(ontoExterne);

		// On cr�e les objets SKOS
		SKOSBuilder skosBuilder = new SKOSBuilder(fileOntoRead.getOntology());
		skosBuilder.creeSKOSOntologie();
		// On importe l'ontologie SKOS dans l'ontologie cible
		Importer importerSKOS = new Importer(skosBuilder.getTargetOntology());
		importerSKOS.importOnto(ontoExterne);

		WriteOntology fileOntoWriterOnto = new WriteOntology(
				skosBuilder.getTargetOntology());
		fileOntoWriterOnto.writeFile(output);

	}

	public static void afficheMessageErreur() {
		System.err
				.println("Argument 1 : type de transcodage : 1 : skostoowl ou 2 : owltoskos");
		System.err.println("Argument 2 : Nom du fichier en entr�e");
		System.err.println("Argument 3 : Nom du fichier en sortie");
	}

	/**
	 * La main prend trois argument: Argument 1 : type de transcodage : 1 :
	 * skostoowl ou 2 : owltoskos Argument 2 : Nom du fichier en entr�e Argument
	 * 3 : Nom du fichier en sortie
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// On v�rifie que l'on a le bon nombre d'argument
		if (args.length != 3) {
			System.err
					.println("Erreur : Nombre d'arguments en entr�e incorrect.");
			System.err.println("Vous devez saisir 3 arguments :");
			afficheMessageErreur();
			System.exit(1);
		} else {
			
			System.out.println("Type de transcodage : " + args[0]);
			System.out.println("Fichier en entr�e : " + args[1]);
			System.out.println("Fichier en sortie : " + args[2]);
			if (!(args[0].equals("1") || args[0].equals("2"))) {
				System.err
						.println("La valeur du premier argument est incorrect : ");
				afficheMessageErreur();
			} else {
				if (args[0].equals("1")) {
					// Transcodage SKOS To OLW
					// Version Linux
					// String nomFichier = "file:/home/yoann/BERGONIE/canals.skos";
					// Version Windows
					//String nomFichier = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\canals.skos";

					// Version Linux
					// String nomFichierSortie =
					// "/home/yoann/BERGONIE/OUT/ontologie.owl";
					// Version Windows
					//String nomFichierSortie = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\ontologie.owl";

					skosToOWL(args[1], args[2]);
					
				} else {
					// Transcodage OWL To SKOS
					// Partie chargement ontologie, import & �criture

					//String nomFichierOnto = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OWL\\bcbsarcoma_v3.owl";
					//String fichierSKOSOutput = "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\skosOutput.owl";
					OWLToSkos(args[1], args[2]);
				}
			}
		}
	}

}
