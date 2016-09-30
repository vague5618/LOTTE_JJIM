/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */
var db = require("../../util/dbUtil");
var WaitDO = require('../DO/WaitDO');
var TrueFalseDO = require('../DO/TrueFalseDO');

exports.JJimRequest = function(jjimUserId, callback) // 찜 조회
{
    var Wait_arr = [];

    db.getDBConnection(function (connection){//수정해야됨!
        connection.execute(
            "SELECT S.STORENAME, P.PRODUCTNAME, P.PRODUCTID FROM STORE S, PRODUCT P, WAIT W WHERE W.USERID = :USERID AND S.STOREID=W.STOREID AND P.PRODUCTID=W.PRODUCTID",

            [jjimUserId], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var WaitInfo = new WaitDO.JJimRequestDO(null,null);
                    Wait_arr.push(WaitInfo);
                    callback(Wait_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var WaitInfo = new WaitDO.JJimRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2]);
                        Wait_arr.push(WaitInfo);
                    }

                    callback(Wait_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });
}


exports.JJimAddRequest = function(jjimUserId,jjimProductName, jjimAddress, callback) //찜 추가
{
    var TrueFalse_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(


            "INSERT INTO WAIT (STOREID, PRODUCTID , USERID) VALUES ((SELECT STOREID FROM STORE WHERE ADDRESS = :jjimAddAddress) ,(SELECT PRODUCTID FROM PRODUCT WHERE PRODUCTNAME = :productname) , :userid )"
            , [jjimAddress, jjimProductName, jjimUserId],


                function(err, result){

                if(err){ //DB 쿼리 에러시에
                    var errorCode ="";

                    for(var index=4; index<9; index++)
                    {
                        errorCode = errorCode.concat(err.message[index]);
                    }

                    if(errorCode == "00001")
                    {
                        //무결성 제약 조건위배!
                        var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("dup");
                        TrueFalse_arr.push(TrueFalseInfo);
                        callback(TrueFalse_arr);
                    }
                    else {
                        console.error(err.message);
                    }
                }

                //실패했을때
                else if(result.rowsAffected==0)
                {
                    var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("false");
                    TrueFalse_arr.push(TrueFalseInfo);
                    callback(TrueFalse_arr);
                }

                //성공했을때
                else{
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



exports.JJimDeleteRequest = function(jjimDeleteUserId,jjimDeleteProductID, jjimDeleteStoreName, callback) //찜 삭제
{
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            "DELETE FROM WAIT WHERE USERID=:jjimDeleteUserId AND PRODUCTID = :jjimDeleteProductID AND STOREID = (SELECT STOREID FROM STORE WHERE STORENAME = :jjimDeleteStoreName)"
            ,[jjimDeleteUserId,jjimDeleteProductID,jjimDeleteStoreName],

            function(err, result){

                if(err){
                    console.error(err.message);
                }

                //실패했을때
                else if(result.rowsAffected==0)
                {
                    var TrueFalseInfo = new TrueFalseDO.TrueFalseDO("false");
                    TrueFalse_arr.push(TrueFalseInfo);
                    callback(TrueFalse_arr);
                }


                //성공했을때
                else{
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

