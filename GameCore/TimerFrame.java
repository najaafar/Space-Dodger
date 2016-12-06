
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

public class TimerFrame  extends JPanel{
	Boolean timerEnd;
    Timer timer;
	int time;
	JLabel label;
	
    public TimerFrame(){
		timerEnd = true;
        label = new JLabel("Time:");
        add(label, BorderLayout.PAGE_START);
    }
	
	public void setTime(String time){// updates the JLabel content
		
		int temp = Integer.parseInt(time);
		
		if(temp == 0){
			this.label.setText("Time's Up!");

		}else{
			this.label.setText("Time: "+ time + " seconds");
			this.time = temp; 
		}
		
	}
	
	public int getTimeLeft(){
		
		return this.time;
		
	}
	

}