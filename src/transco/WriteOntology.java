package transco;

import java.io.File;
import java.util.Collections;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class WriteOntology {

	OWLOntology ontologie;
	
	
	
	public OWLOntology getOntologie() {
		return ontologie;
	}

	public void setOntologie(OWLOntology ontologie) {
		this.ontologie = ontologie;
	}

	public WriteOntology(OWLOntology onto) {
		this.ontologie = onto;
	}
	
	public void writeFile(String nomFichier)
			throws Exception {
		// Get hold of an ontology manager

		System.out.println("\n");
		System.out
				.println("*************** Traitement d'�criture du fichier OWL *******************");
		System.out.println("");

		OWLOntologyManager managerWriter = OWLManager
				.createOWLOntologyManager();

		if (ontologie.isEmpty()) {
			System.out
					.println("L'ontologie est vide. Pas de fichier � �crire.");
		} else {
			System.out
					.println("L'ontologie n'est pas vide. Il est possible d'�crire un fichier OWL.");
		}

		// Now save a local copy of the ontology. (Specify a path appropriate to
		// your setup)
		File file = new File(nomFichier);

		// OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
		RDFXMLOntologyFormat rdfXMLFormat = new RDFXMLOntologyFormat();

		managerWriter.saveOntology(ontologie, rdfXMLFormat,
				IRI.create(file.toURI()));
		System.out.println("Nom du fichier cr�� : " + nomFichier);

	}

	public void parcoursWalker() {

		System.out.println("Début du parcours du walker");

		if (ontologie.isEmpty()) {
			System.out.println("L'ontologie est vide.");
		} else
			System.out.println("L'ontologie n'est pas vide.");

		// How to walk the asserted structure of an ontology

		// Create the walker
		OWLOntologyWalker walker = new OWLOntologyWalker(
				Collections.singleton(ontologie));
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
