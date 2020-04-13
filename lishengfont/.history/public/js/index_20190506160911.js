var myApp = angular.module('register', [])
myApp.controller('loginController', ['$scope', '$http', '$templateCache', function ($scope, $http, $templateCache) {
    var vm = $scope
    vm.loginUser="xxx"
    vm.password="密码"
    vm.pattern="/[/u0391-/uFFE5]/gi"
    vm.check_zh=function(){
        vm.loginUser=vm.loginUser.replace(/[\u4E00-\u9FA5]|[\uFE30-\uFFA0]/g,'');
    }
    vm.login = function () {
        requestdata = {
            name: vm.loginUser,
            pass: vm.password
        }
        $http({
            method: 'POST',
            url: 'http://localhost:8080/login',
            cache: $templateCache,
            data: requestdata
        }).then(function (response) {

            var status = response.data.status
            if (status == "505") {

                window.alert("用户名或密码错误")
            } else {

                window.location.href = "iframe.html";
            }

        }, function (response) {
            console.log(response)
        });
    }


}]);
