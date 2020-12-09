import java.awt.*;

public class NumberField {
    public int num;
    private int sign;
    int x,y,width;
    enum Display {MINUS,NUMBER,NIL}
    private Display display;
    public boolean cool;

    /**
     * Constructs a field for manual point inclusion
     * @param x
     * @param y
     * @param num
     */
    public NumberField(int x, int y,int num)
    {
        this.x = x;
        width = 0;
        this.y = y;
        this.num = num;
        this.display = Display.NUMBER;
    }

    /**
     * Sets the width of the field
     * @param width new width of field
     * @return the current instance of NumberField
     */
    public NumberField setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * Determines if point is in the NumberField
     * @param xcoord x-coordinate of point
     * @param ycoord y-coordinate of point
     * @return true if point is in the field, false if not.
     */
    public boolean inBox(int xcoord, int ycoord)
    {
        if (ycoord < y|| ycoord> y+58) {
            return false;
        }
        if (xcoord < x ||xcoord > x + width) {
            return false;
        }
        return true;
    }

    /**
     * Draws the NumberField
     * @param g Graphics object in JPanel
     */
    public void paint(Graphics g)
    {
        switch (display)
        {
            case MINUS:
                g.drawImage(TSPPanel.images[2],x,y,null);
                break;
            case NUMBER:
                Graph.drawNumber(x,y,num,g);
                break;
        }
    }

    /**
     * Interprets a keystroke and updates the field accordingly when in the cold state
     * @param code KeyEvent character code
     */
    public void coldInput(int code)
    {
        if (code == 8 || code == 127)
        {
            num = 0;
            display = Display.NIL;
        }
        if(code > 48 && code <58)
        {
            num = code -48;
            display = Display.NUMBER;
        }
        if (code == 45)
        {
            num = 0;
            display = Display.MINUS;
        }
        cool = false;
    }
    /**
     * Interprets a keystroke and updates the field accordingly when not in the cold state
     * @param code KeyEvent character code
     */
    public void input(int code)
    {
        if (code == 8 || code == 127)
        {
            Display temp = (num >= 0)?Display.NIL:Display.MINUS;
            num /= 10;
            if (num == 0)
            {
                display = temp;
            }
        }
        if(code >= 48 && code <58)
        {
            if (num>9999||num<-999)
                return;
            if (num < 0 || display == Display.MINUS) {
                num = 10*num - (code -48);
            }
            else
            {
                num = 10*num + (code -48);
            }
            display = Display.NUMBER;

        }
        if (code == 45) {
            num *= -1;
            if (num == 0)
            {
                display = (display==Display.MINUS)?Display.NIL:Display.MINUS;
            }
        }
    }
}
