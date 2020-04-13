import java.util

import com.alibaba.fastjson.JSON

object RecommandUtil {
    def recommand(order:(String,String))={
        val conn=RedisConnection.getConnection()
        var obj= JSON.parseObject(order._2)
        var id=obj.get("id").toString
        var tags=obj.get("tag").toString.split(";")
        var users: util.Set[String] = conn.zrange("activeUser",0,-1)
        users.toArray().foreach(user=>{
            var flag=true

            for(y<-tags if flag){
               if(conn.sismember("user:"+user.toString+":tag",y)){
                   conn.sadd("user:"+user.toString+":recommand",id)
                   flag=false
               }
            }
        })

        tags.foreach(tag=>{
            var news: util.Set[String] = conn.smembers("tag:"+tag+":news")

            conn.sadd("tag:"+tag+":news",id);
            news.toArray().foreach(x=>{
                conn.sadd("news:"+x+":recommand",id)
                conn.sadd("news:"+id+":recommand",x.toString);


            })

        }


    )
        conn.close()
    }



}
