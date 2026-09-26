package main.java.Editor.constant;

import java.awt.*;

public final class EditorConstants {

    private EditorConstants() {
    }

    // =========================
    // 窗口
    // =========================

    public static final String APP_NAME =
            "MiniGalEngine";

    public static final String EDITOR_TITLE =
            "MiniGalEngine Editor";

    public static final int WINDOW_WIDTH =
            1400;

    public static final int WINDOW_HEIGHT =
            850;

    public static final int MIN_WINDOW_WIDTH =
            1000;

    public static final int MIN_WINDOW_HEIGHT =
            650;


    // =========================
    // 项目目录
    // =========================

    public static final String SCRIPTS_DIR =
            "scripts";

    public static final String RESOURCES_DIR =
            "resources";

    public static final String MAIN_SCRIPT =
            "main.txt";

    public static final String RESOURCE_SCRIPT =
            "resource.txt";


    // =========================
    // 资源目录
    // =========================

    public static final String BG_DIR =
            "bg";

    public static final String CHAR_DIR =
            "char";

    public static final String BGM_DIR =
            "bgm";

    public static final String VOICE_DIR =
            "voice";


    // =========================
    // UI 文本
    // =========================

    public static final String PROJECT_TITLE =
            "PROJECT";

    public static final String RESOURCE_TITLE =
            "RESOURCES";

    public static final String SCRIPT_TITLE =
            "SCRIPT";


    // =========================
    // 颜色
    // =========================

    public static final Color BACKGROUND =
            new Color(
                    30,
                    32,
                    36
            );

    public static final Color PANEL =
            new Color(
                    37,
                    40,
                    45
            );

    public static final Color PANEL_LIGHT =
            new Color(
                    45,
                    48,
                    54
            );

    public static final Color EDITOR_BACKGROUND =
            new Color(
                    25,
                    27,
                    30
            );

    public static final Color BORDER =
            new Color(
                    60,
                    63,
                    70
            );

    public static final Color TEXT =
            new Color(
                    225,
                    228,
                    232
            );

    public static final Color TEXT_SECONDARY =
            new Color(
                    150,
                    155,
                    165
            );

    public static final Color ACCENT =
            new Color(
                    92,
                    124,
                    250
            );


    // =========================
    // 字体
    // =========================

    public static final String UI_FONT =
            "SansSerif";

    public static final String CODE_FONT =
            "Monospaced";

    public static final int UI_FONT_SIZE =
            14;

    public static final int CODE_FONT_SIZE =
            16;


    // =========================
    // Tree
    // =========================

    public static final int TREE_ROW_HEIGHT =
            26;

    public static final int RESOURCE_TREE_ROW_HEIGHT =
            28;


    // =========================
    // Editor
    // =========================

    public static final int PROJECT_PANEL_WIDTH =
            230;

    public static final int RESOURCE_PANEL_WIDTH =
            280;
}