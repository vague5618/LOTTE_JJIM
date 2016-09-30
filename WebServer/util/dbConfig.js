/**
 * Created by LOTTE_n-pc on 2016-02-05.
 */
module.exports = {
    user          : process.env.NODE_ORACLEDB_USER || "gon",
    password      : process.env.NODE_ORACLEDB_PASSWORD || "gon",
    connectString : process.env.NODE_ORACLEDB_CONNECTIONSTRING || "choi-pc/MyDatabase",
    externalAuth  : process.env.NODE_ORACLEDB_EXTERNALAUTH ? true : false
};
