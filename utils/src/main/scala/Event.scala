
case class Event(uid: Int, timestamp: Long, x: Float, y: Float)

object Event {

  def distance(p1: Event, p2: Event): Double = {
    math.sqrt(math.pow(p1.x - p2.x, 2) + math.pow(p1.y - p2.y, 2))
  }

  def speed(e1: Event, e2: Event): Double = {

    val td = math.abs(e1.timestamp - e2.timestamp).toDouble/(3600 * 1000)

    distance(e1, e2)/td
  }

  val tooFast = 100

}
