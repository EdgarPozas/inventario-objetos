/// Initialize dependencies
const express=require("express");
const path=require("path");
const app=express();
/// Defining a router
const router=express.Router();

/// Define the view's folder
app.set("view",path.join(__dirname,"../views"));

/// Route GET /
router.get("/",async (req,res)=>{
    res.render("home");
});

/// Export module
module.exports=router;
