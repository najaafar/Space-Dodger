
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

public class TimerFrame  extends JPanel{
    MyTimeListener listener; 
    Timer timer;
    public TimerFrame() {
 
        JLabel label = new JLabel("Time: 1:00");

        add(label, BorderLayout.PAGE_START);

        //pass the label into the MyTimeListener constructor
        listener = new MyTimeListener(label);

        //the timer fires every 1000 MS (1 second)
        //when it does, it calls the actionPerformed() method of MyTimeListener
        timer = new Timer(1000, listener);
        //start the timer
        timer.start();

        if(listener.getStatus() == true){
			//JOptionPane.showMessageDialog(null, "Game Over!");
            timer.stop();  
            timer.removeActionListener(listener);
            //System.exit(0);
            //return;
        }
  
    }
}