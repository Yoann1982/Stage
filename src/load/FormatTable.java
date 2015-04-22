package load;

/**
 * Cette classe contient les méthodes permettant de stocker les informations concernant le format de la table i2b2 à charger.
 * 
 * @author Yoann Keravec <br> Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class FormatTable {

	private String column;
	private String type;
	private int taille;
	private boolean isNullable;
	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}
	/**
	 * @param column the column to set
	 */
	public void setColumn(String column) {
		this.column = column;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the isNullable
	 */
	public boolean isNullable() {
		return isNullable;
	}
	/**
	 * @param isNullable the isNullable to set
	 */
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	/**
	 * @return the taille
	 */
	public int getTaille() {
		return taille;
	}
	/**
	 * @param taille the taille to set
	 */
	public void setTaille(int taille) {
		this.taille = taille;
	}
	
	
	
}
