var express = require('express');
var router = express.Router();
var gcm = require('node-gcm');
var PushDAO = require('../model/DAO/PushDAO');
var TrueFalseDO = require('../model/DO/TrueFalseDO');

var server_api_key = 'AIzaSyCW_g7xkUY8wXfONKgOviOudsnNLCOJdvg';
var gcm_sender = new gcm.Sender(server_api_key);


router.post('/', function(req, res, next) {

      var req_data = JSON.stringify(req.body);
      var JSON_obj = JSON.parse(req_data);
      var RequestType = JSON_obj.RequestType;

    if(RequestType=="SupplyRequest")
         SupplyRequest(JSON_obj,res);
});

function SupplyRequest(JSON_obj,res) // 점포에서 상품 id, 점포 id 받고, STOCK에 상품 갯수 ADD
{
    var productId = JSON_obj.productId;
    var storeId = JSON_obj.storeId;
    var addStock = JSON_obj.addStock;


    PushDAO.SupplyRequest(productId, storeId, '1' , function(result){

        console.log(result);

        if(result==false)
        {
            var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("false");
            res.send(TrueFalseInfo);
        }

        else
        {
            PushReady(productId, storeId);
            res.send(result);
        }
    });
}


function PushReady(productId, storeId) //상품 id, 점포 id를 받아 Wait 테이블의 user id, product name 추출
{
    PushDAO.PushReady(productId, storeId, function(result) {
            PushMemberCheck(productId, storeId, result);
    });
}


function PushMemberCheck(productId, storeId, JSONArray) // product id, user id, product name
{
    var target_ID;
    var target_productName;

    for (var index = 0; index < JSONArray.length; index++) {

        target_ID = JSONArray[index].userID;
        target_productName = JSONArray[index].productName;

        PushDAO.PushMemberCheck(target_ID, function (result) { // 추출한 ID와 Push알림 상태 체크 후 Token ID반환

            var registerIDs = [];

            if(result[0].tokenID!=null) {
                registerIDs.push(result[0].tokenID);
            }

            GCMSender(registerIDs, target_productName, productId, result[0].userID);
        });

        PushDAO.WaitUserDelete(target_ID, productId, storeId, function (result)
        {
            console.log("WaitUserDelete after Push :" + result[0]);
        });
    }
}

function GCMSender(registerIDs, productName, productId, userId)
{
    var message = new gcm.Message({
        collapseKey: 'demo',
        delayWhileIdle: true,
        timeToLive: 2,
        data: {
            title: 'LOTTE_PUSH',
            message: productName+'이 입고되었습니다.',
            productId : productId,
            userId : userId
        }
    });

    gcm_sender.send(message, registerIDs[0], 3, function (err, result) {

        if(err)
        {
            console.error(err.message);
        }
        else {
            console.log(result);
        }});
}

function WaitUserDelete(productId, storeId)
{

    PushDAO.WaitUserDelete(productId, storeId, function(result) {

        console.log("WaitUserDelete Result : "+ result)

    });
}


module.exports = router;
