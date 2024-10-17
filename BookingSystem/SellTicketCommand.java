import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class SellTicketCommand implements Command{
    /**
     * Represents a command to sell tickets for a voyage.
     */
    static DecimalFormat df;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.00", symbols);
    }
    /**
     * Executes the sell ticket command.
     *
     * @param commandInfos Array containing command information
     * @param args         Array containing command line arguments
     * @param voyages      HashMap containing all voyages
     * @param isLastCommand Indicates if this is the last command in the list
     */
    @Override
    public void execute(String[] commandInfos, String[] args, HashMap<Integer, Bus> voyages, boolean isLastCommand) {
        // Checker if command is used properly.
        if (commandInfos.length != 3){
            FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"SELL_TICKET\" command!", true, true);
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
        String[] toSell = commandInfos[2].split("_");

        if (voyages.containsKey(id)){
            Bus voyage = voyages.get(id);
            String[][] seats = voyage.getSeats();
            double addedRevenue = 0;
            // This loop checks if any problem occur before selling.
            for (String string : toSell) {
                // Validating seat number format.
                try{
                    Integer.parseInt(string);
                }catch (Exception e){
                    FileIO.writeToFile(args[1], "ERROR: " + string + " is not a positive integer, seat number must be a positive integer!", true, true);
                    return;
                }

                int seatNumber = Integer.parseInt(string);
                // Checker if seat number is positive.
                if (seatNumber <= 0) {
                    FileIO.writeToFile(args[1], "ERROR: " + seatNumber + " is not a positive integer, seat number must be a positive integer!", true, true);
                    return;
                }

                int[] position = Command.calculateSeatPosition(voyage, seatNumber);
                int seatRow = position[0];
                int seatCol = position[1];
                // Error handling for seats.
                if (seatRow >= seats.length || seatCol >= seats[0].length) {
                    FileIO.writeToFile(args[1], "ERROR: There is no such a seat!", true, true);
                    return;
                } else if (seats[seatRow][seatCol].equals("X")) {
                    FileIO.writeToFile(args[1], "ERROR: One or more seats already sold!", true, true);
                    return;
                }
            }
            // This loops sells if no problem occurred before.
            for (String s : toSell) {
                int seatNumber = Integer.parseInt(s);

                int[] position = Command.calculateSeatPosition(voyage, seatNumber);
                int seatRow = position[0];
                int seatCol = position[1];
                // Selling the seat and updating the revenue.
                seats[seatRow][seatCol] = "X";
                if (voyage instanceof PremiumBus && seatCol == 0) {
                    voyage.addRevenue(((PremiumBus) voyage).getPremPrice());
                    addedRevenue += ((PremiumBus) voyage).getPremPrice();
                } else {
                    voyage.addRevenue(voyage.getPrice());
                    addedRevenue += voyage.getPrice();
                }
            }
            // Displaying info from the sold seats.
            FileIO.writeToFile(args[1], "Seat " + commandInfos[2].replace("_", "-") + " of the Voyage "
                    + voyage.getId() + " from " + voyage.getFrom() + " to " + voyage.getTo() + " was successfully sold for "
                    + df.format(addedRevenue) + " TL.", true, true);
        }
        else { // If the ID does not exist display an error.
            FileIO.writeToFile(args[1], "ERROR: There is no voyage with ID of " + id  + "!", true, true);
        }
    }
}