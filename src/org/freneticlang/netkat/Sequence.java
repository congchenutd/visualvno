package org.freneticlang.netkat;

import java.util.ArrayList;
import java.util.List;

/**
 * The Sequence policy
 * Based on the code of Prof. Foster and Cong Chen
 * Change:
 *      Added support for arbitrary number of policies
 *      e.g., new Sequence(new PolicyA(), new PolicyB(), new PolicyC(), new PolicyD());
 * 
 * @author Xi Wang <xi.wang@us.fujitsu.com>
 */

public class Sequence implements Policy {
	
	private List<Policy> policies = new ArrayList<Policy>();

    @Override
    public String toString()
    {
        String result = "(";
        for(int i = 0; i < policies.size(); ++i)
        {
            if(i != 0)
                result += " ; ";
            result += policies.get(i).toString();
        }
        result += ")";
        return result;
    }
    
    public Sequence(Policy... policies) {
        for(int i = 0; i < policies.length; ++i)
            this.policies.add(policies[i]);
    }
}

/* Previous code dated 20141218
public class Sequence implements Policy {
    private Policy left, right;

    public Sequence(Policy left, Policy right) {
        this.left = left;
        this.right = right;
    }

    public String toString() {
        return ("(" + left + " ; " + right + ")");
    }
}
*/