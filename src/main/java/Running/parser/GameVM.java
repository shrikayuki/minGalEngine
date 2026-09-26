package main.java.Running.parser;

import main.java.Running.type.OpCode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

public class GameVM {

    /**
     * 当前正在执行的脚本
     */
    private ScriptFrame currentFrame;


    /**
     * 脚本调用栈
     *
     * main.txt
     *    ↓ CALL chapter1
     * chapter1.txt
     *    ↓ CALL battle
     * battle.txt
     *
     * stack:
     * main.txt
     * chapter1.txt
     */
    private final Deque<ScriptFrame> callStack =
            new ArrayDeque<>();


    /**
     * 脚本仓库
     *
     * 用来根据名字加载其他脚本
     */
    private final ScriptRepository repository;


    private boolean running;


    // =========================================================
    // 旧构造方法
    // =========================================================

    /**
     * 保留旧 API。
     *
     * 这个构造方法仍然可以运行单个 ScriptProgram。
     */
    public GameVM(
            ScriptProgram program
    ) {

        if (program == null) {

            throw new IllegalArgumentException(
                    "ScriptProgram 不能为空"
            );
        }


        this.repository = null;


        this.currentFrame =
                new ScriptFrame(
                        "<memory>",
                        program
                );


        this.running = true;
    }


    // =========================================================
    // 新构造方法
    // =========================================================

    /**
     * 多脚本 Runtime 使用。
     */
    public GameVM(
            ScriptRepository repository,
            String entryScript
    ) throws IOException {

        if (repository == null) {

            throw new IllegalArgumentException(
                    "ScriptRepository 不能为空"
            );
        }


        if (
                entryScript == null
                        || entryScript.isBlank()
        ) {

            throw new IllegalArgumentException(
                    "入口脚本不能为空"
            );
        }


        this.repository =
                repository;


        ScriptProgram program =
                repository.load(
                        entryScript
                );


        this.currentFrame =
                new ScriptFrame(
                        entryScript,
                        program
                );


        this.running = true;
    }


    // =========================================================
    // next
    // =========================================================

    public Instruction next() {

        while (
                running
                        && currentFrame != null
        ) {

            // -------------------------------------------------
            // 当前脚本执行完
            // -------------------------------------------------

            if (
                    currentFrame.pc
                            >= currentFrame.program
                            .getInstructions()
                            .size()
            ) {

                returnFromScript();

                continue;
            }


            Instruction instruction =
                    currentFrame.program
                            .getInstructions()
                            .get(
                                    currentFrame.pc++
                            );


            OpCode opCode =
                    instruction.getOpCode();


            // -------------------------------------------------
            // LABEL
            // -------------------------------------------------

            if (
                    opCode
                            == OpCode.LABEL
            ) {

                continue;
            }


            // -------------------------------------------------
            // JUMP
            // -------------------------------------------------

            if (
                    opCode
                            == OpCode.JUMP
            ) {

                String label =
                        (String)
                                instruction
                                        .getOperands()[0];


                jumpTo(
                        label
                );


                continue;
            }


            // -------------------------------------------------
            // CALL
            // -------------------------------------------------

            if (
                    opCode
                            == OpCode.CALL
            ) {

                String scriptName =
                        (String)
                                instruction
                                        .getOperands()[0];


                callScript(
                        scriptName
                );


                continue;
            }


            // -------------------------------------------------
            // RETURN
            // -------------------------------------------------

            if (
                    opCode
                            == OpCode.RETURN
            ) {

                returnFromScript();

                continue;
            }


            // -------------------------------------------------
            // END
            // -------------------------------------------------

            if (
                    opCode
                            == OpCode.END
            ) {

                running = false;

                callStack.clear();

                currentFrame = null;

                return null;
            }


            // -------------------------------------------------
            // 普通指令
            // -------------------------------------------------

            return instruction;
        }


        running = false;

        return null;
    }


    // =========================================================
    // CALL
    // =========================================================

    private void callScript(
            String scriptName
    ) {

        if (repository == null) {

            throw new IllegalStateException(
                    "当前 VM 没有 ScriptRepository，"
                            + "不能执行 CALL"
            );
        }


        try {

            ScriptProgram program =
                    repository.load(
                            scriptName
                    );


            // 保存当前脚本执行位置
            callStack.push(
                    currentFrame
            );


            // 切换到新脚本
            currentFrame =
                    new ScriptFrame(
                            scriptName,
                            program
                    );


        } catch (IOException e) {

            throw new IllegalStateException(
                    "无法加载脚本："
                            + scriptName,
                    e
            );
        }
    }


    // =========================================================
    // RETURN
    // =========================================================

    private void returnFromScript() {

        if (callStack.isEmpty()) {

            // 已经回到了入口脚本之外
            running = false;

            currentFrame = null;

            return;
        }


        currentFrame =
                callStack.pop();
    }


    // =========================================================
    // JUMP
    // =========================================================

    public void jumpTo(
            String label
    ) {

        if (currentFrame == null) {

            throw new IllegalStateException(
                    "当前没有执行中的脚本"
            );
        }


        Integer target =
                currentFrame.program
                        .getLabels()
                        .get(
                                label
                        );


        if (target == null) {

            throw new IllegalArgumentException(
                    "Label not found: "
                            + label
                            + " in "
                            + currentFrame.scriptName
            );
        }


        currentFrame.pc =
                target;
    }


    // =========================================================
    // PC
    // =========================================================

    public int getPc() {

        if (currentFrame == null) {

            return 0;
        }


        return currentFrame.pc;
    }


    public void setPc(
            int pc
    ) {

        if (currentFrame == null) {

            throw new IllegalStateException(
                    "当前没有执行中的脚本"
            );
        }


        if (
                pc < 0
                        || pc > currentFrame.program
                        .getInstructions()
                        .size()
        ) {

            throw new IllegalArgumentException(
                    "Invalid PC: "
                            + pc
            );
        }


        currentFrame.pc =
                pc;


        this.running =
                pc
                        < currentFrame.program
                        .getInstructions()
                        .size();
    }


    // =========================================================
    // 状态
    // =========================================================

    public boolean isRunning() {

        return running;
    }


    public int size() {

        if (currentFrame == null) {

            return 0;
        }


        return currentFrame.program
                .getInstructions()
                .size();
    }


    // =========================================================
    // 当前脚本
    // =========================================================

    public String getCurrentScriptName() {

        if (currentFrame == null) {

            return null;
        }


        return currentFrame.scriptName;
    }


    public ScriptProgram getCurrentProgram() {

        if (currentFrame == null) {

            return null;
        }


        return currentFrame.program;
    }


    public int getCallDepth() {

        return callStack.size();
    }


    // =========================================================
    // Frame
    // =========================================================

    private static class ScriptFrame {

        private final String scriptName;

        private final ScriptProgram program;

        private int pc;


        private ScriptFrame(
                String scriptName,
                ScriptProgram program
        ) {

            this.scriptName =
                    scriptName;

            this.program =
                    program;

            this.pc = 0;
        }
    }
}