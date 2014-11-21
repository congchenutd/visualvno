package com.fujitsu.us.oovn.message;

import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;

public interface Devirtualizable
{
    public void devirtualize(VirtualSwitch vsw);
}
