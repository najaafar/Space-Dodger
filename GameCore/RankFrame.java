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
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;


//class for the table
public class RankFrame extends JPanel {

	JTable jt;

  public RankFrame(String input,String player_list){ //TODO Data as parameter

		String[] columns = {"Player", "Score"}; //TODO Add stats cols
		String[][] data = new String[50][2];
		String[] scores = input.split(",");

    int j;
		int s = scores.length;
		for(int i = 0;i<s;i++){
       j = i + 1;
  	 		data[i][0] = new String("Player" + j);
		 		data[i][1] = scores[i];//player name
         //data[i][....] for stats
		}

		TableModel model = new DefaultTableModel(data, columns) {
	    @Override
	    public Class getColumnClass(int columns) {
	        Class returnValue;
	        if ((columns >= 0) && (columns < getColumnCount())) {
	            returnValue = getValueAt(0, columns).getClass();
	        } else {
	            returnValue = Object.class;
	        }
	        return returnValue;
	    }
    };
		jt = new JTable(model)
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

		 RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
     jt.setRowSorter(sorter);
		 jt.setPreferredScrollableViewportSize(new Dimension(300,500));
		 jt.setFillsViewportHeight(true);
		 //System.out.println(player_list)
		 //insert table to a scrollpane
		 JScrollPane jps = new JScrollPane(jt);
		 //JLabel label = new JLabel(player_list);
		 //show table in panel
		 //add(label);
		 add(jps);
	}

}
