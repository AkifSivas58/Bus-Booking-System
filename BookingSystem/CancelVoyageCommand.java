import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class CancelVoyageCommand implements Command{
    /**
     * Command subclass to cancel a voyage.
     */
    static DecimalFormat df;
    // Setting the decimal separator to '.'
    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.00", symbols);
    }
    /**
     * Executes the cancel voyage command.
     *
     * @param commandInfos   Information about the command
     * @param args           Command line arguments
     * @param voyages        Map containing voyage IDs and associated buses
     * @param lastCommandIndex Flag indicating if this is the last command
     */
    @Override
    public void execute(String[] commandInfos, String[] args, HashMap<Integer, Bus> voyages, boolean lastCommandIndex) {
        // Checker if the command is used properly.
        if (commandInfos.length != 2){
            FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!", true, true);
            return;
        }
        // Handler for invalid voyage ID format.
        try{
            Integer.parseInt(commandInfos[1]);
        }
        catch (Exception e){
            FileIO.writeToFile(args[1], "ERROR: " + commandInfos[1] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return;
        }

        int id = Integer.parseInt(commandInfos[1]);
        // Checker if ID is positive.
        if (id <= 0){
            FileIO.writeToFile(args[1], "ERROR: " + id + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
        }
        else if (voyages.containsKey(id)){
            Bus voyage = voyages.get(id);

            // Printing cancelled voyage details.
            FileIO.writeToFile(args[1], "Voyage " + id + " was successfully cancelled!", true, true);
            FileIO.writeToFile(args[1], "Voyage details can be found below:", true, true);
            FileIO.writeToFile(args[1], "Voyage " + id, true, true);
            FileIO.writeToFile(args[1], voyage.getFrom() + "-" + voyage.getTo(),true, true);
            voyage.showSeats(args);

            String[][] seats = voyage.getSeats();
            // Returning the money back if a seat was sold and updating revenue.
            if (seats != null) {
                for (String[] row : seats){
                    for (int i = 0; i < row.length; i++){
                        if ("X".equals(row[i])) {
                            if (voyage instanceof PremiumBus && i == 0){
                                voyage.addRevenue(-((PremiumBus) voyage).getPremPrice());
                            }
                            else {
                                voyage.addRevenue(-voyage.getPrice());
                            }
                        }
                    }
                }
            }
            // Writing the last revenue and cancelling the voyage
            FileIO.writeToFile(args[1], "Revenue: " + df.format(voyage.getRevenue()), true, true);
            voyages.remove(id);
        }
        else {
            // Handling if the voyage does not exist.
            FileIO.writeToFile(args[1], "ERROR: There is no voyage with ID of " + id + "!", true, true);
        }
    }
}