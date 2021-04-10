/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const objectFunctions=require("../functions/object-functions");

/// Route GET /api/object
/// Return all objects
router.get("/",async (req,res)=>{
    try{
        let values=await objectFunctions.all();
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /api/object/id/:id
/// Return object by id
router.get("/id/:id",async (req,res)=>{
    try{
        let values=await objectFunctions.getById(req.params.id);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route POST /api/object
/// Create new object
router.post("/",async (req,res)=>{
    try{
        let values=await objectFunctions.create(req.body);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route PUT /api/object/:id
/// Update object
router.put("/:id",async (req,res)=>{
    try{
        let values=await objectFunctions.update(req.params.id,req.body);
        res.json(values);
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route DELETE /api/object/:id
/// Delete object
router.delete("/:id",async (req,res)=>{
    try{
        let values=await objectFunctions.delete(req.params.id);
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
