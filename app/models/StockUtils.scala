package models

/**
 * TODO: This class/file will contain my code to get stock prices.
 *       Treat this class like a web service client.
 */
import java.io._
import scala.io.Source

// TODO Use like this:
//import java.io._
//import scala.io.Source
//import com.alvinalexander.finance.model.StockUtils
//
//// XPath: http://thinkandroid.wordpress.com/2010/01/05/using-xpath-and-html-cleaner-to-parse-html-xml/
//// XPath: http://www.programmingmobile.com/2012/01/tutorial-scraping-html-with-htmlcleaner.html
//object Main extends App {
//  
//  def getStockPrice(symbol: String) = {
//    val html = IO.getHtmlFromFile(symbol)
//    StockUtils.extractPriceFromHtml(html, symbol)
//  }
//
//}


import org.htmlcleaner.HtmlCleaner

object StockUtils {

  // not tested
  def getHtmlFromUrl(symbol: String) = {
    val url = "http://finance.yahoo.com/q/ks?s=%s+Key+Statistics".format(symbol.trim.toUpperCase)
    Source.fromURL(url).mkString
  }

  def getHtmlFromFile(symbol: String) = {
    val filename = symbol + ".html"
    Source.fromFile(filename).getLines.mkString
  }

  def saveContentsToFile(contents: String, filename: String) {
    val pw = new PrintWriter(new File(filename))
    pw.write(contents)
    pw.close
  }

  /*
   * <div class="yfi_rt_quote_summary_rt_top"><p>
   * <span class="time_rtq_ticker">
   * <span id="yfs_l84_hpq">19.70</span></span>
   */
  def extractPriceFromHtml(html: String, symbol: String): String = {
    val cleaner = new HtmlCleaner
    val rootNode = cleaner.clean(html)

    var keepLooking = true
    var price = "0.00"
    val elements = rootNode.getElementsByName("span", true)
    for (elem <- elements if keepLooking) {
      val classType = elem.getAttributeByName("id")
      if (classType != null && classType.equalsIgnoreCase("yfs_l84_" + symbol.toLowerCase)) {
        price = elem.getText.toString.trim
        keepLooking = false
      }
    }
    return price
  }

}












