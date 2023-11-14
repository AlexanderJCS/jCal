package uihelper.layout;

import jangl.coords.WorldCoords;

public class Canvas {
    private final WorldCoords topLeft, widthHeight;

    public Canvas(WorldCoords topLeft, WorldCoords widthHeight) {
        this.topLeft = topLeft;
        this.widthHeight = widthHeight;
    }

    public WorldCoords topLeft() {
        // Copy the WorldCoords object since it is mutable
        return new WorldCoords(this.topLeft.x, this.topLeft.y);
    }

    public WorldCoords widthHeight() {
        // Copy the WorldCoords object since it is mutable
        return new WorldCoords(this.widthHeight.x, this.widthHeight.y);
    }

    public float width() {
        return this.widthHeight().x;
    }

    public float height() {
        return this.widthHeight().y;
    }
}
