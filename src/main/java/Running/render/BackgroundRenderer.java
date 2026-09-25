package main.java.Running.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BackgroundRenderer {

    /**
     * 当前背景
     */
    private BufferedImage background;

    /**
     * 即将切换进去的背景
     */
    private BufferedImage nextBackground;


    // =========================
    // 设置背景
    // =========================

    public void setBackground(String path) {

        try {

            BufferedImage image =
                    ImageIO.read(
                            new File(path)
                    );

            if (image == null) {

                throw new RuntimeException(
                        "无法识别图片: " + path
                );
            }

            nextBackground = image;

        } catch (IOException e) {

            throw new RuntimeException(
                    "无法加载背景图片: " + path,
                    e
            );
        }
    }


    // =========================
    // 判断是否有背景
    // =========================

    public boolean hasBackground() {

        return background != null;
    }


    // =========================
    // 动画结束
    // =========================

    public void finishTransition() {

        if (nextBackground == null) {
            return;
        }

        background =
                nextBackground;

        nextBackground = null;
    }


    // =========================
    // 绘制
    // =========================

    public void draw(
            Graphics g,
            int width,
            int height,
            float alpha
    ) {

        if (background == null &&
                nextBackground == null) {

            return;
        }


        Graphics2D g2 =
                (Graphics2D) g.create();


        // =========================
        // 当前背景
        // =========================

        if (background != null) {

            float oldAlpha =
                    1.0f - alpha;

            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            oldAlpha
                    )
            );

            g2.drawImage(
                    background,
                    0,
                    0,
                    width,
                    height,
                    null
            );
        }


        // =========================
        // 新背景
        // =========================

        if (nextBackground != null) {

            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            alpha
                    )
            );

            g2.drawImage(
                    nextBackground,
                    0,
                    0,
                    width,
                    height,
                    null
            );
        }


        g2.dispose();
    }
}