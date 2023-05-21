package com.practice.crawling;

import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrawlingPracticeApplication {
	  public static void getStockPriceList() {

		    // 1.조회할 URL 셋팅 및 Document 객체로드
		    final String stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=1";
		    Connection conn = Jsoup.connect(stockList);

		    try {
		      // Document 객체 로드
		      Document document = conn.get();
		      
		      // getStockHeader() -> 칼럼명 정보 조회
		      String thead = getStockHeader(document); // 칼럼명 
		      
		      // getStockList()   -> 리스트 데이터 조회
		      String tbody = getStockList(document);   // 데이터 리스트
		      
		      System.out.println(thead);
		      System.out.println(tbody);

		    } catch (IOException ignored) {
		    }
		  }

	  
	  	  // 칼럼명 정보를 조회하는 함수
		  public static String getStockHeader(Document document) {
		    // tr 태그로 접근
		    Elements stockTableBody = document.select("table.type_2 thead tr");
		    StringBuilder sb = new StringBuilder();
		    // th를 for문으로 각각 조회  
		    for (Element element : stockTableBody) {
		      for (Element td : element.select("th")) {
		        sb.append(td.text());
		        sb.append("   ");
		      }
		      break;
		    }
		    return sb.toString();
		  }
		  
		  // 데이터 리스트 조회하는 함수
		  public static String getStockList(Document document) {
		    // document.select() 로  html 태그에 접근하여  tr 태그로 접근
		    Elements stockTableBody = document.select("table.type_2 tbody tr");
		    StringBuilder sb = new StringBuilder();
		    for (Element element : stockTableBody) {
		      if (element.attr("onmouseover").isEmpty()) {
		        continue;
		      }
		      
		      // td를 for문으로 조회
		      for (Element td : element.select("td")) {
		        String text;
		        if(td.select(".center a").attr("href").isEmpty()){
		          text = td.text();
		        }else{
		          text = "https://finance.naver.com"+td.select(".center a").attr("href");
		        }
		        sb.append(text);
		        sb.append("   ");
		      }
		      sb.append(System.getProperty("line.separator")); //줄바꿈
		    }
		    return sb.toString();
		  }

	public static void main(String[] args) {
		SpringApplication.run(CrawlingPracticeApplication.class, args);
		getStockPriceList();
	}

}
