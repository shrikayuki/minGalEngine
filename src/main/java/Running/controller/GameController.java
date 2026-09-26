package main.java.Running.controller;

import main.java.Running.manager.AudioManager;
import main.java.Running.manager.ResourceManager;
import main.java.Running.parser.GameVM;
import main.java.Running.parser.Instruction;
import main.java.Running.panel.GameScenePanel;
import main.java.Running.type.Choice;
import main.java.Running.type.OpCode;

import java.util.List;

public class GameController {
    public interface View {
        void showDialogue(String text);
        void showChoices(List<Choice> choices);
        void changeBackground(String name, boolean instant, Runnable onFinished);
        void changeCharacter(String name, boolean instant, Runnable onFinished);
        void onGameFinished();
    }

    private final GameVM vm;
    private final ResourceManager resources;
    private final AudioManager audio;
    private final View view;

    private boolean running;
    private boolean waiting;
    private boolean skipMode;
    private int currentInstructionIndex = -1;

    public GameController(GameVM vm, ResourceManager resources, AudioManager audio, View view) {
        this.vm = vm;
        this.resources = resources;
        this.audio = audio;
        this.view = view;
    }

    public void start() {
        running = true;
        waiting = false;
        currentInstructionIndex = -1;
        advance();
    }

    public void advance() {
        if (!running || waiting) return;

        Instruction instruction = vm.next();
        if (instruction == null) {
            finish();
            return;
        }

        currentInstructionIndex = -1;
        OpCode code = instruction.getOpCode();
        Object[] args = instruction.getOperands();

        switch (code) {
            case SAY -> {
                waiting = true;
                currentInstructionIndex = vm.getPc() - 1;
                view.showDialogue((String) args[0]);
            }
            case CHOICE -> {
                waiting = true;
                currentInstructionIndex = vm.getPc() - 1;
                @SuppressWarnings("unchecked")
                List<Choice> choices = (List<Choice>) args[0];
                view.showChoices(choices);
            }
            case BG -> view.changeBackground((String) args[0], skipMode, this::advance);
            case CHAR -> view.changeCharacter((String) args[0], skipMode, this::advance);
            case BGM -> {
                audio.playBgm(resources.getBgm((String) args[0]));
                advance();
            }
            case VOICE -> {
                audio.playVoice(resources.getVoice((String) args[0]));
                advance();
            }
            default -> advance();
        }
    }

    public void continueFromDialogue() {
        if (!waiting) return;
        waiting = false;
        currentInstructionIndex = -1;
        advance();
    }

    public void choose(Choice choice) {
        if (!waiting || choice == null) return;
        waiting = false;
        currentInstructionIndex = -1;
        vm.jumpTo(choice.getTarget());
        advance();
    }

    public void setSkipMode(boolean skipMode) {
        this.skipMode = skipMode;
    }

    public boolean isSkipMode() { return skipMode; }
    public boolean isWaiting() { return waiting; }
    public boolean isRunning() { return running; }
    public int getSavePc() { return currentInstructionIndex >= 0 ? currentInstructionIndex : vm.getPc(); }

    public void stop() {
        running = false;
        waiting = false;
    }

    public void finish() {
        running = false;
        waiting = false;
        currentInstructionIndex = -1;
        view.onGameFinished();
    }
}
