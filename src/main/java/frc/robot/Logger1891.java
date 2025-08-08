package frc.robot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class Logger1891 {
    static protected Logger logger4j;
  
    
    public static void getInstance() {
        if (logger4j == null) {
            logger4j = LogManager.getLogger(Logger1891.class);
            System.out.println("finish logger1891 getinstance");
        }
        
    }

    public static void debug(String logLine) {
        try {
            getInstance();
            logger4j.debug(logLine);
        }
        catch (Exception e) {
            System.out.println("exception in debug " + e.getMessage());
        }
    }
    public static void warn(String logLine) {
        try {
            getInstance();
            logger4j.warn(logLine);
        }
        catch (Exception e) {
            System.out.println("exception in warn " + e.getMessage());
        }
    }
    public static void info(String logLine) {
        try {
            getInstance();
            logger4j.info(logLine);
            System.out.println("info: " + logLine);
        }
        catch (Exception e) {
            System.out.println("exception in info " + e.getMessage());
        }
    }
    public static void error(String logLine) {
        try {
            getInstance();
            logger4j.error(logLine);
        }
        catch (Exception e) {
            System.out.println("exception in error " + e.getMessage());
        }
    }
} 