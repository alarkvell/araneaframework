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

import org.apache.commons.lang.StringUtils;
import org.araneaframework.uilib.form.FilteredInputControl;
import org.araneaframework.uilib.form.control.inputfilter.InputFilter;
import org.araneaframework.uilib.support.TextType;
import org.araneaframework.uilib.support.UiLibMessages;
import org.araneaframework.uilib.util.MessageUtil;
import org.araneaframework.uilib.util.ValidationUtil;

/**
 * Class that represents a textbox control.
 * 
 * @author Jevgeni Kabanov (ekabanov <i>at</i> araneaframework <i>dot</i> org)
 * 
 */
public class TextControl extends StringValueControl implements FilteredInputControl<String> {

  private InputFilter inputFilter;

  protected TextType textType = TextType.TEXT;

  /**
   * Empty constructor.
   */
  public TextControl() {}

  /**
   * Makes a text control with specific type.
   * 
   * @param textType specific type.
   */
  public TextControl(TextType textType) {
    this.textType = textType;
  }

  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   */
  public TextControl(Long minLength, Long maxLength) {
    setMinLength(minLength);
    setMaxLength(maxLength);
  }

  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   * @param trimValue whether the value from request will be trimmed.
   */
  public TextControl(Long minLength, Long maxLength, boolean trimValue) {
    setMinLength(minLength);
    setMaxLength(maxLength);
    setTrimValue(trimValue);
  }

  /**
   * Makes a text control with specific type and minimum and maximum length constraints.
   * 
   * @param textType specific type.
   * @param minLength minimum permitted length.
   * @param maxLength maximum permitted length.
   */
  public TextControl(TextType textType, Long minLength, Long maxLength) {
    this.textType = textType;
    setMinLength(minLength);
    setMaxLength(maxLength);
  }

  /**
   * Sets the specific text type.
   * 
   * @param textType the specific text type.
   */
  public void setTextType(TextType textType) {
    this.textType = textType;
  }

  /**
   * Returns the type of this <code>TextControl</code>.
   * 
   * @return the <code>TextType</code> of this <code>TextControl</code>.
   * @since 1.2.1
   */
  public TextType getTextType() {
    return this.textType;
  }

  /** @since 1.0.11 */
  public InputFilter getInputFilter() {
    return this.inputFilter;
  }

  /** @since 1.0.11 */
  public void setInputFilter(InputFilter inputFilter) {
    this.inputFilter = inputFilter;
  }

  // *********************************************************************
  // * INTERNAL INTERFACE
  // *********************************************************************
  /**
   * In case text control type is other than {@link TextType#TEXT} makes custom checks.
   */
  @Override
  protected void validateNotNull() {
    super.validateNotNull();

    if (this.textType.equals(TextType.NUMBER_ONLY)) {
      for (int i = 0; i < getRawValue().length(); i++) {
        if (!Character.isDigit(getRawValue().charAt(i))) {
          addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NOT_A_NUMBER,
              MessageUtil.localize(getLabel(), getEnvironment())));
          break;
        }
      }
    } else if (this.textType.equals(TextType.EMAIL)) {
      if (!ValidationUtil.isEmail(getRawValue())) {
        addError(MessageUtil.localizeAndFormat(getEnvironment(), UiLibMessages.NOT_AN_EMAIL,
            MessageUtil.localize(getLabel(), getEnvironment())));
      }
    }

    if (getInputFilter() != null && !StringUtils.containsOnly(this.value, getInputFilter().getCharacterFilter())) {
      addError(MessageUtil.localizeAndFormat(getEnvironment(), getInputFilter().getInvalidInputMessage(), MessageUtil.localize(
          getLabel(), getEnvironment()), getInputFilter().getCharacterFilter()));
    }
  }

  /**
   * Returns {@link ViewModel}.
   * 
   * @return {@link ViewModel}.
   */
  @Override
  public ViewModel getViewModel() {
    return new ViewModel();
  }

  // *********************************************************************
  // * VIEW MODEL
  // *********************************************************************

  /**
   * @author <a href="mailto:olegm@webmedia.ee">Oleg Mürk</a>
   */
  public class ViewModel extends StringValueControl.ViewModel {

    protected String textType;

    protected InputFilter inputFilter;

    protected ViewModel() {
      this.textType = TextControl.this.textType.getName();
      this.inputFilter = TextControl.this.getInputFilter();
    }

    public String getTextType() {
      return this.textType;
    }

    public InputFilter getInputFilter() {
      return this.inputFilter;
    }
  }
}
