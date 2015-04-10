package com.utils.google;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * The google result crawler which use the thread random sleep and fake user
 * agent.
 * 
 * @author birui
 *
 */
public class GoogleSearch {

	static final String[] userAgentList = {
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20130406 Firefox/23.0",
			"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/533(KHTML, like Gecko) Element Browser 5.0",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:5.0) Gecko/20100101 Firefox/5.0",
			"IBM WebExplorer /v0.94', 'Galaxy/1.0 [en] (Mac OS X 10.5.6; U; en)",
			"Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)",
			"Opera/9.80 (Windows NT 6.0) Presto/2.12.388 Version/12.14",
			"Mozilla/5.0 (iPad; CPU OS 6_0 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Version/6.0 Mobile/10A5355d Safari/8536.25",
			" Version/6.0 Mobile/10A5355d Safari/8536.25 Chrome/28.0.1468.0 Safari/537.36",
			"Chrome/28.0.1468.0 Safari/537.36" }; // fake user agent list for
													// search
	static Random r = new Random();

	/**
	 * Searching TOP X results from google and crawler only title and snippet of
	 * each result
	 * 
	 * @param query
	 *            The searching query
	 * @param range
	 *            The search result number
	 * @return
	 */
	public static String results(String query, int range) {
		// TODO Auto-generated constructor stub
		String pageContent = "";
		String newQuery = query.replaceAll(" ", "+");
		try {
			if (range > 5) {
				timeout();
			}
			List<Document> documentList = new ArrayList<Document>();
			int page;
			if (range % 10 != 0) {
				page = 1 + range / 10;
			} else {
				page = range / 10;
			}
			for (int i = 0; i < page; i++) {
				documentList.add(Jsoup
						.connect(
								"http://www.google.com/search?q=" + newQuery
										+ "&start=" + i)
						.userAgent(userAgentList[r.nextInt(8)]).get());
			}

			for (Document document : documentList) {
				Elements titles = document.getElementsByClass("r");
				Elements snippets = document.getElementsByClass("st");

				for (int j = 0; j < range; j++) {
					pageContent += " "
							+ titles.get(j).text().trim()
									.replaceAll("[\\p{Punct}\\s]+", " ");
					pageContent += " "
							+ snippets.get(j).text().trim()
									.replaceAll("[\\p{Punct}\\s]+", " ");
				}
			}

			System.out.println(query + " searching done!");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			pageContent += "";
		}
		return pageContent;
	}

	private static void timeout() {
		// TODO Auto-generated method stub
		int sleepTime = r.nextInt(10000) + 20000;
		System.out.println("Sleeping...." + sleepTime + "ms.");
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
