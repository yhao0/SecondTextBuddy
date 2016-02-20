import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.*;
import org.omg.CORBA.INITIALIZE;


public class TextBuddyTest {
	private TextBuddy buddy;
	ArrayList<String> arrayList;	
	
	@Before
	public void setup(){
		buddy = new TextBuddy();
		arrayList = new ArrayList<String>();
	}
	
	public void makeTestList(int number) {
		for (int i = 0; i < number; i++) {
			arrayList.add(String.valueOf(i));
		}
	}

	@Test
	public void testExecuteCommand() {
		assertEquals(TextBuddy.COMMAND_TYPE.ADD_ITEM, buddy.determineCommandType("add"));
		assertEquals(TextBuddy.COMMAND_TYPE.WRONG_COMMAND, buddy.determineCommandType("del"));
		assertEquals(TextBuddy.COMMAND_TYPE.DELETE_ITEM, buddy.determineCommandType("delete"));
		assertEquals(TextBuddy.COMMAND_TYPE.CLEAR_ITEM, buddy.determineCommandType("clear"));
		assertEquals(TextBuddy.COMMAND_TYPE.DISPLAY_ITEM, buddy.determineCommandType("display"));
		assertEquals(TextBuddy.COMMAND_TYPE.WRONG_COMMAND, buddy.determineCommandType("1"));
	}
	
	@Test
	public void testAddCommand() {
		buddy.clearItem();
		buddy.addItem("0");
		makeTestList(3);
		buddy.addItem("1");
		buddy.addItem("2");
		assertEquals(arrayList, buddy.list);
	}
	
	@Test
	public void testClearCommand() {
		buddy.clearItem();
		buddy.addItem("0");
		makeTestList(3);
		buddy.addItem("1");
		buddy.addItem("2");	
		assertEquals(arrayList, buddy.list);
		buddy.clearItem();
		assertEquals(0, buddy.list.size());
	}

	@Test
	public void testDeleteCommand() {
		buddy.clearItem();
		buddy.addItem("0");
		buddy.addItem("1");
		buddy.addItem("2");
		assertEquals(3, buddy.list.size());
		buddy.deleteItem("3");
		assertEquals(2, buddy.list.size());
		makeTestList(2);
		assertEquals(arrayList, buddy.list);
	}
	
	@Test
	public void testWrongCommand() {
		buddy.clearItem();
		String test = "wrong is a wrong command input";
		assertEquals(test, buddy.wrongCommand("wrong"));
	}
	
	@Test
	public void testSortCommand() {
		buddy.clearItem();
		buddy.addItem("C");
		buddy.addItem("B");
		buddy.addItem("A");
		buddy.sortItems();
		arrayList.add("A");
		arrayList.add("B");
		arrayList.add("C");
		assertEquals(arrayList, buddy.list);
		buddy.addItem("A");
		arrayList.add("A");
		Collections.sort(arrayList);
		buddy.sortItems();
		assertEquals(arrayList, buddy.list);
	}
	
	@Test
	public void testDisplayCommand() {
		buddy.clearItem();
		buddy.addItem("A");
		buddy.addItem("B");
		String result = "1. A\n2. B\n";
		assertEquals(result, buddy.testDisplayItem());
	}
	
	@Test
	public void testSearchCommand() {
		buddy.clearItem();
		buddy.addItem("CS2103 CE2");
		buddy.addItem("CS2105 Assignment 1");
		buddy.addItem("CS2103 recurring task");
		buddy.addItem("CS2101 evaluation");
		String result = "CS2103 CE2\nCS2103 recurring task\n";
		assertEquals(result, buddy.searchItem("CS2103"));
	}
}
