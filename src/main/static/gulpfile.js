var applicationContext = 'ROOT';

var gulp = require('gulp');
var jade = require('gulp-jade');
var sass = require('gulp-sass');
var autoprefixer = require('gulp-autoprefixer');
var uglify = require('gulp-uglify');
var sourcemaps = require('gulp-sourcemaps');
var concat = require('gulp-concat');
var del = require('del');
var spritesmith = require('gulp.spritesmith');
var gulpif = require('gulp-if');
var gulpsync = require('gulp-sync')(gulp);
var watch = require('gulp-watch');
var bower = require('gulp-bower');
var jslint = require('gulp-jslint');
var KarmaServer = require('karma').Server;
var es = require('event-stream');

gulp.task('jade', function () {
    return gulp.src('./assets/**/*.jade')
        .pipe(jade({
            locals: {}
        }))
        .pipe(gulp.dest('../../../target/static-resources/app/'))
});

gulp.task('css', gulpsync.sync(['css-build', 'css-third-party', 'css-third-party-resources']));
gulp.task('css-build', gulpsync.sync(['sass', 'autoprefixer', 'css-concat']));
gulp.task('css-third-party', function () {
    return gulp.src([
        './bower_components/lato/css/lato.css',
        './bower_components/normalize-css/normalize.css',
        './bower_components/font-awesome/css/font-awesome.css',
    ]).pipe(concat('source.css')).pipe(gulp.dest('../../../target/static-resources/app/third-party/style'));
});
gulp.task('css-third-party-resources', function () {
    return es.concat(
        gulp.src('./bower_components/font-awesome/fonts/*').pipe(gulp.dest('../../../target/static-resources/app/third-party/fonts')),
        gulp.src('./bower_components/lato/font/**/*').pipe(gulp.dest('../../../target/static-resources/app/third-party/font'))
    );
});
gulp.task('sass', function () {
    return gulp.src('./assets/**/*.sass')
        .pipe(sass({outputStyle: 'compressed'}).on('error', sass.logError))
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});
gulp.task('autoprefixer', function () {
    return gulp.src('../../../target/static-resources/app/style/main.css')
        .pipe(autoprefixer({}))
        .pipe(gulp.dest('../../../target/static-resources/app/style/'));
});
gulp.task('css-concat', function () {
    return gulp.src(['../../../target/static-resources/app/style/sprite.css', '../../../target/static-resources/app/style/main.css'])
        .pipe(concat('app.css'))
        .pipe(gulp.dest('../../../target/static-resources/app/style/'))
});
gulp.task('images', gulpsync.sync(['copy-images', 'sprites']));
gulp.task('copy-images', function () {
    return gulp.src(['./assets/**/*.png', './assets/**/*.jpg'])
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});
gulp.task('sprites', function () {
    return gulp.src('./assets/**/sprite_*.{png,jpg}').pipe(spritesmith({
        imgName: '../images/sprite.png',
        cssName: 'sprite.css'
    })).pipe(gulpif('*.png',
        gulp.dest('../../../target/static-resources/app/images'),
        gulp.dest('../../../target/static-resources/app/style/'))
    );
});

gulp.task('bower', function () {
    return bower();
});

gulp.task('js', gulpsync.sync(['js-test', 'js-build', 'js-third-party']));
gulp.task('js-test', function (done) {
    new KarmaServer({
        configFile: __dirname + '/karma.conf.js',
        singleRun: true,
        autoWatch: false
    }, function () {
        done(); // avoiding karma to shutdown watch on test failures.
    }).start();
});
gulp.task('js-build', gulpsync.sync(['lint', 'copy-js', 'uglify']));
gulp.task('lint', function () {
    return gulp.src('./assets/**/*.js').pipe(jslint({
            node: false,
            evil: false,
            nomen: true,
            vars: true,
            unparam: true,
            global: [],
            predef: ['angular', '_', 'window', '$', 'hljs'],
            edition: '2014-07-08',
            errorsOnly: true,
        }).on('error', function () {
            // no-op
        })
    );
});
gulp.task('js-third-party', function () {
    return gulp.src([
        './bower_components/underscore/underscore.js',
        './bower_components/jquery/dist/jquery.js',
        './bower_components/angular/angular.js',
        './bower_components/angular-route/angular-route.js'
    ]).pipe(concat('source.js')).pipe(gulp.dest('../../../target/static-resources/app/third-party/'));
});
gulp.task('copy-js', function () {
    return gulp.src('./assets/**/*.js')
        .pipe(gulp.dest('../../../target/static-resources/app/'));
});

gulp.task('uglify', function () {
    return gulp.src('./assets/**/*.js')
        .pipe(sourcemaps.init())
        .pipe(concat('app.min.js'))
        .pipe(uglify({
            mangle: false // otherwhise the sourcemap/debugger does not work properly.
        }))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('../../../target/static-resources/app/js/'));
});

gulp.task('clean', function () {
    return del.sync(['../../../target/static-resources/', '../../../target/apache-tomee/webapps/ROOT/app/'], {
        force: true
    });
});

gulp.task('copy-to-target', function () {
    return gulp.src('../../../target/static-resources/app/**/*')
        .pipe(gulp.dest('../../../target/apache-tomee/webapps/' + applicationContext + '/app/'));
});

gulp.task('build', gulpsync.sync(['clean', 'bower', 'jade', 'images', 'css', 'js']));
gulp.task('default', gulpsync.sync(['build', 'copy-to-target']), function () {
    gulp.watch(
        ['./assets/**/*', '../../test/**/*.js'],
        gulpsync.sync(['build', 'copy-to-target'])
    );
});
