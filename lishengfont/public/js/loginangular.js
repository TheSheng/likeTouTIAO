var myApp = angular.module('recommand', ['ngCookies'])
myApp.config(function($httpProvider){
    console.log($httpProvider.defaults.headers)
    //删除后请求头里不再有 X-Requested-With 属性
    delete $httpProvider.defaults.headers.common['content-type'];

    //为请求头添加Authorization属性为'code_bunny'
    $httpProvider.defaults.headers.common['Access-Control-Allow-Origin'] = '*';
});
myApp.controller('loginController', ['$scope', '$http', '$templateCache','$cookies','$cookieStore', function ($scope, $http, $templateCache,$cookies, $cookieStore) {
    var vm = $scope
    vm.username=""
    vm.password=""
    vm.imgUrl="images/login.jpg"
    vm.pattern="/[/u0391-/uFFE5]/gi"
    vm.check_zh=function(){
        vm.loginUser=vm.loginUser.replace(/[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/g,'');
    }
    function checkMobile(){

        var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
        if (!myreg.test(vm.username)) {
            swal("OMG!", "请填写正确的手机号", "error");
            return false;
        } else {
            return true;
        }
    }
    vm.saoma=function() {
        vm.maCount=0
        vm.maStatu=true
        $http({
            method: 'GET',
            url: 'http://129.211.63.167:8080/services/news/saveMa',

        }).then(function (response) {

              getMa()

        }, function (response) {

        });

    }
    vm.maCount=0
    vm.reset=function(){
        jQuery("#erweima").attr("src","images/erweima.jpg")
        vm.statu=false
        vm.maCount=0
        vm.saoma()

    }

    vm.input="请输入验证码"

    vm.code=""
    vm.userId=""
    vm.toIndex=function (){
        if(vm.code==''){
            swal("别急","还未收到短信验证码","error")
            return
        }
        if(vm.input!=vm.code){
            swal("错误","短信验证码错误","error")
            return
        }
        if(vm.userId==""){
            swal("稍等","尚未得到账号信息","error")
            return
        }
         vm.loginByDanxin()
    }
    vm.loginByDanxin=function(){
        $cookieStore.put("user",vm.userId)
        swal("ok","登录成功","success")
        setTimeout(function () {
            window.location.href = "/";
        },2000)
    }
    vm.sendDuanxin=function(){
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/sendDuanxin',
            data:{
                username:vm.username
            }

        }).then(function (response) {
            rs=response.data
            if(rs['entity']=="每天最多只能发三次哦"){
                swal("error","每天最多发三次","error")
                return
            }

            if(JSON.parse(rs['entity'])['ReturnStatus']!="Success"){
                swal("错误","系统错误，请联系管理员","error")
                return
            }

            vm.code=rs['message']





        }, function (response) {
            console.log(response.data)
        });
    }

    vm.huoqu=function(){
       if(checkMobile()){
           $http({
               method: 'GET',
               url: 'http://129.211.63.167:8080/services/user/duanxinyanzheng/'+vm.username,

           }).then(function (response) {
               var rs=response.data
                if(rs['body']=="不存在"){
                    swal("错误","该手机号尚未注册","error")
                    return
                }
               vm.userId=rs['body']
                console.log(vm.userId)

                vm.sendDuanxin()


           }, function (response) {
               console.log(response)
           });
           return
       }else{
           swal("错误","请输入正确的手机号","error")
       }

    }
   vm.changeMa=function(){

       vm.imgUrl='images/login.jpg'
        vm.maStatu=false
    }
  function toLogin() {
      window.location.href="/login"
  }
    function  getMa() {
        console.log("请求中")
        if(vm.maStatu==false){
            return
        }
        vm.maCount=vm.maCount+1
        if(vm.maCount>30){
            swal("错误","二维码已失效","error")
            jQuery("#erweima").attr("src","images/guoqi.jpg")
            return
        }

        $http({
            method: 'GET',
            url: 'http://129.211.63.167:8080/services/news/getMa',

        }).then(function (response) {
                var rs=response.data


            if(rs==false){
               setTimeout(getMa,1000)
                return
            }
            $cookieStore.put("user","43")
            swal("Good","成功登录！","success")
            setTimeout(function () {
                window.location.href = "/";
            },2000)


        }, function (response) {
            console.log(response)
        });
    }
    function checkPass(){

        var patrn=/^(\w){6,20}$/;
        if (!patrn.exec(vm.password)) {

            swal("OMG!", "只能输入6-20个字母、数字、下划线", "error");
            return false
        }
        return true
    }
    vm.login = function () {
        if(checkMobile()){
            if(checkPass()){

                requestdata = {
                    userName: vm.username,
                    passWord: hex_md5(vm.password)
                }
                console.log(requestdata)
                $http({
                    method: 'POST',
                    url: 'http://129.211.63.167:8080/services/user/login',
                    cache: $templateCache,
                    withCredentials:true,
                    data: requestdata
                }).then(function (response) {

                    var status = response.data.code
                    if (status == "500") {
                        swal("OMG!", response.data.message, "error");

                    } else {
                        $cookieStore.put("user",response.data.body.id)
                        swal("Good",response.data.message,"success")
                        setTimeout(function () {
                            window.location.href = "/";
                        },2000)

                    }

                }, function (response) {
                    console.log(response)
                });

            }

        }

        // checkPass()

    }

    vm.register = function () {
        if(checkMobile()){
            if(checkPass()){
                requestdata = {
                    userName: vm.username,
                    passWord: hex_md5(vm.password),
                    imgUrl:vm.imgUrl
                }
                console.log(requestdata)
                $http({
                    method: 'POST',
                    url: 'http://129.211.63.167:8080/services/user/register',
                    cache: $templateCache,

                    data: requestdata
                }).then(function (response) {

                    var status = response.data.code
                    if (status == "500") {
                        swal("OMG!", response.data.message, "error");

                    } else {
                        console.log(response.data)
                        $cookieStore.put("user",response.data.body.id)
                        swal({
                            title: "Good!",
                            text: '注册成功，接下来让我们选择兴趣爱好',
                            imageUrl: "images/thumbs-up.jpg",
                            html: true,
                            timer: 3000,
                            showConfirmButton: false
                        });
                        setTimeout(function () {
                            window.location.href = "/tag";
                        },3000)

                    }

                }, function (response) {
                    console.log(response)
                });

            }

        }

        // checkPass()

    }


}]);
