package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import views._
import models._
import scala.collection.mutable.ArrayBuffer

object Stocks extends Controller {

  // TODO what does apply and unapply do for you?
  // TODO what is this field used for?
  // Once defined it handle automatically, validation, submission, errors, redisplaying ...
  // TODO i don't know how to handle 'id' here
  // http://www.playframework.org/documentation/2.0.4/ScalaTodoList
  // http://www.playframework.org/documentation/2.0.4/ScalaForms
  // text, boolean, etc: http://www.playframework.org/documentation/api/2.0/scala/play/api/data/Forms$.html
  val stockForm: Form[Stock] = Form(
    // defines a mapping that will handle Stock values
    // "id" -> ignored(1234),
    mapping(
      // verifying here creates a field-level error
      // if your test returns false, the error is shown
      "symbol" -> nonEmptyText.verifying("Doh - Stock already exists (1)!", Stock.findBySymbol(_) == 0),
      "company" -> optional(text))
      ((symbol, company) => Stock(0, symbol, company))
      ((s: Stock) => Some((s.symbol, s.company)))
      verifying("Doh - Stock already exists (2)!", fields => fields match {
        // this block creates a 'form' error; trying to display it in the template
        // this only gets called if all field validations are okay
        case Stock(i, s, c) =>  Stock.findBySymbol(s) == 0
      })
  )
  // TODO - I don't understand that unapply method
  // "ean" -> nonEmptyText.verifying("Doh - Stock already exists!", Stock.findBySymbol(_).isEmpty),
  
  text
  
  def list = Action {
    Ok(html.stock.list(Stock.getAll3(), stockForm))
  }
  
  /**
   * I think this maps to views.stock.[form.scala.html]
   */
  def form = Action {
    Ok(html.stock.form(stockForm))
  }
  
  def add = Action {
    Ok(html.stock.form(stockForm))
  }
  
  /**
   * Display a form pre-filled with an existing Stock.
   */
  def edit(id: Long) = TODO
  
  /**
   * Handle form submission.
   */
  def submit = Action { implicit request =>
    stockForm.bindFromRequest.fold(
      errors => BadRequest(html.stock.form(errors)),  // back to form
      stock => {
        val res = Stock.save2(stock)
        println("SAVE RES: " + res)
        Redirect(routes.Stocks.list)
      }
    )
  }
  
    // from Contacts
//  def submit = TODO
//  def submit = Action { implicit request =>
//    stockForm.bindFromRequest.fold(
//      errors => BadRequest(html.stock.form(errors)),
//      stock => Ok(html.stock.summary(stock))
//    )
//  }
  
  def delete(id: Long) = Action {
    val res = Stock.deleteFs("FOO")
    println("DELETE RESULT: " + res)
    //Stock.delete(id)
    Redirect(routes.Stocks.list)
  }  

  def getCurrentPrice(symbol: String) = Action {
    val htmlContent = StockUtils.getHtmlFromUrl(symbol)
    val price = StockUtils.extractPriceFromHtml(htmlContent, symbol)
    Ok(html.stock.price(symbol, price))
  }
  
  def currentPrices() = Action {
    // get a list of all stocks
    val stocks = Stock.getAll1()
    // get the price for each stock
    var stockPrices = new ArrayBuffer[(String, BigDecimal)]()
    stocks.foreach{ stock =>
      val htmlContent = StockUtils.getHtmlFromUrl(stock.symbol)
      val priceString = StockUtils.extractPriceFromHtml(htmlContent, stock.symbol)
      val price = BigDecimal(priceString)
      val tup = (stock.symbol, price)
      stockPrices += tup
    }
    Ok(html.stock.current_prices(stockPrices.toList))
  }
  
}



















