package com.tournesol.network.wifi;

import com.tournesol.network.NetworkDevice;
import com.tournesol.network.NetworkManager;

public class WifiNetworkDevice extends NetworkDevice {

	public String name;
	public String ip;
	
	public WifiNetworkDevice(String name, String ip){
		this.name = name;
		this.ip = ip;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object other){
		if(other instanceof WifiNetworkDevice)
			return ((WifiNetworkDevice)other).ip.equals(ip);
		
		return super.equals(other);
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public NetworkManager getManager() {
		return WifiNetworkManager.i;
	}

	@Override
	public Object getID() {
		return ip;
	}
}
