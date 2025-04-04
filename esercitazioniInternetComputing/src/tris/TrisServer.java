package tris;
import java.util.*;
import java.io.*;
import java.net.*;

public class TrisServer {
    private ServerSocket serverSocket;
    private final int SERVER_PORT = 5000;
    private List<Socket> waitingUsers = new LinkedList<>();


    public TrisServer(){
        try{
            serverSocket = new ServerSocket(SERVER_PORT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        System.out.println("Starting TrisServer...");
        while(true){
            Socket socket = serverSocket.accept();
            SocketHandler socketHandler = new SocketHandler(socket, waitingUsers);
            socketHandler.start();
        }
    }

    public static String beautifyBoard(List<List<Integer>> board){
        StringBuilder builder = new StringBuilder();
        for(List<Integer> row : board){
            builder.append(row.toString());
            builder.append("\n");
        }
        return builder.toString();
    }

    public static boolean isSolved(List<List<Integer>> board) {
        int n = board.size(); // Assuming a square board (n x n)

        // Check rows and columns
        for (int i = 0; i < n; i++) {
            if (checkLine(board.get(i)) || checkLine(getColumn(board, i))) {
                return true;
            }
        }

        // Check diagonals
        if (checkLine(getMainDiagonal(board)) || checkLine(getAntiDiagonal(board))) {
            return true;
        }

        // Check if board is full (draw condition)
        return isFull(board);
    }

    private static boolean checkLine(List<Integer> line) {
        return !line.contains(0) && line.stream().distinct().count() == 1;
    }

    private static List<Integer> getColumn(List<List<Integer>> board, int col) {
        List<Integer> column = new ArrayList<>();
        for (List<Integer> row : board) {
            column.add(row.get(col));
        }
        return column;
    }

    private static List<Integer> getMainDiagonal(List<List<Integer>> board) {
        List<Integer> diagonal = new ArrayList<>();
        for (int i = 0; i < board.size(); i++) {
            diagonal.add(board.get(i).get(i));
        }
        return diagonal;
    }

    private static List<Integer> getAntiDiagonal(List<List<Integer>> board) {
        List<Integer> diagonal = new ArrayList<>();
        int n = board.size();
        for (int i = 0; i < n; i++) {
            diagonal.add(board.get(i).get(n - 1 - i));
        }
        return diagonal;
    }

    private static boolean isFull(List<List<Integer>> board) {
        for (List<Integer> row : board) {
            if (row.contains(0)) {
                return false; // Empty cell found, board is not full
            }
        }
        return true; // No empty cells, board is full
    }
    public static void main(String[] args) throws IOException {
        TrisServer server = new TrisServer();
        server.start();
    }
}

class SocketHandler extends Thread{
    private Socket socket;
    private List<Socket> waitingUsers;

    public SocketHandler(Socket socket, List<Socket> waitingUsers){
        this.socket = socket;
        this.waitingUsers = waitingUsers;
    }

    public void run(){
        try{
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message = "This is a list of all available users: " + waitingUsers.toString() + " . You have been added to the queue. Please select a user.";
            out.println(message);
            waitingUsers.add(socket);
            System.out.println("Added user to queue: " + socket.getRemoteSocketAddress().toString());
            String response = in.readLine();
            for(Socket u : waitingUsers){
                if(response.equals(u.getRemoteSocketAddress().toString())){
                    GameHandler game = new GameHandler(socket, u);
                    game.start();
                    waitingUsers.remove(u);
                }
                else{
                    boolean goodUserSelected = false;
                    while(!goodUserSelected){
                        String errMessage = "ERR USER NOT AVAILABLE";
                        out.println(errMessage);
                        response = in.readLine();
                        for(Socket user : waitingUsers){
                            if(response.equals(user.getRemoteSocketAddress().toString())){
                                goodUserSelected = true;
                                GameHandler game = new GameHandler(socket, u);
                                game.start();
                                waitingUsers.remove(u);
                            }
                        }
                    }
                }
            }
            // GameHandler game = new GameHandler(socket,
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

class GameHandler extends Thread {
    private Socket player1;
    private Socket player2;
    private List<List<Integer>> board = new ArrayList<>();

    public GameHandler(Socket player1, Socket player2){
        this.player1 = player1;
        this.player2 = player2;
        for(int i = 0; i < 3; i++){
            board.add(new ArrayList<>());
            for(int j = 0; j < 3; j++){
                board.get(i).add(j,0);
            }
        }
    }

    public void run(){
        boolean gameOver = false;
        Socket[] players = new Socket[]{player1, player2};
        int turn = 0;
        try{
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true);
            PrintWriter[] outs = new PrintWriter[]{out1,out2};
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
            BufferedReader[] ins = new BufferedReader[]{in1,in2};
            while(!gameOver){
                if(turn == 0){
                    turn = 2;
                    String player1 = "You go first, player 1. Input your move in index X, index Y notation. (Ex. 0 0 fills first column, first row) ";
                    String player2 = "Wait for your turn, player 2";
                    out1.println(player1);
                    out2.println(player2);
                    String move = in1.readLine();
                    StringTokenizer stringTokenizer = new StringTokenizer(move);
                    int x = Integer.parseInt(stringTokenizer.nextToken());
                    int y = Integer.parseInt(stringTokenizer.nextToken());
                    board.get(x).set(y,1);
                }
                else{
                    turn = (turn % 2)+1;
                    int index = turn - 1;
                    //if(index == 0){
                        outs[index].println(TrisServer.beautifyBoard(board));
                        String move = in1.readLine();
                        StringTokenizer stringTokenizer = new StringTokenizer(move);
                        int x = Integer.parseInt(stringTokenizer.nextToken());
                        int y = Integer.parseInt(stringTokenizer.nextToken());
                        board.get(x).set(y,turn);
                        gameOver = TrisServer.isSolved(board);
                    //}
                }
            }
            String endMessage = "Game Over!";
            out1.println(endMessage);
            out2.println(endMessage);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
