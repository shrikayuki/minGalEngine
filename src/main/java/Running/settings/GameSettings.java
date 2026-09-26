package main.java.Running.settings;

import main.java.Running.constants.RunningConstants;

public class GameSettings {
    private boolean fullscreen;
    private int textSpeed;
    private float animationSpeed;
    private int width;
    private int height;

    public GameSettings() {
        this.fullscreen = false;
        this.textSpeed = RunningConstants.TEXT_SPEED_DEFAULT;
        this.animationSpeed = RunningConstants.ANIMATION_SPEED_DEFAULT;
        this.width = RunningConstants.GAME_WIDTH;
        this.height = RunningConstants.GAME_HEIGHT;
    }

    public boolean isFullscreen() { return fullscreen; }
    public void setFullscreen(boolean fullscreen) { this.fullscreen = fullscreen; }

    public int getTextSpeed() { return textSpeed; }
    public void setTextSpeed(int textSpeed) {
        this.textSpeed = Math.max(1, textSpeed);
    }

    public float getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = Math.max(0.01f, Math.min(1.0f, animationSpeed));
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void setResolution(int width, int height) {
        this.width = Math.max(640, width);
        this.height = Math.max(480, height);
    }
}
