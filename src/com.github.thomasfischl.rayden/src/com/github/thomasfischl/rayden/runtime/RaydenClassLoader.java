package com.github.thomasfischl.rayden.runtime;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.common.io.Files;

public class RaydenClassLoader extends ClassLoader {

  private File workingFolder;

  public RaydenClassLoader(ClassLoader parent, File workingFolder) {
    super(parent);
    this.workingFolder = workingFolder;
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
      return defineClass(name, ByteBuffer.wrap(os.toByteArray()), null);
    } else {
      return super.findClass(name);
    }
  }

}
