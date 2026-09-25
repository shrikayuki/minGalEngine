package main.java.Running.parser;

import main.java.Running.type.OpCode;

public class Instruction {

    private final OpCode opCode;
    private final Object[] operands;

    public Instruction(OpCode opCode, Object[] operands) {
        this.opCode = opCode;
        this.operands = operands;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public Object[] getOperands() {
        return operands;
    }
}