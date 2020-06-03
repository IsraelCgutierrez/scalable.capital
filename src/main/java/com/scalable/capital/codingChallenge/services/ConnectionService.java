package com.scalable.capital.codingChallenge.services;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class ConnectionService {
  private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionService.class);

  public Document getDocument(String url) throws IOException {
    Document connection = Jsoup.parse(new URL(url), 6000);
    return connection.clone();
  }


}
