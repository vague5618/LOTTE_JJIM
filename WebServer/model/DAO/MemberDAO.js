/**
 * Created by LOTTE_n-pc on 2016-02-05.
 */
var db = require("../../util/dbUtil");
var MemberDO = require('../DO/MemberDO');
var TrueFalseDO = require('../DO/TrueFalseDO');




exports.LoginRequest = function(userID, userPW, callback)
{

    var TrueFalse_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT * FROM MEMBER WHERE USERID = :UserId AND USERPW = :UserPw",
            [userID,userPW],
            //  "SELECT username, encryption_aes.DEC_AES(userpass) FROM MEMBER1234 WHERE username = '김예나' AND encryption_aes.DEC_AES(userpass) = '12'",
            //"SELECT userid, encryption_aes.DEC_AES(userpw) FROM MEMBER1234 WHERE userid = '김예나' AND encryption_aes.DEC_AES(userpw) = '12'",

            function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("false");
                    TrueFalse_arr.push(TrueFalseInfo);
                    callback(TrueFalse_arr);
                }
                else
                {
                    var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("true");
                    TrueFalse_arr.push(TrueFalseInfo);
                    callback(TrueFalse_arr);
                }

                db.disconnectDB(connection);
                });
            });
}



exports.PushRequest = function(userID, callback)
{

   var Member_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT pushcheck FROM MEMBER WHERE USERID = :UserId",
            [userID],

            function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var MemberInfo = new MemberDO.PushRequestDO(null);
                    Member_arr.push(MemberInfo);
                    callback(Member_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var MemberInfo = new MemberDO.PushRequestDO(result.rows[i][0]);
                        Member_arr.push(MemberInfo);
                    }

                    callback(Member_arr);
                }

                db.disconnectDB(connection);
            }




        );
    });
}



exports.PushOptionRequest = function(userID, check, callback) // 푸쉬 상태 update를 위한 클래스
{
    var TrueFalse_arr = [];


    db.getDBConnection(function (connection){
        connection.execute(
            "UPDATE MEMBER SET PUSHCHECK= :PUSHCHECK WHERE USERID= :USERID"
            ,[check, userID],


            function (err, result) {

                if (err) {
                    console.error(err.message);
                }

                //실패했을때
                else if (result.rowsAffected == 0) {
                    callback(false);
                }

                //성공했을때
                else {
                    connection.commit(function (err) {

                        //커밋실패시에
                        if (err) {
                            console.error(err.message);
                        }

                        //커밋성공시에
                        var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("true");
                        TrueFalse_arr.push(TrueFalseInfo);
                        callback(TrueFalse_arr);

                        db.disconnectDB(connection);
                    });
                }
            }



        );
    });
}


exports.LoginTestRequest = function(id,pw,callback)
{
    var Member_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT USERID, encryption_aes.DEC_AES(userPW) FROM MEMBER WHERE USERID = :id AND encryption_aes.DEC_AES(userPW) = :pw"
            ,[id, pw]

            ,
            function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var MemberInfo = new MemberDO.TestLoginRequestDO(null);
                    Member_arr.push(MemberInfo);
                    callback(Member_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var MemberInfo = new MemberDO.TestLoginRequestDO(result.rows[i][0]);
                        Member_arr.push(MemberInfo);
                    }

                    callback(Member_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });


}






exports.TokenRequest = function(tokenID,userID, callback)
{

    var TrueFalse_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "UPDATE MEMBER SET TOKENID = :tokenID WHERE USERID = :userID ",
            [tokenID,userID],

            function (err, result) {

                if (err) {
                    console.error(err.message);
                }

                //실패했을때
                else if (result.rowsAffected == 0) {
                    callback(false);
                }

                //성공했을때
                else {
                    connection.commit(function (err) {

                        //커밋실패시에
                        if (err) {
                            console.error(err.message);
                        }

                        //커밋성공시에
                        var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("true");
                        TrueFalse_arr.push(TrueFalseInfo);
                        callback(TrueFalse_arr);

                        db.disconnectDB(connection);
                    });
                }
            }

        );
    });
}




