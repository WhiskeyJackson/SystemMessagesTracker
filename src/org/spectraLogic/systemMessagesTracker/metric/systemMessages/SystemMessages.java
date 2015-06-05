package org.spectraLogic.systemMessagesTracker.metric.systemMessages;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="systemMessages")
public class SystemMessages {

	private List<Message> messages = new ArrayList<Message>();

	@XmlElement(name = "message")
	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	@Override
	public String toString() {
		return "SystemMessages [messages=" + messages.toString() + "]";
	}

	public static SystemMessages parseFromStream(InputStream xml) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(SystemMessages.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (SystemMessages) unmarshaller.unmarshal(xml);

	}


}
