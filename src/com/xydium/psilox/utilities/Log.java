package com.xydium.psilox.utilities;

import java.util.ArrayList;

import com.xydium.psilox.utilities.Log.LogLevel;

public class Log {

	private static LogLevel logLevel = LogLevel.INTERNAL;
	private static ArrayList<String> lines = new ArrayList<String>();
	private static boolean consoleEnabled = true;
	
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

	public static void error(String msg, Object...objects)
	{
		addLine(LogLevel.ERROR, String.format(msg, objects));
	}

	public static void warning(String msg, Object...objects)
	{
		addLine(LogLevel.WARNING, String.format(msg, objects));
	}

	public static void info(String msg, Object...objects)
	{
		addLine(LogLevel.INFO, String.format(msg, objects));
	}

	public static void debug(String msg, Object...objects)
	{
		addLine(LogLevel.DEBUG, String.format(msg, objects));
	}

	public static void internal(String msg, Object...objects)
	{
		addLine(LogLevel.INTERNAL, String.format(msg, objects));
	}

	public static void setLogLevel(LogLevel logLevel)
	{
		Log.logLevel = logLevel;
	}
	
	public static void setLogLevel(String logLevel) {
		Enum.valueOf(LogLevel.class, logLevel);
	}

	public static void setConsoleEnabled(boolean consoleEnabled)
	{
		Log.consoleEnabled = consoleEnabled;
	}

	public static LogLevel getLogLevel()
	{
		return logLevel;
	}

	public static boolean getConsoleEnabled()
	{
		return consoleEnabled;
	}

	private static void addLine(LogLevel level, String msg)
	{
		if(level.ordinal() > Log.logLevel.ordinal()) return;
		if(lines.size() > 1000) lines.clear();
		String line = level.tag + " " + msg;
		lines.add(line);

		if(consoleEnabled)
		{
			if(level == LogLevel.ERROR) {
				System.err.println(line);
			} else {
				System.out.println(line);
			}
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

	public static ArrayList<String> getLines() {
		return lines;
	}
	
}
