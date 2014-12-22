photoplatformControllers.controller('ImageCtrl',
    ['$scope', '$rootScope', '$location', '$http', '$cookieStore', '$route', 'UserService', 'ImageService','$modal',
        function ($scope, $rootScope, $location, $http, $cookieStore, $route, UserService, ImageService, $modal) {
            var user = $cookieStore.get('user');
            //if user is not logged in or logged in redirect to login page
            if (undefined == user || !$rootScope.isLoggedIn()) {
                $location.path("/login");
                return;
            }

            var start = 0;
            var count = 100;

            $scope.copy = angular.copy;
            $scope.images = [];

            $scope.uploadImage = function () {
                var param = $scope.files;
                $scope.modalInstance = $modal.open({
                    templateUrl: '/views/partials/profile/photographer/image/uploadIndicator.html',
                    windowClass: 'upload-dialog'
                });

                ImageService.upload(param, $rootScope.user)
                    .success(function (message) {
                        $scope.modalInstance.close();
                        $('#preview').hide();
                        $('#preview').html('');
                        $('#upload').prop('disabled', true);
                        $scope.getAllImages();
                        $rootScope.success = message;
                    })
                    .error(function (e) {
                        $scope.errors = e;
                    });
            };

            /**
             * Get all images.
             */
            $scope.getAllImages = function () {
                ImageService.getAllImages(start, count).success(function (images) {
                    $scope.images = images;
                }).error(function (data) {
                    $scope.errors = data.errors;
                })
            };

            /**
             * Delete one image by image id.
             */
            $scope.deleteImage = function (image, index) {
                ImageService.deleteImage(image.id).success(function (message) {
                    $scope.images.splice(index, 1);
                    $rootScope.success = message;
                }).error(function (data) {
                    $scope.log(data.errors);
                    $scope.errors = data.errors;
                })
            };

            /**
             * Update one image by image id.
             */
            $scope.updateImage = function (editImage, originImage, status) {
                //replace comma by dot.
                editImage.price = ("" + editImage.price).replace(",", ".");
                ImageService.updateImage(editImage).success(function (image) {
                    angular.extend(originImage, image);
                    status.editing = false;
                    $rootScope.success = image.messageSuccess;
                }).error(function (data) {
                    $scope.log(data.errors);
                    $scope.errors = data.errors;
                })
            };

        }])
;