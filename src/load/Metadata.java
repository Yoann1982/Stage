package load;

import java.util.HashMap;

public class Metadata {

	HashMap<String, Object> metadataMap = new HashMap<String, Object>();
	
	public void put(String key, Object value) {
		this.metadataMap.put(key,value);
		
	}
	
	public Object get(String key) {
		return this.metadataMap.get(key);
	}
	
}
