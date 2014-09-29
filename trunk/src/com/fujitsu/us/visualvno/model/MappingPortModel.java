package com.fujitsu.us.visualvno.model;

public class MappingPortModel extends PortModel
{
    public MappingPortModel(ShapeModel shape) {
        super(shape, 0);
    }

    @Override
    public boolean canConnectTo(PortModel that) {
        return canReconnectTo(that);
    }
    
    @Override
    public boolean canReconnectTo(PortModel that) {
        return that != null &&
               this.getShape() != that.getShape();
    }
}
