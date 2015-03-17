package load;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import transco.Builder;

/**
 * Cette classe gère le chargement d'une hiérarchie SKOS au sein de la table
 * Metadata d'I2B2.
 * 
 * @author Yoann Keravec Date: 13/03/2015<br>
 *         Institut Bergonié<br>
 */
public class SKOSToI2B2Builder extends Builder {

	private List<Metadata> listeMetadata = new ArrayList<Metadata>();
	private PrefixManager prefixOnto;
	private String iriSKOS = "http://www.w3.org/2004/02/skos/core#";
	private PrefixManager prefixSKOS = new DefaultPrefixManager(iriSKOS);

	/**
	 * Constructeur de la classe SKOSToI2B2Builder.
	 * 
	 * @param ontologie
	 *            Ontologie d'origine (OWL) @see OWLOntology.
	 */
	public SKOSToI2B2Builder(OWLOntology ontologie) {
		this.originalOntology = ontologie;
	}

	/*
	 * Principe de la classe Chargement du fichier en entrée à l'aide de la
	 * classe OWLReader Recherche du ConceptScheme pour initialiser le niveau 0
	 * Recherche au sein de l'ontologie du concept "I2B2" Traitement récursif
	 * sur les fils pour créer les enregistrements => méthode dédiée
	 */

	/**
	 * Cette méthode permet de créer un enregistrement Metadata et de le stocker
	 * dans la liste d'enregistrement de la classe
	 */
	public Metadata createMetadataRecord(OWLIndividual individu, int niveau) {
		Metadata metadata = new Metadata();
		metadata.setcHLevel(niveau);
		metadata.setcFullName(individu.asOWLNamedIndividual().getIRI().toString());
		metadata.setcName(getPrefLabel(individu));
		System.out.println("HLEVEL : " + niveau + " FullName : " + individu.asOWLNamedIndividual().getIRI().toString() + " Name : " + getPrefLabel(individu));

		return metadata;
	}

	/**
	 * Cette méthode recherche les ConceptScheme. Pour chaque valeur retrouvé,
	 * un enregistrement de niveau 0 est créée.
	 */
	public void findAndCreateConceptScheme(int niveau) {

		// On recherche tous les ConceptScheme et pour chacun, on crée un
		// enregistrement
		// List<OWLNamedIndividual> scheme = fact.get

		// On récupère la liste d'individu de type ConceptScheme
		List<OWLIndividual> listeIndiv = findSchemeByObjectProperty(
				"ConceptScheme", prefixSKOS);

		// Pour chaque occurence on crée un record
		for (OWLIndividual curIndiv : listeIndiv) {
			createRecord(curIndiv, niveau);
		}

	}

	

	/**
	 * Cette méthode récursive permet de créer les enregistrements
	 * correspondants aux fils du concept en entrée
	 */
	public void creationEnregistrement(OWLNamedIndividual individu, int niveau) {

	}

	public void load() {

		System.out.println("Création des niveaux 0.");
		// Chargement d'un résoneur pour pouvoir trouver les broader mais
		// également les narrower

		// Recherche des ConceptScheme et création au niveau 0
		int niveau = 0;
		findAndCreateConceptScheme(niveau);

		System.out.println("Création des niveaux 1 et supérieur.");

		// Recherche du Concept I2B2 et créations des enregistrements à partir
		// de ce concept
		// Set<OWLNamedIndividual> listeIndiv = ontology
		// .getIndividualsInSignature();

		// On recherche l'élément Thing et on parcours ses fils

		// On recherche l'IRI de l'ontologie
		IRI iriProject = foundIriProjectByIndividual();
		// On crée le préfixe associé
		if (iriProject != null) {
			prefixOnto = new DefaultPrefixManager(iriProject.toString() + "#");
			// On cherche l'individu correspondant à #2 (SarcomaBcb)

			OWLNamedIndividual indivI2B2 = rechercheIndividu("2",
					iriProject.toString() + "#");

			// OWLNamedIndividual indivI2B2 =
			// fact.getOWLNamedIndividual("1",prefixOnto);

			// On parcours les narrower de cet individu et on crée un
			// enregistrement
			// de niveau inférieur
			List<OWLIndividual> listeNarrowers = getNarrowers(indivI2B2);

			// creation des enregistrement
			if (listeNarrowers != null && !listeNarrowers.isEmpty())
				createNarrowers(listeNarrowers, niveau);
		} else {
			System.out
					.println("L'IRI du projet n'a pu être trouvée. Arrêt des traitements.");
		}
	}

	public void createNarrowers(List<OWLIndividual> listeIndiv, int niveau) {
		// OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		// OWLDataFactory fact = manager.getOWLDataFactory();

		for (OWLIndividual curseurIndiv : listeIndiv) {
			createRecord(curseurIndiv, niveau + 1);
			List<OWLIndividual> listeIndiv2 = getNarrowers(curseurIndiv);
			if (listeIndiv2 != null && !listeIndiv2.isEmpty())
				createNarrowers(listeIndiv2, niveau + 1);
		}
	}

	/**
	 * Cette methode crée un enregistrement.
	 * 
	 * @param niveau
	 */
	public void createRecord(OWLIndividual individu, int niveau) {

		//System.out.println("Création de l'enregistrement : " + individu
		//		+ " au niveau " + niveau + ".");
		// Création des metadata
		listeMetadata.add(createMetadataRecord(individu, niveau));

	}
	
	/**
	 * Cette méthode permet de liste tous les ObjectProperty de l'ontologie.
	 */
	public void listOP() {
		System.out.println("OP : " + originalOntology.getObjectPropertiesInSignature());
	}
	
	
	
}
