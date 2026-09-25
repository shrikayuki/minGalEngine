package main.java.Running.window;

import main.java.Running.parser.GameVM;
import main.java.Running.parser.ScriptParser;
import main.java.Running.settings.GameSettings;
import main.java.Running.ui.GameUIConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

public class StartWindow extends JFrame {

    // =========================================================
    // UI 配置
    // =========================================================

    private final GameUIConfig uiConfig;


    // =========================================================
    // 主面板
    // =========================================================

    private JPanel mainPanel;


    // =========================================================
    // 设置
    // =========================================================
    private final GameSettings settings;


    public StartWindow() {

        this(
                new GameSettings()
        );
    }

    // =========================================================
    // 构造方法
    // =========================================================

    public StartWindow(GameSettings settings) {

        // -----------------------------------------------------
        // 默认 UI 配置
        // -----------------------------------------------------

        uiConfig = new GameUIConfig();

        // -----------------------------------------------------
        // 设置
        // -----------------------------------------------------
        this.settings = settings;


        // -----------------------------------------------------
        // 窗口
        // -----------------------------------------------------

        setTitle(
                uiConfig.getTitle()
        );

        setSize(
                1000,
                700
        );

        setMinimumSize(
                new Dimension(
                        800,
                        600
                )
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // -----------------------------------------------------
        // 创建 UI
        // -----------------------------------------------------

        createUI();
    }


    // =========================================================
    // 创建 UI
    // =========================================================

    private void createUI() {

        mainPanel =
                new JPanel() {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        super.paintComponent(g);

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        // -------------------------------------------------
                        // 默认背景
                        // -------------------------------------------------

                        GradientPaint gradient =
                                new GradientPaint(
                                        0,
                                        0,
                                        new Color(
                                                20,
                                                22,
                                                30
                                        ),
                                        getWidth(),
                                        getHeight(),
                                        new Color(
                                                55,
                                                45,
                                                70
                                        )
                                );

                        g2.setPaint(
                                gradient
                        );

                        g2.fillRect(
                                0,
                                0,
                                getWidth(),
                                getHeight()
                        );

                        // -------------------------------------------------
                        // 中央装饰光晕
                        // -------------------------------------------------

                        RadialGradientPaint glow =
                                new RadialGradientPaint(
                                        new Point(
                                                getWidth() / 2,
                                                getHeight() / 3
                                        ),
                                        Math.max(
                                                getWidth(),
                                                getHeight()
                                        ) * 0.55f,
                                        new float[]{
                                                0.0f,
                                                1.0f
                                        },
                                        new Color[]{
                                                new Color(
                                                        120,
                                                        100,
                                                        180,
                                                        80
                                                ),
                                                new Color(
                                                        0,
                                                        0,
                                                        0,
                                                        0
                                                )
                                        }
                                );

                        g2.setPaint(
                                glow
                        );

                        g2.fillRect(
                                0,
                                0,
                                getWidth(),
                                getHeight()
                        );

                        g2.dispose();
                    }
                };

        mainPanel.setLayout(
                new GridBagLayout()
        );

        setContentPane(
                mainPanel
        );


        // =========================================================
        // 中央菜单
        // =========================================================

        JPanel menuPanel =
                createMenuPanel();

        mainPanel.add(
                menuPanel
        );
    }


    // =========================================================
    // 创建菜单
    // =========================================================

    private JPanel createMenuPanel() {

        JPanel panel =
                new JPanel();

        panel.setOpaque(false);

        panel.setLayout(
                new BoxLayout(
                        panel,
                        BoxLayout.Y_AXIS
                )
        );


        // =====================================================
        // 封面区域
        // =====================================================

        JLabel cover =
                createCover();

        cover.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                cover
        );


        // =====================================================
        // 标题
        // =====================================================

        panel.add(
                Box.createVerticalStrut(
                        20
                )
        );

        JLabel title =
                new JLabel(
                        uiConfig.getTitle()
                );

        title.setFont(
                new Font(
                        "微软雅黑",
                        Font.BOLD,
                        44
                )
        );

        title.setForeground(
                Color.WHITE
        );

