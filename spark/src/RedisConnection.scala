import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

//Redis连接池
object RedisConnection {
  val conf = new JedisPoolConfig()
  //最大连接数
  conf.setMaxTotal(20)
  //最大空闲连接数
  conf.setMaxIdle(10)
  conf.setTestOnBorrow(true)
  conf.setMaxWaitMillis(5000)


  val pool = new JedisPool(conf, "129.211.63.167", 6379, 10000,"lisheng442")

  def getConnection(): Jedis = {
    pool.getResource()
  }


}
