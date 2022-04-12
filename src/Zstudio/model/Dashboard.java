package Zstudio.model;

public class Dashboard {
    private String nickname;
    private int points;

    public Dashboard(String nickname, int points) {
        this.nickname = nickname;
        this.points = points;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPoints() {
        return points;
    }
}
