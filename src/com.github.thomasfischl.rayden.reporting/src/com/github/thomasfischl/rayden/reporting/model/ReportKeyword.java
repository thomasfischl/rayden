package com.github.thomasfischl.rayden.reporting.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class ReportKeyword {

  private String name;

  private long startTime;

  private long finishTime;

  private List<ReportKeyword> keywords = new ArrayList<ReportKeyword>();

  @XmlAttribute
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlAttribute
  public long getStartTime() {
    return startTime;
  }

  public void setStartTime(long startTime) {
    this.startTime = startTime;
  }

  @XmlAttribute
  public long getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(long finishTime) {
    this.finishTime = finishTime;
  }

  @XmlElement
  @XmlElementWrapper(name="keywords")
  public List<ReportKeyword> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<ReportKeyword> keywords) {
    this.keywords = keywords;
  }

}
