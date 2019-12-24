import java.io.*;
import java.net.Socket;

/**
 * This is a thread that keeps listening to an assigned client until the connection is failed. 
 * @author Liu Yuchen
 * @version 1.2
 */
public class ListenRunnable implements Runnable{
    private Socket socket;
    private int playerID;
    private String request;

    /**
     * This is the constructor for this thread. Basically, the socket and playerID should be fixed at the first place.
     * 
     * @param socket the socket reference
     * @param playerID the player's ID, which is an integer
     */
    public ListenRunnable(Socket socket, int playerID){
        this.socket=socket;
        this.playerID=playerID;
    }
    
    /**
     * This is the main executable of this thread. When a request is heard, it will launch a new thread to process it, and make actions.
     */
    public void run(){
        if(playerID==Server.CLIENTSNUMBER){
            //InformationHub.ifStarted=true;
            System.out.println("Clients online!");
        }
    	try {
            System.out.println(InformationHub.sockets.size());
            InputStreamReader streamReader=new InputStreamReader(socket.getInputStream());
	        BufferedReader reader=new BufferedReader(streamReader);
            PrintWriter oneto1writer=new PrintWriter(socket.getOutputStream(),true);
            System.out.println("listener for client"+playerID+" has started!");
            //Initially assign an ID to the client.
            oneto1writer.println(playerID);
	        while(true){
                try{
                    request=reader.readLine();
                    System.out.println(request);
                    if(InformationHub.ifStarted){
                        launchCommand(request,true);
                    }
                    if(request.equals("Name input.")){
                        if(playerID==1){
                            InformationHub.name1=true;
                        }
                        if(playerID==2){
                            InformationHub.name2=true;
                            InformationHub.ifStarted=true;
                        }
                    }
                }catch(Exception ex) {
                    System.out.println("Client"+playerID+" is offline!");
                    InformationHub.sockets.remove(socket);
                    System.out.println(InformationHub.sockets.size());
                    if(playerID <= Server.CLIENTSNUMBER){
                        InformationHub.ifStarted=false;
                        InformationHub.clientOnline=0;
                        launchCommand("M|Game Ends. One of the players left.",false);
                        System.out.println("GAME OVER!");
                    }
                    break;
                }
            }
    	}catch(Exception ex) {
    		System.out.println("Error occurs when listen to the server!");
    	}
    }

    /**
     * This method is used to launch the new thread, which processes the request and then take actions.
     * 
     * @param request a string containing information from the client
     * @param needProcess it tells the new thread what to do(if date processing is needed in advance)
     */
    private void launchCommand(String request,boolean needProcess){
        Runnable commandRunnable=new CommandRunnable(playerID,request,needProcess);
        Thread thread = new Thread(commandRunnable);
        thread.setName("command in thread "+playerID);
        thread.start();
    }
}