import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class RefundTicketCommand implements Command{
    /**
     * Represents a command to refund tickets for a voyage.
     */
    static DecimalFormat df;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.00", symbols);
    }
    /**
     * Executes the refund ticket command.
     *
     * @param commandInfos   Array containing command information
     * @param args           Array containing command line arguments
     * @param voyages        HashMap containing all voyages
     * @param isLastCommand  Indicates if this is the last command in the list
     */
    @Override
    public void execute(String[] commandInfos, String[] args, HashMap<Integer, Bus> voyages, boolean isLastCommand) {
        // Checker if command used properly.
        if (commandInfos.length != 3){
            FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"REFUND_TICKET\" command!", true, true);
            return;
        }
        // Validating ID format.
        try{
            Integer.parseInt(commandInfos[1]);
        }catch (Exception e){
            FileIO.writeToFile(args[1], "ERROR: " + commandInfos[1] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return;
        }

        int id = Integer.parseInt(commandInfos[1]);
        String[] toRefund = commandInfos[2].split("_");

        if (voyages.containsKey(id)){
            Bus voyage = voyages.get(id);
            String[][] seats = voyage.getSeats();
            double refunded = 0;
            // This loop checks if any problem occur before refunding a ticket.
            for (String string : toRefund) {
                // Validating seat number format.
                try{
                    Integer.parseInt(string);
                }catch(Exception e){
                    FileIO.writeToFile(args[1], "ERROR: " + string + " is not a positive integer, seat number must be a positive integer!", true, true);
                    return;
                }

                int seatNum = Integer.parseInt(string);
                // Checker if seat number is positive.
                if (seatNum <= 0) {
                    FileIO.writeToFile(args[1], "ERROR: " + seatNum + " is not a positive integer, seat number must be a positive integer!", true, true);
                    return;
                }

                int seatRow;
                int seatCol;
                if (voyage instanceof Minibus) {
                    FileIO.writeToFile(args[1], "ERROR: Minibus tickets are not refundable!", true, true);
                    return;
                } else {
                    int[] position = Command.calculateSeatPosition(voyage, seatNum);
                    seatRow = position[0];
                    seatCol = position[1];
                }
                // Error handling for seats.
                if (seatRow < 0) {
                    FileIO.writeToFile(args[1], "ERROR: " + Integer.parseInt(string) + " is not a positive integer, seat number must be a positive integer!", true, true);
                    return;
                } else if (seatRow >= seats.length || seatCol >= seats[0].length) {
                    FileIO.writeToFile(args[1], "ERROR: There is no such a seat!", true, true);
                    return;
                } else if (seats[seatRow][seatCol].equals("*")) {
                    FileIO.writeToFile(args[1], "ERROR: One or more seats are already empty!", true, true);
                    return;
                }
            }
            // This loop refunds the tickets if no problem occurred before.
            for (String s : toRefund) {
                int seatNum = Integer.parseInt(s);

                int seatRow;
                int seatCol;
                int[] position = Command.calculateSeatPosition(voyage, seatNum);
                seatRow = position[0];
                seatCol = position[1];
                // Emptying the seat and updating the revenue.
                seats[seatRow][seatCol] = "*";
                double refundAmount;
                if (voyage instanceof PremiumBus && seatCol == 0) {
                    refundAmount = (1 - voyage.getRefundCut()) * ((PremiumBus) voyage).getPremPrice();
                } else {
                    refundAmount = (1 - voyage.getRefundCut()) * voyage.getPrice();
                }
                refunded += refundAmount;
                voyage.addRevenue(-refundAmount);
            }
            FileIO.writeToFile(args[1], "Seat " + commandInfos[2].replace("_", "-") + " of the Voyage "
                    + voyage.getId() + " from " + voyage.getFrom() + " to " + voyage.getTo() + " was successfully refunded for "
                    + df.format(refunded) + " TL.", true, true);
        }
        else { // If ID does not exist display error.
            FileIO.writeToFile(args[1], "ERROR: There is no voyage with ID of " + id + "!", true, true);
        }
    }
}