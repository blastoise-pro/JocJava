package joc1;

import java.awt.*;
import java.util.Set;

interface Collider {
    Shape getCollider();

    void updateCollider();

    String getLabel();

    Set<String> getCollisionMask();

    boolean colliderIsActive();

    void onColliderEnter(Collider other);

    void onColliderStay(Collider other);

    void onColliderExit(Collider other);
}
