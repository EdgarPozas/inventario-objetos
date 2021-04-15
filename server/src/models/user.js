/// Initialize dependencies
const mongoose = require('mongoose');
const moment = require('moment-timezone');
const dateMexico = moment.tz(Date.now(), "America/Mazatlan");

/// Create schema
const schema=new mongoose.Schema({
    firstName:String,
    lastName:String,
    email:String,
    password:String,
    active:{ type:Boolean, default:true },
    verified:{ type:Boolean, default:false },
    createdAt:{ type:Date, default:dateMexico }
});

/// Export model
module.exports=mongoose.model("User",schema);
