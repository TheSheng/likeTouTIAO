
import java.util

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.evaluation.{RankingMetrics, RegressionMetrics}
import org.apache.spark.mllib.recommendation.{ALS, Rating}
import org.apache.spark.{SparkConf, SparkContext}
import org.jblas.DoubleMatrix
import scala.collection.mutable
/**
  * Created by hadoop on 17-5-3.
  * 协同过滤(处理对象movie,使用算法ALS:最小二乘法(实现用户推荐)
  * 余弦相似度实现商品相似度推荐
  */
object alsReCommand {
    def main(args: Array[String]): Unit = {
        Logger.getLogger("org.apache.spark").setLevel(Level.WARN)
        Logger.getLogger("org.eclipse.jetty.server").setLevel(Level.OFF)
        val conf=new SparkConf().setMaster("local").setAppName("AlsTest")
        val sc=new SparkContext(conf)
        CF(sc,"/Users/lisheng/Documents/Jhipster/毕业设计/spark/rate.data")
    }
    def getByValue(map:scala.collection.mutable.Map[String,Int],value:Int): String ={
        var key=""
        var isContinue=true
        map.foreach(x=>{
            if(isContinue) {
                if (x._2 == value) {
                    key = x._1
                    isContinue=false
                }
            }
        })
        key
    }
    def CF(sc:SparkContext,fileName:String): Unit ={
        val movieFile=sc.textFile(fileName)

        val RatingDatas=movieFile.map(_.split("\t").take(3))
        //转为Ratings数据
        println()
        import scala.collection.JavaConversions.mapAsScalaMap
        var map:scala.collection.mutable.Map[String,Int]=scala.collection.mutable.Map();
        val value = sc.broadcast(map)
        var tagindex=1;

        var usermap:scala.collection.mutable.Map[String,Int]=scala.collection.mutable.Map();
        val uservalue = sc.broadcast(usermap)
        var userindex=1;
        RatingDatas.foreach(x=>{

            if(!value.value.contains(x(1))){
                value.value+=(x(1)->tagindex)
                tagindex=tagindex+1
            }
            if(!uservalue.value.contains(x(0))){
                uservalue.value+=(x(0)->userindex)
                userindex=userindex+1
            }



        })
       map=value.value
        usermap=uservalue.value

        val ratings=RatingDatas.map(x =>Rating(usermap.get(x(0)).get,map.get(x(1)).get,x(2).toDouble))
        //获取用户评价模型,设置k因子,和迭代次数,隐藏因子lambda,获取模型
        /*
        *   rank ：对应ALS模型中的因子个数，也就是在低阶近似矩阵中的隐含特征个数。因子个
              数一般越多越好。但它也会直接影响模型训练和保存时所需的内存开销，尤其是在用户
              和物品很多的时候。因此实践中该参数常作为训练效果与系统开销之间的调节参数。通
              常，其合理取值为10到200。
            iterations ：对应运行时的迭代次数。ALS能确保每次迭代都能降低评级矩阵的重建误
               差，但一般经少数次迭代后ALS模型便已能收敛为一个比较合理的好模型。这样，大部分
               情况下都没必要迭代太多次（10次左右一般就挺好）。
           lambda ：该参数控制模型的正则化过程，从而控制模型的过拟合情况。其值越高，正则
              化越严厉。该参数的赋值与实际数据的大小、特征和稀疏程度有关。和其他的机器学习
              模型一样，正则参数应该通过用非样本的测试数据进行交叉验证来调整。
        * */
        val model=ALS.train(ratings,50,10,0.01)
        //基于用户相似度推荐
       // println("userNumber:"+model.userFeatures.count()+"\t"+"productNum:"+model.productFeatures.count())
        //指定用户及商品,输出预测值
        //println(model.predict(789,123))
        //为指定用户推荐的前N商品
//        model.recommendProducts(2,11).foreach(x=>{
//            println(getByValue(usermap,x.user),getByValue(map,x.product),x.rating)
//        })
        //为每个人推荐前十个商品
        model.recommendProductsForUsers(10).take(model.userFeatures.count().toInt).foreach{

            case(x,y) =>y.foreach(z=>{
                println(getByValue(usermap,z.user),getByValue(map,z.product),z.rating)
            })
        }
        //基于商品相似度(使用余弦相似度)进行推荐,获取某个商品的特征值
        val itemFactory=model.productFeatures.lookup(3).head
        val itemVector=new DoubleMatrix(itemFactory)
        //求余弦相似度
        val sim=model.productFeatures.map{
            case(id,factory)=>
                val factorVector=new DoubleMatrix(factory)
                val sim=cosineSimilarity(factorVector,itemVector)
                (id,sim)
        }
        val sortedsim=sim.top(11)(Ordering.by[(Int,Double),Double]{
            case(id,sim)=>sim
        })
       // println(sortedsim.take(10).mkString("\n"))
        //模型评估,通过均误差
        //实际用户评估值
        val actualRatings=ratings.map{
            case Rating(user,item,rats) => ((user,item),rats)
        }
        val userItems=ratings.map{
            case(Rating(user,item,rats)) => (user,item)
        }
        //模型的用户对商品的预测值
        val predictRatings=model.predict(userItems).map{
            case(Rating(user,item,rats)) =>((user,item),rats)
        }
        //联合获取rate值
        val rates=actualRatings.join(predictRatings).map{
            case x =>(x._2._1,x._2._2)
        }
        //求均方差
        val regressionMetrics=new RegressionMetrics(rates)
        //越接近0越佳
        //println(regressionMetrics.meanSquaredError)
        //全局平均准确率(MAP)
        val itemFactors = model.productFeatures.map { case (id, factor)
        => factor }.collect()
        val itemMatrix = new DoubleMatrix(itemFactors)
        //分布式广播商品的特征矩阵
        val imBroadcast = sc.broadcast(itemMatrix)
        //计算每一个用户的推荐,在这个操作里，会对用户因子矩阵和电影因子矩阵做乘积，其结果为一个表示各个电影预计评级的向量（长度为
        //1682，即电影的总数目）
        val allRecs = model.userFeatures.map{ case (userId, array) =>
            val userVector = new DoubleMatrix(array)
            val scores = imBroadcast.value.mmul(userVector)
            val sortedWithId = scores.data.zipWithIndex.sortBy(-_._1)
            val recommendedIds = sortedWithId.map(_._2 + 1).toSeq   //+1,矩阵从0开始
            (userId, recommendedIds)
        }
        //实际评分
        val userMovies = ratings.map{ case Rating(user, product, rating) =>
            (user, product)}.groupBy(_._1)
        val predictedAndTrueForRanking = allRecs.join(userMovies).map{ case
            (userId, (predicted, actualWithIds)) =>
            val actual = actualWithIds.map(_._2)
            (predicted.toArray, actual.toArray)
        }
        //求MAP,越大越好吧
        val rankingMetrics = new RankingMetrics(predictedAndTrueForRanking)
        println("Mean Average Precision = " + rankingMetrics.meanAveragePrecision)
    }
    //余弦相似度计算
    def cosineSimilarity(vec1:DoubleMatrix,vec2:DoubleMatrix):Double={
        vec1.dot(vec2)/(vec1.norm2()*vec2.norm2())
    }
}
