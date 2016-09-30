/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */


exports.OrderDetailCompleteRequestDO = function(productName ,salesPrice, orderQuantity,eventText,productID){
    this.productName = productName;
    this.salesPrice = salesPrice;
    this.orderQuantity = orderQuantity;
    this.eventText = eventText;
    this.productID = productID;
}
