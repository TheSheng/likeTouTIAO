package com.lisheng.project.user.controller;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Demo {
    /*** 639172每个位数上的数字都是不同的，且平方后所得数字的所有位数都不会出现组成它自身的数字。

     * （639172*639172=408540845584），类似于639172这样的6位数还有几个？分别是什么？

     *分析：

     * 1. 6位数的范围是：100000-999999

     *    2. 6位数的各个数字不重复（将6位数说循环取10的余数，然后将i= i/10在进行10的余  数，可以获取6位数的每个数子）

     *    3. 6位数平方后的各个位数和6位数不重复

     */

//判断六位数每一位是否重复

    public  static boolean isRepeat(long i){

//将6位数说循环取10的余数，然后将i= i/10在进行10的余数，可以获取6位数的每个数子

//创建新的map存放不重复的数字

        HashMap<Long, String> map= new HashMap<Long,String>();

        while(i!= 0){

//map的key是否包含重复数字

            if(map.containsKey(i%10)){

                return true;

            }else{

//map不包含重复的数字时，将取余获取到的数字放到map的key中

                map.put(i%10, "1");

            }

//获取除过10后获取到的结果（目的时，可以进行取10余数的继续操作，获取十位，百位，千位，万位，十万位上的数字）

            i = i/10;

        }

        return false;

    }

//排队拿六位数平方后的结果是否和六位数重复

    public static boolean isPingFangrepeat(long res,long i){

//创建一个map存放六位数的每个位数的数字

        HashMap<Long, String> map= new HashMap<Long,String>();

//循环获取六位数各个位数上的数字，并放入map集合中

        while(i!=0){

            map.put(i%10, "1");

            i = i/10;

        }

//循环判断平方后的结果的各个数位上的数字是否在map中

        while(res!=0){

            if(map.containsKey(res%10)){

                return true;

            }

            res = res /10;

        }

        return  false;

    }

    public static void main(String[] args) {

        for(long  i=100000;i<= 999999;i++){

//判断6位数中是否又重复的数据

//如果包含重复的数据，就跳过，继续遍历下一个数据

            if(isRepeat(i)){

                continue;

            }else if(isPingFangrepeat(i*i,i)){

                continue;

            }else {

                System.out.println(i); //输出结果：203879,639172

            }

        }

    }

}


