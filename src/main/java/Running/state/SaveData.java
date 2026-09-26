package main.java.Running.state;

public class SaveData {
    public static final int VERSION = 1;

    private final int version;
    private final int pc;
    private final String background;
    private final String character;
    private final String bgm;

    public SaveData(int pc, String background, String character, String bgm) {
        this(VERSION, pc, background, character, bgm);
    }

    public SaveData(int version, int pc, String background, String character, String bgm) {
        this.version = version;
        this.pc = pc;
        this.background = background;
        this.character = character;
        this.bgm = bgm;
    }

    public int getVersion() { return version; }
    public int getPc() { return pc; }
    public String getBackground() { return background; }
    public String getCharacter() { return character; }
    public String getBgm() { return bgm; }
}
