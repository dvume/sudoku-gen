package ru.dvume.sudokugen;

import java.util.ArrayList;
import java.util.Collection;

public class CheckArrayList<T> extends ArrayList<T> {
    public int boardNum = 0;
    public CheckArrayList() {
        super();
    }

    public CheckArrayList(Collection<? extends T> c, int boardNum) {
        super(c);
        this.boardNum = boardNum;
    }
}