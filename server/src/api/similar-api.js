/// Initialize dependencies
const express=require("express");
const app=express();
const multer=require("multer");
const storage = multer.memoryStorage();
const upload = multer({ storage: storage });
const Jimp = require("jimp");
/// Defining a router
const router=express.Router();
/// Import function
const fileFunctions=require("../functions/file-functions");
/// Import model
const Objects=require("../models/object");

/// Route POST /api/similar
/// Sends an file to be uploaded
var fieldUpload = upload.fields([{ name: 'image', maxCount: 1 }])
router.post("/",fieldUpload,async (req,res)=>{
    try{

        let objects=await Objects.find({
            urlImage:{"$ne":""},
            active:true
        });
        
        if(objects.length==0)
            throw Error("No objects")

        const img1 = await Jimp.read(req.files.image[0].buffer);

        let percentages=[];
        for(let i=0;i<objects.length;i++){
            const img2 = await Jimp.read({
                url:objects[i].urlImage
            });

            let distance=Jimp.distance(img1, img2);
            let percentage=Jimp.diff(img1, img2).percent;
            percentages.push({
                object:objects[i],
                percentage:percentage,
                distance:distance
            });
        }

        percentages.sort((a,b)=>a.percentage-b.percentage);

        res.json({
            status:200,
            percentages:percentages,
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
