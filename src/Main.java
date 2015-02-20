import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Doru on 2/20/2015.
 */

public class Main {
    public static void main(String args[]) {
        final JFrame workspace = new JFrame("Lab1");
        workspace.setLayout(null);
        workspace.setSize(new Dimension(700, 500));
        workspace.setMinimumSize(new Dimension(700, 500));


        final ArrayList<JPanel> listaPaneluri = new ArrayList<JPanel>();
        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            int time = 0;
            int clickedRecs = 0;

            @Override
            public void run() {

                time++;
                final int xPos = new Random().nextInt(600);
                final int yPos = new Random().nextInt(400);
                MyRekt x = new MyRekt(xPos, yPos, 50, 50);
                workspace.add(x);
                x.setVisible(true);

                listaPaneluri.add(x);
                workspace.pack();
                workspace.repaint();
                workspace.setVisible(true);
                if (time == 5) {
                    for (JPanel j : listaPaneluri) {
                        if (!j.isEnabled()) {
                            clickedRecs++;
                        }
                    }


                    t.cancel();
                    System.out.println("Scor: " + clickedRecs);
                    JOptionPane.showMessageDialog(null, "Scor: " + clickedRecs);

                }
            }

        }, 0, 1000);


    }


}
