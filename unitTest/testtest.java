import org.junit.Test;
import org.spectraLogic.systemMessagesTracker.metric.systemMessages.Message;


public class testtest {

	@Test
	public void test() throws IllegalArgumentException, IllegalAccessException {

		Message message = new Message();

		System.out.println(message.getSQLTable());
		System.out.println(message.getSqlData().toString());

	}

}
