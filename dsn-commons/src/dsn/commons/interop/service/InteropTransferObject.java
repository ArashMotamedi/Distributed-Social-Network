package dsn.commons.interop.service;

import java.util.HashMap;
import dsn.commons.Utils;

public class InteropTransferObject {
	private HashMap<String, Object> hash = new HashMap<String, Object>();

	// Method to deserialize string into a Transfer Object
	public static InteropTransferObject deserialize(String str) {
		// Prepare a result object
		InteropTransferObject to = new InteropTransferObject();

		try {
			// Parse the string
			String[] pairs = str.split(";");
			for (int i = 0; i < pairs.length; i += 1) {
				String[] parts = pairs[i].split(":");

				// pairParts[0] is the name
				// pairParts[1] is the value

				if (parts.length == 1)
				{
					// Empty value for key
					to.add(Utils.urlDecode(parts[0]), "");
				}
				else
				{
					// is value an array?
					if (parts[1].startsWith("{") && parts[1].endsWith("}")) {
						// Do the array thing!
						// Remove the curly braces first
						parts[1] = parts[1].substring(1, parts[1].length() - 1);
						String[] values = parts[1].split(",");
	
						// Decode the values
						for (int j = 0; j < values.length; j++) {
							values[j] = Utils.urlDecode(values[j]);
						}
	
						// Add to the thing!
						to.add(Utils.urlDecode(parts[0]), values);
					} else {
						// Do the single value!
						to.add(Utils.urlDecode(parts[0]), Utils.urlDecode(parts[1]));
					}
				}
			}
		} catch (Exception ex) {
			// If anything goes wrong, just return the empty thing!
			to = new InteropTransferObject();
		}

		return to;
	}

	public String serialize() {
		// Prepare a result string
		String result = "";
		String[] names = hash.keySet()
				.toArray(new String[hash.keySet().size()]);
		Object[] values = hash.values().toArray();

		for (int i = 0; i < names.length; i++) {
			result += Utils.urlEncode(names[i]) + ":";
			if (values[i].getClass().isArray()) {
				result += "{";
				// Now insert all array members
				Object[] vals = (Object[]) values[i];
				try
				{
					for (int j = 0; j < vals.length; j++) {
						try { result += Utils.urlEncode(vals[j].toString()); }
						catch (Exception e)	{}
						if (j < vals.length - 1) {
							result += ",";
						}
					}
				}
				catch (Exception ex) {}
				result += "}";
			} else {
				// It's a single value
				result += Utils.urlEncode(values[i].toString());
			}

			// Need a comma?
			if (i < names.length - 1) {
				result += ";";
			}
		}
		return result;
	}
	
	public String dump()
	{
		String result = "     ---------- TRANSFER OBJECT DUMP ----------\n";
		String[] keys = this.getKeys();
		
		for (int i = 0; i < keys.length; i++)
		{
			result += "     " + keys[i] + ": ";
			
			try
			{
				result += this.getValue(keys[i]) + "\n";
			}
			catch (Exception ex)
			{
				try
				{
					String[] vals = this.getValues(keys[i]);
					for (int j = 0; j < vals.length; j++)
					{
						result += "\n          " + j + ": " + vals[j];
					}
					result += "\n";
				}
				catch (Exception ex2) {}
			}
		}
		
		result += "     ------------------------------------------";
		
		return result;
	}

	// containsKey
	public boolean containsKey(String name) {
		return hash.containsKey(name);
	}

	// keys
	public String[] getKeys() {
		return hash.keySet().toArray(new String[hash.keySet().size()]);
	}

	// Method for adding name/value pair
	private void add(String name, Object value) throws Exception {
		if (name == null)
			throw new Exception("Key cannot be null");

		if (value == null)
			throw new Exception("Value cannot be null");

		if (hash.containsKey(name))
			throw new Exception("Duplicate key specified");

		hash.put(name, value);
	}

	public void add(String name, String value) throws Exception {
		add(name, (Object) value);
	}

	public void add(String name, String[] values) throws Exception {
		add(name, (Object) values);
	}

	public String getValue(String name) {
		return (String) hash.get(name);
	}

	public String[] getValues(String name) {
		return (String[]) hash.get(name);
	}
}
