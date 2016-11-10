import java.io.*;
import java.net.*;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.ArrayList;

public class ChatServer{
	
    static Vector ClientSockets;
    static Vector LoginNames;
	static Vector logs;

	
    ChatServer() throws IOException{
		
        ServerSocket server = new ServerSocket(9001);
        ClientSockets = new Vector();
        LoginNames = new Vector();
		logs = new Vector();
		
		System.out.println("Chat Server is now running...");
		
        while(true){ // creates a client socket whenever a client tries to connect to the server
			
            Socket client = server.accept();
            AcceptClient acceptClient = new AcceptClient(client);
			
        }
    }

    public static void main(String[] args) throws IOException{
		
        ChatServer server = new ChatServer();
		
    }

    class AcceptClient extends Thread{
		
        Socket ClientSocket;
        DataInputStream din;
        DataOutputStream dout;
		

        AcceptClient(Socket client) throws IOException{
			
            ClientSocket = client;
            din = new DataInputStream(ClientSocket.getInputStream());
            dout = new DataOutputStream(ClientSocket.getOutputStream());

            String LoginName = din.readUTF();

            LoginNames.add(LoginName);
            ClientSockets.add(ClientSocket);

            start();
        }

        public void run(){
			
            while(true){
				
                try{
					
                    String msgFromClient = din.readUTF(); // data received is in format: <Login Name> <Message Type>
                    StringTokenizer st = new StringTokenizer(msgFromClient); // tokenizes the message sent from input datastream
                    String LoginName = st.nextToken(); // gets the client username
                    String MsgType = st.nextToken(); // gets the message type (LOGIN, LOGOUT, DATA)
                    int lo = -1;
                    String msg = "";
					String temp = "";

                    while(st.hasMoreTokens()){
						
                        msg = msg + " " + st.nextToken();
						
                    }
					
                    if(MsgType.equals("LOGIN")){ // Message Type = LOGIN
						
						for(int i = 0; i < LoginNames.size(); i++){
						
							Object obj = LoginNames.elementAt(i);
							String m = obj.toString();
							String n = LoginName.toString();

							if(m.matches(LoginName)){

								Socket pSocket = (Socket) ClientSockets.elementAt(i);
								DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
								
								for(int j = 0; j < logs.size(); j++){
									Object log = logs.get(j);
									String tempLog = log.toString();
									pOut.writeUTF(tempLog);
									System.out.println(tempLog);
								}
								
							}
							
                        }
						
                        for(int i = 0; i < LoginNames.size(); i++){
							
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + " has connected.");
							
                        }
						
						temp = LoginName + " has connected";
						logs.add(temp);
						
						System.out.println("Client "+ LoginName + " has connected to the server.");
						
                    }else if(MsgType.equals("LOGOUT")){ // Message Type = LOGOUT
						
                        for(int i = 0; i < LoginNames.size(); i++){
							
                            if (LoginName.equals(LoginNames.elementAt(i)))
                                lo = i;
							
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + " has disconnected.");
							
                        }
						
						temp = LoginName + " has disconnected";
						logs.add(temp);
						System.out.println("Client "+LoginName + " has disconnected from the server.");
						
                        if(lo >= 0){
                            LoginNames.removeElementAt(lo);
                            ClientSockets.removeElementAt(lo);
                        }
						
                    }else{ // Message Type = DATA
						
                        for(int i = 0; i < LoginNames.size(); i++){
							
                            Socket pSocket = (Socket) ClientSockets.elementAt(i);
                            DataOutputStream pOut = new DataOutputStream(pSocket.getOutputStream());
                            pOut.writeUTF(LoginName + ": " + msg);
                        }
						
						temp = LoginName + ": " + msg;
						logs.add(temp);
						System.out.println("Client "+LoginName + ": "+ msg);
                    }
					
					
                    if(MsgType.equals("LOGOUT")) // client with Message Type LOGOUT is stopped by the server
                        break;

                }catch(IOException e){
                    
                    e.printStackTrace();
                }
            }
        }
    }
}
