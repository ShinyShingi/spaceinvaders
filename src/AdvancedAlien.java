public class AdvancedAlien extends Alien {
    AdvancedAlien(int x, int y) {
        super(x, y);
    }
    int getMaxHP() {
        return level + 1;
    }
    int getSpeed() {
        return 6;
    }
    int getSpriteSize() {
        return 50;
    }
    String getSprite() {
        return "alien2.gif";
    }
    Direction getDirection(){
        return Direction.RIGHT;
    }

}
