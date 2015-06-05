package org.spectraLogic.systemMessagesTracker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.spectraLogic.systemMessagesTracker.metric.systemMessages.BlueScaleDate;
import org.spectraLogic.systemMessagesTracker.metric.systemMessages.BlueScaleTime;
import org.spectraLogic.systemMessagesTracker.metric.systemMessages.Message;
import org.spectraLogic.systemMessagesTracker.metric.systemMessages.SystemMessages;

public class MessagesDatabase {
	private static final String dataBaseUrl = "jdbc:mysql://10.10.10.77:3306/metrics";
	private static final String ADMIN_USERNAME = "root";
	private static final String ADMIN_PASSWORD = "admin";


	public int addMessages(String serialNumber, SystemMessages messages) throws ParseException, SQLException {

		List<Message> messagesToBeAdded = new ArrayList<Message>();

		for(Message message: messages.getMessages()){
			if(fetchMessage(message).isEmpty()){
				messagesToBeAdded.add(message);
			}
		}

		for(Message message: messagesToBeAdded){
			addMessage(serialNumber, message);
		}

		return messagesToBeAdded.size();
	}

	private boolean needsToBeAdded(Message message, Date mostRecent) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy MM dd HH mm ss");
		Date date = format.parse(message.getDate().getYear() + " " + message.getDate().getMonth() + " " + message.getDate().getDay() + " " +
				message.getTime().getHour() + " " + message.getTime().getMinute() + " " + message.getTime().getSecond());


		return date.after(mostRecent);
	}

	public String getMostRecentMessage() throws SQLException {
		String statement = "Select message_timestamp from system_messages\n" +
				"ORDER BY message_timestamp DESC\n" +
				"LIMIT 1;";

		return this.executeQuery(statement);
	}

	private String fetchMessage(Message message) throws SQLException {
		String statement = "SELECT * FROM system_messages \n" +
				"WHERE number = '"+ StringEscapeUtils.escapeSql(message.getNumber()) +"'\n" +
				"and date_month = '"+ StringEscapeUtils.escapeSql(message.getDate().getMonth()) +"'\n" +
				"and date_day = '"+ StringEscapeUtils.escapeSql(message.getDate().getDay()) +"'\n" +
				"and date_year = '"+ StringEscapeUtils.escapeSql(message.getDate().getYear()) +"'\n" +
				"and time_hour = '"+ StringEscapeUtils.escapeSql(message.getTime().getHour()) +"'\n" +
				"and time_minute = '"+ StringEscapeUtils.escapeSql(message.getTime().getMinute()) +"'\n" +
				"and time_second = '"+ StringEscapeUtils.escapeSql(message.getTime().getSecond()) +"';";

		return executeQuery(statement);
	}

	private void addMessage(String librarySerialNumber, Message message) throws SQLException{

		String timestamp = parseTimestamp(message);

		String updateStatement = "INSERT INTO system_messages.messages \r\n" +
				" (library_sn, "
				+ "message_timestamp, "
				+ "number_occurrences, "
				+ "number, "
				+ "severity, "
				+ "date_month, "
				+ "date_day, "
				+ "date_year, "
				+ "time_hour, "
				+ "time_minute, "
				+ "time_second, "
				+ "notification, "
				+ "remedy) \r\n" +
				"VALUES\r\n" +
				" ('" + StringEscapeUtils.escapeSql(librarySerialNumber) + "', "
				+ "'" + StringEscapeUtils.escapeSql(timestamp) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getNumberOccurrences()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getNumber()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getSeverity()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getDate().getMonth()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getDate().getDay()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getDate().getYear()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getTime().getHour()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getTime().getMinute()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getTime().getSecond()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getNotification()) + "', "
				+ "'" + StringEscapeUtils.escapeSql(message.getRemedy()) + "');";

		executeUpdate(updateStatement);
	}


	private String parseTimestamp(Message message) {
		BlueScaleDate date = message.getDate();
		BlueScaleTime time = message.getTime();

		String timestamp = date.getYear() + "-" + date.getMonth() + "-" + date.getDay() + " " + time.getHour() + ":" + time.getMinute() + ":" + time.getSecond();

		return timestamp;
	}


	private int executeUpdate(String statement) throws SQLException{

		try (
				Connection connection = DriverManager.getConnection(dataBaseUrl, ADMIN_USERNAME, ADMIN_PASSWORD);
				PreparedStatement ps = connection.prepareStatement(statement);
				){
			int effectedEntries = -1;
			effectedEntries =  ps.executeUpdate();
			return effectedEntries;
		}
	}

	private String executeQuery(String statement) throws SQLException{

		try (
				Connection connection = DriverManager.getConnection(dataBaseUrl, ADMIN_USERNAME, ADMIN_PASSWORD);
				PreparedStatement ps = connection.prepareStatement(statement);
				ResultSet rs = ps.executeQuery();

				){

			if(rs.getType() == 0){
				return "";
			}else if(rs.next() == false){
				return "";
			} else {
				return rs.getString(1);
			}


		}

	}

	public void toCsv() throws SQLException{
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

		String statement = "SELECT * FROM messages " +
				"INTO OUTFILE '/mnt/lab/Users/NickF/SystemMessageDB/System_Messages_"+ format.format(date) +".csv' " +
				"FIELDS ENCLOSED BY '\"' TERMINATED BY ';' ESCAPED BY '\"' " +
				"LINES TERMINATED BY '\\r\\n';";

		executeQuery(statement);
	}

}
