var db = require("../../util/dbUtil");
var ProductDO = require('../DO/ProductDO');
var TrueFalseDO = require('../DO/TrueFalseDO');


exports.ProductKeywordRequest = function(productKeyword, callback) // 검색어로 상품명 반환
{
    var Product_arr = [];
    var TrueFalse_arr = [];
    db.getDBConnection(function (connection){

        connection.execute(
            "SELECT PRODUCTNAME, PRICE, productID FROM PRODUCT WHERE PRODUCTNAME LIKE :productKeyword"
            ,['%'+productKeyword+'%'], function(err, result){

                if(err){
                    console.error(err.message);
                    return;
                }

                if(result.rows.length==0) {
                    var ProductInfo = new ProductDO.ProductKeywordRequestDO(null,null);
                    Product_arr.push(ProductInfo);
                    callback(Product_arr);
                }
                else
                {
                    for(var i=0; i<result.rows.length; i++)
                    {
                        var ProductInfo = new ProductDO.ProductKeywordRequestDO(result.rows[i][0],result.rows[i][1],result.rows[i][2]);
                        Product_arr.push(ProductInfo);
                    }

                    callback(Product_arr);
                }

                db.disconnectDB(connection);
            }
        );
    });
}