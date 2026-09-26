package main.java.Running.manager;

import main.java.Running.state.SaveData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    private final File saveDirectory;

    public SaveManager(File saveDirectory) {
        this.saveDirectory = saveDirectory;
        if (!saveDirectory.exists() && !saveDirectory.mkdirs()) {
            throw new IllegalStateException("Cannot create save directory: " + saveDirectory);
        }
    }

    public File getSaveDirectory() { return saveDirectory; }

    public void save(String slot, SaveData data) throws IOException {
        validateSlot(slot);
        File target = new File(saveDirectory, slot + ".sav");
        File temp = new File(saveDirectory, slot + ".sav.tmp");

        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(temp)))) {
            out.writeInt(data.getVersion());
            out.writeInt(data.getPc());
            writeNullable(out, data.getBackground());
            writeNullable(out, data.getCharacter());
            writeNullable(out, data.getBgm());
        }

        if (target.exists() && !target.delete()) {
            throw new IOException("Cannot replace save file: " + target);
        }
        if (!temp.renameTo(target)) {
            throw new IOException("Cannot finalize save file: " + target);
        }
    }

    public SaveData load(String slot) throws IOException {
        validateSlot(slot);
        File file = new File(saveDirectory, slot + ".sav");
        if (!file.isFile()) throw new FileNotFoundException("Save not found: " + slot);

        try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            int version = in.readInt();
            if (version != SaveData.VERSION) throw new IOException("Unsupported save version: " + version);

            int pc = in.readInt();
            String background = readNullable(in);
            String character = readNullable(in);
            String bgm = readNullable(in);
            return new SaveData(version, pc, background, character, bgm);
        }
    }

    public boolean exists(String slot) {
        return new File(saveDirectory, slot + ".sav").isFile();
    }

    public List<String> listSlots(int count) {
        List<String> result = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            String slot = String.valueOf(i);
            if (exists(slot)) result.add(slot);
        }
        return result;
    }

    private void validateSlot(String slot) {
        if (slot == null || !slot.matches("[1-9][0-9]*")) {
            throw new IllegalArgumentException("Invalid save slot: " + slot);
        }
    }

    private void writeNullable(DataOutputStream out, String value) throws IOException {
        if (value == null) {
            out.writeBoolean(false);
        } else {
            out.writeBoolean(true);
            out.writeUTF(value);
        }
    }

    private String readNullable(DataInputStream in) throws IOException {
        return in.readBoolean() ? in.readUTF() : null;
    }
}
