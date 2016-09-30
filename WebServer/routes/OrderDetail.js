/**
 * Created by LOTTE_n-pc on 2016-02-19.
 */
var express = require('express');
var router = express.Router();
var OrderDetailDAO = require('../model/DAO/OrderDetailDAO');

router.post('/', function(req, res, next) {

    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;

    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="OrderDetailCompleteRequest")
        OrderDetailCompleteRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="OrderDetailRequest")
        OrderDetailRequest(JSON_obj,res);

});

function OrderDetailCompleteRequest(JSON_obj,res) //OrderDetail 목록 확인 함수
{
    var orderNum = JSON_obj[0]['orderNum'];

    OrderDetailDAO.OrderDetailCompleteRequest(orderNum,function(result){
        res.send(result);
    });
}


    function OrderDetailRequest(JSON_obj,res) //OrderDetail 목록 추가요청 함수
    {
     var length =  JSON_obj.length - 2 ;

    for (var index = 0 ;  index <= length ; index ++){

        var orderNumber = JSON_obj[index].orderNumber;
        var storeName = JSON_obj[index].storeName;
        var productName = JSON_obj[index].productName;
        var orderingQuantity = JSON_obj[index].orderingQuantity;
        var salesPrice = JSON_obj[index].salesPrice;
        var inIndex= index;


        OrderDetailDAO.OrderDetailRequest(orderNumber, storeName, productName,orderingQuantity,salesPrice,inIndex,length , function(result){
                res.send(result);
        })

        var delStock = orderingQuantity;

        OrderDetailDAO.StockMinus(productName, storeName,delStock, function(result){
        });
        }

}



module.exports = router;
