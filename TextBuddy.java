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
import java.time.OffsetDateTime;
import java.util.*;

public class TextBuddy {

	private static final String UNABLE_TO_DELETE = "Unable to delete something that isn't there.";
	private static final String UNABLE_TO_WRITE_TO_FILE = "Unable to write to file";
	private static final String UNABLE_TO_PROCESS_COMMAND = "%1$s is a wrong command input";	
	private static final String UNABLE_TO_CREATE_FILE = "Unable to create file";
	private static final String NOTHING_TO_ADD = "Did you forget to input something? Try again.";
	private static final String NOTHING_TO_CLEAR = "Nothing to clear.";
	private static final String NOTHING_TO_SORT = "Nothing to sort.";
	private static final String NOTHING_TO_SEARCH = "Nothing to search";
	private static final String WELCOME_MESSAGE = "Welcome to TextBuddy. %1$s is ready for use.";
	
	private static final String SUCCESS_IN_ADDING_ITEM = "added to %1$s: \"%2$s\"";
	private static final String SUCCESS_IN_DELETING_ITEM = "deleted from %1$s: \"%2$s\"";	
	private static final String SUCCESS_IN_CLEARING = "all content deleted from %1$s";
	private static final String SUCCESS_IN_SORTING = "all content sorted alphabetically";
	
	private static final String EMPTY_FILE = "%1$s is empty.\n";
	private static final String EMPTY_STRING = "";
	private static final String GIVE_YOUR_COMMAND = "command: ";
	private static final String FORMAT_FOR_DISPLAYING = "%1$d. %2$s\n"; 
	private static final String FORMAT_FOR_WRITING = "%1$d. %2$s\n";
	private static final String FORMAT_FOR_SEARCHING = "%1$d. %2$s\n";
	
	private static final int FIRST_INDEX = 1;
	private static final int OFFSET = 1;
	private static final int EMPTY = 0;
	
	
	private static Scanner sc = new Scanner(System.in);
	
	protected static ArrayList<String> list = new ArrayList<String>();

	private static String fileName;
	
	//Possible command types
	enum COMMAND_TYPE {
		ADD_ITEM, DELETE_ITEM, CLEAR_ITEM, DISPLAY_ITEM, WRONG_COMMAND, SORT_ITEM, SEARCH_ITEM
	};

	public static void main(String[] args) {
		initializing(args);
		
		String command = userGiveCommand();

		while(canCarryOn(command)) {
			executeCommand(command, fileName);
			command = userGiveCommand();
		}
		
		commitToFile();
		sc.close();
	}
	
	// set up the file and welcome the user
	private static void initializing(String[] args) {
		fileName = args[0];
		createFile(fileName);
		showToUser(String.format(WELCOME_MESSAGE, fileName));
	}
	
