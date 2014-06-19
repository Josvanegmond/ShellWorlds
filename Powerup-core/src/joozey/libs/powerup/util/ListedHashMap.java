package joozey.libs.powerup.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class ListedHashMap<K,V>
{	
	private HashMap<K,ArrayList<V>> map;
	
	public ListedHashMap()
	{
		this.map = new HashMap<K,ArrayList<V>>();
	}

	public V put( K key, V value )
	{
		if( this.map.containsKey(key) == false )
		{
			this.map.put( key, new ArrayList<V>() );
		}

		this.map.get( key ).add( value );
		
		return value;
	}

	public void clear()
	{
		this.map.clear();
	}

	public boolean containsKey(Object key)
	{
		return this.map.containsKey( key );
	}

	public boolean containsValue(Object value)
	{
		for( ArrayList<V> list : this.map.values() )
		{
			if( list.contains( value ) == true ) { return true; }
		}
		
		return false;
	}
	
	public ArrayList<V> get( Object key )
	{
		ArrayList<V> value = this.map.get(key);
		
		return value;
	}

	public boolean isEmpty()
	{
		return this.map.isEmpty();
	}

	public Set<K> keySet()
	{
		return this.map.keySet();
	}

	public ArrayList<V> remove(Object key)
	{
		return this.map.remove( key );
	}

	public int size()
	{
		return this.map.size();
	}

	public Collection<ArrayList<V>> values()
	{
		return this.map.values();
	}

	public void putAll( K key, V[] values )
	{
		for( V value : values )
		{
			this.put( key, value );
		}
	}
}
