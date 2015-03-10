import java.util.Set;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;

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

	// END skosToOWL

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

	public static OWLReader loadOWLFile(String input) {
		OWLReader fileOntoRead = new OWLReader();
		fileOntoRead.loadOntology(input);
		return fileOntoRead;
	}

	public static void readContentOfOntology(OWLOntology onto) {

		Set<OWLNamedIndividual> listClassOnto = onto
				.getIndividualsInSignature();

		for (OWLNamedIndividual cls : listClassOnto) {
			System.out.println("INDIVIDU : " + cls.toString());
			Set<OWLAnnotation> listeAnnot = cls.getAnnotations(onto);
			Set<OWLAxiom> listeAxiom = cls.getReferencingAxioms(onto);
			for (OWLAxiom curAxiom : listeAxiom) {
				System.out.println("Axiom : " + curAxiom.toString());
				System.out.println("Axiom Type : " + curAxiom.getAxiomType());
				System.out.println("Axiom Class : " + curAxiom.getClass());
				System.out.println("Signature : "
						+ curAxiom.getSignature().size());
				Set<OWLEntity> listeEntity = curAxiom.getSignature();
				IRI iriEntity = null;
				for (OWLEntity curEntity : listeEntity) {
					System.out.println("ENTITY : " + curEntity.toString());
					System.out.println("Type de l'entité : "
							+ curEntity.getEntityType());
					String typeEntity = curEntity.getEntityType().toString();
					switch (typeEntity) {
					case "ObjectProperty":
						// On crée un objet ObjectProperty
						iriEntity = curEntity.asOWLObjectProperty().getIRI();
						System.out.println("IRI : " + iriEntity);
						// En fonction du code ...
						break;
					case "NamedIndividual":
						// On récupère l'IRI de l'entité NamedIndividual
						iriEntity = curEntity.asOWLNamedIndividual().getIRI();
						// On crée une classe ayant cet IRI
						System.out.println("IRI : " + iriEntity);
						break;
					default:
						System.out.println("Le type " + typeEntity
								+ " n'est pas supporté.");
						break;
					}
				}

				Set<OWLObjectProperty> listeObProp = curAxiom
						.getObjectPropertiesInSignature();

				for (OWLObjectProperty curOb : listeObProp) {
					System.out.println("Obj Prop : " + curOb);
					// System.out.println("Domain : " + curOb.);
				}
			}
		}

		Set<OWLClass> listeClasse = onto.getClassesInSignature();
		System.out.println("Taille Classe : " + listeClasse.size());
		for (OWLClass curClass : listeClasse) {
			System.out.println("Classe ; " + curClass.asOWLClass());
			Set<OWLNamedIndividual> listeIndiv = curClass.asOWLClass()
					.getIndividualsInSignature();
			for (OWLNamedIndividual clsIndiv : listeIndiv) {
				Set<OWLAnnotation> listeAnnot = clsIndiv.getAnnotations(onto);
				System.out.println("Taille liste annot : " + listeAnnot.size());
				for (OWLAnnotation curAnnot : listeAnnot) {
					System.out.println("Property ; "
							+ curAnnot.getProperty().toString());
					System.out.println("Value ; "
							+ curAnnot.getValue().toString());
				}
			}
		}

	}

	/**
	 * Cette méthode permet d'afficher un rappel sur les arguments à utiliser en
	 * entrée
	 */
	public static void afficheMessageErreur() {
		System.err
				.println("Argument 1 : type de transcodage : 1 : skostoowl ou 2 : owltoskos");
		System.err.println("Argument 2 : Nom du fichier en entrée");
		System.err.println("Argument 3 : Nom du fichier en sortie");
	}

	/**
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
					// Transcodage SKOS To OLW
					// Version Linux
					// String nomFichier =
					// "file:/home/yoann/BERGONIE/canals.skos";
					// Version Windows
					// String nomFichier =
					// "C:\\Users\\y.keravec\\Documents\\BERGONIE\\canals.skos";

					// Version Linux
					// String nomFichierSortie =
					// "/home/yoann/BERGONIE/OUT/ontologie.owl";
					// Version Windows
					// String nomFichierSortie =
					// "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\ontologie.owl";

					skosToOWL2(args[1], args[2]);
					// readContentOfOntology(loadOWLFile(args[1]).getOntology());

				} else {
					// Transcodage OWL To SKOS
					// Partie chargement ontologie, import & écriture

					// String nomFichierOnto =
					// "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OWL\\bcbsarcoma_v3.owl";
					// String fichierSKOSOutput =
					// "C:\\Users\\y.keravec\\Documents\\BERGONIE\\OUT\\skosOutput.owl";
					OWLToSkos(args[1], args[2]);
				}
			}
		}
	}

}
