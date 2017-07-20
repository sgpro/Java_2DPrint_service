package org.sgpro.wps;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.hibernate.collection.internal.PersistentSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonProvider {

	private static Gson gson;
	static  {
		GsonBuilder gb = new GsonBuilder();
		gb
		.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
		.setDateFormat("yyyy-MM-dd HH:mm:ss");
		
		gb.registerTypeAdapter(Set.class, new SetAdapter());
		gb.registerTypeAdapter(HashSet.class, new SetAdapter());
		gb.registerTypeAdapter(TreeSet.class, new SetAdapter());
		gb.registerTypeAdapter(PersistentSet.class, new SetAdapter());
		gson = gb.create();
	}
	public static Gson getGson() {
		return gson;
	}
	
	static class SetAdapter implements JsonSerializer<Set> {

		@Override
		public JsonElement serialize(Set arg0, Type arg1,
				JsonSerializationContext arg2) {
			// TODO Auto-generated method stub
			
			return null;
			
		}
	}
}
