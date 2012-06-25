package com.workflowintg.partner;

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import com.myworkflow.workflow.Workflow;
import com.myworkflow.workflow.WorkflowObserver;
import com.sun.istack.logging.Logger;
import com.workflowintg.ad.AdWorkflow;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;
import com.workflowintg.task.TaskDownloadFile;
import com.workflowintg.task.TaskParse;

public class PartnerSession implements Runnable, WorkflowObserver {

	private Logger LOGGER = Logger.getLogger(PartnerSession.class);
	private String partner;
	private PartnerContext context;
	private int total = 0;
	private int processed = 0;
	private boolean parseFinished = false;
	private Document config;

	public void setParseFisnished() {
		parseFinished = true;
		checkSessionEnd();
	}

	public synchronized void incrementProcessedTotal() {
		processed++;
	}

	public synchronized void incrementTotal() {
		total++;
	}

	public PartnerSession(String partner) {
		this.partner = partner;
	}

	public String getPartnerName() {
		return partner;
	}

	public Document getPartnerConfiguration() {
		return config;
	}

	private void loadConfiguration() throws IOException {
		LOGGER.info("Loading partner configuration");
		InputStream in = getClass().getResourceAsStream("/test-config.xml");
		config = Jsoup.parse(in, "UTF-8", "", Parser.xmlParser());
	}

	private void setUpContext() {
		context = GuiceServletConfig.getDependencyInjector().getInstance(
				PartnerContext.class);
		context.setFileUrl("http://www.gestionaleimmobiliare.it/" +
				"export/xml/trovit_com/prossima-casa_it.xml");
		context.setPartnerSession(this);
		context.subscribe(this);
	}

	private void executeWorkflow() {

		Workflow w = context.getWorkflowInstance(Workflow.class);
		w.addTask("download", new TaskDownloadFile(w));
		w.addTask("parse", new TaskParse(w));

		context.queueExecutorTask(partner, w);
	}

	public void run() {
		System.out.println("Starting partner session: " + partner);
		try {
			loadConfiguration();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		setUpContext();
		executeWorkflow();
	}

	public synchronized void notifyFinishedEvent(Workflow w) {

		if (w instanceof AdWorkflow
				&& ((AdWorkflow) w).getPartner().equals(partner)) {
			incrementProcessedTotal();
			LOGGER.info("AD PROCESSED [" + partner + "] => total: " + total
					+ " | processed: " + processed);
			checkSessionEnd();
		}
	}

	private void checkSessionEnd() {
		if (total == processed && parseFinished) {
			LOGGER.info("Partner [" + partner + "] context finished.");
			context.finish();
		}
	}

}
