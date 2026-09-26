package main.java.Running.animation;

import javax.swing.Timer;

public class Transition {
    private final Timer timer;
    private final Runnable onUpdate;
    private final Runnable onFinish;
    private final float step;
    private float progress;
    private boolean running;

    public Transition(int interval, float step, Runnable onUpdate, Runnable onFinish) {
        if (interval <= 0 || step <= 0) throw new IllegalArgumentException("Invalid transition parameters");
        this.step = step;
        this.onUpdate = onUpdate == null ? () -> {} : onUpdate;
        this.onFinish = onFinish == null ? () -> {} : onFinish;
        this.timer = new Timer(interval, e -> update());
    }

    private void update() {
        progress += step;
        if (progress >= 1.0f) {
            progress = 1.0f;
            timer.stop();
            running = false;
            onUpdate.run();
            onFinish.run();
        } else {
            onUpdate.run();
        }
    }

    public void start() {
        stop();
        progress = 0.0f;
        running = true;
        timer.start();
    }

    public void stop() {
        timer.stop();
        running = false;
    }

    public float getProgress() { return progress; }
    public boolean isRunning() { return running; }
}
