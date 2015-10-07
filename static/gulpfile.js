var applicationContext = 'ROOT';

var gulp = require('gulp');
var jade = require('gulp-jade');
var sass = require('gulp-sass');
var autoprefixer = require('gulp-autoprefixer');
var uglify = require('gulp-uglify');
var sourcemaps = require('gulp-sourcemaps');
var concat = require('gulp-concat');
var del = require('del');
var sprity = require('sprity');
var gulpif = require('gulp-if');
var gulpsync = require('gulp-sync')(gulp);
var watch = require('gulp-watch');
var bower = require('gulp-bower');
var jslint = require('gulp-jslint');

gulp.task('jade', function () {
    return gulp.src('./assets/**/*.jade')
        .pipe(jade({
            locals: {}
        }))
        .pipe(gulp.dest('../src/main/webapp/app/'))
});

gulp.task('css', gulpsync.sync(['css-build', 'css-third-party', 'css-third-party-resources']));
gulp.task('css-build', gulpsync.sync(['sass', 'autoprefixer', 'css-concat']));
gulp.task('css-third-party', function () {
    return gulp.src([
        './bower_components/normalize-css/normalize.css',
        './bower_components/font-awesome/css/font-awesome.css',
        './bower_components/highlight/src/styles/default.css'
    ]).pipe(concat('third-party.css')).pipe(gulp.dest('../src/main/webapp/app/style/'));
});
gulp.task('css-third-party-resources', function () {
    return gulp.src('./bower_components/font-awesome/fonts/*').pipe(gulp.dest('../src/main/webapp/app/fonts'));
});

gulp.task('sass', function () {
    return gulp.src('./assets/**/*.sass')
        .pipe(sass({outputStyle: 'compressed'}).on('error', sass.logError))
        .pipe(gulp.dest('../src/main/webapp/app/'));
});

gulp.task('autoprefixer', function () {
    return gulp.src('../src/main/webapp/app/style/main.css')
        .pipe(autoprefixer({}))
        .pipe(gulp.dest('../src/main/webapp/app/style/'));
});

gulp.task('css-concat', function () {
    return gulp.src(['../src/main/webapp/app/style/sprite.css', '../src/main/webapp/app/style/main.css'])
        .pipe(concat('app.css'))
        .pipe(gulp.dest('../src/main/webapp/app/style/'))
});

gulp.task('images', gulpsync.sync(['copy-images', 'sprites']));

gulp.task('copy-images', function () {
    return gulp.src(['./assets/**/*.png', './assets/**/*.jpg'])
        .pipe(gulp.dest('../src/main/webapp/app/'));
});

gulp.task('sprites', function () {
    return sprity.src({
        src: './assets/**/sprite_*.{png,jpg}',
        style: '../sprite.css',
        margin: 1,
        prefix: 'tribe-icon'
    }).pipe(gulpif('*.png', gulp.dest('../src/main/webapp/app/images'), gulp.dest('../src/main/webapp/app/style/')));
});

gulp.task('bower', function () {
    return bower();
});

gulp.task('js', gulpsync.sync(['js-build', 'js-third-party']));
gulp.task('js-build', gulpsync.sync(['lint', 'copy-js', 'uglify']));
gulp.task('lint', function () {
    return gulp.src('./assets/**/*.js')
        .pipe(jslint({
            node: false,
            evil: false,
            nomen: true,
            vars: true,
            global: [],
            predef: ['angular', '_', 'window', '$', 'hljs'],
            reporter: 'default',
            edition: '2014-07-08',
            // specify whether or not
            // to show 'PASS' messages
            // for built-in reporter
            errorsOnly: false
        }));
});
gulp.task('js-third-party', function () {
    return gulp.src([
        './bower_components/underscore/underscore-min.js',
        './bower_components/highlight/src/highlight.js',
        './bower_components/jquery/dist/jquery.min.js',
        './bower_components/angular/angular.min.js',
        './bower_components/angular-route/angular-route.min.js'
    ]).pipe(concat('third-party.js')).pipe(gulp.dest('../src/main/webapp/app/js/'));
});
gulp.task('copy-js', function () {
    return gulp.src('./assets/**/*.js')
        .pipe(gulp.dest('../src/main/webapp/app/'));
});

gulp.task('uglify', function () {
    return gulp.src('./assets/**/*.js')
        .pipe(sourcemaps.init())
        .pipe(concat('app.min.js'))
        .pipe(uglify({
            mangle: false // otherwhise the sourcemap/debugger does not work properly.
        }))
        .pipe(sourcemaps.write('.'))
        .pipe(gulp.dest('../src/main/webapp/app/js/'));
});

gulp.task('clean', function () {
    return del.sync(['../src/main/webapp/app/', '..target/apache-tomee/webapps/ROOT/app/'], {
        force: true
    });
});

gulp.task('copy-to-target', function () {
    return gulp.src('../src/main/webapp/app/**/*')
        .pipe(gulp.dest('../target/apache-tomee/webapps/' + applicationContext + '/app/'));
});

gulp.task('build', gulpsync.sync(['clean', 'bower', 'jade', 'images', 'css', 'js', 'copy-to-target']));
gulp.task('default', ['build'], function () {
    gulp.watch('./assets/**/*', ['build']);
});
