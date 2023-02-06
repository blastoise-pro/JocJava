package joc1;

interface MouseReciever {
    boolean containsCoords(Vec2 coords);
    void onMouseEnter();

    void onMouseExit();

    void onMouseClick();
}
