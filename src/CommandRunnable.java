import java.io.PrintWriter;
import java.net.Socket;

/**
 * This class of thread is mainly in charge of processing the request from the clients and then broadcasting the command
 * to its clients.
 * 
 * @author Liu Yuchen
 * @version 1.2
 */
public class CommandRunnable implements Runnable{
    private int playerID;
    private String request;
    private boolean needProcess;
    
    /**
     * This is the constructor of this thread. Basically, this sets the playerID and the data that this thread needs to handle.
     * @param ID the playerID of the client supportor that launches this thread
     * @param request the request waiting to be handled
     * @param needProcess the indicator that indicates whether the data need to be preprocessed.
     */
    public CommandRunnable(int ID, String request,boolean needProcess){
        //this.socket=socket;
        this.playerID=ID;
        this.request=request;
        this.needProcess=needProcess;
    }
    
    /**
     * This method is the main execuatable. It process the request, if needed. And then broadcast it to all the clients. 
     * The contact information is stored in the set in InformationHub.
     */
    public void run(){
        int result=0;
        System.out.println("Command in "+playerID+" is now launching.");
        for (Socket socket:InformationHub.sockets){
            try{
                PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
                String request = processCommand();
                writer.println(request);
                System.out.println(request);
                result=InformationHub.outcomeDeterminator();
                writer.println("E|"+result);
            }catch(Exception ex){
                System.out.println("Errors occur when using command in client"+playerID);
            }
        } 
    }

    /**
     * This method add marks to the request to make it understandable for the clients.
     * 
     * @return the mark-added command that needs to be broadcast
     * @throws Exception when certain bit of the request cannot to cast to integer successfully
     */
    private String processCommand() throws Exception{
        if(!needProcess){
            return request;
        }
        else{
            int nrow = Integer.parseInt(request.substring(0, 1));
            int ncolumn= Integer.parseInt(request.substring(2, 3));
            InformationHub.setGrid(nrow, ncolumn, playerID);
            return "U|"+nrow+"|"+ncolumn+"|"+playerID;
        }
    }

}