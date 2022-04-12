package Zstudio.model;

public class GameCell {

    private int points;
    private boolean isCat;
    private boolean isCheese;
    private boolean isDiscovered;
    private boolean isPropitious;
    private boolean isUnpropitious;


    public GameCell(int points, boolean isCat, boolean isCheese, boolean isDiscovered, boolean isPropitious,boolean isUnpropitious) {
        this.points = points;
        this.isCat = isCat;
        this.isCheese = isCheese;
        this.isDiscovered = isDiscovered;
        this.isPropitious = isPropitious;
        this.isUnpropitious = isUnpropitious;
    }

    public GameCell() {
        this.points = (int) ((Math.random()*3)+1) * 10;
    }


    public int getPoints() {
        return points;
    }

    public boolean isCat() {
        return isCat;
    }

    public boolean isPropitious() {
        return isPropitious;
    }

    public boolean isUnpropitious() {
        return isUnpropitious;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }

    public void setDiscovered() {

        isDiscovered = true;
    }

    @Override
    public String toString() {
        if(isCheese)
            return "CH";
        if(isCat)
            return "CC";
        if(isPropitious)
            return "++";
        if(isUnpropitious)
            return "--";
        else
            return String.format("%d",points);
    }

}
