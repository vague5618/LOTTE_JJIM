/**
 * Created by LOTTE_n-pc on 2016-02-17.
 */

exports.PushRequestDO = function(tokenID,productName){
    this.tokenID = tokenID;
    this.productName = productName;
}

exports.PushReadyDO = function(userID, productName){
    this.userID = userID;
    this.productName = productName;
}


exports.PushMemberCheckDO = function(tokenID,userID){
    this.tokenID = tokenID;
    this.userID = userID;
}

