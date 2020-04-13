var myApp = angular.module('article', ['ngCookies'])
myApp.filter('trustHtml', function ($sce) {
    return function (input) {
        return $sce.trustAsHtml(input);
    }
});

myApp.controller('articleController', ['$scope', '$http', '$templateCache', '$cookies', '$cookieStore', function ($scope, $http, $templateCache, $cookies, $cookieStore) {
    var ls = $scope
    var cookie = $cookieStore

    function getloginUser() {
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/user/loginUser',
            cache: $templateCache,
            data: {
                id: ls.userId

            },
            withCredentials: true,


        }).then(function (response) {
            // console.log(response.data)
            ls.user = response.data.body
            getArticle()


        }, function (response) {
            getloginUser()
        });
    }

    ls.index = 1
    ls.video = function (str) {
        str = str.replace(/\\/g, "")
        str = unescape(str.replace(/\u/g, "%u"))
        str = str.replace(/%/g, "")
        //    console.log(str)
        var rs1 = str.match(/\(.*\)/g)
        if (null != rs1) {
            if (ls.index == 1 && rs1.length > 0) {
                ls.index = ls.index + 1
                var videoJson = JSON.parse(rs1[0].substring(1, rs1[0].length - 1))
                $("#video").append("\t\t\t\t\t\t<iframe  style=\"width: 780px;height: 500px\"src=\"http://v.qq.com/txp/iframe/player.html?vid=" + videoJson['vid'] + "&showstart=0\"></iframe>\n")
            }
        }
        var rs2 = str.match(/IMGDATA/g)
        if (rs2.length > 0) {
            $("#word-display").remove()
            var json = str.match(/({.*})/)
            //console.log(json[1])
            $("#imgJi").append("<link rel=\"stylesheet\" href=\"//mat1.gtimg.com/pingjs/ext2020/dcom-static/build/static/css/static.css\" />\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/configF2017/5a978a31.js\" charset=\"utf-8\"></script>\n" +
                "<div id=\"TopNav\" style=\"display: none\"></div>\n" +
                "\n" +
                "<div class=\"content clearfix\">\n" +
                "\n" +
                "    <div class=\"content-article\" style='float: none'>\n" +
                "        <script>\n" +
                "            window.DATA = {\n" +
                "            }\n" +
                "        </script>\n" +
                "        <div class=\"PictureWrap\"><div data-reactroot=\"\" class=\"photoWrap\" tabindex=\"-1\">c</div></div>\n" +
                "\n" +
                "        <script>IMGDATA = [" + json + "]</script>\n" +
                "        <div id=\"Status\"></div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<script src=\"//mat1.gtimg.com/pingjs/ext2020/dcom-static/build/static/js/static.js\"></script>")
        }

        // console.log(window.DATA)

        return str

    }

    function unique(arr) {
        var newarr = new Array()
        arr.forEach(old => {
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

    ls.zhuanma = function (str) {
        str = str.replace(/\\/g, "")
        // $(".LEFT").append(str)
        return unescape(str.replace(/\u/g, "%u"))
    }
    ls.userId = cookie.get('user')
    getloginUser()
    ls.newsId = cookie.get('article')

    function getArticle() {
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/getById',
            cache: $templateCache,
            data: {
                id: ls.newsId

            },
            withCredentials: true,


        }).then(function (response) {
            // console.log(response.data)
            ls.news = response.data
            getLike()
            userLiulan('liulan')
            ls.video(ls.news.article)


        }, function (response) {
            getArticle()
        });
    }


    if (null == ls.userId) {

        swal("OMG!", "请先登录", "error")
        setTimeout(login, 3000)
    }

    function login() {
        window.location.href = 'login'
    }


    function filterNum(num) {
        if (num < 10) {
            return "0" + num;
        } else {
            return num;
        }
    }

    ls.save = function (id) {
        console.log("保存新闻")

        $cookieStore.put("article", id)
        window.location.href = "/article"
    }

    function getNowDateFormat() {
        var nowDate = new Date();
        var year = nowDate.getFullYear();
        var month = filterNum(nowDate.getMonth() + 1);
        var day = filterNum(nowDate.getDate());
        var hours = filterNum(nowDate.getHours());
        var min = filterNum(nowDate.getMinutes());
        var seconds = filterNum(nowDate.getSeconds());
        return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + seconds;
    }

    ls.city = returnCitySN.cname;
    ls.huifu = function () {
        console.log("回复")
        console.log($(".send").parent().find(".mytextarea").text())
    }

    $(".btn-info").click(function () {
        var replay = {
            id: ls.id,
            articleId: ls.newsId,
            userId: ls.userId,
            img: ls.user.imgUrl,
            replyName: ls.user.userName,
            beReplyName: null,
            content: $(".btn-info").prev().val(),
            time: getNowDateFormat(),
            address: ls.city,
            osname: getOS(),
            browse: Browse(),
            replyBody: new Array()

        }

        pinglun(replay)


    })

    function pinglun(replay) {
        console.log("提交评论")
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/pinglun',
            cache: $templateCache,
            data: replay,
            withCredentials: true,


        }).then(function (response) {

            userLiulan('pinglun')
            // $(".comment-list").empty()
            // getPL()


        }, function (response) {
            pinglun(replay)
        });
    }

    $(".btn-info").click(function () {
        console.log($(this).prev().val())
    })

    ls.zanfun = function (type) {

        if (type == 'zan') {
            console.log('zan这里')
            if (ls.isCai == true) {
                zanAPI('quxiaocai')
                zanAPI('zan')
                ls.cai = ls.cai - 1
                ls.zan = ls.zan + 1
                ls.isZan = true
                ls.isCai = false
                return
            } else {
                zanAPI('zan')
                ls.zan = ls.zan + 1
                ls.isZan = true
                return
            }
        } else if (type == 'cai') {
            console.log('踩这里')
            if (ls.isZan == true) {
                zanAPI('quxiaozan')
                zanAPI('cai')
                ls.cai = ls.cai + 1
                ls.zan = ls.zan - 1
                ls.isZan = false
                ls.isCai = true
                return
            } else {
                zanAPI('cai')
                ls.cai = ls.cai + 1
                ls.isCai = true
                return
            }

        } else if (type == 'quxiaozan') {
            console.log('取消赞')
            zanAPI(type)
            ls.zan = ls.zan - 1
            ls.isZan = false
            return

        } else if (type == 'quxiaocai') {
            zanAPI(type)
            ls.cai = ls.cai - 1
            ls.isCai = false
            return
        } else if (type == 'shoucang') {
            console.log('收藏')
            zanAPI(type)
            ls.shoucang = ls.shoucang + 1
            ls.isShoucang = true
            return
        } else if (type == 'quxiaoshoucang') {
            console.log('取消收藏')
            zanAPI(type)
            ls.shoucang = ls.shoucang - 1
            ls.isShoucang = false
            return
        }


    }

    function zanAPI(type) {
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/zan',
            cache: $templateCache,
            data: {
                type: type,
                userId: ls.userId,
                articleId: ls.newsId
            },
            withCredentials: true,


        }).then(function (response) {

            userLiulan(type)


        }, function (response) {

            // zanAPI(type)
        });
    }

    function userForNews() {
        $http({
            method: 'GET',
            url: 'http://129.211.63.167:8080/services/news/userForNews/' + ls.newsId + '/' + ls.userId,
            cache: $templateCache,

            withCredentials: true,


        }).then(function (response) {
            ls.isZan = response.data.isZan
            ls.isShoucang = response.data.isShoucang
            ls.isCai = response.data.isCai


        }, function (response) {
            userForNews()
        });
    }

    $(".btn-info").click(function () {
        console.log($(this).prev().val())
    })


