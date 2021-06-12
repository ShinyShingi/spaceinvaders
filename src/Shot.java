import java.awt.*;

public class Shot extends Rectangle
{
    Image shot;
    Image shot2;
    int direction = 0;
    int speed;
    public Shot(int x, int y)
    {
        this.x = x;
        this.y = y;
        width = 30;
        height = 40;

        speed = 5;
        shot = Toolkit.getDefaultToolkit().getImage(getClass().getResource("shot.gif"));
        shot2= Toolkit.getDefaultToolkit().getImage(getClass().getResource("secondweapon.gif"));

    }
    public void draw(Graphics g, Component c)
    {
        g.drawImage(shot, this.x , this.y, c);

    }
    public void secondShot(Graphics g, Component c)
    {
        g.drawImage(shot2, this.x , this.y, c);

    }
}
