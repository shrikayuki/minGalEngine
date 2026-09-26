package main.java.Running.panel;

import main.java.Running.constants.RunningConstants;
import main.java.Running.type.Choice;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DialoguePanel extends JPanel {
    public interface Listener {
        void onAdvance();
        void onChoice(Choice choice);
    }

    private final JLabel textLabel;
    private final JPanel choicePanel;
    private Listener listener;
    private Timer timer;
    private String text = "";
    private int index;
    private boolean typing;

    public DialoguePanel(Listener listener) {
        this.listener = listener;
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(0, 0, 0, 190));

        textLabel = new JLabel();
        textLabel.setForeground(Color.WHITE);
        textLabel.setFont(new Font(RunningConstants.UI_FONT, Font.PLAIN, RunningConstants.DIALOGUE_FONT_SIZE));
        textLabel.setBorder(BorderFactory.createEmptyBorder(16, 28, 12, 28));

        choicePanel = new JPanel();
        choicePanel.setOpaque(false);
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
        choicePanel.setBorder(BorderFactory.createEmptyBorder(5, 80, 18, 80));
        choicePanel.setVisible(false);

        add(textLabel, BorderLayout.CENTER);
        add(choicePanel, BorderLayout.SOUTH);

        java.awt.event.MouseAdapter mouse = new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                onAdvance();
            }
        };
        addMouseListener(mouse);
        textLabel.addMouseListener(mouse);
    }

    public void setListener(Listener listener) { this.listener = listener; }

    public void startTyping(String text, int speed) {
        stopTyping();
        this.text = text == null ? "" : text;
        this.index = 0;
        this.typing = true;
        textLabel.setText("");
        timer = new Timer(Math.max(1, speed), e -> {
            if (index >= this.text.length()) {
                stopTyping();
                return;
            }
            index++;
            textLabel.setText(this.text.substring(0, index));
        });
        timer.start();
    }

    public void showInstant(String text) {
        stopTyping();
        this.text = text == null ? "" : text;
        this.index = this.text.length();
        this.typing = false;
        textLabel.setText(this.text);
    }

    public void showChoices(List<Choice> choices) {
        stopTyping();
        choicePanel.removeAll();
        choicePanel.setVisible(true);

        for (int i = 0; i < choices.size(); i++) {
            Choice choice = choices.get(i);
            JButton button = new JButton((i + 1) + ". " + choice.getText());
            button.setFont(new Font(RunningConstants.UI_FONT, Font.PLAIN, RunningConstants.CHOICE_FONT_SIZE));
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(RunningConstants.CHOICE_WIDTH, RunningConstants.CHOICE_HEIGHT));
            button.addActionListener(e -> {
                hideChoices();
                if (listener != null) listener.onChoice(choice);
            });
            choicePanel.add(button);
            choicePanel.add(Box.createVerticalStrut(8));
        }
        revalidate();
        repaint();
    }

    public void hideChoices() {
        choicePanel.removeAll();
        choicePanel.setVisible(false);
        revalidate();
        repaint();
    }

    public boolean hasChoices() { return choicePanel.isVisible(); }
    public boolean isTyping() { return typing; }

    public void finishTyping() {
        if (!typing) return;
        stopTyping();
        index = text.length();
        textLabel.setText(text);
    }

    private void onAdvance() {
        if (hasChoices()) return;
        if (listener != null) listener.onAdvance();
    }

    private void stopTyping() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        typing = false;
    }
}
