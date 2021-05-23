import java.awt.*;

public class Boom extends Rectangle{
    static Image pic;
    static int x;
    static int y;
    public Boom(int x, int y)
    {
        this.x = x;
        this.y = y;
        pic = Toolkit.getDefaultToolkit().getImage(getClass().getResource("boom.gif"));
    }
}
