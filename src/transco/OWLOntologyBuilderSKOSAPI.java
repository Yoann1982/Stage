package transco;

import java.net.URI;
import java.util.List;
import java.util.Set;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSEntity;
import org.semanticweb.skos.SKOSLiteral;
import org.semanticweb.skos.SKOSUntypedLiteral;

/**
 * Cette classe permet de constuire une ontologie à partir de données SKOS.
 * @author Yoann Keravec<br>
 * Date: 09/03/2015<br>
 * Institut Bergonié<br>
 */

/**
 * OWLOntologyBuilder is used to create and maintain the ontology for the
 * alignment
 */
public class OWLOntologyBuilderSKOSAPI {
	private OWLOntology ontology = null;
	private OWLDataFactory fact = null;
	private OWLOntologyManager manager;

	public OWLOntologyBuilderSKOSAPI() {

		try {
			this.manager = OWLManager.createOWLOntologyManager();
			fact = this.manager.getOWLDataFactory();

			ontology = this.manager.createOntology();

		} catch (Exception ex) {
			System.err.println("Erreur lors de la création de l'objet OWLOntologyBuilder.");
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

			// On parcours le contenu du ConceptSKOS

			// 1 - Traitement du Concept
			// On crée l'objet OWLClass associé au SKOS Concept
			SKOSConcept concept = skosConcept.getConcept();
			URI uriConcept = concept.getURI();
			owlClass = fact.getOWLClass(IRI.create(uriConcept));

			
			// On vérifie s'il est déjà présent dans l'ontologie.
			// Si ce n'est pas le cas, on le rattache à Thing
			addURIAThing(uriConcept);
			
			// 2 - On parcours la liste d'annotations

			List<SKOSAnnotation> listAnnot = skosConcept.getListAnnotation();

			for (SKOSAnnotation curAnnot : listAnnot) {

				// On regarde la classe propriétaire de l'annotation
				String typeAnnot = curAnnot.getURI().getFragment();

				String lang = "";
				String value = "";
				URI uriClasse2 = null;
				// On récupère la valeur (si pas une classe)

				if (curAnnot.isAnnotationByConstant()) {

					SKOSLiteral literal = curAnnot
							.getAnnotationValueAsConstant();
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
					// On récupère l'URI de la seconde classe correspondant à la relation
					// partie Range
					uriClasse2 = entity2.getURI();
				}

				//System.out.print("Le type d'annotation traitée : " + typeAnnot+". \t");
				//System.out.println("La valeur de l'annotation : " + value);

				// On transcode en fonction du type d'annotation
				switch (typeAnnot) {
				case "prefLabel":
					// System.out.println("Transcodage de prefLabel -> label");
					addLabel(owlClass, value);
					break;
				case "altLabel":
					// System.out.println("Transcodage de altLabel -> label");
					addLabel(owlClass, value);
					break;
				case "definition":
					// System.out.println("Transcodage de definition -> comment");
					addComment(owlClass, value);
					break;
				case "scopeNote":
					// System.out.println("Transcodage de scopeNote -> comment");
					addComment(owlClass, value);
					break;
				case "broader":
					
					System.out.print("Le type d'annotation traitée : " + typeAnnot+". \t");
					System.out.println("La valeur de l'annotation : " + value);
	
					
					//System.out.println("Transcodage de broader");

					// Il faut ici construire une relation SubClassOf

					/**
					 * 1 - On regarde si la classe est SubClassOf Thing Si c'est
					 * le cas, on supprime cet axiome
					 * 
					 * 2 - On construit la deuxième classe si elle n'existe pas.
					 * On la rattache à Thing 
					 * 3 - On crée la relation SubClassOf entre la classe que l'on traite et cette
					 * deuxième classe.
					 */

					// 1 - On regarde si la classe est SubClassOf Thing

					// On la recherche dans le factory
					OWLSubClassOfAxiom rechercheThing = fact
							.getOWLSubClassOfAxiom(owlClass, fact.getOWLThing());
					if (rechercheThing.getSuperClass() == null) {
						
						System.out
								.println("La classe n'est pas rattachée à Thing.");
					} else {
						
						System.out
								.println("La classe traitée est rattachée à Thing. Suppression de la relation.");
						RemoveAxiom removeAxiom = new RemoveAxiom(ontology,
								rechercheThing);
						manager.applyChange(removeAxiom);
					}

					// 2 - Il faut construire la deuxième classe si elle n'existe pas

					OWLClass classe2 = fact.getOWLClass(IRI.create(uriClasse2));

					// On recherche dans l'ontologie si la classe est déjà présente
					addURIAThing(uriClasse2);

					// 3 - On crée l'axiome SubClassOf entre owlClass et classe2
					addSubClassOf(owlClass,classe2);
					break;
				case "related":
					//System.out.println("Voir comment on transcode cette balise.");
					// On vérifie si la classe reliée existe dans l'ontologie.
					// Si ce n'est pas le cas, on l'ajoute en la rattachant à
					// Thing
					addURIAThing(uriClasse2);
					break;
				default:
					System.out.println("Le type " + typeAnnot
							+ " n'est pas supporté.");
					break;
				}
			}
		} catch (Exception ex) {
			System.err.println("Erreur lors de l'écriture de la classe à partir du concept "+ skosConcept + ".");
			ex.printStackTrace();
		}
	}

