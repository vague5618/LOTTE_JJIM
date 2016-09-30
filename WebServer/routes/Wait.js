/**
 * Created by LOTTE_n-pc on 2016-02-17.
 */
/**
 * Created by LOTTE_n-pc on 2016-02-15.
 */
var express = require('express');
var router = express.Router();
var WaitDAO = require('../model/DAO/WaitDAO');

router.post('/', function(req, res, next) {
    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;

    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="JJimRequest")
        JJimRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="JJimAddRequest")
        JJimAddRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="JJimDeleteRequest")
        JJimDeleteRequest(JSON_obj,res);
});

function JJimRequest(JSON_obj,res)
{
    var jjimUserId = JSON_obj[0]['userID'];
    WaitDAO.JJimRequest(jjimUserId,function(result){
        res.send(result);
    });
}


function JJimAddRequest(JSON_obj,res)
{
    var jjimUserId = JSON_obj[0]['userID'];
    var jjimProductName = JSON_obj[1]['productName'];
    var jjimAddress = JSON_obj[2]['address'];

    WaitDAO.JJimAddRequest(jjimUserId,jjimProductName,jjimAddress,function(result){
        res.send(result);
    });
}



function JJimDeleteRequest(JSON_obj,res)
{
    var jjimDeleteUserId = JSON_obj[0]['userID'];
    var jjimDeleteProductID = JSON_obj[1]['productID'];
    var jjimDeletestoreName = JSON_obj[2]['storeName'];


    WaitDAO.JJimDeleteRequest(jjimDeleteUserId,jjimDeleteProductID,jjimDeletestoreName,function(result){
        res.send(result);
    });
}


module.exports = router;
