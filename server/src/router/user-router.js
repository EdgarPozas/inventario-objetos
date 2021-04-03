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

/// Route GET /user/register
router.get("/register",async (req,res)=>{
    res.render("register");
});

/// Route POST /user/register
router.post("/register",async (req,res)=>{
    let values=userFunctions.create(req.body);
    res.send(values.status);
});

/// Export module
module.exports=router;
