/// Initialize dependencies
const express=require("express");
const path=require("path");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const userFunctions=require("../functions/user-functions");

/// Define the view's folder
app.set("view",path.join(__dirname,"../views"));

/// Route GET /api/user
router.get("/",async (req,res)=>{
    res.render("register");
});

/// Route POST /api/user
router.post("/",async (req,res)=>{
    let values=userFunctions.create(req.body);
    res.send(values.status);
});

/// Export module
module.exports=router;
