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
		buddy.testClearItem();
		buddy.testAddItem("0");
		makeTestList(3);
		buddy.testAddItem("1");
		buddy.testAddItem("2");
		assertEquals(arrayList, buddy.list);
	}
	
	@Test
	public void testClearCommand() {
		buddy.testClearItem();
		// add items into the list before clearing
		buddy.testAddItem("0"); 
		makeTestList(3);
		buddy.testAddItem("1");
		buddy.testAddItem("2");	
		assertEquals(arrayList, buddy.list);
		buddy.testClearItem();
		assertEquals(0, buddy.list.size());
	}

	@Test
	public void testDeleteCommand() {
		buddy.testClearItem();
		buddy.testAddItem("0");
		buddy.testAddItem("1");
		buddy.testAddItem("2");
		assertEquals(3, buddy.list.size());
		buddy.testDeleteItem("3");
		assertEquals(2, buddy.list.size());
		makeTestList(2);
		assertEquals(arrayList, buddy.list);
	}
	
	@Test
	public void testWrongCommand() {
		buddy.testClearItem();
		String test = "wrong is a wrong command input";
		assertEquals(test, buddy.testWrongCommand("wrong"));
	}
	
	@Test
	public void testSortCommand() {
		buddy.testClearItem();
		buddy.testAddItem("C");
		buddy.testAddItem("B");
		buddy.testAddItem("A");
		arrayList.add("A");
		arrayList.add("B");
		arrayList.add("C");
		assertNotEquals(arrayList, buddy.list);
		buddy.testSortItems();
		assertEquals(arrayList, buddy.list);
		buddy.testAddItem("A");
		arrayList.add("A");
		Collections.sort(arrayList);
		buddy.testSortItems();
		assertEquals(arrayList, buddy.list);
	}
	
	@Test
	public void testDisplayCommand() {
		buddy.testClearItem();
		buddy.testAddItem("A");
		buddy.testAddItem("B");
		String result = "1. A\n2. B\n";
		assertEquals(result, buddy.testDisplayItem());
	}
	
	@Test
	public void testSearchCommand() {
		buddy.testClearItem();
		assertEquals("Nothing to search", buddy.testSearchItem("CS2103"));
		buddy.testAddItem("CS2103 CE2");
		buddy.testAddItem("CS2105 Assignment 1");
		buddy.testAddItem("CS2103 recurring task");
		buddy.testAddItem("CS2101 evaluation");
		String result = "1. CS2103 CE2\n2. CS2103 recurring task\n";
		assertEquals(result, buddy.testSearchItem("CS2103"));
	}
}
