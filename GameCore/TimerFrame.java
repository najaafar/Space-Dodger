
import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

public class TimerFrame  extends JPanel{
    MyTimeListener listener; 
    Timer timer;
	int time;
	boolean timerEnd;
	
    public TimerFrame() {
		timerEnd = true;
        JLabel label = new JLabel("Time: 1:00");

        add(label, BorderLayout.PAGE_START);

        // pass the label into the MyTimeListener constructor
        listener = new MyTimeListener(label);

		//	the timer fires every 1000 MS (1 second) when it does, it calls the actionPerformed() method of MyTimeListener		
        timer = new Timer(1000, listener);
		
        //start the timer
        timer.start();
/*
        if(listener.getStatus() == true){
			System.out.println(listener.getStatus());
			this.setTimeEnd(false);
            //this.timer.stop();  
            //timer.removeActionListener(listener);
			
        }
  */
    }
	
	public void setTime(int time){
		
		this.time = time;
		
	}
	
	public int getTimeLeft(){
		
		return this.time;
		
	}
	
	public void setTimeEnd(boolean temp){
		
		this.timerEnd = temp;
		
	}
	
	public boolean isTimerOn(){
		
		if(listener.getStatus() == true){
			System.out.println(listener.getStatus());
			this.setTimeEnd(false);
            this.timer.stop();  
            timer.removeActionListener(listener);
			
        }
		
		return this.timerEnd;
		
	}
	
}