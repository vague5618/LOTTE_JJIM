/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */
var express = require('express');
var router = express.Router();
var MemberDAO = require('../model/DAO/MemberDAO');

router.post('/', function(req, res, next) {

    var req_data = JSON.stringify(req.body);
    var JSON_obj = JSON.parse(req_data);
    var type_index = JSON_obj.length - 1;

    console.log(JSON_obj[type_index]['RequestType']);

    if(JSON_obj[type_index]['RequestType']=="LoginRequest")
        LoginRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="PushOptionRequest")
        PushOptionRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="PushRequest")
        PushRequest(JSON_obj,res);
    else if(JSON_obj[type_index]['RequestType']=="TokenRequest")
        TokenRequest(JSON_obj,res);

});

function LoginRequest(JSON_obj,res)  // 로그인 Request
{
    var userID = JSON_obj[0]['userID'];
    var userPW = JSON_obj[1]['userPW'];

    MemberDAO.LoginRequest(userID,userPW,function(result){
        res.send(result);
    });
}


function PushOptionRequest(JSON_obj,res) // push 옵션 변경시 Request
{
    var userID = JSON_obj[0]['userID'];
    var check = JSON_obj[1]['check'];

    MemberDAO.PushOptionRequest(userID,check,function(result){
        res.send(result);
    });
}



function PushRequest(JSON_obj,res) // push 설정 화면 로드시 Request
{
    var userID = JSON_obj[0]['userID'];

    MemberDAO.PushRequest(userID,function(result){
        res.send(result);
    });
}

function LoginTestRequest(JSON_obj,res) // push 설정 화면 로드시 Request
{
    var userID = JSON_obj[0]['userID'];

    MemberDAO.LoginTestRequest(userID,function(result){
        res.send(result);
    });
}




function TokenRequest(JSON_obj,res) // Token 리퀘스트
{
    var tokenID = JSON_obj[0]['tokenID'];
    var userID = JSON_obj[1]['userID'];

    MemberDAO.TokenRequest(tokenID,userID,function(result){
        res.send(result);
    });
}


module.exports = router;
