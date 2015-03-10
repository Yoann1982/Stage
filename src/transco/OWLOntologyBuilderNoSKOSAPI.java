/**
 * 
 */
package transco;

import java.net.URI;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.RemoveAxiom;

/**
 * @author y.keravec
 *
 */
public class OWLOntologyBuilderNoSKOSAPI {
	private OWLOntology originalOntology;
	private OWLDataFactory fact = null;
	private OWLOntologyManager manager;
	private OWLOntology targetOntology;
	private OWLClass classePrincipale = null;
	private OWLClass classeSecondaire = null;
	private IRI iriProp = null;
	private IRI iriIndiv = null; 
			
	public OWLOntologyBuilderNoSKOSAPI() {

		try {
			this.manager = OWLManager.createOWLOntologyManager();
			fact = this.manager.getOWLDataFactory();

			originalOntology = this.manager.createOntology();

		} catch (Exception ex) {
			System.err
					.println("Erreur lors de la création de l'objet OWLOntologyBuilder.");
			ex.printStackTrace();
		}
	}

	public OWLOntologyBuilderNoSKOSAPI(OWLOntology onto) {

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

	public OWLOntology getTargetOntology() {
		return targetOntology;
	}

	public void setTargetOntology(OWLOntology targetOntology) {
		this.targetOntology = targetOntology;
	}

	public OWLOntology getOriginalOntology() {
		return originalOntology;
	}

	public void setOriginalOntology(OWLOntology ontology) {
		this.originalOntology = ontology;
	}

	/**
	 * Cette méthode permet d'ajouter un axiom SubClassOf
	 * 
	 * @param classeSub
	 * @param superClasse
	 */
	public void addSubClassOf(OWLClass classeSub, OWLClass superClasse) {
		OWLAxiom axiomThing = fact
				.getOWLSubClassOfAxiom(classeSub, superClasse);
		AddAxiom addAxiomThing = new AddAxiom(targetOntology, axiomThing);
		manager.applyChange(addAxiomThing);
	}

	/**
	 * Cette méthode recherche dans l'ontologie si la classe est déjà présente.
	 * Elle retourne vraie dans ce cas.
	 * 
	 * @param classeRecherchee
	 * @return True si présent, False sinon
	 */
	public boolean isPresent(OWLClass classeRecherchee) {

		Set<OWLClass> listClassOnto = targetOntology.getClassesInSignature();
		boolean dejaPresent = false;
		for (OWLClass cls : listClassOnto) {
			if (cls.getIRI().equals(classeRecherchee.getIRI())) {
				//System.out
				//		.println("La classe est déjà présente. On ne rajoute pas la classe.");
				dejaPresent = true;
				break;
			}
		}
		return dejaPresent;
	}

	/**
	 * Cette méthode vérifie si l'ontologie contient une classe correspond à
	 * l'URI en entrée Si aucune classe n'est retrouvée, elle est rattachée à
	 * Thing (SubClassOf)
	 * 
	 * @param uriAAjouter
	 */
	public void addURIAThing(URI uriAAjouter) {
		OWLClass classeRelated = fact.getOWLClass(IRI.create(uriAAjouter));
		boolean dejaPresent = isPresent(classeRelated);
		if (!dejaPresent) {
			// System.out.println("On rattache la classe à Thing.");
			OWLClass thing = fact.getOWLThing();
			// Si on on ne la trouve pas :
			addSubClassOf(classeRelated, thing);
		}
	}

	/**
	 * Cette méthode vérifie si l'ontologie contient une classe correspond à
	 * l'URI en entrée Si aucune classe n'est retrouvée, elle est rattachée à
	 * Thing (SubClassOf)
	 * 
	 * @param uriAAjouter
	 */
	public void addToThing(OWLClass classeRelated) {
		boolean dejaPresent = isPresent(classeRelated);
		if (!dejaPresent) {
			//System.out.println("On rattache la classe " + classeRelated
			//		+ " à Thing.");
			OWLClass thing = fact.getOWLThing();
			// Si on on ne la trouve pas :
			addSubClassOf(classeRelated, thing);
		}
	}

	/**
	 * Cette méthode permet de retrouver l'IRI correspondant aux classes
	 * présentes dans l'ontologie d'origine
	 * 
	 * @return IRI (partie Scheme et SchemeSpecificPart) des classes
	 */
	public IRI foundIriProject() {

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
		return IRI.create(iriClasseFille.getScheme() + ":"
				+ iriClasseFille.toURI().getSchemeSpecificPart());
	}

	/**
	 * Cette méthode permet d'initialiser l'ontologie cible (SKOS)
	 * 
	 * @param iriOntology
	 *            : Correspond à l'IRI de l'ontologie cible à initialiser
	 */
	public void initTargetOnto(IRI iriOntology) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		try {
			this.targetOntology = manager.createOntology(iriOntology);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.err
					.println("Problème avec l'initialiation de l'ontologie cible.");
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet d'ajouter un commentaire à une classe.
	 * 
	 * @param owlClass
	 *            Classe cible
	 * @param value
	 *            Commentaire à ajouter
	 */
	public void addComment(OWLClass owlClass, String value) {
		OWLAnnotation commentAnno = fact.getOWLAnnotation(
				fact.getRDFSComment(), fact.getOWLLiteral(value));

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(),
				commentAnno);
		this.manager.applyChange(new AddAxiom(targetOntology, ax));
	}

	/**
	 * Cette méthode permet d'ajouter un label à une classe.
	 * 
	 * @param owlClass
	 *            Classe cible.
	 * @param value
	 *            Label à ajouter.
	 */
	public void addLabel(OWLClass owlClass, String value) {
		OWLAnnotation labelAnno = fact.getOWLAnnotation(fact.getRDFSLabel(),
				fact.getOWLLiteral(value));

		OWLAxiom ax = fact.getOWLAnnotationAssertionAxiom(owlClass.getIRI(),
				labelAnno);
		this.manager.applyChange(new AddAxiom(targetOntology, ax));
	}

	/**
	 * Cette méthode ajoute dans l'ontologie cible un axiom de type ObjectProperty
	 * @param iriObjProp IRI de la relation
	 * @param domain Classe correspondante au domain de la relation
	 * @param range Classe correspondante au range de la relation
	 */
	public void addObjectProperty(IRI iriObjProp, OWLClass domain, OWLClass range) {
		// 1 - if broader
		if (iriObjProp != null
				&& iriObjProp.toURI().getFragment().equals("broader")) {
			/**
			 * 1 - On regarde si la classe est SubClassOf Thing Si
			 * c'est le cas, on supprime cet axiome
			 * 
			 * 2 - On la rattache à Thing si elle n'est pas déjà
			 * présente dans l'ontologie 3 - On crée la relation
			 * SubClassOf entre la classe que l'on traite et cette
			 * deuxième classe.
			 */

			// 1 - On regarde si la classe est SubClassOf Thing

			// On la recherche dans le factory
			OWLSubClassOfAxiom rechercheThing = fact
					.getOWLSubClassOfAxiom(domain,
							fact.getOWLThing());
			if (rechercheThing.getSuperClass() != null) {

				RemoveAxiom removeAxiom = new RemoveAxiom(
						targetOntology, rechercheThing);
				manager.applyChange(removeAxiom);
			}

			// 2 - On ajoute classeCree2 à Thing si elle n'existe
			// pas
			// dans l'ontologie
			addToThing(range);

			// 3 - On crée l'axiome SubClassOf entre owlClass et
			// classe2
			addSubClassOf(domain, range);
		}
	}
	
	/**
	 * Cette méthode à partir d'un individu, recherche les relations dans laquelle cet individu intervient
	 * et crée l'ensemble de ces relations (ainsi que les classes associées).
	 * @param individu L'individu que l'on traite.
	 */
	public void addRelation(OWLNamedIndividual individu ) {
		Set<OWLAxiom> listeAxiom = individu
				.getReferencingAxioms(originalOntology);
		for (OWLAxiom curAxiom : listeAxiom) {
			// Ne s'applique qu'au type ObjectPropertyAssertion pour
			// récupérer les broader
			if (curAxiom.getAxiomType().toString()
					.equals("ObjectPropertyAssertion")) {

				// 1 - On récupère le domaine (Subject), on en fait une
				// classe
				// 2 - On récupère le range (Object), on en fait une classe
				// 3 - On récupère la propriété (Property), on en récupère
				// l'IRI

				OWLObjectPropertyAssertionAxiom curAxiom2 = (OWLObjectPropertyAssertionAxiom) curAxiom;

				classePrincipale = fact.getOWLClass(curAxiom2.getSubject()
						.asOWLNamedIndividual().getIRI());

				classeSecondaire = fact.getOWLClass(curAxiom2.getObject()
						.asOWLNamedIndividual().getIRI());

				iriProp = curAxiom2.getProperty().getNamedProperty()
						.getIRI();
				
				addObjectProperty(iriProp, classePrincipale, classeSecondaire);
			}
		}
	}
	
	public void addAnnotation(OWLNamedIndividual individu) {
		Set<OWLAnnotation> listAnnotIndiv = individu.getAnnotations(originalOntology);
		for (OWLAnnotation curAnnotIndiv : listAnnotIndiv) {
			OWLLiteral litteral = (OWLLiteral) curAnnotIndiv.getValue();

			// On transcode en fonction du type d'annotation

			String typeAnnot = curAnnotIndiv.getProperty().getIRI().toURI()
					.getFragment();

			String value = litteral.getLiteral();

			// On crée la classe à laquelle on veut rattacher l'annotation
			OWLClass classeAnot = fact.getOWLClass(iriIndiv);
			
			switch (typeAnnot) {
			case "prefLabel":
				// System.out.println("Transcodage de prefLabel -> label");
				addLabel(classeAnot, value);
				break;
			case "altLabel":
				// System.out.println("Transcodage de altLabel -> label");
				addLabel(classeAnot, value);
				break;
			case "definition":
				// System.out.println("Transcodage de definition -> comment");
				addComment(classePrincipale, value);
				break;
			case "scopeNote":
				// System.out.println("Transcodage de scopeNote -> comment");
				addComment(classePrincipale, value);
				break;
			default:
				System.out.println("Le type " + typeAnnot
						+ " n'est pas supporté.");
				break;
			}
		}
	}
	
	public void createOntology() {

		// On récupère l'iri de l'ontologie cible
		IRI iriProject = foundIriProject();

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
