import javax.swing.*;
import java.awt.*;

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
