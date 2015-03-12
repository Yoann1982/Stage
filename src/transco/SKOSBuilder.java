package transco;

import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.PrefixOWLOntologyFormat;

/**
 * Cette classe permet de construire une hiérarchie SKOS à partir d'une
 * OWLOntology.
 * @author Yoann Keravec<br>
 * Date: 09/03/2015<br>
 * Institut Bergonié<br>
 */
public class SKOSBuilder extends Builder {

	private String iriRDFS = "http://www.w3.org/2000/01/rdf-schema#";
	private String iriS = "http://www.w3.org/2004/02/skos/core#";
	private PrefixManager prefixSKOS = new DefaultPrefixManager(iriS);
	private PrefixManager prefixOnto;
	private OWLOntologyFormat format;
	
	
	
	public OWLOntologyFormat getFormat() {
		return format;
	}

	public void setFormat(OWLOntologyFormat format) {
		this.format = format;
	}

	public PrefixManager getPrefixOnto() {
		return prefixOnto;
	}

	public void setPrefixOnto(PrefixManager prefixOnto) {
		this.prefixOnto = prefixOnto;
	}

	public PrefixManager getPrefixSKOS() {
		return prefixSKOS;
	}

	public void setPrefixSKOS(PrefixManager prefixSKOS) {
		this.prefixSKOS = prefixSKOS;
	}

	/**
	 * Constructeur de la classe SKOSBuilder.
	 * @param ontologie Ontologie d'origine (OWL) @see OWLOntology.
	 */
	public SKOSBuilder(OWLOntology ontologie) {
		this.originalOntology = ontologie;
	}

	public void addPrefix(IRI iriProject) {
		
		if (format.isPrefixOWLOntologyFormat()) {
			Set<String> listePrefix = format.asPrefixOWLOntologyFormat()
					.getPrefixNames();
			boolean prefixPresent = false;
			for (String prefix : listePrefix) {
				if (iriProject.equals(
						format.asPrefixOWLOntologyFormat().getIRI(prefix))) {
					prefixPresent = true;
					break;
				}
			}
			// Si on n'a pas retrouvé de prefix associé, on l'ajoute
			if (!prefixPresent) {
				if (iriProject.toString().contains("#")) {
					format.asPrefixOWLOntologyFormat().setPrefix("bcbs",
							iriProject.toString());
				} else {
					format.asPrefixOWLOntologyFormat().setPrefix("bcbs",
							iriProject.toString() + "#");
				}
			}
		}
	}
	
	/**
	 * Cette méthode crée une relation hasBroader entre le concept père et le
	 * concept fils.
	 * 
	 * @param conceptPere @see OWLIndividual
	 * @param conceptFils @see OWLIndividual
	 */
	public void addBroader(OWLIndividual conceptFils, OWLIndividual conceptPere) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On crée la relation broader

