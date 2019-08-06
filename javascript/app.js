requirejs.config({
    baseUrl: '.',
    paths: {
        js: 'js'
    }
});
console.log('app, called.');
requirejs(['js/test']);
