package main.java.Running.window;

import main.java.Running.constants.RunningConstants;
import main.java.Running.manager.GameLauncher;
import main.java.Running.manager.SaveManager;
import main.java.Running.panel.StartBackgroundPanel;
import main.java.Running.panel.StartMenuPanel;
import main.java.Running.settings.GameSettings;
import main.java.Running.state.SaveData;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StartWindow extends JFrame {
    private final GameSettings settings;
    private final File scriptFile = new File("game.txt");
    private final File resourcesDirectory = new File("resources");
    private final SaveManager saveManager = new SaveManager(new File("saves"));

    public StartWindow() {
        settings = new GameSettings();
        setTitle(RunningConstants.GAME_TITLE);
        setSize(settings.getWidth(), settings.getHeight());
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        StartBackgroundPanel background = new StartBackgroundPanel();
        background.setLayout(new GridBagLayout());

        StartMenuPanel menu = new StartMenuPanel(new StartMenuPanel.Listener() {
            @Override public void onNewGame() { startNewGame(); }
            @Override public void onLoadGame() { loadGame(); }
            @Override public void onSettings() { openSettings(); }
            @Override public void onExit() { System.exit(0); }
        });

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        background.add(menu, constraints);

        setContentPane(background);
    }

    private void startNewGame() {
        if (!scriptFile.isFile()) {
            showError("找不到脚本：" + scriptFile.getPath());
            return;
        }
        try {
            GameLauncher.launch(scriptFile, resourcesDirectory, settings);
            dispose();
        } catch (IOException | RuntimeException e) {
            showError("启动游戏失败：" + e.getMessage());
        }
    }

    private void loadGame() {
        String slot = chooseExistingSlot();
        if (slot == null) return;

        try {
            SaveData data = saveManager.load(slot);
            GameLauncher.launchLoaded(
                    scriptFile,
                    resourcesDirectory,
                    new File("saves"),
                    settings,
                    data
            );
            dispose();
        } catch (IOException | RuntimeException e) {
            showError("读取存档失败：" + e.getMessage());
        }
    }

    private String chooseExistingSlot() {
        StringBuilder message = new StringBuilder("可用存档：\n\n");
        boolean any = false;
        for (int i = 1; i <= 9; i++) {
            String slot = String.valueOf(i);
            if (saveManager.exists(slot)) {
                message.append("Slot ").append(slot).append("\n");
                any = true;
            }
        }
        if (!any) {
            JOptionPane.showMessageDialog(this, "目前没有存档。");
            return null;
        }

        Object[] options = new Object[9];
        for (int i = 0; i < 9; i++) {
            String slot = String.valueOf(i + 1);
            options[i] = saveManager.exists(slot) ? "Slot " + slot : "Slot " + slot + " (空)";
        }

        Object value = JOptionPane.showInputDialog(
                this,
                message,
                "读取存档",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );
        if (value == null) return null;

        String digits = String.valueOf(value).replaceAll("\\D+", "");
        if (digits.isBlank()) return null;
        String slot = digits;
        if (!saveManager.exists(slot)) {
            showError("该槽位为空。");
            return null;
        }
        return slot;
    }

    private void openSettings() {
        SettingsWindow window = new SettingsWindow(this, settings);
        window.setVisible(true);
        setSize(settings.getWidth(), settings.getHeight());
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "MiniGalEngine", JOptionPane.ERROR_MESSAGE);
    }
}
