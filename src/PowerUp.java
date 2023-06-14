public class PowerUp {
    private final int cost;
    private final String type;

    public PowerUp() {
        cost = -1;
        type = "";
    }

    public PowerUp(String type) {
        this.type = type.toLowerCase();
        if (type.equals("blackoutbatman")) {
            cost = 500;
        } else {
            cost = 250;
        }
    }

    public int getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }
}
