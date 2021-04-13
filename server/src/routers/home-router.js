/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Models
const User=require("../models/user");
const Objects=require("../models/object");
const Room=require("../models/room");
const Position=require("../models/position");

/// Route GET /
router.get("/",async (req,res)=>{
    res.render("home");
});

/// Route GET /users
router.get("/users",async (req,res)=>{
    try{
        let users=await User.find({});
        res.render("users",{users});
    }catch(ex){
        res.send(ex)
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
        res.send(ex)
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
        res.send(ex)
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
        res.send(ex)
    }
});

/// Route GET /rooms
router.get("/rooms",async (req,res)=>{
    try{
        let rooms=await Room.find({});
        res.render("rooms",{rooms});
    }catch(ex){
        res.send(ex)
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
        let objectsAux=await Objects.find({_id:{$in:arrayObjects}});
        res.render("rooms-individual",{room,objects:objectsAux});
    }catch(ex){
        res.send(ex)
    }
});

/// Route GET /about
router.get("/about",async (req,res)=>{
    res.render("about");
});

/// Route GET /user/verify/:id
router.get("/user/verify/:id",async (req,res)=>{
    res.render("verify");
});

/// Export module
module.exports=router;
