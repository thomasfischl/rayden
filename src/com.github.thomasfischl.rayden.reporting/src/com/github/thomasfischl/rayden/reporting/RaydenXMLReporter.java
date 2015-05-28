package com.github.thomasfischl.rayden.reporting;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Stack;

import com.github.thomasfischl.rayden.api.RaydenExtReporter;
import com.github.thomasfischl.rayden.reporting.model.Report;
import com.github.thomasfischl.rayden.reporting.model.ReportKeyword;
import com.github.thomasfischl.rayden.reporting.model.ReportMessage.ReportMessageType;

public class RaydenXMLReporter implements RaydenExtReporter {

  private Report report;

  private Stack<ReportKeyword> stack = new Stack<>();

  private ReportKeyword currReportKeyword;

  private File workingDirectory = new File(".");

  public void setWorkingDirectory(File workingDirectory) {
    this.workingDirectory = workingDirectory;
  }

  @Override
  public void reportKeywordBegin(String name) {
    stack.push(currReportKeyword);

    ReportKeyword keyword = new ReportKeyword();
    keyword.setName(name);
    keyword.setStartTime(getTime());

    currReportKeyword.addReportKeyword(keyword);
    currReportKeyword = keyword;
  }

  @Override
  public void reportKeywordEnd(String name) {
    currReportKeyword.setFinishTime(getTime());
    currReportKeyword = stack.pop();
  }

  @Override
  public void reportUnkownKeyword(String name) {
    // TODO Fix me
  }

  @Override
  public void log(String msg) {
    if (currReportKeyword != null) {
      currReportKeyword.addMessage(ReportMessageType.INFO, msg);
    }
  }

  @Override
  public void error(String msg) {
    if (currReportKeyword != null) {
      currReportKeyword.addMessage(ReportMessageType.ERROR, msg);
    }
  }

  @Override
  public void error(Throwable e) {
    ByteArrayOutputStream os = new ByteArrayOutputStream();
    e.printStackTrace(new PrintStream(os, true));
    error(os.toString());
  }

  @Override
  public void reportTestCaseStart(String name) {
    ReportKeyword keyword = new ReportKeyword();
    keyword.setName(name);
    keyword.setStartTime(getTime());
    currReportKeyword = keyword;

    report.getTestCases().add(currReportKeyword);
  }

  @Override
  public void reportTestCaseEnd(String keywordName) {
    currReportKeyword.setFinishTime(getTime());
    currReportKeyword = null;
    stack.clear();
  }

  @Override
  public void start() {
    report = new Report();
    report.setStartTime(getTime());
  }

  @Override
  public void stop() {
    report.setFinishTime(getTime());
    RaydenXMLReportStore store = new RaydenXMLReportStore();
    workingDirectory.mkdirs();
    store.store(new File(workingDirectory, "report-" + report.getStartTime() + ".xml"), report);
  }

  private long getTime() {
    return System.currentTimeMillis();
  }
}
