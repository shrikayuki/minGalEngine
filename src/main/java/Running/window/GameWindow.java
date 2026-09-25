package main.java.Running.window;

import main.java.Running.animation.FadeTransition;
import main.java.Running.animation.Transition;
import main.java.Running.animation.TransitionStrategy;
import main.java.Running.constants.RunningConstants;
import main.java.Running.manager.AudioManager;
import main.java.Running.settings.GameSettings;
import main.java.Running.type.Choice;
import main.java.Running.parser.GameVM;
import main.java.Running.parser.Instruction;
import main.java.Running.type.OpCode;
import main.java.Running.render.BackgroundRenderer;
import main.java.Running.render.CharacterRenderer;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameWindow extends JFrame {

    // =========================================================
    // VM
    // =========================================================

    private final GameVM vm;


    // =========================================================
    // UI
    // =========================================================

    private final JLabel textLabel;

    private final JPanel choicePanel;

    private final JPanel gamePanel;


    // =========================================================
    // Renderer
    // =========================================================

    private final BackgroundRenderer backgroundRenderer;

    private final CharacterRenderer characterRenderer;


    // =========================================================
    // 音频
    // =========================================================

    private final AudioManager audioManager;


    // =========================================================
    // 对话打字机
    // =========================================================

    private Timer textTimer;

    private String currentText = "";

    private int textIndex = 0;

    private boolean typing = false;


    // =========================================================
    // 动画
    // =========================================================

    private final TransitionStrategy transitionStrategy;


    private Transition backgroundTransition;

    private Transition characterTransition;

    // =========================================================
    // 设置
    // =========================================================
    private final GameSettings settings;


    // =========================================================
    // 构造方法
    // =========================================================

    public GameWindow(GameVM vm, GameSettings settings) {

        this.vm = vm;

        this.settings = settings;


        // =====================================================
        // 创建 Renderer
        // =====================================================

        backgroundRenderer =
                new BackgroundRenderer();

        characterRenderer =
                new CharacterRenderer();


        // =====================================================
        // 创建音频管理器
        // =====================================================

        audioManager =
                new AudioManager();


        // =====================================================
        // 创建动画策略
        // =====================================================

        transitionStrategy =
                new FadeTransition();


        // =====================================================
        // 游戏画面
        // =====================================================

        getRootPane()
                .registerKeyboardAction(
                        e -> showGameMenu(),
                        KeyStroke.getKeyStroke(
                                "ESCAPE"
                        ),
                        JComponent.WHEN_IN_FOCUSED_WINDOW
                );

        gamePanel = new JPanel() {

            @Override
            protected void paintComponent(
                    Graphics g
            ) {

                super.paintComponent(g);


                // =============================================
                // 背景动画进度
                // =============================================

                float backgroundAlpha =
                        1.0f;

                if (backgroundTransition != null &&
                        backgroundTransition.isRunning()) {

                    backgroundAlpha =
                            transitionStrategy.alpha(
                                    backgroundTransition
                                            .getProgress()
                            );
                }


                // =============================================
                // 角色动画进度
                // =============================================

                float characterAlpha =
                        1.0f;

                if (characterTransition != null &&
                        characterTransition.isRunning()) {

                    characterAlpha =
                            transitionStrategy.alpha(
                                    characterTransition
                                            .getProgress()
                            );
                }


                // =============================================
                // 绘制背景
                // =============================================

                backgroundRenderer.draw(
                        g,
                        getWidth(),
                        getHeight(),
                        backgroundAlpha
                );


                // =============================================
                // 绘制角色
                // =============================================

                characterRenderer.draw(
                        g,
                        getWidth(),
                        getHeight(),
                        characterAlpha
                );
            }
        };


        // =====================================================
        // 窗口
        // =====================================================

        setTitle("MiniGalEngine");

        setSize(
                settings.getWidth(),
                settings.getHeight()
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        setLocationRelativeTo(null);


        // =====================================================
        // 对话文字
        // =====================================================

        textLabel =
                new JLabel();

        textLabel.setFont(
                new Font(
                        "微软雅黑",
                        Font.PLAIN,
                        24
                )
        );

        textLabel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );


        // =====================================================
        // 选择区域
        // =====================================================

        choicePanel =
                new JPanel();

        choicePanel.setLayout(
                new BoxLayout(
                        choicePanel,
                        BoxLayout.Y_AXIS
                )
        );

        choicePanel.setBorder(
                BorderFactory.createEmptyBorder(
                        10,
                        100,
                        20,
                        100
                )
        );

        choicePanel.setVisible(false);


        // =====================================================
        // 底部区域
        // =====================================================

        JPanel bottomPanel =
                new JPanel(
                        new BorderLayout()
                );


        bottomPanel.add(
                textLabel,
                BorderLayout.CENTER
        );


        bottomPanel.add(
                choicePanel,
                BorderLayout.SOUTH
        );


        // =====================================================
        // 加入窗口
        // =====================================================

        add(
                gamePanel,
                BorderLayout.CENTER
        );


        add(
                bottomPanel,
                BorderLayout.SOUTH
        );


        // =====================================================
        // 鼠标点击
        // =====================================================

        java.awt.event.MouseAdapter dialogueMouse =
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseClicked(
                            java.awt.event.MouseEvent e
                    ) {

                        // 选择状态下不能点击跳过
                        if (!choicePanel.isVisible()) {

                            handleDialogueClick();
                        }
                    }
                };


        gamePanel.addMouseListener(
                dialogueMouse
        );

        textLabel.addMouseListener(
                dialogueMouse
        );


        // =====================================================
        // 开始执行脚本
        // =====================================================

        showNext();
        applyFullscreen();
    }


    // =========================================================
    // 执行下一条指令
    // =========================================================

    private void showNext() {

        // -----------------------------------------------------
        // 清除旧的选择
        // -----------------------------------------------------

        choicePanel.removeAll();

        choicePanel.setVisible(false);


        // -----------------------------------------------------
        // 从 VM 获取下一条指令
        // -----------------------------------------------------

        Instruction instruction =
                vm.next();


        // -----------------------------------------------------
        // 没有下一条指令
        // -----------------------------------------------------

        if (instruction == null) {

            returnToTitle();

            return;
        }


        // =====================================================
        // BG
        // =====================================================

        if (instruction.getOpCode()
                == OpCode.BG) {

            String path =
                    (String)
                            instruction
                                    .getOperands()[0];


            changeBackground(path);

            return;
        }


        // =====================================================
        // CHAR
        // =====================================================

        if (instruction.getOpCode()
                == OpCode.CHAR) {

            String path =
                    (String)
                            instruction
                                    .getOperands()[0];


            changeCharacter(path);

            return;
        }


        // =====================================================
        // BGM
        // =====================================================

        if (instruction.getOpCode()
                == OpCode.BGM) {

            String path =
                    (String)
                            instruction
                                    .getOperands()[0];


            audioManager.playBgm(
                    "resources/audio/bgm/"
                            + path
            );


            // BGM 不需要等待

            showNext();

            return;
        }


        // =====================================================
        // VOICE
        // =====================================================

        if (instruction.getOpCode()
                == OpCode.VOICE) {

            String path =
                    (String)
                            instruction
                                    .getOperands()[0];


            audioManager.playVoice(
                    "resources/audio/voice/"
                            + path
            );


            // VOICE 不需要等待

            showNext();

            return;
        }


        // =====================================================
        // SAY
        // =====================================================

        if (instruction.getOpCode()
                == OpCode.SAY) {

            String text =
                    (String)
                            instruction
                                    .getOperands()[0];


            startTyping(text);


            revalidate();

            repaint();

            return;
        }


        // =====================================================
        // CHOICE
        // =====================================================

        if (instruction.getOpCode()
                == OpCode.CHOICE) {

            @SuppressWarnings("unchecked")
            List<Choice> choices =
                    (List<Choice>)
                            instruction
                                    .getOperands()[0];


            showChoices(choices);


            revalidate();

            repaint();
        }
    }


    // =========================================================
    // 背景切换
    // =========================================================

    private void changeBackground(
            String path
    ) {

        String fullPath =
                "resources/bg/"
                        + path;


        // -----------------------------------------------------
        // 判断是不是第一次加载
        // -----------------------------------------------------

        boolean firstLoad =
                !backgroundRenderer
                        .hasBackground();


        // -----------------------------------------------------
        // 加载新背景
        // -----------------------------------------------------

        backgroundRenderer.setBackground(
                fullPath
        );


        // =====================================================
        // 第一次加载
        // =====================================================

        if (firstLoad) {

            backgroundRenderer
                    .finishTransition();

            gamePanel.repaint();

            showNext();

            return;
        }


        // =====================================================
        // 创建背景动画
        // =====================================================

        if (backgroundTransition != null) {

            backgroundTransition.stop();
        }


        backgroundTransition =
                new Transition(

                        // Timer 间隔
                        RunningConstants.TRANSITION_TIMER_DELAY,

                        // 每次增加
                        settings.getAnimationSpeed(),

                        // update
                        () -> {

                            gamePanel.repaint();
                        },

                        // finish
                        () -> {

                            backgroundRenderer
                                    .finishTransition();


                            gamePanel.repaint();


                            // 动画结束
                            // 才执行下一条脚本

                            showNext();
                        }
                );


        backgroundTransition.start();
    }


    // =========================================================
    // 角色切换
    // =========================================================

    private void changeCharacter(
            String path
    ) {

        // -----------------------------------------------------
        // 清除角色
        // -----------------------------------------------------

        if (path.equals("NONE")) {

            characterRenderer.clear();

            gamePanel.repaint();

            showNext();

            return;
        }


        String fullPath =
                "resources/char/"
                        + path;


        // -----------------------------------------------------
        // 是否第一次加载
        // -----------------------------------------------------

        boolean firstLoad =
                !characterRenderer
                        .hasCharacter();


        // -----------------------------------------------------
        // 设置新角色
        // -----------------------------------------------------

        characterRenderer.setCharacter(
                fullPath
        );


        // =====================================================
        // 第一次加载
        // =====================================================

        if (firstLoad) {

            characterRenderer
                    .finishTransition();

            gamePanel.repaint();

            showNext();

            return;
        }


        // =====================================================
        // 停止旧动画
        // =====================================================

        if (characterTransition != null) {

            characterTransition.stop();
        }


        // =====================================================
        // 创建角色动画
        // =====================================================

        characterTransition =
                new Transition(

                        // Timer
                        RunningConstants.TRANSITION_TIMER_DELAY,

                        // progress
                        settings.getAnimationSpeed(),

                        // update
                        () -> {

                            gamePanel.repaint();
                        },

                        // finish
                        () -> {

                            characterRenderer
                                    .finishTransition();


                            gamePanel.repaint();


                            // 动画结束后
                            // 执行下一条

                            showNext();
                        }
                );


        characterTransition.start();
    }


    // =========================================================
    // 显示选择
    // =========================================================

    private void showChoices(
            List<Choice> choices
    ) {

        choicePanel.removeAll();


        for (int i = 0;
             i < choices.size();
             i++) {

            Choice choice =
                    choices.get(i);


            JButton button =
                    new JButton(
                            (i + 1)
                                    + ". "
                                    + choice
                                    .getText()
                    );


            button.setFont(
                    new Font(
                            RunningConstants.UI_FONT,
                            Font.PLAIN,
                            RunningConstants.DIALOGUE_FONT_SIZE
                    )
            );


            button.setAlignmentX(
                    Component.CENTER_ALIGNMENT
            );


            button.setMaximumSize(
                    new Dimension(
                            RunningConstants.CHOICE_WIDTH,
                            RunningConstants.CHOICE_HEIGHT
                    )
            );


            // =================================================
            // 选择按钮
            // =================================================

            button.addActionListener(
                    e -> {

                        // 跳转 LABEL

                        vm.jumpTo(
                                choice.getTarget()
                        );


                        // 执行下一条

                        showNext();
                    }
            );


            choicePanel.add(button);


            choicePanel.add(
                    Box.createVerticalStrut(
                            10
                    )
            );
        }


        choicePanel.setVisible(true);


        revalidate();

        repaint();
    }


    // =========================================================
    // 开始打字机
    // =========================================================

    private void startTyping(
            String text
    ) {

        // -----------------------------------------------------
        // 停止上一句
        // -----------------------------------------------------

        if (textTimer != null) {

            textTimer.stop();
        }


        // -----------------------------------------------------
        // 初始化
        // -----------------------------------------------------

        currentText =
                text;

        textIndex =
                0;

        typing =
                true;


        textLabel.setText(
                ""
        );


        // -----------------------------------------------------
        // 创建 Timer
        // -----------------------------------------------------

        textTimer =
                new Timer(
                        settings.getTextSpeed(),
                        e -> {

                            // =================================
                            // 打字完成
                            // =================================

                            if (textIndex
                                    >= currentText.length()) {

                                textTimer.stop();

                                typing = false;

                                return;
                            }


                            // =================================
                            // 多显示一个字符
                            // =================================

                            textIndex++;


                            textLabel.setText(
                                    currentText.substring(
                                            0,
                                            textIndex
                                    )
                            );
                        }
                );


        textTimer.start();
    }


    // =========================================================
    // 对话点击
    // =========================================================

    private void handleDialogueClick() {

        // =====================================================
        // 正在打字
        // =====================================================

        if (typing) {

            textTimer.stop();


            textLabel.setText(
                    currentText
            );


            typing = false;


            return;
        }


        // =====================================================
        // 文本已经完整显示
        // =====================================================

        showNext();
    }

    private void applyFullscreen() {

        GraphicsDevice device =
                GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice();


        // =====================================================
        // 当前是全屏
        // =====================================================

        if (settings.isFullscreen()) {

            // 已经是全屏
            if (device.getFullScreenWindow() == this) {
                return;
            }

            // 窗口必须先变成不可显示状态
            dispose();

            // 解除窗口装饰
            setUndecorated(true);

            // 进入全屏
            device.setFullScreenWindow(this);

            return;
        }


        // =====================================================
        // 当前是窗口模式
        // =====================================================

        if (device.getFullScreenWindow() == this) {

            // 退出全屏
            device.setFullScreenWindow(null);

            // 确保窗口不可显示
            dispose();

            // 恢复窗口边框
            setUndecorated(false);

            // 恢复分辨率
            setSize(
                    settings.getWidth(),
                    settings.getHeight()
            );

            setLocationRelativeTo(null);

            // 重新显示
            setVisible(true);

            return;
        }


        // =====================================================
        // 普通窗口
        // =====================================================

        if (!isDisplayable()) {
            setUndecorated(false);
        }

        setSize(
                settings.getWidth(),
                settings.getHeight()
        );

        setLocationRelativeTo(null);
    }

    private void showGameMenu() {

        Object[] options = {

                "继续游戏",

                "返回标题",

                "设置",

                "退出游戏"
        };


        int result =
                JOptionPane.showOptionDialog(
                        this,

                        "游戏菜单",

                        "菜单",

                        JOptionPane.DEFAULT_OPTION,

                        JOptionPane.PLAIN_MESSAGE,

                        null,

                        options,

                        options[0]
                );


        if (result == 0) {

            // 继续游戏

            return;
        }


        if (result == 1) {

            // 返回标题

            returnToTitle();

            return;
        }


        if (result == 2) {

            // 设置

            SettingsWindow settingsWindow =
                    new SettingsWindow(
                            this,
                            settings
                    );

            settingsWindow.setVisible(
                    true
            );

            applySettings();

            return;
        }


        if (result == 3) {

            System.exit(0);
        }
    }

    private void returnToTitle() {

        if (textTimer != null) {
            textTimer.stop();
        }

        audioManager.stopBgm();
        audioManager.stopVoice();

        if (backgroundTransition != null) {
            backgroundTransition.stop();
        }

        if (characterTransition != null) {
            characterTransition.stop();
        }

        dispose();

        SwingUtilities.invokeLater(
                () -> {
                    StartWindow window =
                            new StartWindow(settings);

                    window.setVisible(true);
                }
        );
    }

    private void applySettings() {

        // =========================
        // 应用全屏 / 窗口模式
        // =========================

        applyFullscreen();


        // =========================
        // 应用文字速度
        // =========================

        if (textTimer != null) {

            textTimer.setDelay(
                    settings.getTextSpeed()
            );
        }


        // =========================
        // 刷新界面
        // =========================

        revalidate();
        repaint();
    }
}