package com.jojo.proxshark;


public interface ConnectionHandler {

	public void read();

	public void write();

	public void close();
}
