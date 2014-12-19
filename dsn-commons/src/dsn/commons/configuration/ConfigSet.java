package dsn.commons.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import dsn.commons.development.Debug;

public class ConfigSet {
	private HashMap<String, String> attributes = new HashMap<String, String>();
	private String filePath;

	public ConfigSet(ConfigType configType) {
		if (configType != ConfigType.DEBUG)
			Debug.logInfo("Loading " + configType + " configuration");

		// Based on config type, open a config file and populate data
		filePath = configType.toString().toLowerCase() + ".config";
		File f = new File(filePath);
		filePath = f.getAbsolutePath();

		try {
			if (f.exists() == false)
				throw new FileNotFoundException();

			FileReader reader = new FileReader(f);
			BufferedReader in = new BufferedReader(reader);

			String line;
			while ((line = in.readLine()) != null) {
				// Retrieve, prune and trim next line
				line = line.replace('\t', ' ').trim();

				// Find a space,
				int space = line.indexOf(' ');

				String attrName, attrValue;

				// Was there a space?
				if (space > 0) {
					attrName = line.substring(0, space).toLowerCase();
					attrValue = line.substring(space).trim();

					// New value overwrites the old value
					attributes.put(attrName, attrValue);
				} else {
					if (configType != ConfigType.DEBUG)
						Debug.logWarning("Config file " + filePath
								+ " has invalid line: " + line);
				}
			}
			in.close();
		} catch (IOException ex) {
			if (configType != ConfigType.DEBUG)
				Debug.logWarning("Config file " + filePath
						+ " not found. Continuing with an empty ConfigSet");
		} catch (Exception ex) {
			if (configType != ConfigType.DEBUG) {
				Debug.logError(ex);
				Debug.logInfo("Continuing with an empty ConfigSet");
			}
		}
	}

	private void save() {
		// Persist the configset
		FileWriter outFile = null;
		PrintWriter out = null;

		try {
			outFile = new FileWriter(filePath);
			out = new PrintWriter(outFile);

			for (Iterator<String> iter = attributes.keySet().iterator(); iter
					.hasNext();) {
				String name, value;
				name = iter.next();
				value = attributes.get(name);
				out.println(name + " " + value);
			}
		} catch (IOException ex) {
			Debug.logError(ex);
		} finally {
			try {
				out.close();
			} catch (Exception ex) {
				Debug.logError(ex);
			}
		}
	}

	public String getAttribute(String name) {
		name = name.toLowerCase();
		// Have we recorded this attribute before?
		if (attributes.containsKey(name))
			return attributes.get(name);

		// Else
		return "";
	}

	private void setAttribute(String name, String value) {
		name = name.toLowerCase();
		// Put this in!
		attributes.put(name, value);
	}
}
