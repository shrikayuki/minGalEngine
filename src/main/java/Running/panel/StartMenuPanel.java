package main.java.Running.panel;

import main.java.Running.constants.RunningConstants;

import javax.swing.*;
import java.awt.*;

public class StartMenuPanel extends JPanel {
    public interface Listener {
        void onNewGame();
        void onLoadGame();
        void onSettings();
        void onExit();
    }

    public StartMenuPanel(Listener listener) {
        setOpaque(false);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(0, 80, 70, 80));

        add(title("MINIGALENGINE"));
        add(Box.createVerticalStrut(36));
        add(button("开始游戏", listener::onNewGame));
        add(Box.createVerticalStrut(12));
        add(button("读取存档", listener::onLoadGame));
        add(Box.createVerticalStrut(12));
        add(button("设置", listener::onSettings));
        add(Box.createVerticalStrut(12));
        add(button("退出", listener::onExit));
    }

    private JLabel title(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(Color.WHITE);
        label.setFont(new Font(RunningConstants.UI_FONT, Font.BOLD, 34));
        return label;
    }

    private JButton button(String text, Runnable action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(280, 48));
        button.setFont(new Font(RunningConstants.UI_FONT, Font.PLAIN, 18));
        button.addActionListener(e -> action.run());
        return button;
    }
}
