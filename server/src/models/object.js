/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    name:String,
    description:String,
    functionality:String,
    tags:[{type:String}],
    urlImage:String,
    urlSound:String,
    price:Number,
    sharedBy:[{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
    positions:[{ type: mongoose.Schema.Types.ObjectId, ref: 'Position' }],
    createdBy:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("Objects",schema);
