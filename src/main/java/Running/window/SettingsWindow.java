package main.java.Running.window;

import main.java.Running.constants.RunningConstants;
import main.java.Running.settings.GameSettings;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JDialog {
    private final GameSettings settings;
    private final JCheckBox fullscreen;
    private final JSlider textSpeed;
    private final JSlider animationSpeed;
    private final JComboBox<String> resolution;

    private final int[][] resolutions = {
            {800, 600},
            {1000, 700},
            {1280, 720},
            {1280, 800},
            {1366, 768},
            {1920, 1080}
    };

    public SettingsWindow(Window owner, GameSettings settings) {
        super(owner, "设置", ModalityType.APPLICATION_MODAL);
        this.settings = settings;
        this.fullscreen = new JCheckBox("全屏", settings.isFullscreen());
        this.textSpeed = new JSlider(
                RunningConstants.TEXT_SPEED_FAST,
                RunningConstants.TEXT_SPEED_SLOW,
                settings.getTextSpeed()
        );
        int animation = Math.round(settings.getAnimationSpeed() * 100);
        this.animationSpeed = new JSlider(5, 20, Math.max(5, Math.min(20, animation)));
        this.resolution = new JComboBox<>();

        for (int[] value : resolutions) {
            resolution.addItem(value[0] + " × " + value[1]);
        }
        resolution.setSelectedItem(settings.getWidth() + " × " + settings.getHeight());

        buildUI();
        setSize(420, 300);
        setLocationRelativeTo(owner);
    }

    private void buildUI() {
        JPanel root = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 10, 8, 10);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;

        c.gridx = 0; c.gridy = 0;
        root.add(fullscreen, c);

        c.gridy++;
        root.add(new JLabel("文字速度（越小越快）"), c);
        c.gridy++;
        root.add(textSpeed, c);

        c.gridy++;
        root.add(new JLabel("动画速度（越大越快）"), c);
        c.gridy++;
        root.add(animationSpeed, c);

        c.gridy++;
        root.add(new JLabel("分辨率"), c);
        c.gridy++;
        root.add(resolution, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("取消");
        JButton apply = new JButton("应用");
        cancel.addActionListener(e -> dispose());
        apply.addActionListener(e -> applyAndClose());
        buttons.add(cancel);
        buttons.add(apply);

        c.gridy++;
        root.add(buttons, c);
        setContentPane(root);
    }

    private void applyAndClose() {
        settings.setFullscreen(fullscreen.isSelected());
        settings.setTextSpeed(textSpeed.getValue());
        settings.setAnimationSpeed(animationSpeed.getValue() / 100.0f);

        int index = resolution.getSelectedIndex();
        if (index >= 0 && index < resolutions.length) {
            settings.setResolution(resolutions[index][0], resolutions[index][1]);
        }
        dispose();
    }
}
