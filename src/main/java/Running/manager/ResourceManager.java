package main.java.Running.manager;

import java.io.File;

public class ResourceManager {
    private final File root;
    private final File backgroundDir;
    private final File characterDir;
    private final File bgmDir;
    private final File voiceDir;

    public ResourceManager(File resourcesDirectory) {
        if (resourcesDirectory == null) throw new IllegalArgumentException("resourcesDirectory == null");
        this.root = resourcesDirectory;
        this.backgroundDir = new File(root, "bg");
        this.characterDir = new File(root, "char");
        this.bgmDir = new File(new File(root, "audio"), "bgm");
        this.voiceDir = new File(new File(root, "audio"), "voice");
    }

    public File getRoot() { return root; }
    public File getBackground(String name) { return new File(backgroundDir, name); }
    public File getCharacter(String name) { return new File(characterDir, name); }
    public File getBgm(String name) { return new File(bgmDir, name); }
    public File getVoice(String name) { return new File(voiceDir, name); }
}
