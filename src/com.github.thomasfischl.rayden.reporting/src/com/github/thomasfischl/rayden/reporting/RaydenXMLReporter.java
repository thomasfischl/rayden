package com.github.thomasfischl.rayden.reporting;

import java.io.File;
import java.util.Stack;

import com.github.thomasfischl.rayden.api.IRaydenExtReporter;
import com.github.thomasfischl.rayden.reporting.model.Report;
import com.github.thomasfischl.rayden.reporting.model.ReportKeyword;

public class RaydenXMLReporter implements IRaydenExtReporter {

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

    currReportKeyword.getKeywords().add(keyword);
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
    // TODO Auto-generated method stub

  }

  @Override
  public void error(String msg) {
    // TODO Auto-generated method stub

  }

  @Override
  public void error(Throwable e) {
    // TODO Auto-generated method stub

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
