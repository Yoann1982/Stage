/**
 * 
 */
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
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
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

	OWLOntology originalOntology;
	OWLOntology targetOntology;
	IRI iriRDF = IRI.create("http://www.w3.org/2000/01/rdf-schema");
	IRI iriSKOS = IRI.create("http://www.w3.org/2004/02/skos/core");
	

	public OWLOntology getOriginalOntology() {
		return originalOntology;
	}

	public void setOriginalOntology(OWLOntology originalOntology) {
		this.originalOntology = originalOntology;
	}

	public OWLOntology getTargetOntology() {
		return targetOntology;
	}

	public void setTargetOntology(OWLOntology targetOntology) {
		this.targetOntology = targetOntology;
	}

	public SKOSBuilder(OWLOntology ontologie) {
		this.originalOntology = ontologie;
	}

	public void addBroader(OWLIndividual classeMere, OWLIndividual classeFille) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		

		// On crée la relation broader
		
		OWLObjectProperty hasBroader = fact.getOWLObjectProperty(IRI
				.create(iriSKOS + "#broader"));
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(
				hasBroader, classeMere, classeFille);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
	}

	/**
	 * Cette méthode permet d'ajouter le prefLable au concept SKOS à partir du label de la classe OWL d'origine
	 * @param individuClasse
	 */
	public void addPrefLabel(OWLIndividual individuClasse) {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();
		
		OWLAnnotationValue annotValeur = null;
		
		// récupération des annotations
		Set<OWLAnnotation> listeAnnot = individuClasse.asOWLNamedIndividual().getAnnotations(originalOntology);
		
		//Set<OWLAnnotation> listeAnnot = classe.getAnnotations(originalOntology);

		if (listeAnnot.size() != 0) {
			
			System.out.println("Dans la boucle (taille > 0)");
			
			for (OWLAnnotation curseurAnnot : listeAnnot) {
				System.out.println("Propriété : " + curseurAnnot.getProperty().getIRI() );
				System.out.println("IRI Label : " +IRI.create(iriRDF+ "#label"));
				// On récupère le type de propriété rdfs:label
				if (curseurAnnot.getProperty().getIRI().equals(IRI.create(iriRDF+ "#label"))) {
					// on récupère la valeur
					annotValeur = curseurAnnot.getValue();
					
					System.out.println("Propriété : " + curseurAnnot.getProperty().getIRI() + " Valeur : " + annotValeur );
					
					
					// Le type de prefLabel est un OWL#AnnotationProperty
					// ==> il faut bidouiller avec ca.
					// Il faut faire une annotation de type SKOS:PrefLabel au lieu de rdfs:label
					// et la rattacher à l'individu
					
					// 1 - On crée un OWLAnnotationProperty de type SKOS:prefLabel
					OWLAnnotationProperty propertyPrefLabel =
							fact.getOWLAnnotationProperty(IRI
									.create(iriSKOS + "#prefLabel"));
					
					// 2 - On crée un OWLAnnotation avec ce type et avec la valeur de l'annotation OWL
					OWLAnnotation targetAnnot =
							fact.getOWLAnnotation(propertyPrefLabel, annotValeur);
					
					// 3 - On transforme l'individu en AnonymousIndividual 
					// qui est une représentation de OWLAnnotationSubject
					//OWLAnonymousIndividual annotSujet = individuClasse.asOWLAnonymousIndividual(); 
					IRI iriIndividu = individuClasse.asOWLNamedIndividual().getIRI();
					System.out.println("IRI Indiv : " + iriIndividu);
					
					// 4 - On crée l'axiom d'annotation
					OWLAnnotationAssertionAxiom axiomAnnot = fact.getOWLAnnotationAssertionAxiom(iriIndividu, targetAnnot);
					
					// 5 - On crée le addAxiom
					
					AddAxiom addAxiomAnnot = new AddAxiom(targetOntology, axiomAnnot);
					manager.applyChange(addAxiomAnnot);
					
					// On crée un SKOS:prefLabel
					//OWLIndividual individuPrefLabel = fact
					//		.getOWLNamedIndividual(IRI.create(iriRDF+ "#label"));
					
					//fact.getOWLAnnotationProperty(IRI.create(iriRDF+ "#label"));

					/*
					
					OWLObjectProperty prefLabel = fact.getOWLObjectProperty(IRI
							.create(iriSKOS + "#prefLabel"));
					OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(prefLabel, individuClasse, individuPrefLabel);

					AddAxiom addAxiomChange = new AddAxiom(ontology, assertion);
					manager.applyChange(addAxiomChange);
					*/
				}
			}
		}

	}

	public void creeSKOSOntologie() {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On initialise l'ontologie cible.
		initTargetOnto();
		
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

			// 1 - Transformation en SKOS:Concept
			OWLIndividual individuConcept = fact
					.getOWLNamedIndividual(iriClasse);

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
					Set<OWLClass> listeClasse = curseur.getClassesInSignature();

					// Il n'y a qu'un seul élément dans le Set
					for (OWLClass curseurClasse : listeClasse) {
						// On crée l'individu correspondant
						OWLIndividual individuConceptAssocie = fact
								.getOWLNamedIndividual(curseurClasse.getIRI());
						// On crée la relation Broader
						addBroader(individuConcept, individuConceptAssocie);
						// On ajoute le prefLabel au concept Père
						addPrefLabel(individuConcept);
						// On ajoute le prefLable au concept fils
						addPrefLabel(individuConceptAssocie);
					}
				}
			}
		}
	}

	public void initTargetOnto() {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			this.targetOntology = manager.createOntology();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.out.println("Problème avec l'initialiation de l'ontologie cible.");
			e.printStackTrace();
		}
	}
	
}
