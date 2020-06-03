package com.scalable.capital.codingChallenge;

import com.scalable.capital.codingChallenge.services.CrawlerService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
class CodingChallengeApplicationTests {

  @Autowired
  CrawlerService crawlerService;

  @Test
  void extractNameTest() {
    String l1 = "name.js";
    String result = crawlerService.extractName(l1);
    assertEquals(l1, result);

    String l2 = "././..././././.././././name.js";
    result = crawlerService.extractName(l2);
    assertEquals(l1, result);

    String l3 = "././..././././.././././name.js?some=stuff";
    result = crawlerService.extractName(l3);
    assertEquals(l1, result);

  }

  @Test
  void googleTest() throws IOException {
    List<String> result = crawlerService.getGoogleResult("rand");
    assertNotNull(result);
    assertTrue(result.size() > 0);
    result.forEach(Assert::assertNotNull);
  }

  @Test
  void getLibrariesTest() {
    List<String> result = crawlerService.getLibraries("https://regex101.com/");
    assertNotNull(result);
    assertTrue(result.size() > 0);
    result.forEach(Assert::assertNotNull);
  }

  @Test
  void getLibrariesErrorTest() {
    List<String> result = crawlerService.getLibraries("no page");
    assertNotNull(result);
    assertEquals(0, result.size());
  }

}
