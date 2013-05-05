package com.jojo.proxshark;

import java.io.IOException;
import java.nio.channels.SelectionKey;

import android.util.Log;

public class ServerConnectionHandler extends AbstractConnectionHandler {

	public ServerConnectionHandler(State state) {
		super(state);
	}

	@Override
	public void read() {
		try {
			state.bufServerToClient.compact();
			if (state.server.read(state.bufServerToClient) == -1) {
				close();
			}
			state.bufServerToClient.flip();

			String data = Tools.decodeBuffer(state.bufClientToServer);
			Log.v(ClientConnectionHandler.class.getName(), data);

			if (data.contains("i")) {

			}
		} catch (IOException e) {
			Log.e(ServerConnectionHandler.class.getName(), "Unable to read from the server", e);
			close();
		}
	}

	@Override
	public void write() {
		try {
			state.server.write(state.bufClientToServer);
		} catch (IOException e) {
			Log.e(ServerConnectionHandler.class.getName(), "Unable to write to the server", e);
			close();
		}
	}

	public void connect() {
		try {
			if (state.server.finishConnect()) {
				state.serverKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			}
		} catch (IOException e) {
			Log.e("ProxShark", "Unable to connect to the server", e);
			((ConnectionHandler) state.serverKey.attachment()).close();
		}
	}
}