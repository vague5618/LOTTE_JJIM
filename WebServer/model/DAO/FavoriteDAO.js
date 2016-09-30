var db = require("../../util/dbUtil");
var FavoriteDO = require('../DO/FavoriteDO');
var TrueFalseDO = require('../DO/TrueFalseDO');

exports.FavoriteListRequest = function(favoriteUserId, callback) // 즐겨찾기 목록 조회
{
    var Favorite_arr = [];  //// 목록조회같은 Request에 필요한 배열
    db.getDBConnection(function (connection){
        connection.execute(
            "SELECT F.STORENAME, F.ADDRESS, S.LATITUDE, S.LONGITUDE FROM FAVORITE F,STORE S WHERE F.USERID = :UserId AND S.STOREID = F.STOREID",
            [favoriteUserId], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var FavoriteInfo = new FavoriteDO.FavoriteListRequestDO(null,null,null,null);
                    Favorite_arr.push(FavoriteInfo);
                    callback(Favorite_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var FavoriteInfo = new FavoriteDO.FavoriteListRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2],result.rows[i][3]);
                        Favorite_arr.push(FavoriteInfo);
                    }

                    callback(Favorite_arr);
                }
                db.disconnectDB(connection);
            }
        );
    });
}

exports.FavoriteAddRequest = function(favoriteAddUserId, favoriteAddAddress, favoriteAddStoreName, callback) //즐겨찾기 추가
{
    var TrueFalse_arr = [];

    db.getDBConnection(function (connection){
        connection.execute(
            "INSERT INTO FAVORITE VALUES (:userID ,(SELECT STOREID FROM STORE WHERE STORENAME = :favoAddStoreName AND ADDRESS = :favoAddAddress), :favoAddStoreName, :favoAddAddress)"
            ,[favoriteAddUserId,favoriteAddStoreName,favoriteAddAddress,favoriteAddStoreName,favoriteAddAddress],

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

exports.FavoriteDeleteRequest = function(favoriteDeleteUserId, favoriteAddAddress, callback) //즐겨찾기 삭제
{

    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){
        connection.execute(
            "DELETE FROM FAVORITE WHERE USERID = :UserId and ADDRESS = :favoAddAddress",
            [favoriteDeleteUserId,favoriteAddAddress],
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


