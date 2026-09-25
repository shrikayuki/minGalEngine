package main.java.Running.render;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CharacterRenderer {

    /**
     * 当前角色
     */
    private BufferedImage character;

    /**
     * 即将出现的角色
     */
    private BufferedImage nextCharacter;


    // =========================
    // 设置角色
    // =========================

    public void setCharacter(String path) {

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

            nextCharacter = image;

        } catch (IOException e) {

            throw new RuntimeException(
                    "无法加载角色图片: " + path,
                    e
            );
        }
    }


    // =========================
    // 是否存在角色
    // =========================

    public boolean hasCharacter() {

        return character != null;
    }


    // =========================
    // 完成切换
    // =========================

    public void finishTransition() {

        if (nextCharacter == null) {
            return;
        }

        character =
                nextCharacter;

        nextCharacter = null;
    }


    // =========================
    // 清除角色
    // =========================

    public void clear() {

        character = null;

        nextCharacter = null;
    }


    // =========================
    // 绘制角色
    // =========================

    public void draw(
            Graphics g,
            int width,
            int height,
            float alpha
    ) {

        if (character == null &&
                nextCharacter == null) {

            return;
        }


        Graphics2D g2 =
                (Graphics2D) g.create();


        // =========================
        // 旧角色
        // =========================

        if (character != null) {

            float oldAlpha =
                    1.0f - alpha;

            drawCharacter(
                    g2,
                    character,
                    width,
                    height,
                    oldAlpha
            );
        }


        // =========================
        // 新角色
        // =========================

        if (nextCharacter != null) {

            drawCharacter(
                    g2,
                    nextCharacter,
                    width,
                    height,
                    alpha
            );
        }


        g2.dispose();
    }


    // =========================
    // 真正绘制角色
    // =========================

    private void drawCharacter(
            Graphics2D g,
            BufferedImage image,
            int width,
            int height,
            float alpha
    ) {

        if (image == null) {
            return;
        }


        // =========================
        // 图片尺寸
        // =========================

        int originalWidth =
                image.getWidth();

        int originalHeight =
                image.getHeight();


        // =========================
        // 高度限制
        // =========================

        double scale =
                (height * 0.95)
                        / originalHeight;


        // =========================
        // 宽度限制
        // =========================

        double widthScale =
                (width * 0.9)
                        / originalWidth;


        // =========================
        // 取较小值
        // =========================

        scale =
                Math.min(
                        scale,
                        widthScale
                );


        // =========================
        // 最终尺寸
        // =========================

        int drawWidth =
                (int)
                        (originalWidth * scale);

        int drawHeight =
                (int)
                        (originalHeight * scale);


        // =========================
        // 水平居中
        // =========================

        int x =
                (width - drawWidth) / 2;


        // =========================
        // 底部对齐
        // =========================

        int y =
                height - drawHeight;


        // =========================
        // 设置透明度
        // =========================

        g.setComposite(
                AlphaComposite.getInstance(
                        AlphaComposite.SRC_OVER,
                        Math.max(
                                0.0f,
                                Math.min(
                                        1.0f,
                                        alpha
                                )
                        )
                )
        );


        // =========================
        // 绘制
        // =========================

        g.drawImage(
                image,
                x,
                y,
                drawWidth,
                drawHeight,
                null
        );
    }
}