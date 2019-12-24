import java.net.Socket;
import java.util.*;

/**
 * This class contains all the information shared across the server and the client supporters. 
 * 
 * @author Liu Yuchen
 * @version 1.1
 */
public class InformationHub {
    private static int[][] gameGrid={{0,0,0},{0,0,0},{0,0,0}};
    public static int clientOnline=0;
    public static boolean ifStarted=false;
    public static Set<Socket> sockets = new HashSet<>();//the contact information of all clients
    public static boolean name1=false;
    public static boolean name2=false;
    
    /**
     * This method makes zero the instance variables of InformationHub.
     */
    public static void refresh(){
        for(int i=0;i<=2;i++){
            for (int j=0;j<=2;j++){
                gameGrid[i][j]=0;
            }
        }
        clientOnline=0;
        ifStarted=false;
        sockets.clear();
    }

    /***
     * This method changes the chess on the board from 0 to 1 or 2
     *  
     * @param nrow the row number of the chess to change
     * @param ncol the column number of the chess to change
     * @param ID the number 1 or 2 the chess is to change to
     */
    public static void setGrid(int nrow, int ncol, int ID){
        gameGrid[nrow][ncol]= ID;
    }

    /**
     * This method returns integer representing the result after each chess is down.
     * 
     * @return int0 -> no one wins; int1 -> player1 wins; int2 -> player2 wins; int3 -> draws
     */
    public static int outcomeDeterminator(){
        //check for the rows and columns and diagnals
        for(int i=0;i<=2;i++){
            if(gameGrid[i][0]==gameGrid[i][1] && gameGrid[i][1]==gameGrid[i][2]){
                if(gameGrid[i][0]==1) return 1;
                if(gameGrid[i][0]==2) return 2;
            }
            if(gameGrid[0][i]==gameGrid[1][i] && gameGrid[1][i]==gameGrid[2][i]){
                if(gameGrid[0][i]==1) return 1;
                if(gameGrid[0][i]==2) return 2;
            }
        }
        if(gameGrid[0][0]==gameGrid[1][1] && gameGrid[1][1]==gameGrid[2][2]){
            if(gameGrid[0][0]==1) return 1;
            if(gameGrid[0][0]==2) return 2;
        }
        else if(gameGrid[0][2]==gameGrid[1][1] && gameGrid[1][1]==gameGrid[2][0]){
            if(gameGrid[0][2]==1) return 1;
            if(gameGrid[0][2]==2) return 2;
        }    
        for(int i=0;i<=2;i++){
            for(int j=0;j<=2;j++){
                if(gameGrid[i][j]==0)
                return 0;
            }
        }
        return 3;
    }

}