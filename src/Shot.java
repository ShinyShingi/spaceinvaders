import java.awt.*;

public class Shot extends Rectangle
{
    Image shot;
    int direction = 0;
    int speed;
    public Shot(int x, int y)
    {
        this.x = x;
        this.y = y;
        width = 20;
        height = 40;

        speed = 5;
        shot = Toolkit.getDefaultToolkit().getImage(getClass().getResource("shot.gif"));

    }
    public void draw(Graphics g, Component c)
    {
        g.drawImage(shot, this.x , this.y, c);

    }
}
