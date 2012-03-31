package com.tournesol.game.unit;

import com.tournesol.game.utility.Cache;

public class CacheUnit<T extends Unit> extends Cache<T>{

	public CacheUnit(Class<T> cls, int cache_size) {
		super(cls, cache_size);
	}

	@Override
	public T get(){
		
		T unit = super.get();
		if(unit.alive)
		{
			//Vérifier si un unit n'est pas utilisé dans la cache, autrement doubler la cache
			for(int i = 0 ; i < cache_size; i++)
				if(!cache[i].alive){
					cache_index = i - 1;
					return get();
				}
					
			doubleCache();
			return get();
		}
		
		return unit;
	}
	
	@Override
	protected T createInstance(Class<T> cls) throws IllegalAccessException, InstantiationException{
		T unit =  cls.newInstance();
		unit.alive = false;
		return unit;
	}
}
