package util;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SolrIndexer {

    public void indexFiles(String directoryPath, String solrUrl) throws IOException, SolrServerException {
	    SolrClient solr = new HttpSolrClient.Builder(solrUrl).build();
//	    String directory1 = "/home/uat/Production/installers/tomcat/webapps/cures_articleimages/50/2023/03/24";
	    File directory = new File(directoryPath);
	    File[] files = directory.listFiles();

	    System.out.println(files.length);
	    if (files != null) {
	        for (File file : files) {
	            if (file.isFile() && file.getName().toLowerCase().endsWith(".json")) {
	                // Read the file
	                StringBuilder content = new StringBuilder();
	                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	                    String line;
	                    while ((line = reader.readLine()) != null) {
	                        content.append(line).append("\n");
	                    }
	                }

	                // Extract article ID from the filename
	                int articleId = extractArticleId(file.getName());
	                System.out.println(" filename format: " + file.getName());
	                if (articleId == -1) {
	                    System.out.println("Invalid filename format: " + file.getName());
	                    continue; // Skip this file if filename format is invalid
	                }

	                // Build a Solr query to retrieve the document by its ID
	                SolrQuery query = new SolrQuery();
	                query.setQuery("article_id:" + articleId);

	                // Execute the query
	                QueryResponse response = solr.query(query);

	                // Check if any documents are returned
	                boolean documentExists = !response.getResults().isEmpty();
	                JSONObject decryptedContent = decryptAndParse(content.toString());
	                if (documentExists) {
	                    // Retrieve the existing document from the response
	                    SolrDocument existingDocument = response.getResults().get(0);

	                    if (decryptedContent != null) {
	                        // Add new content to the existing document
	                        existingDocument.setField("content", decryptedContent.toString());

	                        // Check if the content_new field exists and add it to the document
	                        if (existingDocument.getFieldValue("content_new") == null) {
	                            existingDocument.setField("content_new", decryptedContent.toString());
	                        }

	                        // Retrieve and print the content_new field from the document
	                        Object contentNewFieldValue = existingDocument.getFieldValue("content_new");
	                        if (contentNewFieldValue != null) {
	       //                     System.out.println("content_new: " + contentNewFieldValue.toString());
	                        } else {
	                            System.out.println("content_new not found in the document.");
	                        }

	                        // Update the document in Solr
	                        solr.add(convertToSolrInputDocument(existingDocument));
	                    } else {
	                        System.out.println("Error decrypting and parsing content for file: " + file.getName());
	                        continue; // Skip this file if decryption or parsing fails
	                    }
	                } else {
	                    System.out.println("Document with ID " + articleId + " does not exist in Solr.");
	                }
	            }
	        }
	    }

	    // Commit changes to the Solr index
	    solr.commit();

	    // Close Solr client
	    solr.close();
	}


    public static void main(String[] args) {
        String directoryPath = "C:\\JAVA\\New"; // Directory containing files
        String solrUrl = "http://localhost:8983/solr/test"; // Replace with your Solr collection URL

        SolrIndexer indexer = new SolrIndexer();
        try {
            indexer.indexFiles(directoryPath, solrUrl);
        } catch (IOException | SolrServerException e) {
            e.printStackTrace();
        }
    }

    public JSONObject decryptAndParse(String encodedContent) {
        try {
            // Decode URL-encoded data
            String decodedData = URLDecoder.decode(encodedContent, "UTF-8");

            // Parse the decoded JSON string into a JSONObject
            return new JSONObject(decodedData);
        } catch (UnsupportedEncodingException | JSONException e) {
            e.printStackTrace();
            return null; // Handle decoding or parsing errors
        }
    }

    private int extractArticleId(String filename) {
        try {
            String idStr = filename.substring(filename.lastIndexOf('_') + 1, filename.lastIndexOf('.'));
            return Integer.parseInt(idStr);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return -1; // Invalid filename format
        }
    }

    private SolrInputDocument convertToSolrInputDocument(SolrDocument solrDocument) {
        SolrInputDocument inputDocument = new SolrInputDocument();
        solrDocument.forEach(inputDocument::addField);
        return inputDocument;
    }
}
