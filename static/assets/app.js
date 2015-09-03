var app = angular.module('tribeio', ['ngRoute']);
app.config(['$routeProvider', '$locationProvider', function ($routeProvider, $locationProvider) {
    $routeProvider.when('/', {
        templateUrl: 'app/main.html'
    }).when('/projects', {
        templateUrl: 'app/projects.html'
    }).otherwise({
        redirectTo: '/'
    });
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
}]);
app.controller('MainController', ['$element', function ($element) {
    $element.bind('click', function () {
        $('html, body').animate({scrollTop: $('div.tribe-projects').offset().top}, 'slow');
    });
}]);
app.controller('TwitterController', ['$element', function ($element) {

}]);

