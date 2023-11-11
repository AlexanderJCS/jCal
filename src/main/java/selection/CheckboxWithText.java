package selection;

import jangl.color.Color;
import jangl.coords.WorldCoords;
import jangl.graphics.font.Text;
import jangl.io.mouse.MouseEvent;
import uihelper.Fonts;

import java.util.List;

public class CheckboxWithText implements AutoCloseable {
    private final Checkbox checkbox;
    private final Text text;

    public CheckboxWithText(WorldCoords location, String text, Color calendarColor) {
        location = new WorldCoords(location.x, location.y);

        this.checkbox = new Checkbox(location, calendarColor);

        location.x += this.checkbox.getDimensions().x * 1.2f;
        this.text = new Text(location, Fonts.ARIAL_BLACK, this.checkbox.getDimensions().y, text);
    }

    public boolean isSelected() {
        return this.checkbox.isSelected();
    }

    public WorldCoords getDimensions() {
        return this.checkbox.getDimensions();
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getText() {
        return this.text.getText();
    }

    public void toggle() {
        this.checkbox.toggle();
    }

    public void setState(boolean state) {
        this.checkbox.setState(state);
    }

    public void draw() {
        this.checkbox.draw();
        this.text.draw();
    }

    public void update(List<MouseEvent> mouseEvents) {
        this.checkbox.update(mouseEvents);
    }

    @Override
    public void close() {
        this.checkbox.close();
        this.text.close();
    }
}
