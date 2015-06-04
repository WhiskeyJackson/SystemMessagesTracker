package org.spectraLogic.systemMessagesTracker.commandLine;

public class CommandLineOptions {

	public boolean isSsl = true;
	public String ipAddress = "192.168.3.67";
	public String username = "su";
	public String password = "dingus2015";
	public boolean help = false;
	public boolean toCsv = false;

	public static CommandLineOptions parseArgs(String[] args) {

		CommandLineOptions options = new CommandLineOptions();

		for(String arg: args){

			if(arg.contains("-ssl")){
				options.isSsl = true;
			} else if (arg.split("\\.").length >= 4){
				options.ipAddress = arg;
			} else if (arg.contains("u=")){
				options.ipAddress = arg.split("=")[1];
			} else if (arg.contains("p=")){
				options.ipAddress = arg.split("=")[1];
			} else if (arg.contains("-h")) {
				options.help = true;
			} else if (arg.contains("-csv")){
				options.toCsv = true;
			}
		}

		return options;
	}

	@Override
	public String toString() {
		return "IP: " + ipAddress + "\r\n" +
				"Username: " + username + "\r\n" +
				"Password: " + password + "\r\n" +
				"SSL: " + isSsl + "\r\n";
	}

}
