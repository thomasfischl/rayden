package com.github.thomasfischl.rayden.reporting;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.github.thomasfischl.rayden.reporting.model.Report;

public class RaydenXMLReportStore {

  public void store(File xmlFile, Report report) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Report.class);
      Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

      // output pretty printed
      jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

      jaxbMarshaller.marshal(report, xmlFile);
      jaxbMarshaller.marshal(report, System.out);

    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

  public Report loadReport(File xmlFile) {
    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Report.class);
      Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
      return (Report) jaxbUnmarshaller.unmarshal(xmlFile);
    } catch (JAXBException e) {
      throw new RuntimeException(e);
    }
  }

}
