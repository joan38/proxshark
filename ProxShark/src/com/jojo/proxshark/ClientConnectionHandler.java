package com.jojo.proxshark;

import java.io.IOException;

import android.util.Log;

public class ClientConnectionHandler implements ConnectionHandler {

	private final State state;

	public ClientConnectionHandler(State state) {
		this.state = state;
	}

	@Override
	public void read() {
		try {
			state.bufClientToServer.compact();
			if (state.client.read(state.bufClientToServer) == -1) {
				state.client.close();
			}
			state.bufClientToServer.flip();

			Log.v(ClientConnectionHandler.class.getName(), Tools.decodeBuffer(state.bufClientToServer));
		} catch (IOException e) {
			Log.e(ClientConnectionHandler.class.getName(), "Unable to read from the client", e);
			closeAll();
		}
	}

	@Override
	public void write() {
		try {
			state.client.write(state.bufServerToClient);

			if (!state.bufServerToClient.hasRemaining() && !state.server.isConnected()
					&& !state.server.isConnectionPending()) {
				state.client.close();
			}
		} catch (IOException e) {
			Log.e(ClientConnectionHandler.class.getName(), "Unable to write to the client", e);
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
