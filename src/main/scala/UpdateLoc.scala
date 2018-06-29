import org.apache.flink.api.common.functions.RichFlatMapFunction
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.util.Collector

class UpdateLoc extends RichFlatMapFunction[Event, (Event, Event)] {

  lazy val lastEvent: ValueState[Event] = getRuntimeContext.getState(
    new ValueStateDescriptor[Event]("last event", classOf[Event])
  )

  override def flatMap(value: Event, out: Collector[(Event, Event)]): Unit = {
    val last = lastEvent.value()
    if (last != null && Event.speed(value, last) > Event.tooFast) {
      out.collect((last, value))
    }
    lastEvent.update(value)
  }
}
