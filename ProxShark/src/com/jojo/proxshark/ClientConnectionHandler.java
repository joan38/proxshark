package com.jojo.proxshark;

import java.io.IOException;

import android.util.Log;

public class ClientConnectionHandler extends AbstractConnectionHandler {

	public ClientConnectionHandler(State state) {
		super(state);
	}

	@Override
	public void read() {
		try {
			state.bufClientToServer.compact();
			if (state.client.read(state.bufClientToServer) == -1) {
				close();
			}
			state.bufClientToServer.flip();

			Log.v(ClientConnectionHandler.class.getName(), Tools.decodeBuffer(state.bufClientToServer));
		} catch (IOException e) {
			Log.e(ClientConnectionHandler.class.getName(), "Unable to read from the client", e);
			close();
		}
	}

	@Override
	public void write() {
		try {
			state.client.write(state.bufServerToClient);
		} catch (IOException e) {
			Log.e(ClientConnectionHandler.class.getName(), "Unable to write to the client", e);
			close();
		}
	}
}
