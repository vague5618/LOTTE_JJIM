/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */
exports.OrderCompleteRequestDO = function(orderNum,storeName,fullCost){
    this.orderNum = orderNum;
    this.storeName = storeName;
    this.fullCost = fullCost;
}


exports.OrderNumRequestDO = function(orderNumber){
    this.orderNumber = orderNumber;

}


exports.OrderCancelRequestDO = function(storeID,productID,quantity){
    this.storeID = storeID;
    this.productID = productID;
    this.quantity = quantity;
}
