package org.freneticlang.netkat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

/**
 * A simple wrap of the WebKAT api
 * Based on the code of Prof. Foster
 * 
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class WebKAT
{
    private static String serverUrl = new String("http://localhost:9000");
    
    public static void setServer(String url) {
        serverUrl = url;
    }
    
    /**
     * Upload a policy
     */
    public static void update(Policy policy) {
        try {
            /* TODO(jnf): Convert policies to JSON properly. */
            String json = "{ data : \"" + policy.toString() + "\", type : \"policy\"}";
            Request.Post(serverUrl + "/update")
                .bodyString(json, ContentType.DEFAULT_TEXT)
                .execute().returnContent();
        } catch (Exception e) {
            System.out.println("update request failed: " + e.toString());
        }
    }

    /**
     * Retrieve the flow table of a given switch as a json array
     */
    public static JsonArray flowTable(long switchId)
    {
        // get the json string from the server
        String response;
        try {
            response = Request.Get(serverUrl + "/" + Long.toString(switchId) + "/flow_table")
                .execute().returnContent().asString();
            
            // FIXME: a workaround for malformed ip address
            // The server returns an ip address in the format of "nwSrc": (10.0.0.1, 32)
            // Convert it to "nwSrc": 10.0.0.1 to conform to json syntax
            Pattern pattern = Pattern.compile("\\((?<ip>[\\d+\\.]+)\\,\\s*\\d+\\)");  // for (10.0.0.1, 32)
            Matcher matcher = pattern.matcher(response);
            
            // replace the entire match "(10.0.0.1, 32)" with the named group ip "10.0.0.1"
            response = matcher.replaceAll("${ip}");
            
        } catch (Exception e) {
            System.out.println("flowtable request failed: " + e.toString());
            return null;
        }
        
        // convert the string to a json array
        try {
            JsonParser paser = new JsonParser();
            return paser.parse(response).getAsJsonArray();
        } catch(Exception e) {
            System.out.println("parsing flowtable result failed: " + e);
            return null;
        }
    }

//    public static void main( String[] args )
//    {
//        WebKAT.setServer("http://192.168.56.101:9000");
//        Policy policy = new Sequence(new Filter(new Test("switch", "0")), 
//                                     new Modification("port", "9"));
//		System.out.println(policy);
//        update(policy);
//        System.out.println(flowTable(0));
//        System.out.println(flowTable(1));
//    }
}
