package org.spectraLogic.systemMessagesTracker.systemMessages;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(namespace = "org.spectraLogic.systemMessagesTracker.systemMessages.Message")
public class BlueScaleTime {
	private String hour = "";
	private String minute = "";
	private String second = "";

	@XmlElement(name = "hour")
	public String getHour() {
		return hour;
	}
	public void setHour(String hour) {
		this.hour = hour;
	}

	@XmlElement(name = "minute")
	public String getMinute() {
		return minute;
	}
	public void setMinute(String minute) {
		this.minute = minute;
	}

	@XmlElement(name = "second")
	public String getSecond() {
		return second;
	}
	public void setSecond(String second) {
		this.second = second;
	}
	@Override
	public String toString() {
		return "BlueScaleTime [hour=" + hour + ", minute=" + minute + ", second=" + second + "]";
	}


}
