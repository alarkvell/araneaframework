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

package org.araneaframework.http.widget;

import javax.servlet.http.HttpServletResponse;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.core.Assert;
import org.araneaframework.http.util.ServletUtil;
import org.araneaframework.uilib.support.FileInfo;

/**
 * Widget that serves content (allows downloading) of specified files.
 * 
 * @author Taimo Peelo (taimo@araneaframework.org)
 * @author Alar Kvell (alar@araneaframework.org)
 */
public class FileDownloaderWidget extends DownloaderWidget {
  protected FileInfo file;
  
  protected String fileName;
  
  protected boolean contentDispositionInline = true;
  
  public FileDownloaderWidget(FileInfo file) {
    super(file.readFileContent(), file.getContentType());
    Assert.notNullParam(file, "file");
    this.fileName = normalizeFileName(file.getOriginalFilename());
  }
  
  public FileDownloaderWidget(byte[] fileContent, String contentType, String fileName) {
    super(fileContent, contentType);
    Assert.notEmptyParam(fileName, "fileName");
    this.fileName = normalizeFileName(fileName);
  }
  
  /**
   * Returns value of currently used content-disposition response header.
   * @return false if content-disposition header is set to "attachment"
   */
  public boolean isContentDispositionInline() {
    return contentDispositionInline;
  }

  /**
   * Sets content-disposition header to "inline" (true) or "attachment" (false).
   */
  public void setContentDispositionInline(boolean contentDispositionInline) {
    this.contentDispositionInline = contentDispositionInline;
  }
  
  /** Used internally to extract only file name from supplied filename (no file path). */
  protected String normalizeFileName(String fileName) {
    return FileDownloaderWidget.staticNormalizeFileName(fileName);
  }

  // as IE (6) provides full names for uploaded files, we use some heuristics
  // to ensure filename does not include the full path. By no means bulletproof
  // as it does some harm to filenames containing slashes/backslashes.
  public static String staticNormalizeFileName(String fileName) {
    fileName = fileName.trim();
    // shouldn't happen, but anyway...
    if (fileName.endsWith("\\"))
      fileName = fileName.substring(0, fileName.length()-1);
    if (fileName.endsWith("/"))
      fileName = fileName.substring(0, fileName.length()-1);
    
    if (fileName.lastIndexOf('\\') != -1) {
      fileName = fileName.substring(fileName.lastIndexOf('\\') + 1);
    }
    // IE on MAC?
    if (fileName.lastIndexOf('/') != -1) {
      fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    return fileName;
  }

  protected void action(Path path, InputData input, OutputData output) throws Exception {
    super.action(path, input, output);

    HttpServletResponse response = ServletUtil.getResponse(output);
    response.setHeader("Content-Disposition", (contentDispositionInline ? "inline;" : "attachment;") + "filename=" + fileName);

    close();
  }

  protected void close() {
  }

}
