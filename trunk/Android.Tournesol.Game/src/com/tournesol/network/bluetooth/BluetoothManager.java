package com.tournesol.network.bluetooth;

import java.util.ArrayList;
import java.util.UUID;

import com.tournesol.network.bluetooth.BluetoothManager;
import com.tournesol.network.bluetooth.BluetoothNetworkDevice;
import com.tournesol.network.bluetooth.BluetoothNetworkSocket;
import com.tournesol.network.Messages;
import com.tournesol.network.NetworkDevice;
import com.tournesol.network.NetworkManager;
import com.tournesol.network.NetworkSocket;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class BluetoothManager extends NetworkManager {

	private static UUID UUID_RFCOMM_GENERIC =  UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private static boolean accept_loop = false;
	
	//Instance singleton
	public final static BluetoothManager i = new BluetoothManager(); 
	
	final BroadcastReceiver receiver = new BroadcastReceiver(){
		public void onReceive(Context context, Intent intent){
			
			try
			{
				if(BluetoothDevice.ACTION_FOUND.equals(intent.getAction())){
					BluetoothDevice device =  intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					deviceFound(new BluetoothNetworkDevice(device));
				}
				
				if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(intent.getAction())){
					context.unregisterReceiver(receiver);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	};
	
	public void findDevicesImpl() {
		context.registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
		context.registerReceiver(receiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter != null)
			bluetoothAdapter.startDiscovery();
	}
	
	@Override
	protected NetworkSocket connectDeviceImpl(NetworkDevice device) throws Exception {
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter == null)
			return null;
		
		bluetoothAdapter.cancelDiscovery();
		BluetoothNetworkDevice bluetoothDevice = (BluetoothNetworkDevice)device;
		 
		BluetoothSocket socket = bluetoothDevice.device.createRfcommSocketToServiceRecord(UUID_RFCOMM_GENERIC);
		socket.connect(); 
		BluetoothNetworkSocket networkSocket = new BluetoothNetworkSocket(device, socket);
		sockets.add(networkSocket);
		this.readSocket(networkSocket);
		
		//Envoyer une demande de nom
		this.sendMessageImpl(Messages.ASK_NAME, getDeviceName(), networkSocket);
		
		//Envoyer une demande d'identifiant de l'appareil
		this.sendMessageImpl(Messages.ASK_DEVICE_ID, getDeviceID(), networkSocket);
		
		return networkSocket;
	}
	
	@Override
	public boolean isFindingDevices(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter != null)
			return bluetoothAdapter.isDiscovering();
		
		return false;
	}
	
	public void makeDiscoverable(){
		Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
		discoverableIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(discoverableIntent);
	}
	
	@Override
	public boolean isDiscoverable(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter != null)
			return bluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE;
		
		return false;
	}
	
	public boolean cancelFindingDevices(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter != null)
			return bluetoothAdapter.cancelDiscovery();
		
		return false;
	}
	
	public ArrayList<BluetoothDevice> getBondedDevices(){
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter != null)
			return getBondedDevices(BluetoothAdapter.getDefaultAdapter());
		
		return new ArrayList<BluetoothDevice>();
	}
	
	public ArrayList<BluetoothDevice> getBondedDevices(BluetoothAdapter bluetoothAdapter){
		if(bluetoothAdapter != null)
			return new ArrayList<BluetoothDevice>(bluetoothAdapter.getBondedDevices());
		
		return new ArrayList<BluetoothDevice>();
	}
	 
	@Override
	public void acceptDevicesImpl() throws Exception{
		
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter == null)
			return;
		
		while(accept_loop)
		{
			BluetoothServerSocket serverSocket = 
				bluetoothAdapter.listenUsingRfcommWithServiceRecord(context.getPackageName(), UUID_RFCOMM_GENERIC);
			
			BluetoothSocket socket = serverSocket.accept();
			BluetoothNetworkSocket networkSocket = new BluetoothNetworkSocket(new BluetoothNetworkDevice(socket.getRemoteDevice()), socket);
			sockets.add(networkSocket);
			serverSocket.close();
			readSocket(networkSocket);
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

}
