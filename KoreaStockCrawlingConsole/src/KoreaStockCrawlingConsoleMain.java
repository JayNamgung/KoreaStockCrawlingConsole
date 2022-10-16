import java.io.IOException;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KoreaStockCrawlingConsoleMain {

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		getStockPriceList();
	}
	
	/**
	 * 주식 가격 목록
	 */
	public static void getStockPriceList() {
		
		final String stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=1";
		Connection conn = Jsoup.connect(stockList);
		
		try {
			Document document = conn.get();
			String thead = getStockHeader(document);
			String tbody = getStockList(document);
			System.out.println(thead);
			System.out.println(tbody);
		} catch (IOException ex) {
		}
	}
	
	/**
	 * 주식 전문 헤더
	 * @param document
	 * @return
	 */
	public static String getStockHeader(Document document) {
		Elements stockTableBody = document.select("table.type_2 thead tr");
		StringBuilder sb = new StringBuilder();
		
		for(Element element : stockTableBody) {
			for(Element td : element.select("th")) {
				sb.append(td.text());
				sb.append(" ");
			}
			break;
		}
		return sb.toString();
	}
	
	/**
	 * 주식 종목 목록
	 * @param document
	 * @return
	 */
	public static String getStockList(Document document) {
		Elements stockTableBody = document.select("table.type_2 tbody tr");
		StringBuilder sb = new StringBuilder();
		
		for(Element element : stockTableBody) {
			if(element.attr("onmouseover").isEmpty()) {
				continue;
			}
		
			for(Element td : element.select("td")) {
				String text;
				if(td.select(".center a").attr("href").isEmpty()) {
					text = td.text();
				}else {
					text = "https://finance.naver.com"+td.select(".center a").attr("href");
		        }
				sb.append(text);
				sb.append(" ");
			}
			sb.append(System.getProperty("line.separator")); // 줄바꿈
		}
		return sb.toString();
	}
}