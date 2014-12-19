package dsn.commons.development;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import dsn.commons.configuration.ConfigSet;
import dsn.commons.configuration.ConfigType;

public class Debug {
	private final static ConfigSet configSet = new ConfigSet(ConfigType.DEBUG);
	private final static String logPath = configSet.getAttribute("logPath");
	private final static String logDestination = configSet.getAttribute(
			"logDestination").toLowerCase();
	private final static String logItems = configSet.getAttribute("logItems")
			.toLowerCase();

	private enum DebugMessageType {
		DEBUG, INFO, WARNING, ERROR
	}

	private static void logMessage(DebugMessageType type, String message) {
		if (logItems.indexOf(type.toString().toLowerCase()) == -1) {
			// ConfigSet has not initialized, or
			// This type of message is not being logged
			return;
		}

		// Stamp the event with current date and time
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String messageLine = sdf.format(Calendar.getInstance().getTime()) + " "
				+ type.toString() + ": " + message;

		// Determine where to show it
		if (logDestination.indexOf("console") >= 0) {
			System.out.println(messageLine);
		}

		if (logDestination.indexOf("file") >= 0) {
			FileWriter outFile = null;
			PrintWriter out = null;

			try {
				outFile = new FileWriter(logPath, true);
				out = new PrintWriter(outFile);
				out.println(messageLine);
			} catch (IOException ex) {
			} finally {
				try {
					out.close();
				} catch (Exception ex) {
				}
			}

		}
	}

	public static void log(String message) {
		// This will log a debug line
		logMessage(DebugMessageType.DEBUG, message);
	}

	public static void logInfo(String message) {
		// This will log a debug line
		logMessage(DebugMessageType.INFO, message);
	}

	public static void logWarning(String message) {
		// This will log a debug line
		logMessage(DebugMessageType.WARNING, message);
	}

	public static void logError(Exception ex) {
		// Customize the logMessage method
		StringWriter traceWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(traceWriter, false);
		ex.printStackTrace(printWriter);
		printWriter.close();

		String text = traceWriter.getBuffer().toString();
		logMessage(DebugMessageType.ERROR, text);
	}
}
