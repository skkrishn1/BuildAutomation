package com.ap.ui.portal.installation.command.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the model containing the 
 * data in form of key-value pair.
 *
 * @author Abhishek Tembe
 *
 */
public class AbstractModel {

	/**
	 * Data-structure to hold the key value pairs. 
	 */
	private Map<String, KeyValuePairModel> _values = new HashMap<String, KeyValuePairModel>(); 
	
	/**
	 * Adds a bunch of attributes to the model. Here the key is retreived
	 * from the {@link KeyValuePairModel} object itself. 
	 * @param attributes list of {@link KeyValuePairModel} objects.
	 */
	public void addAttributes (List<KeyValuePairModel> attributes)  {
		if (attributes != null) {
			for (KeyValuePairModel attribute : attributes) {
				String key = attribute.getKey();
				addAttribute(key, attribute);
			}
		}
	}
	
	/**
	 * Adds a new attribute to the model. 
	 * @param key key against which the attribute is to be stored.
	 * @param attribute attribute, {@link KeyValuePairModel} instance.
	 */
	public void addAttribute(String key, KeyValuePairModel attribute) {
		_values.put(attribute.getKey(), attribute);
		
	}

	/**
	 * Retrieves a {@link KeyValuePairModel} attribute from the model.
	 * @param key key of the attribute
	 * @return {@link KeyValuePairModel} instance.
	 */
	public KeyValuePairModel getAttribute (String key) {
		return _values.get(key);
	}
	
	/**
	 * Copies all the {@link KeyValuePairModel} attributes
	 * into the current instance.
	 * @param input {@link AbstractModel} instance to copy
	 * 			 	attributes.
	 */
	public void copyAttributes(AbstractModel input) {
		
		if (input == null || input._values == null) {
			return;
		}
		
		Map<String, KeyValuePairModel> _inAttributeMap = input._values;
		Set<String> _inKeys = _inAttributeMap.keySet();
		for (String key : _inKeys) {
			KeyValuePairModel value = _inAttributeMap.get(key);
			addAttribute(key, value);
		}
	}
	
	/**
	 * Returns String representation of the key-values added in this object.
	 * @return String representation of the key-values added in this object.
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String values = (_values != null)?_values.toString():"";
		String toString = new StringBuilder()
							.append(this.getClass().getName())
							.append("=> Values:")
							.append(values)
							.append("}")
						  .toString(); 
		return toString;
	}
}
