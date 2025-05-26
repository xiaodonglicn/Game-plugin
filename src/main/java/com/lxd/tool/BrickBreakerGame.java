package com.lxd.tool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreakerGame extends JPanel implements ActionListener, KeyListener, MouseMotionListener, ComponentListener {
    private int width; // 动态宽度
    private int height; // 动态高度
    private int paddleWidth = 100; // 挡板宽度
    private final int PADDLE_HEIGHT = 10; // 挡板高度
    private final int BALL_SIZE = 20; // 球的大小
    private int brickRows = 5; // 砖块行数
    private int brickCols = 8; // 砖块列数
    private int brickWidth; // 动态砖块宽度
    private final int BRICK_HEIGHT = 20; // 砖块高度

    private Timer timer;
    private int paddleX; // 挡板水平位置
    private int ballX, ballY; // 球的坐标
    private int ballSpeedX, ballSpeedY; // 球的速度
    private boolean[][] bricks; // 砖块状态（true 表示存在）
    private int currentLevel = 1; // 当前关卡
    private int score = 0; // 得分
    private boolean gameOver = false; // 游戏是否结束
    private boolean gameWon = false; // 是否通关
    private boolean gameStarted = false; // 游戏是否开始
    private JButton startButton; // 开始游戏按钮
    private JButton restartButton; // 重新游戏按钮

    public BrickBreakerGame() {
        setBackground(new Color(30, 30, 30)); // 暗色背景
        setFocusable(true);
        addKeyListener(this);
        addMouseMotionListener(this);
        addComponentListener(this); // 监听窗口大小变化

        // 初始化开始游戏按钮
        startButton = new JButton("Start Game");
        startButton.setBackground(new Color(50, 50, 50)); // 暗色按钮背景
        startButton.setForeground(Color.WHITE); // 白色文字
        startButton.setFocusable(false);
        startButton.addActionListener(e -> startGame());
        startButton.setBounds(0, 0, 100, 30); // 初始位置
        startButton.setVisible(true); // 初始显示
        add(startButton);

        // 初始化重新游戏按钮
        restartButton = new JButton("Restart Game");
        restartButton.setBackground(new Color(50, 50, 50)); // 暗色按钮背景
        restartButton.setForeground(Color.WHITE); // 白色文字
        restartButton.setFocusable(false);
        restartButton.setVisible(false); // 初始隐藏
        restartButton.addActionListener(e -> restartGame());
        add(restartButton);

        initializeGame();
    }

    // 初始化游戏
    private void initializeGame() {
        width = getWidth();
        height = getHeight();
        brickWidth = width / brickCols; // 动态计算砖块宽度

        paddleX = width / 2 - paddleWidth / 2;
        ballX = width / 2 - BALL_SIZE / 2;
        ballY = height - 100;

        // 初始化球的速度（第一关速度较慢，每关增加速度）
        ballSpeedX = 2 + (currentLevel - 1); // 第一关速度为 2，每关增加 1
        ballSpeedY = 2 + (currentLevel - 1); // 第一关速度为 2，每关增加 1

        // 初始化砖块
        bricks = new boolean[brickRows][brickCols];
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                bricks[i][j] = true;
            }
        }

        // 隐藏开始游戏按钮
        startButton.setVisible(false);
        gameStarted = true;
        timer = new Timer(10, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制挡板
        g.setColor(new Color(100, 100, 100)); // 暗灰色挡板
        g.fillRect(paddleX, height - 50, paddleWidth, PADDLE_HEIGHT);

        // 绘制球
        g.setColor(new Color(255, 100, 100)); // 红色球
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // 绘制砖块
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j]) {
                    g.setColor(new Color(50 + i * 20, 50 + j * 20, 50)); // 暗色砖块
                    g.fillRect(j * brickWidth, i * BRICK_HEIGHT + 30, brickWidth, BRICK_HEIGHT);
                }
            }
        }

        // 绘制得分和关卡
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 20);
        g.drawString("Level: " + currentLevel, width - 100, 20);

        // 游戏结束提示
        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", width / 2 - 80, height / 2);
            restartButton.setBounds(width / 2 - 50, height / 2 + 50, 100, 30);
            restartButton.setVisible(true); // 显示重新游戏按钮
        }

        // 游戏胜利提示
        if (gameWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("You win!", width / 2 - 60, height / 2);
            restartButton.setBounds(width / 2 - 50, height / 2 + 50, 100, 30);
            restartButton.setVisible(true); // 显示重新游戏按钮
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted) {
            moveBall();
            checkCollisions();
            repaint();
        }
    }

    // 移动球
    private void moveBall() {
        ballX += ballSpeedX;
        ballY += ballSpeedY;

        // 球碰到左右边界
        if (ballX <= 0 || ballX >= width - BALL_SIZE) {
            ballSpeedX = -ballSpeedX;
        }

        // 球碰到上边界
        if (ballY <= 0) {
            ballSpeedY = -ballSpeedY;
        }

        // 球碰到下边界（游戏结束）
        if (ballY >= height - BALL_SIZE) {
            gameOver = true;
            timer.stop();
        }
    }

    // 检测碰撞
    private void checkCollisions() {
        // 球碰到挡板
        if (ballY + BALL_SIZE >= height - 50 && ballX >= paddleX && ballX <= paddleX + paddleWidth) {
            ballSpeedY = -ballSpeedY;
        }

        // 球碰到砖块
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j]) {
                    int brickX = j * brickWidth;
                    int brickY = i * BRICK_HEIGHT + 30;

                    if (ballX + BALL_SIZE >= brickX && ballX <= brickX + brickWidth &&
                        ballY + BALL_SIZE >= brickY && ballY <= brickY + BRICK_HEIGHT) {
                        bricks[i][j] = false;
                        ballSpeedY = -ballSpeedY;
                        score += 10;

                        // 检查是否过关
                        if (isLevelComplete()) {
                            nextLevel();
                        }
                    }
                }
            }
        }
    }

    // 检查是否过关
    private boolean isLevelComplete() {
        for (int i = 0; i < brickRows; i++) {
            for (int j = 0; j < brickCols; j++) {
                if (bricks[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // 进入下一关
    private void nextLevel() {
        currentLevel++;
        if (currentLevel > 5) {
            gameWon = true;
            timer.stop();
        } else {
            // 每关增加球的速度
            ballSpeedX = 2 + (currentLevel - 1); // 每关增加 1
            ballSpeedY = 2 + (currentLevel - 1); // 每关增加 1
            initializeGame();
        }
    }

    // 开始游戏
    private void startGame() {
        gameStarted = true;
        startButton.setVisible(false);
        initializeGame();
    }

    // 重新开始游戏
    private void restartGame() {
        gameOver = false;
        gameWon = false;
        currentLevel = 1;
        score = 0;
        restartButton.setVisible(false);
        initializeGame();
    }

    // 键盘控制挡板
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // 左键移动挡板
        if (key == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 20;
        }
        // 右键移动挡板
        if (key == KeyEvent.VK_RIGHT && paddleX < width - paddleWidth) {
            paddleX += 20;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // 鼠标控制挡板
    @Override
    public void mouseMoved(MouseEvent e) {
        int mouseX = e.getX();
        paddleX = mouseX - paddleWidth / 2; // 挡板中心跟随鼠标

        // 限制挡板不超出边界
        if (paddleX < 0) {
            paddleX = 0;
        }
        if (paddleX > width - paddleWidth) {
            paddleX = width - paddleWidth;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    // 监听窗口大小变化
    @Override
    public void componentResized(ComponentEvent e) {
        // 重新计算小球速度和位置
        ballSpeedX = 2 + (currentLevel - 1);
        ballSpeedY = 2 + (currentLevel - 1);
        ballX = Math.min(ballX, getWidth() - BALL_SIZE); // 限制小球不超出新窗口边界
        ballY = Math.min(ballY, getHeight() - BALL_SIZE);
        initializeGame();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}
}