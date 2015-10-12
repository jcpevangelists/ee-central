describe('testing code highlighting', function () {
    var $compile;
    var $rootScope;
    var $sce;
    var $timeout;

    beforeEach(module('tribe-project-details'));

    beforeEach(inject(function (_$compile_, _$rootScope_, _$sce_, _$timeout_) {
        $compile = _$compile_;
        $rootScope = _$rootScope_;
        $sce = _$sce_;
        $timeout = _$timeout_;
    }));

    afterEach(function () {
        if (hljs) {
            delete hljs;
        }
    });

    it('should call hljs for the highlight elements', function () {
        var elements = [];
        hljs = {
            highlightBlock: function (el) {
                elements.push(el);
            }
        };
        var element = $compile('<div prism ng-bind-html="project.documentation"></div>')($rootScope);
        var doc = $sce.trustAsHtml([
            '	<div class="listingblock">',
            '		<div class="content">',
            '			<pre class="highlight">',
            '				<code class="language-java">',
            'package org.test;',
            'import org.test.one;',
            'public class TestClass {',
            '    public void testMe() {',
            '        // testing code 1',
            '    }',
            '}',
            '				</code>',
            '			</pre>',
            '		</div>',
            '	</div>',
            '	<div class="listingblock">',
            '		<div class="content">',
            '			<pre class="highlight">',
            '				<code class="language-java">',
            'package org.test;',
            'import org.test.one;',
            'public class TestClass {',
            '    public void testMe() {',
            '        // testing code 2',
            '    }',
            '}',
            '				</code>',
            '			</pre>',
            '		</div>',
            '	</div>'
        ].join(''));
        $rootScope.project = {
            documentation: doc
        };
        $rootScope.$digest();
        $timeout.flush();
        expect(elements.length).toBe(2);
        expect(angular.element(elements[0]).html()).toContain('// testing code 1');
        expect(angular.element(elements[1]).html()).toContain('// testing code 2');
    });
});