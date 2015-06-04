package org.spectraLogic.systemMessagesTracker.systemMessages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "org.spectraLogic.systemMessagesTracker.systemMessages.SystemMessages")
public class Message {

	private String numberOccurrences = "";
	private String number = "";
	private String severity = "";
	private BlueScaleDate date = new BlueScaleDate();
	private BlueScaleTime time = new BlueScaleTime();
	private String notification = "";
	private String remedy = "";

	@XmlElement(name = "numberOccurrences")
	public String getNumberOccurrences() {
		return numberOccurrences;
	}
	public void setNumberOccurrences(String numberOccurrences) {
		this.numberOccurrences = numberOccurrences;
	}

	@XmlElement(name = "number")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}

	@XmlElement(name = "severity")
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@XmlElement(name = "date")
	public BlueScaleDate getDate() {
		return date;
	}
	public void setDate(BlueScaleDate date) {
		this.date = date;
	}

	@XmlElement(name = "time")
	public BlueScaleTime getTime() {
		return time;
	}
	public void setTime(BlueScaleTime time) {
		this.time = time;
	}

	@XmlElement(name = "notification")
	public String getNotification() {
		return notification;
	}
	public void setNotification(String notification) {
		this.notification = notification;
	}

	@XmlElement(name = "remedy")
	public String getRemedy() {
		return remedy;
	}
	public void setRemedy(String remedy) {
		this.remedy = remedy;
	}
	@Override
	public String toString() {
		return "Message [numberOccurrences=" + numberOccurrences + ", number=" + number + ", severity=" + severity + ", date=" + date.toString()
				+ ", time=" + time.toString() + ", notification=" + notification + ", remedy=" + remedy + "]";
	}


}
