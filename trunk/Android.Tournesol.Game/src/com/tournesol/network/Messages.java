package com.tournesol.network;

public class Messages {
	
	//Network messages
	public static final byte PING = 0;
	public static final byte PONG = 1;
	
	//Utiliser pour déterminer si le device existe
	public static final byte INITIAL_REQUEST = 2;
	public static final byte INITIAL_RESPONSE = 3;
	public static final byte DEVICE_FOUND = 4;
	public static final byte EXCEPTION = 5;
	
	//Les trois étapes d'une connection
	public static final byte ASK_NAME = 6;
	public static final byte RESPOND_NAME = 7;
	public static final byte CONNECTION = 8;
	public static final byte ASK_DEVICE_ID = 9;
	public static final byte RESPOND_DEVICE_ID = 10;
	
	public static final byte CLOSE = Byte.MAX_VALUE;
	
	public static final byte KEY_EVENT = 20;
	
	
}
