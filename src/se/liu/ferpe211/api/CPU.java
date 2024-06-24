package se.liu.ferpe211.api;

import se.liu.ferpe211.api.configuration.Config;
import se.liu.ferpe211.api.configuration.settings.SettingsManager;
import se.liu.ferpe211.api.configuration.settings.impl.SettingBoolean;
import se.liu.ferpe211.api.configuration.settings.impl.SettingDouble;
import se.liu.ferpe211.api.event.CPUListener;
import se.liu.ferpe211.api.event.EventType;
import se.liu.ferpe211.api.memory.RAM;
import se.liu.ferpe211.api.operation.OperationManager;
import se.liu.ferpe211.api.operation.instruction.InstructionProvider;
import se.liu.ferpe211.api.register.IndexRegisterType;
import se.liu.ferpe211.api.register.ProcessorStatus;
import se.liu.ferpe211.api.register.ProgramCounter;
import se.liu.ferpe211.api.register.Register;
import se.liu.ferpe211.api.register.Register8Bit;
import se.liu.ferpe211.impl.Emulator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * Class representation of the 6502 CPU
 * The CPU class is responsible for executing a program. It has a memory, a program counter, a stack pointer,
 * an accumulator, a processor status register, an index register and a list of registers.
 */
public class CPU
{
    /** Registers */
    private ProgramCounter programCounter;
    private Register8Bit stackPointer;
    private Register8Bit accumulator;
    private ProcessorStatus processorStatus;
    private Register8Bit indexRegisterX;
    private Register8Bit indexRegisterY;

    private volatile boolean executing;

    private RAM memory;

    private List<CPUListener> listeners;

    private List<Register> registers;

    public OperationManager getOperationManager() {
        return operationManager;
    }

