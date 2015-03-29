package transco;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;

/**
 * Cette classe permet d'importer une ontologie externe au sein d'une autre ontologie.
 * @author Yoann Keravec<br>
 * Date: 09/03/2015<br>
 * Institut Bergonié<br>
 */

public class Importer {

	private OWLOntology ontology;
	private OWLOntologyFormat format;
	
	public Importer (OWLOntology onto) {
		this.ontology = onto;
		this.format = ontology.getOWLOntologyManager().getOntologyFormat(ontology);
	}
	
	public void setFormat(OWLOntologyFormat format) {
		this.format = format;
	}



	public OWLOntologyFormat getFormat() {
		return format;
	}






	/**
	 * Cette méthode permet d'importer une ontologie externe.
	 * @param ontoExterne L'ontologie à importer.
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
			System.out.println("L'import de l'ontologie "+ontoExterne+ " a échoué.");
			e.printStackTrace();
		}
	}

	/**
	 * Getter de l'attribut ontology.
	 * @return Une Ontologie
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Setter de l'attribut ontology.
	 * @param ontology Une ontologie
	 */
	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}


	
}
