package com.github.thomasfischl.rayden.runtime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.ByteBuffer;
import java.security.CodeSource;

import com.google.common.collect.FluentIterable;
import com.google.common.io.Files;

public class RaydenClassLoader extends URLClassLoader {

  private File workingFolder;

  public RaydenClassLoader(ClassLoader parent, File workingFolder) {
    super(new URL[0], parent);
    this.workingFolder = workingFolder;
    appendLibraryFolder();
  }

  private void appendLibraryFolder() {
    FluentIterable<File> it = Files.fileTreeTraverser().preOrderTraversal(workingFolder);
    it.forEach(f -> {
      if (f.getName().endsWith(".jar")) {
//        System.out.println("Add jar to Classpath: " + f.getAbsolutePath());
        try {
          addURL(f.toURI().toURL());
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    });
  }

  
  
  @Override
  protected Class<?> findClass(String name) throws ClassNotFoundException {
    String path = name.replace('.', '/').concat(".class");

    File classFile = new File(workingFolder, path);

    if (!classFile.exists()) {
      classFile = new File(workingFolder, "bin/" + path);
    }

    if (classFile.isFile()) {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      try {
        Files.copy(classFile, os);
      } catch (IOException e) {
        throw new ClassNotFoundException("Could not read class file '" + classFile + "'.", e);
      }
      return defineClass(name, ByteBuffer.wrap(os.toByteArray()), (CodeSource) null);
    } else {
      return super.findClass(name);
    }
  }

}
