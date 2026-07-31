import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 600, HEIGHT = 600, DOT_SIZE = 25;
    private final int ALL_DOTS = 900;
    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private int dots, apple_x, apple_y, score = 0, level = 1;
    private boolean left = false, right = true, up = false, down = false, inGame = true;
    private Timer timer;
    private Random rand = new Random();

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
    }

    private void initGame() {
        dots = 3;
        for (int i = 0; i < dots; i++) {
            x[i] = 150 - i * DOT_SIZE;
            y[i] = 150;
        }
        locateApple();
        timer = new Timer(140, this);
        timer.start();
    }

    private void locateApple() {
        apple_x = rand.nextInt(WIDTH / DOT_SIZE) * DOT_SIZE;
        apple_y = rand.nextInt(HEIGHT / DOT_SIZE) * DOT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.setColor(Color.RED);
            g.fillOval(apple_x, apple_y, DOT_SIZE, DOT_SIZE);
            for (int i = 0; i < dots; i++) {
                if (i == 0) g.setColor(Color.GREEN);
                else g.setColor(new Color(45, 180, 0));
                g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
            }
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score + " | Level: " + level + " | By Er Bharat", 10, 20);
        } else {
            String msg = "Game Over! Score: " + score;
            g.setColor(Color.WHITE);
            g.drawString(msg, WIDTH/2 - 70, HEIGHT/2);
            g.drawString("Press R to Restart", WIDTH/2 - 60, HEIGHT/2 + 20);
        }
    }

    private void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++; score += 10;
            if (score % 50 == 0) { level++; timer.setDelay(Math.max(60, 140 - level*10)); }
            Toolkit.getDefaultToolkit().beep(); // Sound Effect
            locateApple();
        }
    }

    private void move() {
        for (int i = dots; i > 0; i--) { x[i] = x[i-1]; y[i] = y[i-1]; }
        if (left) x[0] -= DOT_SIZE;
        if (right) x[0] += DOT_SIZE;
        if (up) y[0] -= DOT_SIZE;
        if (down) y[0] += DOT_SIZE;
    }

    private void checkCollision() {
        if (x[0] >= WIDTH || x[0] < 0 || y[0] >= HEIGHT || y[0] < 0) inGame = false;
        for (int i = dots; i > 0; i--) if (i > 4 && x[0] == x[i] && y[0] == y[i]) inGame = false;
        if (!inGame) timer.stop();
    }

    @Override public void actionPerformed(ActionEvent e) { if (inGame) { checkApple(); checkCollision(); move(); } repaint(); }
    @Override public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT &&!right) { left=true; up=false; down=false; }
        if (key == KeyEvent.VK_RIGHT &&!left) { right=true; up=false; down=false; }
        if (key == KeyEvent.VK_UP &&!down) { up=true; left=false; right=false; }
        if (key == KeyEvent.VK_DOWN &&!up) { down=true; left=false; right=false; }
        if (key == KeyEvent.VK_R &&!inGame) { score=0; level=1; left=false; right=true; up=false; down=false; inGame=true; initGame(); }
    }
    @Override public void keyReleased(KeyEvent e) {} @Override public void keyTyped(KeyEvent e) {}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game Advanced - By Er Bharat Rajput");
        frame.add(new SnakeGame()); frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); frame.setVisible(true);
    }
}
