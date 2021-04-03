/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    name:String,
    price:Number,
    latitude:Double,
    longitude:Double,
    owner:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    registers:[{ type: mongoose.Schema.Types.ObjectId, ref: 'ObjectRegister' }],
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("Object",schema);
