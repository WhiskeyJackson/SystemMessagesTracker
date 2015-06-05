package org.spectraLogic.systemMessagesTracker.metric.systemMessages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "org.spectraLogic.systemMessagesTracker.systemMessages.Message")
public class BlueScaleDate {
	private String month = "";
	private String day = "";
	private String year = "";

	@XmlElement(name = "month")
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}

	@XmlElement(name = "day")
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}

	@XmlElement(name = "year")
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	@Override
	public String toString() {
		return "BlueScaleDate [month=" + month + ", day=" + day + ", year=" + year + "]";
	}


}