	// run the commands, if wrong command will inform the user
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
				showToUserWithoutNewline(displayItem());
				break;
			case WRONG_COMMAND:
				wrongCommand(userCommand);
				break;
			case SORT_ITEM:
				sortItems();
				break;
			case SEARCH_ITEM:
				showToUserWithoutNewline(searchItem(userInput));
				break;
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
		} else if (commandTypeString.equalsIgnoreCase("sort")) {
			return COMMAND_TYPE.SORT_ITEM;
		} else if (commandTypeString.equalsIgnoreCase("search")) {
			return COMMAND_TYPE.SEARCH_ITEM;
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
			closeEverything(outFile, outputWriter, writer);
		} catch (IOException e) {
			showToUser(UNABLE_TO_WRITE_TO_FILE);
		}
	}
	
	// close the streams and writer
	private static void closeEverything(FileOutputStream outFile, OutputStreamWriter outputWriter,
			BufferedWriter writer) throws IOException {
		writer.close();
		outFile.close();
		outputWriter.close();
	}

	private static void writeToFile(BufferedWriter writer) throws IOException {
		int index = FIRST_INDEX; 
		while (index - OFFSET < list.size()) {
			String input = String.format(FORMAT_FOR_WRITING, index, list.get(index - OFFSET));
			writer.write(input);
			index++;
		}
	}
	
	// checks if the file exist before creating it
	private static void createFile(String fileName) {
		try {
			File file = new File(fileName);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (IOException e) {
			showToUser(UNABLE_TO_CREATE_FILE);
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
			String itemToBeDeleted = list.get(indexToDelete - OFFSET);
			list.remove(indexToDelete - OFFSET);
			showToUser(String.format(SUCCESS_IN_DELETING_ITEM, fileName, itemToBeDeleted));
		} catch (IndexOutOfBoundsException e) {
			showToUser(UNABLE_TO_DELETE);
		} catch (InputMismatchException e) {
			//sc.nextLine(); // since the input is not a number, we need to consume the remaining line
			showToUser(UNABLE_TO_DELETE);
		}
	}
	
	// public method to test deleteItem method
	public static void testDeleteItem(String userInput) {
		deleteItem(userInput);
	}
	
	private static String wrongCommand(String command) {
		return String.format(UNABLE_TO_PROCESS_COMMAND, command);
	}
	
	// public method to test wrongCommand method
	public static String testWrongCommand(String command) {
		return wrongCommand(command);
	}

	// clear the whole list
	private static void clearItem() {
		if (list.size() != EMPTY) {
			list.clear();
			showToUser(String.format(SUCCESS_IN_CLEARING, fileName));
		} else {
			showToUser(NOTHING_TO_CLEAR);
		}
	}
	
	// public method to test clearItem method
	public static void testClearItem() {
		clearItem();
	}
	
	// prints out everything in the list
	private static String displayItem() {
		if (list.size() != EMPTY) {
			String output = getDisplayItems();
			return output;
		} else {
			return String.format(EMPTY_FILE, fileName);
		}
	}

	private static String getDisplayItems() {
		int index = FIRST_INDEX;
		String output = EMPTY_STRING; 
		while (index - OFFSET < list.size()){
			String input = list.get(index - OFFSET);
			output += String.format(FORMAT_FOR_DISPLAYING, index, input);
			index++;
		}
		return output;
	}
	
	// public method to test displayItem method
	public static String testDisplayItem() {
		return displayItem();
	}
	
	private static String searchItem(String itemToBeSearched) {
		if (list.size() != EMPTY) {
			String output = getSearchItems(itemToBeSearched);
			return output;
		} else {
			return NOTHING_TO_SEARCH;
		}
	}
	
	// find the items
	private static String getSearchItems(String itemToBeSearched) {
		String output = EMPTY_STRING;
		int numberIndex = FIRST_INDEX;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).contains(itemToBeSearched)) {
				output += String.format(FORMAT_FOR_SEARCHING, numberIndex, list.get(i));
				numberIndex++;
			}
		}
		return output;
	}

	// public method to test searchItem
	public static String testSearchItem(String itemToBeSearched) {
		return searchItem(itemToBeSearched);
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
	private static void addItem(String userInput) {
		if (isNullString(userInput)) {
			showToUser(NOTHING_TO_ADD);
		} else {
			list.add(userInput);
			showToUser(String.format(SUCCESS_IN_ADDING_ITEM, fileName, userInput));
		}
	}
	
	// public method to test addItem method
	public static void testAddItem(String userInput) {
		addItem(userInput);
	}
	
	// sorts the items in the ArrayList
	private static void sortItems() {
		if (list.size() != EMPTY) {
			Collections.sort(list);
			showToUser(SUCCESS_IN_SORTING);
		} else {
			showToUser(NOTHING_TO_SORT);
		}
	}
	
	// public method to test sortItems method
	public static void testSortItems() {
		sortItems();
	}
	
	// check if string is null
	private static boolean isNullString(String input) {
		return input.equals("");
	}
	
	private static void showToUser(String text) {
		System.out.println(text);
	}
	
	private static void showToUserWithoutNewline(String text) {
		System.out.print(text);
	}

	private static void showCommandToUser(String text) {
		System.out.print(text);
	}

}