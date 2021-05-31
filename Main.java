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
import javax.swing.*;
import java.awt.*;
/**
 * The purpose of this class is to allow the program to be runnable and deal with global variables
 */
public class Main {
    public static JFrame frame = new JFrame("TSP Tour Calculator");

    /**
     * The main method of the entire project
     * @param args
     */
    public static void main(String[] args)
    {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(1200,600);
        TSPPanel panel = new TSPPanel();
        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

    }
}
