package util;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SolrIndexer {
    public void indexFiles(String directoryPath, String solrUrl) throws IOException {
        SolrClient solr = new HttpSolrClient.Builder(solrUrl).build();

        File directory = new File(directoryPath);
        File[] files = directory.listFiles();

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

                    // Create a Solr document for the file
                    SolrInputDocument document = new SolrInputDocument();
                    document.addField("id", file.getName()); // Unique identifier for the document
                    String decryptcontent = decryptData(content.toString());
                    document.addField("content", decryptcontent); // File content
                    System.out.println(file.getName());
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
        String directoryPath = "C:\\JAVA\\New"; // Directory containing files
        String solrUrl = "http://localhost:8983/solr/test"; // Replace with your Solr collection URL

        SolrIndexer indexer = new SolrIndexer();
        try {
            indexer.indexFiles(directoryPath, solrUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String decryptData(String encryptedData) throws UnsupportedEncodingException {
        try {
            // Decode URL-encoded data
            String decodedData = URLDecoder.decode(encryptedData, "UTF-8");

            // Convert the decoded data to JSON object
            JSONObject json = new JSONObject(decodedData);

            // Extract information from the JSON object
            long time = json.getLong("time");
            JSONArray blocks = json.getJSONArray("blocks");

            // Iterate over the blocks and decrypt each one
            StringBuilder decryptedText = new StringBuilder();
            for (int i = 0; i < blocks.length(); i++) {
                JSONObject block = blocks.getJSONObject(i);
                if (block.has("data")) {
                    JSONObject data = block.getJSONObject("data");
                    if (data.has("text")) {
                        String text = data.getString("text");
                        // Perform decryption here (if needed)
                        decryptedText.append(text).append("\n");
                    }
                }
            }

            // Return the decrypted text or process it further
            return decryptedText.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "Error occurred while decrypting the data.";
        }
    }
}
