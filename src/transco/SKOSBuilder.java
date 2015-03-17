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
	 * l'ontologie OWL d'origine
	 */
	public void createSKOSOntologie() {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On récupère l'iri de l'ontologie cible
		IRI iriProject = foundIriProjectByClass();

		IRI iriProjectDiese = IRI.create(iriProject.toString() + "#");

		addPrefix(iriProjectDiese, "bcbs");

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

			String iriClasse = cls.getIRI().toURI().getFragment();

			// 1 - Transformation en SKOS:Concept
			OWLNamedIndividual individuConcept = addIndividual(prefixSKOS,
					"Concept", prefixOnto, iriClasse);

			// 2 - On récupère les sous-classes de la classe référence
			// Si la liste à une taille de 0, c'est que la classe n'est mère
			// d'aucune autre
			Set<OWLClassExpression> listeClasseExpression = cls
					.getSubClasses(originalOntology);

			// Si la liste de sous classe est vide, on ne traite pas (c'est que
			// Concept n'est père d'aucun autre concept

			if (listeClasseExpression.size() != 0) {

				// On va pour chaque relation SubClassOf retrouvée créer un
				// équivalent SKOS:Broader
				for (OWLClassExpression curseur : listeClasseExpression) {
					// On veut récuperer la classe associée à la relation

					OWLClass curseurClasse = curseur.asOWLClass();

					// On crée l'individu correspondant
					OWLNamedIndividual individuConceptAssocie = fact
							.getOWLNamedIndividual(curseurClasse.getIRI());
					// On crée la relation Broader
					addBroader(individuConceptAssocie, individuConcept);
					// On ajoute le prefLabel au concept Père
					addPrefLabel(individuConcept);
					// On ajoute le prefLable au concept fils
					addPrefLabel(individuConceptAssocie);
					// On ajoute le lien avec le schéma pour le concept Père
					addScheme(individuConcept, scheme);
					// On ajoute le lien avec le schéma pour le concept fils
					addScheme(individuConceptAssocie, scheme);
					// }
				}
			}
		}
	}
}
