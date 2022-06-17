package ru.dvume.sudokugen;

import ru.dvume.sudokugen.gen.SudokuBoardImpl;

public class Main {
    public static void main(String[] args) {
        SudokuBoardImpl s = new SudokuBoardImpl();
        s.fillBoard();
        s.printBoard();
    }
}
