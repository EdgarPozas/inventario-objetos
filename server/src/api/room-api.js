/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const roomFunctions=require("../functions/room-functions");

/// Route GET /api/room
/// Return all rooms
router.get("/",async (req,res)=>{
    try{
        let values=await roomFunctions.all();
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /api/room/id/:id
/// Return room by id
router.get("/id/:id",async (req,res)=>{
    try{
        let values=await roomFunctions.getById(req.params.id);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route POST /api/room
/// Create new room
router.post("/",async (req,res)=>{
    try{
        let values=await roomFunctions.create(req.body);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route PUT /api/room/:id
/// Update room
router.put("/:id",async (req,res)=>{
    try{
        let values=await roomFunctions.update(req.params.id,req.body);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route DELETE /api/room/:id
/// Delete room
router.delete("/:id",async (req,res)=>{
    try{
        let values=await roomFunctions.delete(req.params.id);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Export module
module.exports=router;
