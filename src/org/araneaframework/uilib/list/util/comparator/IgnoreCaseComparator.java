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

package org.araneaframework.uilib.list.util.comparator;

import java.io.Serializable;
import java.util.Locale;

/**
 * Not-null comparator that compares <code>String</code> values case insensitive. This comparator is not
 * <code>Locale</code>-specific.
 */
public class IgnoreCaseComparator implements StringComparator, Serializable {

  public static final IgnoreCaseComparator INSTANCE = new IgnoreCaseComparator();

  private IgnoreCaseComparator() {}

  public boolean getIgnoreCase() {
    return true;
  }

  public Locale getLocale() {
    throw new RuntimeException("Locales not supported");
  }

  public int compare(String o1, String o2) {
    return String.CASE_INSENSITIVE_ORDER.compare(o1, o2);
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof IgnoreCaseComparator;
  }

  @Override
  public int hashCode() {
    return 703271433;
  }
}
