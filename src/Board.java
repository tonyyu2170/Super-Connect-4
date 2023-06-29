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
        String [] text = {"\u001B[31mPlayer1's Bank: " + p1.getMoney() + "\u001B[0m", "\u001B[33mPlayer2's Bank: " + p2.getMoney() + "\u001B[0m", "It is now " + turnToColor() + "Player" + (turn + 1) + "'s\u001B[0m turn"};
        fPrint(text);
    }

    public void printPowerUps() {
        String p1Powers = "";
        for (int i = 0; i < p1.getPowers().size(); i++) {
            p1Powers += p1.getPowers().get(i).getType() + " ";
        }
        String p2Powers = "";
        for (int i = 0; i < p2.getPowers().size(); i++) {
            p2Powers += p2.getPowers().get(i).getType() + " ";
        }
        String [] text = {"\u001B[31mPlayer1's powerups: \u001B[0m" + p1Powers, "\u001B[33mPlayer2's powerups: \u001B[0m" + p2Powers};
        fPrint(text);
    }

    public void printShop() {
        String [] text = {"\u001B[30m--1--BlackoutBatman--\u001B[0m Changes all pieces to black for 6 total turns - $500", "\u001B[36m--2--AerialAssault--\u001B[0m Remove an entire column of a player's choosing - $250", "\u001B[32m--3--JokersBulldozer--\u001B[0m Remove an entire row of a player's choosing - $250", "Please type the corresponding number to the PowerUp you would like to purchase. To exit the shop, type 'exitshop'"};
        fPrint(text);
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
        fPrint("Thanks for playing! The board and all powerups have been cleared");
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

    public boolean isGameOver() {
        boolean won = false;
        //checks for row and diagonal wins
        for (int r = 0; r < 6; r++) {
            for (int c = 0; c < 4; c++) {
                if (board[r][c] != -1 && board[r][c] == board[r][c+1] && board[r][c+1] == board[r][c+2] && board[r][c+2] == board[r][c+3]) {
                    won = true;
                    break;
                }
                else if (board[r][c] != -1 && r < 3 && board[r][c] == board[r+1][c+1] && board[r+1][c+1] == board[r+2][c+2] && board[r+2][c+2] == board[r+3][c+3]) {
                    won = true;
                    break;
                } else if (board[r][c] != -1 && r >= 3 && board[r][c] == board[r-1][c+1] && board[r-1][c+1] == board[r-2][c+2] && board[r-2][c+2] == board[r-3][c+3]) {
                    won = true;
                    break;
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
            fPrint("Congratulations! Player " + turn + " won!");
            reset();
            return true;
        } else if (isBoardFull()) {
            fPrint("Its a tie!");
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
        batmanTurns = 6;
        turnToPlayer().usePowerup("blackoutbatman");
    }

    public void aerialAssault(int col) {
        for(int i = 0; i < board.length; i++) {
            board[i][col - 1] = -1;
        }
        turnToPlayer().usePowerup("aerialassault");
    }

    public void jokersBulldozer(int row){
        if (row != 6) {
            for (int i = 6 - row; i > 0; i--) {
                System.arraycopy(board[i - 1], 0, board[i], 0, board[0].length);
            }
        }
        Arrays.fill(board[0], -1);
        turnToPlayer().usePowerup("jokersbulldozer");
    }

    public void play() {
        String [] text = {"The rules of Super Connect 4 are as follows:", "The game will end when there is a four-in-a-row or a stalemate", "Players will take turns placing pieces one at a time with an objective to connect 4 of their pieces in a row, whether diagonal, horizontal, or vertical", "Turns will commence as the following: Player 1 (red) will move first followed by Player 2 (yellow). After a player moves, their turn is over", "To place your piece, type in 'move', click enter, and then type which column you want to place the piece in. The columns are numbered 1-7, from left to right", "Super Connect 4 also has powers", "To open the shop, type 'shop,' and to exit the game, type 'exit'", "To see your powers, type 'powers'. To use a power, type 'usepower'", "Have Fun!"};
        fPrint(text);
        Scanner in = new Scanner(System.in);


        Board board = new Board();
        printBoard();
        //each loop of outer while loop represents 1 turn
        while (!isGameOver()) {
            String input = in.nextLine();
            //each loop of inner while loop represents what the player does DURING their turn
            while (!input.equals("move")) {
                Player p = turnToPlayer();
                switch (input) {

                    case "exit" -> {
                        reset();
                        System.exit(0);
                    }

                    case "powers" -> {
                        printPowerUps();
                        break;
                    }

                    case "shop" -> {
                        printShop();
                        String nextIn = in.nextLine();
                        while (!nextIn.equals("exitshop")) {
                            switch (nextIn) {

                                case "1" -> {
                                    if (p.getMoney() < 500) {
                                        fPrint("You need " + (500 - p.getMoney()) + " more gold to purchase blackoutbatman");
                                        break;
                                    }
                                    p.addPowerup("blackoutbatman");
                                    String [] t = {"You have successfully purchased blackoutbatman", "You now have " + p.getMoney() + " gold remaining", "Type 'exitshop' to exit the shop"};
                                    fPrint(t);
                                    break;
                                }

                                case "2" -> {
                                    if (p.getMoney() < 250) {
                                        fPrint("You need " + (250 - p.getMoney()) + " more gold to purchase aerialassault");
                                        break;
                                    }
                                    p.addPowerup("aerialassault");
                                    String [] t = {"You have successfully purchased aerialassault", "You now have " + p.getMoney() + " gold remaining", "Type 'exitshop' to exit the shop"};
                                    fPrint(t);
                                    break;
                                }

                                case "3" -> {
                                    if (p.getMoney() < 250) {
                                        fPrint("You need " + (250 - p.getMoney()) + " more gold to purchase jokersbulldozer");
                                        break;
                                    }
                                    p.addPowerup("jokersbulldozer");
                                    String [] t = {"You have successfully purchased jokersbulldozer", "You now have " + p.getMoney() + " gold remaining", "Type 'exitshop' to exit the shop"};
                                    fPrint(t);
                                    break;
                                }
                                default -> {
                                    fPrint("Please enter a valid input (1-3)");
                                }

                            }
                            nextIn = in.nextLine();
                        }
                        fPrint("You have left the shop");
                        break;
                    }

                    case "usepower" -> {
                        fPrint("Which power would you like to use? ('blackoutbatman' 'aerialassault' 'jokersbulldozer')");
                        String power = in.nextLine().toLowerCase();

                        while (!power.equals("blackoutbatman") && !power.equals("aerialassault") && !power.equals("jokersbulldozer")) {
                            fPrint("Please enter a valid powerup ('blackoutbatman' 'aerialassault' 'jokersbulldozer')");
                            power = in.nextLine().toLowerCase();
                        }

                        switch (power) {

                            case "blackoutbatman" -> {
                                if (!p.containsPower("blackoutbatman")) {
                                    fPrint("You don't own blackoutbatman");
                                    break;
                                }
                                blackoutBatman();
                                fPrint("Blackoutbatman will be active for the next 6 moves");
                                printBoard();
                                break;
                            }

                            case "aerialassault" -> {
                                if (!p.containsPower("aerialassault")) {
                                    fPrint("You don't own aerialassault");
                                    break;
                                }
                                printBoard();
                                fPrint("Which column would you like to bomb? (1-7)");
                                String column = in.nextLine();
                                while (!column.equals("1") && !column.equals("2") && !column.equals("3") && !column.equals("4") && !column.equals("5") && !column.equals("6") && !column.equals("7")) {
                                    fPrint("Please enter a valid column number (1-7)");
                                    column = in.nextLine();
                                }
                                aerialAssault(Integer.parseInt(column));
                                fPrint("You have successfully bombed column " + column);
                                printBoard();
                                break;
                            }

                            case "jokersbulldozer" -> {
                                if (!p.containsPower("jokersbulldozer")) {
                                    fPrint("You don't own jokersbulldozer");
                                    break;
                                }
                                printBoard();
                                fPrint("Which row would you like to bulldoze? (1-6)");
                                String row = in.nextLine();
                                while (!row.equals("1") && !row.equals("2") && !row.equals("3") && !row.equals("4") && !row.equals("5") && !row.equals("6")) {
                                    fPrint("Please enter a valid row number (1-6)");
                                    row = in.nextLine();
                                }
                                jokersBulldozer(Integer.parseInt(row));
                                fPrint("You have successfully bulldozed row " + row);
                                printBoard();
                                break;
                            }

                        }
                        break;
                    }

                    default -> {
                        fPrint("Please enter a valid value ('move' 'exit' 'powers' 'shop' 'usepower')") ;
                    }

                }
                input = in.nextLine();
            }

            fPrint("Which column would you like your piece to go in? (1-7)");
            String move = in.nextLine();
            while (!isValidMove(board, move)) {
                fPrint("Please enter a valid column number (1-7)");
                move = in.nextLine();
            }
            printBoard();
        }
    }

    private boolean isValidMove(Board board, String move) {
        if (!move.equals("1") && !move.equals("2") && !move.equals("3") && !move.equals("4") && !move.equals("5") && !move.equals("6") && !move.equals("7")) {
            return false;
        }
        return move(Integer.parseInt(move));
    }

    private Player turnToPlayer() {
        if (turn == 0) {
            return p1;
        }
        return p2;
    }

    private String turnToColor() {
        if (turn == 0) {
            return "\u001B[31m";
        }
        return "\u001B[33m";
    }

    private void fPrint(String text) {
        System.out.println("-----------------------------------------");
        System.out.println(text);
        System.out.println("-----------------------------------------");
    }

    private void fPrint(String [] text) {
        System.out.println("-----------------------------------------");
        for (int i = 0; i < text.length; i++) {
            System.out.println(text[i]);
        }
        System.out.println("-----------------------------------------");
    }
}