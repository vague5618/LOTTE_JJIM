/**
 * Created by LOTTE_n-pc on 2016-02-17.
 */

var express = require('express');
var router = express.Router();
var ProductDAO = require('../model/DAO/ProductDAO');


router.post('/', function(req, res, next) {

    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;

    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="ProductKeywordRequest")
        ProductKeywordRequest(JSON_obj,res);

});



function ProductKeywordRequest(JSON_obj,res)
{
    var productKeyword = JSON_obj[0]['productKeyword'];


    ProductDAO.ProductKeywordRequest(productKeyword,function(result){
        res.send(result);
    });


}



    module.exports = router;
