package main.java.Running.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundRenderer {
    private BufferedImage currentImage;
    private BufferedImage nextImage;
    private String currentPath;
    private String nextPath;

    public void setBackground(String path) {
        BufferedImage image = load(path);
        if (image == null) return;
        nextImage = image;
        nextPath = path;
        if (currentImage == null) finishTransition();
    }

    public void draw(Graphics g, int width, int height, float alpha) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            if (currentImage != null) {
                drawImage(g2, currentImage, width, height, 1.0f);
            }
            if (nextImage != null && nextImage != currentImage) {
                drawImage(g2, nextImage, width, height, alpha);
            }
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

    public boolean hasBackground() { return currentImage != null; }
    public String getCurrentPath() { return currentPath; }

    public void clear() {
        currentImage = null;
        nextImage = null;
        currentPath = null;
        nextPath = null;
    }

    private BufferedImage load(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException | RuntimeException e) {
            System.err.println("Failed to load background: " + path);
            return null;
        }
    }

    private void drawImage(Graphics2D g, BufferedImage image, int width, int height, float alpha) {
        float imageRatio = image.getWidth() / (float) image.getHeight();
        float viewRatio = width / (float) Math.max(1, height);
        int drawWidth;
        int drawHeight;

        if (imageRatio > viewRatio) {
            drawHeight = height;
            drawWidth = Math.round(height * imageRatio);
        } else {
            drawWidth = width;
            drawHeight = Math.round(width / imageRatio);
        }

        int x = (width - drawWidth) / 2;
        int y = (height - drawHeight) / 2;

        Composite old = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0, Math.min(1, alpha))));
        g.drawImage(image, x, y, drawWidth, drawHeight, null);
        g.setComposite(old);
    }
}
