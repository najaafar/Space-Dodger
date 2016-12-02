
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

public class TimerFrame  extends JPanel{
    //MyTimeListener listener; 
    Timer timer;
	int time;
	boolean timerEnd;
	JLabel label;
	
    public TimerFrame() {
		timerEnd = true;
        label = new JLabel("Time: 1:00");

        add(label, BorderLayout.PAGE_START);


    }
	
	public void setTime(String time){
		
		int temp = Integer.parseInt(time);
		int minute = temp / 60;
		int second = temp % 60;
		
		if(minute > 0){
			
			if(second < 10){
				this.label.setText("Time: "+ minute + ":0" + second);
			}else{
				this.label.setText("Time: "+ minute + ":" + second);
			}
			
		}else if(minute < 0){
			
			if(second < 10){
				this.label.setText("Time: "+ minute + ":0" + second);
			}else{
				this.label.setText("Time: "+ minute + ":" + second);
			}
			
		}
		
		if(minute == 0 && second == 0){
			this.label.setText("Time's Up");
		
		}
	}
	
	public int getTimeLeft(){
		
		return this.time;
		
	}
	
	public void setTimeEnd(boolean temp){
		
		this.timerEnd = temp;
		
	}

}