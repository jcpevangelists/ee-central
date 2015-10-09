describe("main page top header tests", function () {
    beforeEach(module('tribe-controllers-home'));

    it("should scroll three times slower than the whole page", function () {
        var cssValue;
        var spy = spyOn(angular, 'element').and.callFake(function() {
            spy.and.callThrough(); // disabling spy otherwise angular gets nuts.
            return {
                scroll: function (callback) {
                    callback();
                },
                scrollTop: function () {
                    return 3;
                }
            };
        });
        inject(function ($controller) {
            $controller('HeaderImageController', {
                $element: {
                    css: function (value) {
                        cssValue = value;
                    }
                },
                $window: {}
            });
        });
        expect(JSON.stringify(cssValue)).toBe(JSON.stringify({
            'transform': 'translateY(1px)'
        }));
    });
});