    private OperationManager operationManager;
    private SettingsManager settingsManager;

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }

    public Register8Bit getStackPointer() {
        return stackPointer;
    }
    public ProgramCounter getProgramCounter() {
        return programCounter;
    }

    public Register8Bit getAccumulator() {
        return accumulator;
    }

    public ProcessorStatus getProcessorStatus() {
        return processorStatus;
    }

    public List<Register> getRegisters() {
        return registers;
    }

    public Register8Bit getIndexRegister(IndexRegisterType indexRegisterType){
        switch (indexRegisterType){
            case X -> {
                return indexRegisterX;
            }
            default -> {
                return indexRegisterY;
            }
        }
    }


    public RAM getMemory() {
        return memory;
    }

    public void setExecuting(final boolean executing) {
        this.executing = executing;
    }

    public CPU(){
        Emulator.LOGGER.log(Level.INFO, "Creating CPU");
        memory = new RAM();
        programCounter = new ProgramCounter("Program Counter");
        stackPointer = new Register8Bit("Stack Pointer");
        accumulator = new Register8Bit("Accumulator");
        processorStatus = new ProcessorStatus("Processor Status Register");
        indexRegisterX = new Register8Bit("Index Register X");
        indexRegisterY = new Register8Bit("Index Register Y");
        executing = false;
        operationManager = new OperationManager(this);

        registers = new ArrayList<>();
        registers.add(programCounter);
        registers.add(stackPointer);
        registers.add(accumulator);
        registers.add(indexRegisterX);
        registers.add(indexRegisterY);
        registers.add(processorStatus);

        listeners = new ArrayList<>();
        settingsManager = new SettingsManager(this);
        this.populateSettings();
	Config config = new Config("config", settingsManager);
        this.addListener(config);
        this.reset();
    }

    public void pushByte(byte value) {
        memory.writeByte(Byte.toUnsignedInt(stackPointer.getValue()), value);
        stackPointer.decrement();
    }

    public byte pullByte() {
        stackPointer.increment();
        return memory.readByte(Byte.toUnsignedInt(stackPointer.getValue()));
    }

    /**
     * Method for populating the settingsmanager with settings
     * The defined settings have a default value defined in their Settings classes for their types e.g {@link SettingDouble}
     * where default is 0
     */
    private void populateSettings(){
        SettingDouble cycleRate = new SettingDouble("CycleRate", "How fast cycles should be executed");
        settingsManager.register(cycleRate);

        SettingBoolean patching = new SettingBoolean("Patching", "Enable patching of memory");
        settingsManager.register(patching);
    }

    /**
     * Method for resetting the CPU
     * The program counter is reset, clears the processor status flags aswell as sets the stackpointer
     * The memory is also initilized here.
     */
    public void reset(){
        programCounter.setValue(0x0000);
        stackPointer.setValue((byte) 0xFF);
        processorStatus.setValue((byte) 0x0000);

        executing = false;

        this.memory.init();
        notifyListeners(EventType.CPU_RESET);

    }

    /**
     * Method for executing next instruction in memory with respect to the program counter
     * @return the amount of cycles that the instruction takes to execute
     * @see #executeThread() to see how the cycles are used to simulate cycle delay based on cyclerate
     */
    public int executeNextInstruction(){
            byte instructionByte = memory.readByte(programCounter.getValue());
            programCounter.increment();
            InstructionProvider instruction = operationManager.getInstruction(instructionByte);
            instruction.execute();
            notifyListeners(EventType.INSTRUCTION_EXECUTED);
            return instruction.getCycles();
    }

    /**
     * Method for executing the instructions in the memory until we reach BREAK
     * A seperate thread is constructed in orer to simulate the delay for each instruction based on their cycles
     * The new thread runs an executor service which keeps scheduling itself until executing is false
     * the thread then monitors the shutdown of the executorservice in order to keep it alive until its actually shutdown since
     * ExecutorServic is autoclosable since java 19.
     * and the cycle rate defined in settings.
     */
    public void executeThread() {
        executing = true;
        double cycleRateDouble = (double) settingsManager.getSetting("cyclerate").getValue();
        int cycleRate = (int) Math.round(cycleRateDouble);
        Thread schedulerThread = new Thread(() -> {
            try (ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor()) {
                Runnable task = new Runnable()
                {
                    @Override public void run() {
                        if (executing) {
                            int cycles = executeNextInstruction();
                            scheduler.schedule(this, (long) cycles * cycleRate, TimeUnit.MILLISECONDS);
                        } else {
                            scheduler.shutdown();
                        }
                    }
                };

                scheduler.schedule(task, 0, TimeUnit.MILLISECONDS);

                while (!scheduler.isTerminated()) {
                    try {
                        scheduler.awaitTermination(1, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        Emulator.LOGGER.log(Level.SEVERE, "Couldnt terminate execution thread successfully: " + e);
                        Thread.currentThread().interrupt();
                        break;
                    }
                }

            }
        });
        schedulerThread.start();
    }

    /**
     * Method for executing the instructions in the memory until we reach BREAK
     * This is done on the main thread and is mainly used in the tests
     */
    public void execute(){
        executing = true;
        while (executing) {
            this.executeNextInstruction();
        }
    }

    /**
     * Method for loading a binary file into memory
     * @param file to be loaded into memory
     */
    public void loadFile(File file){
	try {
	    byte[] bytes = Files.readAllBytes(file.toPath());
            this.getMemory().setData(bytes);
            notifyListeners(EventType.MEMORY_CHANGED);
	} catch (IOException e) {
            Emulator.LOGGER.log(Level.SEVERE, "Error loading binary file", e);
	}
    }

    /**
     * Method for exporting the current memory into a binary file
     * @param file location for where the file should be saved
     */
    public void dump(File file){
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            for (byte b : memory.getData()) {
                fileOutputStream.write(b);
            }
            Emulator.LOGGER.log(Level.INFO,"Data written to file successfully.");
        } catch (IOException e) {
            Emulator.LOGGER.log(Level.SEVERE, "Error writing to file", e);
        }
    }

    public void notifyListeners(EventType eventType) {
        Emulator.LOGGER.log(Level.INFO, "Event Recived: " + eventType);
        for (CPUListener listener : listeners) {
            listener.cpuChanged(eventType);
        }
    }

    public void addListener(CPUListener listener) {
        listeners.add(listener);
    }

}
