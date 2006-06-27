package org.araneaframework.uilib.form.control;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.core.ActionListener;
import org.araneaframework.jsp.util.StringUtil;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.uilib.support.TextType;

/**
 * TextControl with Ajax autocompletion support
 * 
 * @author Steven Jentson (steven@webmedia.ee)
 * 
 */
public class AutoCompleteTextControl extends TextControl {
  private static final Logger log = Logger.getLogger(AutoCompleteTextControl.class);	
  public static final String LISTENER_NAME = "autocomplete"; 

  protected long minCompletionLength = 1;
  protected DataProvider dataProvider;

  public AutoCompleteTextControl() {}

  /**
   * @param minCompletionLength number of chars that must be input before suggestions are provided
   */
  public AutoCompleteTextControl(long minCompletionLength) {
    this.minCompletionLength = minCompletionLength;
  }

  public AutoCompleteTextControl(TextType textType) {
    super(textType);
  }
  
  /**
   * @param minCompletionLength number of chars that must be input before suggestions are provided
   */
  public AutoCompleteTextControl(TextType textType, long minCompletionLength) {
    super(textType);
    this.minCompletionLength = minCompletionLength;
  }

  protected void init() throws Exception {
    super.init();
    addActionListener(LISTENER_NAME, new AutoCompleteActionListener());
  }

  public void setDataProvider(DataProvider dataProvider) {
    this.dataProvider = dataProvider;
  }

  public interface DataProvider {
    public List getSuggestions(String input);
  }

  private class AutoCompleteActionListener implements ActionListener {
    public void processAction(Object actionId, InputData input,
        OutputData output) throws Exception {
      String val = innerData == null ? null : ((String[]) innerData)[0];
      List suggestions = dataProvider.getSuggestions((String) val);

      //XXX: outputting some hardcoded HTML from here is not good
      StringBuffer xml = new StringBuffer();
      xml.append("<ul>");
      for (int i = 0; i < suggestions.size(); i++) {
        xml.append("<li>");
        xml.append(StringUtil.escapeHtmlEntities((String) suggestions.get(i)));
        xml.append("</li>");
      }
      xml.append("</ul>");

      log.debug("Writing output: " + xml.toString());
      HttpServletResponse response = ((ServletOutputData) output).getResponse();
      response.setContentType("text/xml");
      response.getWriter().write(xml.toString());
    }
  }
  
  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  public Object getViewModel() {
    return new ViewModel();
  }	  

  //*********************************************************************
  //* VIEW MODEL
  //*********************************************************************  	
  public class ViewModel extends TextControl.ViewModel {
    private long minCompletionLength;
    
    public ViewModel() {
      this.minCompletionLength = AutoCompleteTextControl.this.minCompletionLength;
    }

    public long getMinCompletionLength() {
      return minCompletionLength;
    }
  }
}
