package com.tournesol.game.utility;

import java.lang.reflect.Array;

public class Cache<T> {

	public Class<T> cls;
	public T[] cache;
	public int cache_index;
	public int cache_size;
	
	@SuppressWarnings("unchecked")
	public Cache(Class<T> cls, int cache_size){
		this.cls = cls;
		this.cache_size = cache_size;
		cache = (T[])Array.newInstance(cls, cache_size);
		
		for(int i = 0 ; i < cache_size; i++)
			try {
				cache[i] = createInstance(cls);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			}
	}
	
	public T get(){
		
		cache_index++;
		if(cache_index == cache_size)
			cache_index = 0;
		
		return cache[cache_index];
	}
	
	@SuppressWarnings("unchecked")
	protected void doubleCache()
	{
		T[] newCache = (T[])Array.newInstance(cls, cache.length * 2);
		for(int i = 0 ; i < newCache.length; i++)
		{
			if(i < cache.length)
				newCache[i] = cache[i];
			else
				try {
					newCache[i] = createInstance(cls);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				}
		}
		
		cache = newCache;
		cache_size = cache.length;
	}
	
	protected T createInstance(Class<T> cls) throws IllegalAccessException, InstantiationException{
		return cls.newInstance();
	}
}
