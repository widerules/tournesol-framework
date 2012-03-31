package com.tournesol.network.wifi;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.tournesol.network.NetworkDevice;
import com.tournesol.network.NetworkSocket;

public class WifiNetworkSocket extends NetworkSocket{

	public Socket socket;
	public BufferedOutputStream bufferedOutputStream;
	public BufferedInputStream bufferedInputStream;
	
	public WifiNetworkSocket(NetworkDevice device, Socket socket){
		super(device);
		this.socket = socket;
	}

	public InputStream getInputStream() throws IOException{
		
		if(bufferedInputStream == null)
			bufferedInputStream = new BufferedInputStream(socket.getInputStream());
		
		return bufferedInputStream;
	}

	@Override
	public OutputStream getOutputStream() throws IOException {
		
		if(bufferedOutputStream == null)
			bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
		
		return bufferedOutputStream;
	}

	@Override
	public void close() throws IOException {
		if(bufferedInputStream != null)
			try { bufferedInputStream.close(); } catch(Exception e){};
		
		if(bufferedOutputStream != null)
			try { bufferedOutputStream.close(); } catch(Exception e){};
		
		if(socket != null)
			try { socket.close(); } catch(Exception e){};
	}
}
