package com.tournesol.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Looper;
import android.util.Log;

public abstract class NetworkManager implements IMessageListener{

	private static final int PING_INTERVAL = 2000;
	
	//Liste des sockets connectés
	protected final ArrayList<NetworkSocket> sockets = new ArrayList<NetworkSocket>();
	
	//Observateur de messages
	public final ArrayList<IMessageListener> message_listeners = new ArrayList<IMessageListener>();
	
	//Context utiliser pour obtenir des services
	protected Context context;
	
	public NetworkManager(){
		this.message_listeners.add(this);
	}
	
	public void init(Context context){
		this.context = context;
	}
	
	public void findDevices(){
		new Thread(){
			
			@Override
			public void run() {
				try
				{
					findDevicesImpl();
				}
				catch(Exception e){
					notifyListeners(Messages.EXCEPTION, e, null, null);
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	protected abstract void findDevicesImpl() throws Exception;

	protected void deviceFound(NetworkDevice device){
		notifyListeners(Messages.DEVICE_FOUND, null, null, device);
	}
	
	
	public void connectDevice(final NetworkDevice device){
		new Thread(){
			
			@Override
			public void run() {
				try
				{
					connectDeviceImpl(device);
				}
				catch(Exception e){
					notifyListeners(Messages.EXCEPTION, e, null, device);
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	protected abstract NetworkSocket connectDeviceImpl(NetworkDevice device) throws Exception;
	
	
	public void acceptDevices(){
		
		if(getAcceptLoop())
			return;
		
		new Thread(){
			
			@Override
			public void run() {
				try
				{
					Looper.prepare();
					setAcceptLoop(true);
					acceptDevicesImpl();
				}
				catch(Exception e){
					setAcceptLoop(false);
					notifyListeners(Messages.EXCEPTION, e, null, null);
					e.printStackTrace();
				}
			}
		}.start();
		
	}
	
	protected abstract void acceptDevicesImpl() throws Exception;

	
	public int socketCount(){
		return sockets.size();
	}
	
	public ArrayList<NetworkDevice> getConnectedDevices(){
		
		ArrayList<NetworkDevice> lst = new ArrayList<NetworkDevice>();
		
		for(NetworkSocket socket : sockets)
			lst.add(socket.device);

		return lst;
	}
	
	public boolean isDeviceConnected(NetworkDevice device){
		
		int count = sockets.size();
		for(int i = 0; i < count; i++)
			if(sockets.get(i).device.equals(device))
				return true;
		
		return false;
	}
	
	public void disconnectDevice(NetworkDevice device){
		
		int count = sockets.size();
		for(int i = 0; i < count; i++)
			if(sockets.get(i).device.equals(device))
				disconnectSocket(sockets.get(i));
	}
	
	protected void disconnectSocket(final NetworkSocket socket){
		new Thread(){
			
			@Override
			public void run() {
				try
				{
					sockets.remove(socket);
					sendMessageImpl(Messages.CLOSE, null, socket);
					socket.close();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}.start();
	}
	 
	
	public abstract boolean isFindingDevices();
	public abstract boolean isDiscoverable();
	public abstract boolean getAcceptLoop();
	public abstract void setAcceptLoop(boolean value);
	
	/**
	 * Envoyer un message aux appareils connectés.
	 */
	public void sendMessage(byte message_id, Object message){
		
		int count = sockets.size();
		for(int i = 0; i < count; i++)
			sendMessage(message_id, message, sockets.get(i));
	}
	
	public void sendMessage(final byte message_id, final Object message, final NetworkSocket socket){
		
		new Thread(){
			
			@Override
			public void run(){
				try {
					sendMessageImpl(message_id, message, socket);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}.start();
	}
	
	protected void sendMessageImpl(byte message_id, Object message, NetworkSocket socket) throws IOException{
		synchronized(socket){	
			final OutputStream stream = socket.getOutputStream();
			stream.write(message_id); 
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(baos);
		    oos.writeObject(message);
	 
		    stream.write(baos.size());
		    stream.write(baos.toByteArray());
		    stream.flush(); 
		}
	}
	
	/**
	 * Créer un thread continu pour la lecture d'un appareil.
	 */
	protected void readSocket(final NetworkSocket socket){

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					while(true){

						
						final InputStream stream = socket.getInputStream();
						
						//Lire le ID du message 
						byte message_id = (byte)stream.read();
						Object message = null;
						int total_count = stream.read();
						if(total_count > 0)
						{
							int count = 0;
							ByteArrayOutputStream streamBuffer = new ByteArrayOutputStream();
							byte[] buffer = new byte[total_count];

							try {

								count = stream.read(buffer, 0, buffer.length);
								streamBuffer.write(buffer, 0, count);
							} catch (IOException e1) {e1.printStackTrace();}

							ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(streamBuffer.toByteArray()));
							message = ois.readObject();
						}
						notifyListeners(message_id, message, socket, socket.device);

						if(message_id == Messages.CLOSE){
							disconnectSocket(socket);
							return;
						}

					}
				}
				catch(SocketException e){
					e.printStackTrace();
					disconnectSocket(socket);
					return;
				}
				catch (Exception e) {
					e.printStackTrace();
					disconnectSocket(socket);
					notifyListeners(Messages.EXCEPTION, e, socket, socket.device);
					return;
				}
			} 
		}).start();
	}
	
	protected void notifyListeners(byte message_id, Object message, NetworkSocket socket, NetworkDevice device){
		for(int i = 0; i < message_listeners.size(); i++)
			message_listeners.get(i).reveiveMessage(message_id, message, socket, device);
	}
	
	public void reveiveMessage(byte message_id, Object message, NetworkSocket socket, NetworkDevice device){
		
		if(message_id == Messages.PING){
			sendMessage(Messages.PONG, getDeviceName(), socket);
		}
		
		if(message_id == Messages.INITIAL_REQUEST){
			sendMessage(Messages.INITIAL_RESPONSE, getDeviceName(), socket);
		}
		
		if(message_id == Messages.ASK_NAME){
			device.setName(message.toString());
			sendMessage(Messages.RESPOND_NAME, getDeviceName(), socket);
		}
		
		if(message_id == Messages.ASK_DEVICE_ID){
			device.device_id = message.toString();
			sendMessage(Messages.RESPOND_DEVICE_ID, getDeviceID(), socket);
			notifyListeners(Messages.CONNECTION, "", socket, device);
		}
		
		if(message_id == Messages.RESPOND_NAME){
			device.setName(message.toString());
		}
		
		if(message_id == Messages.RESPOND_DEVICE_ID){
			device.device_id = message.toString();
			notifyListeners(Messages.CONNECTION, "", socket, device);
		}
	}
	
	public String getDeviceName(){
		return android.os.Build.MODEL;
	}
	
	public String getDeviceID(){
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		return wifiManager.getConnectionInfo().getMacAddress();
	}
	
	public NetworkDevice findDevice(String device_id){
		
		int count = sockets.size();
		for(int i = 0; i < count; i++)
			if(sockets.get(i).device.device_id.equals(device_id))
				return sockets.get(i).device;
		
		return null;
	}
}
