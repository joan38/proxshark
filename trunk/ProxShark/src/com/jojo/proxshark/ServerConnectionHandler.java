package com.jojo.proxshark;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import android.util.Log;

public class ServerConnectionHandler implements ConnectionHandler {

	private final State state;

	public ServerConnectionHandler(State state) {
		this.state = state;
	}

	@Override
	public void read() {
		try {
			state.bufServerToClient.compact();
			if (state.server.read(state.bufServerToClient) == -1) {
				state.server.close();
			}
			state.bufServerToClient.flip();

			String data = Tools.decodeBuffer(state.bufServerToClient);
			data = Tools.doReplacements(data);
			Tools.encodeBuffer(state.bufServerToClient, data);

			Log.v(ServerConnectionHandler.class.getName(), data);
		} catch (IOException e) {
			Log.e(ServerConnectionHandler.class.getName(), "Unable to read from the server", e);
			closeAll();

		}
	}

	@Override
	public void write() {
		try {
			state.server.write(state.bufClientToServer);

			if (!state.bufClientToServer.hasRemaining() && !state.client.isConnected()) {
				state.server.close();
			}
		} catch (IOException e) {
			Log.e(ServerConnectionHandler.class.getName(), "Unable to write to the server", e);
			closeAll();
		}
	}

	public void connect() {
		try {
			if (state.server.finishConnect()) {
				state.serverKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
		} catch (IOException e) {
			Log.e("ProxShark", "Unable to connect to the server", e);
			closeAll();
		}
	}

	private void closeAll() {
		try {
			state.client.close();
			state.server.close();
		} catch (Exception ignored) {
		}
	}
}