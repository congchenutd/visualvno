package org.freneticlang.netkat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;


public class MyTest
{

    public static void main(String[] args)
    {
        WebKAT.setServer("http://192.168.56.101:9000");
        
//        (filter (switch = 1));
//        ((filter (ipSrc = 10.0.0.1 & ipDst = 10.0.0.2); port:=2 + 
//        (filter (ipSrc = 10.0.0.1 & ipDst = 10.0.0.3); port:=1 +  
//        (filter (ipDst = 10.0.0.1); port := 3))
        Policy policy = 
            new Sequence(
                new Filter(new Test("switch", "1")),
                new Union(
                    new Sequence(
                        new Filter(new And(new Test("ipSrc", "10.0.0.1"),
                                           new Test("ipDst", "10.0.0.2"))),
                        new Modification("port", "2")
                    ),
                    new Sequence(
                        new Filter(new And(new Test("ipSrc", "10.0.0.1"),
                                           new Test("ipDst", "10.0.0.3"))),
                        new Modification("port", "1")
                    ),
                    new Sequence(
                        new Filter(new Test("ipDst", "10.0.0.1")),
                        new Modification("port", "3")
                    )
                )
            );
        
        System.out.println(policy);
        
        // update the policy and retrieve the flow table of switch 1
        WebKAT.update(policy);
        JsonArray flowtable = WebKAT.flowTable(1);
        
        // pretty print
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(flowtable));
    }

}
