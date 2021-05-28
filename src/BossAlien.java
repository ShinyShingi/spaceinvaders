public class BossAlien extends Alien{
    BossAlien(int x, int y){
        super(x,y);
    }

    int getMaxHP(){return level + 5;}
    int getSpeed(){return 7;}
    int getSpriteSize(){return 100;}
    String getSprite(){return "alien3.gif";}
    Direction getDirection(){
        return Direction.LEFT;
    }
}
