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
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Cette méthode permet de construire une hiérarchie SKOS à partir d'une
 * OWLOntology
 * 
 * @author y.keravec
 *
 */
public class SKOSBuilder {

	private OWLOntology originalOntology;
	private OWLOntology targetOntology;
	private IRI iriRDFS = IRI.create("http://www.w3.org/2000/01/rdf-schema");
	private IRI iriSKOS = IRI.create("http://www.w3.org/2004/02/skos/core");

	/**
	 * Getter de OriginalOntology
	 * @return L'ontologie d'origine (OWL)
	 */
	public OWLOntology getOriginalOntology() {
		return originalOntology;
	}

	/**
	 * Setter de l'ontologie d'origine
	 * @param originalOntology L'ontologie d'orgine (OWL)
	 */
	public void setOriginalOntology(OWLOntology originalOntology) {
		this.originalOntology = originalOntology;
	}

	/**
	 * Getter de l'ontologie cible (SKOS)
	 * @return Ontologie cible (SKOS)
	 */
	public OWLOntology getTargetOntology() {
		return targetOntology;
	}

	/**
	 * Setter de l'ontologie cible (SKOS)
	 * @param targetOntology Ontologie cible (SKOS)
	 */
	public void setTargetOntology(OWLOntology targetOntology) {
		this.targetOntology = targetOntology;
	}

	/**
	 * Constructeur de la classe SKOSBuilder
	 * @param ontologie Ontologie d'origine (OWL)
	 */
	public SKOSBuilder(OWLOntology ontologie) {
		this.originalOntology = ontologie;
	}

	/**
	 * Cette méthode crée une relation hasBroader entre le concept père et le
	 * concept fils
	 * 
	 * @param conceptPere
	 * @param conceptFils
	 */
	public void addBroader(OWLIndividual conceptFils, OWLIndividual conceptPere) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On crée la relation broader

