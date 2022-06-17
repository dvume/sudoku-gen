package ru.dvume.sudokugen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class SudokuBoard {
    private final int BOARD_SIZE = 9;
    private final int[][] board = new int[BOARD_SIZE][BOARD_SIZE];
    private final ArrayList<CheckArrayList<Integer>> variants = new ArrayList<>();
    private final ArrayList<CheckArrayList<Integer>> backupVariants = new ArrayList<>();

    private void removeVariants(int index, int value) {
        variants.remove(index);
        for (ArrayList<Integer> lst : variants) {
            lst.remove((Integer) value);
        }
    }

    private void resetVariants() {
        variants.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            variants.add(new CheckArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), i));
        }
    }

    private void removeNumbersBySquareCheck(int lineIndex, int squareColumn) {
        int lineLow = lineIndex / 3 * 3;
        int lineHigh = lineLow + 3;
        int colHigh = squareColumn * 3;
        int colLow = colHigh - 3;

        for (int i = lineLow; i < lineHigh; i++) {
            for (int j = colLow; j < colHigh; j++) {
                if (board[i][j] != 0) {
                    for (int k = colLow; k < colHigh; k++) {
                        variants.get(k).remove((Integer) board[i][j]);
                    }
                }
            }
        }
    }

    private void removeNumbersByColumnCheck(int lineIndex) {
        for (int i = 0; i <= lineIndex; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] != 0) {
                    variants.get(j).remove((Integer) board[i][j]);
                }
            }
        }
    }

    private void removeExistingNumbers(int lineIndex) {
        removeNumbersBySquareCheck(lineIndex, 1);
        removeNumbersBySquareCheck(lineIndex, 2);
        removeNumbersBySquareCheck(lineIndex, 3);
        removeNumbersByColumnCheck(lineIndex);
    }

    private void prepareVariants(int lineIndex) {
        resetVariants();
        removeExistingNumbers(lineIndex);
    }

    private boolean existEmptyVariant() {
        for (CheckArrayList<Integer> variant : variants) {
            if (variant.size() == 0) {
                return true;
            }
        }
        return false;
    }

    private void clearBoardLine(int lineIndex) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[lineIndex][i] = 0;
        }
    }

    private void rollbackVariants(int lineIndex) {
        variants.clear();
        variants.addAll(backupVariants);
        clearBoardLine(lineIndex);
    }

    private void fillBackup() {
        backupVariants.clear();
        for (CheckArrayList<Integer> variant : variants) {
            CheckArrayList<Integer> l = new CheckArrayList<>();
            l.boardNum = variant.boardNum;
            l.addAll(variant);
            backupVariants.add(l);
        }
    }

    public void fillBoard() {
        Random rand = new Random();

        for (int i = 0; i < BOARD_SIZE; i++) {
            prepareVariants(i);
            if (existEmptyVariant()) {
                i--;
                rollbackVariants(i);
            }
            fillBackup();

            //Заполняем линию всеми цифрами
            while (variants.size() > 0) {
                int varIndex = 0;
                int min = BOARD_SIZE + 1;
                int num = 0;

                //проверяем, есть ли ячейка, в которой допустимо поставить только одно число.
                for (int j = 0; j < variants.size(); j++) {
                    if (variants.get(j).size() == 1) {
                        num = variants.get(j).get(0);
                        board[i][variants.get(j).boardNum] = num;
                        min = variants.get(j).size();
                        removeVariants(j, num);

                        if (existEmptyVariant()) {
                            rollbackVariants(i);
                        }
                        break;
                    }
                }

                //Если такая ячейка была найдена, по переходим к заполнению следующих ячеек.
                if (min == 1) {
                    continue;
                }

                min = BOARD_SIZE + 1;

                //массив хранящий кол-во повторений каждой из цифр.
                int[] numCount = new int[BOARD_SIZE];
                Arrays.fill(numCount, 0);

                //Проходим по всем оставшимся ячейкам и всем вариантам для этих ячеек
                // и заполняем массив повторений.
                for (CheckArrayList<Integer> variant : variants) {
                    for (Integer integer : variant) {
                        numCount[integer - 1]++;
                    }
                }

                CheckArrayList<Integer> n = new CheckArrayList<Integer>();

                //Определяем список цифр, который повторялись, наименьшее кол-во раз.
                for (int j = 0; j < numCount.length; j++) {
                    if (numCount[j] == min) {
                        n.add(j + 1);
                    } else if (numCount[j] != 0 && min > numCount[j]) {
                        n.clear();
                        n.add(j + 1);
                        min = numCount[j];
                    }
                }

                num = n.get(rand.nextInt(n.size()));
                n.clear();
                for (int ii = 0; ii < variants.size(); ii++) {
                    for (int j = 0; j < variants.get(ii).size(); j++) {
                        if (variants.get(ii).get(j) == num) {
                            n.add(ii);
                            break;
                        }
                    }
                }

                varIndex = n.get(rand.nextInt(n.size()));
                board[i][variants.get(varIndex).boardNum] = num;
                removeVariants(varIndex, num);
                if (existEmptyVariant()) {
                    rollbackVariants(i);
                }
            }
        }
    }

    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
                if ((j + 1) % 3 == 0) {
                    System.out.print(" ");
                }
            }
            System.out.println();
            if ((i + 1) % 3 == 0) {
                System.out.println();
            }
        }
    }
}
