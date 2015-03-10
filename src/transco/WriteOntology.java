package transco;

import java.io.File;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/**
 * Cette classe permet d'écrire un fichier RDF/OWL
 * 
 * @author Yoann Keravec<br>
 *         Date: 09/03/2015<br>
 *         Institut Bergonié<br>
 */

public class WriteOntology {

	private OWLOntology ontology;

	/**
	 * Constructeur de la classe WriteOntology.
	 * 
	 * @param ontologie
	 */
	public WriteOntology(OWLOntology ontologie) {
		this.ontology = ontologie;
	}

	/**
	 * Getter de Ontology.
	 * 
	 * @return
	 */
	public OWLOntology getOntologie() {
		return ontology;
	}

	/**
	 * Setter de Ontology.
	 * 
	 * @param ontologie
	 */
	public void setOntologie(OWLOntology ontologie) {
		this.ontology = ontologie;
	}

	/**
	 * Cette méthode permet d'écrire un fichier RDF/OWL.
	 * 
	 * @param nomFichier
	 *            Nom du fichier à écrire.
	 */
	public void writeFile(String nomFichier) {
		// Get hold of an ontology manager

		if (ontology != null) {

			System.out.println("\n");
			System.out
					.println("*************** Traitement d'écriture du fichier OWL *******************");
			System.out.println("");

			OWLOntologyManager managerWriter = OWLManager
					.createOWLOntologyManager();

			if (ontology.isEmpty()) {
				System.out
						.println("L'ontologie est vide. Pas de fichier à écrire.");
			} else {
				System.out
						.println("L'ontologie n'est pas vide. Il est possible d'écrire un fichier OWL.");
			}

			// Now save a local copy of the ontology. (Specify a path
			// appropriate to
			// your setup)
			File file = new File(nomFichier);

			RDFXMLOntologyFormat rdfXMLFormat = new RDFXMLOntologyFormat();

			// OWLXMLOntologyFormat owlXMLFormat = new OWLXMLOntologyFormat();

			try {
				managerWriter.saveOntology(ontology, rdfXMLFormat,
						IRI.create(file.toURI()));
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de l'écriture du fichier RDF.");
				e.printStackTrace();
			}
			System.out.println("Nom du fichier créé : " + nomFichier);
		}
	}
}
