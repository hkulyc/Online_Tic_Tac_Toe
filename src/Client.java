import java.io.*;
import java.net.Socket;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This is the class containing all the functions of one client. This class has an executable main, which will launch two threads, one is
 * the thread for GUI display, and another is for communication. 
 * 
 * @author Liu Yuchen
 * @version 1.2
 */
public class Client {
    private JMenuBar menuBar;
    private JMenu Control, Help;
    private JMenuItem Exit, Rule, Author;
    private JFrame mainFrame;
    private static final int PORTNUMBER = 7000;
    private Socket socket;
    private JPanel mainPanel, messagePanel, gridPanel, textPanel;
    private JLabel messageLabel;
    private JTextField nameInputField;
    private JButton nameButton;
    public static ArrayList<JLabel> chesses = new ArrayList<JLabel>();
    public static ArrayList<JButton> chessButtons = new ArrayList<JButton>();
    public int playerID = Server.CLIENTSNUMBER + 1;
    public boolean yourTurn = false;

    /**
     * This method configures the structure of the frame and the main panel.
     */
    public void mainFrameconfiguration() {
        mainFrame = new JFrame("Tic Tac Toe");
        mainFrame.setSize(250, 350);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainPanel = new JPanel();
        mainFrame.getContentPane().add(mainPanel);
    }

    /**
     * This method configures the menu.
     */
    public void menuConfiguration() {
        menuBar = new JMenuBar();
        Control = new JMenu("Control");
        Help = new JMenu("Help");
        menuBar.add(Control);
        menuBar.add(Help);
        Exit = new JMenuItem("Exit");
        Exit.addActionListener(ExitListener);
        Rule = new JMenuItem("Instruction");
        Rule.addActionListener(HelpListener);
        Author = new JMenuItem("Author");
        Author.addActionListener(AuthorListener);
        Control.add(Exit);
        Help.add(Rule);
        Help.add(Author);
        mainFrame.setJMenuBar(menuBar);
    }

    /**
     * This method configures the message panel.
     */
    public void messageConfiguration() {
        messageLabel = new JLabel("Enter your player name...");
        messagePanel = new JPanel();
        messagePanel.setBackground(Color.GREEN);
        messagePanel.add(messageLabel);
        mainFrame.add(messagePanel, BorderLayout.NORTH);
    }

