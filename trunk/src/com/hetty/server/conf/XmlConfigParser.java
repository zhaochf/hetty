package com.hetty.server.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hetty.object.AppServiceSecurity;
import com.hetty.object.Application;
import com.hetty.object.LocalService;
import com.hetty.object.Service;
import com.hetty.object.ServiceProvider;

public class XmlConfigParser implements ConfigParser {
	
	private final static Logger logger = LoggerFactory.getLogger(XmlConfigParser.class);
	
	private String configFile = null;
	private Document document;
	private Element root = null;

	public XmlConfigParser(String configFile) {
		this.configFile = configFile;
		root = getRoot();
	}



	/**
	 * analyse service configure and return a list,the list is a LocalService and each localService 
	 * corresponding a service
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Service> parseService() {
		List<Service> slist = new ArrayList<Service>();

		try {
			Node serviceRoot = root.selectSingleNode("//services");

			List<Element> serviceList = serviceRoot.selectNodes("//service");

			int i = 0;
			for (Element serviceNode : serviceList) {
				String name = serviceNode.attributeValue("name");// service name
				String interfaceStr = serviceNode.attributeValue("interface");// interface package infor

				Class<?> type = Class.forName(interfaceStr);

				LocalService ls = new LocalService("" + i, name);
				ls.setTypeClass(type);
				List<Element> versionList = serviceNode.selectNodes("provider");
				for (Element element : versionList) {
					String vname = element.attributeValue("version");
					String processor = element.attributeValue("class");
					String isDefault = element.attributeValue("default");
					Class<?> pclass = Class.forName(processor);
					ServiceProvider sv = new ServiceProvider(vname, pclass);
					if (isDefault != null && "true".equals(isDefault.trim())) {
						ls.addDefaultVersion(sv);
					} else {
						ls.addProvider(sv);
					}
				}
				slist.add(ls);
				i++;
			}
		} catch (ClassNotFoundException e) {
			logger.error(e.getMessage(),e);
			throw new RuntimeException("read the service config file failured,please check.");
		}

		return slist;
	}

	
	/**
	 * 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Application> parseApplication() {
		List<Application> alist = new LinkedList<Application>();
		Element aroot = getRoot();
		Node root = aroot.selectSingleNode("//applications");
		List<Element> appList = root.selectNodes("application");
		for (Element e : appList) {
			String key = e.attributeValue("appKey");
			String secret = e.attributeValue("appSecret");
			String name = e.attributeValue("appName");
			Application app = new Application();
			app.setKey(key);
			app.setSecret(secret);
			app.setName(name);
			alist.add(app);
		}
		return alist;
	}

	
	
	/**
	 * get the config xml's security info
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> parseSecurity() {
		List<Object> olist = new LinkedList<Object>();
		Element aroot = getRoot();
		Node root = aroot.selectSingleNode("//security-settings");
		List<Element> sList = root.selectNodes("security-setting");
		for (Element se : sList) {
			String appKey = se.attributeValue("appKey");
			String sname = se.attributeValue("service");
			if (null != sname) {
				String version = se.attributeValue("version");
				if (version == null) {
					version = "0";
				}
				AppServiceSecurity ass = new AppServiceSecurity();
				ass.setApplicationKey(appKey);
				ass.setServiceName(sname);
				ass.setServiceVersion(version);
				olist.add(ass);
			}
		}
		return olist;
	}

	@SuppressWarnings("unchecked")
	private Element getRoot() {
		try {
			Document doc = getDocument();
			List<Element> list = doc.selectNodes("//deployment");
			if (list.size() > 0) {
				Element aroot = list.get(0);
				return aroot;
			}
		} catch (DocumentException e) {
			e.printStackTrace();
		}catch(IOException e1){
			e1.printStackTrace();
		}
		return null;
	}

	private Document getDocument() throws DocumentException, IOException {
		InputStream is = getFileStream();
		try {
			if (document == null) {
				SAXReader reader = new SAXReader();
				reader.setValidation(false);
				if (is == null) {
					throw new RuntimeException(
							"we can not find the service config file:"
									+ configFile);
				}
				document = reader.read(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("get xml Document failed.");
		} finally {
			is.close();
		}
		return document;
	}

	private InputStream getFileStream() {
		return getFileStream(configFile);
	}

	private InputStream getFileStream(String file) {
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(file);
		return is;
	}
}
