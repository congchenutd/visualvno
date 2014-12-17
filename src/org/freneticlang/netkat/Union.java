package org.freneticlang.netkat;

import java.util.ArrayList;
import java.util.List;

/**
 * The Union policy
 * Based on the code of Prof. Foster
 * Change:
 *      Added support for arbitrary number of policies
 *      e.g., new Union(new PolicyA(), new PolicyB(), new PolicyC(), new PolicyD());
 * 
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class Union implements Policy
{
    private List<Policy> policies = new ArrayList<Policy>();

    @Override
    public String toString()
    {
        String result = "(";
        for(int i = 0; i < policies.size(); ++i)
        {
            if(i != 0)
                result += " | ";
            result += policies.get(i).toString();
        }
        result += ")";
        return result;
    }
    
    public Union(Policy... policies) {
        for(int i = 0; i < policies.length; ++i)
            this.policies.add(policies[i]);
    }
}
