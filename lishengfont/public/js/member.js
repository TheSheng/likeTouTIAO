var myApp = angular.module('member', ['ngCookies'])

myApp.controller('memberController', ['$scope', '$http', '$templateCache','$cookies','$cookieStore', function ($scope, $http, $templateCache,$cookies, $cookieStore) {
    var vm = $scope
    var cookie=$cookieStore
    vm.userId=cookie.get('user')
    if(null==vm.userId){

        swal("OMG!", "请先登录", "error")
        setTimeout(login,1000)
    }
    function login() {
        window.location.href='login'
    }
    vm.saveArticle=function(data){
        console.log("保存新闻")

        $cookieStore.put("article",data.id)
        window.location.href="/article"
    }
    vm.exit=function(){
        $cookieStore.remove("user")
        window.location.href="/login"
    }
    vm.getUser=function() {
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/user/loginUser',
            cache: $templateCache,
            data: {
                id: vm.userId

            },
            withCredentials: true,


        }).then(function (response) {
            // console.log(response.data)
            vm.user = response.data.body


        }, function (response) {
             vm.getUser()
        });
    }
    vm.getUser()
    vm.opennew=function(url){
        window.open(url,"_blank")
    }
    vm.gai=function(){
        window.location.href="/gai"
    }
    vm.size=4
    vm.page=0
    vm.type=""
    vm.suo=false
    vm.newslist=new Set()
    vm.getInfo=function (type) {
       if(!vm.suo){
           vm.suo=true
       }else{
           return
       }
        if(vm.type!=type){
            vm.page=0
            vm.newslist=new Set()
            vm.rs=new Array()
            vm.type=type
        }
        var url='http://129.211.63.167:8080/services/news/getUserAction/'+vm.userId+'/'+type
        if(type=='liulan'){
           url= 'http://129.211.63.167:8080/services/news/getUserAction/'+vm.user.userName+'/'+type
        }
        $http({
            method: 'POST',
            url: url,
            cache: $templateCache,
            data: {
                size:vm.size,
                page:vm.page,
                type:vm.type
            }
        }).then(function (response) {
            vm.page=vm.page+1
            response.data.forEach(x=>{
                vm.newslist.add(x)
            })
            vm.rs=Array.from(vm.newslist)
            vm.rs=unique(vm.rs)
            vm.suo=false
        }, function (response) {
            vm.suo=false

             vm.getInfo(type)
        });

    }
    vm.getInfo("zan")

    $(window).scroll(function(){

            var scrollTop = $(this).scrollTop();
            var scrollHeight = $(document).height();
            var windowHeight = $(this).height();
            if (scrollTop + windowHeight == scrollHeight) {
                vm.getInfo(vm.type)
            }



    });
    function unique(arr) {
        var newarr=new Array()
        arr.forEach(old=>{
            var statu=true
            var statu=true
            newarr.forEach(news=>{
                if(statu) {
                    if (news.url == old.url) {
                        statu = false
                    }
                }

            })
            if(statu){
                newarr.push(old)
            }

        })
        return newarr
    }

}]);
