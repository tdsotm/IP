import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Doru on 2/19/2015.
 */
public class Uranus extends JPanel implements ActionListener {
    int xPos, yPos;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(xPos, yPos, 50, 50);
        g.setColor(Color.black);
        g.fillRect(xPos, yPos, 50, 50);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.toString());
    }

    public Uranus(int x, int y, int width, int height) {

        super();
        xPos = x;
        yPos = y;
        Dimension d = new Dimension(50, 50);
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setOpaque(true);
        this.setBackground(Color.black);
        this.setMinimumSize(d);
        this.setMaximumSize(d);
        this.setPreferredSize(d);
        this.setSize(d);
        this.setLayout(null);

        this.setBounds(x, y, width, height);
        this.repaint();
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(e.toString());
                Component c = (Component) e.getSource();
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(c);
                parentFrame.remove(c);

                //  setVisible(false);
                c.setEnabled(false);

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //this.setOpaque(false);


    }
}
