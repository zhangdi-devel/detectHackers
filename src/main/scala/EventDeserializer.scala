import org.apache.flink.api.common.serialization.AbstractDeserializationSchema
import java.io.{ByteArrayInputStream, InputStreamReader}

import org.json4s.NoTypeHints
import org.json4s.native.Serialization

class EventDeserializer extends AbstractDeserializationSchema[Event] {
  override def deserialize(message: Array[Byte]): Event = {
    implicit val formats = Serialization.formats(NoTypeHints)
    val reader = new InputStreamReader(new ByteArrayInputStream(message))
    Serialization.read[Event](reader)
  }
}
