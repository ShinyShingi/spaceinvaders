import java.util.Random;

public class NormalAlien extends Alien{
    NormalAlien(int x, int y){
        super(x,y);
    }

    int getMaxHP(){return level +1;}
    int getSpeed(){ Random rand = new Random();
        int speed = rand.nextInt((5+level)-1)+1;
        return speed;
    }
    int getSpriteSize(){return 30;}
    String getSprite(){return "alien1.gif";}
    Direction getDirection(){
        return Direction.DOWN;
    }
}
