package com.tournesol.network.wifi;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import java.util.Enumeration;

import android.content.Context;
import android.net.wifi.WifiManager;

import com.tournesol.network.Messages;
import com.tournesol.network.NetworkDevice;
import com.tournesol.network.NetworkManager;
import com.tournesol.network.NetworkSocket;


public class WifiNetworkManager extends NetworkManager{

	public static int CONNECTION_TIME_OUT = 5000;
	public static int CHECK_HOST_TIMEOUT = 300;
	public static int port = 5487;
	private static boolean accept_loop = false;
	
	//Instance singleton
	public final static WifiNetworkManager i = new WifiNetworkManager(); 
	
	private boolean isFindingDevice = false;

	@Override
	public void findDevicesImpl() throws Exception {
		
		isFindingDevice = true;

		try
		{
			String current_ip = getLocalIpAddress();
			String prefix = current_ip.substring(0, current_ip.lastIndexOf(".") + 1);

			//Tester toutes les adresses locaux
			for (int i=1;i<254;i++){
				final String ip = prefix + i;
				if(ip.equals(current_ip))
					continue;

				try
				{
					if (InetAddress.getByName(ip).isReachable(CHECK_HOST_TIMEOUT)){

						WifiNetworkDevice device = new WifiNetworkDevice("?", ip);
						NetworkSocket socket = connectDeviceImplNoAsk(device);
						if(socket != null)
							sendMessageImpl(Messages.INITIAL_REQUEST, "", socket);
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(Exception e){
			isFindingDevice = false;
			throw e;
		}
		
		isFindingDevice = false;
	}
	
	/**
	 * Obtenir l'adresse IP de l'appareil.
	 */
	public String getLocalIpAddress(){
		try {
			/*
	        for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	            NetworkInterface intf = en.nextElement();
	            for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                InetAddress inetAddress = enumIpAddr.nextElement();
	                if (!inetAddress.isLoopbackAddress()) {
	                    return inetAddress..getHostAddress();
	                }
	            }
	        }
	        */
			
			WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
			int i = wifiManager.getConnectionInfo().getIpAddress();
			return (i & 0xFF) + "." +
			       ((i >>  8) & 0xFF) + "." +
			       ((i >> 16) & 0xFF) + "." +
			       ((i >> 24) & 0xFF);
			
	    } catch (Exception ex) {
	        ex.printStackTrace();
	    }
	    return null;
	}
	
	@Override
	public void reveiveMessage(byte message_id, Object message, NetworkSocket socket, NetworkDevice device){
		
		super.reveiveMessage(message_id, message, socket, device);
		
		if(message_id == Messages.INITIAL_RESPONSE){
			this.deviceFound(new WifiNetworkDevice(message.toString(), ((WifiNetworkDevice)device).ip));
			this.disconnectSocket(socket);
		}
		
	}

	@Override
	protected NetworkSocket connectDeviceImpl(NetworkDevice device) throws Exception {
		
		NetworkSocket networkSocket = connectDeviceImplNoAsk(device);

		//Envoyer une demande de nom
		this.sendMessageImpl(Messages.ASK_NAME, getDeviceName(), networkSocket);
		
		//Envoyer une demande d'identifiant de l'appareil
		this.sendMessageImpl(Messages.ASK_DEVICE_ID, getDeviceID(), networkSocket);
		
		//Commencer une procédure continue de ping
		//this.ping(networkSocket);
		
		return networkSocket;
	}
	
	protected NetworkSocket connectDeviceImplNoAsk(NetworkDevice device) throws Exception {
		
		WifiNetworkDevice wifiDevice = (WifiNetworkDevice)device;
		WifiNetworkSocket networkSocket = new WifiNetworkSocket(wifiDevice, createSocket(wifiDevice.ip, port));
		this.sockets.add(networkSocket);
		this.readSocket(networkSocket);

		return networkSocket;
	}

	@Override
	public boolean isFindingDevices() {
		return isFindingDevice;
	}

	@Override
	public boolean isDiscoverable() {
		return true;
	}

	@Override
	protected void acceptDevicesImpl() throws Exception {
		
		ServerSocket serverSocker = new ServerSocket(port);

		try
		{
			while(accept_loop)
			{
				Socket socket = serverSocker.accept();
				/*
				socket.setSoTimeout(0);
				socket.setKeepAlive(true);
				socket.setTcpNoDelay(true);
				*/
				WifiNetworkSocket wifiSocket = new WifiNetworkSocket(new WifiNetworkDevice("?", socket.getInetAddress().getHostAddress()), socket);
				this.sockets.add(wifiSocket);
				this.readSocket(wifiSocket);
			}
		}
		catch(Exception e){
			serverSocker.close();
			throw e;
		}
	}

	@Override
	public boolean getAcceptLoop() {
		return accept_loop;
	}

	@Override
	public void setAcceptLoop(boolean value) {
		accept_loop = value;
	}
	
	private static Socket createSocket(String ip, int port) throws Exception{
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(ip, port), CONNECTION_TIME_OUT);
		return socket;
	}
}
