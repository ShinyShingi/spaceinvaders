import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;
import java.awt.Graphics2D;

public class SpaceInvadersMain extends JPanel implements Runnable, ActionListener, KeyListener
{
    JFrame fenster;
    SpaceShip player;

    private Timer timer;
    private Thread gamethread;
    private Timer BossTimer;
    private Timer AdvancedTimer;

    private Random random = new Random();


    Vector<Alien> aliens = new Vector<Alien>();
    Vector<Shot> shots = new Vector<Shot>();
    Vector<Boom> booms = new Vector<Boom>();
    Vector<BossShot>bossShots = new Vector<BossShot>();
    Image background;
    Image boom;
    Image life;
    int score;
    boolean bossExists = false;
    private volatile boolean running = true;
    public static void main(String[] args)
    {
        new SpaceInvadersMain();
    }

    public SpaceInvadersMain()
    {
        fenster = new JFrame("SpaceInvaders");
        fenster.setSize(1800,1200);
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        fenster.add(this);
        fenster.setResizable(false);
        fenster.setVisible(true);
        fenster.addKeyListener(this);
        fenster.requestFocusInWindow();
        // Raumschiff erzeugen
        player = new SpaceShip(500,500);
        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("back.png"));
        life       = Toolkit.getDefaultToolkit().getImage(getClass().getResource("lives.gif"));
        // Timer erzeugen
        //timer = new Timer(2000, this);
        timer = new Timer(random.nextInt(500) + 300, this);
        //timer.setRepeats(false);
        timer.start();
        BossTimer = new Timer(50000, new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent Boss)
            {
                if (player.lives> 0 && running)
                    aliens.addElement(new BossAlien(random.nextInt(1200)+300, 20));

            }
        });
        BossTimer.start();
        AdvancedTimer = new Timer(5000, new ActionListener(){
            @Override
            public void actionPerformed (ActionEvent adv)
            {
                if (player.lives> 0 && running)
                    aliens.addElement(new AdvancedAlien(random.nextInt(1300)+300, 20));

            }
        });
        AdvancedTimer.start();
        score = 0;

        // Thread starten
        gamethread = new Thread(this);
        gamethread.start();

    }


    @Override
    public void run()
    {

        long cd = (long)(1.0/60.0*1000);
        while(true) {
            if (!running) {
                continue;
            }

            long start = System.currentTimeMillis();
            player.move();


            repaint();

            Enumeration<Alien> a = aliens.elements();
            while (a.hasMoreElements()) {
                Alien alien = a.nextElement();
                alien.move();
                if (alien instanceof BossAlien && !bossExists) {
                    bossExists = true;

                    Timer BossShotTimer;
                    BossShotTimer = new Timer(2000, new ActionListener(){

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            System.out.println("blah");

                            bossShots.add(new BossShot(alien.x+50, alien.y+100));
                        }
                    });
                    BossShotTimer.start();
                }
                if (alien.y > this.getHeight())
                    aliens.remove(alien);

                if (player.intersects(alien)) {
                    player.lives--;
                    player.x = 600;
                    player.y = 600;
                    aliens.clear();
                    try {
                        String invadersound = "G:\\JAVA\\spaceinvaders\\pictures\\nooooo.wav";
                        Clip invaderSoundClip = AudioSystem.getClip();
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(invadersound).getAbsoluteFile());
                        invaderSoundClip.open(audioInputStream);
                        invaderSoundClip.start();
                    } catch (UnsupportedAudioFileException s) {
                        s.printStackTrace();
                    } catch (LineUnavailableException s) {
                        s.printStackTrace();
                    } catch (IOException s) {
                        s.printStackTrace();
                    }

                }


                Enumeration<Shot> es = shots.elements();
                while (es.hasMoreElements()) {
                    Shot xd = es.nextElement();
                    if (alien.intersects(xd) && alien.isAlive()) {
                        alien.applyDamage();
                        shots.remove(xd);
                       System.out.println(alien.hp);

                        if (!alien.isAlive()) {

                            alien.playDeathAnimation();
                            Timer boomTimer;
                            boomTimer = new Timer(300, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    aliens.remove(alien);
                                    try {
                                        String invadersound = "G:\\JAVA\\spaceinvaders\\pictures\\boom.wav";
                                        Clip invaderSoundClip = AudioSystem.getClip();
                                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(invadersound).getAbsoluteFile());
                                        invaderSoundClip.open(audioInputStream);
                                        invaderSoundClip.start();
                                    } catch (UnsupportedAudioFileException s) {
                                        s.printStackTrace();
                                    } catch (LineUnavailableException s) {
                                        s.printStackTrace();
                                    } catch (IOException s) {
                                        s.printStackTrace();
                                    }

                                }
                                });
                            alien.direction = Direction.IDLE;
                            boomTimer.setRepeats(false);
                            boomTimer.start();


                            score++;
                            if(score%100 == 0) {
                                player.lives++;
                            }
                            if (score%20 == 0) {
                                Alien.level++;
                            }

                        }

                    }
                }
            }

            Enumeration<BossShot> bs = bossShots.elements();
            while (bs.hasMoreElements()) {
                BossShot xdd = bs.nextElement();
                xdd.y = xdd.y + xdd.speed;
                if (xdd.y + xdd.height > this.getHeight())
                    bossShots.remove(xdd);
            }

            Enumeration<Shot> es = shots.elements();
            while (es.hasMoreElements()) {
                Shot xd = es.nextElement();
                xd.y = xd.y - xd.speed;
                if (xd.y + xd.height < 0)
                    shots.remove(xd);
            }


            long end = System.currentTimeMillis();
            long diff = start - end;

            long timeToWait = cd - diff;
            if (timeToWait > 0) {

                try {

                    Thread.sleep(timeToWait);

                } catch (InterruptedException e) {
                }
            }
            else {
                System.out.println("Time skew detected;");
            }

        }



    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        g.drawImage(background, 0, 0, this);
        if(player != null)
            player.draw(g,this);

        Enumeration<Alien> e = aliens.elements();
        while (e.hasMoreElements())
        {
           e.nextElement().draw(g, this);
        }
        Enumeration<BossShot> bossshot = bossShots.elements();
        while (bossshot.hasMoreElements()) {
            BossShot bossShot = bossshot.nextElement();
            bossShot.draw(g, this);
        }

        Enumeration<Shot> itshot = shots.elements();
        while (itshot.hasMoreElements()) {
           Shot shot = itshot.nextElement();
           shot.draw(g, this);
        }

        String scoreString = Integer.toString(score);
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(Font.BOLD, 30));
        g.drawString(scoreString, 40, 60);


        g.drawImage(life, 10, 100, 30, 30, null);
        String livesString = Integer.toString(player.lives);
        g.setColor(Color.WHITE);
        g.drawString(livesString, 40, 125);
        g.drawString("LEVEL: " + Alien.level, 40, 200);

        if (player.lives <= 0){

            g2d.setFont(new Font("Helvetica", Font.BOLD, 80));

            FontMetrics fmgo = g2d.getFontMetrics();
            String over = "GAME OVER";
            g2d.setColor(Color.RED);
            int m = (getWidth() - fmgo.stringWidth(over)) /2;
            g2d.drawString(over, m, 400);

            g2d.setFont(new Font("Helvetica", Font.BOLD, 60));

            FontMetrics fms = g2d.getFontMetrics();
            String score = "Score: " + scoreString;
            int a = (this.getWidth() - fms.stringWidth(score)) /2;
            g2d.drawString(score, a, 700);


            FontMetrics fml = g2d.getFontMetrics();
            String level = "Level: " + Alien.level;
            int b = (getWidth() - fml.stringWidth(level)) /2;
            g2d.drawString(level, b, 550);



            FontMetrics fmsp = g2d.getFontMetrics();
            String space = "Press SPACE, to start again";
            g2d.setFont(new Font("Helvetica", Font.BOLD, 60));
            g2d.setColor(Color.WHITE);
            int c = (getWidth() - fmsp.stringWidth(space)) /2;
            g2d.drawString(space, c, 800);

        }
        if (!running) {
            g2d.setFont(g.getFont().deriveFont(Font.BOLD, 260));
            FontMetrics fmp = g2d.getFontMetrics();
            String pause = "PAUSE";
            int d = (getWidth() - fmp.stringWidth(pause)) /2;
            g2d.setColor(Color.WHITE);
            g2d.drawString(pause, 230, 500);

        }



    }

      public void actionPerformed(ActionEvent e)
    {
        if (player.lives> 0 && running) {
            aliens.addElement(new NormalAlien(random.nextInt(1200)+300, 20));
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP)
            player.direction = SpaceShip.up;
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            player.direction = SpaceShip.down;
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            player.direction = SpaceShip.left;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            player.direction = SpaceShip.right;
        if (player.lives== 0 && e.getKeyCode()== KeyEvent.VK_SPACE){
            player.lives = 3;
            aliens.clear();
            score = 0;
            aliens.addElement(new NormalAlien(20, 20));
            return;
        }
        if (!running && player.lives> 0 && e.getKeyCode() == KeyEvent.VK_ESCAPE){

            running = true;
        }
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            running = false;
        }
        //System.out.println(running);


    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.direction = 0;
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            shots.add(new Shot(player.x, player.y));
            try {
                String shootsound = "G:\\JAVA\\spaceinvaders\\pictures\\shoot.wav";
                Clip shootSoundClip = AudioSystem.getClip();
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(shootsound).getAbsoluteFile());
                shootSoundClip.open(audioInputStream);
                shootSoundClip.start();
            } catch (UnsupportedAudioFileException s) {
                s.printStackTrace();
            } catch (LineUnavailableException s) {
                s.printStackTrace();
            } catch (IOException s) {
                s.printStackTrace();
            }
        }

    }

    private void moveAlien(Graphics g)
    {
        Enumeration<Alien> e = aliens.elements();
        while (e.hasMoreElements())
        {
            Alien x = e.nextElement();
            x.draw(g, this);
        }
    }


}
/*import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.Vector;

public class SpaceInvadersMain extends JPanel implements Runnable, ActionListener , KeyListener
{
    JFrame fenster;
    SpaceShip player;

    private Timer timer;
    private Thread gamethread;
    Vector<Alien> aliens = new Vector<Alien>();
    Vector<Shot> shots = new Vector<Shot>();
    Image background;
    Image boom;
    public static void main(String[] args)
    {
        new SpaceInvadersMain();
    }

    public SpaceInvadersMain()
    {
        fenster = new JFrame("SpaceInvaders");
        fenster.setSize(1400,900);
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLACK);
        fenster.add(this);
        fenster.setResizable(false);
        fenster.setVisible(true);
        fenster.addKeyListener(this);
        fenster.requestFocusInWindow();
        // Raumschiff erzeugen
        player = new SpaceShip(500,500);

        //alien = new Alien(210,20, Alien.NORMAL);
        //Alien x = new Alien(210,20, Alien.NORMAL);
        background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("back.jpg"));
        boom       = Toolkit.getDefaultToolkit().getImage(getClass().getResource("boom.gif"));
        // Timer erzeugen
        timer = new Timer(2000, this);
        timer.start();
        // Thread starten
        gamethread = new Thread(this);
        gamethread.start();
    }
    @Override
    public void run()
    {
        long cd = (long)(1.0/60.0*1000);
        while(true) {
            long start = System.currentTimeMillis();
            player.move();
            //alien.move();


            repaint();
            Enumeration<Alien> a = aliens.elements();
            while (a.hasMoreElements()) {
                Alien alien = a.nextElement();
                alien.move();
                if (alien.y > 880)
                    aliens.remove(alien);

                if (player.intersects(alien)) {
                    // lives--;
                    player.x = 600;
                    player.y = 600;
                    aliens.clear();
                }
                Enumeration<Shot> es = shots.elements();
                while (es.hasMoreElements()) {
                    Shot xd = es.nextElement();
                    if (alien.intersects(xd)) {

                        aliens.remove(alien);
                    }
                }
            }
            Enumeration<Shot> es = shots.elements();
            while (es.hasMoreElements()) {
                Shot xd = es.nextElement();
                xd.y = xd.y - xd.speed;
                if (xd.y + xd.height < 0)
                    shots.remove(xd);
            }


            long end = System.currentTimeMillis();
            long diff = start - end;

            long timeToWait = cd - diff;
            if (timeToWait > 0) {

                try {

                    Thread.sleep(timeToWait);

                } catch (InterruptedException e) {
                }
            }
            else {
                System.out.println("Time skew detected;");
            }
        }



    }



    protected void paintComponent(Graphics g)
    {

        super.paintComponent(g);
        g.drawImage(background, 0, 0, this);

        if(player != null)
            player.draw(g,this);

        Enumeration<Alien> e = aliens.elements();
        while (e.hasMoreElements())
        {
           e.nextElement().draw(g, this);
        }
        Enumeration<Shot> itshot = shots.elements();
        while (itshot.hasMoreElements()) {
           Shot shot = itshot.nextElement();
           shot.draw(g, this);
        }


    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        //System.out.println("Neu");
        aliens.addElement( new Alien(20,20, Alien.NORMAL));
        aliens.addElement(new Alien(500, 20, Alien.ADVANCED));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP)
            player.direction = SpaceShip.up;
        if(e.getKeyCode() == KeyEvent.VK_DOWN)
            player.direction = SpaceShip.down;
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
            player.direction = SpaceShip.left;
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
            player.direction = SpaceShip.right;

    }

    @Override
    public void keyReleased(KeyEvent e) {
        player.direction = 0;
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            shots.add(new Shot(player.x, player.y));
        }

    }

    private void moveAlien(Graphics g)
    {
        Enumeration<Alien> e = aliens.elements();
        while (e.hasMoreElements())
        {
            Alien x = e.nextElement();
            x.draw(g, this);
        }
    }
}
*/