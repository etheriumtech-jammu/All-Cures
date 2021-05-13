package util;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.BaseCloudSolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class SolrUtil {
	public static HttpSolrClient buildSolrFactory(){
//		final List<String> solrUrls = new ArrayList<String>();
//	
//	    solrUrls.add("http://localhost:8983/solr/#/doctor");
//	
//	    return ( new CloudSolrClient.Builder(solrUrls)).build();
		
//		String urlString = "http://localhost:8983/solr/#/doctor";
//		HttpSolrClient solr = new HttpSolrClient.Builder(urlString).build();
//		//solr.setParser(new XMLResponseParser());
//		return solr;
		
		String serverURL = Constant.SolrServer;
		SolrClient solr = new HttpSolrClient.Builder(serverURL).build();

		((HttpSolrClient) solr).setParser(new XMLResponseParser());
		return (HttpSolrClient) solr;
	}
	
	public static String jsonifyObject(Object obj){
		Gson gson = new GsonBuilder().serializeNulls().create();	
		String jsondata = gson.toJson(obj);
		return jsondata;
	}

}
