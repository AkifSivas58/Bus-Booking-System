public class Minibus extends Bus{
    /**
     * Represents a minibus as a type of bus.
     */
    private final int seatNum;
    private String[][] seats;
    /**
     * Constructs a Minibus object.
     *
     * @param id         ID of the minibus
     * @param from       Departure location
     * @param to         Destination location
     * @param totalRows  Total number of rows of seats
     * @param price      Price of the minibus
     */
    public Minibus(String id, String from, String to, String totalRows, String price){
        super(id, from, to, price, totalRows);
        this.seatNum = 2 * Integer.parseInt(totalRows);
        this.seats = new String[Integer.parseInt(totalRows)][2];
        for (int i = 0; i < Integer.parseInt(totalRows); i++){
            for (int j = 0; j < 2; j++){
                seats[i][j] = "*";
            }
        }
    }
    /**
     * Displays the seats of the minibus.
     *
     * @param args Command line arguments
     */
    public void showSeats(String[] args){
        for (int i = 0; i < seats.length; i++){
            for (int j = 0; j < seats[0].length; j++){
                if (j+1 == seats[0].length){
                    FileIO.writeToFile(args[1], seats[i][j], true, false);
                }
                else {
                    FileIO.writeToFile(args[1], seats[i][j] + " ", true, false);
                }
            }
            FileIO.writeToFile(args[1],  "", true, true);
        }
    }
    public String[][] getSeats() {
        return seats;
    }

    public double getRefundCut() {
        return 0;
    }

    public int getSeatNum() {
        return seatNum;
    }
}