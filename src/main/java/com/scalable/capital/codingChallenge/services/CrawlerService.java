package com.scalable.capital.codingChallenge.services;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Service
public class CrawlerService {

  @Autowired
  ConnectionService connectionService;

  private static Logger LOG = LoggerFactory
     .getLogger(CrawlerService.class);

  /**
   * Process specific keyword
   *
   * @param searchKey
   */
  public void process(String searchKey) {
    LOG.info("Process: " + searchKey);
    try {
      List<String> urls = getGoogleResult(searchKey);
      List<String> libraries = extractLibraries(urls);
      printTop5(libraries);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * prints top 5 libraries given a list of them
   *
   * @param libraries
   */
  public void printTop5(List<String> libraries) {
    Map<String, Long> ordered = countByStreamGroupBy(libraries).entrySet()
       .stream()
       .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
       .collect(
          toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
             LinkedHashMap::new));
    for (String s : new ArrayList<>(ordered.keySet()).subList(0, Math.min(5, ordered.size()))) {
      System.out.println(s + " -> " + ordered.get(s));
    }
  }

  /**
   * Groups by name and count frequency
   *
   * @param inputList
   * @param <T>
   * @return
   */
  public <T> Map<T, Long> countByStreamGroupBy(List<T> inputList) {
    return inputList.stream().collect(Collectors.groupingBy(k -> k, Collectors.counting()));
  }


  /**
   * Extract all libraries from a list of urls
   *
   * @param urls
   * @return
   */
  private List<String> extractLibraries(List<String> urls) {
    return urls.stream().parallel().flatMap(url -> getLibraries(url).stream()).collect(Collectors.toList());
  }

  /**
   * Get a list of libraries from a url
   *
   * @param url
   * @return
   */
  public List<String> getLibraries(String url) {
    try {
      Document page = connectionService.getDocument(url);
      Elements elem = page.select("script[src]");
      List<String> paths = elem.stream().map(e -> e.attr("src")).collect(Collectors.toList());
      return paths.stream().map(this::extractName).collect(Collectors.toList());
    } catch (IOException e) {
      LOG.error("Can't open :" + url);
    }
    return new ArrayList<>();
  }

  /**
   * given a library it extracts the name ignoring the paths
   *
   * @param path
   * @return
   */
  public String extractName(String path) {
    if (path.lastIndexOf("/") > 0) {
      return path.substring(path.lastIndexOf("/") + 1).replaceAll("\\?.+","");
    }
    return path;
  }

  /**
   * crawls the results from google
   *
   * @param searchKey
   * @return
   * @throws IOException
   */
  public List<String> getGoogleResult(String searchKey) throws IOException {
    Document googlePage = connectionService.getDocument("https://www.google.com/search?q=" + searchKey);
    Elements elem = googlePage.select(".r a:has(h3)");
    return elem.stream().map(e -> e.attr("href")).collect(Collectors.toList());
  }
}
