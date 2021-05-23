import java.awt.*;

public class SpaceShip extends Rectangle
{
    int speed;
    int direction;
    Image pic;
    int lives;
    static int up = 1;
    static int down = 2;
    static int right = 3;
    static int left = 4;

    public SpaceShip (int x, int y)
    {
        this.x          = x;
        this.y          = y;
        this.width      = 50;
        this.height     = 50;
        speed = 8;
        lives = 3;
        pic = Toolkit.getDefaultToolkit().getImage(getClass().getResource("shipanimated.gif"));
    }
    public void move()
    {
        if(direction == up)
        {
            y = y - speed;
            if(y < 0)
                y=0;
        }
        if(direction == down)
        {
            y = y + speed;
            if(y > 820)
                y = 820;
        }
        if(direction == right)
        {
            x = x + speed;
            if(x >  1350)
                x = 1350;
        }
        if(direction == left)
        {
            x = x - speed;
            if(x < 0)
                x = 0;
        }

    }
    public  void draw (Graphics g, Component c)
    {
        g.drawImage(pic, this.x, this.y, c);
        return;
    }

}
