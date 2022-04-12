package Zstudio.controller;

import Zstudio.database.ConnectDB;
import Zstudio.database.GameService;
import Zstudio.model.Dashboard;
import Zstudio.model.GameInfo;
import Zstudio.model.MouseCheeseGame;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;


public class CheeseGameController {
    Scanner input = new Scanner(System.in);
    int currentRowMousePosition = 0 ;
    int currentColMousePosition = 0;
    String currentCellFigure = "";
    int newRowMousePosition = 0 ;
    int newColMousePosition = 0;
    MouseCheeseGame game;
    private String[][] gameBoard;

    GameService gameService;
    private String nickname = "";

    public void play(int rows, int cols, boolean testMode, Connection con)  {
        this.gameService = new GameService(con);

        System.out.println("Bienvenido a Cheese Mouse Game!!");
        if(gameService.getGameDB() != null) {
            boolean exitMenu = false;
            while (!exitMenu) {
                String message = "Se ha detectado una partida en cuso...\nPara continuar la partida pulse: [c]\nPara empezar una nueva partida pulse: [n]" ;
                String answer = readString(message);
                switch (answer) {
                    case "c":
                        GameInfo gameInfo = gameService.getGameDB();
                        this.nickname = gameInfo.getNickname();
                        this.currentRowMousePosition = gameInfo.getCurrentRowMousePosition();
                        this.currentColMousePosition = gameInfo.getCurrentColMousePosition();
                        gameBoard = continueBoard(rows,cols);
                        game = new MouseCheeseGame(rows,cols,testMode, con , false);
                        game.setTotalEarnedPoints(gameInfo.getTotalPoints());
                        game.setRowMousePosition(gameInfo.getCurrentRowMousePosition());
                        game.setColMousePosition(gameInfo.getCurrentColMousePosition());
                        exitMenu=true;
                        break;
                    case "n":
                        dataBaseReset();
                        newGame(rows, cols, testMode, con, true);
                        exitMenu=true;
                        break;
                }

            }
        }else{
            dataBaseReset();
            newGame(rows, cols, testMode, con, true);
        }

        System.out.println("Bienvenido a Cheese Mouse Game!!");
        System.out.println("El objetivo es que el raton 'MM' llegue al queso 'CH'.");
        System.out.println("Para desplazar el raton pulse:");
        System.out.println("[R]: Derecha (Right)");
        System.out.println("[L]: Izquierda (Left)");
        System.out.println("[U]: Arriba (Up)");
        System.out.println("[D]: Abajo (Down)");
        System.out.println("O tambien puede:");
        System.out.println("[H]: Mostrar tabla puntuaciones (Hall of frames)");
        System.out.println("[E]: Salir (Exit)");
        System.out.print( printBoard()+"\n");
        while (!game.hasLost() && !game.hasWon()){

            String choice = readString("Inserte eleccion:");
            switch(choice){
                case "e":
                    boolean exitSaveMenu=false;
                    while(!exitSaveMenu){
                        String saveGame = readString("Quieres guardar la partida?\n[Y] si (Yes)\n[N] no (No)\n[B] Volver atras (Back)");
                        switch (saveGame.toLowerCase()){
                            case "y":
                                GameInfo gameToSave = new GameInfo(this.nickname,
                                                    game.getTotalEarnedPoints(),
                                                    this.currentRowMousePosition,
                                                    this.currentColMousePosition);
                                gameService.setGameDB(gameToSave);
                                System.out.println("Guardando...");
                                try {
                                    ConnectDB.closeConnection();
                                }catch (SQLException e){
                                    System.out.println("Error closing resource2 " + e.getClass().getName());
                                }
                                System.exit(0);
                            case "n":
                                gameService.deleteFieldsDB("gameinfo");
                                gameService.deleteFieldsDB("cell");
                                System.out.println("saliendo sin guardar...");
                                try {
                                    ConnectDB.closeConnection();
                                }catch (SQLException e){
                                    System.out.println("Error closing resource2 " + e.getClass().getName());
                                }
                                System.exit(0);

                            case "b":
                                exitSaveMenu=true;
                                break;
                        }
                    }
                    break;
                case "r","l","u","d":
                    String moveOutput = this.move(choice);
                    if(moveOutput != null){
                        System.out.println(moveOutput);
                    }
                    break;
                case "h":
                    ArrayList<Dashboard> dashboardArr = gameService.getDashboard();
                    System.out.println("HALL OF FRAMES");
                    System.out.println("--------------");
                    for(int i=0; i<dashboardArr.size();i++){
                        System.out.println(dashboardArr.get(i).getPoints()+ " " + dashboardArr.get(i).getNickname());
                    }
                    System.out.println("--------------");
                    break;
            }
        }
        if(game.hasWon()){
            gameService.setInfoDashboard(this.nickname,game.getTotalEarnedPoints());
            gameService.deleteFieldsDB("gameinfo");
            gameService.deleteFieldsDB("cell");
        }
    }

