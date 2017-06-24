package model;

/**
 * @author Mohamed Tolba
 */
public class enums {

    public enum State {
        PENCIL_SKETCHING(1),
        BRUSH_SKETCHING(2),
        ERASE_SKETCHING(3),
        RECTANGLE_DRAW(4),
        SQUARE_DRAW(5),
        ELLIPSE_DRAW(6),
        CIRCLE_DRAW(7),
        LINE_DRAW(8),
        TRIANGLE_DRAW(9),
        DRAG_MODE(10),
        RESIZE_MODE(11),
        DELETE_MODE(12),
        FILL_MODE(13),
        UNDO_MODE(14),
        REDO_MODE(15);
        private int value;
        State(int value) {
            this.value = value;
        }
    }
}