		OWLObjectProperty hasBroader = fact.getOWLObjectProperty(IRI
				.create(iriSKOS + "#broader"));
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(
				hasBroader, conceptFils, conceptPere);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
	}

	/**
	 * Cette méthode permet d'ajouter le prefLable au concept SKOS à partir du
	 * label de la classe OWL d'origine
	 * 
	 * @param individuClasse
	 */
	public void addPrefLabel(OWLIndividual individuClasse) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		OWLAnnotationValue annotValeur = null;

		// récupération des annotations
		Set<OWLAnnotation> listeAnnot = individuClasse.asOWLNamedIndividual()
				.getAnnotations(originalOntology);

		if (listeAnnot.size() != 0) {

			System.out.println("Dans la boucle (taille > 0)");

			for (OWLAnnotation curseurAnnot : listeAnnot) {
				System.out.println("Propriété : "
						+ curseurAnnot.getProperty().getIRI());
				System.out.println("IRI Label : "
						+ IRI.create(iriRDFS + "#label"));
				// On récupère le type de propriété rdfs:label
				if (curseurAnnot.getProperty().getIRI()
						.equals(IRI.create(iriRDFS + "#label"))) {
					// on récupère la valeur
					annotValeur = curseurAnnot.getValue();

					System.out.println("Propriété : "
							+ curseurAnnot.getProperty().getIRI()
							+ " Valeur : " + annotValeur);

					// 1 - On crée un OWLAnnotationProperty de type
					// SKOS:prefLabel
					OWLAnnotationProperty propertyPrefLabel = fact
							.getOWLAnnotationProperty(IRI.create(iriSKOS
									+ "#prefLabel"));

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
					System.out.println("IRI Indiv : " + iriIndividu);

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
	 * Cette méthode permet de retrouver l'IRI correspondant aux classes
	 * présentes dans l'ontologie d'origine
	 * 
	 * @return IRI (partie Scheme et SchemeSpecificPart) des classes
	 */
	public IRI foundIriProject() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		IRI iriClasseFille = null;

		// Set<OWLClass> listClassOnto = originalOntology.getCl;

		Set<OWLClassExpression> listeClasseExpression = fact.getOWLThing()
				.getSubClasses(originalOntology);

		// Si la liste de sous classe est vide, on ne traite pas (c'est que
		// Concept n'est père d'aucun autre concept

		if (listeClasseExpression.size() != 0) {

			// On va pour chaque relation SubClassOf retrouvée crééer un
			// équivalent SKOS:Broader
			for (OWLClassExpression curseur : listeClasseExpression) {
				iriClasseFille = curseur.asOWLClass().getIRI();
				System.out.println("IRI classe : " + iriClasseFille);
				System.out.println("IRI namespace : "
						+ iriClasseFille.getNamespace());
				System.out
						.println("IRI scheme : " + iriClasseFille.getScheme());
				System.out.println("IRI specifi : "
						+ iriClasseFille.getScheme() + ":"
						+ iriClasseFille.toURI().getSchemeSpecificPart());
				break;
			}
		}
		return IRI.create(iriClasseFille.getScheme() + ":"
				+ iriClasseFille.toURI().getSchemeSpecificPart());
	}

	/**
	 * Cette méthode permet de récupérer la class OWLClass associé à l'URI
	 * 
	 * @param type
	 * @return OWLClass : la classe associée à l'URI
	 */
	public OWLClass recupClassByIRI(IRI type) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		return fact.getOWLClass(type);

	}

	/**
	 * Cette méthode permet d'ajouter dans l'ontologie un individu du type
	 * indiqué en entrée
	 * 
	 * @param type
	 *            : La classe à laquelle correspond l'individu
	 * @param individu
	 *            : OWLNamedIndividual l'individu qui instancie la classe (type)
	 */
	public void addClassAssertion(IRI iriClasse, OWLNamedIndividual individu) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		OWLClassAssertionAxiom classAssertion = fact.getOWLClassAssertionAxiom(
				recupClassByIRI(iriClasse), individu);

		manager.addAxiom(targetOntology, classAssertion);

	}

	/**
	 * Cette méthode permet d'ajouter un individu dans l'ontologie Elle retourne
	 * l'individu correspondant
	 * 
	 * @param type
	 *            : Type (Classe) de l'individu
	 * @param name
	 *            : Nom de l'individu
	 * @return
	 */
	public OWLNamedIndividual addIndividual(IRI type, IRI name) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		OWLNamedIndividual ind = fact.getOWLNamedIndividual(name);

		addClassAssertion(type, ind);
		return ind;
	}

	/**
	 * Cette méthode permet d'ajouter une relation inScheme entre un Concept et
	 * un ConceptScheme
	 * 
	 * @param classe
	 *            : Correspond au Concept SKOS
	 * @param scheme
	 *            : Correspond au ConceptScheme SKOS
	 */
	public void addScheme(OWLIndividual classe, OWLIndividual scheme) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On crée la relation ConceptScheme

		OWLObjectProperty hasBroader = fact.getOWLObjectProperty(IRI
				.create(iriSKOS + "#inScheme"));
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(
				hasBroader, classe, scheme);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
	}

	/**
	 * Cette méthode permet de créer l'ontologie SKOS cible à partir de
	 * l'ontologie OWL d'origine
	 */
	public void creeSKOSOntologie() {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On récupère l'iri de l'ontologie cible
		IRI iriProject = foundIriProject();

		// On initialise l'ontologie cible.
		initTargetOnto(iriProject);

		// On crée l'entite Scheme

		IRI iriClasseSKOS = IRI.create(iriSKOS + "#ConceptScheme");

		OWLNamedIndividual scheme = addIndividual(iriClasseSKOS,
				IRI.create(iriProject + "#" + "BCBSarcomes"));

		Set<OWLClass> listClassOnto = originalOntology.getClassesInSignature();
		for (OWLClass cls : listClassOnto) {

			/*
			 * Pour chaque classe lue, on va : 1 - La transformer en Individual
			 * de SKOS:Concept 2 - Récupérer sa relation SubClassOf 3 -
			 * Récupérer la classe associée 4 - Transformer celle-ci en
			 * Individual de SKOS:Concept si pas déjà fait 5 - Créer une
			 * relation de type broader entre les deux concepts
			 */

			IRI iriClasse = cls.getIRI();

			iriClasseSKOS = IRI.create(iriSKOS + "#Concept");
			// 1 - Transformation en SKOS:Concept
			OWLNamedIndividual individuConcept = addIndividual(iriClasseSKOS,
					iriClasse);

			// 2 - On récupère les sous-classes de la classe référence
			// Si la liste à une taille de 0, c'est que la classe n'est mère
			// d'aucune autre
			Set<OWLClassExpression> listeClasseExpression = cls
					.getSubClasses(originalOntology);

			// Si la liste de sous classe est vide, on ne traite pas (c'est que
			// Concept n'est père d'aucun autre concept

			if (listeClasseExpression.size() != 0) {

				// On va pour chaque relation SubClassOf retrouvée crééer un
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

	/**
	 * Cette méthode permet d'initialiser l'ontologie cible (SKOS)
	 */
	public void initTargetOnto(IRI iriOntology) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			this.targetOntology = manager.createOntology(iriOntology);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.out
					.println("Problème avec l'initialiation de l'ontologie cible.");
			e.printStackTrace();
		}
	}

}
