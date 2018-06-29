import org.scalatest.{FlatSpec, Inside, Inspectors, OptionValues}
import org.slf4j.{Logger, LoggerFactory}

abstract class UnitSpec
  extends FlatSpec
    with OptionValues
    with Inside
    with Inspectors {

  def logger: Logger = LoggerFactory.getLogger(getClass)
}
