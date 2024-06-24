# JCPU - Java 6502 Emulator with Graphic UI

JCPU is a Java-based emulator for the 6502 microprocessor with a graphical user interface (GUI). It allows users to run programs written for the 6502 architecture and observe their execution in a visual environment.

## Features
- Emulation of the 6502 microprocessor
- Graphical user interface for interactive emulation
- Support for loading and executing 6502 assembly programs
- Real-time visualization of memory, registers, and execution flow
- Memory viewer to inspect and modify memory contents
- Debugging capabilities with stepping through instructions or running them continuously
- Register viewer to monitor the state of CPU registers
- Disassembler to convert machine code back into assembly instructions

## Screenshot
![JCPU Screenshot](https://github.com/cxe0/JCPU/assets/164516044/4108b01a-9d0d-4bdb-a046-1f28fff9d83f)

## Memory Viewer
The memory viewer provides a real-time display of the memory contents. Users can inspect memory addresses and modify their values during emulation. This feature is crucial for debugging and understanding how the program manipulates data.

## Debugging
JCPU includes robust debugging tools that allow users to:
- Step through instructions one at a time to observe the exact sequence of operations.
- Run the program continuously while observing changes in memory and registers.

## Register Viewer
The register viewer shows the current values of all CPU registers, including the accumulator, index registers, stack pointer, and program counter. This feature helps users track the changes in the CPU state throughout program execution.

## Disassembler
The disassembler in JCPU converts machine code back into human-readable assembly instructions. This tool is useful for examining the output of compiled programs and understanding their structure and functionality.


## Configuration File
JCPU uses a `config.json` file located in the current directory where the program is run. This file allows users to customize various settings of the emulator. Below is an example of the `config.json` file:

```json
[
  {
    "value": 0.0,
    "name": "CycleRate",
    "description": "How fast cycles should be executed"
  },
  {
    "value": false,
    "name": "Patching",
    "description": "Enable patching of memory"
  }
]
```

### Changing Configuration Settings
- **CycleRate**: This setting controls the speed at which cycles are executed. Adjusting the `value` can speed up or slow down the emulation.
- **Patching**: This setting enables or disables memory patching. Set `value` to `true` to enable patching, allowing modifications to memory during emulation.

To change these settings, edit the `config.json` file with your desired values and save it. The changes will take effect the next time JCPU is run.

## Sample Binaries
The `samples/` directory contains sample binaries that users can load into the emulator to test various functionalities. These binaries include very simple programs, such as counting to 10, which are useful for learning and experimentation. To load a sample binary, navigate to the `samples/` directory, select the desired file, and load it into JCPU to observe its execution.

---

## TODO
- Fix correct cycles when zero page wrap around
- Need to implement subroutines
- Increment and decrement instructions
- Shift instructions
- Interrupts
