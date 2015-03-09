import java.awt.*;
import java.util.Enumeration;
import java.util.Vector;


class AlbPict implements Runnable {

    int naptime = 30;
    Albine albine;

    public AlbPict(Albine context) {
        albine = context;
    }

    public void
    nap(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException waken) {

        }
    }

    public void
    run() {
        while (true) {
            nap(naptime);
            albine.osPaint();
        }
    }
}

class SlowAlbina implements Runnable {

    public static double vx, vy;
    int naptime;

    public void
    nap(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException waken) {

        }
    }

    public void
    run() {
        while (true) {
            nap(naptime);
            vx = Math.random() - 0.5;
            vy = Math.random() - 0.5;
            naptime = (int) (Math.random() * 2000);
        }
    }
}

class Albina implements Runnable {

    double x, y;
    double dx, dy;
    double ddx, ddy;

    double r;
    Color c;
    Albine context;
    int naptime = 10;

    public Albina(Albine context) {
        double w = context.size().width;
        double h = context.size().height;
        r = Math.random() * Math.max(w, 500) / 5;
        x = Math.random() * w;
        y = Math.random() * h;

        this.context = context;
        move();
    }

    private synchronized void
    move() {
        double w = context.size().width;
        double h = context.size().height;
        ddx = SlowAlbina.vx / r * 10;
        ddy = SlowAlbina.vy / r * 10;
        dx += ddx;
        dy += ddy;
        x += dx;
        y += dy;

        if (x + r > w) {
            x = w - r;
            dx = -dx * .9;
        }
        if (x - r < 0) {
            x = r;
            dx = -dx * .9;
        }
        if (y + r > h) {
            y = h - r;
            dy = -dy * .9;
        }
        if (y - r < 0) {
            y = r;
            dy = -dy * .9;
        }
    }

    public synchronized void
    draw(Graphics g) {
        int ix = (int) (x - r);
        int iy = (int) (y - r);

//       int id = (int)(2*r);
//       g.setColor(c);
//       g.fillOval(ix, iy, id, id);                   
//       g.setColor(Color.black);


        Image img1 = Toolkit.getDefaultToolkit().getImage("albinuta.gif");
        System.out.println(img1);
        g.drawImage(img1, ix, iy, null);

    }


    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        Image img1 = Toolkit.getDefaultToolkit().getImage("albinuta.gif");
        g2.drawImage(img1, 10, 10, null);
        g2.finalize();
    }

    public void
    nap(int duration) {                              // run in real time
        try {
            Thread.sleep(duration);
        } catch (InterruptedException waken) {
            System.out.println("Albina.nap -- botched");
        }
    }

    public void
    run() {                                          // bouncing ball
        while (true) {
            nap(naptime);
            move();
        }
    }
}


public class Albine extends java.applet.Applet {

    Vector albine = new Vector();
    Vector controls = new Vector();


    int count = 0;
    Dimension screen;
    Graphics osg;
    Image osi;
    SlowAlbina w = new SlowAlbina();
    Thread wind;
    AlbPict p = new AlbPict(this);
    Thread picture;

    Albina b = new Albina(this);
    Thread t = new Thread(b);


    private String
    ballReport() {
        String t;
        if (count == 0) t = "  no albine active ";
        else if (count == 1) t = "   1 ball active ";
        else if (count < 10) t = "  " + count + " albine active ";
        else if (count < 100) t = " " + count + " albine active ";
        else t = count + " albine active ";
        return t;
    }

    private void
    insert() {
        Albina b = new Albina(this);
        Albina b2 = new Albina(this);
        Albina b3 = new Albina(this);// create a ball
        Thread t = new Thread(b);
        albine.addElement(b);
        albine.addElement(b2);
        albine.addElement(b3);
        // insert in list
        controls.addElement(t);


        t.start();

    }

    private void
    remove() {
        Thread t = (Thread) controls.elementAt(0);
        t.stop();
        controls.removeElement(t);
        albine.removeElementAt(0);
        count--;


    }

    public void
    init() {
        trace("begin Albine.init()");


        screen = this.size();
        osi = createImage(screen.width, screen.height);
        osg = osi.getGraphics();

        insert();

        controls.removeElementAt(0);
        trace("end   Albine.init()");
    }

    public boolean
    action(Event e, Object o) {
        insert();

        return super.action(e, o);
    }

    public void
    osPaint() {
        Dimension s = this.size();
        if (s.width != screen.width || s.height != screen.height) {
            screen = s;
            osi = createImage(screen.width, screen.height);
            osg = osi.getGraphics();
        }
        synchronized (osg) {
            osg.setColor(Color.white);
            osg.fillRect(0, 0, screen.width, screen.height);
            for (Enumeration e = albine.elements(); e.hasMoreElements(); ) {
                ((Albina) e.nextElement()).draw(osg);
            }
        }
        repaint();
    }

    public void
    paint(Graphics g) {
        synchronized (osg) {
            g.drawImage(osi, 0, 0, null);
        }
    }

    public void
    update(Graphics g) {
        paint(g);
    }

    public void
    trace(String msg) {

    }

    public void
    start() {
        trace("begin Albine.start()");
        wind = new Thread(w);
        wind.start();
        picture = new Thread(p);
        picture.start();
        picture.setPriority(picture.getPriority() + 1);

        for (Enumeration b = albine.elements(); b.hasMoreElements(); ) {
            Thread t = new Thread((Albina) b.nextElement());
            controls.addElement(t);
        }
        for (Enumeration c = controls.elements(); c.hasMoreElements(); ) {
            Thread t = (Thread) c.nextElement();
            t.start();
        }

        trace("end   Albine.start()");
    }

    public void
    stop() {
        trace("begin Albine.stop()");
        wind.stop();
        picture.stop();

        for (Enumeration e = controls.elements(); e.hasMoreElements(); ) {
            Thread t = (Thread) e.nextElement();
            t.stop();
        }
        controls.removeAllElements();

        trace("end   Albine.stop()");
    }


}                                                  