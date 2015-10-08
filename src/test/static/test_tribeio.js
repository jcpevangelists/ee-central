describe("test suite: module: tribeio", function () {
    beforeEach(module('tribeio'));

    it("contains spec with an expectation", function () {
        var controller;
        var cssValue;
        var angularWindow = {};
        var jqueryWindow = {
            scroll: function (callback) {
                callback();
            },
            scrollTop: function () {
                return 3;
            }
        };
        spyOn(window, "$").and.callFake(function(el) {
            if(el === angularWindow) {
                return jqueryWindow;
            }
            throw 'unexpected el'
        });
        inject(function ($controller) {
            controller = $controller('HeaderImageController', {
                $element: {
                    css: function (value) {
                        cssValue = value;
                    }
                },
                $window: angularWindow
            });
        });
        expect(JSON.stringify(cssValue)).toBe(JSON.stringify({
            'transform': 'translateY(1px)'
        }));
    });
});