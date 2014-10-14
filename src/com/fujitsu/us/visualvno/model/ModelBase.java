package com.fujitsu.us.visualvno.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Base class for all the model classes
 * Provides property change events forwarding via a PropertyChangeSupport object
 * EditParts should call addPropertyChangeLister() to add itself as listerner
 * Subclasses should call firePropertyChange() to send updates to EditParts,
 * which will handle via propertyChanged()
 * 
 * @author Cong Chen <cong.chen@us.fujitsu.com>
 */
public abstract class ModelBase implements IPropertySource, Serializable
{
    private static final long serialVersionUID = 1;

    private static final IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];
    
    // takes care of event forwarding
    private transient PropertyChangeSupport pcsDelegate
                            = new PropertyChangeSupport(this);
    
    public synchronized void addPropertyChangeListener(PropertyChangeListener l)
    {
        if(l == null)
            throw new IllegalArgumentException();
        pcsDelegate.addPropertyChangeListener(l);
    }
    
    public synchronized void removePropertyChangeListener(PropertyChangeListener l) {
        if(l != null)
            pcsDelegate.removePropertyChangeListener(l);
    }

    protected void firePropertyChange(String property, Object oldValue, Object newValue)
    {
        if(pcsDelegate.hasListeners(property))
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
    }
    
    /** Deserialization constructor */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        pcsDelegate = new PropertyChangeSupport(this);
    }

    @Override
    public Object getEditableValue() {
        return this;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return EMPTY_ARRAY;
    }

    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {}
}
