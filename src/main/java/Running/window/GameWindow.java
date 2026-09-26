package main.java.Running.window;

import main.java.Running.constants.RunningConstants;
import main.java.Running.controller.GameController;
import main.java.Running.manager.AudioManager;
import main.java.Running.manager.ResourceManager;
import main.java.Running.manager.SaveManager;
import main.java.Running.panel.DialoguePanel;
import main.java.Running.panel.GameScenePanel;
import main.java.Running.settings.GameSettings;
import main.java.Running.state.SaveData;
import main.java.Running.type.Choice;
import main.java.Running.parser.GameVM;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameWindow extends JFrame implements GameController.View {
    private final GameVM vm;
    private final GameSettings settings;
    private final ResourceManager resources;
    private final SaveManager saveManager;
    private final AudioManager audioManager;

    private final GameScenePanel scenePanel;
    private final DialoguePanel dialoguePanel;
    private final GameController controller;
    private final JLabel skipLabel;

    private Timer skipTimer;

    public GameWindow(GameVM vm, GameSettings settings) {
        this(vm, settings, new File("resources"), new File("saves"), true);
    }

    public GameWindow(GameVM vm, GameSettings settings, File resourcesDirectory) {
        this(vm, settings,
                resourcesDirectory,
                new File(resourcesDirectory.getParentFile() == null
                        ? new File("saves").getPath()
                        : new File(resourcesDirectory.getParentFile(), "saves").getPath()),
                true);
    }

    public GameWindow(
            GameVM vm,
            GameSettings settings,
            File resourcesDirectory,
            File saveDirectory,
            boolean autoStart
    ) {
        this.vm = vm;
        this.settings = settings;
        this.resources = new ResourceManager(resourcesDirectory);
        this.saveManager = new SaveManager(saveDirectory);
        this.audioManager = new AudioManager();
        this.scenePanel = new GameScenePanel(resources);
        this.scenePanel.setAnimationSpeed(settings.getAnimationSpeed());
        this.dialoguePanel = new DialoguePanel(null);
        this.controller = new GameController(vm, resources, audioManager, this);
        this.skipLabel = new JLabel("Skip: OFF");

        buildWindow();
        bindEvents();
        applyFullscreen();

        if (autoStart) {
            controller.start();
        }
    }

    private void buildWindow() {
        setTitle(RunningConstants.GAME_TITLE);
        setSize(settings.getWidth(), settings.getHeight());
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 6));
        topBar.setOpaque(true);
        topBar.setBackground(new Color(20, 20, 25));

        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");
        JButton skipButton = new JButton("Skip");
        JButton menuButton = new JButton("Menu");

        skipButton.setToolTipText("开启/关闭快速跳过");
        skipLabel.setForeground(Color.WHITE);

        saveButton.addActionListener(e -> saveGame());
        loadButton.addActionListener(e -> loadGame());
        skipButton.addActionListener(e -> toggleSkip());
        menuButton.addActionListener(e -> showGameMenu());

        topBar.add(saveButton);
        topBar.add(loadButton);
        topBar.add(skipButton);
        topBar.add(skipLabel);
        topBar.add(menuButton);

        dialoguePanel.setPreferredSize(new Dimension(0, 175));

        add(topBar, BorderLayout.NORTH);
        add(scenePanel, BorderLayout.CENTER);
        add(dialoguePanel, BorderLayout.SOUTH);
    }

    private void bindEvents() {
        dialoguePanel.setListener(new DialoguePanel.Listener() {
            @Override
            public void onAdvance() {
                if (controller.isSkipMode()) return;
                if (dialoguePanel.isTyping()) {
                    dialoguePanel.finishTyping();
                    return;
                }
                controller.continueFromDialogue();
            }

            @Override
            public void onChoice(Choice choice) {
                stopSkipTimer();
                controller.choose(choice);
                if (controller.isSkipMode()) startSkipTimer();
            }
        });

        getRootPane().registerKeyboardAction(
                e -> showGameMenu(),
                KeyStroke.getKeyStroke("ESCAPE"),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }

    @Override
    public void showDialogue(String text) {
        dialoguePanel.hideChoices();
        if (controller.isSkipMode()) {
            dialoguePanel.showInstant(text);
            startSkipTimer();
        } else {
            dialoguePanel.startTyping(text, settings.getTextSpeed());
        }
    }

    @Override
    public void showChoices(List<Choice> choices) {
        stopSkipTimer();
        dialoguePanel.showChoices(choices);
    }

    @Override
    public void changeBackground(String name, boolean instant, Runnable onFinished) {
        scenePanel.setAnimationSpeed(settings.getAnimationSpeed());
        scenePanel.changeBackground(name, instant, onFinished);
    }

    @Override
    public void changeCharacter(String name, boolean instant, Runnable onFinished) {
        scenePanel.setAnimationSpeed(settings.getAnimationSpeed());
        scenePanel.changeCharacter(name, instant, onFinished);
    }

    @Override
    public void onGameFinished() {
        stopSkipTimer();
        audioManager.stopBgm();
        SwingUtilities.invokeLater(this::returnToTitle);
    }

    public void loadState(SaveData data) {
        stopSkipTimer();
        dialoguePanel.hideChoices();
        dialoguePanel.showInstant("");
        scenePanel.restore(data.getBackground(), data.getCharacter());
        audioManager.restoreBgm(data.getBgm() == null ? null : resources.getBgm(data.getBgm()));
        vm.setPc(data.getPc());
        controller.setSkipMode(false);
        updateSkipLabel();
        controller.start();
    }

    private SaveData createSaveData() {
        return new SaveData(
                controller.getSavePc(),
                scenePanel.getBackgroundName(),
                scenePanel.getCharacterName(),
                audioManager.getCurrentBgm()
        );
    }

    private void saveGame() {
        String slot = chooseSlot("选择保存槽位", false);
        if (slot == null) return;

        try {
            saveManager.save(slot, createSaveData());
            JOptionPane.showMessageDialog(this, "保存成功：Slot " + slot);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "保存失败：" + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        String slot = chooseSlot("选择读取槽位", true);
        if (slot == null) return;

        try {
            SaveData data = saveManager.load(slot);
            loadState(data);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(this,
                    "读取失败：" + e.getMessage(),
                    "Load Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private String chooseSlot(String title, boolean load) {
        Object[] options = new Object[9];
        for (int i = 0; i < options.length; i++) {
            String slot = String.valueOf(i + 1);
            options[i] = load && !saveManager.exists(slot)
                    ? "Slot " + slot + " (空)"
                    : "Slot " + slot;
        }

        Object value = JOptionPane.showInputDialog(
                this,
                load ? "选择要读取的存档" : "选择保存到哪个槽位",
                title,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (value == null) return null;
        String selected = String.valueOf(value);
        String digits = selected.replaceAll("\\D+", "");
        if (digits.isBlank()) return null;
        String slot = digits;

        if (load && !saveManager.exists(slot)) {
            JOptionPane.showMessageDialog(this, "这个槽位没有存档。");
            return null;
        }
        return slot;
    }

    private void toggleSkip() {
        controller.setSkipMode(!controller.isSkipMode());
        if (controller.isSkipMode()) {
            if (dialoguePanel.isTyping()) dialoguePanel.finishTyping();
            startSkipTimer();
        } else {
            stopSkipTimer();
        }
        updateSkipLabel();
    }

    private void startSkipTimer() {
        if (!controller.isSkipMode()) return;
        if (skipTimer == null) {
            skipTimer = new Timer(90, e -> skipTick());
            skipTimer.setRepeats(true);
        }
        if (!skipTimer.isRunning()) skipTimer.start();
    }

    private void skipTick() {
        if (!controller.isSkipMode() || !controller.isRunning()) {
            stopSkipTimer();
            return;
        }
        if (dialoguePanel.hasChoices()) {
            stopSkipTimer();
            return;
        }
        if (controller.isWaiting()) {
            controller.continueFromDialogue();
        } else {
            controller.advance();
        }
    }

    private void stopSkipTimer() {
        if (skipTimer != null) skipTimer.stop();
    }

    private void updateSkipLabel() {
        skipLabel.setText(controller.isSkipMode() ? "Skip: ON" : "Skip: OFF");
    }

    private void showGameMenu() {
        Object[] options = {
                "继续游戏",
                "Save",
                "Load",
                controller.isSkipMode() ? "Skip: ON" : "Skip: OFF",
                "设置",
                "返回标题",
                "退出游戏"
        };

        int result = JOptionPane.showOptionDialog(
                this,
                "游戏菜单",
                "Menu",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        switch (result) {
            case 0 -> { }
            case 1 -> saveGame();
            case 2 -> loadGame();
            case 3 -> toggleSkip();
            case 4 -> {
                SettingsWindow window = new SettingsWindow(this, settings);
                window.setVisible(true);
                applySettings();
            }
            case 5 -> returnToTitle();
            case 6 -> System.exit(0);
            default -> { }
        }
    }

    private void applySettings() {
        scenePanel.setAnimationSpeed(settings.getAnimationSpeed());
        setSize(settings.getWidth(), settings.getHeight());
        applyFullscreen();
        revalidate();
    }

    private void applyFullscreen() {
        GraphicsDevice device = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        if (settings.isFullscreen()) {
            dispose();
            setUndecorated(true);
            device.setFullScreenWindow(this);
        } else {
            if (device.getFullScreenWindow() == this) device.setFullScreenWindow(null);
            dispose();
            setUndecorated(false);
            setSize(settings.getWidth(), settings.getHeight());
            setLocationRelativeTo(null);
        }
    }

    private void returnToTitle() {
        stopSkipTimer();
        controller.stop();
        audioManager.stopBgm();
        dispose();
        SwingUtilities.invokeLater(() -> new StartWindow().setVisible(true));
    }
}
