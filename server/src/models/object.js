/// Initialize dependencies
const mongoose = require('mongoose');
const moment = require('moment-timezone');
const dateMexico = moment.tz(Date.now(), "America/Mazatlan");

/// Create schema
const schema=new mongoose.Schema({
    name:String,
    description:String,
    functionality:String,
    tags:[{type:String}],
    urlImage:String,
    urlSound:String,
    price:Number,
    active:{type:Boolean,default:true},
    sharedBy:[{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
    positions:[{ type: mongoose.Schema.Types.ObjectId, ref: 'Position' }],
    createdBy:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    createdAt:{ type:Date, default:dateMexico }
});

/// Export model
module.exports=mongoose.model("Objects",schema);
