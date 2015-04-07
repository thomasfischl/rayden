package com.github.thomasfischl.rayden.runtime;

public class RaydenScriptFailedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public RaydenScriptFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public RaydenScriptFailedException(String message) {
    super(message);
  }

  public RaydenScriptFailedException(Throwable cause) {
    super(cause);
  }

}
