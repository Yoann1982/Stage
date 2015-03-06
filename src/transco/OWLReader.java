/**
 * 
 */
package transco;

import java.io.File;
import java.net.URI;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Cette méthode permet le chargement d'un fichier OWL en mémoire
 * 
 * @author y.keravec
 *
 */
public class OWLReader {
	
	private OWLOntology ontology;

	
	/**
	 * Cette méthode permet de récupérer l'ontologie chargée
	 * @return
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Cette méthode permet de charger une ontologie en mémoire
	 * @param ontologie
	 */
	public void setOntologie(OWLOntology ontologie) {
		this.ontology = ontologie;
	}


	public void chargeOntology(String nomFichier) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		URI uriFichier = new File(nomFichier).toURI();
		IRI ontologyIRIOWL = IRI.create(uriFichier);
		this.ontology = manager.loadOntology(ontologyIRIOWL);
	}
	
	
	
}
