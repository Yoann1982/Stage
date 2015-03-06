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
 * Cette m�thode permet le chargement d'un fichier OWL en m�moire
 * 
 * @author y.keravec
 *
 */
public class OWLReader {
	
	private OWLOntology ontology;

	
	/**
	 * Cette m�thode permet de r�cup�rer l'ontologie charg�e
	 * @return
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Cette m�thode permet de charger une ontologie en m�moire
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
