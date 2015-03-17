/**
 * 
 */
package transco;

import java.util.Set;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Cette classe permet de construire une ontologie à partir d'une structure
 * SKOS.
 * 
 * @author Yoann Keravec<br>
 *         Date: 10/03/2015<br>
 *         Institut Bergonié<br>
 */

public class OWLOntologyBuilder extends Builder {

	/**
	 * Constructeur de la classe. Crée une ontologie d'origine vide.
	 */
	public OWLOntologyBuilder() {

		try {
			this.manager = OWLManager.createOWLOntologyManager();
			this.fact = manager.getOWLDataFactory();

			this.originalOntology = manager.createOntology();

		} catch (Exception ex) {
			System.err
					.println("Erreur lors de la création de l'objet OWLOntologyBuilder.");
			ex.printStackTrace();
		}
	}

	/**
	 * Constructeur de la classe. Charge l'ontology d'origine à partir de
	 * l'ontologie en paramètre.
	 * 
	 * @param onto
	 *            @see OWLOntology
	 */
	public OWLOntologyBuilder(OWLOntology onto) {

		try {
			this.manager = OWLManager.createOWLOntologyManager();
			fact = this.manager.getOWLDataFactory();
			originalOntology = onto;
		} catch (Exception ex) {
			System.err
					.println("Erreur lors de la création de l'objet OWLOntologyBuilder.");
			ex.printStackTrace();
		}
	}

	

	

	/**
	 * Cette méthode crée l'ontologie cible à partir de l'ontologie d'origine.
	 * L'IRI de l'ontologie créée est récupérée en cherchant la valeur de l'IRI
	 * des individus dans l'ontologie d'origine.
	 */
	public void createOntology() {

		// On récupère l'iri de l'ontologie cible
		IRI iriProject = foundIriProjectByIndividual();
		createOntology(iriProject);
	}

	/**
	 * Cette méthode crée l'ontologie cible à partir de l'ontologie d'origine.
	 * L'IRI de l'ontologie créée est en paramètre d'entrée de la méthode.
	 * @param iriProject @see IRI IRI de l'ontologie à créer.
	 */
	public void createOntology(IRI iriProject) {

		// On initialise l'ontologie cible.
		initTargetOnto(iriProject);

		Set<OWLNamedIndividual> listClassOnto = originalOntology
				.getIndividualsInSignature();
		for (OWLNamedIndividual indivCursor : listClassOnto) {

			iriIndiv = indivCursor.getIRI();

			// On traite dans un premier temps les axioms pour créer les
			// SubClassOf
			addRelation(indivCursor);
			// On traite dans un second temps les annotations (prefLabel,
			// altLabel, etc.)
			addAnnotation(indivCursor);

		}
	}

}
