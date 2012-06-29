package com.workflowintg.task;

import java.io.FileInputStream;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.myworkflow.TaskResult;
import com.myworkflow.task.Task;
import com.myworkflow.workflow.Workflow;
import com.workflowintg.context.AdContext;
import com.workflowintg.dispatcher.JettyDispatcherServer;
import com.workflowintg.dispatcher.rest.GuiceServletConfig;
import com.workflowintg.partner.PartnerAd;
import com.workflowintg.partner.PartnerContext;

public class TaskParse extends Task {

	private int count = 0;
	private String xml = "";
	private TaskResult result = null;
	private static Logger LOGGER = Logger.getLogger(TaskParse.class);

	public TaskParse(Workflow w) {
		super(w);
	}

	@Override
	public TaskResult runTask() {

		PartnerContext context = (PartnerContext) this.getWorkflow()
				.getContext();
		AdContext adContext = GuiceServletConfig.getDependencyInjector()
				.getInstance(AdContext.class);
		adContext.subscribe(context.getPartnerSession());

		LOGGER.info("Parsing file from "
				+ context.getPartnerSession().getPartnerName() + " session");

		try {
			String fileName = ((PartnerContext) this.getWorkflow().getContext())
					.getLocalFileName();

			FileInputStream fis = new FileInputStream(fileName);
			XMLInputFactory factory = (XMLInputFactory) XMLInputFactory
					.newInstance();
			factory.setProperty(
					"http://java.sun.com/xml/stream/properties/report-cdata-event",
					Boolean.TRUE);
			XMLStreamReader staxXmlReader = (XMLStreamReader) factory
					.createXMLStreamReader(fis);
			for (int event = staxXmlReader.next(); event != XMLStreamConstants.END_DOCUMENT; event = staxXmlReader
					.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					String element = staxXmlReader.getLocalName();
					String attrs = "";
					for (int i = 0; i < staxXmlReader.getAttributeCount(); i++) {
						attrs += " " + staxXmlReader.getAttributeLocalName(i)
								+ "=\"" + staxXmlReader.getAttributeValue(i)
								+ "\"";
					}
					if (element.equals("ad")) {
						xml = "<ad" + attrs + ">";

					} else {
						if (!xml.trim().equals("")) {
							xml = xml + "\n<" + element + attrs + ">";
						}
					}
				} else if (event == XMLStreamConstants.CDATA) {
					xml += ("<![CDATA[") + staxXmlReader.getText().trim()
							+ ("]]>");
				} else if (event == XMLStreamConstants.CHARACTERS) {
					xml += staxXmlReader.getText().trim();
				} else if (event == XMLStreamConstants.END_ELEMENT) {
					String element = staxXmlReader.getLocalName();
					if (!xml.trim().equals("")) {
						xml = xml + "</" + element + ">\n";
					}
					if (element.equals("ad")) {
						count++;
						enqueueAd(xml, context);
						xml = "";
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			result = new TaskResult("success", "TaskParse failed");
		}
		LOGGER.info(context.getPartnerSession().getPartnerName()
				+ " parse finished. Parsed " + count);
		result = result == null ? new TaskResult("success",
				"TaskParse finished") : result;
		context.getPartnerSession().setParseFisnished();
		return result;
	}

	private void enqueueAd(String xml, PartnerContext context) {
		PartnerAd ad = new PartnerAd();
		ad.setXml(xml);
		ad.setPartner(context.getPartnerSession().getPartnerName());
		applyTransformation(context, ad);
		JettyDispatcherServer.getRequestQueue().putMessage(ad);
		context.getPartnerSession().incrementTotal();
	}

	private void applyTransformation(PartnerContext context, PartnerAd ad) {

		Document config = context.getPartnerSession().getPartnerConfiguration();
		Document doc = Jsoup.parse(ad.getXml(), "", Parser.xmlParser());
		try {
			for (Element e : config.select("map>tag")) {
				if (e.attr("type").equals("title")) {
					StringBuilder strValue = new StringBuilder();
					for (Element param : e.select("parameter")) {
						strValue.append(doc.select(param.attr("name")).first()
								.text()
								+ "<br />");
					}
					ad.setTitle(strValue.toString());
				} else if (e.attr("type").equals("description")) {
					StringBuilder strValue = new StringBuilder();
					for (Element param : e.select("parameter")) {
						strValue.append(doc.select(param.attr("name")).first()
								.text()
								+ "<br />");
					}
					ad.setDescription(strValue.toString());
				} else if (e.attr("type").equals("image_url")) {
					for (Element param : e.select("parameter")) {
						for (Element image : doc.select(param.attr("name"))) {
							ad.addImageUrl(image.text());
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
