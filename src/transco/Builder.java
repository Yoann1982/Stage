package transco;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAnnotationValue;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.model.RemoveAxiom;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/**
 * Cette classe construit les méthodes et attributs communs aux builder (SKOS &
 * OWL)
 * 
 * @author Yoann Keravec<br>
 *         Date: 10/03/2015<br>
 *         Institut Bergonié<br>
 */

public class Builder {

	protected OWLOntology originalOntology;
	protected OWLDataFactory fact = null;
	protected OWLOntologyManager manager;
	protected OWLOntology targetOntology;
	protected String iriRDFS = "http://www.w3.org/2000/01/rdf-schema#";
	protected String iriSKOS = "http://www.w3.org/2004/02/skos/core#";
	protected PrefixManager prefixSKOS = new DefaultPrefixManager(iriSKOS);
	protected PrefixManager prefixOnto;
	protected OWLOntologyFormat format;
	protected OWLClass classePrincipale = null;
	protected OWLClass classeSecondaire = null;
	protected IRI iriProp = null;
	protected IRI iriIndiv = null;
	protected IRI iriProject = null;

	/**
	 * Getter de l'attribut format
	 * 
	 * @return @see OWLOntologyFormat.
	 */
	public OWLOntologyFormat getFormat() {
		return format;
	}

	/**
	 * Setter de l'attribut format
	 * 
	 * @param @see OWLOntologyFormat format
	 */
	public void setFormat(OWLOntologyFormat format) {
		this.format = format;
	}

	/**
	 * Getter de l'attribut prefixOnto. Il correspond au PrefixManager de
	 * l'ontologie à construire.
	 * 
	 * @return @PrefixManager de l'ontologie.
	 */
	public PrefixManager getPrefixOnto() {
		return prefixOnto;
	}

	/**
	 * Setter de l'attribut prefixOnto. Il correspond au PrefixManager de
	 * l'ontologie à construire.
	 * 
	 * @param @PrefixManager prefixOnto
	 */
	public void setPrefixOnto(PrefixManager prefixOnto) {
		this.prefixOnto = prefixOnto;
	}

	/**
	 * Getter de l'attribut prefixSKOS. Le PrefixManager correspond au préfixe
	 * SKOS.
	 * 
	 * @return @see PrefixManager
	 */
	public PrefixManager getPrefixSKOS() {
		return prefixSKOS;
	}

	/**
	 * Setter de l'attribut prefixSKOS. Le PrefixManager correspond au préfixe
	 * SKOS.
	 * 
	 * @param @see PrefixManager prefixSKOS
	 */
	public void setPrefixSKOS(PrefixManager prefixSKOS) {
		this.prefixSKOS = prefixSKOS;
	}

	/**
	 * Getter de targetOntology.
	 * 
	 * @return L'ontologie cible
	 */
	public OWLOntology getTargetOntology() {
		return targetOntology;
	}

	/**
	 * Setter de targetOntology.
	 * 
	 * @param targetOntology
	 *            L'ontologie cible
	 */
	public void setTargetOntology(OWLOntology targetOntology) {
		this.targetOntology = targetOntology;
	}

	/**
	 * Getter de originalOntology.
	 * 
	 * @return L'ontologie d'origine
	 */
	public OWLOntology getOriginalOntology() {
		return originalOntology;
	}

	/**
	 * Setter de originalOntology.
	 * 
	 * @param ontology
	 *            L'ontologie d'origine
	 */
	public void setOriginalOntology(OWLOntology ontology) {
		this.originalOntology = ontology;
	}

	/**
	 * Getter du OWLDataFactory.
	 * 
	 * @return @see OWLDataFactory
	 */
	public OWLDataFactory getFact() {
		return fact;
	}

	/**
	 * Setter du OWLDataFactory.
	 * 
	 * @param fact
	 * @see OWLDataFactory
	 */
	public void setFact(OWLDataFactory fact) {
		this.fact = fact;
	}

