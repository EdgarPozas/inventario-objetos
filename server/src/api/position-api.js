/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const positionFunctions=require("../functions/position-functions");

/// Route GET /api/position
/// Return all positions
router.get("/",async (req,res)=>{
    try{
        let values=await positionFunctions.all();
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /api/position/id/:id
/// Return position by id
router.get("/id/:id",async (req,res)=>{
    try{
        let values=await positionFunctions.getById(req.params.id);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route POST /api/position
/// Create new position
router.post("/",async (req,res)=>{
    try{
        let values=await positionFunctions.create(req.body);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route PUT /api/position/:id
/// Update position
router.put("/:id",async (req,res)=>{
    try{
        let values=await positionFunctions.update(req.params.id,req.body);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route DELETE /api/position/:id
/// Delete position
router.delete("/:id",async (req,res)=>{
    try{
        let values=await positionFunctions.delete(req.params.id);
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
