package joc1;

enum Direction {
    UP    ((byte)1, new Vec2(0, 1)),
    DOWN  ((byte)2, new Vec2(0, -1)),
    LEFT  ((byte)4, new Vec2(-1, 0)),
    RIGHT ((byte)8, new Vec2(1, 0));

    private final byte directionBit;
    private final Vec2 vector;

    Direction(byte directionBit, Vec2 vector) {
        this.directionBit = directionBit;
        this.vector = vector;
    }

    byte directionBit()
    {
        return directionBit;
    }

    Vec2 vector(){
        return vector;
    }
}

public class DirectionalInput {
    byte directionBits;

    public DirectionalInput() {
        directionBits = 0;
    }

    public void addInput(Direction dir){
        directionBits |= dir.directionBit();
    }

    public void removeInput(Direction dir) {
        directionBits &= ~dir.directionBit();
    }

    public boolean isPressed(Direction dir) {
        return (directionBits & dir.directionBit()) != 0;
    }

    public Vec2 getDirection() {
        Vec2 res = new Vec2();
        for (Direction dir:Direction.values()){
            if (isPressed(dir))
                res.Add(dir.vector());
        }
        return res.normalized();
    }
}
