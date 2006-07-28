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
package org.araneaframework.servlet.filter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.AraneaFileNotFoundException;
import org.araneaframework.core.BaseService;
import org.araneaframework.extension.resource.ExternalResource;
import org.araneaframework.extension.resource.ExternalResourceInitializer;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.router.PathInfoServiceRouterService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
*/
public class StandardServletFileImportService extends BaseService {
	private static final Logger log = Logger.getLogger(StandardServletFileImportService.class);
	private static boolean isInitialized = false;
	private static ExternalResource resources;
	private long cacheHoldingTime = 3600000;

	public static final String IMPORTER_FILE_NAME = "FileImporterFileName";
	public static final String IMPORTER_GROUP_NAME = "FileImporterGroupName";
	public static final String OVERRIDE_PREFIX = "override";
	public static final String FILE_IMPORTER_NAME = "fileimporter";
	
	public static final String FILE_NAME_RE = new String("^[a-zA-Z0-9./\\-]+$");
	
	synchronized static void initialize(ServletContext context) {
		if (!isInitialized) {
			ExternalResourceInitializer initializer = new ExternalResourceInitializer();
			resources = initializer.getResources(context);
			isInitialized = true;
		}
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		if (!isInitialized) {
			ServletConfig config = (ServletConfig) getEnvironment().getEntry(ServletConfig.class);
			initialize(config.getServletContext());
		}

		String fileName = (String)input.getGlobalData().get(IMPORTER_FILE_NAME);
		String groupName = (String)input.getGlobalData().get(IMPORTER_GROUP_NAME);
		
		if (fileName == null) {
			fileName = (String)output.getAttribute(PathInfoServiceRouterService.PATH_ARGUMENT);
			
			if (fileName.indexOf(".") == -1) {
				groupName = fileName;
				fileName = null;
			}
		}
		else if (groupName == null) {
			groupName = fileName;
		}

		HttpServletResponse response = ((ServletOutputData)output).getResponse();
	
		List filesToLoad = new ArrayList();
		OutputStream out = ((ServletOutputData)output).getResponse().getOutputStream();
		try {
			if (fileName != null) {
				if (resources.isAllowedFile(fileName)) {
					setHeaders(response, resources.getContentType(fileName));
					filesToLoad.add(fileName);
					loadFiles(filesToLoad, out);
				}
				/*
				 * Fallback to the filesystem. Container takes care if it is okay to load
				 * a file from the application's system.
				 * 
				 * XXX: in case of plain new FileInputStream(fileName)
				 * container behaviour is not known. i.e Jetty allows loading files
				 * from application context that way, but Weblogic does not.
				 */
				else {
					try {
						// allow only very simple filenames to prevent malicious URL hacking
						// and check that remaining simple filename does not contain ".."
						if (!(fileName.matches(FILE_NAME_RE) && (fileName.indexOf("..") == -1)))
							throw new SecurityException("Filename too weird, preventing fallback.");

						// now it is sure that file cannot lie outside the web application context
						// access to WEB-INF and META-INF should also be prevented - might be
						// sensitive stuff in configuration files there
						
						if (fileName.toUpperCase().indexOf("WEB-INF") != -1 || fileName.toUpperCase().indexOf("META-INF") != -1)
							throw new SecurityException("Fallback not allowed.");
						
						ServletConfig sc = (ServletConfig) getEnvironment().getEntry(ServletConfig.class);
						String realContextPath = sc.getServletContext().getRealPath("/");
						
						String realFileName = realContextPath + "/" + fileName;

						// checking for the existence
						new FileInputStream(realFileName);
						filesToLoad.add(realFileName);
						// this will try override directory first but for fallback that should not matter
						loadFiles(filesToLoad, out);
					}
					catch (FileNotFoundException e) {
						log.warn("Not allowed to import "+fileName+" add it to the allowed list or make sure it exists on the filesystem.");
						throw new AraneaFileNotFoundException();	
					}
					catch (SecurityException e) {
						log.warn("Not allowed to import "+fileName+" add it to the allowed list or make sure it exists on the filesystem.");
						throw new AraneaFileNotFoundException();	
					}
				}
			}
			else if (groupName != null) {
				Map group = resources.getGroupByName(groupName);
				if (group != null && group.size() > 0) {
					Map.Entry entry = (Map.Entry)(group.entrySet().iterator().next());
					setHeaders(response, (String)entry.getValue());
					filesToLoad.addAll(group.keySet());
					loadFiles(filesToLoad, out);
				}
				else {
					log.warn("Non-existent group specified for file importing, "+groupName);
					throw new AraneaFileNotFoundException();
				}
			}
			else {
				log.debug("No fileName or groupName specified. Doing nothing");
			}
		}
		catch (AraneaFileNotFoundException e) {
			String notFoundName = fileName == null ? groupName : fileName;
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Imported file or group '" + notFoundName + "' not found.");
		}
	}

	private void setHeaders(HttpServletResponse response, String contentType) {
	  response.setHeader("Cache-Control", "max-age=" + (cacheHoldingTime / 1000));
	  response.setDateHeader ("Expires", System.currentTimeMillis () + cacheHoldingTime);
	  response.setContentType(contentType);
	}

	private void loadFiles(List files, OutputStream out) throws Exception {
		for (Iterator iter = files.iterator(); iter.hasNext();) {
			String fileName = (String) iter.next();
			
			ClassLoader loader = getClass().getClassLoader();
			// first we try load an override
			URL fileURL = loader.getResource(OVERRIDE_PREFIX+"/"+fileName);
			
			if (fileURL == null) {
				// fallback to the original
				fileURL = loader.getResource(fileName);
			}
			
			FileInputStream fileInputStream = null;
			// fallback to the filesystem
			if (fileURL == null) {
				try {
					fileInputStream = new FileInputStream(fileName);
				}
				catch (FileNotFoundException e) {
					// not being able to load results in AraneaFileNotFoundException, see below
				}
				catch (SecurityException e) {
					// not being able to load results in AraneaFileNotFoundException, see below
				}
			}
			
			if (fileURL != null || fileInputStream != null) {
				InputStream inputStream = null;
				
				if (fileInputStream != null) {
					inputStream = fileInputStream;
				} 
				else {
					inputStream = fileURL.openStream();
				}
				
				log.debug("Loading "+fileName);
				if (inputStream!=null) {
					int length = 0;
					byte[] bytes = new byte[1024];
					do {
						length = inputStream.read(bytes);
						if (length==-1) break;
						out.write(bytes, 0, length);
					} while (length!=-1);
				}
			}
			else {
				log.warn("Unable to locate resource '"+fileName+"'");
				throw new AraneaFileNotFoundException();
			}
		}
	}
}
