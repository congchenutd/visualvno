package com.fujitsu.us.visualvno.figures;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Rectangle;

import com.fujitsu.us.visualvno.model.PortModel;

/**
 * A XYLayout that keeps the children in a circule
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public class CircleLayout extends XYLayout
{

    @Override
    public void layout(IFigure container)
    {
        List<PortFigure> ports = getPorts(container);
        int portCount = ports.size();
        int portWidth = PortModel.DEFAULT_WIDTH;
        
        // arrange the ports in a circle
        int radius = container.getSize().width() / 2 - portWidth / 2;
        for(int i = 0; i < portCount; ++i)
        {
            PortFigure child = ports.get(i);
            Rectangle constraint = (Rectangle) getConstraint(child);
            if(constraint == null)
                continue;
            
            int x = constraint.x;
            int y = constraint.y;
            
            // just added, arrange the ports clockwise
            if(x == 0 && y == 0)
            {
                double theta = 2 * Math.PI / portCount * i;        // clockwise
                x = (int) (radius + radius * Math.sin(theta)); // relative coordinates
                y = (int) (radius - radius * Math.cos(theta));
            }
            // ensure the ports are on a circle
            else
            {
                int dx = x - radius;
                int dy = y - radius;
                int d = (int) Math.sqrt(dx * dx + dy * dy);
                if(d == 0)  // avoid divided by 0
                    x = y = 0;
                else if(d != radius)
                {
                    x = radius + radius * dx / d;
                    y = radius + radius * dy / d;
                }
            }
            child.setBounds(new Rectangle(x, y, portWidth, portWidth));
        }
    }

    /**
     * Pick ports from all the children of the container
     */
    @SuppressWarnings("unchecked")
	private List<PortFigure> getPorts(IFigure container)
    {
        List<PortFigure> result = new ArrayList<PortFigure>();
        for(IFigure child: (List<IFigure>) container.getChildren())
            if(child instanceof PortFigure)
                result.add((PortFigure) child);
        return result;
    }

}
