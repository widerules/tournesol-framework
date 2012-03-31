package com.tournesol.network;

public abstract class NetworkDevice {
	
	public String device_id;
	
	public abstract String getName();
	public abstract Object getID();
	public abstract void setName(String name);
	public abstract NetworkManager getManager();
	
	
	public boolean isConnected(){
		return getManager().isDeviceConnected(this);
	}
	
	@Override
	public boolean equals(Object other){
		if(other instanceof NetworkDevice)
			return ((NetworkDevice)other).getID().equals(this.getID());
		
		return super.equals(other);
	}
}
