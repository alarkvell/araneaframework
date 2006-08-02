/**
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
 **/

package org.araneaframework.uilib.form.control;

import java.math.BigDecimal;

import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.ErrorUtil;


/**
 * This class represents a textbox control that accepts only valid 
 * floating-point numbers.
 * 
 * This class does not support localization. It does not use @link NumberFormat
 * class to parse and format @link BigDecimal objects because @link NumberFormat
 * would convert @link BigDecimal objects into doubles.
 * 
 * To customize parsing and formatting one could create a subclass of it and
 * override @link #createBigDecimal(String) and @link #toString(BigDecimal)
 * methods. To use the subclass in JSPs also another JSP Tag must be created
 * to use this implementation and configure validation script.
 * 
 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
 * @author <a href="mailto:rein@araneaframework.org">Rein Raudjärv</a>
 */
public class FloatControl extends EmptyStringNullableControl {

	private BigDecimal minValue;
	private BigDecimal maxValue;
	private Integer maxDecimalDigits;

	/**
	 * Empty.
	 */
	public FloatControl() {
		//Empty
	}

	/**
	 * Makes a float control that has minimum, maximum value and maximum decimal digits count.
	 * 
	 * @param minValue minimum permitted value.
	 * @param maxValue maximum permitted value.
	 * @param maxDecimalDigits maximum permitted decimal digits count.
	 */
	public FloatControl(BigDecimal minValue, BigDecimal maxValue, Integer maxDecimalDigits) {
		setMinValue(minValue);
		setMaxValue(maxValue);
		setMaxDecimalDigits(maxDecimalDigits);
	}

	/**
	 * Makes a float control that has minimum and maximum value.
	 * 
	 * @param minValue minimum permitted value.
	 * @param maxValue maximum permitted value.
	 */
	public FloatControl(BigDecimal minValue, BigDecimal maxValue) {
		this(minValue, maxValue, null);
	}
	
	/**
	 * Sets the maximum value.
	 * @param maxValue maximum value.
	 */
	public void setMaxValue(BigDecimal maxValue) {
		this.maxValue = maxValue;
	}

	/**
	 * Sets the minimum value.
	 * @param minValue minimum value.
	 */
	public void setMinValue(BigDecimal minValue) {
		this.minValue = minValue;
	}
	
	/**
	 * Sets the maximum decimal digits count.
	 * @param maxDecimalDigits maximum decimal digits count.
	 */
	public void setMaxDecimalDigits(Integer maxDecimalDigits) {
		if (maxDecimalDigits != null && maxDecimalDigits.intValue() < 0) {
			throw new IllegalArgumentException("Maximum decimal digits count cannot be negative");
		}
		this.maxDecimalDigits = maxDecimalDigits;
	}

	/**
	 * Returns the maxValue.
	 * @return the maxValue.
	 */
	public BigDecimal getMaxValue() {
		return maxValue;
	}

	/**
	 * Returns the minValue.
	 * @return the minValue.
	 */
	public BigDecimal getMinValue() {
		return minValue;
	}

	/**
	 * Returns the maximum decimal digits count.
	 * @return the maximum decimal digits count.
	 */
	public Integer getMaxDecimalDigits() {
		return maxDecimalDigits;
	}

	/**
	 * Returns "BigDecimal".
	 * @return "BigDecimal".
	 */
	public String getRawValueType() {
		return "BigDecimal";
	}

	//*********************************************************************
	//* INTERNAL METHODS
	//*********************************************************************  	

	/**
	 * Trims request parameter.
	 */
	protected String preprocessRequestParameter(String parameterValue) {
		String result = super.preprocessRequestParameter(parameterValue);
		return (result == null ? null : result.trim());
	}

	/**
	 * Checks that the submitted data is a valid floating-point number.
	 * 
	 */
	protected Object fromRequest(String parameterValue) {
		BigDecimal result = null;

		try {
			result = createBigDecimal(parameterValue);
		}
		catch (NumberFormatException e) {
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.NOT_A_NUMBER, 
							ErrorUtil.localize(getLabel(), getEnvironment()),
							getEnvironment()));          
		}

		return result;
	}

	/**
	 * 
	 */
	protected String toResponse(Object controlValue) {
		return toString((BigDecimal) controlValue);
	}
	
	/**
	 * Converts String into BigDecimal. This method can be overrided in subclasses.
	 * 
	 * @param str String object
	 * @return BigDecimal object
	 * @throws NumberFormatException <tt>str</tt> is not a valid representation
     *	       of a BigDecimal
	 */
	protected BigDecimal createBigDecimal(String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
		return new BigDecimal(str);
	}
	
	/**
	 * Converts BigDecimal into String. This method can be overrided in subclasses.
	 * 
	 * @param dec BigDecimal object
	 * @return String object
	 */
	protected String toString(BigDecimal dec) {
        if (dec == null) {
            return null;
        }
		return dec.toString();
	}
	
	/**
	 * Checks that the submitted value is in permitted range.
	 * 
	 */
	protected void validateNotNull() {
		// minimum and maximum permitted values
		if (minValue != null && maxValue != null && ((((BigDecimal) value).compareTo(minValue) == -1) || ((BigDecimal) value).compareTo(maxValue) == 1)) {
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.NUMBER_NOT_BETWEEN, 
							new Object[] {
									ErrorUtil.localize(getLabel(), getEnvironment()),
									minValue.toString(),
									maxValue.toString()
							},          
							getEnvironment()));           
		}      
		else if (minValue != null && ((BigDecimal) value).compareTo(minValue) == -1) {      
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.NUMBER_NOT_GREATER, 
							new Object[] {
									ErrorUtil.localize(getLabel(), getEnvironment()),
									minValue.toString(),
							},          
							getEnvironment()));       
		}    
		else if (maxValue != null && ((BigDecimal) value).compareTo(maxValue) == 1) {
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.NUMBER_NOT_LESS, 
							new Object[] {
									ErrorUtil.localize(getLabel(), getEnvironment()),
									maxValue.toString(),
							},          
							getEnvironment()));         
		}
		
		// maximum permitted decimal digits count
		if (maxDecimalDigits != null && ((BigDecimal) value).scale() > maxDecimalDigits.intValue()) {
			addError(
					ErrorUtil.localizeAndFormat(
							UiLibMessages.DECIMAL_DIGITS_COUNT_NOT_LESS, 
							new Object[] {
									ErrorUtil.localize(getLabel(), getEnvironment()),
									maxDecimalDigits.toString(),
							},
							getEnvironment()));         			
		}
	}

	/**
	 * Returns {@link ViewModel}.
	 * @return {@link ViewModel}.
	 */
	public Object getViewModel() {
		return new ViewModel();
	}

	//*********************************************************************
	//* VIEW MODEL
	//*********************************************************************    

	/**
	 * @author <a href="mailto:ekabanov@webmedia.ee">Jevgeni Kabanov</a>
	 * 
	 */
	public class ViewModel extends StringArrayRequestControl.ViewModel {

		private BigDecimal maxValue;
		private BigDecimal minValue;

		/**
		 * Takes an outer class snapshot.     
		 */    
		public ViewModel() {
			this.maxValue = FloatControl.this.getMaxValue();
			this.minValue = FloatControl.this.getMinValue();
		}       

		/**
		 * Returns maximum permitted value.
		 * @return maximum permitted value.
		 */
		public BigDecimal getMaxValue() {
			return this.maxValue;
		}

		/**
		 * Returns minimum permitted value.
		 * @return minimum permitted value.
		 */
		public BigDecimal getMinValue() {
			return this.minValue;
		}  
	}
}
