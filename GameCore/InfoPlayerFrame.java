
import javax.swing.*;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

public class InfoPlayerFrame  extends JPanel{
  static JLabel label;

    public InfoPlayerFrame() {
        label = new JLabel("Your Score: ");
        add(label, BorderLayout.PAGE_END);
    }

    public void updateInfo(int playerScore){

      this.label.setText("Your Score: " + Integer.toString(playerScore));

    }

    // public void setTime(String time){// updates the JLabel content
    //
  	// 	int temp = Integer.parseInt(time);
    //
  	// 	if(temp == 0){
  	// 		this.label.setText("Time's Up!");
    //
  	// 	}else{
  	// 		this.label.setText("Time: "+ time + " seconds");
  	// 		this.time = temp;
  	// 	}

  	//  }
}
