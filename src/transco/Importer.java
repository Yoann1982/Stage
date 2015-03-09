package transco;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Cette méthode permet d'importer une ontologie externe au sein d'une autre ontologie.
 * @author Yoann Keravec<br>
 * Date: 09/03/2015<br>
 * Institut Bergonié<br>
 */

public class Importer {

	private OWLOntology ontology;
	
	public Importer (OWLOntology onto) {
		this.ontology = onto;
	}
	
	/**
	 * Cette méthode permet d'importer une ontologie externe
	 */
	public void importOnto(String ontoExterne) {
		
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLDataFactory fact = manager.getOWLDataFactory();
		
		IRI toImport=IRI.create(ontoExterne);
		
		OWLImportsDeclaration importDeclaraton = fact.getOWLImportsDeclaration(toImport);
		this.ontology.getOWLOntologyManager().applyChange(new AddImport(this.ontology, importDeclaraton));
		try {
			this.ontology.getOWLOntologyManager().loadOntology(toImport);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			System.out.println("L'import de l'ontologie "+ontoExterne+ " a échoué.");
			e.printStackTrace();
		}
	}
	
}
