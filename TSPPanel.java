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
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
/**
 * The purpose of this class is to render the GUI
 */
public class TSPPanel extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
    public static String[] imagePaths = {"/X.png","/Y.png","/Minus.png","/Number0.png","/Number1.png","/Number2.png","/Number3.png","/Number4.png","/Number5.png","/Number6.png","/Number7.png","/Number8.png","/Number9.png","/TourPressed.png","/TourHover.png","/Tour.png","/AddPressed.png","/AddHover.png","/Add.png","/ClearPressed.png","/ClearHover.png","/Clear.png","/PointExcluded.png","/PointExcludedSelected5.png","/PointExcludedSelected4.png","/PointExcludedSelected3.png","/PointExcludedSelected2.png","/PointExcludedSelected1.png","/PointIncludedSelected5.png","/PointIncludedSelected4.png","/PointIncludedSelected3.png","/PointIncludedSelected2.png","/PointIncludedSelected1.png","/PointIncluded.png"};
    public static BufferedImage[] images = new BufferedImage[imagePaths.length];
    public static int currentTime = 0;
    public Button[] buttons;
    public int width;
    public int height;
    public Graph graph;
    public int initialX;
    public int initialY;
    public boolean moving,typingX,typingY;
    private NumberField fieldX, fieldY;

    /**
     * Constructs a JPanel for displaying the program
     */
    public TSPPanel()
    {
        typingY = typingX = moving = false;
        buttons = new Button[]{new Button(Button.ButtonType.ADD, 0,0),new Button(Button.ButtonType.CLEAR,0,0),new Button(Button.ButtonType.TOUR,0,0)};
        setBackground(Color.white);
        Timer timer = new Timer(1000/20,this);
        timer.start();
        addMouseListener(this);
        addMouseMotionListener(this);
        setFocusable(true);
        addKeyListener(this);
        graph = new Graph();
        for (int i =0; i < imagePaths.length; i++)//loads all the images for use (this is what made it super fast)
        {
            BufferedImage image = null;
            try {
                URL url = getClass().getResource(imagePaths[i]);
                image = ImageIO.read(url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            images[i] = image;
        }
        width = getWidth();
        height = getHeight();
        fieldX = fieldY = new NumberField(0,0,0);
    }

    /**
     * Paints the JPanel
     * @param g Graphics object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        graph.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,this.getHeight()*4/5-5,this.getWidth(),10);
        g.fillRect(0,0,this.getWidth(),1);
        g.setColor(Color.GRAY);
        g.fillRect(0,this.getHeight()*4/5,this.getWidth(),this.getHeight()*4/5+2);
        for (Button button :buttons)
        {
            button.paint(g);
        }
        g.drawImage(images[0],10,this.getHeight()*9/10-10,null);
        g.drawImage(images[1],this.getWidth()/5,this.getHeight()*9/10-10,null);
        g.setColor(Color.BLACK);
        g.fillRect(40, this.getHeight()*9/10-25, this.getWidth()/5-45, 64);
        g.fillRect(this.getWidth()/5+30, this.getHeight()*9/10-25, this.getWidth()/5-45, 64);
        g.setColor(Color.white);
        if (typingX&&fieldX.cool)
        {
            g.setColor(new Color(99,155,255));
        }
        g.fillRect(43, this.getHeight()*9/10-22, this.getWidth()/5-51, 58);
        g.setColor(Color.white);
        if (typingY&&fieldY.cool)
        {
            g.setColor(new Color(99,155,255));
        }
        g.fillRect(this.getWidth()/5+33, this.getHeight()*9/10-22, this.getWidth()/5-51, 58);
        graph.paint2(g,!typingX,!typingY);
        if (typingX) { fieldX.paint(g);}
        if (typingY) {fieldY.paint(g);}
    }

    /**
     * Updating the size of the JPanel, repainting, and increasing the time
     * @param e the event of time passing
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (width!= this.getWidth()||height!=this.getHeight()) {
            buttons[0].setCoords(this.getWidth() * 2 / 5, this.getHeight() * 5 / 6 - 5);
            buttons[1].setCoords(this.getWidth() * 3 / 5, this.getHeight() * 5 / 6 - 5);
            buttons[2].setCoords(this.getWidth() * 4 / 5, this.getHeight() * 5 / 6 - 5);
            graph.updateSize(this.getWidth(),this.getHeight()*4/5);
            width = this.getWidth();
            height = this.getHeight();
            fieldX = new NumberField(45, height*9/10-10,graph.getCursorX()).setWidth(this.width/5-51);
            fieldY = new NumberField(width/5+35,height*9/10-10,graph.getCursorY()).setWidth(this.width/5-51);
        }
        repaint();
        currentTime = ++currentTime%10;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Handles keys being pressed
     * @param e KeyEvent holding information of the key pressed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_DELETE&&!typingY&&!typingX)
        {
            graph.delete();
        }
        if (typingX)
        {
            if (!fieldX.cool) {
                fieldX.input(e.getKeyCode());
            } else {
                fieldX.coldInput(e.getKeyCode());
            }
        }
        if (typingY)
        {
            if (!fieldY.cool) {
                fieldY.input(e.getKeyCode());
            } else {
                fieldY.coldInput(e.getKeyCode());
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Handles what to do when a mouse button is pressed
     * @param e MouseEvent with info on the mouse button being pressed and location of cursor
     */
    @Override
    public void mousePressed(MouseEvent e) {
        if (typingX)
        {
            fieldX.cool = false;
            graph.setCursorX(fieldX.num);
        }
        if (typingY)
        {
            fieldY.cool = false;
            graph.setCursorY(fieldY.num);
        }
        if (fieldX.inBox(e.getX(),e.getY()))
        {
            typingY = false;
            typingX = true;
            fieldX.cool=true;
            return;
        }
        typingX = false;
        if (fieldY.inBox(e.getX(),e.getY()))
        {
            typingY = true;
            fieldY.cool =true;
            return;
        }
        typingY = false;
        Button interest = null;
        for (Button button: buttons)
        {
            if (button.inButton(e.getX(),e.getY()))
            {
                button.setState(Button.ButtonState.PRESSED);
                interest = button;
            }
            else
            {
                button.setState(Button.ButtonState.UNPRESSED);
            }
        }
        if (e.getY()<this.getHeight()*4/5&&e.getButton()==MouseEvent.BUTTON1) {
            graph.append();
        }
        if (e.getY()<this.getHeight()*4/5&&e.getButton()==MouseEvent.BUTTON3)
            graph.delete();
        if (e.getY()<this.getHeight()*4/5&&e.getButton()==MouseEvent.BUTTON2)
        {
            initialX = e.getX();
            initialY = e.getY();
            moving = true;
        }
        if (interest != null && interest.getType()== Button.ButtonType.CLEAR)
        {
            graph.points.clear();
            graph.edges = null;
        }
        if (interest != null && interest.getType()== Button.ButtonType.TOUR)
        {
            graph.getTSP();
        }
        if (interest != null && interest.getType()== Button.ButtonType.ADD)
        {
            graph.append();
        }

    }

    /**
     * Handles what to do when a mouse button is released
     * @param e MouseEvent with info on the mouse button being released and location of cursor
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON2)
            moving = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    /**
     * Handles what to do when a mouse is dragged
     * @param e MouseEvent with info on the mouse button being pressed and location of cursor
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        if (moving)
        {
            graph.updateCorner(initialX,initialY,e.getX(),e.getY());
            initialX = e.getX();
            initialY = e.getY();
        }
    }
    /**
     * Handles what to do when a mouse is moved
     * @param e MouseEvent with info on the location of cursor
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        for (Button button: buttons)
        {
            if (button.inButton(e.getX(),e.getY()))
            {
                button.setState(Button.ButtonState.HOVER);
            }
            else
            {
                button.setState(Button.ButtonState.UNPRESSED);
            }
        }
        if (e.getY()< this.getHeight()*4/5)
            graph.updateCursor(e.getX(),e.getY());
        if (moving)
        {
            graph.updateCorner(initialX,initialY,e.getX(),e.getY());
            initialX = e.getX();
            initialY = e.getY();
        }
    }
}
