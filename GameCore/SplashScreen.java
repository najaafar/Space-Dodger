import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

public class SplashScreen extends JWindow {
  private int duration;

  public SplashScreen(int d) {
    duration = d;
  }
 
  public void showSplash() {
    JPanel content = (JPanel) getContentPane();
    content.setBackground(Color.white);
 
    int width = 700;
    int height = 200;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screen.width - width) / 2;
    int y = (screen.height - height) / 2;
    setBounds(x, y, width, height);

    // Build the splash screen
    JLabel label = new JLabel(new ImageIcon("player.png"));
    JLabel copyrt = new JLabel("Loading Space Dodger...", JLabel.CENTER);
    copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
    content.add(label, BorderLayout.CENTER);
    content.add(copyrt, BorderLayout.SOUTH); 
 
    setVisible(true);
 
    try {
      Thread.sleep(duration);
    } catch (Exception e) {}

    setVisible(false);
  }

  public void showSplashAndExit() {
    showSplash(); 
  } 
}
