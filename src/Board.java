import java.util.Arrays;
import java.util.Scanner;

public class Board {
    private final int [][] board;
    private int turn;
    private int batmanTurns;
    private Player p1;
    private Player p2;

    public Board() {
        board = new int [6][7];
        turn = 0;
        p1 = new Player(0);
        p2 = new Player(1);
        batmanTurns = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = -1;
            }
        }
    }

    public void printBoard() {
        System.out.println("---------------------------");
        for (int[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (batmanTurns <= 0) {
                    switch (ints[j]) {
                        case 0 -> System.out.print("\u001B[31m[o]\u001B[0m"); // red color
                        case 1 -> System.out.print("\u001B[33m[o]\u001B[0m"); // yellow color
                        default -> System.out.print("[ ]");
                    }
                } else {
                    if (ints[j] == -1) {
                        System.out.print("[ ]");
                    } else {
                        System.out.print("\u001B[30m[o]\u001B[0m");
                    }
                }
            }
            System.out.println();
        }
        printPowerUps();
        System.out.println("\u001B[31mPlayer1's Bank: " + p1.getMoney() + "\u001B[0m");
        System.out.println("\u001B[33mPlayer2's Bank: " + p2.getMoney() + "\u001B[0m");
        System.out.println("It is now " + turnToColor() + "Player" + (turn + 1) + "'s\u001B[0m turn");
        System.out.println("--------------------------");
    }

    public void printPowerUps() {
        System.out.println("---------------------------");
        System.out.print("\u001B[31mPlayer1's powerups: \u001B[0m");
        for (int i = 0; i < p1.getPowers().size(); i++) {
            System.out.print(p1.getPowers().get(i).getType() + " ");
        }
        System.out.println();
        System.out.print("\u001B[33mPlayer2's powerups: \u001B[0m");
        for (int i = 0; i < p2.getPowers().size(); i++) {
            System.out.print(p2.getPowers().get(i).getType() + " ");
        }
        System.out.println();
        System.out.println("---------------------------");
    }

    public void printShop() {
        System.out.println("--------------------------");
        System.out.println("\u001B[30m--1--BlackoutBatman--\u001B[0m Changes all pieces to black for 6 total turns - $500");
        System.out.println("\u001B[36m--2--AerialAssault--\u001B[0m Remove an entire column of a player's choosing - $250");
        System.out.println("\u001B[32m--3--JokersBulldozer--\u001B[0m Remove an entire row of a player's choosing - $250");
        System.out.println("Please type the corresponding number to the PowerUp you would like to purchase. To exit the shop, type 'exitshop'");
        System.out.println("--------------------------");
    }

    public void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = -1;
            }
        }
    }

    public void reset() {
        clearBoard();
        turn = 0;
        p1 = new Player(0);
        p2 = new Player(1);

        System.out.println("---------------------------");
        System.out.println("Thanks for playing! The board and all powerups have been cleared");
        System.out.println("---------------------------");
    }

    public boolean isBoardFull() {
        for (int[] ints : board) {
            for (int j = 0; j < board[0].length; j++) {
                if (ints[j] == -1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameWon() {
        boolean won = false;
        //checks for row and diagonal wins
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] != -1 && board[r][c] == board[r][c+1] && board[r][c+1] == board[r][c+2] && board[r][c+2] == board[r][c+3]) {
                    won = true;
                }
                else if (board[r][c] != -1 && r < 3 && board[r][c] == board[r+1][c+1] && board[r+1][c+1] == board[r+2][c+2] && board[r+2][c+2] == board[r+3][c+3]) {
                    won = true;
                } else if (board[r][c] != -1 && r >= 3 && board[r][c] == board[r-1][c+1] && board[r-1][c+1] == board[r-2][c+2] && board[r-2][c+2] == board[r-3][c+3]) {
                    won = true;
                }
            }
        }

        //checks for column win
        if (!won) {
            for (int c = 0; c < 7; c++) {
                for (int r = 0; r < 3; r++) {
                    if (board[r][c] != -1 && board[r][c] == board[r + 1][c] && board[r + 1][c] == board[r + 2][c] && board[r + 2][c] == board[r + 3][c]) {
                        won = true;
                        break;
                    }
                }
            }
        }

        if (won) {
            System.out.println("---------------------------");
            System.out.println("Congratulations! Player " + turn + " won!");
            System.out.println("---------------------------");
            reset();
            return true;
        }

        return false;
    }


    public boolean move(int column) { // column goes from 1-7 left-right
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i][column - 1] == -1) {
                board[i][column - 1] = turnToPlayer().getPlayerNum();
                turnToPlayer().addMoney(50);
                batmanTurns--;
                if (turn == 0) {turn = 1;}
                else if (turn == 1) {turn = 0;}
                return true;
            }
        }
        return false;
    }

    public void blackoutBatman() {
        turnToPlayer().addMoney(-500);
        batmanTurns = 6;
        turnToPlayer().usePowerup("blackoutbatman");
    }

    public void aerialAssault(int col) {
        turnToPlayer().addMoney(-250);
        for(int i = 0; i < board.length; i++) {
            board[i][col - 1] = -1;
        }
        turnToPlayer().usePowerup("aerialassault");
    }

    public void jokersBulldozer(int row){
        turnToPlayer().addMoney(-250);
        if (row != 6) {
            for (int i = 6 - row; i > 0; i--) {
                System.arraycopy(board[i - 1], 0, board[i], 0, board[0].length);
            }
        }
        Arrays.fill(board[0], -1);
        turnToPlayer().usePowerup("jokersbulldozer");
    }

    public Player turnToPlayer() {
        if (turn == 0) {
            return p1;
        }
        return p2;
    }

    public String turnToColor() {
        if (turn == 0) {
            return "\u001B[31m";
        }
        return "\u001B[33m";
    }

    public void play() {
        System.out.println("---------------------------");
        System.out.println("The rules of Super Connect 4 are as follows:");
        System.out.println("The game will end when there is a four-in-a-row or a stalemate.");
        System.out.println("Players will take turns placing pieces one at a time with an objective to connect 4 of their pieces in a row, whether diagonal, horizontal, or vertical.");
        System.out.println("Turns will commence as the following: Player 1 (red) will move first followed by Player 2 (yellow). After a player moves, their turn is over.");
        System.out.println("To place your piece, type in 'move', click enter, and then type which column you want to place the piece in. The columns are numbered 1-7, from left to right");
        System.out.println("Super Connect 4 also has powers");
        System.out.println("To open the shop, type 'shop,' and to exit the game, type 'exit'");
        System.out.println("To see your powers, type 'powers'. To use a power, type 'usepower'");
        System.out.println("Have Fun!");
        System.out.println("---------------------------");
        Scanner in = new Scanner(System.in);


        Board board = new Board();
        printBoard();
        //each loop of outer while loop represents 1 turn
        while (!isGameWon() && !isBoardFull()) {
            String input = in.nextLine();
            //each loop of inner while loop represents what the player does DURING their turn
            while (!input.equals("move")) {
                switch (input) {
                    case "exit" -> {
                        reset();
                        System.exit(0);
                    }
                    case "powers" -> printPowerUps();
                    case "shop" -> {
                        printShop();
                        String nextIn = in.nextLine();
                        while (!nextIn.equals("exitshop")) {
                            switch (nextIn) {
                                case "1" -> {
                                    if (turnToPlayer().getMoney() < 500) {
                                        System.out.println("---------------------------");
                                        System.out.println("You don't have enough money for this powerup");
                                        System.out.println("---------------------------");
                                        break;
                                    }
                                    turnToPlayer().addPowerup("blackoutbatman");
                                    System.out.println("---------------------------");
                                    System.out.println("You have successfully purchased blackoutbatman");
                                    System.out.println("Type 'exitshop' to exit the shop");
                                    System.out.println("---------------------------");
                                }
                                case "2" -> {
                                    if (turnToPlayer().getMoney() < 250) {
                                        System.out.println("---------------------------");
                                        System.out.println("You don't have enough money for this powerup");
                                        System.out.println("---------------------------");
                                        break;
                                    }
                                    turnToPlayer().addPowerup("aerialassault");
                                    System.out.println("---------------------------");
                                    System.out.println("You have successfully purchased aerialassault");
                                    System.out.println("Type 'exitshop' to exit the shop");
                                    System.out.println("---------------------------");
                                }
                                case "3" -> {
                                    if (turnToPlayer().getMoney() < 250) {
                                        System.out.println("---------------------------");
                                        System.out.println("You don't have enough money for this powerup");
                                        System.out.println("---------------------------");
                                        break;
                                    }
                                    turnToPlayer().addPowerup("jokersbulldozer");
                                    System.out.println("---------------------------");
                                    System.out.println("You have successfully purchased jokersbulldozer");
                                    System.out.println("Type 'exitshop' to exit the shop");
                                    System.out.println("---------------------------");
                                }
                                default -> {
                                    System.out.println("---------------------------");
                                    System.out.println("Please enter a valid input");
                                    System.out.println("---------------------------");
                                }
                            }
                            nextIn = in.nextLine();
                        }
                        System.out.println("---------------------------");
                        System.out.println("You have left the shop");
                        System.out.println("---------------------------");
                    }
                    case "usepower" -> {
                        System.out.println("---------------------------");
                        System.out.println("Which type of powerup would you like to use?");
                        System.out.println("---------------------------");
                        String power = in.nextLine().toLowerCase();
                        while (!power.equals("blackoutbatman") && !power.equals("aerialassault") && !power.equals("jokersbulldozer")) {
                            System.out.println("---------------------------");
                            System.out.println("Please enter a valid powerup (blackoutbatman, aerialassault, jokersbulldozer)");
                            System.out.println("---------------------------");
                            power = in.nextLine().toLowerCase();
                        }
                        switch (power) {
                            case "blackoutbatman" -> {
                                if (!turnToPlayer().containsPower("blackoutbatman")) {
                                    System.out.println("---------------------------");
                                    System.out.println("You don't own this powerup");
                                    System.out.println("---------------------------");
                                    break;
                                }
                                blackoutBatman();
                                System.out.println("---------------------------");
                                System.out.println("Blackoutbatman will be active for the next 6 moves");
                                System.out.println("---------------------------");
                                printBoard();
                            }
                            case "aerialassault" -> {
                                if (!turnToPlayer().containsPower("aerialassault")) {
                                    System.out.println("---------------------------");
                                    System.out.println("You don't own this powerup");
                                    System.out.println("---------------------------");
                                    break;
                                }
                                System.out.println("---------------------------");
                                System.out.println("Which column would you like to bomb?");
                                System.out.println("---------------------------");
                                String column = in.nextLine();
                                while (!column.equals("1") && !column.equals("2") && !column.equals("3") && !column.equals("4") && !column.equals("5") && !column.equals("6") && !column.equals("7")) {
                                    System.out.println("---------------------------");
                                    System.out.println("Please enter a valid column number");
                                    System.out.println("---------------------------");
                                    column = in.nextLine();
                                }
                                aerialAssault(Integer.parseInt(column));
                                System.out.println("---------------------------");
                                System.out.println("You have successfully bombed column " + column);
                                System.out.println("---------------------------");
                                printBoard();
                            }
                            case "jokersbulldozer" -> {
                                if (!turnToPlayer().containsPower("jokersbulldozer")) {
                                    System.out.println("---------------------------");
                                    System.out.println("You don't own this powerup");
                                    System.out.println("---------------------------");
                                    break;
                                }
                                System.out.println("---------------------------");
                                System.out.println("Which row would you like to bulldoze?");
                                System.out.println("---------------------------");
                                String row = in.nextLine();
                                while (!row.equals("1") && !row.equals("2") && !row.equals("3") && !row.equals("4") && !row.equals("5") && !row.equals("6")) {
                                    System.out.println("---------------------------");
                                    System.out.println("Please enter a valid column number");
                                    System.out.println("---------------------------");
                                    row = in.nextLine();
                                }
                                jokersBulldozer(Integer.parseInt(row));
                                System.out.println("---------------------------");
                                System.out.println("You have successfully bulldozed row " + row);
                                System.out.println("---------------------------");
                                printBoard();
                            }
                        }
                    }
                    default -> {
                        System.out.println("---------------------------");
                        System.out.println("Please enter a valid value");
                        System.out.println("---------------------------");
                    }
                }
                input = in.nextLine();
            }

            System.out.println("---------------------------");
            System.out.println("Which column would you like your piece to go in?");
            System.out.println("---------------------------");
            String move = in.nextLine();
            while (!isValidMove(board, move)) {
                System.out.println("---------------------------");
                System.out.println("Please enter a valid column number");
                System.out.println("---------------------------");
                move = in.nextLine();
            }
            printBoard();
        }
    }

    public boolean isValidMove(Board board, String move) {
        if (!move.equals("1") && !move.equals("2") && !move.equals("3") && !move.equals("4") && !move.equals("5") && !move.equals("6") && !move.equals("7")) {
            return false;
        }
        return move(Integer.parseInt(move));
    }
}
