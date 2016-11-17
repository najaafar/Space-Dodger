
import javax.swing.*;
import javax.swing.JLabel; 
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

public class InfoPlayerFrame  extends JPanel{ 

    public InfoPlayerFrame() { 
        JLabel label = new JLabel("Ranking: "); 
        add(label, BorderLayout.PAGE_END);
    }
}