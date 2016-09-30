/**
 * Created by LOTTE_n-pc on 2016-02-05.
 */
var oracledb = require('oracledb');
var dbConfig = require('./dbConfig.js');

exports.getDBConnection = function(callback) {

    oracledb.getConnection({
        user: dbConfig.user,
        password: dbConfig.password,
        connectString: dbConfig.connectString
    }, function (err, connection) {
        if (err) {
            console.error(err.message);
            return;
        }
        callback(connection);
    });
}

exports.disconnectDB = function(connection)
{
    connection.release(
        function(err) {
            if (err) {
                console.error(err.message);
            }
        });
}
