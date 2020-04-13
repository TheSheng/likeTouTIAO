var myApp = angular.module('tag', ['ngCookies'])

myApp.controller('tagController', ['$scope', '$http', '$templateCache','$cookies','$cookieStore', function ($scope, $http, $templateCache,$cookies, $cookieStore) {
    var vm = $scope
    var cookie=$cookieStore
    swal({
        title: "标签选择!",
        text: '单击添加标签，再次点击删除标签。选完后点击提交',
        imageUrl: "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1294713168,3221264383&fm=27&gp=0.jpg",
        html: true,
        timer: 3000,
        showConfirmButton: false
    });
    vm.userId=cookie.get('user')
    if(null==vm.userId){

        swal("OMG!", "请先登录", "error")
        setTimeout(login,1000)
    }
    function login() {
        window.location.href='login'
    }
    $http({
        method: 'POST',
        url: 'http://129.211.63.167:8080/services/user/loginUser',
        cache: $templateCache,
        data :{
            id:vm.userId

        },
        withCredentials:true,


    }).then(function (response) {
        // console.log(response.data)
        vm.user=response.data.body
       // console.log(vm.user)




    }, function (response) {
        showMessage('呀！加载失败了，请刷新');
    });

    vm.bno=Math.ceil(Math.random()*4);
    vm.cno=Math.ceil(Math.random()*7);
    vm.userchose=new Array();
    vm.editTag=function(tag){
        var index=vm.userchose.indexOf(tag)
        if(index==-1){
            vm.userchose.push(tag)
            var str=""
            vm.userchose.forEach(x=>str=str+x+',')
            str = str.substr(0, str.length - 1);
            swal({
                title: "添加成功!",
                text: '您以选择'+str,
                imageUrl: "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1294713168,3221264383&fm=27&gp=0.jpg",
                html: true,
                timer: 1000,
                showConfirmButton: false
            });
        }else{
            vm.userchose.splice(index,1)
            var str=""
            vm.userchose.forEach(x=>str=str+x+',')

            str = str.substr(0, str.length - 1);
            swal({
                title: "删除成功!",
                text: '您以选择'+str,
                imageUrl: "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1551053409,19869788&fm=26&gp=0.jpg",
                html: true,
                timer: 1000,
                showConfirmButton: false
            });

        }
    }

    $http({
        method: 'GET',
        url: 'http://129.211.63.167:8080/services/news/getTagTop',
        cache: $templateCache,
        withCredentials:true,


    }).then(function (response) {

        vm.tagList=response.data.body
       // console.log(vm.tagList)

    }, function (response) {

    });
    vm.commit=function () {
        if(vm.userchose.length<5){
            swal("OMG!", "最少得选择五个哦", "error");
            return
        }
        var data={
           tags:vm.userchose,
            username:vm.user.userName

        }

      //  console.log(data)
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/addMoreTag',
            cache: $templateCache,
            data :data,
            withCredentials:true,


        }).then(function (response) {
            userLiulan()



        }, function (response) {
            vm.commit()

        });
    }

    function  userLiulan(){
        $http({
            method: 'POST',
            url: 'http://129.211.63.167:8080/services/news/userNotes',

            data :{
                userId:vm.user.userName,
                newsId:"",
                tags:vm.userchose.join(";"),
                type:"register"

            },
            withCredentials:true,


        }).then(function (response) {
            window.location.href='/'



        }, function (response) {

        });

    }

}]);
