import javax.swing.*;
import java.util.Objects;
import java.util.Vector;

public class BossAlien extends Alien{
    BossAlien(int x, int y){
        super(x,y);
    }
    Timer bossShotTimer;
    int getMaxHP(){return level + 5;}
    int getSpeed(){return 4;}
    int getSpriteSize(){return 100;}
    String getSprite(){return "alien3.gif";}
    Direction getDirection(){
        return Direction.LEFT;
    }

    public void attack(Vector<BossShot> bossShots) {
        if (Objects.isNull(bossShotTimer))
        {
            bossShotTimer = new javax.swing.Timer(2000, BossAttack -> {
                bossShots.addElement(new BossShot(x,y));
            });
            bossShotTimer.start();
        }

    }

    public void die() {
        if (!Objects.isNull(bossShotTimer)){
            bossShotTimer.stop();

        }
    }
}
