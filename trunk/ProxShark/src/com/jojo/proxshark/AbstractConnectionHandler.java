package com.jojo.proxshark;

import android.util.Log;

public abstract class AbstractConnectionHandler implements ConnectionHandler {

	protected final State state;

	public AbstractConnectionHandler(State state) {
		this.state = state;
	}

	@Override
	public void close() {
		Log.i(AbstractConnectionHandler.class.getName(), "Connection closed");
		try {
			state.client.close();
			state.server.close();
		} catch (Exception ignored) {
		}
	}
}
