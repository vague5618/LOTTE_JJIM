/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */
var db = require("../../util/dbUtil");
var OrderingDO = require('../DO/OrderingDO');
var TrueFalseDO = require('../DO/TrueFalseDO');




exports.OrderRequest = function(orderNumber, userID,storeName,cost, callback) // 예약 추가
{
    var TrueFalse_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "INSERT INTO ORDERING  VALUES (:ordernum, :userID , (select storeid from store where storeName = :storeName) , :cost )",
            [ orderNumber, userID,storeName,cost],

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


exports.OrderCompleteRequest = function(userID, callback) // 예약 조회
{
    var Ordering_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT O.ORDERNUMBER, S.STORENAME ,O.TOTALCOST FROM ORDERING O,STORE S WHERE USERID = :userid and S.STOREID = O.STOREID ",
            [userID], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var OrderingInfo = new OrderingDO.OrderCompleteRequestDO(null,null,null);
                    Ordering_arr.push(OrderingInfo);
                    callback(Ordering_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var OrderingInfo = new OrderingDO.OrderCompleteRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2]);
                        Ordering_arr.push(OrderingInfo);
                    }

                    callback(Ordering_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });


}


exports.OrderDeleteRequest = function(userID,orderNum, callback) //예약 수령.
{
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            "DELETE FROM ORDERING WHERE userID = :userID and orderNumber = :orderNum",
            [userID,orderNum],
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




exports.OrderCancelRequest = function(orderNum, callback) //예약 취소
{
    var Ordering_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT STOREID, PRODUCTID,QUANTITY FROM ORDERDETAIL WHERE ORDERNUMBER = :orderNum",
            [orderNum],
            
            function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var OrderingInfo = new OrderingDO.OrderCancelRequestDO(null,null,null);
                    Ordering_arr.push(OrderingInfo);
                    callback(Ordering_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var OrderingInfo = new OrderingDO.OrderCancelRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2]);
                        Ordering_arr.push(OrderingInfo);
                    }

                    callback(Ordering_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });
    

}



exports.StockPlus = function(storeID, productID,quantity,inIndex,length , callback) //예약 취소시 재고 다시 더해주는 함수
{
    var TrueFalse_arr = [];

    db.getDBConnection(function (connection) {
        connection.execute(
            "UPDATE stock SET productstock = productstock + :quantity WHERE storeID = :storeid and productID = :productID ",
            [quantity, storeID, productID],

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
                        if(inIndex == length-1)
                        callback(TrueFalse_arr);

                        db.disconnectDB(connection);
                    });
                }
            }

        );
    });
}


exports.OrderNumRequest = function(callback) // 예약 번호 부여 리퀘스트
{
    var OrderNumber_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT MAX(ORDERNUMBER)+1 FROM ORDERING",

            function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var OrderNumberInfo = new OrderingDO.OrderNumRequestDO(null);
                    OrderNumber_arr.push(OrderNumberInfo);
                    callback(OrderNumber_arr);
                }
                else
                {
                    var OrderNumberInfo = new OrderingDO.OrderNumRequestDO(result.rows[0][0]);
                    OrderNumber_arr.push(OrderNumberInfo);
                    callback(OrderNumber_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });

}



