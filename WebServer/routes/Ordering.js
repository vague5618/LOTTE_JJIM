/**
 * Created by LOTTE_n-pc on 2016-02-19.
 */
/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */
var express = require('express');
var router = express.Router();
var OrderingDAO = require('../model/DAO/OrderingDAO');
var OrderDetailDAO  = require('../model/DAO/OrderDetailDAO');

router.post('/', function(req, res, next) {

    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;


    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="OrderDeleteRequest")
        OrderDeleteRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="OrderCompleteRequest")
        OrderCompleteRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="OrderRequest")
        OrderRequest(JSON_obj,res);
    else if (JSON_obj[type_index]['RequestType']=="OrderNumRequest")
        OrderNumRequest(JSON_obj,res);
    else if (JSON_obj[type_index]['RequestType']=="OrderCancelRequest")
        OrderCancelRequest(JSON_obj,res);

});


function OrderRequest(JSON_obj,res)
{
    var orderNumber = JSON_obj[0]['orderNumber'];
    var userID = JSON_obj[1]['userID'];
    var storeName = JSON_obj[2]['storeName'];
    var cost = JSON_obj[3]['cost'];

    OrderingDAO.OrderRequest(orderNumber,userID,storeName,cost,function(result){
        console.log(result);
        res.send(result);
    });
}





function OrderCompleteRequest(JSON_obj,res)
{
    var userID = JSON_obj[0]['userID'];
    console.log(userID);

    OrderingDAO.OrderCompleteRequest(userID,function(result){
        console.log(result);
        res.send(result);
    });
}


function OrderDeleteRequest(JSON_obj,res)
{
    var userID = JSON_obj[0]['userID'];
    var orderNum = JSON_obj[1]['orderNum'];

    OrderingDAO.OrderDeleteRequest(userID,orderNum,function(result){
        res.send(result);
    });
}




function OrderNumRequest(JSON_obj,res)
{
    var userID = JSON_obj[0]['userID'];

    OrderingDAO.OrderNumRequest(function(result){
        res.send(result);
    });
}


function OrderCancelRequest(JSON_obj,res)
{
    var userID = JSON_obj[0]['userID'];
    var orderNum = JSON_obj[1]['orderNum'];

    OrderingDAO.OrderCancelRequest(orderNum,function(result){

        var length =  result.length ;
        for (var index = 0 ;  index < length ; index ++){
            var storeID = result[index].storeID;
            var productID = result[index].productID;
            var quantity = result[index].quantity;
            var inIndex= index;

            OrderingDAO.StockPlus(storeID, productID,quantity,inIndex,length , function(result){
            })

        }

        OrderingDAO.OrderDeleteRequest(userID,orderNum,function(result){
            res.send(result);
        })
    });
}




module.exports = router;
