import java.util.HashMap;

public interface Command {
    /**
     * Interface representing a command to be executed.
     */

    /**
     * Executes the command.
     *
     * @param commandInfos   Information about the command
     * @param args           Command line arguments
     * @param voyages        Map containing voyage IDs and associated buses
     * @param isLastCommand  Flag indicating if this is the last command
     */
    void execute(String[] commandInfos, String[] args, HashMap<Integer, Bus> voyages, boolean isLastCommand);

    /**
     * Calculates the row and column position of a seat based on the type of the bus.
     *
     * @param voyage      The bus object representing the voyage
     * @param seatNumber  The number of the seat
     * @return An array with two elements representing the row and column position of the seat
     */
    static int[] calculateSeatPosition(Bus voyage, int seatNumber) {
        int seatRow, seatCol;
        if (voyage instanceof PremiumBus) {
            seatRow = (seatNumber - 1) / 3;
            seatCol = (seatNumber - 1) % 3;
        } else if (voyage instanceof StandardBus) {
            seatRow = (seatNumber - 1) / 4;
            seatCol = (seatNumber - 1) % 4;
        } else {
            seatRow = (seatNumber - 1) / 2;
            seatCol = (seatNumber - 1) % 2;
        }
        return new int[]{seatRow, seatCol};
    }
}