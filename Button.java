import java.awt.*;

public class Button {
    public enum ButtonType{ADD, CLEAR, TOUR}
    public enum ButtonState{PRESSED, HOVER, UNPRESSED}
    private ButtonType type;
    private ButtonState state;
    private int x,y;
    private int imageIndex;

    /**
     * Constructs a new Button
     * @param type type of the button
     * @param x the x-coordinate of the top-left corner
     * @param y the y-coordinate of the top-left corner
     */
    public Button(ButtonType type, int x, int y)
    {
        this.type = type;
        this.x = x;
        this.y = y;
        this.state = ButtonState.UNPRESSED;
        switch (type)
        {
            case ADD:
                imageIndex = 16;
                break;
            case TOUR:
                imageIndex = 13;
                break;
            case CLEAR:
                imageIndex = 19;
                break;
        }
    }

    public ButtonType getType() {
        return type;
    }

    public void setCoords(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Paints the Button
     * @param g Graphics object of JPanel
     */
    public void paint(Graphics g)
    {
        g.drawImage(TSPPanel.images[imageIndex+state.ordinal()],x,y,null);
    }
    public void setState(ButtonState state) {
        this.state = state;
    }

    /**
     * Determines whether the given point is in the button
     * @param xCoord x-coordinate of the point
     * @param yCoord y-coordinate of the point
     * @return true if point is in the button, false elsewise
     */
    public boolean inButton(int xCoord, int yCoord)
    {
        if (xCoord<x||xCoord>x+192||yCoord<y||yCoord>y+96)
        {
            return false;
        }
        return true;
    }
}
