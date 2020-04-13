import java.util

import com.alibaba.fastjson.JSON

object NewsUtil {
  def editNews(order: (String, String)) = {
    val conn = RedisConnection.getConnection()
    var obj = JSON.parseObject(order._2)
    var user = obj.get("userId")
    var tag = obj.get("tags").toString.split(";")
    var types = obj.get("type").toString

    if (types.equals("liulan")) {
      tag.foreach(x => {
        var score = conn.zincrby("users:" + user + ":tagNotes", 1, x).intValue()
        if (score > 5) {
          conn.sadd("users:" + user + ":tag", x)

          var tagNews: util.Set[String] = conn.smembers("tag:" + x + ":news")

          tagNews.toArray().foreach(y => {

            conn.sadd("users:" + user + ":recommand", y.toString)
          })
        }
      })
    } else if (types.equals("register")) {
      tag.foreach(x => {

        var tagNews: util.Set[String] = conn.smembers("tag:" + x + ":news")
        tagNews.toArray().foreach(y => {
          conn.sadd("users:" + user + ":recommand", y.toString)
        })
      })
    }


    else if (types.equals("zan") || types.equals("shoucang") || types.equals("pinglun")) {
      tag.foreach(x => {
        var score = conn.zincrby("users:" + user + ":tagNotes", 5, x).intValue()

        conn.sadd("users:" + user + ":tag", x)

        var tagNews: util.Set[String] = conn.smembers("tag:" + x + ":news")

        tagNews.toArray().foreach(y => {

          conn.sadd("users:" + user + ":recommand", y.toString)
        })


      })
    } else if (types.equals("quxiaozan") || types.equals("quxiaoshoucang")) {
      tag.foreach(x => {
        var score = conn.zincrby("users:" + user + ":tagNotes", -8, x).intValue()
        if (score < 0) {
          conn.srem("users:" + user + ":tag", x)
          var tagNews: util.Set[String] = conn.smembers("tag:" + x + ":news")

          tagNews.toArray().foreach(y => {

            conn.srem("users:" + user + ":recommand", y.toString)
          })
        }

      })

    }
    else if (types.equals("cai")) {
      tag.foreach(x => {
        var score = conn.zincrby("users:" + user + ":tagNotes", -10, x).intValue()
        if (score < 0) {
          conn.srem("users:" + user + ":tag", x)
          var tagNews: util.Set[String] = conn.smembers("tag:" + x + ":news")

          tagNews.toArray().foreach(y => {

            conn.srem("users:" + user + ":recommand", y.toString)
          })
        }

      })

    } else if (types.equals("quxiaocai")) {
      tag.foreach(x => {
        var score = conn.zincrby("users:" + user + ":tagNotes", 4, x).intValue()
        if (score > 5) {
          conn.sadd("users:" + user + ":tag", x)
          var tagNews: util.Set[String] = conn.smembers("tag:" + x + ":news")

          tagNews.toArray().foreach(y => {

            conn.sadd("users:" + user + ":recommand", y.toString)
          })
        }


      })

    }
    conn.close()


  }
}
