import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import org.junit.*;


public class TextBuddyTest {
	private TextBuddy buddy;
	ArrayList<String> arrayList;	
	
	@Before
	public void setup(){
		buddy = new TextBuddy();
		arrayList = new ArrayList<String>();
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
		buddy.addItem("1");
		arrayList.add("1");
		assertEquals(arrayList, buddy.list);
		buddy.addItem("me");
		buddy.addItem("haha");
		arrayList.add("me");
		arrayList.add("haha");
		assertEquals(arrayList, buddy.list);
	}



}
