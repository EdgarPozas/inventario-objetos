/// Initialize dependencies
const mongoose = require('mongoose');
const moment = require('moment-timezone');
const dateMexico = moment.tz(Date.now(), "America/Mazatlan");

/// Create schema
const schema=new mongoose.Schema({
    name:String,
    description:String,
    active:{type:Boolean,default:true},
    createdBy:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    createdAt:{ type:Date, default:dateMexico }
});

/// Export model
module.exports=mongoose.model("Room",schema);
