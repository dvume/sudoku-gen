package ru.dvume.sudokugen;

public class Main {
    public static void main(String[] args) {
        SudokuBoard s = new SudokuBoard();
        s.fillBoard();
        s.printBoard();
    }
}
