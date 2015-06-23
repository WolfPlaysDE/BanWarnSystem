package de.wolfplays.banwarnsystem.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import de.wolfplays.banwarnsystem.BanWarnSystem;

public class PluginLogger {

	private static PrintWriter pWriter;
	public static File logfile = new File(BanWarnSystem.getInstance().getDataFolder(), "log.txt");
	
	static {
		try {
			pWriter = new PrintWriter(new FileWriter(logfile, true), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadLog() {
		if(!logfile.exists()) {
				try {
					logfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	public static void log(String toLog) {
		Date date = new Date();
		pWriter.println("[ " + date.toString() + " ] " + toLog); 
        pWriter.flush();
	}
	
}