    /**Delete Databases = Cell && GameInfo to
     * prevent errors if someone manipulates the Database
     */
    private void dataBaseReset(){
        gameService.deleteFieldsDB("gameinfo");
        gameService.deleteFieldsDB("cell");
    }

    /**
     * Create a new Game
     * @param rows int
     * @param cols int
     * @param testMode boolean
     * @param con Connection
     * @param newGame boolean
     */
    private void newGame(int rows, int cols, boolean testMode, Connection con, boolean newGame){
        while (nickname.equals("")){
            this.nickname = readString("Introduce tu Nickname");
        }
        gameBoard = startBoard(rows,cols);
        game = new MouseCheeseGame(rows,cols,testMode, con, newGame);
        game.start();
    }

    /**
     * Start board to print
     * @param rows int
     * @param cols int
     * @return String[][]
     */
    private String[][] startBoard(int rows, int cols) {
        String[][] board = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j]="00";
            }
        }
        board[0][0]="MM";
        board[rows-1][cols-1]="CH";
        return board;
    }

    /**
     * Continue a board from Database
     * @param rows int
     * @param cols int
     * @return String[][]
     */
    private String[][] continueBoard(int rows, int cols) {
        //TODO Mejorar la eficiencia haciendo que reciba un array y hacer una unica consulta a la DB
        //GameCell [][] gameboard = gameService.getGameboard(rows, cols);

        String[][] board = new String[rows][cols];

        int lastDiscoveredI = 0;
        int lastDiscoveredJ = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(gameService.getCellDB(i,j).isDiscovered()){
                    board[i][j]="·.";
                    lastDiscoveredI = i;
                    lastDiscoveredJ = j;
                }else{
                    board[i][j]="00";
                }
            }
        }
        board[lastDiscoveredI][lastDiscoveredJ]="MM";
        board[rows-1][cols-1]="CH";
        return board;
    }


    public String move(String movement) {
        return performMovement(game.startMouseMovement(movement));
    }

    private String performMovement(String question) {
        if(question == null) {
            newRowMousePosition = game.getRowMousePosition();
            newColMousePosition = game.getColMousePosition();
            if(newRowMousePosition != currentRowMousePosition || newColMousePosition != currentColMousePosition) {
                moveMouseToNewCell();
                if(currentCellFigure=="CH"){
                    return printBoard() + String.format("Felicidades, has ganado!!\nPuntuacion total: %3d",game.getTotalEarnedPoints());
                }
                if(currentCellFigure=="CC") {
                    gameBoard[currentRowMousePosition][currentColMousePosition]=currentCellFigure;
                    return printBoard() + "Has perdido";
                }
                return printBoard() + String.format("Puntos acumulados: %3d\n",game.getTotalEarnedPoints());
            }
            return null;
        }

        System.out.print(question);
        //Preguntar al usuario
        String userAnswer = input.nextLine();
        game.completeMouseMovement(userAnswer);
        return performMovement(null);
    }

    private void moveMouseToNewCell() {
        gameBoard[newRowMousePosition][newColMousePosition]="MM";
        switch (currentCellFigure){
            case "--","++":
                gameBoard[currentRowMousePosition][currentColMousePosition]=currentCellFigure;
                break;
            default:
                gameBoard[currentRowMousePosition][currentColMousePosition]="·.";
                break;

        }
        currentCellFigure = game.getCurrentFigure();
        currentRowMousePosition = newRowMousePosition;
        currentColMousePosition = newColMousePosition;
    }

    private String printBoard() {
        String board ="";
        for(int x = 0; x < gameBoard.length; x++){
            for(int y = 0; y < gameBoard[0].length ; y++){
                if(y > 0 && y < gameBoard[0].length)
                    board += " ";
                board += (gameBoard[x][y]);
            }
            board +=("\n");
        }
        return board;
    }

    /**
     * Read input of console
     * @param message String
     * @return String
     */
    public String readString (String message){
        String strInput;
        do {
            System.out.println(message);
            strInput = input.nextLine();
        }while(strInput.equals(""));
        return strInput.toLowerCase();
    }
}
