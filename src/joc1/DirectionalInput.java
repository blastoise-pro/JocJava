package joc1;

enum Directions {
    UP    ((byte)1),
    DOWN  ((byte)2),
    LEFT  ((byte)4),
    RIGHT ((byte)8);

    private final byte directionBit;

    Directions(byte directionBit) {
        this.directionBit = directionBit;
    }

    byte getBit()
    {
        return directionBit;
    }
}

public class DirectionalInput {
    private byte directionBits;

    public DirectionalInput() {
        directionBits = 0;
    }

    public void addInput(Directions dir){
        directionBits |= dir.getBit();
    }

    public void removeInput(Directions dir) {
        directionBits &= ~dir.getBit();
    }

    public boolean isPushed(Directions dir) {
        return (directionBits & dir.getBit()) != 0;
    }
}
