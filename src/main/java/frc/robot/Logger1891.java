package frc.robot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public final class Logger1891 {
    protected Logger logger4j;
  
    
    public static void getInstance() {
        logger4j = LogManager.getLogger(Logger1891.class);
        System.out.println("finish logger1891 constructer");
    }

    public static void debug(String logLine) {
        logger4j.debug(logLine);
    }
    public static void warn(String logLine) {
        logger4j.warn(logLine);
    }
    public static void info(String logLine) {
        logger4j.info(logLine);
    }
    public static void error(String logLine) {
        logger4j.error(logLine);
    }
} 