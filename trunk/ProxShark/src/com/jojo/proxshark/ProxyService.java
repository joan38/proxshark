package com.jojo.proxshark;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ProxyService extends Service {

	public static final String GROOVESHARK_IP = "8.20.213.76";
	public static final int GROOVESHARK_HTTP_PORT = 80;
	public static final int GROOVESHARK_HTTPS_PORT = 443;
	public static final int PROXY_HTTP_PORT = 1080;
	public static final int PROXY_HTTPS_PORT = 1443;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.v("ProxShark", "Service started");

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Selector
					Selector selector = Selector.open();

					// Proxy http
					ServerSocketChannel proxyHttp = ServerSocketChannel.open();
					proxyHttp.configureBlocking(false);
					proxyHttp.socket().bind(new InetSocketAddress(PROXY_HTTP_PORT));
					SelectionKey proxyHttpKey = proxyHttp.register(selector, SelectionKey.OP_ACCEPT);
					proxyHttpKey.attach(new HttpProxyHandler());
					Log.i(ProxyService.class.getName(), "Proxy binded to the port " + PROXY_HTTP_PORT);

					// Proxy https
					ServerSocketChannel proxyHttps = ServerSocketChannel.open();
					proxyHttps.configureBlocking(false);
					proxyHttps.socket().bind(new InetSocketAddress(PROXY_HTTPS_PORT));
					SelectionKey proxyHttpsKey = proxyHttps.register(selector, SelectionKey.OP_ACCEPT);
					proxyHttpsKey.attach(new HttpsProxyHandler());
					Log.i(ProxyService.class.getName(), "Proxy binded to the port " + PROXY_HTTPS_PORT);

					while (!Thread.interrupted()) {
						selector.select();

						for (SelectionKey key : selector.selectedKeys()) {
							if (!key.isValid()) {
								key.channel().close();
							} else if (key.isAcceptable()) {
								((ProxyHandler) key.attachment()).accept(key);
								Log.i(ProxyService.class.getName(), "New client connected");
							} else if (key.isConnectable()) {
								((ServerConnectionHandler) key.attachment()).connect();
								Log.i(ProxyService.class.getName(), "Connected to the server");
							} else if (key.isReadable()) {
								((ConnectionHandler) key.attachment()).read();
							} else if (key.isWritable()) {
								((ConnectionHandler) key.attachment()).write();
							}
						}

						selector.selectedKeys().clear();
					}
				} catch (IOException e) {
					Log.wtf(ProxyService.class.getName(), e);
				}
			}
		}).start();

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}
}