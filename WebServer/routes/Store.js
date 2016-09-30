/**
 * Created by LOTTE_n-pc on 2016-02-12.
 */
var express = require('express');
var router = express.Router();
var StoreDAO = require('../model/DAO/StoreDAO');

router.post('/', function(req, res, next) {

    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;

    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="StoreSearchRequest")
        storeSearch(JSON_obj,res);
});

function storeSearch(JSON_obj,res){

    var address1 = JSON_obj[0]['add1'];
    var address2 = JSON_obj[1]['add2'];
    var address3 = JSON_obj[2]['add3'];

    StoreDAO.storeSearch(address1,address2,address3,function(result){
        res.send(result);
    });
}

module.exports = router;
