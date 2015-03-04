package transco;

import java.util.List;

import org.semanticweb.skos.SKOSConcept;

public class Concept {

	private SKOSConcept concept = null;
	private List<ObjectProperty> objectProperty = null;
	private List<DataProperty> dataProperty = null;
	private List<Annotation> annotationList = null;
	
	public Concept (SKOSConcept conceptInput) {
		this.concept = conceptInput;
	}
	
	/**
	 * Getter du SKOSConcept
	 * @return le SKOSConcept associé
	 */
	public SKOSConcept getConcept() {
		return concept;
	}
	
	/**
	 * Setter du SKOSConcept
	 * @param concept SKOSConcept
	 */
	public void setConcept(SKOSConcept concept) {
		this.concept = concept;
	}
	
	/**
	 * Getter de la liste d'ObjectProperty
	 * @return Une liste d'ObjectProperty
	 */
	public List<ObjectProperty> getObjectProperty() {
		return objectProperty;
	}
	
	/**
	 * Setter de la liste d'ObjectProperty
	 * @param objectProperty Une liste d'ObjectProperty
	 */
	public void setObjectProperty(List<ObjectProperty> objectProperty) {
		this.objectProperty = objectProperty;
	}
	
	/**
	 * Getter de DataProperty
	 * @return DataProperty
	 */
	public List<DataProperty> getDataProperty() {
		return dataProperty;
	}
	
	/**
	 * Setter de la liste de DataProperty
	 * @param dataProperty
	 */
	public void setDataProperty(List<DataProperty> dataProperty) {
		this.dataProperty = dataProperty;
	}
	
	/**
	 * Getter de la liste d'Annotation
	 * @return
	 */
	public List<Annotation> getAnnotationList() {
		return annotationList;
	}
	
	/**
	 * Setter de la liste d'Annotation
	 * @param annotationList Liste d'Annotation
	 */
	public void setAnnotationList(List<Annotation> annotationList) {
		this.annotationList = annotationList;
	}
	
	
	
}
