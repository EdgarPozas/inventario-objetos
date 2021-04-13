/// Initialize dependencies
const express=require("express");
const app=express();
const bcrypt=require("bcrypt");
/// Defining a router
const router=express.Router();
/// Models
const User=require("../models/user");
const Objects=require("../models/object");
const Room=require("../models/room");
const Position=require("../models/position");
/// Functions
const userFunctions=require("../functions/user-functions");
const user = require("../models/user");

/// Route GET /
router.get("/",async (req,res)=>{
    res.redirect("/objects");
});

/// Route GET /users
router.get("/users",async (req,res)=>{
    try{
        let users=await User.find({});
        res.render("users",{users});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /users/:id
router.get("/users/:id",async (req,res)=>{
    try{
        let {id}=req.params;
        let user=await User.findById(id);
        let objects=await Objects.find({createdBy:id});
        let rooms=await Room.find({createdBy:id});
        res.render("users-individual",{user,objects,rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /objects
router.get("/objects",async (req,res)=>{
    try{
        let objects=await Objects.find({}).populate("createdBy").populate("sharedBy").populate("positions");
        let users=await User.find({});
        let rooms=await Room.find({});
        res.render("objects",{objects,users,rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /objects/:id
router.get("/objects/:id",async (req,res)=>{
    try{
        let {id}=req.params;
        let object=await Objects.findById({_id:id}).populate("createdBy").populate("sharedBy").populate("positions");
        let users=await User.find({});
        let rooms=await Room.find({});
        res.render("objects-individual",{object,users,rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /rooms
router.get("/rooms",async (req,res)=>{
    try{
        let rooms=await Room.find({});
        res.render("rooms",{rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /rooms/:id
router.get("/rooms/:id",async (req,res)=>{
    try{
        let {id}=req.params;
        let room=await Room.findById({_id:id}).populate("createdBy");
        let positions=await Position.find({room:id},"_id");
        let arrayPositions=[];
        for(let i=0;i<positions.length;i++){
            arrayPositions.push(positions[i]._id);
        }
        let objects=await Objects.aggregate([
            { $project: { lastPosition: { $arrayElemAt: ['$positions', -1] } } },
            { $match: { lastPosition: {$in:arrayPositions} } }
        ]);
        let arrayObjects=[];
        for(let i=0;i<objects.length;i++){
            arrayObjects.push(objects[i]._id+"");
        }
        let objectsAux=await Objects.find({_id:{$in:arrayObjects}}).populate("sharedBy").populate("positions");
        res.render("rooms-individual",{room,objects:objectsAux});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /user/verify/:id
router.get("/users/verify/:id",async (req,res)=>{
    try{
        let values=await userFunctions.verify(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);
        res.render("verify");
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /users/verify/:id
router.get("/users/recovery/:id",async (req,res)=>{
    try{
        let user=await User.findById(req.params.id);
        res.render("recovery",{user});
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
    
});

/// Route POST /users/recovery/:id
router.post("/users/recovery/:id",async (req,res)=>{
    try{
        let {
            email,
            password,
        }=req.body;
        
        let user=await User.findOne({
            _id:req.params.id,
            email:email
        }).exec();

        if(!user)
            throw Error("User not found");

        user.password=await bcrypt.hash(password, 10);
        await user.save();

        res.render("recovery", {
            status:200,
            user:user,
        });
    }catch(ex){
        console.log(ex);
        let user=await User.findById(req.params.id);
        res.render("recovery", {
            status:400,
            user:user,
        });
    }
});


/// Route GET /about
router.use((req,res,next)=>{
    res.render("notfound");
});


/// Export module
module.exports=router;
