package com.fujitsu.us.visualvno.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * Abstract base for all the model elements
 */
public abstract class ModelBase implements IPropertySource, Serializable
{
    /** An empty property descriptor. */
    private static final IPropertyDescriptor[] EMPTY_ARRAY = new IPropertyDescriptor[0];

    private static final long serialVersionUID = 1;

    /** A delegate to handle events */
    private transient PropertyChangeSupport pcsDelegate = new PropertyChangeSupport(this);

    public synchronized void addPropertyChangeListener(
                                        PropertyChangeListener listener) {
        if(listener != null)
            pcsDelegate.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(
                                        PropertyChangeListener listener) {
        if(listener != null)
            pcsDelegate.removePropertyChangeListener(listener);
    }
    
    protected void firePropertyChange(String property, Object oldValue, 
                                                       Object newValue) {
        if(pcsDelegate.hasListeners(property))
            pcsDelegate.firePropertyChange(property, oldValue, newValue);
    }

    /**
     * Get a value for this property source that can be edited in a property sheet.
     */
    @Override
    public Object getEditableValue() {
        return this;
    }

    /**
     * Children should override this.
     * The default implementation returns an empty array.
     */
    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        return EMPTY_ARRAY;
    }

    /**
     * Children should override this. 
     * The default implementation returns null.
     */
    @Override
    public Object getPropertyValue(Object id) {
        return null;
    }

    /**
     * Children should override this. 
     * The default implementation returns false.
     */
    @Override
    public boolean isPropertySet(Object id) {
        return false;
    }

    /**
     * Deserialization constructor. 
     * Initializes transient fields.
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        in.defaultReadObject();
        pcsDelegate = new PropertyChangeSupport(this);
    }

    @Override
    public void resetPropertyValue(Object id) {}

    @Override
    public void setPropertyValue(Object id, Object value) {}
}