package com.fujitsu.us.oovn.message;

import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;

public interface Virtualizable
{
    public void virtualize(PhysicalSwitch psw);
}
