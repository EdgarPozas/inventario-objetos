/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const groupFunctions=require("../functions/group-functions");

/// Route GET /api/group
router.get("/",async (req,res)=>{
    res.render("register");
});

/// Route POST /api/group
router.post("/",async (req,res)=>{
    let values=groupFunctions.create(req.body);
    res.send(values.status);
});

/// Export module
module.exports=router;
