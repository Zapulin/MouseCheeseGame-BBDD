package Zstudio.model;

import Zstudio.database.GameService;

import java.sql.Connection;

public class MouseCheeseGame {

    private int totalEarnedPoints = 0;
    private int colMousePosition;
    private int rowMousePosition;
    private boolean won;
    private boolean lost;
    private boolean testMode;
    GameService gameService;
    private int rows;
    private int cols;
    private PropitiousCell tmpPropitiousCell;
    private UnpropitiousCell tmpUnpropitiousCell;

    /**
     * Crear un tablero de rows x cols y colocar el gato, el queso, una celda propicia y una celda no propicia.
     * @param rows nombre de files
     * @param cols nombre de columnes
     */
    public MouseCheeseGame(int rows, int cols,boolean testMode, Connection con, boolean newGame){
        this.rows = rows;
        this.cols = cols;
        this.gameService = new GameService(con);
        this.testMode = testMode;

        if(newGame){
            //gameService.deleteFieldsDB("cell");

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    GameCell cell = new GameCell();
                    gameService.setPosPointsDB(i,j,cell);
                }
            }

            //añadimos el gato
            while(true) {
                int x = (int) (Math.random() * rows);
                int y = (int) (Math.random() * cols);
                if ((x > 0 || y > 0) && (x < rows || y > cols) && !(x == rows-1 && y == cols-1)){
                    gameService.setCatDB(x,y);
                    break;
                }
            }

            //añadimos celda de pregunta propicia
            while(true) {
                int x = (int) (Math.random() * rows);
                int y = (int) (Math.random() * cols);
                if ((x > 0 || y > 0) && (x < rows || y > cols) && !(x == rows-1 && y == cols-1)){
                    GameCell cell = gameService.getCellDB(x,y);
                    if(!cell.isCat()){
                        gameService.setPropitiousDB(x,y);
                        break;
                    }
                }
            }

            //añadimos celda de pregunta Unpropicia
            while(true) {
                int x = (int) (Math.random() * rows);
                int y = (int) (Math.random() * cols);
                if ((x > 0 || y > 0) && (x < rows || y > cols) && !(x == rows-1 && y == cols-1)){
                    GameCell cell = gameService.getCellDB(x,y);
                    //boolean isPropitious = gameService.isCellPropitious(x,y);
                    if(!cell.isCat() || !cell.isPropitious()){
                        gameService.setUnpropitiousDB(x,y);
                        break;
                    }
                }
            }

            //añadimos el queso
            gameService.setCheeseDB(rows-1,cols-1);

            if(testMode) {
                printSolutionBoard();
            }
        }

    }

    public void start() {
        colMousePosition = 0;
        rowMousePosition = 0;
        getCurrentCell().setDiscovered();
        gameService.setDiscoveredDB(rowMousePosition,colMousePosition);
        completeMouseMovement();
    }

    public void setColMousePosition(int colMousePosition) {
        this.colMousePosition = colMousePosition;
    }

    public void setRowMousePosition(int rowMousePosition) {
        this.rowMousePosition = rowMousePosition;
    }

    public boolean hasWon() {
        return won;
    }

    public boolean hasLost() {
        return lost;
    }

    public int getTotalEarnedPoints() {
        return totalEarnedPoints;
    }

    public void setTotalEarnedPoints(int totalEarnedPoints) {
        this.totalEarnedPoints = totalEarnedPoints;
    }

    public int getColMousePosition() {
        return colMousePosition;
    }

    public int getRowMousePosition() {
        return rowMousePosition;
    }

    public String startMouseMovement(String movement) {
        switch (movement){
            case "l":
                if (colMousePosition == 0) {
                    return null;
                }
                if(getCell(rowMousePosition,colMousePosition-1).isDiscovered()) {
                    return null;
                }
                colMousePosition--;
                break;
            case "r":
                if (colMousePosition == rows-1) {
                    return null;
                }
                if(getCell(rowMousePosition,colMousePosition+1).isDiscovered()) {
                    return null;
                }
                colMousePosition++;
                break;
            case "u":
                if (rowMousePosition == 0) {
                    return null;
                }
                if(getCell(rowMousePosition-1,colMousePosition).isDiscovered())
                    return null;
                rowMousePosition--;
                break;
            case "d":
                if (rowMousePosition == cols-1) {
                    return null;
                }
                if(getCell(rowMousePosition+1,colMousePosition).isDiscovered()) {
                    return null;
                }
                rowMousePosition++;
                break;
        }
        GameCell currentCell = getCurrentCell();
        currentCell.setDiscovered();
        gameService.setDiscoveredDB(rowMousePosition,colMousePosition);

        if (rowMousePosition == rows-1 && colMousePosition == cols-1 ){
            this.won = true;
            return null;

        }else if(currentCell.isCat()){
            this.lost = true;
            return null;

        }else if(currentCell.isUnpropitious()) {
            this.tmpUnpropitiousCell = new UnpropitiousCell();
            return this.tmpUnpropitiousCell.getQuestion();

        }else if(currentCell.isPropitious()){
            int rndInt = (int) (Math.random()*5)+1;
            String question = gameService.getQuestionDB(rndInt);
            String answer = gameService.getAnswerDB(rndInt);

            this.tmpPropitiousCell = new PropitiousCell(question, answer);
            return this.tmpPropitiousCell.getQuestion();
        }
        completeMouseMovement();
        return null;
    }
    private GameCell getCell(int x,int y) {
        return gameService.getCellDB(x,y);
    }

    private GameCell getCurrentCell() {
        GameCell currentCell = gameService.getCellDB(rowMousePosition,colMousePosition);
        return currentCell;
    }


    public int completeMouseMovement(String userAnswer) {
        GameCell currentCell = getCurrentCell();
        if(currentCell.isPropitious()) {
            if (this.tmpPropitiousCell.submitAnswer(userAnswer)) {
                this.totalEarnedPoints += 50;
                return 50;
            }
        }
        if(currentCell.isUnpropitious()) {
            if (this.tmpUnpropitiousCell.submitAnswer(userAnswer)) {
                this.totalEarnedPoints -= 50;
                return -50;
            }
        }
        return 0;
    }

    public int completeMouseMovement() {
        GameCell tmpCell = getCurrentCell();
        return this.totalEarnedPoints += tmpCell.getPoints();
    }

    public String getCurrentFigure() {
        return this.getCurrentCell().toString();
    }


    public void printSolutionBoard(){
        for(int x = 0; x < rows; x++){
            for(int y = 0; y < cols ; y++){
                if(y > 0 && y < cols)
                    System.out.print(" ");
                System.out.print(gameService.getCellDB(x,y).toString());
            }
            System.out.println();
        }
        System.out.println();
    }
}
