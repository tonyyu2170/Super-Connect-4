import java.util.ArrayList;

public class Player {
    private int money;
    private final ArrayList<PowerUp> powers;
    private final int playerNum;

    public Player(int playerNum) {
        money = 0;
        powers = new ArrayList<PowerUp>();
        this.playerNum = playerNum;
    }

    public void addPowerup(String type) {
        powers.add(new PowerUp(type));
        money -= powers.get(powers.size() - 1).getCost();
    }

    public void usePowerup(String type) {
        for (int i = 0; i < powers.size(); i++) {
            if (powers.get(i).getType().equals(type)) {
                powers.remove(i);
                break;
            }
        }
    }

    public ArrayList<PowerUp> getPowers() {
        return powers;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public int getMoney() {
        return money;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public boolean containsPower(String type) {
        for (PowerUp power : powers) {
            if (type.equals(power.getType())) {
                return true;
            }
        }
        return false;
    }
}
