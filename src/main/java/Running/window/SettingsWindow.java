package main.java.Running.window;

import main.java.Running.constants.RunningConstants;
import main.java.Running.settings.GameSettings;

import javax.swing.*;
import java.awt.*;

public class SettingsWindow extends JDialog {

    private final GameSettings settings;

    private final JCheckBox fullscreenBox;

    private final JSlider textSpeedSlider;

    private final JSlider animationSpeedSlider;

    private final JComboBox<String> resolutionBox;


    public SettingsWindow(
            JFrame parent,
            GameSettings settings
    ) {

        super(
                parent,
                "设置",
                true
        );

        this.settings =
                settings;


        // =====================================================
        // 窗口
        // =====================================================

        setSize(
                450,
                400
        );

        setLocationRelativeTo(
                parent
        );

        setResizable(
                false
        );


        // =====================================================
        // 主面板
        // =====================================================

        JPanel panel =
                new JPanel();

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        25,
                        30,
                        25,
                        30
                )
        );

        panel.setLayout(
                new GridBagLayout()
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.insets =
                new Insets(
                        10,
                        5,
                        10,
                        5
                );


        // =====================================================
        // 全屏
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(
                new JLabel("全屏"),
                gbc
        );


        fullscreenBox =
                new JCheckBox();

        fullscreenBox.setSelected(
                settings.isFullscreen()
        );

        gbc.gridx = 1;

        panel.add(
                fullscreenBox,
                gbc
        );


        // =====================================================
        // 文本速度
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(
                new JLabel("文本速度"),
                gbc
        );


        // -----------------------------------------------------
        // 获取当前文本速度
        // -----------------------------------------------------

        int currentTextSpeed =
                settings.getTextSpeed();


        // -----------------------------------------------------
        // 防止旧配置越界
        //
        // FAST  = 10
        // SLOW  = 200
        //
        // 如果以前保存过 10000，
        // 这里会自动限制到 200。
        // -----------------------------------------------------

        currentTextSpeed =
                Math.max(
                        RunningConstants.TEXT_SPEED_FAST,
                        Math.min(
                                currentTextSpeed,
                                RunningConstants.TEXT_SPEED_SLOW
                        )
                );


        // -----------------------------------------------------
        // 创建文本速度 Slider
        //
        // 底层数值：
        //
        // 10ms  = 快
        // 50ms  = 默认
        // 200ms = 慢
        //
        // 但是我们使用：
        //
        // setInverted(true)
        //
        // 所以 UI：
        //
        // 左边 = 慢
        // 右边 = 快
        // -----------------------------------------------------

        textSpeedSlider =
                new JSlider(
                        RunningConstants.TEXT_SPEED_FAST,
                        RunningConstants.TEXT_SPEED_SLOW,
                        currentTextSpeed
                );


        textSpeedSlider.setInverted(
                true
        );


        textSpeedSlider.setMajorTickSpacing(
                20
        );


        textSpeedSlider.setPaintTicks(
                true
        );


        gbc.gridx = 1;

        panel.add(
                textSpeedSlider,
                gbc
        );


        // =====================================================
        // 动画速度
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(
                new JLabel("动画速度"),
                gbc
        );


        // -----------------------------------------------------
        // 获取当前动画速度
        // -----------------------------------------------------

        int currentAnimationSpeed =
                (int) (
                        settings.getAnimationSpeed()
                                * RunningConstants.ANIMATION_SLIDER_SCALE
                );


        // -----------------------------------------------------
        // 防止动画速度越界
        // -----------------------------------------------------

        currentAnimationSpeed =
                Math.max(
                        RunningConstants.ANIMATION_SLIDER_MIN,
                        Math.min(
                                currentAnimationSpeed,
                                RunningConstants.ANIMATION_SLIDER_MAX
                        )
                );


        // -----------------------------------------------------
        // 创建动画速度 Slider
        //
        // 5  = 0.05
        // 10 = 0.10
        // 20 = 0.20
        //
        // 数值越大，动画越快
        //
        // 所以这里保持：
        //
        // 左边 = 慢
        // 右边 = 快
        // -----------------------------------------------------

        animationSpeedSlider =
                new JSlider(
                        RunningConstants.ANIMATION_SLIDER_MIN,
                        RunningConstants.ANIMATION_SLIDER_MAX,
                        currentAnimationSpeed
                );


        animationSpeedSlider.setMajorTickSpacing(
                5
        );


        animationSpeedSlider.setPaintTicks(
                true
        );


        gbc.gridx = 1;

        panel.add(
                animationSpeedSlider,
                gbc
        );


        // =====================================================
        // 分辨率
        // =====================================================

        gbc.gridx = 0;
        gbc.gridy++;

        panel.add(
                new JLabel("分辨率"),
                gbc
        );


        String[] resolutions =
                new String[
                        RunningConstants.RESOLUTIONS.length
                        ];


        for (
                int i = 0;
                i < RunningConstants.RESOLUTIONS.length;
                i++
        ) {

            Dimension resolution =
                    RunningConstants.RESOLUTIONS[i];


            resolutions[i] =
                    resolution.width
                            + "x"
                            + resolution.height;
        }


        resolutionBox =
                new JComboBox<>(
                        resolutions
                );


        resolutionBox.setSelectedItem(
                settings.getWidth()
                        + "x"
                        + settings.getHeight()
        );


        gbc.gridx = 1;

        panel.add(
                resolutionBox,
                gbc
        );


        // =====================================================
        // 按钮
        // =====================================================

        JPanel buttonPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT
                        )
                );


        // -----------------------------------------------------
        // 取消
        // -----------------------------------------------------

        JButton cancel =
                new JButton(
                        "取消"
                );


        cancel.addActionListener(
                e -> dispose()
        );


        // -----------------------------------------------------
        // 确定
        // -----------------------------------------------------

        JButton confirm =
                new JButton(
                        "确定"
                );


        confirm.addActionListener(
                e -> applySettings()
        );


        buttonPanel.add(
                cancel
        );


        buttonPanel.add(
                confirm
        );


        gbc.gridx = 0;
        gbc.gridy++;

        gbc.gridwidth = 2;


        panel.add(
                buttonPanel,
                gbc
        );


        // =====================================================
        // 添加主面板
        // =====================================================

        add(
                panel
        );
    }


    // =========================================================
    // 应用设置
    // =========================================================

    private void applySettings() {

        // =====================================================
        // 全屏
        // =====================================================

        settings.setFullscreen(
                fullscreenBox.isSelected()
        );


        // =====================================================
        // 文本速度
        //
        // Slider：
        //
        // 左边 = 大数值 = 慢
        // 右边 = 小数值 = 快
        //
        // setInverted(true)
        // 只改变显示方向，
        // 不改变实际 value。
        // =====================================================

        settings.setTextSpeed(
                textSpeedSlider.getValue()
        );


        // =====================================================
        // 动画速度
        // =====================================================

        settings.setAnimationSpeed(
                animationSpeedSlider.getValue()
                        / 100.0f
        );


        // =====================================================
        // 分辨率
        // =====================================================

        String resolution =
                (String)
                        resolutionBox
                                .getSelectedItem();


        if (resolution != null) {

            String[] parts =
                    resolution.split("x");


            int width =
                    Integer.parseInt(
                            parts[0]
                    );


            int height =
                    Integer.parseInt(
                            parts[1]
                    );


            settings.setResolution(
                    width,
                    height
            );
        }


        // =====================================================
        // 关闭设置窗口
        // =====================================================

        dispose();
    }
}