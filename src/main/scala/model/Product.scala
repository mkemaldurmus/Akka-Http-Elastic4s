package model

case class Product(item_id: Long, name: String, locale: String, click: Int, purchase: Int)

case class Message(message: String)

case class Config(id: Long, sort: String, status: Boolean)

case class RequestParameters(size: Int, page: Int) {
  require(size > 1 && size < 10000, "size must be 1-10000")
  require(page > 0 && page < 1000, "page must be 1-1000")
}
