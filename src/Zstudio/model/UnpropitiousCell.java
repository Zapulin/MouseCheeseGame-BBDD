package Zstudio.model;

public class UnpropitiousCell extends GameCell implements Questionable {

    @Override
    public String getQuestion() {
        return "Inserte un número entre 1 and 3, si no lo adivina, perderà 50 puntos:";
    }

    @Override
    public boolean submitAnswer(String answer) {
        int intValue = 0;
        try {
            intValue = Integer.parseInt(answer);
        } catch (NumberFormatException e) {
            return false;
        }
        int badLuckNumber = (int) (Math.random()*3)+1;
        return intValue == badLuckNumber;
    }

    @Override
    public String toString() {
        return "--";
    }
}
