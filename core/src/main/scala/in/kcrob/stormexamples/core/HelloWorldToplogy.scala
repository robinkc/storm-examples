package in.kcrob.stormexamples.core

import java.util

import org.apache.storm.spout.SpoutOutputCollector
import org.apache.storm.task.{OutputCollector, TopologyContext}
import org.apache.storm.{Config, LocalCluster, topology}
import org.apache.storm.topology.OutputFieldsDeclarer
import org.apache.storm.topology.base.{BaseRichBolt, BaseRichSpout}
import org.apache.storm.tuple.{Fields, Tuple, Values}
import org.apache.storm.utils.Utils


/**
  * Created by kcrob.in on 09/11/17.
  */

//Inspired by storm's code base
class NameSpout
  extends BaseRichSpout{

  var collector: SpoutOutputCollector = _

  override def open(conf: util.Map[_, _], context: TopologyContext, collector: SpoutOutputCollector): Unit = {
    this.collector = collector
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("word"))
  }

  override def nextTuple(): Unit = {
    Utils.sleep(100)
    collector.emit(new Values("Robin"))
  }

}

class HelloBolt
  extends BaseRichBolt {

  var collector: OutputCollector = _

  override def prepare(stormConf: util.Map[_, _], context: TopologyContext, collector: OutputCollector): Unit ={
    this.collector = collector
  }

  override def execute(input: Tuple): Unit = {
    collector.emit(input, new Values (
      "Hello " + input.getString(0)
    ))
    collector.ack(input)
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("word"))
  }
}

class PrintlnBolt
  extends BaseRichBolt {

  var collector: OutputCollector = _

  override def prepare(stormConf: util.Map[_, _], context: TopologyContext, collector: OutputCollector): Unit ={
    this.collector = collector
  }

  override def execute(input: Tuple): Unit = {
    val str = input.getString(0)
    println(str)
    collector.ack(input)
  }

  override def declareOutputFields(declarer: OutputFieldsDeclarer): Unit = {
    declarer.declare(new Fields("word"))
  }
}

object HelloWorldToplogy
  extends App{

  import org.apache.storm.topology.TopologyBuilder

  private val builder = new TopologyBuilder
  builder.setSpout("name", new NameSpout)
  builder.setBolt("hello", new HelloBolt).shuffleGrouping("name")
  builder.setBolt("print", new PrintlnBolt).shuffleGrouping("hello")

  private val conf = new Config
//  conf.setDebug(true)
  conf.setNumWorkers(3)

  private val cluster = new LocalCluster()
  cluster.submitTopology("Helloji", conf, builder.createTopology())

  Thread.sleep(10000)
  //Utils.sleep(10000)

  cluster.killTopology("Helloji")
  cluster.shutdown()

}
