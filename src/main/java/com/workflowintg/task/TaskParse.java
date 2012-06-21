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
import com.workflowintg.dispatcher.JettyDispatcherServer;
import com.workflowintg.partner.PartnerContext;

public class TaskParse extends Task {

	private boolean newItem = false;
	private String current = "";
	private int count = 0;
	private String xml="";
	
	private static Logger LOGGER = Logger.getLogger(TaskParse.class);
	
	
	public TaskParse(Workflow w) {
		super(w);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TaskResult runTask() {
		// TODO Auto-generated method stub
		try {
			String fileName = ((PartnerContext)this.getWorkflow().getContext()).getLocalFileName();
					
			FileInputStream fis = new FileInputStream(fileName);
			XMLInputFactory factory = (XMLInputFactory)XMLInputFactory.newInstance();
			factory.setProperty("http://java.sun.com/xml/stream/properties/report-cdata-event", Boolean.TRUE);
			XMLStreamReader staxXmlReader = (XMLStreamReader) factory.createXMLStreamReader(fis);
			for (int event = staxXmlReader.next();event != XMLStreamConstants.END_DOCUMENT;
					event = staxXmlReader.next()) {
				if (event == XMLStreamConstants.START_ELEMENT) {
					String element = staxXmlReader.getLocalName();
					String attrs="";
					for(int i=0;i<staxXmlReader.getAttributeCount();i++){
						attrs+=" "+staxXmlReader.getAttributeLocalName(i)+"=\""+staxXmlReader.getAttributeValue(i)+"\"";
					}
					if(element.equals("ad")){
						xml = "<ad"+attrs+">";
						
					}
					else{
						if(!xml.trim().equals("")){
							xml = xml+"\n<"+element+attrs+">";
						}
					}
				}
				else if (event == XMLStreamConstants.CDATA){
					xml+=("<![CDATA[")+staxXmlReader.getText().trim()+("]]>");
				}
				else if (event == XMLStreamConstants.CHARACTERS){
					xml+=staxXmlReader.getText().trim();
				}	  
				else if (event == XMLStreamConstants.END_ELEMENT) {
					String element = staxXmlReader.getLocalName();
					if(!xml.trim().equals("")){
						xml = xml+"</"+element+">\n";
					}
					if(element.equals("ad")){
						Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
						for(Element e:doc.select("picture>picture_url")){
//							LOGGER.info("Image: "+e.text());
						}
						for(Element e:doc.select("ID")){
//							LOGGER.info("ID: "+e.text());
						}
						JettyDispatcherServer.getRequestQueue().putMessage(xml);
						xml="";
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return new TaskResult("success", "TaskParse failed");
		}
		return new TaskResult("success", "TaskParse finished");
	}
}
