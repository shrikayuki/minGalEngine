package main.java.Running.animation;

import javax.swing.Timer;

public class Transition {

    private Timer timer;

    private final Runnable onUpdate;

    private final Runnable onFinish;

    private float progress;

    private boolean running;


    public Transition(
            int interval,
            float step,
            Runnable onUpdate,
            Runnable onFinish
    ) {

        this.onUpdate = onUpdate;
        this.onFinish = onFinish;

        this.progress = 0.0f;
        this.running = false;


        timer = new Timer(
                interval,
                e -> {

                    progress += step;


                    if (progress >= 1.0f) {

                        progress = 1.0f;

                        timer.stop();

                        running = false;

                        onUpdate.run();

                        onFinish.run();

                        return;
                    }


                    onUpdate.run();
                }
        );
    }


    public void start() {

        stop();

        progress = 0.0f;

        running = true;

        timer.start();
    }


    public void stop() {

        if (timer != null) {

            timer.stop();
        }

        running = false;
    }


    public float getProgress() {

        return progress;
    }


    public boolean isRunning() {

        return running;
    }
}