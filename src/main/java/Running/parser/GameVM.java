package main.java.Running.parser;

import main.java.Running.type.OpCode;

import java.util.List;

public class GameVM {

    private int pc = 0;
    private boolean running = true;

    private final List<Instruction> program;

    public GameVM(List<Instruction> program) {
        this.program = program;
    }

    public Instruction next() {

        while (running) {

            Instruction instruction =
                    program.get(pc++);

            switch (instruction.getOpCode()) {

                case SAY:
                case CHOICE:
                case CHAR:
                case BG:
                case BGM:
                case VOICE:
                    return instruction;

                case LABEL:
                    break;

                case JUMP:

                    String target =
                            (String)
                                    instruction
                                            .getOperands()[0];

                    jumpTo(target);

                    break;

                case END:

                    running = false;
                    return null;
            }
        }

        return null;
    }

    public boolean isRunning() {
        return running;
    }

    public void jumpTo(String label) {

        for (int i = 0; i < program.size(); i++) {

            Instruction instruction =
                    program.get(i);

            if (instruction.getOpCode()
                    == OpCode.LABEL) {

                String name =
                        (String)
                                instruction
                                        .getOperands()[0];

                if (name.equals(label)) {

                    pc = i + 1;

                    return;
                }
            }
        }

        throw new RuntimeException(
                "找不到 LABEL: " + label
        );
    }
}