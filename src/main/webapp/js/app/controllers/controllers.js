var photoplatformControllers = angular.module('photoplatformControllers', []);

photoplatformControllers.controller('LoginCtrl', ['$scope', '$rootScope', '$location', '$http', '$cookieStore', 'UserService', '$route',
function($scope, $rootScope, $location, $http, $cookieStore, UserService, $route) {

	$rootScope.login = function(username, password) {
		UserService.login(username, password).success(function(user) {
			$rootScope.user = user;
			$http.defaults.headers.common[xAuthTokenHeaderName] = user.secToken;
			$cookieStore.put('user', user);
			$location.path("/profile");
		}).error(function(error) {
		});
	};
}]);

photoplatformControllers.controller('RegisterCtrl', ['$scope', '$rootScope', '$location', '$http', '$cookieStore', 'UserService', '$route',
function($scope, $rootScope, $location, $http, $cookieStore, UserService, $route) {

	$rootScope.register = function(username, password, email) {
		var user = {'username' : username, 'password' : password, 'email' : email};
		UserService.register(user).success(function() {
			$location.path("/login");
			$rootScope.success = "Du hast dich erfolgreich registriert. Du kannst dich nun mit deinem Benutzernamen anmelden!";
			$rootScope.transferSuccess = true;
		}).error(function(error) {
		});
	};
}]);
