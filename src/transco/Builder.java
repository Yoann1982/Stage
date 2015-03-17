package transco;

import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Cette classe construit les méthodes et attributs communs aux builder (SKOS & OWL)
 * @author Yoann Keravec<br>
 * Date: 10/03/2015<br>
 * Institut Bergonié<br>
 */

public class Builder {

	protected OWLOntology originalOntology;
	protected OWLDataFactory fact = null;
	protected OWLOntologyManager manager;
	protected OWLOntology targetOntology;

	/**
	 * Getter de targetOntology.
	 * @return L'ontologie cible
	 */
	public OWLOntology getTargetOntology() {
		return targetOntology;
	}

	/**
	 * Setter de targetOntology.
	 * @param targetOntology L'ontologie cible
	 */
	public void setTargetOntology(OWLOntology targetOntology) {
		this.targetOntology = targetOntology;
	}

	/**
	 * Getter de originalOntology.
	 * @return L'ontologie d'origine
	 */
	public OWLOntology getOriginalOntology() {
		return originalOntology;
	}

	/**
	 * Setter de originalOntology.
	 * @param ontology L'ontologie d'origine
	 */
	public void setOriginalOntology(OWLOntology ontology) {
		this.originalOntology = ontology;
	}

	/**
	 * Getter du OWLDataFactory.
	 * @return @see OWLDataFactory
	 */
	public OWLDataFactory getFact() {
		return fact;
	}

	/**
	 * Setter du OWLDataFactory.
	 * @param fact @see OWLDataFactory
	 */
	public void setFact(OWLDataFactory fact) {
		this.fact = fact;
	}

	/**
	 * Getter du manager.
	 * @return @see OWLOntologyManager
	 */
	public OWLOntologyManager getManager() {
		return manager;
	}

	/**
	 * Setter du manager.
	 * @param manager @see OWLOntologyManager
	 */
	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}
	
	
	/**
	 * Cette méthode permet de retrouver l'IRI correspondant aux individus
	 * présents dans l'ontologie d'origine.
	 * 
	 * @return @see IRI (partie Scheme et SchemeSpecificPart) des classes
	 */
	public IRI foundIriProjectByIndividual() {

		IRI iriClasseFille = null;

		Set<OWLNamedIndividual> listIndivOnto = originalOntology
				.getIndividualsInSignature();

		// Si la liste de sous classe est vide, on ne traite pas (c'est que
		// Concept n'est père d'aucun autre concept

		if (listIndivOnto.size() != 0) {

			// On récupère l'IRI du premier individu lu
			for (OWLNamedIndividual cls : listIndivOnto) {
				iriClasseFille = cls.getIRI();
				break;
			}
		}
		if (iriClasseFille != null)
		return IRI.create(iriClasseFille.getScheme() + ":"
				+ iriClasseFille.toURI().getSchemeSpecificPart());
		else return iriClasseFille;
	}
	
	/**
	 * Cette méthode permet de retrouver l'IRI correspondant aux classes
	 * présentes dans l'ontologie d'origine.
	 * 
	 * @return @see IRI (partie Scheme et SchemeSpecificPart) des classes
	 */
	public IRI foundIriProjectByClass() {

		IRI iriClasseFille = null;

		Set<OWLClass> listClassOnto = originalOntology.getClassesInSignature();
		
		// Si la liste de sous classe est vide, on ne traite pas (c'est que
		// Concept n'est père d'aucun autre concept

		if (listClassOnto.size() != 0) {

			// On va pour chaque relation SubClassOf retrouvée, créer un
			// équivalent SKOS:Broader
			for (OWLClass cls : listClassOnto) {
				iriClasseFille = cls.getIRI();
				break;
			}
		}
		return IRI.create(iriClasseFille.getScheme() + ":"
				+ iriClasseFille.toURI().getSchemeSpecificPart());
	}
	
	/**
	 * Cette méthode permet d'initialiser l'ontologie cible (SKOS).
	 * 
	 * @param iriOntology @see IRI
	 *            : Correspond à l'IRI de l'ontologie cible à initialiser
	 */
	public void initTargetOnto(IRI iriOntology) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			this.targetOntology = manager.createOntology(iriOntology);
		} catch (OWLOntologyCreationException e) {
			System.err
					.println("Problème avec l'initialiation de l'ontologie cible.");
			e.printStackTrace();
		}
	}
}
