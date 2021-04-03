/// Initialize dependencies
const mongoose = require('mongoose');

/// Create schema
const schema=new mongoose.Schema({
    firtName:String,
    lastName:String,
    email:String,
    password:String,
    verified:{ type:Boolean, default:false },
    logged:{ type:Boolean, default:false },
    groups:[{ type: mongoose.Schema.Types.ObjectId, ref: 'Group' }],
    objects:[{ type: mongoose.Schema.Types.ObjectId, ref: 'Object' }],
    createdAt:{ type:Date, default:Date.now }
});

/// Export model
module.exports=mongoose.model("User",schema);
