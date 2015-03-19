package load;

import java.util.HashMap;

public class Metadata {

	HashMap metadataMap = new HashMap();
	
	public void put(String key, Object value) {
		this.metadataMap.put(key,value);
		
	}
	
	public Object get(String key) {
		return this.metadataMap.get(key);
	}
	
}
