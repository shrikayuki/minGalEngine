package main.java.Running.settings;

import main.java.Running.constants.RunningConstants;

import java.awt.*;

public class GameSettings {

    // =========================================================
    // 全屏
    // =========================================================

    private boolean fullscreen =
            false;


    // =========================================================
    // 文本速度
    //
    // 单位：毫秒 / 字
    //
    // 数值越小越快
    // =========================================================

    private int textSpeed =
            RunningConstants.TEXT_SPEED_DEFAULT;


    // =========================================================
    // 动画速度
    // =========================================================

    private float animationSpeed =
            RunningConstants.ANIMATION_SPEED_DEFAULT;


    // =========================================================
    // 分辨率
    // =========================================================

    private int width =
            RunningConstants.DEFAULT_WIDTH;

    private int height =
            RunningConstants.DEFAULT_HEIGHT;


    // =========================================================
    // 全屏 Getter / Setter
    // =========================================================

    public boolean isFullscreen() {
        return fullscreen;
    }

    public void setFullscreen(
            boolean fullscreen
    ) {
        this.fullscreen = fullscreen;
    }


    // =========================================================
    // 文本速度 Getter / Setter
    // =========================================================

    public int getTextSpeed() {
        return textSpeed;
    }

    public void setTextSpeed(
            int textSpeed
    ) {

        /*
         * 防止出现非法值。
         *
         * 例如：
         *
         * setTextSpeed(10000)
         *
         * 会自动限制成：
         *
         * 200
         */

        this.textSpeed =
                Math.max(
                        RunningConstants.TEXT_SPEED_FAST,
                        Math.min(
                                textSpeed,
                                RunningConstants.TEXT_SPEED_SLOW
                        )
                );
    }


    // =========================================================
    // 动画速度 Getter / Setter
    // =========================================================

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(
            float animationSpeed
    ) {

        /*
         * 防止动画速度超出范围。
         *
         * 0.05 ~ 0.20
         */

        this.animationSpeed =
                Math.max(
                        RunningConstants.ANIMATION_SPEED_SLOW,
                        Math.min(
                                animationSpeed,
                                RunningConstants.ANIMATION_SPEED_FAST
                        )
                );
    }


    // =========================================================
    // 分辨率 Getter
    // =========================================================

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }


    // =========================================================
    // 分辨率 Setter
    // =========================================================

    public void setResolution(
            int width,
            int height
    ) {

        this.width =
                width;

        this.height =
                height;
    }


    // =========================================================
    // 获取分辨率
    // =========================================================

    public Dimension getResolution() {

        return new Dimension(
                width,
                height
        );
    }
}