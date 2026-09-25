package main.java.Running.ui;

public class GameUIConfig {

    // =========================
    // 游戏标题
    // =========================

    private String title =
            "MiniGalEngine";

    // =========================
    // 封面
    // =========================

    private String cover =
            null;

    // =========================
    // 菜单文字
    // =========================

    private String startText =
            "开始游戏";

    private String settingsText =
            "设置";

    private String exitText =
            "退出游戏";


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getStartText() {
        return startText;
    }

    public void setStartText(String startText) {
        this.startText = startText;
    }


    public String getSettingsText() {
        return settingsText;
    }

    public void setSettingsText(String settingsText) {
        this.settingsText = settingsText;
    }


    public String getExitText() {
        return exitText;
    }

    public void setExitText(String exitText) {
        this.exitText = exitText;
    }
}