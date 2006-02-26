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

package org.araneaframework.jsp.tests;

import junit.framework.TestCase;
import org.araneaframework.jsp.util.StringUtil;

/**
 * @author Konstantin Tretyakov
 */
public class StringUtilTest extends TestCase {

	public StringUtilTest(String name){
		super(name);
	}
	
	public void testStringUtils(){
		// underlineAccessKey
		assertEquals(StringUtil.underlineAccessKey("",""), "");
		assertEquals(StringUtil.underlineAccessKey("aaa",""), "aaa");		
		assertNull(StringUtil.underlineAccessKey(null, "a"));
		assertEquals(StringUtil.underlineAccessKey("Some string", "So"), "Some string");
		assertEquals(StringUtil.underlineAccessKey("Some string", null), "Some string");
		assertEquals(StringUtil.underlineAccessKey("Some string", "s"), "<u>S</u>ome string");
		assertEquals(StringUtil.underlineAccessKey("s", "s"), "<u>s</u>");
		assertEquals(StringUtil.underlineAccessKey(" \n\tõüöä\n\t", "Õ"), " \n\t<u>õ</u>üöä\n\t");
		assertEquals(StringUtil.underlineAccessKey("<u>this</u>","U"),"<<u>u</u>>this</u>");
		
		// escapeHtmlEntities
		assertNull(StringUtil.escapeHtmlEntities(null));
		assertEquals(StringUtil.escapeHtmlEntities(""), "");
		assertEquals(StringUtil.escapeHtmlEntities(" abc def "), " abc def ");
		assertEquals(StringUtil.escapeHtmlEntities("&"), "&amp;");
		assertEquals(StringUtil.escapeHtmlEntities("&&&\n<<< >>>\t\"\"\""), "&amp;&amp;&amp;\n&lt;&lt;&lt; &gt;&gt;&gt;\t&quot;&quot;&quot;");
		
	}
}
