package Zstudio;

import Zstudio.controller.CheeseGameController;
import Zstudio.database.ConnectDB;

import java.sql.Connection;
import java.sql.SQLException;



public class Main {

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        try{
            Connection con = ConnectDB.getInstance();
            boolean showSolutionBoard = true;
            CheeseGameController game = new CheeseGameController();
            game.play(4,4,showSolutionBoard, con);

        }catch (Exception e){
            System.out.println("Error closing resource " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());
        }finally{
            try {
                ConnectDB.closeConnection();
            }catch (SQLException e){
                System.out.println("Error closing resource2 " + e.getClass().getName());
            }
        }
    }
}
