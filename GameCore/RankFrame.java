import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import java.awt.Dimension;
import java.lang.Character;

//class for the table
public class RankFrame extends JPanel {

	JTable jt;

  public RankFrame(){ //TODO Data as parameter

		String[] columns = {"Rank", "Player"}; //TODO Add stats cols
		//read output.txt
		String[][] data = new String[50][2];


    /*
      TODO Sort by time

    */




		int i = 0;
    int j;
		for(/*each player*/){
      j = i + 1;
  			data[i][0] = new String("Rank" + j);
				data[i][1] = //player name
        //data[i][....] for stats
		}

		jt = new JTable(data, columns)
		{
			public boolean isCellEditable(int data, int columns)
			{
				return false;
			}

			public Component prepareRenderer(TableCellRenderer r, int data, int columns)
			{
				Component c = super.prepareRenderer(r, data, columns);
				if (data % 2 == 0) c.setBackground(Color.WHITE);
				else c.setBackground(Color.LIGHT_GRAY);
				return c;
			}

		};

		 jt.setPreferredScrollableViewportSize(new Dimension(300,500));
		 jt.setFillsViewportHeight(true);
		 //insert table to a scrollpane
		 JScrollPane jps = new JScrollPane(jt);
		 //show table in panel
		 //add(jps);
	}

}
