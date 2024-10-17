public abstract class Bus {
    /**
     * Abstract class representing a bus.
     * Has getters for common attributes of different types of bus.
     * Subclasses implement methods for seat infos and displaying seats.
     */
    private final int id;
    private final String from;
    private final String to;
    private final double price;
    private final int totalRows;
    private double revenue;

    public Bus(String id, String from, String to, String price, String totalRows){
        this.id = Integer.parseInt(id);
        this.from = from;
        this.to = to;
        this.price = Double.parseDouble(price);
        this.totalRows = Integer.parseInt(totalRows);
        this.revenue = 0;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public double getPrice() {
        return price;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public double getRevenue() {
        return revenue;
    }

    public void addRevenue(double money) {
        this.revenue += money;
    }

    public abstract String[][] getSeats();
    public abstract void showSeats(String[] args);
    public abstract double getRefundCut();
}