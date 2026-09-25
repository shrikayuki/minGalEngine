package main.java.Running.constants;

import java.awt.*;

public final class RunningConstants {

    private RunningConstants() {
    }


    // =========================================================
    // 游戏窗口默认配置
    // =========================================================

    public static final String GAME_TITLE =
            "MiniGalEngine";

    public static final int DEFAULT_WIDTH =
            1000;

    public static final int DEFAULT_HEIGHT =
            700;


    // =========================================================
    // 文本速度
    //
    // 单位：毫秒 / 字
    //
    // 数值越小
    // ↓
    // Timer 间隔越短
    // ↓
    // 打字越快
    // =========================================================

    public static final int TEXT_SPEED_FAST =
            10;

    public static final int TEXT_SPEED_DEFAULT =
            50;

    public static final int TEXT_SPEED_SLOW =
            200;


    // =========================================================
    // 动画速度
    //
    // 数值越大
    // ↓
    // 动画进行越快
    // =========================================================

    public static final float ANIMATION_SPEED_SLOW =
            0.05f;

    public static final float ANIMATION_SPEED_DEFAULT =
            0.10f;

    public static final float ANIMATION_SPEED_FAST =
            0.20f;


    // =========================================================
    // 动画 Timer
    // =========================================================

    public static final int TRANSITION_TIMER_DELAY =
            30;


    // =========================================================
    // 分辨率
    // =========================================================

    public static final Dimension[] RESOLUTIONS = {

            new Dimension(800, 600),

            new Dimension(1000, 700),

            new Dimension(1280, 720),

            new Dimension(1280, 800),

            new Dimension(1366, 768),

            new Dimension(1920, 1080)
    };


    // =========================================================
    // 游戏 UI
    // =========================================================

    public static final int DIALOGUE_FONT_SIZE =
            24;

    public static final int CHOICE_FONT_SIZE =
            20;

    public static final int CHOICE_WIDTH =
            500;

    public static final int CHOICE_HEIGHT =
            50;


    // =========================================================
    // 字体
    // =========================================================

    public static final String UI_FONT =
            "微软雅黑";

    public static final String CODE_FONT =
            "Monospaced";


    // =========================================================
    // 动画设置滑块
    // =========================================================

    public static final int ANIMATION_SLIDER_MIN =
            5;

    public static final int ANIMATION_SLIDER_MAX =
            20;

    public static final int ANIMATION_SLIDER_SCALE =
            100;
}