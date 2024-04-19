package util;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

public class SolrIndexer {
    public void indexFiles(String directoryPath, String solrUrl) throws IOException {
        SolrClient solr = new HttpSolrClient.Builder(solrUrl).build();
        
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    // Read the file
                    StringBuilder content = new StringBuilder();
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                    }
                    
                    // Create a Solr document for the file
                    SolrInputDocument document = new SolrInputDocument();
                    document.addField("id", file.getName()); // Unique identifier for the document
                    document.addField("content", content.toString()); // File content
                    
                   // Add the document to Solr
                    try {
                        solr.add(document);
                    } catch (org.apache.solr.client.solrj.SolrServerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        
        // Commit changes to the Solr index
        try {
            solr.commit();
        } catch (org.apache.solr.client.solrj.SolrServerException e) {
            e.printStackTrace();
        }
        
        // Close Solr client
        solr.close();
    }
    public static void main(String[] args) {
        String directoryPath = "/var/solr/data/article_new_core"; // Directory containing files
        String solrUrl = "http://localhost:8983/solr/article_new_core"; // Replace with your Solr collection URL

        SolrIndexer indexer = new SolrIndexer();
        try {
            indexer.indexFiles(directoryPath, solrUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
