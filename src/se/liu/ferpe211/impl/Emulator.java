package se.liu.ferpe211.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.disassembler.Disassembler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The Emulator class represents an emulator that emulates a 6502 processor (CPU)
 */

public class Emulator
{
    /**
     * Represents a logger for the Emulator class.
     * The logger is used to log messages and exceptions
     * It is a static final logger initialized with the name of the Emulator class.
     */
    public static final Logger LOGGER = createLogger();

    private CPU cpu;
    private Disassembler disassembler;

    public CPU getCpu() {
	return cpu;
    }

    public Disassembler getDisassembler() {
	return disassembler;
    }

    public Emulator(){
	cpu = new CPU();
	this.disassembler = new Disassembler(cpu);
    }

    /**
     * Method for creating a logger object.
     * The logger is created with the name of the Emulator class.
     * The logger is configured to log to a file named "logfile-YYYY-MM-DD_HH-MM-SS.log" in the "logs" directory.
     * @return Logger
     */
    private static Logger createLogger() {
	Logger logger = Logger.getLogger(Emulator.class.getName());
	logger.setLevel(Level.INFO);

	try {
	    File logDir = new File("logs");
	    if (!logDir.exists()) {
		if (!logDir.mkdir()) {
		    LOGGER.log(Level.SEVERE, "Failed to create log directory");
		}
	    }
	    LocalDateTime currentTime = LocalDateTime.now();
	    String fileName = String.format("logs%1$slogfile-%2$04d-%3$02d-%4$02d_%5$02d-%6$02d-%7$02d.log",
					    File.separator,
					    currentTime.getYear(), currentTime.getMonthValue(), currentTime.getDayOfMonth(),
					    currentTime.getHour(), currentTime.getMinute(), currentTime.getSecond());

	    FileHandler fileHandler = new FileHandler(fileName);
	    fileHandler.setFormatter(new SimpleFormatter());
	    logger.addHandler(fileHandler);
	} catch (IOException e) {
	    logger.log(Level.SEVERE, "Error initializing file handler", e);
	}
	return logger;
    }



}
