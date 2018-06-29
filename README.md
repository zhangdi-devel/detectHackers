# detectHackers
detect hackers

## Event
Event is a case class that contains UserId, Timestamp, X position, and Y position

## Simulation
10,000,000 events are simulated every millisecond

The normal X and Y positions are set to sample from Uniform(0.0, 100.0)

For about 1% of events, an additional 100 is added to X and Y.

## JSON Serialization and Deserialization
For the simulation, which is a pure kafka producer, a Kafka-JSON library for scala was used: https://github.com/ovotech/kafka-serialization.
It transforms case class directly to JSON byte array.

For the flink-kafka consumer, a custom class (EventDeserializer) was implemented to extend org.apache.flink.api.common.serialization.AbstractDeserializationSchema.
In the derialize method, json4s was used to read byte array to the Event case class.

## Hacker detection functionality
The main functionality was implemented in a RichFlatMapFunction: UpdateLoc.

It maintains a state of the last Event.

Whenever a new Event comes, the function checks if the travel speed is too fast.
If it is, output the two events.
