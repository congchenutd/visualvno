package org.freneticlang.netkat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;


public class MyTest
{

    public static void main(String[] args)
    {
        WebKAT.setServer("http://192.168.56.101:9000");
        
    	//Sample NetKat routing policy program using local compilation 
    	//((filter (switch = 1)) ; 
    	//(((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.2))) ; (port := 2)) | 
    	//((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.3))) ; (port := 1)) | 
    	//((filter (ipDst = 10.0.0.1)) ; (port := 3))))
    	
        //Sample NetKat routing policy program written in Java
    	Policy samplePolicy = 
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
    	
    	//Native NetKat Program for VNO1 routing policy using local compilation 20141222-1440
    	/*
		(((filter (switch = 1)) ; 
		(((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.2))) ; (port := 2)) | 
		((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.3))) ; (port := 1)) | 
		((filter (ipDst = 10.0.0.1)) ; (port := 3)) | 
		((filter ((ipSrc = 10.0.0.3) and (ipDst = 10.0.0.2))) ; (port := 2)))) | 
		
		((filter (switch = 2)) ; 
		(((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.1))) ; (port := 2)) | 
		((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.3))) ; (port := 2)) | 
		((filter (ipDst = 10.0.0.2)) ; (port := 5)) | 
		((filter ((ipSrc = 10.0.0.3) and (ipDst = 10.0.0.1))) ; (port := 1)))) | 
		
		((filter (switch = 4)) ; 
		(((filter ((ipSrc = 10.0.0.3) and (ipDst = 10.0.0.1))) ; (port := 2)) | 
		((filter ((ipSrc = 10.0.0.3) and (ipDst = 10.0.0.2))) ; (port := 1)) | 
		((filter (ipDst = 10.0.0.3)) ; (port := 5)) | 
		((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.1))) ; (port := 1)))) | 
		
		((filter (switch = 5)) ; 
		(((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.1))) ; (port := 1)) | 
		((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.3))) ; (port := 2)) | 
		((filter ((ipSrc = 10.0.0.3) and (ipDst = 10.0.0.2))) ; (port := 1)))))
    	*/
    	//Java-based NetKat Program for VNO1 20141222-1720
    	Policy VNO1SW1Policy = 
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
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.3"),
                                               new Test("ipDst", "10.0.0.2"))),
                            new Modification("port", "2")
                        )
                    )
                );
    	
    	Policy VNO1SW2Policy = 
                new Sequence(
                    new Filter(new Test("switch", "2")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "2")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.3"))),
                            new Modification("port", "2")
                        ),
                        new Sequence(
                            new Filter(new Test("ipDst", "10.0.0.2")),
                            new Modification("port", "5")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.3"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "1")
                        )
                    )
                );

    	Policy VNO1SW4Policy = 
                new Sequence(
                    new Filter(new Test("switch", "4")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.3"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "2")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.3"),
                                               new Test("ipDst", "10.0.0.2"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new Test("ipDst", "10.0.0.3")),
                            new Modification("port", "5")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "1")
                        )
                    )
                );
        	
    	Policy VNO1SW5Policy = 
                new Sequence(
                    new Filter(new Test("switch", "5")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.1"),
                                               new Test("ipDst", "10.0.0.3"))),
                            new Modification("port", "2")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.3"),
                                               new Test("ipDst", "10.0.0.2"))),
                            new Modification("port", "1")
                        )
                    )
                );
        
    	Policy VNO1Policy = new Union(VNO1SW1Policy, VNO1SW2Policy, VNO1SW4Policy, VNO1SW5Policy);
        System.out.println("VNO1Policy = " + VNO1Policy);
    	//End of Java-based NetKat Program for VNO1 20141222-1720
    	
    	
    	//Native NetKat Program for VNO2 routing policy using local compilation 20141223-1618
    	/*
		(((filter (switch = 4)) ; 
		(((filter ((ipSrc = 10.0.0.4) and (ipDst = 10.0.0.5))) ; (port := 3)) | 
		((filter (ipDst = 10.0.0.4)) ; (port := 6)))) | 
		
		((filter (switch = 2)) ; 
		(((filter ((ipSrc = 10.0.0.5) and (ipDst = 10.0.0.4))) ; (port := 4)) | 
		((filter (ipDst = 10.0.0.5)) ; (port := 6)))) | 
		
		((filter (switch = 7)) ; 
		(((filter ((ipSrc = 10.0.0.4) and (ipDst = 10.0.0.5))) ; (port := 1)) | 
		((filter ((ipSrc = 10.0.0.5) and (ipDst = 10.0.0.4))) ; (port := 2)))))
    	*/
    	//Java-based NetKat Program for VNO2 20141223-1632
    	Policy VNO2SW4Policy = 
                new Sequence(
                    new Filter(new Test("switch", "4")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.4"),
                                               new Test("ipDst", "10.0.0.5"))),
                            new Modification("port", "3")
                        ),
                        new Sequence(
                            new Filter(new Test("ipDst", "10.0.0.4")),
                            new Modification("port", "6")
                        )
                    )
                );
    	
    	Policy VNO2SW2Policy = 
                new Sequence(
                    new Filter(new Test("switch", "2")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.4"),
                                               new Test("ipDst", "10.0.0.5"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new Test("ipDst", "10.0.0.5")),
                            new Modification("port", "6")
                        )
                    )
                );
    	
    	Policy VNO2SW7Policy = 
                new Sequence(
                    new Filter(new Test("switch", "7")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.4"),
                                               new Test("ipDst", "10.0.0.5"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.5"),
                                               new Test("ipDst", "10.0.0.4"))),
                            new Modification("port", "2")
                        )
                    )
                );
        
    	Policy VNO2Policy = new Union(VNO2SW4Policy, VNO2SW2Policy, VNO2SW7Policy);
        System.out.println("VNO2Policy = " + VNO2Policy);
        //End of Java-based NetKat Program for VNO2 20141223-1632
    	
    	
        //Native NetKat Program for VNO3 routing policy using local compilation 20141223-1729
        /*
        (((filter (switch = 1)) ; 
        (((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.6))) ; (port := 1)) | 
        ((filter (ipDst = 10.0.0.1)) ; (port := 3)) | 
        ((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.6))) ; (port := 1)))) | 

        ((filter (switch = 2)) ; 
        (((filter ((ipSrc = 10.0.0.6) and (ipDst = 10.0.0.1))) ; (port := 1)) | 
        ((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.6))) ; (port := 1)) | 
        ((filter (ipDst = 10.0.0.2)) ; (port := 5)))) | 

        ((filter (switch = 3)) ; 
        (((filter (ipDst = 10.0.0.6)) ; (port := 4)) | 
        ((filter ((ipSrc = 10.0.0.6) and (ipDst = 10.0.0.1))) ; (port := 3)) | 
        ((filter ((ipSrc = 10.0.0.6) and (ipDst = 10.0.0.2))) ; (port := 1)))) | 

        ((filter (switch = 4)) ; 
        (((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.6))) ; (port := 4)) | 
        ((filter ((ipSrc = 10.0.0.6) and (ipDst = 10.0.0.1))) ; (port := 2)) | 
        ((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.6))) ; (port := 3)) | 
        ((filter ((ipSrc = 10.0.0.6) and (ipDst = 10.0.0.2))) ; (port := 2)))) | 

        ((filter (switch = 5)) ; 
        (((filter ((ipSrc = 10.0.0.1) and (ipDst = 10.0.0.6))) ; (port := 2)) | 
        ((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.6))) ; (port := 2)))) | 

        ((filter (switch = 6)) ; 
        (((filter ((ipSrc = 10.0.0.6) and (ipDst = 10.0.0.1))) ; (port := 1)) | 
        ((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.6))) ; (port := 2)))) | 

        ((filter (switch = 7)) ; 
        (((filter ((ipSrc = 10.0.0.6) and (ipDst = 10.0.0.1))) ; (port := 2)) | 
        ((filter ((ipSrc = 10.0.0.2) and (ipDst = 10.0.0.6))) ; (port := 3)))))
        */
        //Java-based NetKat Program for VNO3 20141223-1740
    	Policy VNO3SW1Policy = 
                new Sequence(
                    new Filter(new Test("switch", "1")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.1"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new Test("ipDst", "10.0.0.1")),
                            new Modification("port", "3")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "1")
                        )
                    )
                );
    	
    	Policy VNO3SW2Policy = 
                new Sequence(
                    new Filter(new Test("switch", "2")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.6"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new Test("ipDst", "10.0.0.2")),
                            new Modification("port", "5")
                        )
                    )
                );
    	
    	Policy VNO3SW3Policy = 
                new Sequence(
                    new Filter(new Test("switch", "3")),
                    new Union(
                        new Sequence(
                            new Filter(new Test("ipDst", "10.0.0.6")),
                            new Modification("port", "4")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.6"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "3")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.6"),
                                               new Test("ipDst", "10.0.0.2"))),
                            new Modification("port", "1")
                        )
                    )
                );
    	
    	Policy VNO3SW4Policy = 
                new Sequence(
                    new Filter(new Test("switch", "4")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.1"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "4")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.6"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "2")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "3")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.6"),
                                               new Test("ipDst", "10.0.0.2"))),
                            new Modification("port", "2")
                        )
                    )
                );
    	
    	Policy VNO3SW5Policy = 
                new Sequence(
                    new Filter(new Test("switch", "5")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.1"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "2")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "2")
                        )
                    )
                );
    	
    	Policy VNO3SW6Policy = 
                new Sequence(
                    new Filter(new Test("switch", "6")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.6"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "1")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "2")
                        )
                    )
                );
    	
    	Policy VNO3SW7Policy = 
                new Sequence(
                    new Filter(new Test("switch", "7")),
                    new Union(
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.6"),
                                               new Test("ipDst", "10.0.0.1"))),
                            new Modification("port", "2")
                        ),
                        new Sequence(
                            new Filter(new And(new Test("ipSrc", "10.0.0.2"),
                                               new Test("ipDst", "10.0.0.6"))),
                            new Modification("port", "3")
                        )
                    )
                );
    	
    	Policy VNO3Policy = new Union(VNO3SW1Policy, VNO3SW2Policy, VNO3SW3Policy, VNO3SW4Policy, VNO3SW5Policy, VNO3SW6Policy, VNO3SW7Policy);
        System.out.println("VNO3Policy = " + VNO3Policy);
        //End of Java-based NetKat Program for VNO3 20141223-1740
        
        //Generate combined policy for VNO1, VNO2 and VNO3;
        Policy GlobalPolicy = new Union(VNO1Policy, VNO2Policy, VNO3Policy);
        System.out.println("GlobalPolicy = " + GlobalPolicy);
        
        // update the policy and retrieve the flow table of switch 1
        WebKAT.update(GlobalPolicy);
        JsonArray flowtable = WebKAT.flowTable(1);
        
        // pretty print
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(flowtable));
    }
}
