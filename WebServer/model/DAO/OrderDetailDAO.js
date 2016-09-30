/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */
var db = require("../../util/dbUtil");
var OrderDetailDO = require('../DO/OrderDetailDO');
var TrueFalseDO = require('../DO/TrueFalseDO');


exports.OrderDetailCompleteRequest = function(orderNum, callback) // 예약 상세내역 조회
{
    var OrderDetail_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            //"SELECT S.PRODUCTNAME, S.SALESPRICE, OD.QUANTITY, S.EVENTTEXT, OD.PRODUCTID FROM ORDERDETAIL OD, STOCK S WHERE OD.ORDERNUMBER = :ORDERNUM AND S.STOREID = (SELECT STOREID FROM ORDERING WHERE ORDERNUMBER = :ORDERNUMBER) AND OD.USERID = :userID AND S.PRODUCTID = OD.PRODUCTID ",///////
            "SELECT PRODUCTNAME, COST, QUANTITY, EVENTTEXT, PRODUCTID FROM VIEW3 WHERE ORDERNUMBER = :orderNum",
            [orderNum],

            function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var OrderDetailInfo = new OrderDetailDO.OrderDetailCompleteRequestDO(null,null,null,null,null);
                    OrderDetail_arr.push(OrderDetailInfo);
                    callback(OrderDetail_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var OrderDetailInfo = new OrderDetailDO.OrderDetailCompleteRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2],result.rows[i][3],result.rows[i][4]);
                        OrderDetail_arr.push(OrderDetailInfo);
                    }

                    callback(OrderDetail_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });
}


exports.OrderDetailRequest = function(orderNumber, storeName, productName,orderingQuantity,salesPrice,Inindex,length, callback) //예약 상세내역 추가
{
    var TrueFalse_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "INSERT INTO ORDERDETAIL (ORDERNUMBER,  STOREID, PRODUCTID, QUANTITY , COST ) VALUES (:ordernumber ,(SELECT STOREID FROM STORE WHERE storeName = :storeName), (SELECT PRODUCTID FROM PRODUCT WHERE PRODUCTNAME = :productname),:QUANTITY, :COST)"
            ,[orderNumber,storeName,productName.trim(),orderingQuantity,salesPrice],


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
                        if(Inindex == length)
                            callback(TrueFalse_arr);

                        db.disconnectDB(connection);
                    });
                }
            }
        );
    });
}



exports.StockMinus = function(productName, storeName, delStock, callback) // 재고 입고시 상품 개수 업데이트 해주는 함수
{
    var TrueFalse_arr = [];

    db.getDBConnection(function (connection) {
        connection.execute(
            "UPDATE stock SET productstock = productstock - :delStock WHERE storeid = (SELECT STOREID FROM STORE WHERE STORENAME =  :storeName ) and productName = :productName ",
            [delStock, storeName, productName],

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
                        //callback(TrueFalse_arr);

                        db.disconnectDB(connection);
                    });
                }
            }

        );
    });
}
