package org.spectraLogic.systemMessagesTracker.metric;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.spectraLogic.systemMessagesTracker.database.Column;
import org.spectraLogic.systemMessagesTracker.database.Table;

public interface Metric {

	default Map<String,String> getSqlData() throws IllegalArgumentException, IllegalAccessException{
		Map<String,String> columns = new HashMap<String, String>();

		Field[] fields = this.getClass().getDeclaredFields();
		for(Field field: fields){
			Annotation[] annotations = field.getAnnotations();
			for(Annotation annotation: annotations){
				if(annotation instanceof Column){
					columns.put(((Column)annotation).name(), field.get(this).toString());
				}
			}
		}

		return columns;
	}

	default String getSQLTable(){
		Annotation[] annotations = this.getClass().getAnnotations();
		for(Annotation annotation: annotations){
			if(annotation instanceof Table){
				return ((Table)annotation).name();
			}
		}

		return "";
	}

}
