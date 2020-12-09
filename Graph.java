import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;

public class Graph {
    private int topX,topY,numX,numY;
    private int cursorX,cursorY;
    private int placementX,placementY;
    private int height;
    private int width;
    public HashSet<int[]> points;
    public int[][] edges;

    /**
     * Constructs the display on the grid on the top part of the screen.
     */
    public Graph()
    {
        placementX = placementY = 60;
        cursorX = cursorY = topX = topY = numX = numY = 0;
        points = new HashSet<int[]>();
        edges = null;
    }

    /**
     * Returns the x coordinate of the cursor
     * @return
     */
    public int getCursorX() {
        return cursorX;
    }
    /**
     * Returns the y coordinate of the cursor
     * @return
     */
    public int getCursorY() {
        return cursorY;
    }

    /**
     * Adds a new point for consideration
     */
    public void append()
    {
        if (points.size() < 20)
            points.add(new int[]{cursorX, cursorY});
    }

    /**
     * Deletes a point from consideration
     */
    public void delete()
    {
        for (int[] point : points)
        {
            if (point[0] == cursorX && point[1] == cursorY) {
                points.remove(point);
                break;
            }
        }
    }

    /**
     * Finds the TSP tour of the points in the grid
     */
    public void getTSP()
    {
        int[][] pairs = new int[points.size()][2];
        int i = 0;
        for (int[] pair : points)
        {
            pairs[i] = pair;
            i++;
        }
        TSP.TSPNode node = TSP.branchAndBoundOfVertices(pairs);
        ArrayList<int[]> edgelinks = node.edges;
        edges = new int[edgelinks.size()][4];
        for (int j = 0; j < edges.length; j++)
        {
            int[] edgepair = edgelinks.get(j);
            edges[j] = new int[]{pairs[edgepair[0]][0],pairs[edgepair[0]][1],pairs[edgepair[1]][0],pairs[edgepair[1]][1]};
        }
    }

    /**
     * Changes the size of the grid
     * @param width new width of grid
     * @param height new height of grid
     */
    public void updateSize(int width,int height)
    {
        topX = -(width-60)/120;
        topY = (height-60)/120;
        numX = (width)/60;
        numY = (height)/60;
        this.height = height;
        this.width = width;
    }

    /**
     * Updates the top corner of the grid
     * @param initialX new top corner X
     * @param initialY new top corner Y
     * @param finalX new final corner X
     * @param finalY new final corner Y
     */
    public void updateCorner(int initialX, int initialY, int finalX, int finalY)
    {
        placementX += finalX-initialX;
        while (placementX > 60)
        {
            topX--;
            placementX -= 60;
        }
        while (placementX < 0)
        {
            topX++;
            placementX += 60;
        }
        placementY -= -finalY+initialY;
        while (placementY > 60)
        {
            topY++;
            placementY -= 60;
        }
        while (placementY < 0)
        {
            topY--;
            placementY += 60;
        }
    }

    /**
     * Updates cursor position on screen
     * @param x x of the cursor
     * @param y y of the cursor
     */
    public void updateCursor(int x, int y)
    {
        cursorX = topX+(x-placementX/2)/60;
        cursorY = topY-(y-placementY/2)/60;
    }

