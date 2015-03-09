package transco;

import java.io.File;
import java.net.URI;
import java.util.Collections;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

/**
 * Cette méthode permet le chargement d'un fichier OWL en mémoire
 * @author Yoann Keravec<br>
 * Date: 09/03/2015<br>
 * Institut Bergonié<br>
 */

public class OWLReader {

	private OWLOntology ontology;

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
	 * Cette méthode permet de charger en mémoire une ontologie à partir d'un fichier OWL
	 * @param nomFichier Fichier OWL
	 * @throws OWLOntologyCreationException
	 */
	public void chargeOntology(String nomFichier) {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		URI uriFichier = new File(nomFichier).toURI();
		IRI ontologyIRIOWL = IRI.create(uriFichier);
		try {
			this.ontology = manager.loadOntology(ontologyIRIOWL);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.err.println("Erreur lors du chargement de l'ontologie (appel via Manager).");
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
