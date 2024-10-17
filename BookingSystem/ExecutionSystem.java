import java.util.HashMap;
public class ExecutionSystem {
    /**
     * This class represents the execution system for handling various commands.
     * It contains methods to execute commands.
     */
    // Hashmaps to store voyages and the execution commands.
    static HashMap<Integer, Bus> voyages = new HashMap<>();
    static final HashMap<String, Command> executionCommands = new HashMap<>();
    // Initialize execution commands.
    static {
        executionCommands.put("INIT_VOYAGE", new InitVoyageCommand());
        executionCommands.put("Z_REPORT", new ReportCommand());
        executionCommands.put("SELL_TICKET", new SellTicketCommand());
        executionCommands.put("REFUND_TICKET", new RefundTicketCommand());
        executionCommands.put("PRINT_VOYAGE", new PrintVoyageCommand());
        executionCommands.put("CANCEL_VOYAGE", new CancelVoyageCommand());
    }

    /**
     *  Finds the index of the last non-empty command in the input array.
     *
     * @param commands Array of commands from the input
     * @return Index of the last command.
     */
    private static int lastCommandIndex(String[] commands) {
        for (int i = commands.length - 1; i >= 0; i--) {
            String command = commands[i].trim();
            if (!command.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if a command is the last one in the command array.
     *
     * @param commandName Name of the command
     * @param commands    Array of commands
     * @return True if the command is the last one, otherwise false
     */
    private static boolean isLastCommand(String commandName, String[] commands) {
        int lastIndex = lastCommandIndex(commands);
        if (lastIndex >= 0) {
            String last = commands[lastIndex].split("\t")[0];
            return commandName.equals(last);
        }
        return false;
    }

    /**
     * Main method to execute the commands and read the input.
     *
     * @param args Command line arguments.
     */
    public static void executeCommands(String[] args){
        // Command line arguments validations.
        if (args.length != 2){
            System.out.println("ERROR: This program works exactly with two command line arguments, " +
                    "the first one is the path to the input file whereas the second one is the path to the output file. " +
                    "Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            return;
        }
        // Input validations for input.txt and output.txt.
        try{
            FileIO.readFile(args[0], false, true);
        }catch (Exception e){
            System.out.println("ERROR: This program cannot read from the \""+ args[0] +
                    "\" either this program does not have read permission to read that file or file does not exist. " +
                    "Program is going to terminate!");
            return;
        }
        try{
            FileIO.writeToFile(args[1], "", false, false);
        }catch (Exception e){
            System.out.println("ERROR: This program cannot write to the \"" + args[1] +
                    "\" please check the permissions to write that directory. Program is going to terminate!");
            return;
        }

        // Resetting output file and taking the input into an array.
        FileIO.writeToFile(args[1], "", false, false);
        String[] commands = FileIO.readFile(args[0], false, true);


        // Check if input file is empty.
        if (commands.length == 0){
            executionCommands.get("Z_REPORT").execute(new String[]{"Z_REPORT"}, args, voyages, true);
            return;
        }

        // Iterate over each command.
        for (int i = 0; i < commands.length; i++) {
            String command = commands[i];
            // Skip empty lines.
            if (command.trim().isEmpty()) {
                continue;
            }

            String[] commandInfos = command.split("\t");
            String commandName = commandInfos[0];

            FileIO.writeToFile(args[1], "COMMAND: " + command, true, true);
            // Execute command if it exists, write error if command is not recognized.
            if (executionCommands.containsKey(commandName)) {
                // If it is the last command, execute it without starting a new line.
                if (lastCommandIndex(commands) == i){
                    executionCommands.get(commandName).execute(commandInfos, args, voyages, true);
                    continue;
                }
                executionCommands.get(commandName).execute(commandInfos, args, voyages, false);
            } else {
                FileIO.writeToFile(args[1], "ERROR: There is no command namely " + commandInfos[0] + "!", true, true);
            }
        }
        // If the last command is not Z_REPORT, execute Z_REPORT without starting a new Line.
        if (!isLastCommand("Z_REPORT", commands)) {
            executionCommands.get("Z_REPORT").execute(new String[]{"Z_REPORT"}, args, voyages, true);
        }
    }
}