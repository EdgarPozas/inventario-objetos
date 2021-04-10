/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    name:String,
    description:String,
    active:{type:Boolean,default:true},
    createdBy:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("Room",schema);
