public class StandardBus extends Bus{
    /**
     * Represents a standard bus with 2+2 seating arrangement.
     */
    private final double refundCut;
    private final int seatNum;
    private String[][] seats;
    /**
     * Constructor to initialize a standard bus.
     *
     * @param id         The ID of the bus
     * @param from       The starting point of the voyage
     * @param to         The destination of the voyage
     * @param totalRows  The total number of rows in the bus
     * @param price      The price of each seat
     * @param refundCut  The refund cut percentage
     */
    public StandardBus(String id, String from, String to, String totalRows, String price, String refundCut){
        super(id, from, to, price, totalRows);
        this.refundCut = (double) Integer.parseInt(refundCut) /100;
        this.seatNum= 4 * Integer.parseInt(totalRows);
        this.seats = new String[Integer.parseInt(totalRows)][5];
        for (int i = 0; i < Integer.parseInt(totalRows); i++){
            for (int j = 0; j < 4; j++){
                seats[i][j] = "*";
            }
        }
    }
    /**
     * Method to display the seats of the standard bus.
     *
     * @param args Array containing command line arguments
     */
    public void showSeats(String[] args){
        for (int i = 0; i < seats.length; i++){
            for (int j = 0; j < seats[0].length - 1; j++){
                // Adding a separator between the second and third seats
                if (j == 2){
                    FileIO.writeToFile(args[1], "| ", true, false);
                }
                // Writing the seat status
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
    public String[][] getSeats() {
        return seats;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public double getRefundCut() {
        return refundCut;
    }
}