package main.java.Running.panel;

import main.java.Running.animation.FadeTransition;
import main.java.Running.animation.Transition;
import main.java.Running.animation.TransitionStrategy;
import main.java.Running.constants.RunningConstants;
import main.java.Running.manager.ResourceManager;
import main.java.Running.render.BackgroundRenderer;
import main.java.Running.render.CharacterRenderer;

import javax.swing.*;
import java.awt.*;

public class GameScenePanel extends JPanel {
    private final BackgroundRenderer backgroundRenderer;
    private final CharacterRenderer characterRenderer;
    private final ResourceManager resources;
    private final TransitionStrategy transitionStrategy = new FadeTransition();

    private Transition backgroundTransition;
    private Transition characterTransition;

    public GameScenePanel(ResourceManager resources) {
        this.resources = resources;
        this.backgroundRenderer = new BackgroundRenderer();
        this.characterRenderer = new CharacterRenderer();
        setBackground(Color.BLACK);
    }

    public void changeBackground(String name, boolean instant, Runnable onFinished) {
        java.io.File file = resources.getBackground(name);
        if (!file.isFile()) {
            System.err.println("Background not found: " + file);
            onFinished.run();
            return;
        }
        boolean firstLoad = !backgroundRenderer.hasBackground();
        stopBackgroundTransition();
        backgroundRenderer.setBackground(file.getPath());

        if (instant || firstLoad) {
            backgroundRenderer.finishTransition();
            repaint();
            onFinished.run();
            return;
        }

        backgroundTransition = createTransition(
                () -> repaint(),
                () -> {
                    backgroundRenderer.finishTransition();
                    repaint();
                    onFinished.run();
                }
        );
        backgroundTransition.start();
    }

    public void changeCharacter(String name, boolean instant, Runnable onFinished) {
        stopCharacterTransition();
        if ("NONE".equalsIgnoreCase(name)) {
            characterRenderer.clear();
            repaint();
            onFinished.run();
            return;
        }

        java.io.File file = resources.getCharacter(name);
        if (!file.isFile()) {
            System.err.println("Character not found: " + file);
            onFinished.run();
            return;
        }

        boolean firstLoad = !characterRenderer.hasCharacter();
        characterRenderer.setCharacter(file.getPath());

        if (instant || firstLoad) {
            characterRenderer.finishTransition();
            repaint();
            onFinished.run();
            return;
        }

        characterTransition = createTransition(
                () -> repaint(),
                () -> {
                    characterRenderer.finishTransition();
                    repaint();
                    onFinished.run();
                }
        );
        characterTransition.start();
    }

    public void restore(String background, String character) {
        stopBackgroundTransition();
        stopCharacterTransition();

        if (background != null && !background.isBlank()) {
            backgroundRenderer.setBackground(resources.getBackground(background).getPath());
            backgroundRenderer.finishTransition();
        } else {
            backgroundRenderer.clear();
        }

        if (character != null && !character.isBlank()) {
            characterRenderer.setCharacter(resources.getCharacter(character).getPath());
            characterRenderer.finishTransition();
        } else {
            characterRenderer.clear();
        }
        repaint();
    }

    public String getBackgroundName() {
        return fileName(backgroundRenderer.getCurrentPath());
    }

    public String getCharacterName() {
        return fileName(characterRenderer.getCurrentPath());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        float backgroundAlpha = backgroundTransition == null || !backgroundTransition.isRunning()
                ? 1.0f : transitionStrategy.alpha(backgroundTransition.getProgress());
        float characterAlpha = characterTransition == null || !characterTransition.isRunning()
                ? 1.0f : transitionStrategy.alpha(characterTransition.getProgress());
        backgroundRenderer.draw(g, getWidth(), getHeight(), backgroundAlpha);
        characterRenderer.draw(g, getWidth(), getHeight(), characterAlpha);
    }

    public void stopTransitions() {
        stopBackgroundTransition();
        stopCharacterTransition();
    }

    private Transition createTransition(Runnable update, Runnable finish) {
        return new Transition(
                RunningConstants.TRANSITION_TIMER_DELAY,
                currentAnimationSpeed,
                update,
                finish
        );
    }

    private float currentAnimationSpeed = RunningConstants.ANIMATION_SPEED_DEFAULT;

    public void setAnimationSpeed(float speed) {
        currentAnimationSpeed = Math.max(0.01f, Math.min(1.0f, speed));
    }

    private void stopBackgroundTransition() {
        if (backgroundTransition != null) backgroundTransition.stop();
    }

    private void stopCharacterTransition() {
        if (characterTransition != null) characterTransition.stop();
    }

    private String fileName(String path) {
        if (path == null) return null;
        return new java.io.File(path).getName();
    }
}
