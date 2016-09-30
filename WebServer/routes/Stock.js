/**
 * Created by LOTTE_n-pc on 2016-02-12.
 */
var express = require('express');
var router = express.Router();
var StockDAO = require('../model/DAO/StockDAO');

router.post('/', function(req, res, next) {

    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;

    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="DongStockRequest")
        DongStockCheck(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="NearStockRequest")
        NearStockRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="EventStockRequest")
        EventStockRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="DosirakStockRequest")
        DosirakStockRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="SnackStockRequest")
        SnackStockRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="BeverageStockRequest")
        BeverageStockRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="CandyStockRequest")
        CandyStockRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="LivingStockRequest")
        LivingStockRequest(JSON_obj,res);


});

function DongStockCheck(JSON_obj,res)
{
    var productName = JSON_obj[0]['productName'];
    var address1 = JSON_obj[1]['add1'];
    var address2 = JSON_obj[2]['add2'];
    var address3 = JSON_obj[3]['add3'];


    StockDAO.DongStockCheck(productName,address1,address2,address3,function(result){
        res.send(result);
    });
}


function NearStockRequest(JSON_obj,res)
{
    var productName = JSON_obj[0]['productName'];
    var latitude = JSON_obj[1]['latitude'];
    var longitude = JSON_obj[2]['longitude'];

    StockDAO.NearStockRequest(productName,latitude,longitude,function(result){
        res.send(result);
    });
}

function EventStockRequest(JSON_obj,res)
{
    var address = JSON_obj[0]['address'];
    var storeName = JSON_obj[1]['storeName'];

    StockDAO.EventStockRequest(address,storeName,function(result){
        res.send(result);
    });
}

function DosirakStockRequest(JSON_obj,res)
{
    var address = JSON_obj[0]['address'];
    var storeName = JSON_obj[1]['storeName'];

    StockDAO.DosirakStockRequest(address,storeName,"도시락",function(result){
        res.send(result);
    });
}


function SnackStockRequest(JSON_obj,res)
{
    var address = JSON_obj[0]['address'];
    var storeName = JSON_obj[1]['storeName'];

    StockDAO.SnackStockRequest(address,storeName,"과자",function(result){
        res.send(result);
    });
}

function BeverageStockRequest(JSON_obj,res)
{
    var address = JSON_obj[0]['address'];
    var storeName = JSON_obj[1]['storeName'];

    StockDAO.BeverageStockRequest(address,storeName,"음료",function(result){
        res.send(result);
    });
}

function CandyStockRequest(JSON_obj,res)
{
    var address = JSON_obj[0]['address'];
    var storeName = JSON_obj[1]['storeName'];

    StockDAO.CandyStockRequest(address,storeName,"캔디류",function(result){
        res.send(result);
    });

}

function LivingStockRequest(JSON_obj,res)
{
    var address = JSON_obj[0]['address'];
    var storeName = JSON_obj[1]['storeName'];


    StockDAO.LivingStockRequest(address,storeName,"생활용품",function(result){
        res.send(result);
    });
}


module.exports = router;
