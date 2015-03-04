package transco;

//import java.io.ByteArrayOutputStream;
//import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Collections;
import java.util.Iterator;

import org.semanticweb.owlapi.apibinding.OWLManager;
//import org.semanticweb.owlapi.formats.OWLXMLDocumentFormat;
//import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
//import org.semanticweb.owlapi.io.RDFOntologyFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
//import org.semanticweb.owlapi.io.StreamDocumentTarget;
//import org.semanticweb.owlapi.io.StreamDocumentTarget;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
//import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
//import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;
import org.semanticweb.owlapi.util.OWLOntologyWalkerVisitor;

public class WriteOntology {

	OWLOntology ontologie;
	OWLOntologyManager manager;
	
	/**
	 * Chargement à partir d'un OWLOntologie
	 * @param ontoInput
	 * @throws OWLOntologyCreationException
	 */
	public void chargeOntology(OWLOntology ontoInput) throws OWLOntologyCreationException {
		//OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		//IRI pizzaOntologyIRI = IRI
		//		.create("file:/home/yoann/BERGONIE/pizza.owl");
		//OWLOntology redirectedPizza = manager.loadOntology(pizzaOntologyIRI);
		this.ontologie = ontoInput;
	}
	
	/**
	 * Chargement à partir d'un fichier
	 * @param ontoInput
	 * @throws OWLOntologyCreationException
	 */
	public void chargeOntology(String nomFichier) throws OWLOntologyCreationException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		IRI pizzaOntologyIRI = IRI
				.create(nomFichier);
		OWLOntology redirectedPizza = manager.loadOntology(pizzaOntologyIRI);
		this.ontologie = redirectedPizza;
	}

	
	public void writeFile(String nomFichier) throws Exception {
		// Get hold of an ontology manager
		OWLOntologyManager managerWriter = OWLManager.createOWLOntologyManager();
		//this.manager = manager;
		/*
		TranscoSKOSToOWL transco = new TranscoSKOSToOWL();
		transco.transo(nomFichier);
		transco.parcours();
		OWLOntology transcoOntology = transco.getOntology();
		this.pizza.getAnnotations().size();

		Iterator<OWLAnnotation> itAnnot = this.pizza.getAnnotations()
				.iterator();
		while (itAnnot.hasNext()) {
			OWLAnnotation annot = itAnnot.next();
			System.out.println("Annot : " + annot);
		}
		*/
		
		if (this.ontologie.isEmpty()) {
			System.out.println("L'ontologie est vide. Pas de fichier à écrire.");
		} else {
			System.out.println("L'ontologie n'est pas vide. Il est possible d'écrire un fichier OWL.");
		}


		// Now save a local copy of the ontology. (Specify a path appropriate to
		// your setup)
		System.out.println("Nom fichier : " + nomFichier);
		File file = new File(nomFichier);

		// OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
		RDFXMLOntologyFormat rdfXMLFormat = new RDFXMLOntologyFormat();

		// manager.saveOntology(this.pizza, rdfXMLFormat,
		// IRI.create(file.toURI()));
		System.out.println("DEBUG : juste avant que ca plante");
		System.out.println("Format : " + rdfXMLFormat);
		System.out.println("IRI : " + IRI.create(file.toURI()));
		System.out.println("URI : " + file.toURI());
		managerWriter.saveOntology(this.ontologie, rdfXMLFormat,IRI.create(file.toURI()));
		// manager.saveOntology(this.pizza, owlxmlFormat,
		// new StreamDocumentTarget(new ByteArrayOutputStream()));
	}

	public void parcoursWalker() {

		System.out.println("Début du parcours du walker");
		
		if (this.ontologie.isEmpty()) {
			System.out.println("L'ontologie est vide.");
		}else
			System.out.println("L'ontologie n'est pas vide.");
		
		// How to walk the asserted structure of an ontology

		// Create the walker
		OWLOntologyWalker walker = new OWLOntologyWalker(
				Collections.singleton(this.ontologie));
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

	public OWLOntology getOntologie() {
		return ontologie;
	}

	public void setOntologie(OWLOntology ontologie) {
		this.ontologie = ontologie;
	}

}
