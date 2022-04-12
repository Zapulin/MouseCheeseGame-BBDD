package Zstudio.model;

public class GameInfo {
    private String nickname;
    private int totalPoints;
    private int currentRowMousePosition;
    private int currentColMousePosition;

    public GameInfo(String nickname, int totalPoints, int currentRowMousePosition, int currentColMousePosition) {
        this.nickname = nickname;
        this.totalPoints = totalPoints;
        this.currentRowMousePosition = currentRowMousePosition;
        this.currentColMousePosition = currentColMousePosition;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public void setCurrentRowMousePosition(int currentRowMousePosition) {
        this.currentRowMousePosition = currentRowMousePosition;
    }

    public void setCurrentColMousePosition(int currentColMousePosition) {
        this.currentColMousePosition = currentColMousePosition;
    }

    public String getNickname() {
        return nickname;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getCurrentRowMousePosition() {
        return currentRowMousePosition;
    }

    public int getCurrentColMousePosition() {
        return currentColMousePosition;
    }
}
