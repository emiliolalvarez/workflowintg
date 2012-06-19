package com.workflowintg.dispatcher.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;

import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.json.JSONJAXBContext;

@Provider
@Singleton
public class JAXBContextResolver implements ContextResolver<JAXBContext> {

	// TODO: check if it can be with out this array.
	@SuppressWarnings("rawtypes")
	public static final Class[] CLASSES_TO_BE_BOUND = {
		Class.class,
	};
	
	// TODO: find a better way to do that!
	public static final JSONConfiguration CONFIGURATION = JSONConfiguration
			.mapped().arrays().build();

	private JAXBContext context;

	public JAXBContextResolver() throws Exception {
		this.context = new JSONJAXBContext(CONFIGURATION, CLASSES_TO_BE_BOUND);
	}

	public JAXBContext getContext(Class<?> aClass) {
		return context;
	}
}