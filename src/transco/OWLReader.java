package transco;

import java.io.File;
import java.net.URI;
import java.util.Collections;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

/**
 * Cette méthode permet le chargement d'un fichier OWL en mémoire
 * 
 * @author Yoann Keravec<br>
 *         Date: 09/03/2015<br>
 *         Institut Bergonié<br>
 */

public class OWLReader {

	private OWLOntology ontology;
	private OWLOntologyManager manager;
	private OWLOntologyFormat format;

	public OWLOntologyFormat getFormat() {
		return format;
	}

	public void setFormat(OWLOntologyFormat format) {
		this.format = format;
	}

	public OWLOntologyManager getManager() {
		return manager;
	}

	public void setManager(OWLOntologyManager manager) {
		this.manager = manager;
	}

	/**
	 * Cette méthode permet de récupérer l'ontologie chargée.
	 * 
	 * @return Une ontologie.
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Cette méthode permet de charger une ontologie en mémoire.
	 * 
	 * @param ontologie
	 */
	public void setOntologie(OWLOntology ontologie) {
		this.ontology = ontologie;
	}
	
	/**
	 * Cette méthode permet de charger en mémoire une ontologie à partir d'un
	 * fichier OWL Elle vérifie si l'IRI en entrée est listée dans les prefix de
	 * l'ontologie Si ce n'est pas le cas, elle crée un nouveau prefix
	 * correspondant.
	 * 
	 * @param nomFichier
	 *            Fichier OWL
	 * @throws OWLOntologyCreationException
	 */
	public void loadOntology(String nomFichier, String iriOnto, String prefixEntree) {
		manager = OWLManager.createOWLOntologyManager();

		URI uriFichier = new File(nomFichier).toURI();
		IRI ontologyIRIOWL = IRI.create(uriFichier);
		try {
			this.ontology = manager.loadOntology(ontologyIRIOWL);
			// On récupère le formalisme de l'ontologie traitée
			format = manager.getOntologyFormat(ontology);

			// On vérifie s'il s'agit d'un format qui accepte les prefixes.
			// Dans ce cas, on va récupérer la liste de préfix et vérifier
			// S'il y en a un qui correspond à l'IRI indiquée en paramètre.
			// Si il n'y a pas de prefix associé, on l'ajoute

			if (format.isPrefixOWLOntologyFormat()) {
				Set<String> listePrefix = format.asPrefixOWLOntologyFormat()
						.getPrefixNames();
				boolean prefixPresent = false;
				for (String prefix : listePrefix) {
					if (IRI.create(iriOnto).equals(
							format.asPrefixOWLOntologyFormat().getIRI(prefix))) {
						prefixPresent = true;
						break;
					}
				}
				// Si on n'a pas retrouvé de prefix associé, on l'ajoute
				if (!prefixPresent) {
					if (iriOnto.contains("#")) {
						format.asPrefixOWLOntologyFormat().setPrefix(prefixEntree,
								iriOnto);
					} else {
						format.asPrefixOWLOntologyFormat().setPrefix(prefixEntree,
								iriOnto + "#");
					}
				}
			}

			System.out.println("FORMAT : " + format);
			Set<String> nouvelleListe = format.asPrefixOWLOntologyFormat()
					.getPrefixNames();
			System.out.println("PREFIX : "
					+ format.asPrefixOWLOntologyFormat().getPrefixNames());
			for (String pref : nouvelleListe) {
				System.out.println("IRI de " + pref + " : "
						+ format.asPrefixOWLOntologyFormat().getIRI(pref));
			}

		} catch (OWLOntologyCreationException e) {
			System.err
					.println("Erreur lors du chargement de l'ontologie (appel via Manager).");
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet de charger en mémoire une ontologie à partir d'un
	 * fichier OWL Elle vérifie si l'IRI en entrée est listée dans les prefix de
	 * l'ontologie Si ce n'est pas le cas, elle crée un nouveau prefix
	 * correspondant.
	 * 
	 * @param nomFichier
	 *            Fichier OWL
	 * @throws OWLOntologyCreationException
	 */
	public void loadOntology(String nomFichier, String iriOnto) {	
		loadOntology(nomFichier, iriOnto, "bcbs");
	}

	/**
	 * Cette méthode permet de charger en mémoire une ontologie à partir d'un
	 * fichier OWL
	 * 
	 * @param nomFichier
	 *            Fichier OWL
	 * @throws OWLOntologyCreationException
	 */
	public void loadOntology(String nomFichier) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		URI uriFichier = new File(nomFichier).toURI();
		IRI ontologyIRIOWL = IRI.create(uriFichier);
		try {
			this.ontology = manager.loadOntology(ontologyIRIOWL);
			// On récupère le formalisme de l'ontologie traitée
			format = manager.getOntologyFormat(ontology);
		} catch (OWLOntologyCreationException e) {
			System.err
					.println("Erreur lors du chargement de l'ontologie (appel via Manager).");
			e.printStackTrace();
		}
	}

	/**
	 * Cette méthode permet de parcourir une ontologie.
	 */
	public void parcoursWalker() {

		System.out.println("Début du parcours du walker");

		if (ontology.isEmpty()) {
			System.out.println("L'ontologie est vide.");
		} else
			System.out.println("L'ontologie n'est pas vide.");

		// How to walk the asserted structure of an ontology

		// Create the walker
		OWLOntologyWalker walker = new OWLOntologyWalker(
				Collections.singleton(ontology));
		// Now ask our walker to walk over the ontology
		OWLOntologyWalkerVisitor<Object> visitor = new OWLOntologyWalkerVisitor<Object>(
				walker) {
			@Override
			public Object visit(OWLObjectSomeValuesFrom desc) {
				System.out.println(desc);
				System.out.println(" " + getCurrentAxiom());
				return null;
			}
		};
		// Have the walker walk...
		walker.walkStructure(visitor);

	}

}
