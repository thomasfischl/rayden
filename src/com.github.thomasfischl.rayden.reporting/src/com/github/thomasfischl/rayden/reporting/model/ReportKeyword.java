package com.github.thomasfischl.rayden.reporting.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.github.thomasfischl.rayden.reporting.model.ReportMessage.ReportMessageType;

public class ReportKeyword {

  private String name;

  private long startTime;

  private long finishTime;

  private List<ReportKeyword> keywords;

  private List<ReportMessage> messages;

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

  @XmlElement(name = "keyword")
  @XmlElementWrapper(name = "keywords")
  public List<ReportKeyword> getKeywords() {
    return keywords;
  }

  public void setKeywords(List<ReportKeyword> keywords) {
    this.keywords = keywords;
  }

  public void addReportKeyword(ReportKeyword keyword) {
    if (keywords == null) {
      keywords = new ArrayList<ReportKeyword>();
    }
    keywords.add(keyword);
  }

  @XmlElement(name = "message")
  @XmlElementWrapper(name = "messages")
  public List<ReportMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<ReportMessage> messages) {
    this.messages = messages;
  }

  public void addMessage(ReportMessageType type, String message) {
    if (messages == null) {
      messages = new ArrayList<>();
    }

    ReportMessage msg = new ReportMessage();
    msg.setType(type);
    msg.setMessage(message);
    messages.add(msg);
  }

}
