import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class ChatPanel extends JPanel implements Runnable{

    Socket socket;
    JTextArea ta;
    JButton send, logout, startGame;
    JTextField tf;

    Thread thread;

    DataInputStream din;
    DataOutputStream dout;

    String LoginName;
	String host;

    ChatPanel(String login, String address) throws UnknownHostException, IOException{
		
        LoginName = login;
		host = address;

        ta = new JTextArea(25, 40);
        tf = new JTextField(40);

        tf.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e){

            }

            @Override
            public void keyPressed(KeyEvent e){
				
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
					
                    try{
						
                        if(tf.getText().length() > 0)
                            dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
						
                        tf.setText("");
						
                    }catch (IOException e1){
						
                        e1.printStackTrace();
						
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e){

            }

        });

        send = new JButton("Send");
        logout = new JButton("Logout");

        send.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
				
                try{
					
                    if (tf.getText().length() > 0)
                        dout.writeUTF(LoginName + " " + "DATA " + tf.getText().toString());
					
                    tf.setText("");
					
                }catch(IOException e1){
					
                    e1.printStackTrace();
					
                }
            }

        });

        logout.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
				
                try{
					
                    dout.writeUTF(LoginName + " " + "LOGOUT");
                    System.exit(1);
					
                }catch(IOException e1){
					
                    e1.printStackTrace();
					
                }
            }

        });
		
		

        socket = new Socket(host, 9999);

        din = new DataInputStream(socket.getInputStream());
        dout = new DataOutputStream(socket.getOutputStream());

        dout.writeUTF(LoginName);
        dout.writeUTF(LoginName + " " + "LOGIN");

        thread = new Thread(this);
        thread.start();
        setup();
    }

    private void setup(){
		ta.setEditable(false);
        this.add(new JScrollPane(ta));
        this.add(tf);
        this.add(send);
        this.add(logout);


    }

    @Override
    public void run(){
		
        while(true){
			
            try{
				
                ta.append("\n" + din.readUTF());
				
            }catch (IOException e){
				
                e.printStackTrace();
				
            }
        }
    }


}
