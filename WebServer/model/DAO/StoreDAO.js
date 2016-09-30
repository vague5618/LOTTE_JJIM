/**
 * Created by LOTTE_n-pc on 2016-02-05.
 */
var db = require("../../util/dbUtil");
var StoreDO = require('../DO/StoreDO');
var TrueFalseDO = require('../DO/TrueFalseDO');

exports.storeSearch = function(address1, address2, address3,callback)
{
    var Store_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "SELECT STORENAME, ADDRESS ,LONGITUDE,LATITUDE FROM STORE WHERE ADDRESS1= :AD1 and ADDRESS2= :AD2 and ADDRESS3= :AD3",
            [address1, address2, address3], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var StoreInfo = new StoreDO.StoreSearchDO(null,null,null,null);
                    Store_arr.push(StoreInfo);
                    callback(Store_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                          var StoreInfo = new StoreDO.StoreSearchDO(result.rows[i][0],result.rows[i][1],result.rows[i][2],result.rows[i][3]);

                          Store_arr.push(StoreInfo);
                    }

                    callback(Store_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}

