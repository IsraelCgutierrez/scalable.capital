package com.scalable.capital.codingChallenge;

import com.scalable.capital.codingChallenge.services.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CodingChallengeApplication implements CommandLineRunner {
  private static Logger LOG = LoggerFactory
     .getLogger(CodingChallengeApplication.class);

  @Autowired
  CrawlerService crawlerService;

  public static void main(String[] args) {
    SpringApplication.run(CodingChallengeApplication.class, args);
  }

  @Override
  public void run(String... args) {
    if (args.length > 0) {
      crawlerService.process(args[0]);
    }else{
      LOG.info("No Keyword provided");
    }
  }
}
