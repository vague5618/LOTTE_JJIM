var express = require('express');
var path = require('path');
var favicon = require('serve-favicon');
var logger = require('morgan');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');

// routes

var routes = require('./routes/index');
var Member = require('./routes/Member');
var Store = require('./routes/Store');
var Stock = require('./routes/Stock');
var Favorite =  require('./routes/Favorite');
var Push =  require('./routes/Push');
var Wait =  require('./routes/Wait');
var Product =  require('./routes/Product');
var Ordering =  require('./routes/Ordering');
var OrderDetail =  require('./routes/OrderDetail');



/////////////////////////////////////////////

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

// uncomment after placing your favicon in /public
//app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
app.use(logger('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));


// routes

app.use('/', routes);
app.use('/Member',Member);
app.use('/Store', Store);
app.use('/Stock', Stock);
app.use('/Favorite', Favorite);
app.use('/Wait', Wait);
app.use('/Push', Push);
app.use('/Product', Product);
app.use('/Ordering', Ordering);
app.use('/OrderDetail', OrderDetail);

///////////////////////////////




// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  next(err);
});

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
