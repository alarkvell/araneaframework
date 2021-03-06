/*
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.araneaframework.uilib.form.control;

import static org.araneaframework.uilib.util.ConfigurationUtil.getCustomTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.araneaframework.http.HttpInputData;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ValidationUtil.ParsedDate;

/**
 * This class represents a {@link org.araneaframework.uilib.form.control.TimestampControl}, that holds only time - that
 * is it's default pattern is "HH:mm".
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov </a>
 */
public class TimeControl extends TimestampControl {

  private static final Log LOG = LogFactory.getLog(TimeControl.class);

  /**
   * This is the default time format for this control.
   */
  public final static String DEFAULT_FORMAT = "HH:mm";

  /**
   * Creates the control initializing the pattern to {@link #DEFAULT_FORMAT}.
   */
  public TimeControl() {
    super(DEFAULT_FORMAT, DEFAULT_FORMAT);
  }

  /**
   * Creates the control initializing the time input pattern to <code>dateTimeFormat</code> and the time output pattern
   * to <code>defaultTimeOutputFormat</code>.
   * 
   * @param dateTimeFormat The custom date input pattern.
   * @param defaultTimeOutputFormat The custom date output format.
   */
  public TimeControl(String dateTimeFormat, String defaultTimeOutputFormat) {
    super(dateTimeFormat, defaultTimeOutputFormat);
    this.confOverridden = true;
  }

  @Override
  public void init() throws Exception {
    super.init();
    if (!this.confOverridden) {
      this.dateTimeInputPattern = getCustomTimeFormat(getEnvironment(), true, this.dateTimeInputPattern);
      this.dateTimeOutputPattern = getCustomTimeFormat(getEnvironment(), false, this.dateTimeOutputPattern);
    }
  }

  @Override
  protected ParsedDate parseDate(String parameterValue) {
    Calendar cal = Calendar.getInstance();

    try {
      Date parsed = DateUtils.parseDate(parameterValue, new String[] { this.dateTimeInputPattern });
      cal.setTime(parsed);

      int hour = cal.get(Calendar.HOUR_OF_DAY);
      int minute = cal.get(Calendar.MINUTE);
      int second = cal.get(Calendar.SECOND);

      if (this.value != null) {
        cal.setTime(this.value);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.SECOND, second);
      }
    } catch (ParseException e) {
      LOG.debug("Could not convert time value.");
    }

    return new ParsedDate(cal.getTime(), this.dateTimeOutputPattern);
  }

  /**
   * This method inserts the message when parsing fails. This override sends the correct message.
   * 
   * @since 1.1
   */
  @Override
  protected void addWrongTimeFormatError() {
    addErrorWithLabel(UiLibMessages.WRONG_TIME_FORMAT, this.dateTimeInputPattern);
  }

  /**
   * The TimeControl can read its data from a single input (where parameter name corresponds to TimeControl scope) or,
   * when the former is missing, from three inputs where input name is the same but also appends ".hour", ".minute", or
   * ".second". When either of these three latter values is provided then the time information will be parsed, validated
   * and formatted according to the specified time input pattern. Also note that the latter method will always treat
   * hours as a 24-hour value, and that non-valid values will default to 0.
   * 
   * @since 2.0
   */
  @Override
  protected void readFromRequest(HttpInputData request) {
    String mainScope = getScope().toString();
    String parameterValues[] = request.getParameterValues(getScope().toString());

    if (parameterValues == null || parameterValues.length == 0) {
      String hours = readSingleValue(request, mainScope + ".hour");
      String minutes = readSingleValue(request, mainScope + ".minute");
      String seconds = readSingleValue(request, mainScope + ".second");

      if (hours != null || minutes != null || seconds != null) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, parseTimePartValue(hours, 0, 23));
        cal.set(Calendar.MINUTE, parseTimePartValue(minutes, 0, 59));
        cal.set(Calendar.SECOND, parseTimePartValue(seconds, 0, 59));

        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateTimeInputPattern);
        parameterValues = new String[] { dateFormat.format(cal.getTime()) };
      }
    }

    this.innerData = preprocessRequestParameters(parameterValues);
    this.isReadFromRequest = true;
  }

  /**
   * Reads the parameter value from request. The array of values will be checked for data and, when present, the first
   * value will be returned (no matter the size of the array).
   * 
   * @param request The incoming request.
   * @param name The name of the parameter for which the value will be fetched.
   * @return The value of the parameter, or <code>null</code> when it's missing.
   * @since 2.0
   */
  protected String readSingleValue(HttpInputData request, String name) {
    String values[] = request.getParameterValues(name);
    return values != null && values.length > 0 ? values[0] : null;
  }

  /**
   * Parses the time part value and checks that it is in the allowed range. When the value cannot be parsed or is not
   * in the range, the value of <code>allowedRangeStart</code> will be returned.
   * 
   * @param value The value to parse.
   * @param allowedRangeStart The first allowed value in the range.
   * @param allowedRangeEnd The last allowed value in the range.
   * @return The parsed value or the default.
   * @since 2.0
   */
  protected int parseTimePartValue(String value, int allowedRangeStart, int allowedRangeEnd) {
    int result = allowedRangeStart;
    if (StringUtils.isNumeric(value)) {
      try {
        result = Integer.parseInt(value);
      } catch (NumberFormatException e) {
        // Ignore exception, default value is already set.
      }
    }
    return result >= allowedRangeStart && result <= allowedRangeEnd ? result : allowedRangeStart;
  }

  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  public class ViewModel extends TimestampControl.ViewModel {

    protected String time;

    /**
     * Takes an outer class snapshot.
     */
    public ViewModel() {
      String[] timeInnerData = (String[]) TimeControl.this.innerData;
      this.time = timeInnerData == null ? null : timeInnerData[0];
    }

    /**
     * Returns time as <code>String</code>.
     * 
     * @return time as <code>String</code>.
     */
    public String getTime() {
      return this.time;
    }

    /**
     * Provides the hour (0-23) part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed hour value as string.
     * @since 2.0
     */
    public String getHourOfDay() {
      return readDateValue(this.time, this.dateTimeOutputPattern, Calendar.HOUR_OF_DAY, 0);
    }

    /**
     * Provides the hour (0-11) part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed hour value as string.
     * @since 2.0
     */
    public String getHour() {
      return readDateValue(this.time, this.dateTimeOutputPattern, Calendar.HOUR_OF_DAY, 0);
    }

    /**
     * Provides the minute part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed minute value as string.
     * @since 2.0
     */
    public String getMinutes() {
      return readDateValue(this.time, this.dateTimeOutputPattern, Calendar.MINUTE, 0);
    }

    /**
     * Provides the seconds part value of the current time value. When the time value cannot be parsed (e.g. is
     * undefined) then the default value as "0" will be returned.
     * 
     * @return The parsed seconds value as string.
     * @since 2.0
     */
    public String getSeconds() {
      return readDateValue(this.time, this.dateTimeOutputPattern, Calendar.SECOND, 0);
    }
  }
}
