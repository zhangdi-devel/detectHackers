
import com.ovoenergy.kafka.serialization.core._
import com.ovoenergy.kafka.serialization.circe._
import org.apache.kafka.clients.producer.ProducerRecord

// Import the Circe generic support
import io.circe.generic.auto._
import io.circe.syntax._

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.CommonClientConfigs._

import scala.collection.JavaConverters._

import scala.util.Random
import java.util.Calendar

object Simulation {

  val numOfRecords = 10000000
  val numOfUser = 10000

  case class Test(x: Float, y: Float)

  def main(args: Array[String]): Unit = {
    val producer = new KafkaProducer(
      Map[String, AnyRef](BOOTSTRAP_SERVERS_CONFIG->"localhost:9092").asJava,
      nullSerializer[Unit],
      circeJsonSerializer[Event]
    )

    //val userLoc: Array[(Float, Float)] = Array.fill(numOfUser)((0F, 0F))
    val now: Long = Calendar.getInstance.getTimeInMillis

    for (i <- 1 to numOfRecords) {

      val uid = Random.nextInt(numOfUser)

      val ts: Long = now + i

      val x: Float = Random.nextFloat() * 100

      val y: Float = Random.nextFloat() * 100

      val event = if (Random.nextInt(1000) < 10) {
        Event(uid, ts, x + 100, y + 100)
      } else {
        Event(uid, ts, x, y)
      }

      val pr = new ProducerRecord[Unit, Event]("Spent", event)

      producer.send(pr)
    }

    producer.close()

  }
}
