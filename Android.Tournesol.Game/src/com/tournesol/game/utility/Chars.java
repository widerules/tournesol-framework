package com.tournesol.game.utility;

import java.io.Serializable;

import android.util.FloatMath;

public class Chars implements Serializable{

	private static final long serialVersionUID = -545142887292002663L;
	
	public static final char LINE_BREAK = '\n';
	public static char DECIMAL_SEPARATOR = ',';
	public static final int MAX_CHAR = 200;
	public static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	
	public char[] chars;
	public int length = 0;
	
	public Chars(){
		chars = new char[MAX_CHAR];
	}
	
	public Chars(int capacity){
		chars = new char[capacity];
	}
	
	public Chars(String str){
		chars = new char[str.length()];
		set(str);
	}
	
	public void push(char c){
		
		move(0);
		chars[0] = c;
		length++;
	}
	
	public void add(char c){
		chars[length] = c;
		length++;
	}
	
	public void add(char[] chars){
		add(chars, 0, chars.length);
	}
	
	public void add(char[] chars, int start_index, int length){
		for(int i = start_index ; i < start_index + length; i++)
			add(chars[i]);
	}
	
	public void add(Chars chars){
		add(chars.chars, 0, chars.length);
	}
	
	private static final Chars chars_number = new Chars();
	
	public void add(int number){
		add((long)number);
	}
	
	public void add(long number){
		
		long push = number;
		chars_number.reset();
		
		if(push > 0){
			while (push > 0) {
				chars_number.push(DIGITS[(int)(push % 10)]);
				push /= 10;
			}
			
			add(chars_number);
		}
		else
			add(DIGITS[0]);
	}
	
	public void add(float number, int decimal_count){
		add((double)number, decimal_count);
	}
	
	public void add(double number, int decimal_count){
		int push = (int)Math.round(number * Math.pow(10, decimal_count));
		chars_number.reset();
		
		if(push > 0){
			while (push > 0) {
				chars_number.push(DIGITS[push % 10]);
				push /= 10;
				
				if(chars_number.length == decimal_count){
					chars_number.push(DECIMAL_SEPARATOR);
					
					if(push <= 0)
						chars_number.push(DIGITS[0]);
				}
			}
			
			while(chars_number.length <= decimal_count){
				if(chars_number.length == decimal_count)
					chars_number.push(DECIMAL_SEPARATOR);

				chars_number.push(DIGITS[0]);
			}
			
			add(chars_number);
		}
		else
			add(DIGITS[0]);
	}
	
	public void add(String string){
		for(int i = 0 ; i < string.length() ; i++)
			add(string.charAt(i));
	}
	
	public void set(String string){
		this.reset();
		this.add(string);
	}
	
	public void replace(char c, int index){
		if(index < length)
			chars[index] = c;
	}
	
	public void insert(char c, int index)
	{
		if(index >= length)
			return;
		
		move(index);
		chars[index] = c;
	}
	
	public void reset(){
		length = 0;
	}
	
	public void copy(char[] chars){
		
		for(int i = 0; i < chars.length ; i++)
			if(i >= length)
				return;
			else
				chars[i] = this.chars[i];
	}
	
	public void copy(Chars chars){
		
		chars.reset();
		for(int i = 0; i < chars.chars.length ; i++)
			if(i >= length)
				return;
			else
				chars.add(this.chars[i]);
	}
	
	public int count(char c){
		int count = 0;
		for(int i = 0; i < length; i++)
			if(chars[i] == c)
				count++;
		
		return count;
	}
	
	private static final Cache<Chars> cache_split = new Cache<Chars>(Chars.class, 50); 
	public int split_length;
	public Chars[] split(char split){
		 
		split_length = count(split);
		int char_index = 0;
		for(int split_index = 0 ; split_index <= split_length; split_index++){
			
			cache_split.cache[split_index].reset();
			if(chars.length <= char_index)
				continue;
			
			char current_char = chars[char_index];
			while(current_char != split && char_index < length){
				
				cache_split.cache[split_index].add(current_char);
				char_index++;
				
				if(char_index < length)
					current_char = chars[char_index];
			}
			
			char_index++;
		}
		
		return cache_split.cache;
	}
	
	private void move(int index){ 
		
		if(length == 0)
			return;
		
		for(int i = length - 1; i >= index ; i--)
			chars[i + 1] = chars[i];
	}
	
	@Override
	public String toString(){
		return new String(chars, 0, length);
	}
	
}
