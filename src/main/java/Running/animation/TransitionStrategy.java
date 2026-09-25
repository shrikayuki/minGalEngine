package main.java.Running.animation;

public interface TransitionStrategy {

    /**
     * 根据动画进度计算透明度
     *
     * @param progress 0.0 ~ 1.0
     * @return 透明度 0.0 ~ 1.0
     */
    float alpha(float progress);
}