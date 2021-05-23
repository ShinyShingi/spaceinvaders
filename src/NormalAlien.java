public class NormalAlien extends Alien{
    NormalAlien(int x, int y){
        super(x,y);
    }

    int getMaxHP(){return 1;}
    int getSpeed(){return 5;}
    int getSpriteSize(){return 30;}
    String getSprite(){return "alien1.gif";}
    Direction getDirection(){
        return Direction.RIGHT;
    }
}
