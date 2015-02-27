package transco;

//import java.net.URI;

//import java.util.HashMap;
import java.net.URI;
import java.util.List;
//import java.util.Map;

import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
//import org.semanticweb.owlapi.model.OWLAnnotationProperty;
//import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
//import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
//import org.semanticweb.owlapi.model.change.AddAnnotationInstance;
//import org.semanticweb.owlapi.model.change.AddEntity;
//import org.semanticweb.owlapi.model.change.ChangeVisitor;
//import org.semanticweb.owlapi.model.OWLOntologyChangeVisitor;
//import org.semanticweb.owlapi.model.change.OntologyChange;
//import org.semanticweb.owlapi.model.OWLOntologyChange;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSEntity;
import org.semanticweb.skos.SKOSLiteral;
import org.semanticweb.skos.SKOSUntypedLiteral;

/**
 * OWLOntologyBuilder is used to create and maintain the ontology for the
 * alignment
 */
public class OWLOntologyBuilder {
	private OWLOntology onto = null;
	private OWLDataFactory fact = null;
	private OWLOntologyManager manager;

	public OWLOntologyBuilder() {

		try {
			this.manager = OWLManager.createOWLOntologyManager();
			fact = this.manager.getOWLDataFactory();

			onto = this.manager.createOntology();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Creates an OWL class from the SKOS concept The following mappings have
	 * been assumed skos:Concept -> owl:Class skos:prefLabel -> rdfs:label
	 * skos:altLabel -> rdfs:label skos:broader -> rdfs:subClassOf skos:narrower
	 * -> No need to handle in the class since it will be an another class like
	 * owl:Class with rdfs:subClassOf this class skos:definition -> rdfs:comment
	 * skos:scopeNote -> rdfs:comment
	 * 
	 * @param skosConcept
	 */
	public void createClass(ConceptSKOS skosConcept) {
		OWLClass owlClass = null;

		try {
			System.out.println("\n\n");
			System.out.println("Traitement de création des classes");
			
			// On parcours le contenu du ConceptSKOS

			// 1 - Traitement du Concept

			SKOSConcept concept = skosConcept.getConcept();
			URI uriConcept = concept.getURI();
			owlClass = fact.getOWLClass(IRI.create(uriConcept));

			OWLAxiom ajoutClasse = fact.getOWLDeclarationAxiom(owlClass);
			AddAxiom addAxiomAjout = new AddAxiom(onto, ajoutClasse);
			manager.applyChange(addAxiomAjout);
			
			System.out.println("La classe traitée est : "+ uriConcept);
			
			// On va le rattacher à Thing (subClassOf)
			
			String conceptIRI = uriConcept.getSchemeSpecificPart();
			System.out.println("La valeur de conceptIRI = "+conceptIRI);
			
			OWLClass thing = fact.getOWLThing();
			OWLAxiom axiom = fact.getOWLSubClassOfAxiom(owlClass, thing);
			
			// Il faut associer les deux classes
			AddAxiom addAxiom = new AddAxiom(onto, axiom);
			// We now use the manager to apply the change
			manager.applyChange(addAxiom);
			
			
			// 2 - On parcours la liste d'annotations

			List<SKOSAnnotation> listAnnot = skosConcept.getListAnnotation();

			for (SKOSAnnotation curAnnot : listAnnot) {

				// On regarde la propriété de l'annotation
				String typeAnnot = curAnnot.getURI().getFragment();
				
				String lang = "";
				String value = "";
				URI uriClasse2 = null;
				// On récupère la valeur (si pas une classe)
				
				if (curAnnot.isAnnotationByConstant()) {

					SKOSLiteral literal = curAnnot.getAnnotationValueAsConstant();
					value = literal.getLiteral();
					if (!literal.isTyped()) {
						// if it has language
						SKOSUntypedLiteral untypedLiteral = literal
								.getAsSKOSUntypedLiteral();
						if (untypedLiteral.hasLang()) {
							lang = untypedLiteral.getLang();
						}
					}
				} else {
					// annotation is some resource
					SKOSEntity entity = curAnnot.getAnnotationValue();
					value = entity.getURI().getFragment();
					// l'URI si une classe
					SKOSEntity entity2 = curAnnot.getAnnotationValue();
					uriClasse2 = entity2.getURI();
				}				
				
				System.out.println("URI Annotation " + curAnnot.getURI());
				
				System.out.print("Le type d'annotation traité : "+ typeAnnot);
				System.out.println(" la valeur de l'annotation : "+value);
								
				// On transcode en fonction du type d'annotation
				switch (typeAnnot) {
				case "prefLabel":
					System.out.println("Transcodage de prefLabel -> label");
					addLabel(owlClass, value);
					break;
				case "altLabel":
					System.out.println("Transcodage de altLabel -> label");
					addLabel(owlClass, value);
					break;
				case "definition":
					System.out.println("Transcodage de definition -> comment");
					addComment(owlClass, value);
					break;
				case "scopeNote":
					System.out.println("Transcodage de scopeNote -> comment");
					addComment(owlClass, value);
					break;
				case "broader":
					System.out.println("Transcodage de broader");
					// Il faut ici construire une relation SubClassOf
					
					// Il faut construire la deuxième classe si elle n'existe pas


					OWLClass classe2 = fact.getOWLClass(IRI.create(uriClasse2));
					// Now create the axiom
					OWLAxiom axiom2 = fact.getOWLSubClassOfAxiom(owlClass, classe2);
					
					// Il faut associer les deux classes
					AddAxiom addAxiom2 = new AddAxiom(onto, axiom2);
					// We now use the manager to apply the change
					manager.applyChange(addAxiom2);
					break;
				case "related" :
					System.out.println("Voir comment on transcode cette balise.");
					break;
				default : 
					System.out.println("Le type "+typeAnnot + " n'est pas supporté.");
					break;
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Get a particular class from the Ontology
	 * 
	 * @param className
	 * @return OWLClass
	 */
	public OWLClass getClass(String className) {
		OWLClass ret = null;
		try {
			// URI uri = new URI(uriStr + "#" + className);
			onto.getClass();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ret;

	}

	public OWLOntology getOntology() {
		return onto;
	}

	public void addComment(OWLClass owlClass, String value) {
		OWLAnnotation commentAnno = fact.getOWLAnnotation(
				fact.getRDFSComment(), fact.getOWLLiteral(value));

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(
				owlClass.getIRI(), commentAnno);
		this.manager.applyChange(new AddAxiom(onto, ax));
	}
	
	public void addLabel(OWLClass owlClass, String value) {
		OWLAnnotation labelAnno = fact.getOWLAnnotation(
				fact.getRDFSLabel(), fact.getOWLLiteral(value));

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(
				owlClass.getIRI(), labelAnno);
		this.manager.applyChange(new AddAxiom(onto, ax));
	}
	

	public OWLOntology getOnto() {
		return onto;
	}

	public void setOnto(OWLOntology onto) {
		this.onto = onto;
	}
	
	
	
}
