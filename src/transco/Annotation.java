package transco;

/**
 * Cette classe permet de stocker les informations correspondantes aux objets SKOSAnnotation
 * @author y.keravec
 *
 */
public class Annotation {

	/**
	 * Cet attribut correspond au nom de la balise
	 */
	private String tagName = null;
	
	/**
	 * Cet attribut correspond à la valeur de la balise
	 */
	private String tagValue = null;
	
	/**
	 * Cet attribut correspond à la langue
	 */
	private String tagLang = null;

	public Annotation (String tagName, String tagValue, String tagLang) {
		
		this.tagName = tagName;
		this.tagValue = tagValue;
		this.tagLang = tagLang;
	}
	
	/**
	 * Getter du nom de la balise
	 * @return Nom de la balise
	 */
	public String gettagName() {
		return tagName;
	}

	/**
	 * Setter du nom de la balise
	 * @param tagName Nom de la balise
	 */
	public void settagName(String tagName) {
		this.tagName = tagName;
	}

	/**
	 * Getter de la valeur de la balise
	 * @return Valeur de la balise
	 */
	public String gettagValue() {
		return tagValue;
	}

	/**
	 * Setter de la valeur de la balise
	 * @param tagValue Valeur de la balise
	 */
	public void settagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	/**
	 * Getter de la langue de la valeur de la balise
	 * @return Langue de la valeur de la balise
	 */
	public String gettagLang() {
		return tagLang;
	}

	/**
	 * Setter de la langue de la valeur de la balise
	 * @param tagLang Langue de la valeur de la balise
	 */
	public void settagLang(String tagLang) {
		this.tagLang = tagLang;
	}
	
	
	
}
