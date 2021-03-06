package net.sf.anathema.lib.io;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public abstract class AbstractFileTypeFilter extends FileFilter implements java.io.FileFilter {

  private String getExtension(File file) {
    String fileName = file.getName();
    int lastDotIndex = fileName.lastIndexOf('.');
    if (lastDotIndex < 0) {
      return null;
    }
    if (lastDotIndex == fileName.length() - 1) {
      return "";
    }
    return fileName.substring(lastDotIndex + 1).toLowerCase();
  }

  @Override
  public boolean accept(File f) {
    if (f.isDirectory()) {
      return acceptDirectory(f);
    }
    String extension = getExtension(f);
    return extension != null && acceptExtension(extension);
  }

  protected abstract boolean acceptExtension(String extension);

  protected boolean acceptDirectory(File directory) {
    return !directory.isHidden();
  }
}