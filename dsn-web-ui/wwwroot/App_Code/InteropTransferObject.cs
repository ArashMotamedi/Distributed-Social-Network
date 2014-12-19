using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

/// <summary>
/// Summary description for InteropTransferObject
/// </summary>
public class InteropTransferObject
{
	private static readonly System.Text.Encoding encoding = System.Text.Encoding.UTF8;
	private Dictionary<String, Object> hash = new Dictionary<String, Object>();

	private static String urlEncode(String str)
	{
		try
		{
			return HttpUtility.UrlEncode(str, encoding);
		}
		catch
		{
			return "";
		}
	}

	private static String urlDecode(String str)
	{
		try
		{
			return HttpUtility.UrlDecode(str, encoding);
		}
		catch
		{
			return "";
		}
	}

	// Method to deserialize string into a Transfer Object
	public static InteropTransferObject deserialize(String str)
	{
		// Prepare a result object
		InteropTransferObject to = new InteropTransferObject();

		try
		{
			// Parse the string
			String[] pairs = str.Split(new char[] { ';' });
			for (int i = 0; i < pairs.Length; i += 1)
			{
				String[] parts = pairs[i].Split(new char[] { ':' });

				// pairParts[0] is the name
				// pairParts[1] is the value

				// is value an array?
				if (parts[1].StartsWith("{") && parts[1].EndsWith("}"))
				{
					// Do the array thing!
					// Remove the curly braces first
					parts[1] = parts[1].Substring(1, parts[1].Length - 2);
					String[] values = parts[1].Split(new char[] { ',' });

					// Decode the values
					for (int j = 0; j < values.Length; j++)
					{
						values[j] = urlDecode(values[j]);
					}

					// Add to the thing!
					to.add(urlDecode(parts[0]), values);
				}
				else
				{
					// Do the single value!
					to.add(urlDecode(parts[0]), urlDecode(parts[1]));
				}
			}
		}
		catch
		{
			// If anything goes wrong, just return the empty thing!
			to = new InteropTransferObject();
		}

		return to;
	}

	public String serialize()
	{
		// Prepare a result string
		String result = "";

		String[] names = hash.Keys.ToArray<String>();
		Object[] values = hash.Values.ToArray<Object>();

		for (int i = 0; i < names.Length; i++)
		{
			result += urlEncode(names[i]) + ":";
			if (values[i].GetType().IsArray)
			{
				result += "{";
				// Now insert all array members
				Object[] vals = (Object[])values[i];
				for (int j = 0; j < vals.Length; j++)
				{
					result += urlEncode(vals[j].ToString());
					if (j < vals.Length - 1)
					{
						result += ",";
					}
				}
				result += "}";
			}
			else
			{
				// It's a single value
				result += urlEncode(values[i].ToString());
			}

			// Need a comma?
			if (i < names.Length - 1)
			{
				result += ";";
			}
		}
		return result;
	}

	// containsKey
	public Boolean containsKey(String name)
	{
		return hash.ContainsKey(name);
	}
	
	// keys
	public String[] getKeys()
	{
		return hash.Keys.ToArray();
	}

	// Method for adding name/value pair
	private void add(String name, Object value) {
		if (name == null)
			throw new Exception("Key cannot be null");
		
		if (value == null)
			throw new Exception("Value cannot be null");
		
		if (hash.ContainsKey(name))
			throw new Exception("Duplicate key specified");
		
		hash.Add(name, value);
	}

	public void add(String name, String value)
	{
		add(name, (Object)value);
	}

	public void add(String name, String[] values)
	{
		add(name, (Object)values);
	}

	public String getValue(String name)
	{
		return (String)hash[name];
	}

	public String[] getValues(String name)
	{
		return (String[])hash[name];
	}
}