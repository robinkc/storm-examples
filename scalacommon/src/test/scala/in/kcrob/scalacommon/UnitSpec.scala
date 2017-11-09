package in.kcrob.scalacommon

import org.scalamock.scalatest.MockFactory
import org.scalatest._

/**
  * Created by kcrob.in on 09/11/17.
  */
abstract class UnitSpec
  extends FunSpec
    with MockFactory
    with Matchers
    with OptionValues
    with Inside
    with Inspectors
    with BeforeAndAfter
    with BeforeAndAfterAll
    with Logging

//Only a marker class to mark tests as component
abstract class ComponentSpec extends UnitSpec

//Only a marker class to mark tests as Integration
abstract class IntegrationSpec extends UnitSpec