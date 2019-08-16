package ca.project.sudokusolver;

import java.util.*;

public class Solver {
    private List<Integer> puzzle;

    public Solver(List<Integer> puzzle) {
        this.puzzle = puzzle;
    }

    // solves the puzzle by brute forcing it
    // does this by holding the indexes of all the positions that
    // need to be filled, filling them with the first available
    // number and backtracking whenever we reach a position
    // that has no solution
    public void solve() {
        boolean working = true;
        // index tracker for the withoutSolution list
        int index = 0;
        List <Integer> withoutSolution = new ArrayList<>();
        for (int i = 0; i < puzzle.size(); i++) {

            // check for every empty square
            if (puzzle.get(i) == 0) {

                // add the index of the empty square to withoutSolution
                if (!withoutSolution.contains(i)) withoutSolution.add(i);

                // get the initial solution
                int solution = getSolution(i, 0);

                puzzle.set(i, solution);

                // check if we could find a solution
                if (solution == 0) working = false;

                // if not update previous solutions until we can
                while (!working) {
                    index--;
                    int pos = withoutSolution.get(index);
                    int tempSolution = getSolution(pos, puzzle.get(pos));
                    puzzle.set(pos, tempSolution);

                    // end when we manage to find a working solution in one
                    // of the previous positions
                    if (tempSolution != 0) {
                        working = true;
                        i = pos;
                    }
                }

                index++;
            }
        }
    }

    // get the solution by checking each number greater than the
    // one given at the position given
    private int getSolution(int pos, int numAtPos) {
        int row, col, box;
        row = pos/9;
        col = pos%9;
        box = (row/3 * 3) + (col/3);
        for (int num = numAtPos + 1; num <= 9; num++){
            // if every check passes return the number otherwise cont.
            boolean unused = true;
            if (unused) unused = checkRow(row, num);
            if (unused) unused = checkCol(col, num);
            if (unused) unused = checkBox(box, num);
            if (unused) {
                return num;
            }
        }

        // if no solution can be found return 0 and backtrack
        return 0;
    }

    // check every number in the row to see if the number is already used
    // if it is return false
    private boolean checkRow(int row, int num) {
        for (int i = row * 9; i < 9 + (row * 9); i++) {
            if (puzzle.get(i) == num) return false;
        }
        return true;
    }

    // check every number in the column to see if the number is already used
    // if it is return false
    private boolean checkCol(int col, int num) {
        for (int i = col; i < 81; i += 9) {
            if (puzzle.get(i) == num) return false;
        }
        return true;
    }

    // check every number in the box to see if the number is already used
    // if it is return false
    private boolean checkBox(int box, int num) {
        int i = (box/3 * 27) + (box%3 * 3);
        int limit = i + 20;
        while (i <= limit) {
            if (puzzle.get(i) == num) return false;
            // check if position in box is at the end column if so
            // go to the next row otherwise go to next column
            if (i % 9 == (box%3 * 3) + 2) i = i + 7;
            else if (i % 8 == 0 && box%3 == 2) i = i + 7;
            else i++;
        }
        return true;
    }
}