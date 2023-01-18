package joc1;

import java.awt.*;

interface Collider {
    Shape getCollider();

    String getLabel();

    void onColliderEnter(Collider other);

    void onColliderExit(Collider other);
}
