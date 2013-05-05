package com.jojo.proxshark;

import java.nio.channels.SelectionKey;

public interface ProxyHandler {

	public void accept(SelectionKey key);
}
