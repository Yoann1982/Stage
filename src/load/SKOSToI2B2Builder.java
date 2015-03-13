package load;

import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import transco.Builder;
import transco.OWLReader;

/**
 * Cette classe gère le chargement d'une hiérarchie SKOS au sein de la table
 * Metadata d'I2B2.
 * 
 * @author Yoann Keravec Date: 13/03/2015<br>
 *         Institut Bergonié<br>
 */
public class SKOSToI2B2Builder extends Builder {

	private List<Metadata> listeMetadata;
	private OWLOntology ontology;
	private PrefixManager prefixOnto;
	
	/*
	 * Principe de la classe
	 * Chargement du fichier en entrée à l'aide de la classe OWLReader
	 * Recherche du ConceptScheme pour initialiser le niveau 0
	 * Recherche au sein de l'ontologie du concept "I2B2"
	 * Traitement récursif sur les fils pour créer les enregistrements => méthode dédiée 
	 */
	
	/**
	 * Constructeur de la classe SKOSBuilder.
	 * 
	 * @param ontologie
	 *            Ontologie d'origine (OWL) @see OWLOntology.
	 */
	public SKOSToI2B2Builder(OWLOntology ontologie) {
		this.ontology = ontologie;
	}
	
	/**
	 * Cette méthode permet de créer un enregistrement Metadata et de le stocker dans la liste d'enregistrement de la classe
	 */
	public void createMetadataRecord() {
		
	}
	
	/**
	 * Cette méthode recherche les ConceptScheme.
	 * Pour chaque valeur retrouvé, un enregistrement de niveau 0 est créée.
	 */
	public void rechercheConceptScheme() {
		
	}
	
	/**
	 * Cette méthode récursive permet de créer les enregistrements correspondants aux fils du concept en entrée
	 */
	public void creationEnregistrement(OWLNamedIndividual individu, int niveau) {
		
	}
	
	public void load(String input) {
		
		// Chargement du fichier SKOS
		OWLReader reader = new OWLReader();
		reader.loadOntology(input);
		
		// Chargement d'un résoneur pour pouvoir trouver les broader mais les narrower également
		
		// Recherche du ConceptScheme
		rechercheConceptScheme();
		
		// Recherche du Concept I2B2 et créations des enregistrements à partir de ce concept
		Set<OWLNamedIndividual> listeIndiv = ontology
				.getIndividualsInSignature();
		
		// On recherche l'élément Thing et on parcours ses fils
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();
		
		// On recherche l'IRI de l'ontologie
		IRI iriProject = foundIriProjectByIndividual();
		// On crée le préfixe associé
		prefixOnto = new DefaultPrefixManager(iriProject.toString() + "#");
		
		// On cherche l'individu correspondant à Thing
		OWLNamedIndividual indivThing = fact.getOWLNamedIndividual("Thing", prefixOnto);
		
		// On cherche l'individu correspondant à #1 (I2B2
		OWLNamedIndividual indivI2B2 = fact.getOWLNamedIndividual("1", prefixOnto);
		
		// On parcours les narrower de cet individu et on crée un enregistrement de niveau inférieur
		List<OWLNamedIndividual> listeIndiv = indivI2B2.getNarrowers();
		
		//Set<OWLNamedIndividual> listClassOnto = ontology.getIndividualsInSignature();
		
		for (OWLNamedIndividual curseurIndiv : listeIndiv) {
			if (curseurIndiv.getIRI().toURI().getFragment().equals("1")) {
				
			}
		}
		
		int niveau = 0;
		for (OWLNamedIndividual curseurIndiv : listeIndiv) {
			
			
			
			// creation des enregistrement
			creationEnregistrement(curseurIndiv, niveau);	
		}
		
		
		
	}
	
	
}
