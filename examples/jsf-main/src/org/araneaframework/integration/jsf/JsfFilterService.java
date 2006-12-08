package org.araneaframework.integration.jsf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.ResponseStateManager;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.apache.log4j.Logger;
import org.araneaframework.Environment;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.core.StandardEnvironment;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.integration.jsf.core.AraneaJsfNavigationHandlerWrapper;
import org.araneaframework.integration.jsf.core.AraneaViewHandlerDecorator;
import org.araneaframework.integration.jsf.core.JSFContext;

/**
 * @author Taimo Peelo (taimo@araneaframework.org)
 */
public class JsfFilterService extends BaseFilterService implements JSFContext {
	private static final Logger log = Logger.getLogger(JsfFilterService.class);
	
    private Lifecycle lifecycle = null;
    private Application application = null;
    
    protected Environment getChildEnvironment() {
        return new StandardEnvironment(super.getChildEnvironment(), JSFContext.class, this);
    }
    
    protected void init() throws Exception {
        super.init();

        ApplicationFactory appFactory = getApplicationFactory();
        Assert.notNull(appFactory);
        Assert.notNull(getLifecycleFactory());
        Assert.notNull(getRenderKitFactory());
        Assert.notNull(getFacesContextFactory());
        
        application = appFactory.getApplication();
        application.setViewHandler(new AraneaViewHandlerDecorator(application.getViewHandler()));
        application.setNavigationHandler(new AraneaJsfNavigationHandlerWrapper(application.getNavigationHandler()));
        
        lifecycle =  getLifecycleFactory().getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

		lifecycle.addPhaseListener(getLoggingListener(PhaseId.RESTORE_VIEW));
		lifecycle.addPhaseListener(getLoggingListener(PhaseId.APPLY_REQUEST_VALUES));
		lifecycle.addPhaseListener(getLoggingListener(PhaseId.PROCESS_VALIDATIONS));
		lifecycle.addPhaseListener(getLoggingListener(PhaseId.UPDATE_MODEL_VALUES));
		lifecycle.addPhaseListener(getLoggingListener(PhaseId.INVOKE_APPLICATION));
		lifecycle.addPhaseListener(getLoggingListener(PhaseId.RENDER_RESPONSE));
    	
        Assert.notNull(lifecycle);
    }

	private PhaseListener getLoggingListener(final PhaseId phaseId) {
		return new PhaseListener() {
			public void afterPhase(PhaseEvent event) {
				log.debug("After phase:" + getPhaseId() + " &&'" + getViewId() + "'");
			}

			public void beforePhase(PhaseEvent event) {
				log.debug("Before phase:" + getPhaseId() + " && '" + getViewId() + "'");
			}

			public PhaseId getPhaseId() {
				return phaseId;
			}
			
			private String getViewId() {
				FacesContext ctx = FacesContext.getCurrentInstance();
				if (ctx != null && ctx.getViewRoot() != null) {
					return ctx.getViewRoot().getViewId();
				}
				return null;
			}
    	};
	}
    
    protected void action(Path path, InputData input, OutputData output) throws Exception {
    	if (!isJSFRequest(input)) {
    		super.action(path, input, output);
    		return;
    	}
    	
    	HttpServletRequest request = ServletUtil.getRequest(input);
    	ServletUtil.setRequest(input, (HttpServletRequest) getNonFacesRequest(input));

    	input.extend(JSFRequest.class, request);
    	super.action(path, input, output);
    }

    protected boolean isJSFRequest(InputData input) {
    	return ServletUtil.getRequest(input).getParameter(ResponseStateManager.VIEW_STATE_PARAM) != null;
    }

    public FacesContext initFacesContext(InputData input, OutputData output) {
        ServletContext servletCtx = ((ServletConfig)getEnvironment().getEntry(ServletConfig.class)).getServletContext();
        Assert.notNull(servletCtx, "ServletContext");
        Assert.notNull(ServletUtil.getRequest(input), "request");
        Assert.notNull(ServletUtil.getResponse(output), "response");
        Assert.notNull(lifecycle);

        FacesContext result = 
                getFacesContextFactory().getFacesContext(                    
                    ((ServletConfig)getEnvironment().getEntry(ServletConfig.class)).getServletContext(),
                    ServletUtil.getRequest(input),
                    ServletUtil.getResponse(output),
                    lifecycle
                );
        
        return result;
    }

    public void destroyFacesContext(FacesContext facesContext) {
    	facesContext.release();
    }

    /* Factory getters. */
    public ApplicationFactory getApplicationFactory() {
        return (ApplicationFactory)FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
    }
    
    public LifecycleFactory getLifecycleFactory() {
        return (LifecycleFactory)FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    }
    
    public FacesContextFactory getFacesContextFactory() {
        return (FacesContextFactory)FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    }
    
    public RenderKitFactory getRenderKitFactory() {
        return (RenderKitFactory)FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
    }
    
    public Application getApplication() {
    	return application;
    }
    
    public Lifecycle getLifecycle() {
        return lifecycle;
    };
    
    protected Object getNonFacesRequest(InputData input) {
    	return new PlainRequest(ServletUtil.getRequest(input));
    }
    
    protected static class PlainRequest extends HttpServletRequestWrapper {
		protected PlainRequest(HttpServletRequest request) {
			super(request);
		}

		public String getParameter(String name) {
			if (name.equals(ResponseStateManager.VIEW_STATE_PARAM))
				return null;
			return super.getParameter(name);
		}

		public Map getParameterMap() {
			Map mutable = new HashMap(super.getParameterMap());
			mutable.remove(ResponseStateManager.VIEW_STATE_PARAM);
			return Collections.unmodifiableMap(mutable);
		}

		public Enumeration getParameterNames() {
			Enumeration temp = super.getParameterNames();
			List list = new ArrayList();
			while (temp.hasMoreElements()) {
				Object current = temp.nextElement();
				if (!current.equals(ResponseStateManager.VIEW_STATE_PARAM))
					list.add(current);
			}
			return Collections.enumeration(list);
		}

		public String[] getParameterValues(String name) {
			if (name.equals(ResponseStateManager.VIEW_STATE_PARAM))
				return null;
			return super.getParameterValues(name);
		}
	}
    
}
