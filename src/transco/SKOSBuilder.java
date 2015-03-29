package transco;

import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * Cette classe permet de construire une hiérarchie SKOS à partir d'une
 * OWLOntology.
 * 
 * @author Yoann Keravec<br>
 *         Date: 09/03/2015<br>
 *         Institut Bergonié<br>
 */
public class SKOSBuilder extends Builder {

	/**
	 * Constructeur de la classe SKOSBuilder.
	 * 
	 * @param ontologie
	 *            Ontologie d'origine (OWL) @see OWLOntology.
	 */
	public SKOSBuilder(OWLOntology ontologie) {
		this.originalOntology = ontologie;
	}

	/**
	 * Cette méthode permet de créer l'ontologie SKOS cible à partir de
	 * l'ontologie OWL d'origine. L'IRI est récupérée à partir du contenu du
	 * fichier en entrée.
	 */
	public void createSKOSOntologie() {
		// On récupère l'iri de l'ontologie cible
		iriProject = foundIriProjectByClass();
		createSKOSOntologie(iriProject);
	}

	/**
	 * Cette méthode permet de créer l'ontologie SKOS cible à partir de
	 * l'ontologie OWL d'origine. L'IRI considéré est celle indiquée en
	 * paramètre d'entrée.
	 */
	public void createSKOSOntologie(IRI iriProject) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		IRI iriProjectDiese = IRI.create(iriProject.toString() + "#");

		addPrefix(iriProjectDiese, "bcbs");
		
		addPrefix(IRI.create(iriSKOS), "skos");
		//format.asPrefixOWLOntologyFormat().copyPrefixesFrom(prefixSKOS);

		// On initialise l'ontologie cible.
		initTargetOnto(iriProject);

		prefixOnto = new DefaultPrefixManager(iriProject.toString() + "#");

		OWLNamedIndividual scheme = addIndividual(prefixSKOS, "ConceptScheme",
				prefixOnto, "BCBSarcomes");

		Set<OWLClass> listClassOnto = originalOntology.getClassesInSignature();
		for (OWLClass cls : listClassOnto) {

			/*
			 * Pour chaque classe lue, on va : 1 - La transformer en Individual
			 * de SKOS:Concept 2 - Récupérer sa relation SubClassOf 3 -
			 * Récupérer la classe associée 4 - Transformer celle-ci en
			 * Individual de SKOS:Concept si pas déjà fait 5 - Créer une
			 * relation de type broader entre les deux concepts
			 */


			// On ne traite pas la classe Thing
			if (!fact.getOWLThing().getIRI().equals(cls.getIRI())) {

				// 1 - Transformation en SKOS:Concept

				// Si l'IRI n'est pas complète, c'est que l'entité correspond à
				// l'IRI de l'ontologie.
				// On lui attribut le prefix de l'ontologie
				// / SI l'IRI est complète, on conserve son IRI.

				OWLNamedIndividual individuConcept = addIndividual(prefixSKOS,
						"Concept", cls.getIRI());

				// 2 - On récupère les sous-classes de la classe référence
				// Si la liste à une taille de 0, c'est que la classe n'est mère
				// d'aucune autre
				Set<OWLClassExpression> listeClasseExpression = cls
						.getSubClasses(originalOntology);

				// Si la liste de sous classe est vide, on ne traite pas (c'est
				// que
				// Concept n'est père d'aucun autre concept
				// cls.getIRI().toURI().getFragment());

				if (listeClasseExpression.size() != 0) {

					// On va pour chaque relation SubClassOf retrouvée créer un
					// équivalent SKOS:Broader
					for (OWLClassExpression curseur : listeClasseExpression) {
						// On veut récuperer la classe associée à la relation

						OWLClass curseurClasse = curseur.asOWLClass();

						// On crée l'individu correspondant
						// OWLNamedIndividual individuConceptAssocie = fact
						// .getOWLNamedIndividual(curseurClasse.getIRI());

						IRI iriClasseFille = curseurClasse.getIRI();
						// iriClasseFille.toString());

						OWLNamedIndividual individuConceptAssocie = fact
								.getOWLNamedIndividual(iriClasseFille);

						// On crée la relation Broader avec les noms
						// correspondant à l'IRI en paramètre
						// Si

						addBroader(individuConceptAssocie, individuConcept);

						// On ajoute le prefLabel au concept Père
						addPrefLabel(individuConcept, individuConcept.getIRI());
						// On ajoute le prefLable au concept fils
						// On a besoin de rechercher dans l'ontologie d'origine
						// où l'IRI peut être différente de l'IRI en paramètre
						// on utilise l'individu avec son IRI correspondant à
						// l'ontologue d'origine et on indique son IRI cible

						addPrefLabel(individuConceptAssocie,
								individuConceptAssocie.getIRI());

						// On ajoute le lien avec le schéma pour le concept Père
						addScheme(individuConcept, scheme);
						// On ajoute le lien avec le schéma pour le concept fils

						addScheme(individuConceptAssocie, scheme);

					}
				}
			}
		}
	}
}
