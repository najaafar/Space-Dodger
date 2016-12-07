
import javax.swing.*;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JOptionPane;

public class InfoPlayerFrame  extends JPanel{
  static JLabel label;

    public InfoPlayerFrame() {
        label = new JLabel("Ranking: ");
        add(label, BorderLayout.PAGE_END);
    }

    public void updateInfo(String playerKills){

      this.label.setText("Your Score: " + playerKills);

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
