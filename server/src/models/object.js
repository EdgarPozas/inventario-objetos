/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    name:String,
    description:String,
    function:String,
    tags:[{type:String}],
    urlImage:String,
    urlSound:String,
    price:Number,
    sharedBy:[{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
    positions:[{ 
        latitude:Number,
        longitude:Number,
        altitude:Number,
        date: { type:Date, default:Date.now },
        room:{ type: mongoose.Schema.Types.ObjectId, ref: 'Room' },
        updatedBy:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    }],
    createdBy:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("Objects",schema);