    /**
     * This method configures the grid to place chess.
     */
    public void gridConfiguration() {
        gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(3, 3));
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                JPanel chessPanel = new JPanel();
                JButton chessButton = new JButton();
                JLabel label = new JLabel("");
                label.setFont(label.getFont().deriveFont(64.0f));
                label.setAlignmentX(Component.CENTER_ALIGNMENT);
                chessPanel.setLayout(new GridLayout(1, 1));
                chessButton.setBackground(Color.white);
                chessButton.setEnabled(false);
                chessButton.addActionListener(new playListener(i, j));
                chessButton.add(label);
                chessPanel.add(chessButton);
                chessPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                gridPanel.add(chessPanel);
                chesses.add(label);
                chessButtons.add(chessButton);
            }
        }
        mainFrame.add(gridPanel, BorderLayout.CENTER);
    }

    /**
     * This method configures the panel where to input name.
     */
    public void textConfiguration() {
        nameInputField = new JTextField(12);
        nameButton = new JButton("Submit");
        nameButton.addActionListener(SubmitListener);
        textPanel = new JPanel();
        textPanel.add(nameInputField);
        textPanel.add(nameButton);
        mainFrame.add(textPanel, BorderLayout.SOUTH);
    }

    private ActionListener ExitListener = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(0);
        }
    };

    private ActionListener HelpListener = new ActionListener() {
        public void actionPerformed(ActionEvent actionEvent) {
            JOptionPane.showMessageDialog(mainFrame,
                    "Criteria for a valid move: \n- The move is not occupied by any mark. \n- The move is made in the player's turn. \n- The move is made within the 3 x 3 board. \nThe game would continue and switch among the opposite player until it reaches either one of the following conditions: \n- Player 1 wins. \n- Player 2 wins. \n- Draw.",
                    "Rule", JOptionPane.INFORMATION_MESSAGE);
        }
    };

    private ActionListener SubmitListener = new ActionListener(){
        public void actionPerformed(ActionEvent actionEvent){
            String name=nameInputField.getText();
            messageLabel.setText("WELCOME "+name);
            nameInputField.setEnabled(false);
            nameButton.setEnabled(false);
            try{
                PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
                writer.println("Name input.");
            }catch(Exception ex){
                System.out.println("Cannot send request to server!");
            }
            for(JButton button:chessButtons){
                button.setEnabled(true);
            }
            mainFrame.setTitle("Tic Tac Toe-Player: "+name);
        }
    };

    class playListener implements ActionListener {
        private int nrow, ncol;

        public playListener(int nrow, int ncol) {
            super();
            this.nrow = nrow;
            this.ncol = ncol;
        }

        public void actionPerformed(ActionEvent actionEvent) {
            try {
                Thread.sleep(100);//avoid the violation when a client makes frequent request before the server have handled the last one
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(yourTurn==true){
                try{
                    PrintWriter writer=new PrintWriter(socket.getOutputStream(),true);
                    writer.println(""+nrow+"|"+ncol);
                }catch(Exception ex){
                    System.out.println("Cannot send request to server!");
                }
            }
            else{
                JOptionPane.showMessageDialog(mainFrame, "It is not your turn. Please wait for your component!","Warning", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private ActionListener AuthorListener=new ActionListener(){
        public void actionPerformed(ActionEvent actionEvent){
            try { 
                String url = "https://github.com/hkulyc"; 
                java.net.URI uri = java.net.URI.create(url); 
                java.awt.Desktop dp = java.awt.Desktop.getDesktop(); 
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                dp.browse(uri);
                } 
            } catch (java.lang.NullPointerException e) {  
                e.printStackTrace(); 
            } catch (java.io.IOException e) {  
                e.printStackTrace(); 
            } 
        }
    };
    
    /**
     * This method helps to set the sequence of each configuration functions.
     */
    public void GUIconfiguration(){
        mainFrameconfiguration();
        menuConfiguration();
        messageConfiguration();
        gridConfiguration();
        textConfiguration();
        frameVisible();
    }

    /**
     * This method sets the whole frame to be visible.
     * Note that always call this function in the end of GUI construction!
     */
    public void frameVisible(){
        mainFrame.setVisible(true);
    }

    /**
     * This method handles action needed when the client received a command from the server.
     * Actions performed: Place the chess on the corresponding spot. Change the turn to play.
     * 
     * @param nrow the row position of that spot
     * @param ncol the column position of that spot
     * @param playerID the player who place the chess. This will affect the sign and color of that chess. playerID=1-->draw a green cross;playerID=2-->draw a red cycle
     */
   
    private void placeChess(int nrow, int ncol, int playerID){
        chessButtons.get(nrow*3+ncol).setEnabled(false);
        if(playerID==1){
            chesses.get(nrow*3+ncol).setText("x");
            chesses.get(nrow*3+ncol).setForeground(Color.green);
        }
        else if(playerID==2){
            chesses.get(nrow*3+ncol).setText("o");
            chesses.get(nrow*3+ncol).setForeground(Color.red);
        }
        if(playerID!=this.playerID){
            yourTurn=true;
            if(!nameButton.isEnabled())
                messageLabel.setText("Your opponent has moved, now is your turn.");
            else messageLabel.setText("Your opponent has moved. \nNote that you cannot place a chess unless you submit your name!");
        }
        else{
            yourTurn=false;
            messageLabel.setText("Valid move, wait for your opponent.");
        } 
    }
    
    /**
     * This method handles the action when the client receives an "M" marked message.
     * 
     * @param message the command that started by "M|". This will always indicate the end of the game.
     */
    private void MDealer(String message){
        JOptionPane.showMessageDialog(mainFrame,message.substring(2),"GAME OVER", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    /**
     * This method handles the action when the client receives an "E" marked message.
     * 
     * @param message the result after one move is made
     */
    private void EDealer(String message){
        int result=Integer.parseInt(message.substring(2, 3));
        if(result!=0){
            if(result==3){
                JOptionPane.showMessageDialog(mainFrame, "Draw.","GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            else if(playerID==result){
                JOptionPane.showMessageDialog(mainFrame, "Congratulations. You Win.","GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            else if(playerID!=result){
                JOptionPane.showMessageDialog(mainFrame, "You lose.","GAME OVER", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }
    }
    
    public static void main(String[] args) {
        Client client =new Client();
        client.GUIconfiguration();
        try {
            Socket socket = new Socket("127.0.0.1",PORTNUMBER);
            client.socket=socket;
            InputStreamReader streamReader=new InputStreamReader(socket.getInputStream());
            BufferedReader reader=new BufferedReader(streamReader);
            String command=null;
            client.playerID=Integer.parseInt(reader.readLine());//receive its ID in the first place
            if(client.playerID==1)    client.yourTurn=true;
            if(client.playerID>2){
                JOptionPane.showMessageDialog(client.mainFrame, "A game has started already, please try again later!","Apology", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
            while(true){
                command= reader.readLine();
                if(command==null)
                    break;
                if(command.substring(0,1).equals("U")){
                    int nrow = Integer.parseInt(command.substring(2, 3));
                    int ncolumn=Integer.parseInt(command.substring(4, 5));
                    int playerID=Integer.parseInt(command.substring(6,7));
                    client.placeChess(nrow, ncolumn, playerID);    
                }
                else if(command.substring(0,1).equals("M")){
                    client.MDealer(command);
                }
                else if(command.substring(0,1).equals("E")){
                    client.EDealer(command);
                }
            }
            socket.close();
		} catch (IOException e) {
            System.out.println("Server shut you down!");
		}
        

    }
}