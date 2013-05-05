package com.jojo.proxshark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class is used to execute shell commands.
 * 
 * @author fzql9967
 */
public final class Shell {

	/**
	 * Execute a shell command.
	 * 
	 * Example : String[] cmd = {"su", "-c", "ls /data"}; execute(cmd);
	 * 
	 * @param cmd
	 *            the command plus the params
	 * @return the result of the command or null if an error occured
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String execute(String[] cmd) throws IOException,
			InterruptedException {
		String result = "";

		Process process = new ProcessBuilder(cmd).start();
		process.waitFor();

		BufferedReader stderr = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		String errLine = stderr.readLine();
		while (errLine != null) {
			result = result + "\n" + errLine;
		}

		BufferedReader stdout = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		String outLine = stdout.readLine();
		while (outLine != null) {
			result = result + "\n" + outLine;
			while ((outLine = stderr.readLine()) != null) {
				result = result + "\n" + outLine;
			}
		}

		return result;
	}
}
