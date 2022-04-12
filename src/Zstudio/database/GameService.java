package Zstudio.database;

import Zstudio.model.Dashboard;
import Zstudio.model.GameCell;
import Zstudio.model.GameInfo;
import java.sql.*;
import java.util.ArrayList;


public class GameService {
    Connection con;

    public GameService(Connection con) {
        this.con = con;
    }

    /**
     * DELETE field from DATABASE @param
     * @param database
     */
    public void deleteFieldsDB(String database){
        //Prepared statments NOT working with tables
        try (Statement stmt = con.createStatement()) {
            String query = "DELETE FROM " + database;
            stmt.executeUpdate(query);

        } catch (SQLException eSql) {
            eSql.printStackTrace();

        } catch (Exception e){
            System.out.println("Something gone wrong. Message:" + e.getMessage());
        }
    }

    /**
     * GET CELL from specific position (posX,posY)
     * @param i
     * @param j
     * @return
     */
    public GameCell getCellDB(int i, int j) {
        GameCell cell = null;

        String query = "SELECT * FROM CELL WHERE POSX = ? && POSY = ?;";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,i);
            stmt.setInt(2,j);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                cell =  new GameCell(
                        rs.getInt("points"),
                        rs.getBoolean("isCat"),
                        rs.getBoolean("isCheese"),
                        rs.getBoolean("isDiscovered"),
                        rs.getBoolean("isPropitious"),
                        rs.getBoolean("isUnpropitious"));
            }
            return cell;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cell;
    }

    /**
     * UPDATE INFO CELL (posX,posX,points) to DATABASE CELL
     * @param i
     * @param j
     * @param cell
     */
    public void setPosPointsDB(int i, int j, GameCell cell) {
        String query = "INSERT INTO CELL (posX, posY, points) VALUES (?,?,?);";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,i);
            stmt.setInt(2,j);
            stmt.setInt(3, cell.getPoints());;
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * UPDATE CAT=TRUE to specific cell Table: CELL
     * @param i
     * @param j
     */
    public void setCatDB(int i, int j) {
        String query = "UPDATE CELL SET isCat=TRUE WHERE POSX=? && POSY=?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,i);
            stmt.setInt(2,j);
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * UPDATE PROPITIOUS=TRUE to specific cell Table: CELL
     * @param i
     * @param j
     */
    public void setPropitiousDB(int i, int j) {
        String query = "UPDATE CELL SET isPropitious=TRUE WHERE POSX=? && POSY=?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,i);
            stmt.setInt(2,j);
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * UPDATE UNPROPITIOUS=TRUE to specific cell Table: CELL
     * @param i
     * @param j
     */
    public void setUnpropitiousDB(int i, int j) {
        String query = "UPDATE CELL SET isUnpropitious=TRUE WHERE POSX=? && POSY=?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,i);
            stmt.setInt(2,j);
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * UPDATE CHEESE=TRUE to specific cell Table: CELL
     * @param i
     * @param j
     */
    public void setCheeseDB(int i, int j) {
        String query = "UPDATE CELL SET isCheese=TRUE WHERE POSX=? && POSY=?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,i);
            stmt.setInt(2,j);
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * UPDATE DISCOVER=TRUE to specific cell Table: CELL
     * @param i
     * @param j
     */
    public void setDiscoveredDB(int i, int j) {
        String query = "UPDATE CELL SET isDiscovered=TRUE WHERE POSX=? && POSY=?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,i);
            stmt.setInt(2,j);
            stmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * INSERT GameInfo to Database:GAMEINFO
     * @param game
     */
    public void setGameDB(GameInfo game) {

        String query = "INSERT INTO GAMEINFO (nickname, totalPoints, currentRowMousePosition, currentColMousePosition) VALUES (?,?,?,?);";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1,game.getNickname());
            stmt.setInt(2,game.getTotalPoints());
            stmt.setInt(3,game.getCurrentRowMousePosition());
            stmt.setInt(4, game.getCurrentColMousePosition());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * GET info of game from DATABASE GAMEINFO
     * @return
     */
    public GameInfo getGameDB() {
        GameInfo data = null;

        try (Statement stmt = con.createStatement()) {
            String query = "SELECT * FROM GAMEINFO";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                data =  new GameInfo(
                        rs.getString("nickname"),
                        rs.getInt("totalPoints"),
                        rs.getInt("currentRowMousePosition"),
                        rs.getInt("currentColMousePosition"));
            }
            return data;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * INSERT full row INTO database table:DASHBOARD
     * @param nickname
     * @param points
     */
    public void setInfoDashboard(String nickname, int points) {

        String query = "INSERT INTO DASHBOARD (nickname, totalpoints) VALUES (?,?);";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1,nickname);
            stmt.setInt(2,points);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Get ARRAYLIST of DASHBOARD
     * @return ArrayList<Dashboard>
     */
    public ArrayList<Dashboard> getDashboard(){
        ArrayList<Dashboard> dashboardArr = new ArrayList<Dashboard>();

        try (Statement stmt = con.createStatement()) {
            String query = "SELECT * FROM DASHBOARD ORDER BY TOTALPOINTS DESC";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                Dashboard dashboard = new Dashboard(rs.getString("nickname"),
                                                    rs.getInt("totalPoints"));
                dashboardArr.add(dashboard);

            }
            return dashboardArr;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dashboardArr;
    }

    /**
     * Get ANSWER from DataBase QUESTION by ID (param)
     * @param id
     * @return String
     */
    public String getQuestionDB(int id) {
        String question = "";

        String query = "SELECT QUESTION FROM QUESTION WHERE IDQUESTION = ?;";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                question = rs.getString("question");
            }
        return question;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    return question;
    }

    /**
     * Get QUESTION from DataBase QUESTION by ID (param)
     * @param id
     * @return String
     */
    public String getAnswerDB(int id) {
        String question = "";

        String query = "SELECT ANSWER FROM QUESTION WHERE IDQUESTION = ?;";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                question = rs.getString("answer");
            }
            return question;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return question;
    }

    /**
     * Get array of cells
     * @param rows
     * @param cols
     * @return gamecell[][]
     */
    //TODO Repasar porque no funciona
    public GameCell[][] getGameboard(int rows, int cols){
        GameCell[][] gameboard = new GameCell[rows][cols];

        try (Statement stmt = con.createStatement()) {
            String query = "SELECT * FROM CELL";
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()){
                for (int i = 0; i < rows; i++){
                    for (int j = 0; j < cols; j++) {
                        gameboard[i][j] = new GameCell(rs.getInt("points"),
                                rs.getBoolean("isCat"),
                                rs.getBoolean("isCheese"),
                                rs.getBoolean("isDiscovered"),
                                rs.getBoolean("isPropitious"),
                                rs.getBoolean("isUnpropitious"));
                    }
                }
            }
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    System.out.println(gameboard[i][j].isCat());
                }
            }
            return gameboard;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameboard;
    }
}
