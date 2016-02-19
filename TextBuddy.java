/* Name: Soh Yonghao
 * Assumptions: The user uses a different file name whenever he/she wishes to use textbuddy.
 *				The user does not try to add additional items to the same file.
 *				The user is hard working, so when there is a mistake in any entry, he will input everything again.
 */



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class TextBuddy {

	private static final String UNABLE_TO_DELETE = "Unable to delete something that isn't there.";
	private static final String UNABLE_TO_WRITE_TO_FILE = "Unable to write to file";
	private static final String UNABLE_TO_PROCESS_COMMAND = "%1$s is a wrong command input";	
	private static final String NOTHING_TO_ADD = "Did you forget to input something? Try again.";
	private static final String NOTHING_TO_CLEAR = "Nothing to clear.";
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %1$s is ready for use.";
	
	private static final String SUCCESS_IN_ADDING_ITEM = "added to %1$s: \"%2$s\"";
	private static final String SUCCESS_IN_DELETING_ITEM = "deleted from %1$s: \"%2$s\"";	
	private static final String SUCCESS_IN_CLEARING = "all content deleted from %1$s";
	private static final String SUCCESS_IN_SORTING = "all content sorted alphabetically";
	
	private static final String EMPTY_FILE = "%1$s is empty.";
	private static final String GIVE_YOUR_COMMAND = "command: ";
	private static final String DISPLAY_FORMAT = "%1$d. %2$s"; 
	
	private static Scanner sc = new Scanner(System.in);
	
	protected static ArrayList<String> list = new ArrayList<String>();

	private static String fileName;
	
	//Possible command types
	enum COMMAND_TYPE {
		ADD_ITEM, DELETE_ITEM, CLEAR_ITEM, DISPLAY_ITEM, WRONG_COMMAND, SORT_COMMAND
	};

	public static void main(String[] args) {
		fileName = args[0];
		readFile(fileName);
		showToUser(String.format(WELCOME_MESSAGE, fileName));
		
		String command = userGiveCommand();

		while(canCarryOn(command)) {
			executeCommand(command, fileName);
			command = userGiveCommand();
		}
		
		commitToFile();
		sc.close();
	}
	
	public static void executeCommand(String userCommand, String fileName) {

		COMMAND_TYPE commandType = determineCommandType(userCommand);
		String userInput = getUserInput(userCommand);
		switch (commandType) {
			case ADD_ITEM:
				addItem(userInput);
				break;
			case DELETE_ITEM:
				deleteItem(userInput);
				break;
			case CLEAR_ITEM:
				clearItem();
				break;
			case DISPLAY_ITEM:
				displayItem();
				break;
			case WRONG_COMMAND:
				wrongCommand(userCommand);
				break;
			case SORT_COMMAND:
				sort();
			default:
				//throw an error if the command is not recognized
				throw new Error("Unrecognized command type");
		}
	}
	
	private static String getUserInput(String userCommand) {
		String input = sc.nextLine().trim();
		return input;
	}

	// choose the commandtype
	protected static COMMAND_TYPE determineCommandType(String commandTypeString) {
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
	private static void commitToFile() {
		try {
			FileOutputStream outFile = new FileOutputStream(fileName);  
			OutputStreamWriter outputWriter = new OutputStreamWriter(outFile);
			BufferedWriter writer = new BufferedWriter(outputWriter);
			writeToFile(writer);
			writer.close();
			outFile.close();
			outputWriter.close();
		} catch (IOException e) {
			showToUser(UNABLE_TO_WRITE_TO_FILE);
		}
	}

	private static void writeToFile(BufferedWriter writer) throws IOException {
		int index = 1; 
		while (index - 1 < list.size()) {
			String input = index + ". " + list.get(index - 1) + "\n";
			writer.write(input);
			index++;
		}
	}
	
	private static void readFile(String fileName) {
		try {
			createFile(fileName);
			FileReader inputFile = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(inputFile);
			
			String currentLine = reader.readLine();
			while(currentLine != null) {
				list.add(currentLine);
				currentLine = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			
		}
	}
	
	private static void createFile(String fileName) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			
		}
	}
	
	/* 
	 * deletes the index that the user gives
	 * if user didn't give an index, throw exception
	 * if user gives an index that is out of bounds, throw exception
	 */
	
	private static void deleteItem(String userInput) {
		try {
			int indexToDelete = Integer.parseInt(userInput);
			String itemToBeDeleted = list.get(indexToDelete - 1);
			list.remove(indexToDelete - 1);
			showToUser(String.format(SUCCESS_IN_DELETING_ITEM, fileName, itemToBeDeleted));
		} catch (IndexOutOfBoundsException e) {
			showToUser(UNABLE_TO_DELETE);
		} catch (InputMismatchException e) {
			sc.nextLine(); // since the input is not a number, we need to consume the remaining line
			showToUser(UNABLE_TO_DELETE);
		}
	}
	
	private static void wrongCommand(String command) {
		sc.nextLine(); // since the command is wrong, we need to consume the remaining line
		showToUser(UNABLE_TO_PROCESS_COMMAND);
	}	

	// clear the whole list
	private static void clearItem() {
		if (list.size() != 0) {
			list.clear();
			showToUser(String.format(SUCCESS_IN_CLEARING, fileName));
		} else {
			showToUser(NOTHING_TO_CLEAR);
		}
	}
	
	// print out everything in the list
	private static void displayItem() {
		if (list.size() != 0) {
			int index = 1;
			while (index - 1 < list.size()){
				String input = list.get(index - 1);
				showToUser(String.format(DISPLAY_FORMAT, index, input));
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
	private static boolean canCarryOn(String command) {
		return !command.equals("exit");
	}

	// adds item in
	protected static void addItem(String userInput) {
		if (isNullString(userInput)) {
			showToUser(NOTHING_TO_ADD);
		} else {
			list.add(userInput);
			showToUser(String.format(SUCCESS_IN_ADDING_ITEM, fileName, userInput));
		}
	}
	
	private static void sort() {
		Collections.sort(list);
		showToUser(SUCCESS_IN_SORTING);
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