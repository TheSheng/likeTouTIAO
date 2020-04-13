var myApp = angular.module('recommand', ['ngCookies'])

myApp.controller('indexController', ['$scope', '$http', '$templateCache', '$cookies', '$cookieStore', '$compile', function ($scope, $http, $templateCache, $cookies, $cookieStore, $compile) {
    var vm = $scope
    var cookie = $cookieStore


    vm.local = ""
    $(".list-group-item").click(function () {

        $(".active").removeClass("active")
        $(this).addClass("active")
        showMessage('看来你对' + $(this).text() + '更感兴趣');


    })
    now = new Date(), hour = now.getHours()
    vm.userId = cookie.get('user')
    vm.opennew = function (url) {
        window.open(url, "_blank")
    }
    if (null == vm.userId) {

        swal("OMG!", "请先登录", "error")
        setTimeout(login, 1000)
    }

    function login() {
        window.location.href = 'login'
    }

    if (hour < 6) {
        vm.local = "凌晨好！"
    } else if (hour < 9) {
        vm.local = "早上好！"
    } else if (hour < 12) {
        vm.local = "上午好！"
    } else if (hour < 14) {
        vm.local = "中午好！"
    } else if (hour < 17) {
        vm.local = "下午好！"
    } else if (hour < 19) {
        vm.local = "傍晚好！"
    } else if (hour < 22) {
        vm.local = "晚上好！"
    } else {
        vm.local = "夜里好！"
    }

    function loginUser() {
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
            showMessage('呀！加载失败了，正在刷新');
            loginUser()
        });
    }

    loginUser()


    vm.saveArticle = function (data) {


        $cookieStore.put("article", data.id)
        window.location.href = "/article"
    }
    vm.type = "yule"
    vm.page = 0;
    vm.size = 4;
    vm.newslist = new Array()
    vm.newsUrl = 'http://129.211.63.167:8080/services/news/getNewsByType'
    vm.editTag = function (type) {
        vm.isSearch=false
        vm.type = type
        vm.newsUrl = 'http://129.211.63.167:8080/services/news/getNewsByType'
        vm.page = 0
        vm.newslist = new Array()
        if (vm.type == 'zonghe') {
            vm.newsUrl = 'http://129.211.63.167:8080/services/news/getDay'
            vm.page = 1
            vm.getnews()
            return
        } else if (vm.type == 'tuijian') {
            vm.newsUrl = 'http://129.211.63.167:8080/services/news/getUserLike'
            vm.getLikeNews()
            return
        }


        vm.getnews()
    }

    vm.getLikeNews = function () {

        $http({
            method: 'POST',
            url: vm.newsUrl,
            cache: $templateCache,
            data: {
                "page": vm.page,
                "size": vm.size,
                "username": vm.user.userName
            },
            withCredentials: true,


        }).then(function (response) {
            // console.log(response.data)

            vm.page = vm.page + 1
            var newarray = response.data.content


            if (response.data.last == true) {
                swal({
                    title: "亲!",
                    text: '休息一会吧！已经到底了',
                    imageUrl: "image/shuaxin.gif",
                    html: true,
                    timer: 1000,
                    showConfirmButton: false
                });
            } else {

                newarray.forEach(x =>
                    vm.newslist.push(x)
                )

                vm.newslist = vm.newslist.filter(x => x.article != "")
                vm.newslist = unique(vm.newslist)

                if (vm.newslist.length < 4) {
                    vm.getLikeNews();
                    return
                }

            }


        }, function (response) {
            showMessage('呀！加载失败了，请刷新');
            vm.newslist = new Array();
            vm.getLikeNews()

            scrollTo(0, 0);
        });
    }

    function getDay() {
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/getDay',
            cache: $templateCache,
            data: {
                "page": vm.dayIndex + 1,
                "size": 2,
                "type": vm.type
            },
            withCredentials: true,


        }).then(function (response) {
            // console.log(response.data)

            vm.dayIndex = vm.dayIndex + 1
            var dayarray = response.data.content
            if (response.data.last == true) {
                swal({
                    title: "亲!",
                    text: '休息一会吧！已经到底了',
                    imageUrl: "image/shuaxin.gif",
                    html: true,
                    timer: 1000,
                    showConfirmButton: false
                });
            }
            dayarray.forEach(x => vm.dayList.push(x))
            vm.dayList = unique(vm.dayList)
            vm.dayList = vm.dayList.filter(x => x.article != "")


            if (vm.dayList.length < 2) {
                getday();
                return
            }

        }, function (response) {

            showMessage('呀！加载失败了');

            getDay()
        });
    }

    vm.fu = function () {
        vm.dayList = new Array()

        getDay()

    }
    vm.isSearch=false
    vm.search=""
    //在弹出错误框过程中可以使用回车关闭框以及无限制回车
    vm.inKeyTime=0
    $("body").keydown(function() {
        if(vm.inKeyTime==0) {
            if (event.keyCode == "13") {//keyCode=13是回车键

                vm.find()
            }
        }else{
            showMessage("抱歉,每五秒只能查找一次")
        }

    });
    function descTime(){
        if(vm.inKeyTime>0) {
            vm.inKeyTime = vm.inKeyTime - 1
            setTimeout(descTime,1000)
        }
    }
    vm.getHeight=function(){
        return $(window).height()
    }
    console.log(vm.getHeight())
    vm.find=function(){

        if(vm.search!=""){
            vm.inKeyTime=5
            $(".active").removeClass("active")
            vm.type="search"
            vm.isSearch=true
            $("#normals").css("display","none")
            $("#loading").css("display","inline")
            $http({
                method: 'GET',
                url: 'http://129.211.70.182:9999/search/'+vm.search,
            }).then(function (response) {
                // console.log(response.data)
                $("#loading").css("display","none")
                $("#normals").css("display","inline")
                if(response.data.length<1){
                    swal("抱歉","暂无您想看的信息","error")

                }else{
                    vm.newslist=response.data
                }

                 setTimeout(descTime,1000)

            }, function (response) {
                $("#loading").css("display","none")
                $("#normals").css("display","inline")
                showMessage('呀！加载失败了');


            });

        }
    }
    vm.getnews = function () {

        $http({
            method: 'POST',
            url: vm.newsUrl,
            cache: $templateCache,
            data: {
                "page": vm.page,
                "size": vm.size,
                "type": vm.type
            },
            withCredentials: true,


        }).then(function (response) {
            // console.log(response.data)

            vm.page = vm.page + 1
            var newarray = response.data.content


            if (response.data.last == true) {
                swal({
                    title: "亲!",
                    text: '休息一会吧！已经到底了',
                    imageUrl: "image/shuaxin.gif",
                    html: true,
                    timer: 1000,
                    showConfirmButton: false
                });
            }

            newarray.forEach(x =>
                vm.newslist.push(x)
            )
            vm.newslist = vm.newslist.filter(x => x.article != "")
            vm.newslist = unique(vm.newslist)
            if (vm.newslist.length < 4) {
                vm.getnews();
                return
            }


        }, function (response) {
            showMessage('呀！加载失败了，请刷新');
            vm.newslist = new Array();

            vm.getnews()

            scrollTo(0, 0);
        });
    }
    vm.dayIndex = 0
    vm.dayList = new Array()

    function getday() {
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/getDay',
            cache: $templateCache,
            data: {
                "page": vm.dayIndex,
                "size": vm.size,
                "type": vm.type
            },
            withCredentials: true,


        }).then(function (response) {
            // console.log(response.data)

            vm.dayIndex = vm.dayIndex + 1
            var dayarray = response.data.content
            if (response.data.last == true) {
                swal({
                    title: "亲!",
                    text: '休息一会吧！已经到底了',
                    imageUrl: "image/shuaxin.gif",
                    html: true,
                    timer: 1000,
                    showConfirmButton: false
                });
            }
            dayarray.forEach(x => vm.dayList.push(x))
            vm.dayList = unique(vm.dayList)
            vm.dayList = vm.dayList.filter(x => x.article != "")


        }, function (response) {

            showMessage('呀！加载失败了，请刷新');
            getday()
        });
    }

    getDay()
    vm.getnews()
    vm.statu = true
    vm.more = function () {
        if (vm.statu == true) {
            vm.statu = false
            if (vm.type == "tuijian") {
                vm.getLikeNews()

            } else {
                vm.getnews()
            }


            vm.statu = true
            return
        }
        showMessage('呀！慢点刷新');


    }
    $(window).scroll(function () {
        if (vm.statu == true) {
            if(vm.type=="search"){
                return
            }
            vm.statu = false
            var scrollTop = $(this).scrollTop();
            var scrollHeight = $(document).height();
            var windowHeight = $(this).height();
            if (scrollTop + windowHeight == scrollHeight) {
                if (vm.type == "tuijian") {
                    vm.getLikeNews()

                } else {
                    vm.getnews()
                }
            }
            vm.statu = true
        } else {
            showMessage('呀！慢点刷新');
        }

    });

    function unique(arr) {
        var newarr = new Array()
        arr.forEach(old => {
            var statu = true
            var statu = true
            newarr.forEach(news => {
                if (statu) {
                    if (news.url == old.url) {
                        statu = false
                    }
                }

            })
            if (statu) {
                newarr.push(old)
            }

        })
        return newarr
    }
}]);
