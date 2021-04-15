/// Initialize dependencies
const mongoose = require('mongoose');
const moment = require('moment-timezone');
const dateMexico = moment.tz(Date.now(), "America/Mazatlan");

/// Create schema
const schema=new mongoose.Schema({
    latitude:Number,
    longitude:Number,
    altitude:Number,
    room:{ type: mongoose.Schema.Types.ObjectId, ref: 'Room' },
    createdBy:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    createdAt:{ type:Date, default:dateMexico}
});

/// Export model
module.exports=mongoose.model("Position",schema);
