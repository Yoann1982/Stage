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
 * Cette m�thode permet de construire une hi�rarchie SKOS � partir d'une
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

		

		// On cr�e la relation broader
		
		OWLObjectProperty hasBroader = fact.getOWLObjectProperty(IRI
				.create(iriSKOS + "#broader"));
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(
				hasBroader, classeMere, classeFille);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
	}

	/**
	 * Cette m�thode permet d'ajouter le prefLable au concept SKOS � partir du label de la classe OWL d'origine
	 * @param individuClasse
	 */
	public void addPrefLabel(OWLIndividual individuClasse) {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();
		
		OWLAnnotationValue annotValeur = null;
		
		// r�cup�ration des annotations
		Set<OWLAnnotation> listeAnnot = individuClasse.asOWLNamedIndividual().getAnnotations(originalOntology);
		
		//Set<OWLAnnotation> listeAnnot = classe.getAnnotations(originalOntology);

		if (listeAnnot.size() != 0) {
			
			System.out.println("Dans la boucle (taille > 0)");
			
			for (OWLAnnotation curseurAnnot : listeAnnot) {
				System.out.println("Propri�t� : " + curseurAnnot.getProperty().getIRI() );
				System.out.println("IRI Label : " +IRI.create(iriRDF+ "#label"));
				// On r�cup�re le type de propri�t� rdfs:label
				if (curseurAnnot.getProperty().getIRI().equals(IRI.create(iriRDF+ "#label"))) {
					// on r�cup�re la valeur
					annotValeur = curseurAnnot.getValue();
					
					System.out.println("Propri�t� : " + curseurAnnot.getProperty().getIRI() + " Valeur : " + annotValeur );
					
					
					// Le type de prefLabel est un OWL#AnnotationProperty
					// ==> il faut bidouiller avec ca.
					// Il faut faire une annotation de type SKOS:PrefLabel au lieu de rdfs:label
					// et la rattacher � l'individu
					
					// 1 - On cr�e un OWLAnnotationProperty de type SKOS:prefLabel
					OWLAnnotationProperty propertyPrefLabel =
							fact.getOWLAnnotationProperty(IRI
									.create(iriSKOS + "#prefLabel"));
					
					// 2 - On cr�e un OWLAnnotation avec ce type et avec la valeur de l'annotation OWL
					OWLAnnotation targetAnnot =
							fact.getOWLAnnotation(propertyPrefLabel, annotValeur);
					
					// 3 - On transforme l'individu en AnonymousIndividual 
					// qui est une repr�sentation de OWLAnnotationSubject
					//OWLAnonymousIndividual annotSujet = individuClasse.asOWLAnonymousIndividual(); 
					IRI iriIndividu = individuClasse.asOWLNamedIndividual().getIRI();
					System.out.println("IRI Indiv : " + iriIndividu);
					
					// 4 - On cr�e l'axiom d'annotation
					OWLAnnotationAssertionAxiom axiomAnnot = fact.getOWLAnnotationAssertionAxiom(iriIndividu, targetAnnot);
					
					// 5 - On cr�e le addAxiom
					
					AddAxiom addAxiomAnnot = new AddAxiom(targetOntology, axiomAnnot);
					manager.applyChange(addAxiomAnnot);
					
					// On cr�e un SKOS:prefLabel
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
			 * de SKOS:Concept 2 - R�cup�rer sa relation SubClassOf 3 -
			 * R�cup�rer la classe associ�e 4 - Transformer celle-ci en
			 * Individual de SKOS:Concept si pas d�j� fait 5 - Cr�er une
			 * relation de type broader entre les deux concepts
			 */

			IRI iriClasse = cls.getIRI();

			// 1 - Transformation en SKOS:Concept
			OWLIndividual individuConcept = fact
					.getOWLNamedIndividual(iriClasse);

			// 2 - On r�cup�re les sous-classes de la classe r�f�rence
			// Si la liste � une taille de 0, c'est que la classe n'est m�re
			// d'aucune autre
			Set<OWLClassExpression> listeClasseExpression = cls
					.getSubClasses(originalOntology);

			// Si la liste de sous classe est vide, on ne traite pas (c'est que
			// Concept n'est p�re d'aucun autre concept

			if (listeClasseExpression.size() != 0) {

				// On va pour chaque relation SubClassOf retrouv�e cr��er un
				// �quivalent SKOS:Broader
				for (OWLClassExpression curseur : listeClasseExpression) {
					// On veut r�cuperer la classe associ�e � la relation
					Set<OWLClass> listeClasse = curseur.getClassesInSignature();

					// Il n'y a qu'un seul �l�ment dans le Set
					for (OWLClass curseurClasse : listeClasse) {
						// On cr�e l'individu correspondant
						OWLIndividual individuConceptAssocie = fact
								.getOWLNamedIndividual(curseurClasse.getIRI());
						// On cr�e la relation Broader
						addBroader(individuConcept, individuConceptAssocie);
						// On ajoute le prefLabel au concept P�re
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
			System.out.println("Probl�me avec l'initialiation de l'ontologie cible.");
			e.printStackTrace();
		}
	}
	
}