	/**
	 * Getter du manager.
	 * 
	 * @return @see OWLOntologyManager
	 */
	public OWLOntologyManager getManager() {
		return manager;
	}

	/**
	 * Setter du manager.
	 * 
	 * @param manager
	 * @see OWLOntologyManager
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
		else
			return iriClasseFille;
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
	 * @param iriOntology
	 * @see IRI : Correspond à l'IRI de l'ontologie cible à initialiser
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

	/**
	 * Cette méthode permet d'ajouter le prefLable au concept SKOS à partir du
	 * label de la classe OWL d'origine.
	 * 
	 * @param individuClasse
	 * @see OWLIndividual
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

				} else {
					// On gère toutes les autres annotations
					// on récupère la valeur
					annotValeur = curseurAnnot.getValue();

					// 1 - On crée un OWLAnnotationProperty de type
					OWLAnnotationProperty propertyPrefLabel = fact
							.getOWLAnnotationProperty(curseurAnnot
									.getProperty().getIRI().toURI()
									.getFragment(), prefixOnto);

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
	 * @param type
	 * @see IRI
	 * @return OWLClass : la classe associée à l'URI.
	 */
	public OWLClass recupClassByIRI(IRI type) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		return fact.getOWLClass(type);

	}

	/**
	 * Cette méthode permet d'ajouter dans l'ontologie un individu de la classe
	 * indiqué en paramètre Cette classe est défini par un préfixe et un nom de
	 * classe.
	 * 
	 * @param prefixClasse
	 * @see PrefixManager correspondant à la classe.
	 * @param classe
	 *            La classe à laquelle correspond l'individu.
	 * @param individu
	 * @see OWLNamedIndividual l'individu qui instancie la classe (type)
	 */
	public void addClassAssertion(PrefixManager prefixClasse, String classe,
			OWLNamedIndividual individu) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		OWLClassAssertionAxiom classAssertion = fact.getOWLClassAssertionAxiom(
				fact.getOWLClass(classe, prefixClasse), individu);

		manager.addAxiom(targetOntology, classAssertion);

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

		OWLObjectProperty inScheme = fact.getOWLObjectProperty("inScheme",
				prefixSKOS);
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(inScheme,
				classe, scheme);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
	}

	/**
	 * Cette méthode permet d'ajouter un individu dans l'ontologie. L'individu
	 * est défini par son préfixe (prefixIndividu) et son nom (nomIndividu). La
	 * classe qu'il instancie est définie par son préfixe (prefixClasse) et son
	 * nom (classe). Elle retourne l'individu correspondant.
	 * 
	 * @param prefixClasse
	 * @see PrefixManager de la classe de l'individu à ajouter.
	 * @param class nom de la classe de l'individu à ajouter.
	 * @param prefixIndividu
	 * @see PrefixManager de l'individu à ajouter.
	 * @param name
	 *            : Nom de l'individu à ajouter.
	 * @return @see OWLNamedIndividual L'individu ajouté.
	 */
	public OWLNamedIndividual addIndividual(PrefixManager prefixClasse,
			String classe, PrefixManager prefixIndividu, String nameIndividu) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();
		OWLNamedIndividual ind = fact.getOWLNamedIndividual(nameIndividu,
				prefixIndividu);
		addClassAssertion(prefixClasse, classe, ind);
		return ind;
	}

	/**
	 * Cette méthode crée une relation hasBroader entre le concept père et le
	 * concept fils.
	 * 
	 * @param conceptPere
	 * @see OWLIndividual
	 * @param conceptFils
	 * @see OWLIndividual
	 */
	public void addBroader(OWLIndividual conceptFils, OWLIndividual conceptPere) {

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On crée la relation broader

		OWLObjectProperty hasBroader = fact.getOWLObjectProperty("broader",
				prefixSKOS);
		OWLAxiom assertion = fact.getOWLObjectPropertyAssertionAxiom(
				hasBroader, conceptFils, conceptPere);

		AddAxiom addAxiomChange = new AddAxiom(targetOntology, assertion);
		manager.applyChange(addAxiomChange);
	}

	/**
	 * Cette méthode permet d'ajouter un prefix au sein du format
	 * (OWLOntologyFormat) d'une ontologie. Une recherche est effectuée au sein
	 * de l'attribut format de la classe afin de vérifier si un préfixe
	 * correspondant à l'IRI en entrée existe. Si aucun préfixe n'est retrouvé,
	 * un préfixe est ajouté au format.
	 * 
	 * @param @IRI iriProject IRI recherchée
	 * @param @prefixEntree Valeur du préfixe ajouté si l'IRI n'est pas retrouvé
	 *        dans le format.
	 */
	public void addPrefix(IRI iriProject, String prefixEntree) {

		if (format.isPrefixOWLOntologyFormat()) {
			Set<String> listePrefix = format.asPrefixOWLOntologyFormat()
					.getPrefixNames();
			boolean prefixPresent = false;
			for (String prefix : listePrefix) {
				if (iriProject.equals(format.asPrefixOWLOntologyFormat()
						.getIRI(prefix))) {
					prefixPresent = true;
					break;
				}
			}
			// Si on n'a pas retrouvé de prefix associé, on l'ajoute
			if (!prefixPresent) {
				if (iriProject.toString().contains("#")) {
					format.asPrefixOWLOntologyFormat().setPrefix(prefixEntree,
							iriProject.toString());
				} else {
					format.asPrefixOWLOntologyFormat().setPrefix(prefixEntree,
							iriProject.toString() + "#");
				}
			}
		}
	}

	/**
	 * Cette méthode permet d'ajouter une annotation dans l'ontologie cible à la
	 * classe correspondante à l'individu en entrée.
	 * 
	 * @param individu
	 * @see OWLNamedIndividual
	 */
	public void addAnnotation(OWLNamedIndividual individu) {
		Set<OWLAnnotation> listAnnotIndiv = individu
				.getAnnotations(originalOntology);

		for (OWLAnnotation curAnnotIndiv : listAnnotIndiv) {

			// On exclue les annotation qui ne concerne pas des labels
			String typeAnnotation = curAnnotIndiv.getProperty().getIRI()
					.toURI().getFragment();
			if (typeAnnotation.equals("definition")
					|| typeAnnotation.equals("prefLabel")
					|| typeAnnotation.equals("altLabel")
					|| typeAnnotation.equals("scopeNote")) {

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
					addComment(classeAnot, value);
					break;
				case "scopeNote":
					// System.out.println("Transcodage de scopeNote -> comment");
					addComment(classeAnot, value);
					break;
				default:
					// System.out.println("Le type " + typeAnnot
					// + " n'est pas supporté.");
					break;
				}
			}
		}
	}

	/**
	 * Cette méthode permet d'ajouter un axiom SubClassOf.
	 * 
	 * @param classeSub
	 * @see OWLClass
	 * @param superClasse
	 * @see OWLClass
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
	 * @see OWLClass
	 * @return True si présent, False sinon
	 */
	public boolean isPresent(OWLClass classeRecherchee) {

		Set<OWLClass> listClassOnto = targetOntology.getClassesInSignature();
		boolean dejaPresent = false;
		for (OWLClass cls : listClassOnto) {
			if (cls.getIRI().equals(classeRecherchee.getIRI())) {
				dejaPresent = true;
				break;
			}
		}
		return dejaPresent;
	}

	/**
	 * Cette méthode vérifie si l'ontologie contient une classe correspond à
	 * l'URI en entrée Si aucune classe n'est retrouvée, elle est rattachée à
	 * Thing (SubClassOf).
	 * 
	 * @param uriAAjouter
	 * @see URI
	 */
	public void addURIAThing(URI uriAAjouter) {
		OWLClass classeRelated = fact.getOWLClass(IRI.create(uriAAjouter));
		boolean dejaPresent = isPresent(classeRelated);
		if (!dejaPresent) {
			OWLClass thing = fact.getOWLThing();
			// Si on on ne la trouve pas :
			addSubClassOf(classeRelated, thing);
		}
	}

	/**
	 * Cette méthode vérifie si l'ontologie contient une classe correspond à
	 * l'URI en entrée Si aucune classe n'est retrouvée, elle est rattachée à
	 * Thing (SubClassOf).
	 * 
	 * @param uriAAjouter
	 * @see URI
	 */
	public void addToThing(OWLClass classeRelated) {
		boolean dejaPresent = isPresent(classeRelated);
		if (!dejaPresent) {
			OWLClass thing = fact.getOWLThing();
			// Si on on ne la trouve pas :
			addSubClassOf(classeRelated, thing);
		}
	}

	/**
	 * Cette méthode permet d'ajouter un commentaire à une classe.
	 * 
	 * @param owlClass
	 * @see OWLClass Classe cible
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
	 * @see OWLClass Classe cible.
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
	 * Cette méthode ajoute dans l'ontologie cible un axiom de type
	 * ObjectProperty.
	 * 
	 * @param iriObjProp
	 *            IRI de la relation @see IRI
	 * @param domain
	 *            Classe correspondante au domain de la relation @see OWLClass
	 * @param range
	 *            Classe correspondante au range de la relation @see OWLClass
	 */
	public void addObjectProperty(IRI iriObjProp, OWLClass domain,
			OWLClass range) {
		// 1 - if broader
		if (iriObjProp != null
				&& iriObjProp.toURI().getFragment().equals("broader")) {
			/*
			 * 1 - On regarde si la classe est SubClassOf Thing Si c'est le cas,
			 * on supprime cet axiome
			 * 
			 * 2 - On la rattache à Thing si elle n'est pas déjà présente dans
			 * l'ontologie 3 - On crée la relation SubClassOf entre la classe
			 * que l'on traite et cette deuxième classe.
			 */

			// 1 - On regarde si la classe est SubClassOf Thing

			// On la recherche dans le factory
			OWLSubClassOfAxiom rechercheThing = fact.getOWLSubClassOfAxiom(
					domain, fact.getOWLThing());
			if (rechercheThing.getSuperClass() != null) {

				RemoveAxiom removeAxiom = new RemoveAxiom(targetOntology,
						rechercheThing);
				manager.applyChange(removeAxiom);
			}

			// 2 - On ajoute classeCree2 à Thing si elle n'existe
			// pas
			// dans l'ontologie
			addToThing(range);

			// 3 - On crée l'axiome SubClassOf entre domain et
			// range
			addSubClassOf(domain, range);
		}
	}

	/**
	 * Cette méthode à partir d'un individu, recherche les relations dans
	 * laquelle cet individu intervient et crée l'ensemble de ces relations
	 * (ainsi que les classes associées).
	 * 
	 * @param individu
	 *            L'individu que l'on traite. @see OWLNamedIndividual
	 */
	public void addRelation(OWLNamedIndividual individu) {
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

				iriProp = curAxiom2.getProperty().getNamedProperty().getIRI();

				addObjectProperty(iriProp, classePrincipale, classeSecondaire);
			}
		}
	}

	/**
	 * Cette méthode permet de retrouver une annotation d'un individu avec une IRI et le nom de la balise
	 * @param individu
	 * @param iri
	 * @param balise
	 * @return
	 */
	public String getAnnotation(OWLIndividual individu, PrefixManager prefix, String balise) {
		OWLAnnotationValue annotValeur = null;

		// récupération des annotations
		Set<OWLAnnotation> listeAnnot = individu.asOWLNamedIndividual()
				.getAnnotations(originalOntology);
		if (listeAnnot.size() != 0) {
			for (OWLAnnotation curseurAnnot : listeAnnot) {
				if (curseurAnnot.getProperty().getIRI()
						.equals(IRI.create(prefix.getDefaultPrefix() + balise))) {
					// on récupère la valeur
					annotValeur = curseurAnnot.getValue();
					break;
				}
			}
		}
		OWLLiteral valeur = (OWLLiteral) annotValeur;
		if (annotValeur != null)
			return valeur.getLiteral();
		else
			return null;
	}
		
	public OWLNamedIndividual rechercheIndividu(String individu,
			String iriProject) {
		OWLNamedIndividual individuSortie = null;

		Set<OWLNamedIndividual> listIndivOnto = originalOntology
				.getIndividualsInSignature();

		for (OWLNamedIndividual indivCursor : listIndivOnto) {
			if (indivCursor.getIRI().equals(IRI.create(iriProject + individu))) {
				individuSortie = indivCursor;
				break;
			}
		}
		return individuSortie;
	}

	/**
	 * Cette méthode permet de retrouver les individu liés à l'individu en
	 * entrée par la relation op ayant le prefix.
	 * 
	 * @param op
	 * @param individu
	 * @param prefix
	 * @return
	 */
	public List<OWLIndividual> findIndividualsByObjectProperty(String op,
			OWLIndividual individu, PrefixManager prefix) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();

		// On récupère les individus liés à l'individu en entrée par la relation
		// "op" en entrée.

		OWLObjectProperty objProp = fact.getOWLObjectProperty(op, prefix);
		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> mapping = individu
				.getObjectPropertyValues(getOriginalOntology());

		// Recherche dans le mapping avec la clé qui correspond à l'object
		// property
		// On récupère la liste d'individual qui sont liés à l'invidu en entrée
		// par l'object property
		Set<OWLIndividual> listeIndiv = null;
		if (mapping != null && !mapping.isEmpty()) {
			listeIndiv = mapping.get(objProp);
		}
		if (listeIndiv != null && !listeIndiv.isEmpty())
			return new ArrayList<OWLIndividual>(listeIndiv);
		else
			return null;
	}

	/**
	 * Cette méthode renvoie la liste d'individu narrower de l'individu en
	 * entrée.
	 * 
	 * @param individu
	 * @return
	 */
	public List<OWLIndividual> getNarrowers(OWLIndividual individu) {

		List<OWLIndividual> listeIndiv = null;
		listeIndiv = findIndividualsByObjectProperty("narrower", individu,
				prefixSKOS);
		return listeIndiv;
	}

	public List<OWLIndividual> findSchemeByObjectProperty(String op,
			PrefixManager prefix) {

		// On récupère les individus liés à l'individu en entrée par la relation
		// "op" en entrée.

		// On récupère la liste des individus de l'ontologie
		Set<OWLNamedIndividual> listeIndividu = originalOntology
				.getIndividualsInSignature();
		List<OWLIndividual> listeIndividuSortie = new ArrayList<OWLIndividual>();

		// Pour chacun des individus, on récupère la liste des Axioms de
		// l'ontologie dans lequel il intervient
		for (OWLNamedIndividual curseurIndiv : listeIndividu) {

			Set<OWLIndividualAxiom> listeAxiom = originalOntology
					.getAxioms(curseurIndiv);
			for (OWLIndividualAxiom curAxio : listeAxiom) {
				// On ne traite que les Class Assertion pour rechercher les
				// scheme.
				if (curAxio.isOfType(AxiomType.CLASS_ASSERTION)) {

					// on change le type en OWLClassAssertionAxiom pour pouvoir
					// utiliser ses méthodes
					OWLClassAssertionAxiom classAxiom = (OWLClassAssertionAxiom) curAxio;
					// On ne traite que les concept Scheme.
					if (classAxiom.getClassExpression().asOWLClass().getIRI()
							.equals(IRI.create(prefix.getDefaultPrefix() + op))) {

						listeIndividuSortie.add(classAxiom.getIndividual());
						// on sort de la boucle for curAxio car il ne peut avoir
						// plus d'une occurence pour le type op
						break;
					}
				}
			}
		}
		return listeIndividuSortie;
	}

}
