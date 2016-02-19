/* Name: Soh Yonghao
 * Assumptions: The user uses a different file name whenever he/she wishes to use textbuddy.
 *				The user does not try to add additional items to the same file.
 *				The user is hard working, so when there is a mistake in any entry, he will input everything again.
 */



import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class TextBuddy {

	private static final String INVALID_DELETE = "Unable to delete something that isn't there.";
	private static final String ADDING_NOTHING = "Did you forget to input something? Try again.";
	private static final String CLEARING_NOTHING = "Nothing to clear.";
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %1$s is ready for use.";
	private static final String SUCCESS_IN_ADDING_ITEM = "added to %1$s: \"%2$s\"";
	private static final String SUCCESS_IN_DELETING_ITEM = "deleted from %1$s: \"%2$s\"";	
	private static final String SUCCESS_IN_CLEARING = "all content deleted from %1$s";
	private static final String EMPTY_FILE = "%1$s is empty.";
	private static final String GIVE_YOUR_COMMAND = "command: ";
	private static Scanner sc = new Scanner(System.in);
	private static ArrayList<String> list = new ArrayList<String>();

	//Possible command types
	enum COMMAND_TYPE {
		ADD_ITEM, DELETE_ITEM, CLEAR_ITEM, DISPLAY_ITEM, WRONG_COMMAND
	};

	public static void main(String[] args) {
		String fileName = args[0];
		showToUser(String.format(WELCOME_MESSAGE, fileName));
		
		String command = userGiveCommand();

		while(carryOn(command)) {
			executeCommand(command, fileName);
			command = userGiveCommand();
		}
		
		commitToFile(fileName);
		sc.close();
	}
	
	public static void executeCommand(String userCommand, String fileName) {

		COMMAND_TYPE commandType = determineCommandType(userCommand);

		switch (commandType) {
			case ADD_ITEM:
				addItem(fileName);
				break;
			case DELETE_ITEM:
				deleteItem(fileName);
				break;
			case CLEAR_ITEM:
				clearItem(fileName);
				break;
			case DISPLAY_ITEM:
				displayItem(fileName);
				break;
			case WRONG_COMMAND:
				wrongCommand(userCommand);
				break;
			default:
				//throw an error if the command is not recognized
				throw new Error("Unrecognized command type");
		}
	}

	// choose the commandtype
	private static COMMAND_TYPE determineCommandType(String commandTypeString) {
		if (commandTypeString == null)
			throw new Error("command type string cannot be null!");

		if (commandTypeString.equalsIgnoreCase("add")) {
			return COMMAND_TYPE.ADD_ITEM;
		} else if (commandTypeString.equalsIgnoreCase("delete")) {
			return COMMAND_TYPE.DELETE_ITEM;
		} else if (commandTypeString.equalsIgnoreCase("clear")) {
			return COMMAND_TYPE.CLEAR_ITEM;
		} else if (commandTypeString.equalsIgnoreCase("display")) {
			return COMMAND_TYPE.DISPLAY_ITEM;
		} else {
			return COMMAND_TYPE.WRONG_COMMAND;
		}
	}
	
	// create and write to file after exiting
	private static void commitToFile(String fileName) {
		try {
			FileOutputStream outFile = new FileOutputStream(fileName);  
			BufferedOutputStream bos = new BufferedOutputStream(outFile);
			writeToFile(bos);
		} catch (IOException e) {
			System.out.println("Unable to write to file");
		}
	}

	private static void writeToFile(BufferedOutputStream bos) throws IOException {
		int index = 1; 
		while (index - 1 < list.size()) {
			String input = index + ". " + list.get(index - 1) + "\n";
			byte[] bytes = input.getBytes();
			bos.write(bytes);
			index++;
		}
		bos.close();
	}
	
	/* 
	 * deletes the index that the user gives
	 * if user didn't give an index, throw exception
	 * if user gives an index that is out of bounds, throw exception
	 */
	
	private static void deleteItem(String fileName) {
		try {
			int indexToDelete = sc.nextInt();
			String itemToBeDeleted = list.get(indexToDelete - 1);
			list.remove(indexToDelete - 1);
			showToUser(String.format(SUCCESS_IN_DELETING_ITEM, fileName, itemToBeDeleted));
		} catch (IndexOutOfBoundsException e) {
			showToUser(INVALID_DELETE);
		} catch (InputMismatchException e) {
			sc.nextLine(); // since the input is not a number, we need to consume the remaining line
			showToUser(INVALID_DELETE);
		}
	}
	
	private static void wrongCommand(String command) {
		sc.nextLine(); // since the command is wrong, we need to consume the remaining line
		System.out.println(command + " is a wrong command input.");
	}	

	// clear the whole list
	private static void clearItem(String fileName) {
		if (list.size() != 0) {
			list.clear();
			showToUser(String.format(SUCCESS_IN_CLEARING, fileName));
		} else {
			showToUser(CLEARING_NOTHING);
		}
	}
	
	// print out everything in the list
	private static void displayItem(String fileName) {
		if (list.size() != 0) {
			int index = 1;
			while (index - 1 < list.size()){
				System.out.println(""+ index+". "+list.get(index - 1));
				index++;
			}
		} else {
			showToUser(String.format(EMPTY_FILE, fileName));
		}
	}
	
	// ask user for command
	private static String userGiveCommand() {
		showCommandToUser(GIVE_YOUR_COMMAND);
		String command = sc.next();
		return command;
	}
	
	// check if command is exit
	private static boolean carryOn(String command) {
		return !command.equals("exit");
	}

	// adds item in
	private static void addItem(String fileName) {
		String input = sc.nextLine().trim();
		if (isNullString(input)) {
			showToUser(ADDING_NOTHING);
		} else {
			list.add(input);
			showToUser(String.format(SUCCESS_IN_ADDING_ITEM, fileName, input));
		}
	}
	
	// check if string is null
	private static boolean isNullString(String input) {
		return input.equals("");
	}
	private static void showToUser(String text) {
		System.out.println(text);
	}

	private static void showCommandToUser(String text) {
		System.out.print(text);
	}

}
