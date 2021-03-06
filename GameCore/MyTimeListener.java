import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
	
public class MyTimeListener implements ActionListener{

    private int elapsed_sec = 0; 
    private int minute = 1; 
    private boolean status = false;

    private JLabel label;

    public MyTimeListener(JLabel label){
        this.label = label;
    }

    public void actionPerformed(ActionEvent e){ 
        if(status == false){
            elapsed_sec--; 
            if (minute == 1){ 
                minute--;
                elapsed_sec = 59;
            }

            if(elapsed_sec <= 9){
                label.setText("Time: " + minute + ":0" + elapsed_sec);
            } else {
                label.setText("Time: " + minute + ":" + elapsed_sec);
            }

            if(minute == 0 && elapsed_sec == 0){ 
                label.setText("Time's up"); 
                status = true;
            } 

        } else {
            //System.exit(0);
        }
    }

    public boolean getStatus(){
        return status;
    }
	
	public int getTime(){
		
		return elapsed_sec;
		
	}
}