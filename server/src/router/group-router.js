/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const groupFunctions=require("../functions/group-functions");

/// Route GET /group/register
router.get("/register",async (req,res)=>{
    res.render("register");
});

/// Route POST /group/register
router.post("/register",async (req,res)=>{
    let values=groupFunctions.create(req.body);
    res.send(values.status);
});

/// Export module
module.exports=router;
