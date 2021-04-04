/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const userFunctions=require("../functions/user-functions");
const emailFunctions=require("../functions/email-functions");

/// Route POST /user/login
router.post("/login",async (req,res)=>{
    const {
        email,
        password
    }=req.body;

    try{
        let values=await userFunctions.login(email,password);
        if(values.status!=200)
            throw Error(values.msg);
        req.session.user=values.user;
        res.redirect("/dashboard");
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:400});
    }
});

/// Route POST /user/register
router.post("/register",async (req,res)=>{
    try{
        let values=await userFunctions.create(req.body);

        if(values.status==200){
            const url=`http://localhost:3000/user/verify/${values.user._id}`;
            await emailFunctions.send({
                to:values.user.email,
                subject:"Verificación de correo",
                html:`
                    <div>
                        <h1>Hola!!!, ${values.user.firstName}</h1>
                        <br/>
                        Selecciona el siguiente link para verificar la cuenta <a href="${url}">${url}<a/>
                    </div>
                `
            });
        }

        res.redirect("/user/pos-register");
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:401});
    }
});

/// Route GET /user/pos-register
router.get("/pos-register",async (req,res)=>{
    res.render("pos-register");
});

/// Route GET /user/verify/:id
router.get("/verify/:id",async (req,res)=>{
    try{
        let values=await userFunctions.verify(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);

        res.render("account-verified");
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:401});
    }
});


/// Export module
module.exports=router;
