package com.fujitsu.us.visualvno.model;

import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * Validates the value of a CellEditor
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class NumberValidator implements ICellEditorValidator
{
    private int _min = 0;
    
    public NumberValidator(int min) {
        _min = min;
    }
    
    @Override
    public String isValid(Object value)
    {
        int intValue = -1;
        try {
            intValue = Integer.parseInt((String) value);
        }
        catch (NumberFormatException exc) {
            return "Not a number";
        }
        return (intValue >= _min) ? null : "Value must be >= " + _min;
    }
}
