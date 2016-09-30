/**
 * Created by LOTTE_n-pc on 2016-02-05.
 */
var db = require("../../util/dbUtil");
var StockDO = require('../DO/StockDO');
var TrueFalseDO = require('../DO/TrueFalseDO');

exports.DongStockCheck = function(productName, address1, address2, address3, callback)
{
    var Stock_arr = [];
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "SELECT PRODUCTNAME, STORENAME, ADDRESS, PRODUCTSTOCK, EVENTTEXT, PRICE, SALESPRICE, LATITUDE, LONGITUDE FROM STORE NATURAL JOIN STOCK WHERE" +
            " PRODUCTNAME = :PRODUCTNAME and ADDRESS1=:ADD1 and ADDRESS2=:ADD2 and ADDRESS3=:ADD3"
            ,[productName, address1, address2, address3], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.DongStockCheckDO(null,null,null,null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.DongStockCheckDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]
                            ,result.rows[i][5],result.rows[i][6],result.rows[i][7],result.rows[i][8]);

                        Stock_arr.push(StockInfo);
                    }

                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}


exports.NearStockRequest = function(productName, latitude, longitude, callback)
{
    var Stock_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "SELECT PRODUCTNAME, STORENAME, ADDRESS, PRODUCTSTOCK, EVENTTEXT, PRICE, SALESPRICE, LATITUDE, LONGITUDE FROM STORE NATURAL JOIN STOCK WHERE PRODUCTNAME = :PRODUCTNAME " +
            "and latitude > :latitude1 - 0.01 and latitude < :latitude2 + 0.01"+
            "and longitude > :longitude - 0.01 and longitude < :longitude + 0.01"
            ,[productName, latitude, latitude ,longitude, longitude], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.NearStockRequestDO(null,null,null,null,null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.NearStockRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]
                            ,result.rows[i][5],result.rows[i][6],result.rows[i][7],result.rows[i][8]);

                        Stock_arr.push(StockInfo);
                    }

                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}

//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\카테고리 시작
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\카테고리 시작
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\카테고리 시작

exports.EventStockRequest = function(address, storeName, callback)
{
    var Stock_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "SELECT PRODUCTNAME, EVENTTEXT, SALESPRICE, PRODUCTSTOCK,productID FROM STOCK WHERE STOREID = (SELECT STOREID FROM STORE WHERE ADDRESS = :Address AND STORENAME = :StoreName) AND NOT EVENTTEXT = '행사없음'"
            ,[address, storeName],

            function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.EventStockRequestDO(null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.EventStockRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]);
                        Stock_arr.push(StockInfo);
                    }
                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });

}

exports.DosirakStockRequest = function(address, storeName, category, callback)
{
    var Stock_arr = [];
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            //"select productname, eventtext, salesprice, productstock,productID from STOCK natural join PRODUCT where storeid=(select storeid from store where address = :Address and storename = :StoreName and cateid = :category)",

            "select productname , eventtext, salesprice, productstock, productid from stock where storeid = (select storeid from store where address = :address and storename = :storename) and productid like '1%'",
            [address, storeName], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.DosirakStockRequestDO(null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.DosirakStockRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]);
                        Stock_arr.push(StockInfo);
                    }
                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}

exports.SnackStockRequest = function(address, storeName, category, callback)
{
    var Stock_arr = [];
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "select productname , eventtext, salesprice, productstock, productid from stock where storeid = (select storeid from store where address = :address and storename = :storename) and productid like '2%'",
            [address, storeName], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.SnackStockRequestDO(null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.SnackStockRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]);
                        Stock_arr.push(StockInfo);
                    }

                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}


exports.BeverageStockRequest = function(address, storeName, category, callback)
{
    var Stock_arr = [];
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "select productname , eventtext, salesprice, productstock, productid from stock where storeid = (select storeid from store where address = :address and storename = :storename) and productid like '3%'",
            [address, storeName], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.BeverageStockRequestDO(null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.BeverageStockRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]);
                        Stock_arr.push(StockInfo);
                    }

                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}


exports.CandyStockRequest = function(address, storeName, category, callback)
{
    var Stock_arr = [];
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "select productname , eventtext, salesprice, productstock, productid from stock where storeid = (select storeid from store where address = :address and storename = :storename) and productid like '4%'",
            [address, storeName], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.CandyStockRequestDO(null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.CandyStockRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]);
                        Stock_arr.push(StockInfo);
                    }

                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}


exports.LivingStockRequest = function(address, storeName, category, callback)
{
    var Stock_arr = [];
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "select productname , eventtext, salesprice, productstock, productid from stock where storeid = (select storeid from store where address = :address and storename = :storename) and productid like '5%'",
            [address, storeName], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StockInfo = new StockDO.LivingStockRequestDO(null,null,null,null,null);
                    Stock_arr.push(StockInfo);
                    callback(Stock_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var StockInfo = new StockDO.LivingStockRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2], result.rows[i][3], result.rows[i][4]);
                        Stock_arr.push(StockInfo);
                    }

                    callback(Stock_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}


//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\카테고리 끝
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\카테고리 끝
//\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\카테고리 끝
