package com.kunyan.dispatcher.parser

import org.jsoup.Jsoup
import scala.collection.mutable.ListBuffer
import scala.util.parsing.json.JSON

/**
  * Created by niujiaojiao on 2016/5/18.
  */
object SnowballParser {

  /**
    * 解析雪球网沪深,理财板块
    *
    * @param html 将要解析的文本字符串
    * @return 新闻标题的url链接
    */
  def parse(html: String): ListBuffer[String] = {

    val pageUrl = "https://xueqiu.com"

    getNews(html, pageUrl)

  }

  /**
    * 解析雪球网热门板块：解析JSON字符串
    *
    * @param json 将要解析的文本字符串
    * @return 新闻标题的url链接
    */
  def parseHots(json: String): ListBuffer[String] = {

    var result = ListBuffer[String]()
    val jsonInfo = JSON.parseFull(json)

    if (jsonInfo.isEmpty) {
      println("\"JSON parse value is empty,please have a check!\"")
    } else {

      jsonInfo match {

        case Some(mapInfo: List[Map[String, AnyVal]]) =>

          for (i <- mapInfo.indices) {
            val url = mapInfo(i).getOrElse("target", "")
            val total = "https://xueqiu.com" + url
            result += total
          }

        case None => println("Parsing failed!")
        case other => println("Unknown data structure :" + other)

      }

    }

    result

  }

  /**
    * 拼接字符串获取链接
    *
    * @param html   将要解析的文本信息字符串
    * @param preurl 要拼接的字符串
    * @return 新闻标题链接
    */
  def getNews(html: String, preurl: String): ListBuffer[String] = {

    val doc = Jsoup.parse(html, "UTF-8")
    var result = ListBuffer[String]()
    val list = doc.select("div#center div.list_item  div.list_item_rb  div.list_item_tit a[href]")

    for (i <- 0 until list.size) {

      val url = list.get(i).attr("href")
      val total = preurl + url
      result += total

    }

    result

  }

}
