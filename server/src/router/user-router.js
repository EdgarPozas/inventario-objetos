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
        res.render("errors",{error:"Error al ingresar, revisa los datos"});
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

        res.render("messages",{message:"Revisa tu correo y da click en el link para validar tu correo y poder iniciar sesión"});
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:"Error al registrar, revisa los datos"});
    }
});

/// Route GET /user/verify/:id
router.get("/verify/:id",async (req,res)=>{
    try{
        let values=await userFunctions.verify(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);

        res.render("messages",{message:"Correo verificado, ahora podrás iniciar sesión"});
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:"Error al verificar el correo"});
    }
});

/// Route POST /user/recovery
router.post("/recovery",async (req,res)=>{
    try{
        let values=await userFunctions.getByEmail(req.body.email);
        if(values.status!=200)
            throw Error(values.msg);

        const url=`http://localhost:3000/user/recovery/${values.user._id}`;
        await emailFunctions.send({
            to:values.user.email,
            subject:"Recuperar contraseña",
            html:`
                <div>
                    <h1>Hola, ${values.user.firstName}</h1>
                    <br/>
                    Selecciona el siguiente link para cambiar tu contraseña <a href="${url}">${url}<a/>
                </div>
            `
        });

        res.render("messages",{message:"Revisa tu correo y da click en el link para recuperar recuperar tu cuenta"});
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:"Error al recuperar la contraseña, revisa los datos "});
    }
});

/// Route GET /user/recovery/:id
router.get("/recovery/:id",async (req,res)=>{
    try{
        let values=await userFunctions.getById(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);
        res.render("recovery-password",{user:values.user});
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:"Error al recuperar el usuario"});
    }
});

/// Route PUT /user/change-password/:id
router.put("/change-password/:id",async (req,res)=>{
    try{
        let values=await userFunctions.changePassword(req.params.id,req.body);
        if(values.status!=200)
            throw Error(values.msg);

        res.render("messages",{message:"Contraseña actualizada correctamente, ahora trata de iniciar sesión"});
    }catch(ex){
        console.log(ex);
        res.render("errors",{error:"Error al recuperar la contraseña, revisa los datos "});
    }
});


/// Export module
module.exports=router;
