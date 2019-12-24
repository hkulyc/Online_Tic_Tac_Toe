import java.io.IOException;
import java.net.*;
import javax.swing.*;

/**
 * This is the main executable for the game server. The main will continuously waiting for connection from the clients. Then
 * set up a new thread for that client. The server will start up a new game when there are two players online.
 * 
 * @author Liu Yuchen
 * @version 1.2
 */
public class Server{
    public static final int CLIENTSNUMBER = 2;
    private static final int PORTNUMBER = 7000;
    public static void main(String[] args) {
        try{
            ServerSocket server = new ServerSocket(PORTNUMBER);
            System.out.println("Tic Tac Toe server is running!");
            while(true){
                try{
                    Socket clientSocket = server.accept();
                    if(InformationHub.clientOnline==0)
                        InformationHub.refresh();
                    InformationHub.sockets.add(clientSocket);
                    InformationHub.clientOnline++;
                    int i=InformationHub.clientOnline;
                    Runnable listenRunnable = new ListenRunnable(clientSocket,i);
                    Thread listenThread=new Thread(listenRunnable);
                    listenThread.setName("Player"+i);
                    listenThread.start();
                    System.out.println("Client"+i+" connected!");
                }catch(IOException e){
                    System.out.println("Connect client failed!");
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        while(true){
            //infinite loop
        }
    }

}