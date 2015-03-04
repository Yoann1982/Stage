package transco;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOStoOWLConverter;

public class TranscoSKOSToOWL {

	OWLOntology ontology;
	List<OWLAnnotation> listAnnotationOWL = new ArrayList<OWLAnnotation>();

	/**
	 * Getter de l'ontology
	 * 
	 * @return OWLOntology
	 */
	public OWLOntology getOntology() {
		return ontology;
	}

	/**
	 * Setter de l'ontology
	 * 
	 * @param ontology
	 *            OWLOntology
	 */
	public void setOntology(OWLOntology ontology) {
		this.ontology = ontology;
	}

	public void transo(String nomFichier) {
		// TODO Auto-generated method stub

		SKOSReader reader = new SKOSReader();
		reader.loadFile(nomFichier);

		// Transcodage

		// Création du convertisseur
		SKOStoOWLConverter converter = new SKOStoOWLConverter();

		// De SKOSDataset à OWLOntology
		SKOSDataset dataset = reader.getDataset();
		this.ontology = converter.getAsOWLOntology(dataset);

		// De SKOSAnnotation à OWLAnnotation

		// Parcours de la structure
		// Pour chaque type, on le convertie dans son équivalent OWL
		// et on le stock dans une structure dédiée

		List<ConceptSKOS> listConceptSKOS = reader.getListConceptSKOS();

		// Parcours de la liste de concepts associés
		for (ConceptSKOS conceptCursor : listConceptSKOS) {
			// On récupère la liste d'annotations associés à chaque concept
			List<SKOSAnnotation> listAnnot = conceptCursor.getListAnnotation();

			// On parcours cette liste et on transforme chaque élément en objet
			// OWL

			for (SKOSAnnotation annot : listAnnot) {
				OWLAnnotation annotOWL = converter.getAsOWLAnnotation(annot);
				this.listAnnotationOWL.add(annotOWL);
			}

		}

	}
	
	public void parcours() {
		for (OWLAnnotation cursorAnnot : this.listAnnotationOWL) {
			System.out.println(cursorAnnot.toString());
			
		}
	}
	
}
