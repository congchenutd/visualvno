package com.fujitsu.us.visualvno.parts;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;

/**
 * DirectEditManager for label renaming
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class LabelDirectEditManager extends DirectEditManager
{
    private final Label _label;

    public LabelDirectEditManager(GraphicalEditPart source, Class<?> editorType, 
                                  CellEditorLocator locator, Label label)
    {
        super(source, editorType, locator);
        _label = label;
    }

    @Override
    protected void initCellEditor() {
        getCellEditor().setValue(_label.getText());
    }
}
