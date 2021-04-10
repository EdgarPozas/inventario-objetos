/// Initialize dependencies
const express=require("express");
const app=express();
const multer=require("multer");
const storage = multer.memoryStorage()
const upload = multer({ storage: storage })
/// Defining a router
const router=express.Router();
/// Import function
const fileFunctions=require("../functions/file-functions");

/// Route POST /api/file
/// Sends an file to be uploaded
var fieldUpload = upload.fields([{ name: 'image', maxCount: 1 }, { name: 'audio', maxCount: 1 }])
router.post("/",fieldUpload,async (req,res)=>{
    try{
        let urls=[];
        let fileImage=req.files.image[0];
        let valuesImage=await fileFunctions.upload(fileImage);
        if(valuesImage.status!=200)
            throw Error(valuesImage.msg)
        urls.push(valuesImage)
        let fileAudio=req.files.audio[0];
        let valuesAudio=await fileFunctions.upload(fileAudio);
        if(valuesAudio.status!=200)
            throw Error(valuesAudio.msg)
        urls.push(valuesAudio)
        
        res.json({
            status:200,
            files:urls,
            msg:"Files uploaded"
        });
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
