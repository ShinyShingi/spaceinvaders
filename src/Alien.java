import java.awt.*;

public abstract class Alien extends Rectangle {
    abstract int getMaxHP();

    abstract int getSpeed();

    abstract int getSpriteSize();

    abstract String getSprite();

    abstract Direction getDirection();

    static int NORMAL = 1;
    static int ADVANCED = 2;
    static int ENDBOSS = 3;
    static int level = 1;


    int type;
    Image pic;
    Direction direction = Direction.IDLE;
    int speed;
    int hp;
    int size;


    public Alien(int x, int y) {
        this.x = x;
        this.y = y;
        hp = getMaxHP();
        speed = getSpeed();
        size = getSpriteSize();
        pic = Toolkit.getDefaultToolkit().getImage(getClass().getResource(getSprite()));
        width = getSpriteSize();
        height = getSpriteSize();
        direction = getDirection();

    }

    public void draw(Graphics g, Component c) {
        g.drawImage(pic, this.x, this.y, c);

    }


    public void move() {

        if (direction == Direction.RIGHT) {
            x = x + speed;
            if (x > 1280) {
                direction = Direction.LEFT;
                y = y + 50;
            }
        }
        if (direction == Direction.LEFT) {
            x = x - speed;
            if (x < 30) {
                direction = Direction.RIGHT;
                y = y + 50;
            }
        }

    }

    public void playDeathAnimation() {
        pic = Toolkit.getDefaultToolkit().getImage(getClass().getResource("boom.gif"));
    }

    public void applyDamage() {
        hp = Math.max(hp - 1, 0);
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
