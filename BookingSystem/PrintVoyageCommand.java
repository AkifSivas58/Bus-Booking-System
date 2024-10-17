import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class PrintVoyageCommand implements Command{
    /**
     * Represents a command to print details of a specific voyage.
     */
    static DecimalFormat df;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.00", symbols);
    }
    /**
     * Executes the print voyage command.
     *
     * @param commandInfos   Array containing command information
     * @param args           Array containing command line arguments
     * @param voyages        HashMap containing all voyages
     * @param isLastCommand  Indicates if this is the last command in the list
     */
    @Override
    public void execute(String[] commandInfos, String[] args, HashMap<Integer, Bus> voyages, boolean isLastCommand) {
        // Checker if the command is properly used.
        if (commandInfos.length != 2){
            FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!", true, true);
            return;
        }
        // Validating ID format.
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
            // Displaying info of the voyage if it exists.
            Bus voyage = voyages.get(id);

            FileIO.writeToFile(args[1], "Voyage " + id, true, true);
            FileIO.writeToFile(args[1], voyage.getFrom() + "-" + voyage.getTo(),true, true);
            voyage.showSeats(args);
            FileIO.writeToFile(args[1], "Revenue: " + df.format(voyage.getRevenue()), true, true);
        }
        else { // If voyage does not exist display an error.
            FileIO.writeToFile(args[1], "ERROR: There is no voyage with ID of " + id + "!", true, true);
        }
    }
}
