package com.xydium.utility;

import java.util.ArrayList;

/**
 * Writes Log messages to an internal buffer and flushes them to
 * text files upon overflow or user request.
 *
 * @author Tim Hornick
 *
 */
public class Log
{
	
	private static LogLevel logLevel = LogLevel.INTERNAL;
	private static ArrayList<String> lines = new ArrayList<String>();
	private static boolean consoleEnabled = true;
	
	/**
	 * Writes to the Error log level
	 *
	 * @param e the Error thrown
	 */
	public static void error(Exception e)
	{
		StringBuilder error = new StringBuilder();
		error.append(e.getClass().getName()).append(": ");
		error.append(e.getLocalizedMessage()).append("\n");
		
		for(StackTraceElement el : e.getStackTrace())
		{
			error.append("\t").append(el.toString()).append("\n");
		}
		
		addLine(LogLevel.ERROR, error.toString());
	}

	/**
	 * Writes to the ERROR log level
	 *
	 * @param msg the msg to write
	 */
	public static void error(String msg)
	{
		addLine(LogLevel.ERROR, msg);
	}

	/**
	 * Writes to the WARNING log level
	 *
	 * @param msg the msg to write
	 */
	public static void warning(String msg)
	{
		addLine(LogLevel.WARNING, msg);
	}

	/**
	 * Writes to the INFO log level
	 *
	 * @param msg the msg to write
	 */
	public static void info(String msg)
	{
		addLine(LogLevel.INFO, msg);
	}

	/**
	 * Writes to the DEBUG log level
	 *
	 * @param msg the msg to write
	 */
	public static void debug(String msg)
	{
		addLine(LogLevel.DEBUG, msg);
	}

	/**
	 * Writes to the INTERNAL log level
	 *
	 * @param msg the msg to write
	 */
	public static void internal(String msg)
	{
		addLine(LogLevel.INTERNAL, msg);
	}


	/**
	 * Sets the maximum log level that the log will output to
	 *
	 * @param logLevel the max log level
	 */
	public static void setLogLevel(LogLevel logLevel)
	{
		Log.logLevel = logLevel;
	}

	/**
	 * Sets whether the log should output to the console or not
	 *
	 * @param consoleEnabled whether the log should output to the console
	 */
	public static void setConsoleEnabled(boolean consoleEnabled)
	{
		Log.consoleEnabled = consoleEnabled;
	}

	/**
	 * Gets the maximum log level that the log outputs to
	 *
	 * @return the max log level
	 */
	public static LogLevel getLogLevel()
	{
		return logLevel;
	}

	/**
	 * Gets whether the log writes to the output console
	 *
	 * @return whether the log writes to the output console
	 */
	public static boolean getConsoleEnabled()
	{
		return consoleEnabled;
	}

	private static void addLine(LogLevel level, String msg)
	{
		if(level.ordinal() > Log.logLevel.ordinal()) return;
		String line = level.tag + " " + msg;
		lines.add(line);

		if(consoleEnabled && level.ordinal() <= Log.logLevel.ordinal())
		{
			System.out.println(line);
		}
	}

	public enum LogLevel
	{
		NONE(""),
		ERROR("[ERROR]"),
		WARNING("[WARNING]"),
		INFO("[INFO]"),
		DEBUG("[DEBUG]"),
		INTERNAL("[INTERNAL]");

		public String tag;

		private LogLevel(String tag)
		{
			this.tag = tag;
		}
	}

	/**
	 * @return the lines
	 */
	public static ArrayList<String> getLines() {
		return lines;
	}

}
