public class PremiumBus extends Bus{
    /**
     * Represents a premium bus with 1+2 seating arrangement.
     */
    private final double refundCut;
    private final int premiumRate;
    private final double premPrice;
    private String[][] seats;
    /**
     * Constructs a PremiumBus object.
     *
     * @param id           ID of the premium bus
     * @param from         Departure location
     * @param to           Destination location
     * @param totalRows    Total number of rows of seats
     * @param price        Base price of the premium bus
     * @param refundCut    Refund cut percentage
     * @param premiumRate  Premium rate percentage
     */
    public PremiumBus(String id, String from, String to, String totalRows, String price, String refundCut, String premiumRate){
        super(id, from, to, price, totalRows);
        this.refundCut = (double) Integer.parseInt(refundCut) /100;
        this.premiumRate = Integer.parseInt(premiumRate);
        this.seats = new String[Integer.parseInt(totalRows)][4];
        this.premPrice = this.getPrice() + this.getPrice() * this.premiumRate/100;

        for (int i = 0; i < Integer.parseInt(totalRows); i++){
            for (int j = 0; j < 3; j++){
                seats[i][j] = "*";
            }
        }
    }

    public double getPremPrice() {
        return premPrice;
    }

    public String[][] getSeats() {
        return seats;
    }

    /**
     * Displays the seats of the premium bus.
     *
     * @param args Command line arguments
     */
    public void showSeats(String[] args){
        for (int i = 0; i < seats.length; i++){
            for (int j = 0; j < seats[0].length - 1; j++){
                if (j == 1){
                    FileIO.writeToFile(args[1], "| ", true, false);
                }

                if (j+1 == seats[0].length - 1){
                    FileIO.writeToFile(args[1], seats[i][j], true, false);
                }
                else {
                    FileIO.writeToFile(args[1], seats[i][j] + " ", true, false);
                }
            }
            FileIO.writeToFile(args[1],  "", true, true);
        }
    }
    public double getRefundCut() {
        return refundCut;
    }
}