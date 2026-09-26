package main.java.Running.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterRenderer {
    private BufferedImage currentImage;
    private BufferedImage nextImage;
    private String currentPath;
    private String nextPath;

    public void setCharacter(String path) {
        BufferedImage image = load(path);
        if (image == null) return;
        nextImage = image;
        nextPath = path;
        if (currentImage == null) finishTransition();
    }

    public void draw(Graphics g, int width, int height, float alpha) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            if (currentImage != null) drawCharacter(g2, currentImage, width, height, 1.0f);
            if (nextImage != null && nextImage != currentImage) drawCharacter(g2, nextImage, width, height, alpha);
        } finally {
            g2.dispose();
        }
    }

    public void finishTransition() {
        if (nextImage != null) {
            currentImage = nextImage;
            currentPath = nextPath;
            nextImage = null;
            nextPath = null;
        }
    }

    public void clear() {
        currentImage = null;
        nextImage = null;
        currentPath = null;
        nextPath = null;
    }

    public boolean hasCharacter() { return currentImage != null; }
    public String getCurrentPath() { return currentPath; }

    private BufferedImage load(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException | RuntimeException e) {
            System.err.println("Failed to load character: " + path);
            return null;
        }
    }

    private void drawCharacter(Graphics2D g, BufferedImage image, int width, int height, float alpha) {
        int maxHeight = (int) (height * 0.92);
        double scale = Math.min(1.0, maxHeight / (double) image.getHeight());
        int drawWidth = Math.max(1, (int) Math.round(image.getWidth() * scale));
        int drawHeight = Math.max(1, (int) Math.round(image.getHeight() * scale));
        int x = (width - drawWidth) / 2;
        int y = height - drawHeight;

        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha))));
        g.drawImage(image, x, y, drawWidth, drawHeight, null);
        g.setComposite(old);
    }
}
