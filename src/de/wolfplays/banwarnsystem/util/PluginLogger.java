package de.wolfplays.banwarnsystem.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class PluginLogger {
	
	private static PrintWriter pWriter;
	public static File logfile;
	
	public PluginLogger(File file, String logFileName) {
		logfile = new File(file, logFileName);
		if(!logfile.exists()) {
			try {
				pWriter = new PrintWriter(new FileWriter(logfile, true), true);
				logfile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void log(LogSettings settings, String toLog) {
		Date date = new Date();
		if(settings.equals(LogSettings.NOTHING)) {
			pWriter.println("[ " + date.toString() + " ]" + toLog);
		} else {
			pWriter.println("[ " + date.toString() + " " + settings.getName() + " ]" + toLog);
		}
        pWriter.flush();
	}
	
	public void logCustom(String settings, String toLog, boolean withDate) {
		Date date = new Date();
		if(withDate) {
			if(settings.equals("")) {
				pWriter.println("[ " + date.toString() + " ]" + toLog); 
			} else {
				pWriter.println("[ " + date.toString() + " " + settings + " ]" + toLog); 
			} 
		} else {
			if(settings.equals("")) {
				pWriter.println(toLog); 
			} else {
				pWriter.println("[ " + settings + " ]" + toLog); 
			}
		}
        pWriter.flush();
	}

	public static enum LogSettings {
		NOTHING(""),
		INFO("INFO"),
		WARN("WARN"),
		ERROR("ERROR");

		private String name;

		private LogSettings(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
	
}