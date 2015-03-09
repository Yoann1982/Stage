package transco;

import org.semanticweb.skosapibinding.SKOSManager;
import org.semanticweb.skos.*;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe permet le chargement d'un fichier SKOS au sein d'une structure SKOSDataset
 * @author Yoann Keravec<br>
 * Date: 09/03/2015<br>
 * Institut Bergonié<br>
 */
public class SKOSReader {

	private SKOSDataset dataset;
	private List<ConceptSKOS> listConceptSKOS = new ArrayList<ConceptSKOS>();

	/**
	 * Getter du dataset
	 * @return SKOSDataset
	 */
	public SKOSDataset getDataset() {
		return dataset;
	}

	/**
	 * Setter du dataset
	 * @param dataset
	 *            SKOSDataset
	 */
	public void setDataset(SKOSDataset dataset) {
		this.dataset = dataset;
	}

	/**
	 * Getter de la liste de ConceptSKOS
	 * @return List<ConceptSKOS>
	 */
	public List<ConceptSKOS> getListConceptSKOS() {
		return listConceptSKOS;
	}

	/**
	 * Setter de la liste de ConceptSKOS
	 * @param listConceptSKOS
	 *            List<ConceptSKOS>
	 */
	public void setListConceptSKOS(List<ConceptSKOS> listConceptSKOS) {
		this.listConceptSKOS = listConceptSKOS;
	}

	/**
	 * Cette méthode prend un fichier en entrée et charge son contenu dans une
	 * structure SKOSDataset.
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
			System.out.println("Fichier = " + fileInput);
			URI uriFichier = new File(fileInput).toURI();
			this.dataset = manager.loadDataset(uriFichier);

			for (SKOSConcept concept : dataset.getSKOSConcepts()) {
				writeConceptSKOS(concept);
			}

		} catch (SKOSCreationException e) {
			System.err.println("Erreur lors du chargement du fichier "
					+ fileInput + ".");
			e.printStackTrace();
		}

	}

	// End loadFile.

	/**
	 * Cette méthode permet de charger les informations d'un SKOSConcept
	 * la liste de ConceptSKOS de la classe SKOSReader. 
	 * @param conceptSKOS
	 */
	public void writeConceptSKOS(SKOSConcept conceptSKOS) {

		
		ConceptSKOS conceptSKOSLocal = new ConceptSKOS(conceptSKOS);

		// Alimentation de la liste de ObjectProperty
		for (SKOSObjectRelationAssertion objectAssertion : dataset
				.getSKOSObjectRelationAssertions(conceptSKOS)) {
			conceptSKOSLocal.getlistObjectProperty().add(
					objectAssertion.getSKOSProperty());
		}

		// Alimentation de la liste de DataProperty
		for (SKOSDataRelationAssertion assertion : dataset
				.getSKOSDataRelationAssertions(conceptSKOS)) {
			conceptSKOSLocal.getListDataProperty().add(
					assertion.getSKOSProperty());
		}

		// Alimentation de la liste d'annotation
		for (SKOSAnnotation assertion : dataset.getSKOSAnnotations(conceptSKOS)) {
			conceptSKOSLocal.getListAnnotation().add(assertion);
		}

		this.listConceptSKOS.add(conceptSKOSLocal);
	}

}