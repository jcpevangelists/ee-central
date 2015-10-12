describe("main app module share directive", function () {
    var $compile;
    var $rootScope;
    var $window;

    beforeEach(module('tribe-app-directives'));

    beforeEach(inject(function (_$compile_, _$rootScope_, _$window_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_.$new();
        $window = _$window_;
    }));

    it('should be able to trigger share actions', function () {
        $rootScope.project = {
            name: 'my_project'
        };
        $rootScope.baseFullPath = '//test/';
        var simulateClick = function (socialNetwork) {
            var element = $compile('<i tribe-share-project="' + socialNetwork + '"></i>')($rootScope);
            $rootScope.$digest();
            element.triggerHandler('click');
        };
        spyOn($window, 'open');
        simulateClick('twitter');
        expect($window.open).toHaveBeenCalledWith(
            'https://twitter.com/intent/tweet?text=Check out http:' + $rootScope.baseFullPath + 'projects/my_project',
            'name', 'width=600,height=400'
        );
        simulateClick('facebook');
        expect($window.open).toHaveBeenCalledWith(
            'http://www.facebook.com/sharer/sharer.php?u=http:' + $rootScope.baseFullPath + 'projects/my_project',
            'name', 'width=600,height=400'
        );
        simulateClick('facebook-clone');
        expect($window.open).not.toHaveBeenCalled();
    });
});