/**
 *  This file is part of the TSP Tour Calculator
    Copyright (C) 2021  Reilly Browne

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
import java.awt.*;

/**
 * The purpose of this class is to render and maintain the associated data with a given button
 */
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
