import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import java.awt.color.*;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Random;
import java.awt.event.KeyListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class Snakegame extends JPanel implements ActionListener {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int UNIT_SIZE = 20; // Size of each grid unit
    private static final int DELAY = 100; // Delay between frame updates

    private Snake snake;
    private Food food;
    private boolean isGameOver;

    public Snakegame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new GameKeyListener());

        snake = new Snake();
        food = new Food();
        food.generateFood();
        isGameOver = false;

        Timer timer = new Timer(DELAY, this);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            snake.move();

            if (snake.isCollidedWithWall() || snake.isCollidedWithItself()) {
                isGameOver = true;
            }

            if (snake.collidesWithFood(food)) {
                snake.eatFood();
                food.generateFood();
            }

            repaint();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isGameOver) {
            snake.draw(g);
            food.draw(g);
        } else {
            gameOver(g);
        }
    }

    private void gameOver(Graphics g) {
        // Display game over message
        g.setColor(Color.RED);
        g.setFont(new Font("Helvetica", Font.BOLD, 48));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String gameOverMsg = "Game Over";
        int x = (WIDTH - metrics.stringWidth(gameOverMsg)) / 2;
        int y = HEIGHT / 2;
        g.drawString(gameOverMsg, x, y);
    }

    private class GameKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            Direction newDirection = null;

            switch (key) {
                case KeyEvent.VK_UP:
                    newDirection = Direction.UP;
                    break;
                case KeyEvent.VK_DOWN:
                    newDirection = Direction.DOWN;
                    break;
                case KeyEvent.VK_LEFT:
                    newDirection = Direction.LEFT;
                    break;
                case KeyEvent.VK_RIGHT:
                    newDirection = Direction.RIGHT;
                    break;
            }

            if (newDirection != null) {
                snake.setDirection(newDirection);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            Snakegame game = new Snakegame();
            frame.add(game);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}

enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public Direction reverse() {
        return null;
    }
}

class Point {
    int x, y;

    Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj.getClass() != this.getClass()) {
            return false;
        }

        final Point other = (Point) obj;
        return this.x == other.x && this.y == other.y;
    }
}

class Snake {
    private LinkedList<Point> body;
    private Direction direction;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(5, 5)); // Starting position
        direction = Direction.RIGHT; // Starting direction
    }

    public void move() {
        // move snake in current direction
        Point head = getHead();
        Point newHead = new Point(head.x, head.y);

        switch (direction) {
            case UP:
                newHead.y--;
                break;
            case DOWN:
                newHead.y++;
                break;
            case LEFT:
                newHead.x--;
                break;
            case RIGHT:
                newHead.x++;
                break;
        }

        body.addFirst(newHead);
        body.removeLast();
    }

    public void setDirection(Direction newDirection) {
        if (body.size() > 1 && newDirection == direction.reverse()) {
            return;
        }
        direction = newDirection;
    }

    public boolean collidesWithFood(Food food) {
        Point head = getHead();
        return head.x == food.getX() && head.y == food.getY();
    }

    public void eatFood() {
        Point secondToLast = body.getLast();
        Point newBodyPart = null;

        switch (direction) {
            case UP:
                newBodyPart = new Point(secondToLast.x, secondToLast.y - 1);
                break;
            case DOWN:
                newBodyPart = new Point(secondToLast.x, secondToLast.y + 1);
                break;
            case LEFT:
                newBodyPart = new Point(secondToLast.x - 1, secondToLast.y);
                break;
            case RIGHT:
                newBodyPart = new Point(secondToLast.x + 1, secondToLast.y);
                break;
        }

        body.addLast(newBodyPart);
    }

    public boolean isCollidedWithWall() {
        Point head = getHead();
        return head.x < 0 || head.x >= Snakegame.WIDTH / Snakegame.UNIT_SIZE || head.y < 0
                || head.y >= Snakegame.HEIGHT / Snakegame.UNIT_SIZE;

    }

    public boolean isCollidedWithItself() {
        Point head = getHead();

        for (int i = 1; i < body.size(); i++) {
            if (head.x == body.get(i).x && head.y == body.get(i).y) {
                return true;
            }
        }
        return false;
    }

    public Point getHead() {
        return body.getFirst();
    }

    public void draw(Graphics g) {
        for (Point segment : body) {
            g.setColor(Color.GREEN);
            g.fillRect(segment.x * Snakegame.UNIT_SIZE, segment.y * Snakegame.UNIT_SIZE, Snakegame.UNIT_SIZE,
                    Snakegame.UNIT_SIZE);
        }
    }
}

class Food {
    private int x, y;

    Public Apple() {
        generateFood();
        return null;
    }

    public void generateFood() {
        Random random = new Random();
        x = random.nextInt(Snakegame.WIDTH / Snakegame.UNIT_SIZE);
        y = random.nextInt(Snakegame.HEIGHT / Snakegame.UNIT_SIZE);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x * Snakegame.UNIT_SIZE, y * Snakegame.UNIT_SIZE, Snakegame.UNIT_SIZE, Snakegame.UNIT_SIZE);
    }
}