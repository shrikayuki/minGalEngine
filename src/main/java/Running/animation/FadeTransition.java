package main.java.Running.animation;

public class FadeTransition implements TransitionStrategy {
    @Override
    public float alpha(float progress) {
        return Math.max(0.0f, Math.min(1.0f, progress));
    }
}
