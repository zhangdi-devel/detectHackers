import java.util.{Properties, UUID}

import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.{FlinkKafkaConsumer011, FlinkKafkaProducer011}
import java.sql.Timestamp
import org.apache.flink.streaming.api.scala._
import org.apache.flink.api.common.serialization.SimpleStringSchema

object DetectHacker {
  def main(args: Array[String]): Unit = {
    val props = new Properties()
    props.setProperty("bootstrap.servers", "localhost:9092")
    props.setProperty("group.id", s"test.${UUID.randomUUID()}")
    props.setProperty("auto.offset.reset", "earliest")

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val events = new FlinkKafkaConsumer011[Event]("Spent", new EventDeserializer(), props)

    val hack = env.addSource(events).keyBy(_.uid).flatMap(new UpdateLoc).map{p =>
      s"User: ${p._1.uid}, current time: ${new Timestamp(p._2.timestamp)}, speed: ${Event.speed(p._1, p._2)}"
    }

    //push the results to a kafka topic "Hackers"
    val producer = new FlinkKafkaProducer011[String](
      "localhost:9092",
      "Hackers",
      new SimpleStringSchema()
    )
    hack.addSink(producer)

    //printing is another way to check the results
    //hack.print()

    env.execute()

  }
}
