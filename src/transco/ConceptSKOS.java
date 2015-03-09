package transco;

import java.util.ArrayList;
import java.util.List;

import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSAnnotation;
import org.semanticweb.skos.SKOSDataProperty;
import org.semanticweb.skos.SKOSObjectProperty;


/**
 * Cette classe permet de stocker les données correspondant à un concept SKOS.
 * @author Yoann Keravec<br>
 * Date: 09/03/2015<br>
 * Institut Bergonié<br>
 */


public class ConceptSKOS {

	private SKOSConcept concept;
	private List<SKOSDataProperty> listDataProperty = new ArrayList<SKOSDataProperty>();
	private List<SKOSObjectProperty> listObjectProperty = new ArrayList<SKOSObjectProperty>();
	private List<SKOSAnnotation> listAnnotation = new ArrayList<SKOSAnnotation>();
	
	public ConceptSKOS(SKOSConcept concept) {
		this.concept = concept;
	}
	
	/**
	 * Getter du concept
	 * @return SKOSConcept
	 */
	public SKOSConcept getConcept() {
		return concept;
	}
	
	/**
	 * Setter du concept
	 * @param concept SKOSConcept
	 */
	public void setConcept(SKOSConcept concept) {
		this.concept = concept;
	}
	
	/**
	 * Getter de la liste d'ObjectProperty
	 * @return List<SKOSObjectProperty>
	 */
	public List<SKOSObjectProperty> getlistObjectProperty() {
		return listObjectProperty;
	}
	
	/**
	 * Setter de la liste d'ObjectProperty
	 * @param listObjectProperty List<SKOSObjectProperty>
	 */
	public void setlistObjectProperty(List<SKOSObjectProperty> listObjectProperty) {
		this.listObjectProperty = listObjectProperty;
	}
	
	/**
	 * Getter de la liste de DataProperty
	 * @return List<DataProperty>
	 */
	public List<SKOSDataProperty> getListDataProperty() {
		return listDataProperty;
	}
	
	/**
	 * Setter de la liste de DataProperty
	 * @param listDataProperty List<SKOSDataProperty>
	 */
	public void setListDataProperty(List<SKOSDataProperty> listDataProperty) {
		this.listDataProperty = listDataProperty;
	}
	
	/**
	 * Getter de la liste d'Annotation
	 * @return List<SKOSAnnotation>
	 */
	public List<SKOSAnnotation> getListAnnotation() {
		return listAnnotation;
	}
	
	/**
	 * Setter Liste d'annotation
	 * @param listAnnotation List<SKOSAnnotation>
	 */
	public void setListAnnotation(List<SKOSAnnotation> listAnnotation) {
		this.listAnnotation = listAnnotation;
	}
	
	
	
	
}
