package org.araneaframework.framework.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import javax.servlet.ServletContext;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Widget;
import org.araneaframework.Relocatable.RelocatableWidget;
import org.araneaframework.core.BaseApplicationWidget;
import org.araneaframework.core.RelocatableDecorator;

public class StandardDevelFilterWidget extends BaseApplicationWidget {
  private static final Logger log = Logger.getLogger(StandardDevelFilterWidget.class);
  
  private String childClassName;
  private RelocatableWidget child;

  public void setChildClass(String childClass) {
    this.childClassName = childClass;
  }
  
  protected void init() throws Exception {    
    super.init();  
    
    ClassLoader cl = newClassLoader();
    Class childClass = cl.loadClass(childClassName);

    child = new RelocatableDecorator((Widget) childClass.newInstance());
    child._getComponent().init(getEnvironment());    
    _getComposite().attach("c", child);
  }
  
  private ClassLoader newClassLoader() throws MalformedURLException {
    ServletContext sctx = (ServletContext) getEnvironment().getEntry(ServletContext.class);
    return new MyUrlClassloader(new URL[] {sctx.getResource("/WEB-INF/classes")}, getClass().getClassLoader());
  }
  
  private Serializable reload(Serializable child) throws Exception {
    return deepCopy(newClassLoader(), child);
  }
  
  protected void update(InputData input) throws Exception {
    super.update(input);
    try {
      child._getRelocatable().overrideEnvironment(null);
      child = (RelocatableWidget) reload(child);      
    }
    catch (ClassNotFoundException e) {
      log.error("Failed to reload widget classes", e);
    }
    finally {
      child._getRelocatable().overrideEnvironment(getEnvironment());
    }
    
    _getComposite().attach("c", child);
  }
  
  protected void render(OutputData output) throws Exception {
    try {
      output.pushScope("c");
      child._getWidget().render(output);
    }
    finally {
      output.popScope();
    }
  }
  
  private  Serializable deepCopy(ClassLoader cl, Serializable original) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
    ObjectOutputStream out = new ObjectOutputStream(baos);
    try {
      out.writeObject(original);
    }
    finally {
      out.close();
    }
    
    byte[] serialized = baos.toByteArray();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(serialized);
    
    MyOIS in = new MyOIS(cl, bais);
    Object obj = in.readObject();
    
    return (Serializable) obj;
  }
 
  private static class MyOIS extends ObjectInputStream {
    private ClassLoader cl;
    
    public MyOIS(ClassLoader cl, InputStream in) throws IOException {
      super(in);
      this.cl = cl;
    }

    protected Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
      String name = desc.getName();
      return cl.loadClass(name);
    }
  }
  
  private static class MyUrlClassloader extends URLClassLoader {

    public MyUrlClassloader(URL[] urls, ClassLoader parent) {
      super(urls, parent);
    }
    
    public Class loadClass(String name) throws ClassNotFoundException {
      Class c = findLoadedClass(name);
      if (c != null)
        return c;
      
      try {
        return findClass(name);
      }
      catch (ClassNotFoundException e) {
        return super.loadClass(name);
      }
    }
  }
}
