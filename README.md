import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 800, HEIGHT = 600, UNIT = 25;
    private int[] x = new int[1000], y = new int[1000];
    private int bodyParts = 6, applesEaten, appleX, appleY, level = 1, delay = 120;
    private char direction = 'R';
    private boolean running = false, paused = false;
    private Timer timer;
    private Random random;

    public SnakeGame() {
        random = new Random();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);
        startGame();
    }
    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }
    public void newApple() {
        appleX = random.nextInt(WIDTH / UNIT) * UNIT;
        appleY = random.nextInt(HEIGHT / UNIT) * UNIT;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT, UNIT);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) { g.setColor(Color.GREEN); }
                else { g.setColor(new Color(45, 180, 0)); }
                g.fillRect(x[i], y[i], UNIT, UNIT);
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + applesEaten, 20, 30);
            g.drawString("Level: " + level, WIDTH - 120, 30);
            if(paused){
                g.setFont(new Font("Arial", Font.BOLD, 40));
                g.drawString("PAUSED - Press P", WIDTH/2 - 150, HEIGHT/2);
            }
        } else { gameOver(g); }
    }
    public void move() {
        for (int i = bodyParts; i > 0; i--) { x[i] = x[i-1]; y[i] = y[i-1]; }
        switch (direction) {
            case 'U': y[0] -= UNIT; break;
            case 'D': y[0] += UNIT; break;
            case 'L': x[0] -= UNIT; break;
            case 'R': x[0] += UNIT; break;
        }
    }
    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++; applesEaten++;
            if(applesEaten % 5 == 0){ level++; delay -= 10; timer.setDelay(delay); }
            newApple();
        }
    }
    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) { if (x[0] == x[i] && y[0] == y[i]) running = false; }
        if (x[0] < 0) x[0] = WIDTH - UNIT; if (x[0] >= WIDTH) x[0] = 0;
        if (y[0] < 0) y[0] = HEIGHT - UNIT; if (y[0] >= HEIGHT) y[0] = 0;
        if (!running) timer.stop();
    }
    public void gameOver(Graphics g) {
        g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Game Over", WIDTH/2 - 100, HEIGHT/2 - 50);
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + applesEaten, WIDTH/2 - 40, HEIGHT/2);
        g.drawString("Press R to Restart | Q to Quit", WIDTH/2 - 140, HEIGHT/2 + 40);
    }
    @Override public void actionPerformed(ActionEvent e) { if (running &&!paused) { move(); checkApple(); checkCollisions(); } repaint(); }
    @Override public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: if (direction!= 'R') direction = 'L'; break;
            case KeyEvent.VK_RIGHT: if (direction!= 'L') direction = 'R'; break;
            case KeyEvent.VK_UP: if (direction!= 'D') direction = 'U'; break;
            case KeyEvent.VK_DOWN: if (direction!= 'U') direction = 'D'; break;
            case KeyEvent.VK_P: paused =!paused; break;
            case KeyEvent.VK_R: if(!running){ bodyParts=6; applesEaten=0; level=1; delay=120; direction='R'; running=true; timer.setDelay(delay); timer.start(); for(int i=0;i<bodyParts;i++){x[i]=0; y[i]=0;} } break;
            case KeyEvent.VK_Q: System.exit(0); break;
        }
    }
    @Override public void keyReleased(KeyEvent e) {} @Override public void keyTyped(KeyEvent e) {}
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game Advanced - By Bharat");
        SnakeGame game = new SnakeGame();
        frame.add(game); frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack(); frame.setLocationRelativeTo(null); frame.setVisible(true);
    }
}
