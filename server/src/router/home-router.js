/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();

/// Route GET /
router.get("/",async (req,res)=>{
    res.render("home");
});

/// Export module
module.exports=router;
