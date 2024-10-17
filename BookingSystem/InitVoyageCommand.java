import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class InitVoyageCommand implements Command{
    /**
     * Represents the command to initialize a voyage.
     */
    static DecimalFormat df;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.00", symbols);
    }
    /**
     * Executes the "INIT_VOYAGE" command.
     *
     * @param commandInfos   Information about the command
     * @param args           Command line arguments
     * @param voyages        Map containing voyage IDs and associated buses
     * @param isLastCommand  Flag indicating if this is the last command
     */
    @Override
    public void execute(String[] commandInfos, String[] args, HashMap<Integer, Bus> voyages, boolean isLastCommand){
        // Checker if command is used properly.
        if (commandInfos.length < 7 || commandInfos.length > 9){
            FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
            return;
        }
        // Validating ID format.
        try {
            int id = Integer.parseInt(commandInfos[2]);
            if (id<=0){
                FileIO.writeToFile(args[1], "ERROR: " + id + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
                return;
            }
        }catch (Exception e){
            FileIO.writeToFile(args[1], "ERROR: " + commandInfos[2] + " is not a positive integer, ID of a voyage must be a positive integer!", true, true);
            return;
        }
        // Validating number of seat rows.
        try{
            Integer.parseInt(commandInfos[5]);
            if (Integer.parseInt(commandInfos[5]) < 0) {
                FileIO.writeToFile(args[1], "ERROR: " + commandInfos[5] + " is not a positive integer, number of seat rows of a voyage must be a positive integer!", true, true);
                return;
            }
        }catch (Exception e){
            FileIO.writeToFile(args[1], "ERROR: " + commandInfos[5] + " is not a positive integer, number of seat rows of a voyage must be a positive integer!", true, true);
            return;
        }
        // Validating price.
        try {
            Double.parseDouble(commandInfos[6]);
            if (Double.parseDouble(commandInfos[6]) < 0) {
                FileIO.writeToFile(args[1], "ERROR: " + commandInfos[6] + " is not a positive number, price must be a positive number!", true, true);
                return;
            }
        }catch (Exception e) {
            FileIO.writeToFile(args[1], "ERROR: " + commandInfos[6] + " is not a positive number, price must be a positive number!", true, true);
            return;
        }

        int id = Integer.parseInt(commandInfos[2]);
        // Checker if ID exists.
        if (voyages.containsKey(id)) {
            FileIO.writeToFile(args[1], "ERROR: There is already a voyage with ID of " + id + "!", true, true);
            return;
        }
        // Creating the appropriate type of bus based on the command input.
        switch (commandInfos[1]){
            case "Minibus":
                if (commandInfos.length != 7){
                    FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                    return;
                }
                // Create and show info of the Bus.
                Minibus newMinibus = new Minibus(commandInfos[2], commandInfos[3], commandInfos[4], commandInfos[5], commandInfos[6]);
                voyages.put(newMinibus.getId(), newMinibus);
                FileIO.writeToFile(args[1], "Voyage " + id + " was initialized as a minibus (2) voyage from " + newMinibus.getFrom() + " to " + newMinibus.getTo() +
                                " with " + df.format(newMinibus.getPrice()) + " TL priced " + newMinibus.getSeatNum() + " regular seats. Note that minibus tickets are not refundable.",
                        true, true);
                break;
            case "Standard":
                if (commandInfos.length != 8){
                    FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                    return;
                }
                // Validating refund cut for Standard Bus
                try {
                    Integer.parseInt(commandInfos[7]);
                    if (Integer.parseInt(commandInfos[7]) < 0 || Integer.parseInt(commandInfos[7]) > 100) {
                        FileIO.writeToFile(args[1], "ERROR: " + commandInfos[7] + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                        return;
                    }
                }catch (Exception e){
                    FileIO.writeToFile(args[1], "ERROR: " + commandInfos[7] + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                    return;
                }
                // Create and show info of the Bus.
                StandardBus newStandard = new StandardBus(commandInfos[2], commandInfos[3], commandInfos[4], commandInfos[5], commandInfos[6], commandInfos[7]);
                voyages.put(newStandard.getId(), newStandard);
                FileIO.writeToFile(args[1], "Voyage " + id + " was initialized as a standard (2+2) voyage from "
                        + newStandard.getFrom() + " to " + newStandard.getTo() + " with " + df.format(newStandard.getPrice()) + " TL priced "
                        + newStandard.getSeatNum() + " regular seats. Note that refunds will be " + (int) (newStandard.getRefundCut()*100) +"% less than the paid amount.", true, true);
                break;
            case "Premium":
                if (commandInfos.length != 9){
                    FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                    return;
                }
                // Validating refund cut for Premium Bus.
                try{
                    Integer.parseInt(commandInfos[7]);
                    if (Integer.parseInt(commandInfos[7]) < 0 || Integer.parseInt(commandInfos[7]) > 100) {
                        FileIO.writeToFile(args[1], "ERROR: " + commandInfos[7] + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                        return;
                    }
                }catch (Exception e){
                    FileIO.writeToFile(args[1], "ERROR: " + commandInfos[7] + " is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", true, true);
                    return;
                }
                // Validating the premium fee for Premium Bus.
                try{
                    Integer.parseInt(commandInfos[8]);
                    if (Integer.parseInt(commandInfos[8]) < 0) {
                        FileIO.writeToFile(args[1], "ERROR: " + commandInfos[8] + " is not a non-negative integer, premium fee must be a non-negative integer!", true, true);
                        return;
                    }
                }catch (Exception e){
                    FileIO.writeToFile(args[1], "ERROR: " + commandInfos[8] + " is not a non-negative integer, premium fee must be a non-negative integer!", true, true);
                    return;
                }
                // Create and show info of the Bus.
                PremiumBus newPrem = new PremiumBus(commandInfos[2], commandInfos[3], commandInfos[4], commandInfos[5], commandInfos[6], commandInfos[7], commandInfos[8]);
                voyages.put(newPrem.getId(), newPrem);
                FileIO.writeToFile(args[1], "Voyage " + id + " was initialized as a premium (1+2) voyage from "
                        + newPrem.getFrom() + " to " + newPrem.getTo() + " with " + df.format(newPrem.getPrice()) + " TL priced "
                        + newPrem.getTotalRows() * 2 + " regular seats and " + df.format(newPrem.getPremPrice())
                        + " TL priced " + newPrem.getTotalRows() + " premium seats. Note that refunds will be "
                        + (int) (newPrem.getRefundCut()*100) + "% less than the paid amount.", true, true);
                break;
            default:
                FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                break;
        }
    }
}
