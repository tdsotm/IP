

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Shows animated bouncing balls.
 *
 * @author Cay Horstmann
 * @version 1.33 2007-05-17
 */
public class BounceThread {
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new BounceFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
    }
}

/**
 * A runnable that animates a bouncing ball.
 */
class BallRunnable implements Runnable {
    /**
     * Constructs the runnable.
     *
     * @aBall the ball to bounce
     * @aPanel the component in which the ball bounces
     */
    public BallRunnable(Albinuta aBall, Component aComponent) {
        ball = aBall;
        component = aComponent;
    }

    public void run() {
        try {
            for (int i = 1; i <= STEPS; i++) {
                ball.move(component.getBounds());
                component.repaint();
                Thread.sleep(DELAY);
            }
        } catch (InterruptedException e) {
        }
    }

    private Albinuta ball;

    private Component component;

    public static final int STEPS = 1000;

    public static final int DELAY = 5;
}

/**
 * The frame with panel and buttons.
 */
class BounceFrame extends JFrame {
    /**
     * Constructs the frame with the component for showing the bouncing ball and
     * Start and Close buttons
     */
    public BounceFrame() {
        setSize(DEFAULT_WIDTH + 100, DEFAULT_HEIGHT + 100);
        setTitle("BounceThread");

        comp = new BallComponent();
        add(comp, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        addButton(buttonPanel, "Start", new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                addBall();
            }
        });

        addButton(buttonPanel, "Close", new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Adds a button to a container.
     *
     * @param c        the container
     * @param title    the button title
     * @param listener the action listener for the button
     */
    public void addButton(Container c, String title, ActionListener listener) {
        JButton button = new JButton(title);
        c.add(button);
        button.addActionListener(listener);
    }

    /**
     * Adds a bouncing ball to the canvas and starts a thread to make it bounce
     */
    public void addBall() {
        Albinuta b = new Albinuta();
        Albinuta b2 = new Albinuta();
        Albinuta b3 = new Albinuta();
        comp.add(b);
        comp.add(b2);
        comp.add(b3);
        Runnable r = new BallRunnable(b, comp);
        Runnable r2 = new BallRunnable(b2, comp);
        Runnable r3 = new BallRunnable(b3, comp);
        Thread t = new Thread(r);
        Thread t2 = new Thread(r2);
        Thread t3 = new Thread(r3);
        t.start();
        t2.start();
        t3.start();
    }

    private BallComponent comp;

    public static final int DEFAULT_WIDTH = 450;

    public static final int DEFAULT_HEIGHT = 350;

    public static final int STEPS = 1000;

    public static final int DELAY = 3;
}


/**
 * The component that draws the balls.
 *
 * @author Cay Horstmann
 * @version 1.33 2007-05-17
 */
class BallComponent extends JComponent {
    /**
     * Add a ball to the panel.
     *
     * @param b the ball to add
     */
    public void add(Albinuta b) {
        balls.add(b);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        for (Albinuta b : balls) {
            g2.fill(b.getShape());
        }
    }

    private ArrayList<Albinuta> balls = new ArrayList<Albinuta>();
}

class Albinuta {
    /**
     * Moves the ball to the next position, reversing direction if it hits one of
     * the edges
     */
    public void move(Rectangle2D bounds) {
        x += dx;
        y += dy;
        if (x < bounds.getMinX()) {
            x = bounds.getMinX();
            dx = -dx;
        }
        if (x + XSIZE >= bounds.getMaxX()) {
            x = bounds.getMaxX() - XSIZE;
            dx = -dx;
        }
        if (y < bounds.getMinY()) {
            y = bounds.getMinY();
            dy = -dy;
        }
        if (y + YSIZE >= bounds.getMaxY()) {
            y = bounds.getMaxY() - YSIZE;
            dy = -dy;
        }
    }

    /**
     * Gets the shape of the ball at its current position.
     */
    public Ellipse2D getShape() {
        return new Ellipse2D.Double(x, y, XSIZE, YSIZE);
    }

    private static final int XSIZE = 15;

    private static final int YSIZE = 15;

    private double x = 0;

    private double y = 0;

    private double dx = 1;

    private double dy = 1;
}
