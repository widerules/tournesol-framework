package com.tournesol.network.bluetooth;

import android.bluetooth.BluetoothDevice;

import com.tournesol.network.bluetooth.BluetoothNetworkDevice;
import com.tournesol.network.NetworkDevice;
import com.tournesol.network.NetworkManager;

public class BluetoothNetworkDevice extends NetworkDevice{

	public BluetoothDevice device;
	
	public BluetoothNetworkDevice(BluetoothDevice device){
		this.device = device;
	}

	@Override
	public String getName() {
		return device.getName();
	}
	
	@Override
	public void setName(String name) {

	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof BluetoothNetworkDevice)
			return ((BluetoothNetworkDevice)other).device.equals(device);
		
		return super.equals(other);
	}
	
	@Override
	public NetworkManager getManager() {
		return BluetoothManager.i;
	}

	@Override
	public Object getID() {
		return device.getAddress();
	}
}
