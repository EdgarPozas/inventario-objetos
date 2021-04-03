/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    name:String,
    owner:{ type: mongoose.Schema.Types.ObjectId, ref: 'User' },
    users:[{ type: mongoose.Schema.Types.ObjectId, ref: 'User' }],
    objects:[{ type: mongoose.Schema.Types.ObjectId, ref: 'Object' }],
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("Group",schema);
