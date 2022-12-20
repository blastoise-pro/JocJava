package joc1;

public class DirectionalInput {
    byte directionBits;

    enum Directions {
        UP    (1),
        DOWN  (2),
        LEFT  (4),
        RIGHT (8);

        private final byte directionBit;


    }

    public DirectionalInput() {

    }
}
