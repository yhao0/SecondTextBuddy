import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.*;


public class TextBuddyTest {
	private TextBuddy buddy;
	
	
	@Before
	public void setup(){
		buddy = new TextBuddy();
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

}
