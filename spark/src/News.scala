import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.dstream.ReceiverInputDStream
import org.apache.spark.streaming.kafka.KafkaUtils

object News {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf().setAppName("order").setMaster("local[*]").setJars(List("/Users/lisheng/Documents/SparkDemo/out/artifacts/SparkDemo_jar"))
        val ssc = new StreamingContext(conf, Seconds(2))

        //指定整合kafka相关的参数
        val zkQueue = "152.136.137.34:2181"
        val groupId = "test"
        val topic = Map[String, Int]("userNote" -> 1)

        val data: ReceiverInputDStream[(String, String)] = KafkaUtils.createStream(ssc, zkQueue, groupId, topic)
        data.print()
        data.foreachRDD(x => {
            x.foreachPartition(y => {

                y.foreach(z => {
                    NewsUtil.editNews(z)


                })

            })


        })
        ssc.start()
        ssc.awaitTermination()

    }
}
