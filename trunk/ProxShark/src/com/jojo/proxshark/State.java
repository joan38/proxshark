package com.jojo.proxshark;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class State {

	private static final int BUFFER_SIZE = 4096;
	public final ByteBuffer bufClientToServer;
	public final ByteBuffer bufServerToClient;
	public final SelectionKey clientKey;
	public final SelectionKey serverKey;
	public final SocketChannel client;
	public final SocketChannel server;

	public State(SelectionKey clientKey, SelectionKey serverKey) {
		this.clientKey = clientKey;
		this.serverKey = serverKey;
		this.client = (SocketChannel) clientKey.channel();
		this.server = (SocketChannel) serverKey.channel();
		this.bufClientToServer = ByteBuffer.allocate(BUFFER_SIZE);
		this.bufClientToServer.flip();
		this.bufServerToClient = ByteBuffer.allocate(BUFFER_SIZE);
		this.bufServerToClient.flip();
	}
}
