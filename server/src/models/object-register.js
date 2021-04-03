/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    user:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("ObjectRegister",schema);
