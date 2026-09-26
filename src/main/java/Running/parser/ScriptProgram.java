package main.java.Running.parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptProgram {
    private final List<Instruction> instructions;
    private final Map<String, Integer> labels;

    public ScriptProgram(List<Instruction> instructions, Map<String, Integer> labels) {
        this.instructions = List.copyOf(instructions);
        this.labels = Collections.unmodifiableMap(new HashMap<>(labels));
    }

    public List<Instruction> getInstructions() { return instructions; }
    public Map<String, Integer> getLabels() { return labels; }
}
