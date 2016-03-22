describe("main page top header tests", function () {
    beforeEach(module('javaee-controllers-home'));

    beforeEach(function () {
        var spy = spyOn(angular, 'element').and.callFake(function () {
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
    });

    it("should scroll three times slower than the whole page", function () {
        var cssValue;
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

describe("highlighted projects tests", function () {
    beforeEach(module('javaee-controllers-home'));

    it("should load data", function () {
        var data = {
            projects: {
                'crest': {
                    name: 'crest',
                    longDescription: 'crest longDescription',
                    snapshot: 'snapshot',
                    icon: 'icon'
                }
            }
        };
        var scope = {};
        inject(function ($controller) {
            $controller('ProjectHighlightController', {
                $scope: scope,
                $sce: {
                    trustAsHtml: function (value) {
                        return 'trusted [' + value + ']'
                    }
                },
                javaeeAppService: {
                    whenReady: function (callback) {
                        callback(data);
                    }
                }
            });
        });
        expect(JSON.stringify(scope)).toBe(
            JSON.stringify({
                "highligtedProject": {
                    "name": "crest",
                    "description": "trusted [crest longDescription]",
                    "snapshot": "snapshot",
                    "icon": "icon"
                }
            })
        );
    });
});

describe("projects carousel tests", function () {
    beforeEach(module('javaee-controllers-home'));

    it("should load data", function () {
        var data = {
            projects: {
                'crest': {
                    name: 'crest',
                    longDescription: 'crest longDescription',
                    snapshot: 'snapshot',
                    icon: 'icon'
                }
            }
        };
        var scope = {};
        inject(function ($controller) {
            $controller('ProjectsCarousselController', {
                $scope: scope,
                javaeeAppService: {
                    whenReady: function (callback) {
                        callback(data);
                    }
                }
            });
        });
        expect(scope.projects.length).toBe(1);
        expect(scope.projects[0]).toBe(data.projects['crest']);
    });
});