    /**
     * Paints the grid
     * @param g Graphics object of the JPanel
     */
    public void paint(Graphics g)
    {
        g.setColor(new Color(215,123,186));
        g.fillRect(0,60*topY+placementY-3, width, 6);
        g.fillRect(-60*topX+placementX-3,0,6,height);
        g.setColor(new Color(99,155,255));
        for(int i = 0; i < numX; i++)
        {
            if (topX+i == 0)
            {
                continue;
            }
            g.fillRect(60*i+placementX-2,0,4,height);
        }
        for(int i = 0; i < numY; i++)
        {
            if (topY-i == 0)
            {
                continue;
            }
            g.fillRect(0,60*i+placementY-2,width, 4);
        }
        if (edges != null)
        {
            g.setColor(new Color(223,113,38));
            for (int[] edge : edges)
            {
                g.drawLine(placementX+(edge[0]-topX)*60,placementY+(topY-edge[1])*60,placementX+(edge[2]-topX)*60,placementY+(topY-edge[3])*60);
                if (edge[0]==edge[2] || (edge[3]-edge[1])/(edge[2]-edge[0])!=0)
                {
                    g.drawLine(placementX+(edge[0]-topX)*60+1,placementY+(topY-edge[1])*60,placementX+(edge[2]-topX)*60+1,placementY+(topY-edge[3])*60);
                    g.drawLine(placementX+(edge[0]-topX)*60+2,placementY+(topY-edge[1])*60,placementX+(edge[2]-topX)*60+2,placementY+(topY-edge[3])*60);
                    g.drawLine(placementX+(edge[0]-topX)*60+3,placementY+(topY-edge[1])*60,placementX+(edge[2]-topX)*60+3,placementY+(topY-edge[3])*60);
                    g.drawLine(placementX+(edge[0]-topX)*60-3,placementY+(topY-edge[1])*60,placementX+(edge[2]-topX)*60-3,placementY+(topY-edge[3])*60);
                    g.drawLine(placementX+(edge[0]-topX)*60-2,placementY+(topY-edge[1])*60,placementX+(edge[2]-topX)*60-2,placementY+(topY-edge[3])*60);
                    g.drawLine(placementX+(edge[0]-topX)*60-1,placementY+(topY-edge[1])*60,placementX+(edge[2]-topX)*60-1,placementY+(topY-edge[3])*60);
                }
                else
                {
                    g.drawLine(placementX+(edge[0]-topX)*60,placementY+(topY-edge[1])*60+1,placementX+(edge[2]-topX)*60,placementY+(topY-edge[3])*60+1);
                    g.drawLine(placementX+(edge[0]-topX)*60,placementY+(topY-edge[1])*60+2,placementX+(edge[2]-topX)*60,placementY+(topY-edge[3])*60+2);
                    g.drawLine(placementX+(edge[0]-topX)*60,placementY+(topY-edge[1])*60+3,placementX+(edge[2]-topX)*60,placementY+(topY-edge[3])*60+3);
                    g.drawLine(placementX+(edge[0]-topX)*60,placementY+(topY-edge[1])*60-3,placementX+(edge[2]-topX)*60,placementY+(topY-edge[3])*60-3);
                    g.drawLine(placementX+(edge[0]-topX)*60,placementY+(topY-edge[1])*60-2,placementX+(edge[2]-topX)*60,placementY+(topY-edge[3])*60-2);
                    g.drawLine(placementX+(edge[0]-topX)*60,placementY+(topY-edge[1])*60-1,placementX+(edge[2]-topX)*60,placementY+(topY-edge[3])*60-1);
                }
            }
        }
        for (int[] point : points)
        {
            if (point[0] >= topX || point[0] <= topX+numX || point[1] <= topY || point[1] >= topY-numY)
            {
                g.drawImage(TSPPanel.images[29], placementX+(point[0]-topX)*60-16,placementY+(topY-point[1])*60-16,null);
            }
        }
        BufferedImage cursorImage = TSPPanel.images[23+TSPPanel.currentTime/2];
        g.drawImage(cursorImage,placementX+(cursorX-topX)*60-16,placementY+(topY-cursorY)*60-16,null);
    }

    /**
     * Draws numerical coordinates
     * @param g Graphics object of JPanel
     * @param x whether to show x
     * @param y whether to show y
     */
    public void paint2(Graphics g, boolean x, boolean y)
    {
        if (x){drawNumber(45, height*9/8-10, cursorX,g);}
        if(y){drawNumber(width/5+35,height*9/8-10,cursorY,g);}
    }

    /**
     * Drawing a given number using the number images
     * @param x top-left corner x coordinate
     * @param y top-right corner y coordinate
     * @param num the number being written
     * @param g Graphics object of JPanel
     */
    public static void drawNumber(int x, int y, int num, Graphics g)
    {
        int i = 0;
        if (num < 0)
        {
            i++;
            g.drawImage(TSPPanel.images[2], x, y, null);
        }
        String str = num +"";
        for (; i < 5&&i<str.length(); i++)
        {
            char ch = str.charAt(i);
            g.drawImage(TSPPanel.images[3+ch-'0'],x+22*i,y,null );
        }
    }

    public void setCursorX(int cursorX) {
        this.cursorX = cursorX;
    }

    public void setCursorY(int cursorY) {
        this.cursorY = cursorY;
    }
}
