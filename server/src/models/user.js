/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    firstName:String,
    lastName:String,
    email:String,
    password:String,
    active:{ type:Boolean, default:true },
    verified:{ type:Boolean, default:false },
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("User",schema);
