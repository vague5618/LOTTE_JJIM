/**
 * Created by LOTTE_n-pc on 2016-02-11.
 */

exports.DongStockCheckDO = function(productName, storeName, address, productStock, eventText, price, salesPrice,latitude, longitude) {
    this.productName = productName;
    this.storeName = storeName;
    this.address = address;
    this.productStock = productStock;
    this.salesPrice = salesPrice;
    this.eventText = eventText;
    this.price = price;
    this.longitude = longitude;
    this.latitude = latitude;
}


exports.NearStockRequestDO = function(productName, storeName, address, productStock, eventText, price, salesPrice,latitude, longitude) {
    this.productName = productName;
    this.storeName = storeName;
    this.address = address;
    this.productStock = productStock;
    this.salesPrice = salesPrice;
    this.eventText = eventText;
    this.price = price;
    this.longitude = longitude;
    this.latitude = latitude;
}

exports.EventStockRequestDO = function(productName, event, salesPrice, productQuantity,productID) {
    this.productName = productName;
    this.event = event;
    this.salesPrice = salesPrice;
    this.productQuantity = productQuantity;
    this.productID = productID;

}

exports.DosirakStockRequestDO = function(productName, event, salesPrice, productQuantity,productID) {
    this.productName = productName;
    this.event = event;
    this.salesPrice = salesPrice;
    this.productQuantity = productQuantity;
    this.productID = productID;
}



exports.SnackStockRequestDO = function(productName, event, salesPrice, productQuantity,productID) {
    this.productName = productName;
    this.event = event;
    this.salesPrice = salesPrice;
    this.productQuantity = productQuantity;
    this.productID = productID;
}


exports.BeverageStockRequestDO = function(productName, event, salesPrice, productQuantity,productID) {
    this.productName = productName;
    this.event = event;
    this.salesPrice = salesPrice;
    this.productQuantity = productQuantity;
    this.productID = productID;
}


exports.CandyStockRequestDO = function(productName, event, salesPrice, productQuantity,productID) {
    this.productName = productName;
    this.event = event;
    this.salesPrice = salesPrice;
    this.productQuantity = productQuantity;
    this.productID = productID;
}


exports.LivingStockRequestDO = function(productName, event, salesPrice, productQuantity,productID) {
    this.productName = productName;
    this.event = event;
    this.salesPrice = salesPrice;
    this.productQuantity = productQuantity;
    this.productID = productID;
}

