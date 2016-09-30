/**
 * Created by LOTTE_n-pc on 2016-02-15.
 */
var express = require('express');
var router = express.Router();
var FavoriteDAO = require('../model/DAO/FavoriteDAO');

router.post('/', function(req, res, next) {

    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;

    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="FavoriteRequest")//초록색 부분은 예나랑 맞추는 부분
        FavoriteListRequest(JSON_obj,res);
    if(JSON_obj[type_index]['RequestType']=="FavoriteAddRequest")
        FavoriteAddRequest(JSON_obj,res);
    if(JSON_obj[type_index]['RequestType']=="FavoriteDeleteRequest")
        FavoriteDeleteRequest(JSON_obj,res);
});

function FavoriteListRequest(JSON_obj,res)
{
    var favoriteUserId = JSON_obj[0]['userID'];//예나랑 변수명 맞춰주는 부분

   FavoriteDAO.FavoriteListRequest(favoriteUserId,function(result){
        res.send(result);
    });
}

function FavoriteAddRequest(JSON_obj,res)
{
    var favoriteAddUserId = JSON_obj[0]['userID'];
    var favoriteAddAddress = JSON_obj[1]['address'];
    var favoriteAddStoreName = JSON_obj[2]['storeName'];

    FavoriteDAO.FavoriteAddRequest(favoriteAddUserId,favoriteAddAddress, favoriteAddStoreName,function(result){
        res.send(result);
    });
}



function FavoriteDeleteRequest(JSON_obj,res)
{
    var favoriteDeleteUserId = JSON_obj[0]['userID'];
    var favoriteDeleteStoreName = JSON_obj[1]['address'];

    FavoriteDAO.FavoriteDeleteRequest(favoriteDeleteUserId,favoriteDeleteStoreName,function(result){
        res.send(result);
    });

}
module.exports = router;
