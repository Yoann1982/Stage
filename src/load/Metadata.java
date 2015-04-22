package load;

import java.util.HashMap;

/**
 * Cette classe contient les méthodes permettant de stocker les informations Metadata I2B2 dans une HashMap.
 * @author Yoann Keravec <br> Date: 19/03/2015<br>
 *         Institut Bergonié<br>
 */

public class Metadata {

	HashMap<String, Object> metadataMap = new HashMap<String, Object>();
	
	public void put(String key, Object value) {
		this.metadataMap.put(key,value);
		
	}
	
	public Object get(String key) {
		return this.metadataMap.get(key);
	}

	public String toString() {
		return metadataMap.toString();
	}
	
}