        title.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                title
        );


        // =====================================================
        // 副标题
        // =====================================================

        panel.add(
                Box.createVerticalStrut(
                        8
                )
        );

        JLabel subtitle =
                new JLabel(
                        "A Java Visual Novel Engine"
                );

        subtitle.setFont(
                new Font(
                        "微软雅黑",
                        Font.PLAIN,
                        15
                )
        );

        subtitle.setForeground(
                new Color(
                        190,
                        190,
                        205
                )
        );

        subtitle.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                subtitle
        );


        // =====================================================
        // 菜单按钮
        // =====================================================

        panel.add(
                Box.createVerticalStrut(
                        45
                )
        );

        JButton startButton =
                createMenuButton(
                        uiConfig.getStartText()
                );

        startButton.addActionListener(
                e -> startGame()
        );

        panel.add(
                startButton
        );


        panel.add(
                Box.createVerticalStrut(
                        14
                )
        );


        JButton settingsButton =
                createMenuButton(
                        uiConfig.getSettingsText()
                );

        settingsButton.addActionListener(
                e -> showSettings()
        );

        panel.add(
                settingsButton
        );


        panel.add(
                Box.createVerticalStrut(
                        14
                )
        );


        JButton exitButton =
                createMenuButton(
                        uiConfig.getExitText()
                );

        exitButton.addActionListener(
                e -> System.exit(0)
        );

        panel.add(
                exitButton
        );


        // =====================================================
        // 底部版本号
        // =====================================================

        panel.add(
                Box.createVerticalStrut(
                        30
                )
        );

        JLabel version =
                new JLabel(
                        "MiniGalEngine"
                );

        version.setFont(
                new Font(
                        "微软雅黑",
                        Font.PLAIN,
                        12
                )
        );

        version.setForeground(
                new Color(
                        140,
                        140,
                        155
                )
        );

        version.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        panel.add(
                version
        );


        return panel;
    }


    // =========================================================
    // 默认封面
    // =========================================================

    private JLabel createCover() {

        JLabel coverLabel =
                new JLabel();

        coverLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        coverLabel.setVerticalAlignment(
                SwingConstants.CENTER
        );

        // -----------------------------------------------------
        // 如果以后 Editor 配置了封面
        // -----------------------------------------------------

        if (uiConfig.getCover() != null &&
                !uiConfig.getCover().isBlank()) {

            File file =
                    new File(
                            uiConfig.getCover()
                    );

            if (file.exists()) {

                ImageIcon icon =
                        new ImageIcon(
                                file.getAbsolutePath()
                        );

                Image image =
                        icon.getImage();

                Image scaled =
                        image.getScaledInstance(
                                320,
                                180,
                                Image.SCALE_SMOOTH
                        );

                coverLabel.setIcon(
                        new ImageIcon(
                                scaled
                        )
                );

                return coverLabel;
            }
        }

        // -----------------------------------------------------
        // 没有封面时使用默认封面
        // -----------------------------------------------------

        coverLabel.setPreferredSize(
                new Dimension(
                        320,
                        180
                )
        );

        coverLabel.setMaximumSize(
                new Dimension(
                        320,
                        180
                )
        );

        coverLabel.setOpaque(true);

        coverLabel.setBackground(
                new Color(
                        35,
                        37,
                        48
                )
        );

        coverLabel.setBorder(
                BorderFactory.createLineBorder(
                        new Color(
                                110,
                                100,
                                145
                        ),
                        1
                )
        );

        coverLabel.setText(
                "MiniGalEngine"
        );

        coverLabel.setFont(
                new Font(
                        "微软雅黑",
                        Font.PLAIN,
                        20
                )
        );

        coverLabel.setForeground(
                new Color(
                        170,
                        165,
                        190
                )
        );

        return coverLabel;
    }


    // =========================================================
    // 创建菜单按钮
    // =========================================================

    private JButton createMenuButton(
            String text
    ) {

        JButton button =
                new JButton(
                        text
                ) {

                    @Override
                    protected void paintComponent(
                            Graphics g
                    ) {

                        Graphics2D g2 =
                                (Graphics2D) g.create();

                        g2.setRenderingHint(
                                RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON
                        );

                        Color background;

                        if (getModel().isPressed()) {

                            background =
                                    new Color(
                                            75,
                                            65,
                                            105
                                    );

                        } else if (
                                getModel().isRollover()
                        ) {

                            background =
                                    new Color(
                                            90,
                                            78,
                                            125
                                    );

                        } else {

                            background =
                                    new Color(
                                            55,
                                            55,
                                            70,
                                            220
                                    );
                        }

                        g2.setColor(
                                background
                        );

                        g2.fill(
                                new RoundRectangle2D.Float(
                                        0,
                                        0,
                                        getWidth(),
                                        getHeight(),
                                        16,
                                        16
                                )
                        );

                        g2.setColor(
                                new Color(
                                        120,
                                        110,
                                        150
                                )
                        );

                        g2.draw(
                                new RoundRectangle2D.Float(
                                        0,
                                        0,
                                        getWidth() - 1,
                                        getHeight() - 1,
                                        16,
                                        16
                                )
                        );

                        g2.dispose();

                        super.paintComponent(
                                g
                        );
                    }
                };


        button.setFont(
                new Font(
                        "微软雅黑",
                        Font.PLAIN,
                        20
                )
        );

        button.setForeground(
                Color.WHITE
        );

        button.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        button.setPreferredSize(
                new Dimension(
                        260,
                        52
                )
        );

        button.setMaximumSize(
                new Dimension(
                        260,
                        52
                )
        );

        button.setMinimumSize(
                new Dimension(
                        260,
                        52
                )
        );

        button.setFocusPainted(
                false
        );

        button.setBorderPainted(
                false
        );

        button.setContentAreaFilled(
                false
        );

        button.setOpaque(
                false
        );

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }


    // =========================================================
    // 开始游戏
    // =========================================================

    private void startGame() {

        dispose();

        try {

            ScriptParser parser =
                    new ScriptParser();

            var program =
                    parser.parse(
                            java.nio.file.Path.of(
                                    "game.txt"
                            )
                    );

            GameVM vm =
                    new GameVM(
                            program
                    );

            GameWindow window =
                    new GameWindow(
                            vm,
                            settings
                    );

            window.setVisible(
                    true
            );

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "游戏启动失败：\n"
                            + ex.getMessage(),
                    "启动失败",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // 设置
    // =========================================================

    private void showSettings() {

        SettingsWindow window =
                new SettingsWindow(
                        this,
                        settings
                );

        window.setVisible(true);
    }


    // =========================================================
    // 测试
    // =========================================================

    public static void main(
            String[] args
    ) {

        SwingUtilities.invokeLater(
                () -> {

                    StartWindow window =
                            new StartWindow();

                    window.setVisible(
                            true
                    );
                }
        );
    }
}