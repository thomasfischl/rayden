package com.github.thomasfischl.rayden.reporting.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

public class ReportMessage {

  public enum ReportMessageType {
    INFO, WARNING, ERROR
  }

  private String message;

  private String type;

  @XmlValue
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @XmlAttribute
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setType(ReportMessageType type) {
    this.type = type.name();
  }

}
