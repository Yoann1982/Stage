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

			System.out.println("\n");
			System.out.println("*************** Traitement de cr�ation des classes *******************");
			System.out.println("");
			
			// On parcours le contenu du ConceptSKOS

			// 1 - Traitement du Concept
			// On cr�e l'objet OWLClass associ� au SKOS Concept
			SKOSConcept concept = skosConcept.getConcept();
			URI uriConcept = concept.getURI();
			owlClass = fact.getOWLClass(IRI.create(uriConcept));

			
			// On v�rifie s'il est d�j� pr�sent dans l'ontologie.
			// Si ce n'est pas le cas, on le rattache � Thing
			ajouteURIAThing(uriConcept);
			
			// 2 - On parcours la liste d'annotations

			List<SKOSAnnotation> listAnnot = skosConcept.getListAnnotation();

			for (SKOSAnnotation curAnnot : listAnnot) {

				// On regarde la classe propri�taire de l'annotation
				String typeAnnot = curAnnot.getURI().getFragment();

				String lang = "";
				String value = "";
				URI uriClasse2 = null;
				// On r�cup�re la valeur (si pas une classe)

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
					// On r�cup�re l'URI de la seconde classe correspondant � la relation
					// partie Range
					uriClasse2 = entity2.getURI();
				}

				System.out.print("Le type d'annotation trait�e : " + typeAnnot+". \t");
				System.out.println("La valeur de l'annotation : " + value);

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
					System.out.println("Transcodage de broader");

					// Il faut ici construire une relation SubClassOf

					/**
					 * 1 - On regarde si la classe est SubClassOf Thing Si c'est
					 * le cas, on supprime cet axiome
					 * 
					 * 2 - On construit la deuxi�me classe si elle n'existe pas.
					 * On la rattache � Thing 3 - On cr�e la relation
					 * SubClassOf entre la classe que l'on traite et cette
					 * deuxi�me classe.
					 */

					// 1 - On regarde si la classe est SubClassOf Thing

					// On la recherche dans le factory
					OWLSubClassOfAxiom rechercheThing = fact
							.getOWLSubClassOfAxiom(owlClass, fact.getOWLThing());
					if (rechercheThing.getSuperClass() == null) {
						System.out
								.println("La classe n'est pas rattach�e � Thing.");
					} else {
						System.out
								.println("La classe trait�e est rattach�e � Thing. Suppression de la relation.");
						RemoveAxiom removeAxiom = new RemoveAxiom(onto,
								rechercheThing);
						manager.applyChange(removeAxiom);
					}

					// 2 - Il faut construire la deuxi�me classe si elle n'existe pas

					OWLClass classe2 = fact.getOWLClass(IRI.create(uriClasse2));

					// On recherche dans l'ontologie si la classe est d�j� pr�sente
					ajouteURIAThing(uriClasse2);

					// 3 - On cr�e l'axiom SubClassOf entre owlClass et classe2
					ajoutSubClassOf(owlClass,classe2);
					break;
				case "related":
					System.out
							.println("Voir comment on transcode cette balise.");
					// On v�rifie si la classe reli�e existe dans l'ontologie.
					// Si ce n'est pas le cas, on l'ajoute en la rattachant �
					// Thing
					ajouteURIAThing(uriClasse2);
					break;
				default:
					System.out.println("Le type " + typeAnnot
							+ " n'est pas support�.");
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

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(),
				commentAnno);
		this.manager.applyChange(new AddAxiom(onto, ax));
	}

	public void addLabel(OWLClass owlClass, String value) {
		OWLAnnotation labelAnno = fact.getOWLAnnotation(fact.getRDFSLabel(),
				fact.getOWLLiteral(value));

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(),
				labelAnno);
		this.manager.applyChange(new AddAxiom(onto, ax));
	}

	public OWLOntology getOnto() {
		return onto;
	}

	public void setOnto(OWLOntology onto) {
		this.onto = onto;
	}

	/**
	 * Cette m�thode recherche dans l'ontologie si la classe est d�j� pr�sente.
	 * Elle retourne vraie dans ce cas.
	 * @param classeRecherchee
	 * @return True si pr�sent, False sinon
	 */
	public boolean isPresent(OWLClass classeRecherchee) {

		Set<OWLClass> listClassOnto = onto.getClassesInSignature();
		boolean dejaPresent = false;
		for (OWLClass cls : listClassOnto) {
			if (cls.getIRI() == classeRecherchee.getIRI()) {
				System.out
						.println("La classe est d�j� pr�sente. On ne rajoute pas la classe.");
				dejaPresent = true;
				break;
			}
		}
		return dejaPresent;
	}
	
	/**
	 * Cette m�thode permet d'ajouter un axiom SubClassOf
	 * @param classeSub
	 * @param superClasse
	 */
	public void ajoutSubClassOf(OWLClass classeSub, OWLClass superClasse) {
		OWLAxiom axiomThing = fact.getOWLSubClassOfAxiom(
				classeSub, superClasse);
		AddAxiom addAxiomThing = new AddAxiom(onto, axiomThing);
		manager.applyChange(addAxiomThing);
	}
	
	/**
	 * Cette m�thode v�rifie si l'ontologie contient une classe correspond � l'UTI en entr�e
	 * Si aucune classe n'est retrouv�e, elle est rattach�e � Thing (SubClassOf)
	 * @param uriAAjouter
	 */
	public void ajouteURIAThing(URI uriAAjouter) {
		OWLClass classeRelated = fact.getOWLClass(IRI.create(uriAAjouter));
		boolean dejaPresent = isPresent(classeRelated);
		if (!dejaPresent) {
			System.out.println("On rattache la classe � Thing.");
			OWLClass thing = fact.getOWLThing();
			// Si on on ne la trouve pas :
			ajoutSubClassOf(classeRelated,thing);
		}
	}
	
}
