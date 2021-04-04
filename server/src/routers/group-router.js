/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const groupFunctions=require("../functions/group-functions");
/// Import middleware
const loginMiddleware=require("../middlewares/login-middleware");
//router.use(loginMiddleware);
/// tmp
const userFunctions=require("../functions/user-functions");

/// Route GET /group
router.get("/",async (req,res)=>{
    let values=await userFunctions.login("edgar730a@gmail.com","1");
    req.session.user=values.user;
    console.log(values.user);
    res.render("groups",{
        user:req.session.user
    });
});

/// Route POST /group
router.post("/",async (req,res)=>{
    try{
        let values=await groupFunctions.create(req.body.id,req.body);
        if(values.status!=200)
            throw Error(values.msg);

        res.json({
            status:200,
            msg:{
                content:"Grupo creado correctamente",
                type:"info"
            },
            group:values.group,
        });
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:{
                content:"Error al registrar, revisa los datos",
                type:"error"
            },
            group:undefined,
        });
    }
});

/// Route DELETE /group/:id
router.delete("/:id",async (req,res)=>{
    try{
        let values=await groupFunctions.delete(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);

        res.json({
            status:200,
            msg:{
                content:"Grupo borrado correctamente",
                type:"info"
            },
        });
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:{
                content:"Error al borrar el grupo, revisa los datos",
                type:"error"
            },
        });
    }
});


/// Export module
module.exports=router;
