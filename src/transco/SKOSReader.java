package transco;

import org.semanticweb.skosapibinding.SKOSManager;
import org.semanticweb.skos.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/*
 * Cette classe permet le chargement d'un fichier SKOS au d'une structure SKOSDataset
 */

/**
 * Author: Yoann Keravec<br>
 * Date: 25/02/2015<br>
 * Institut Bergonié<br>
 */
public class SKOSReader {

	private SKOSDataset dataset;
	private List<Concept> listConcept = new ArrayList<Concept>();
	private List<ConceptSKOS> listConceptSKOS = new ArrayList<ConceptSKOS>();

	/**
	 * Getter du dataset
	 * 
	 * @return SKOSDataset
	 */
	public SKOSDataset getDataset() {
		return dataset;
	}

	/**
	 * Setter du dataset
	 * 
	 * @param dataset
	 *            SKOSDataset
	 */
	public void setDataset(SKOSDataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Getter de la liste de concept
	 * 
	 * @return Une liste de concept
	 */
	public List<Concept> getListConcept() {
		return listConcept;
	}

	/**
	 * Setter de la liste de concept
	 * 
	 * @param listConcept
	 *            Une liste de concept
	 */
	public void setListConcept(List<Concept> listConcept) {
		this.listConcept = listConcept;
	}

	/**
	 * Getter de la liste de ConceptSKOS
	 * 
	 * @return List<ConceptSKOS>
	 */
	public List<ConceptSKOS> getListConceptSKOS() {
		return listConceptSKOS;
	}

	/**
	 * Setter de la liste de ConceptSKOS
	 * 
	 * @param listConceptSKOS
	 *            List<ConceptSKOS>
	 */
	public void setListConceptSKOS(List<ConceptSKOS> listConceptSKOS) {
		this.listConceptSKOS = listConceptSKOS;
	}

	/**
	 * Cette méthode prend un fichier en entrée et charge son contenu dans une
	 * structure SKOSDataset
	 * 
	 * @param fileInput
	 */

	public void loadFile(String fileInput) {

		try {

			// First create a new SKOSManager
			SKOSManager manager = new SKOSManager();

			// use the manager to load a SKOS vocabulary from a URI (either
			// physical or on the web)

			// SKOSDataset dataset =
			// manager.loadDataset(URI.create("file:/home/yoann/BERGONIE/canals.skos"));
			System.out.println("Fichier = "+fileInput );
			URI uriFichier = new File(fileInput).toURI();
			this.dataset = manager.loadDataset(uriFichier);

			for (SKOSConcept concept : dataset.getSKOSConcepts()) {
				writeConcept(concept);
				writeConceptSKOS(concept);
			}

		} catch (SKOSCreationException e) {
			e.printStackTrace();
		}

	}

	// End loadFile.

	/**
	 * Cette méthode parcours le fichier SKOS et charge son contenu dans la
	 * liste de concept de la classe SKOSReader
	 */
	public void writeConcept(SKOSConcept conceptSKOS) {

		System.out.println("Concept: " + conceptSKOS.getURI());
		
		Concept conceptLocal = new Concept(conceptSKOS);
		List<Annotation> listAnnot = new ArrayList<Annotation>();

		// On parcours les annotations
		for (SKOSAnnotation assertion : dataset.getSKOSAnnotations(conceptSKOS)) {

			// if the annotation is a literal annotation?
			String lang = "";
			String value = "";

			if (assertion.isAnnotationByConstant()) {

				SKOSLiteral literal = assertion.getAnnotationValueAsConstant();
				value = literal.getLiteral();
				if (!literal.isTyped()) {
					// if it has language
					SKOSUntypedLiteral untypedLiteral = literal
							.getAsSKOSUntypedLiteral();
					if (untypedLiteral.hasLang()) {
						lang = untypedLiteral.getLang();
					}
				}
			} else {
				// annotation is some resource
				SKOSEntity entity = assertion.getAnnotationValue();
				value = entity.getURI().getFragment();
			}
			// On crée un objet Annotation
			Annotation annotationOutput = new Annotation(assertion.getURI()
					.getFragment(), value, lang);

			// on ajoute l'annotation à la liste locale
			listAnnot.add(annotationOutput);
			System.out.println("\t\t" + assertion.getURI().getFragment() + " "
					+ value + " Lang:" + lang);
		}
		// On ajoute la liste d'annotation au concept local
		conceptLocal.setAnnotationList(listAnnot);
		// On ajoute le concept local à la liste attribut de la classe
		this.listConcept.add(conceptLocal);
	}

	// End writeConcept

	public void writeConceptSKOS(SKOSConcept conceptSKOS) {

		//
		ConceptSKOS conceptSKOSLocal = new ConceptSKOS(conceptSKOS);

		// Alimentation de la liste de ObjectProperty
		for (SKOSObjectRelationAssertion objectAssertion : dataset.getSKOSObjectRelationAssertions(conceptSKOS)) {
			conceptSKOSLocal.getlistObjectProperty().add(objectAssertion.getSKOSProperty());
		}

		// Alimentation de la liste de DataProperty
		for (SKOSDataRelationAssertion assertion : dataset
				.getSKOSDataRelationAssertions(conceptSKOS)) {
			conceptSKOSLocal.getListDataProperty().add(assertion.getSKOSProperty());
		}

		// Alimentation de la liste d'annotation
		for (SKOSAnnotation assertion : dataset.getSKOSAnnotations(conceptSKOS)) {
			conceptSKOSLocal.getListAnnotation().add(assertion);
		}

		this.listConceptSKOS.add(conceptSKOSLocal);
	}

	public static void main(String[] arg) {

		SKOSReader reader = new SKOSReader();
		reader.loadFile("file:/home/yoann/BERGONIE/canals.skos");
	}

}