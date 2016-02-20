import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

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

}
