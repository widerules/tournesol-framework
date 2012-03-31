package com.tournesol.network;

public interface IMessageListener {
	void reveiveMessage(byte message_id, Object message, NetworkSocket socket, NetworkDevice device);
}
