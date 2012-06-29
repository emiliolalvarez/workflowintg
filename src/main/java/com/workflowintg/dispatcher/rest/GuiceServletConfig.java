package com.workflowintg.dispatcher.rest;

import java.util.HashMap;
import java.util.Map;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.myworkflow.main.Configuration;
import com.myworkflow.workflow.TransitionDefinition;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.core.util.FeaturesAndProperties;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import com.workflowintg.context.AdContext;
//import com.workflowintg.context.AdContextProvider;
import com.workflowintg.context.AdTransitionDefinition;
import com.workflowintg.db.DbPool;
import com.workflowintg.dispatcher.RequestQueue;
import com.workflowintg.partner.PartnerTransitionDefinition;

public class GuiceServletConfig extends GuiceServletContextListener {
	
	private static Injector injector;
	
	public static Injector getDependencyInjector(){
		return injector;
	}
	
	@Override
	protected Injector getInjector() {
		
		ServletModule sm = new JerseyServletModule() {
			@Override
			protected void configureServlets() {
				
				// Rest Services Setup
				// Must configure at least one JAX-RS resource or the
				// server will fail to start.
				// Resources
				//bind(StatusResource.class).asEagerSingleton();
				
				// Providers				
				bind(JAXBContextResolver.class).asEagerSingleton();
				
				//Other
				bind(Configuration.class).asEagerSingleton();
				bind(DbPool.class).asEagerSingleton();
				bind(TransitionDefinition.class).annotatedWith(Names.named("Partner")).to(PartnerTransitionDefinition.class).asEagerSingleton();
				bind(TransitionDefinition.class).annotatedWith(Names.named("Ad")).to(AdTransitionDefinition.class).asEagerSingleton();
				//bind(AdContext.class).toProvider(AdContextProvider.class).asEagerSingleton();
				bind(AdContext.class).asEagerSingleton();
				bind(RequestQueue.class).asEagerSingleton();
				
				Map<String, String> params = new HashMap<String, String>();
				
				//Tell Jersey to scan the package
				params.put("com.sun.jersey.config.property.packages", "com.workflowintg.dispatcher.rest.resources");
				
				params.put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE.toString());
				params.put(FeaturesAndProperties.FEATURE_XMLROOTELEMENT_PROCESSING, Boolean.TRUE.toString());
				
				serve("/*").with(GuiceContainer.class, params);
			}
		};
		
		injector = Guice.createInjector(sm);
		
		return injector;
	}
}