package com.tournesol.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class NetworkSocket {

	public NetworkDevice device;
	
	public NetworkSocket(NetworkDevice device){
		this.device = device;
	}

	public abstract void close() throws IOException;
	public abstract InputStream getInputStream() throws IOException;
	public abstract OutputStream getOutputStream() throws IOException;
}
