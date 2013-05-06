package com.jojo.proxshark;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import android.util.Log;

public class HttpsProxyHandler implements ProxyHandler {

	@Override
	public void accept(SelectionKey key) {
		SocketChannel client = null;
		SocketChannel server = null;
		try {
			client = ((ServerSocketChannel) key.channel()).accept();
			client.configureBlocking(false);
			SelectionKey clientKey = client.register(key.selector(), SelectionKey.OP_READ
					| SelectionKey.OP_WRITE);

			server = SocketChannel.open();
			server.configureBlocking(false);
			SelectionKey serverKey = server.register(clientKey.selector(), SelectionKey.OP_CONNECT);
			server.connect(new InetSocketAddress(ProxyService.GROOVESHARK_IP,
					ProxyService.GROOVESHARK_HTTPS_PORT));

			State state = new State(clientKey, serverKey);
			clientKey.attach(new ClientConnectionHandler(state));
			serverKey.attach(new ServerConnectionHandler(state));
		} catch (IOException e) {
			Log.e(HttpProxyHandler.class.getName(), "Unable to accept a new client", e);
			try {
				client.close();
				server.close();
			} catch (Exception ignored) {
			}
		}
	}
}
