package com.github.thomasfischl.rayden.runtime;

public class RaydenScriptException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public RaydenScriptException(String message, Throwable cause) {
    super(message, cause);
  }

  public RaydenScriptException(String message) {
    super(message);
  }

  public RaydenScriptException(Throwable cause) {
    super(cause);
  }

}