		OWLObjectProperty hasBroader = fact.getOWLObjectProperty("broader", prefixSKOS);
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(
				hasBroader, conceptFils, conceptPere);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
	}

	/**
	 * Cette méthode permet d'ajouter le prefLable au concept SKOS à partir du
	 * label de la classe OWL d'origine.
	 * 
	 * @param individuClasse @see OWLIndividual
	 */
	public void addPrefLabel(OWLIndividual individuClasse) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		OWLAnnotationValue annotValeur = null;

		// récupération des annotations
		Set<OWLAnnotation> listeAnnot = individuClasse.asOWLNamedIndividual()
				.getAnnotations(originalOntology);

		if (listeAnnot.size() != 0) {
			for (OWLAnnotation curseurAnnot : listeAnnot) {
				// On récupère le type de propriété rdfs:label
				if (curseurAnnot.getProperty().getIRI()
						.equals(IRI.create(iriRDFS + "label"))) {
					// on récupère la valeur
					annotValeur = curseurAnnot.getValue();

					// 1 - On crée un OWLAnnotationProperty de type
					OWLAnnotationProperty propertyPrefLabel = fact
							.getOWLAnnotationProperty("prefLabel", prefixSKOS);

					// 2 - On crée un OWLAnnotation avec ce type et avec la
					// valeur de l'annotation OWL
					OWLAnnotation targetAnnot = fact.getOWLAnnotation(
							propertyPrefLabel, annotValeur);

					// 3 - On transforme l'individu en AnonymousIndividual
					// qui est une représentation de OWLAnnotationSubject
					// OWLAnonymousIndividual annotSujet =
					// individuClasse.asOWLAnonymousIndividual();
					IRI iriIndividu = individuClasse.asOWLNamedIndividual()
							.getIRI();

					// 4 - On crée l'axiom d'annotation
					OWLAnnotationAssertionAxiom axiomAnnot = fact
							.getOWLAnnotationAssertionAxiom(iriIndividu,
									targetAnnot);

					// 5 - On crée le addAxiom

					AddAxiom addAxiomAnnot = new AddAxiom(targetOntology,
							axiomAnnot);
					manager.applyChange(addAxiomAnnot);

				}
			}
		}

	}



	/**
	 * Cette méthode permet de récupérer la class OWLClass associé à l'URI.
	 * 
	 * @param type @see IRI
	 * @return OWLClass : la classe associée à l'URI.
	 */
	public OWLClass recupClassByIRI(IRI type) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		return fact.getOWLClass(type);

	}

	/**
	 * Cette méthode permet d'ajouter dans l'ontologie un individu du type
	 * indiqué en entrée.
	 * 
	 * @param iriClasse
	 *            : La classe à laquelle correspond l'individu @see IRI
	 * @param individu
	 *            : @see OWLNamedIndividual l'individu qui instancie la classe (type)
	 */
	public void addClassAssertion(PrefixManager prefixClasse, String classe, OWLNamedIndividual individu) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		OWLClassAssertionAxiom classAssertion = fact.getOWLClassAssertionAxiom(
				fact.getOWLClass(classe, prefixClasse), individu);

		manager.addAxiom(targetOntology, classAssertion);

	}

	/**
	 * Cette méthode permet d'ajouter un individu dans l'ontologie Elle retourne
	 * l'individu correspondant.
	 * 
	 * @param type
	 *            : Type (Classe) de l'individu @see IRI.
	 * @param name
	 *            : Nom de l'individu @see IRI.
	 * @return @see OWLNamedIndividual L'individu ajouté.
	 */
	public OWLNamedIndividual addIndividual(PrefixManager prefixClasse, String classe, PrefixManager prefix, String name) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();
		OWLNamedIndividual ind = fact.getOWLNamedIndividual(name, prefix);
		addClassAssertion(prefixClasse, classe, ind);
		return ind;
	}

	/**
	 * Cette méthode permet d'ajouter une relation inScheme entre un Concept et
	 * un ConceptScheme.
	 * 
	 * @param classe
	 *            : Correspond au Concept SKOS @see OWLIndividual
	 * @param scheme
	 *            : Correspond au ConceptScheme SKOS @see OWLIndividual
	 */
	public void addScheme(OWLIndividual classe, OWLIndividual scheme) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On crée la relation ConceptScheme

		OWLObjectProperty hasBroader = fact.getOWLObjectProperty("inScheme", prefixSKOS);
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(
				hasBroader, classe, scheme);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
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

		addPrefix(iriProject);
		
		// On initialise l'ontologie cible.
		initTargetOnto(iriProject);

		prefixOnto = new DefaultPrefixManager(iriProject.toString()+"#");
		
		OWLNamedIndividual scheme = addIndividual(prefixSKOS, "ConceptScheme", prefixSKOS, "BCBSarcomes");

		Set<OWLClass> listClassOnto = originalOntology.getClassesInSignature();
		for (OWLClass cls : listClassOnto) {

			/*
			 * Pour chaque classe lue, on va : 
			 * 1 - La transformer en Individual de SKOS:Concept 
			 * 2 - Récupérer sa relation SubClassOf 
			 * 3 - Récupérer la classe associée 
			 * 4 - Transformer celle-ci en Individual de SKOS:Concept si pas déjà fait 
			 * 5 - Créer une relation de type broader entre les deux concepts
			 */

			String iriClasse = cls.getIRI().toURI().getFragment();
			
			// 1 - Transformation en SKOS:Concept
			OWLNamedIndividual individuConcept = addIndividual(prefixSKOS, "Concept", prefixOnto, iriClasse);

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