	/**
	 * Get a particular class from the Ontology.
	 * 
	 * @param className
	 * @return OWLClass
	 */
	public OWLClass getClass(String className) {
		OWLClass ret = null;
		try {
			// URI uri = new URI(uriStr + "#" + className);
			ontology.getClass();
		} catch (Exception ex) {
			System.err.println("Erreur lors de la récupération de la classe OWL.");
			ex.printStackTrace();
		}
		return ret;

	}

	/**
	 * Cette méthode permet d'ajouter un commentaire à une classe.
	 * @param owlClass Classe cible
	 * @param value Commentaire à ajouter
	 */
	public void addComment(OWLClass owlClass, String value) {
		OWLAnnotation commentAnno = fact.getOWLAnnotation(
				fact.getRDFSComment(), fact.getOWLLiteral(value));

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(),
				commentAnno);
		this.manager.applyChange(new AddAxiom(ontology, ax));
	}

	/**
	 * Cette méthode permet d'ajouter un label à une classe.
	 * @param owlClass Classe cible.
	 * @param value Label à ajouter.
	 */
	public void addLabel(OWLClass owlClass, String value) {
		OWLAnnotation labelAnno = fact.getOWLAnnotation(fact.getRDFSLabel(),
				fact.getOWLLiteral(value));

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(),
				labelAnno);
		this.manager.applyChange(new AddAxiom(ontology, ax));
	}
	
	/**
	 * Getter de ontology.
	 * @return
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Setter de Ontology.
	 * @param onto
	 */
	public void setOntology(OWLOntology onto) {
		this.ontology = onto;
	}

	/**
	 * Cette méthode recherche dans l'ontologie si la classe est déjà présente.
	 * Elle retourne vraie dans ce cas.
	 * @param classeRecherchee
	 * @return True si présent, False sinon
	 */
	public boolean isPresent(OWLClass classeRecherchee) {

		Set<OWLClass> listClassOnto = ontology.getClassesInSignature();
		boolean dejaPresent = false;
		for (OWLClass cls : listClassOnto) {
			if (cls.getIRI().equals(classeRecherchee.getIRI())) {
				//System.out.println("La classe est déjà présente. On ne rajoute pas la classe.");
				dejaPresent = true;
				break;
			}
		}
		return dejaPresent;
	}
	
	/**
	 * Cette méthode permet d'ajouter un axiom SubClassOf
	 * @param classeSub
	 * @param superClasse
	 */
	public void addSubClassOf(OWLClass classeSub, OWLClass superClasse) {
		OWLAxiom axiomThing = fact.getOWLSubClassOfAxiom(
				classeSub, superClasse);
		AddAxiom addAxiomThing = new AddAxiom(ontology, axiomThing);
		manager.applyChange(addAxiomThing);
	}
	
	/**
	 * Cette méthode vérifie si l'ontologie contient une classe correspond à l'URI en entrée
	 * Si aucune classe n'est retrouvée, elle est rattachée à Thing (SubClassOf)
	 * @param uriAAjouter
	 */
	public void addURIAThing(URI uriAAjouter) {
		OWLClass classeRelated = fact.getOWLClass(IRI.create(uriAAjouter));
		boolean dejaPresent = isPresent(classeRelated);
		if (!dejaPresent) {
			//System.out.println("On rattache la classe à Thing.");
			OWLClass thing = fact.getOWLThing();
			// Si on on ne la trouve pas :
			addSubClassOf(classeRelated,thing);
		}
	}
	
}