//初始化数据

    function getNewsInfo() {

        $http({
            method: 'GET',
            url: 'http://129.211.63.167:8080/services/news/getNewsInfo/' + ls.newsId,
            cache: $templateCache,

            withCredentials: true,


        }).then(function (response) {

            // console.log(response.data)
            ls.zan = response.data.zan
            ls.cai = response.data.cai
            ls.shoucang = response.data.shoucang


        }, function (response) {
            getNewsInfo()
        });
    }

    getNewsInfo()
    userForNews()

    function userLiulan(type) {
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/userNotes',
            cache: $templateCache,
            data: {
                userId: ls.user.userName,
                newsId: ls.newsId,
                tags: ls.news.tag,
                type: type

            },
            withCredentials: true,


        }).then(function (response) {
            if (type == "pinglun") {
                window.location.href = "/article"
            }


        }, function (response) {

        });

    }

    ls.likeList = new Array()
    ls.statu = true

    function getLike() {
        $http({
            method: 'GET',
            url: 'http://129.211.63.167:8080/services/news/getNewsLike/' + ls.newsId,
            cache: $templateCache,

            withCredentials: true,


        }).then(function (response) {


            ls.likeList = response.data
            ls.likeList = ls.likeList.filter(x => {

                return x.id != ls.newsId
            })
            ls.likeList = unique(ls.likeList)
            if (ls.likeList.length < 1) {
                ls.statu = false
            }


        }, function (response) {
            getLike()
        });
    }


    ls.arr = []

    function getPL() {
        $http({
            method: 'GET',
            url: 'http://129.211.63.167:8080/services/news/articlePL/' + ls.newsId,
            cache: $templateCache,

            withCredentials: true,


        }).then(function (response) {

            response.data.forEach(x => {
                ls.arr.push(JSON.parse(x))
            })
            ls.id = ls.arr.length + 1;

            $(".comment-list").addCommentList({data: ls.arr, add: ""});


        }, function (response) {
            getPL()
        });
    }

    getPL()

    $(function () {

        $("#comment").click(function () {
            var obj = new Object();
            obj.img = ls.user.imgUrl;
            obj.replyName = ls.user.userName;
            obj.content = $("#content").val();
            obj.browse = ls.city;
            obj.osname = getOS();
            obj.browse = Browse();
            obj.replyBody = "";
            $(".comment-list").addCommentList({data: [], add: obj});
        });
    })

    function getOS() {
        var sUserAgent = navigator.userAgent;
        var isWin = (navigator.platform == "Win32") || (navigator.platform == "Windows");
        var isMac = (navigator.platform == "Mac68K") || (navigator.platform == "MacPPC") || (navigator.platform == "Macintosh") || (navigator.platform == "MacIntel");
        if (isMac) return "Mac";
        var isUnix = (navigator.platform == "X11") && !isWin && !isMac;
        if (isUnix) return "Unix";
        var isLinux = (String(navigator.platform).indexOf("Linux") > -1);
        if (isLinux) return "Linux";
        if (isWin) {
            var isWin2K = sUserAgent.indexOf("Windows NT 5.0") > -1 || sUserAgent.indexOf("Windows 2000") > -1;
            if (isWin2K) return "Win2000";
            var isWinXP = sUserAgent.indexOf("Windows NT 5.1") > -1 || sUserAgent.indexOf("Windows XP") > -1;
            if (isWinXP) return "WinXP";
            var isWin2003 = sUserAgent.indexOf("Windows NT 5.2") > -1 || sUserAgent.indexOf("Windows 2003") > -1;
            if (isWin2003) return "Win2003";
            var isWinVista = sUserAgent.indexOf("Windows NT 6.0") > -1 || sUserAgent.indexOf("Windows Vista") > -1;
            if (isWinVista) return "WinVista";
            var isWin7 = sUserAgent.indexOf("Windows NT 6.1") > -1 || sUserAgent.indexOf("Windows 7") > -1;
            if (isWin7) return "Win7";
            var isWin10 = sUserAgent.indexOf("Windows NT 10") > -1 || sUserAgent.indexOf("Windows 10") > -1;
            if (isWin10) return "Win10";
        }
        return "other";
    }

    function Browse() {
        var browser = {};
        var userAgent = navigator.userAgent.toLowerCase();
        var s;
        (s = userAgent.match(/msie ([\d.]+)/)) ? browser.ie = s[1] : (s = userAgent.match(/firefox\/([\d.]+)/)) ? browser.firefox = s[1] : (s = userAgent.match(/chrome\/([\d.]+)/)) ? browser.chrome = s[1] : (s = userAgent.match(/opera.([\d.]+)/)) ? browser.opera = s[1] : (s = userAgent.match(/version\/([\d.]+).*safari/)) ? browser.safari = s[1] : 0;
        var version = "";
        if (browser.ie) {
            version = 'IE ' + browser.ie;
        } else {
            if (browser.firefox) {
                version = 'firefox ' + browser.firefox;
            } else {
                if (browser.chrome) {
                    version = 'chrome ' + browser.chrome;
                } else {
                    if (browser.opera) {
                        version = 'opera ' + browser.opera;
                    } else {
                        if (browser.safari) {
                            version = 'safari ' + browser.safari;
                        } else {
                            version = '未知浏览器';
                        }
                    }
                }
            }
        }
        return version;
    }


}]);
