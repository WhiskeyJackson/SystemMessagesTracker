package org.spectraLogic.systemMessagesTracker;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.text.ParseException;

import javax.xml.bind.JAXBException;

import org.spectraLogic.systemMessagesTracker.commandLine.CommandLineOptions;
import org.spectraLogic.systemMessagesTracker.database.MessagesDatabase;
import org.spectraLogic.systemMessagesTracker.http.HttpClient;
import org.spectraLogic.systemMessagesTracker.metric.systemMessages.SystemMessages;

public class Main {

	public static void main(String[] args) throws KeyManagementException, KeyStoreException, NoSuchAlgorithmException,
	CertificateException, IOException, URISyntaxException, JAXBException, ParseException, SQLException {

		CommandLineOptions options = CommandLineOptions.parseArgs(args);

		if(options.help){

			System.out.println("Valid Arguments separated by spaces: ");
			System.out.println("Library IP Address: '123.456.789.910' ");
			System.out.println("Library Username: 'u=exampleusername' ");
			System.out.println("Library Password: 'p=examplepassword' ");
			System.out.println("Enable SSL flag: '-ssl' ");

		} else if(options.toCsv){

			MessagesDatabase db = new MessagesDatabase();
			db.toCsv();

		} else {

			HttpClient client = new HttpClient();

			InputStream xml = client.getMessages(options);
			SystemMessages messages = SystemMessages.parseFromStream(xml);
			System.out.println("Found " + messages.getMessages().size() + " messages");

			String serialNumber = client.getSerialNumber(options);
			System.out.println("SN: " + serialNumber);

			MessagesDatabase db = new MessagesDatabase();
			int numberOfMessagesAdded = db.addMessages(serialNumber, messages);
			System.out.println("Messages Added: " + numberOfMessagesAdded);
		}
	}
}
