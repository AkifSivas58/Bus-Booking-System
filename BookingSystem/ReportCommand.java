import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;

public class ReportCommand implements Command{
    static DecimalFormat df;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        df = new DecimalFormat("0.00", symbols);
    }

    public void execute(String[] commandInfos, String[] args, HashMap<Integer, Bus> voyages, boolean isLastCommand) {
        if (commandInfos.length != 1){
            if (isLastCommand){
                FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"Z_REPORT\" command!", true, false);;
                return;
            }
            FileIO.writeToFile(args[1], "ERROR: Erroneous usage of \"Z_REPORT\" command!", true, true);
            return;
        }

        FileIO.writeToFile(args[1], "Z Report:", true, true);
        FileIO.writeToFile(args[1], "----------------", true, true);

        if (voyages.isEmpty()){
            FileIO.writeToFile(args[1], "No Voyages Available!", true, true);
            if (isLastCommand){
                FileIO.writeToFile(args[1], "----------------", true, false);
                return;
            }
            FileIO.writeToFile(args[1], "----------------", true, true);
            return;
        }

        int numVoyages = voyages.size();
        int count = 0;
        for (int id : voyages.keySet()){
            count++;
            Bus bus = voyages.get(id);
            FileIO.writeToFile(args[1], "Voyage " + id, true, true);
            FileIO.writeToFile(args[1], bus.getFrom() + "-" + bus.getTo(), true, true);

            bus.showSeats(args);

            FileIO.writeToFile(args[1], "Revenue: " + df.format(bus.getRevenue()), true, true);

            if (count == numVoyages && isLastCommand) {
                FileIO.writeToFile(args[1], "----------------", true, false);
            } else {
                FileIO.writeToFile(args[1], "----------------", true, true);
            }
        }
    }
}