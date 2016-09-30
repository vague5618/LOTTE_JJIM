/**
 * Created by LOTTE_n-pc on 2016-02-17.
 */
var db = require("../../util/dbUtil");
var PushDO = require('../DO/PushDO');
var TrueFalseDO = require('../DO/TrueFalseDO');

exports.SupplyRequest = function(productId, storeId, addStock, callback) // 재고 입고시 상품 개수 업데이트 해주는 함수
{
    var TrueFalse_arr = [];

    db.getDBConnection(function (connection) {
        connection.execute(
            "UPDATE stock SET productstock = productstock + :addstock WHERE storeid = :storeid and productid = :productid ",
            [addStock, storeId, productId],

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



exports.PushReady = function(productId,storeId, callback) // 재고 입고시 Push알림 받을 ID추출
{
    var Push_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT USERID,(SELECT PRODUCTNAME FROM PRODUCT WHERE PRODUCTID = :PRODUCTID) FROM WAIT WHERE PRODUCTID=:PRODUCTID AND STOREID = :STOREID",
            [productId, productId, storeId], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var PushInfo = new PushDO.PushReadyDO(null,null);
                    Push_arr.push(PushInfo);
                    callback(Push_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var PushInfo = new PushDO.PushReadyDO(result.rows[i][0],result.rows[i][1]);
                        Push_arr.push(PushInfo);
                    }

                    callback(Push_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });
}


exports.PushMemberCheck = function(userID, callback) // 추출한 ID와 Push알림 상태 체크 확인 후 Token ID반환
{
    var token_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT TOKENID, USERID FROM MEMBER WHERE USERID=:USERID AND PUSHCHECK='true'",
            [userID], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var tokenInfo = new PushDO.PushMemberCheckDO(null,null);
                    token_arr.push(tokenInfo);
                    callback(token_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var tokenInfo = new PushDO.PushMemberCheckDO(result.rows[i][0],result.rows[i][1]);
                        token_arr.push(tokenInfo);
                    }

                    callback(token_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });
}




exports.WaitUserDelete = function(target_ID,productId,storeId, callback) //즐겨찾기 삭제
{

    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            "DELETE FROM WAIT WHERE USERID = :target_ID and PRODUCTID = :productId and STOREID = :storeID",
            [target_ID,productId,storeId],
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

