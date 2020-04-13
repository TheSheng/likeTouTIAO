var myApp = angular.module('gai', ['ngCookies'])

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

            vm.imgUrl=vm.user.imgUrl
            vm.username=vm.user.userName
            vm.password="请输入新密码"
        }, function (response) {
             vm.getUser()
        });
    }
    vm.getUser()
    function checkMobile(){

        var myreg=/^[1][3,4,5,7,8][0-9]{9}$/;
        if (!myreg.test(vm.username)) {
            swal("OMG!", "请填写正确的手机号", "error");
            return false;
        } else {
            return true;
        }
    }
    function checkPass(){

        var patrn=/^(\w){6,20}$/;
        if (!patrn.exec(vm.password)) {

            swal("OMG!", "只能输入6-20个字母、数字、下划线", "error");
            return false
        }
        return true
    }
    vm.gai = function () {

        if(checkMobile()){
            if(checkPass()){

                requestdata = {
                    userName: vm.username,
                    passWord: hex_md5(vm.password),
                    imgUrl:vm.imgUrl,
                    id:vm.user.id,
                }
                console.log(requestdata)
                $http({

                    method: 'POST',
                    url: 'http://129.211.63.167:8080/services/user/gai',


                    data: requestdata
                }).then(function (response) {
                    $cookieStore.remove("user")
                 if(response.data.message=="修改成功") {
                     swal("更改成功!", "请重新登录", "success")

                 }else {
                     swal("错误!", response.data.message, "error")
                 }

                   setTimeout(login,1000)
                }, function (response) {
                    console.log(response)
                });

            }

        }

        // checkPass()

    }



}]);
