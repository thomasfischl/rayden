package com.github.thomasfischl.rayden.reporting.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Report {

  private String testSuite;

  private long startTime;

  private long finishTime;

  private List<ReportKeyword> testCases = new ArrayList<>();

  @XmlAttribute
  public String getTestSuite() {
    return testSuite;
  }

  public void setTestSuite(String testSuite) {
    this.testSuite = testSuite;
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

  @XmlElement(name="testcase")
  @XmlElementWrapper(name = "testcases")
  public List<ReportKeyword> getTestCases() {
    return testCases;
  }

  public void setTestCases(List<ReportKeyword> testCases) {
    this.testCases = testCases;
  }

}
