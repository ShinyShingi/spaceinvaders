import java.awt.*;

class BossShot  extends Rectangle{


    Image bossShot;

    int direction = 0;
    int speed;
    public BossShot(int x, int y)
    {
        this.x = x;
        this.y = y;
        width = 30;
        height = 40;

        speed = 3;
        bossShot = Toolkit.getDefaultToolkit().getImage(getClass().getResource("bossweapon.gif"));


    }
    public void draw(Graphics g, Component c)
    {
        g.drawImage(bossShot, this.x , this.y, c);

    }

}

