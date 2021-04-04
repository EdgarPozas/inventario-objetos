/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const userFunctions=require("../functions/user-functions");
const emailFunctions=require("../functions/email-functions");
/// Import middleware
const loginMiddleware=require("../middlewares/login-middleware");

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
        res.redirect("/group");
    }catch(ex){
        console.log(ex);
        res.render("home",{
            msg:{
                content:"Error al ingresar, revisa los datos",
                type:"error"
            }
        });
    }
});

/// Route POST /user/register
router.post("/register",async (req,res)=>{
    try{
        let values=await userFunctions.create(req.body);
        if(values.status!=200)
            throw Error(values.msg);

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

        res.render("home",{
            msg:{
                content:"Revisa tu correo y da click en el link para validar tu correo y poder iniciar sesión",
                type:"info"
            }
        });
    }catch(ex){
        console.log(ex);
        res.render("home",{
            msg:{
                content:"Error al registrar, revisa los datos",
                type:"error"
            }
        });
    }
});

/// Route GET /user/verify/:id
router.get("/verify/:id",async (req,res)=>{
    try{
        let values=await userFunctions.verify(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);

        res.render("home",{
            msg:{
                content:"Correo verificado, ahora podrás iniciar sesión",
                type:"info"
            }
        });
    }catch(ex){
        console.log(ex);
        res.render("home",{
            msg:{
                content:"Error al verificar el correo",
                type:"error"
            }
        });
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

        res.render("home",{
            msg:{
                content:"Revisa tu correo y da click en el link para recuperar recuperar tu cuenta",
                type:"info"
            }
        });
    }catch(ex){
        console.log(ex);
        res.render("home",{
            msg:{
                content:"Error al recuperar la contraseña, revisa los datos ",
                type:"error"
            }
        });
    }
});

/// Route GET /user/recovery/:id
router.get("/recovery/:id",async (req,res)=>{
    try{
        let values=await userFunctions.getById(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);

        res.render("recovery-password",{userRecovery:values.user});
    }catch(ex){
        console.log(ex);
        res.render("home",{
            msg:{
                content:"Error al recuperar el usuario",
                type:"error"
            }
        });
    }
});

/// Route PUT /user/change-password/:id
router.put("/change-password/:id",async (req,res)=>{
    try{
        let values=await userFunctions.changePassword(req.params.id,req.body);
        if(values.status!=200)
            throw Error(values.msg);

        res.render("home",{
            msg:{
                content:"Contraseña actualizada correctamente, ahora trata de iniciar sesión",
                type:"info"
            }
        });
    }catch(ex){
        console.log(ex);
        res.render("home",{
            msg:{
                content:"Error al recuperar la contraseña, revisa los datos ",
                type:"error"
            }
        });
    }
});

/// Route GET /user/profile
router.get("/profile",async (req,res)=>{
    let values=await userFunctions.login("edgar730a@gmail.com","1");
    req.session.user=values.user;
    res.render("profile",{
        user:req.session.user
    });
});

/// Route PUT /user/:id
router.put("/:id",async (req,res)=>{
    try{

        let values=await userFunctions.update(req.params.id,req.body);
        if(values.status!=200)
            throw Error(values.msg);

        let msg="Información actualizada correctamente";
        if(!values.user.verified){
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
            msg="Información actualizada correctamente, hemos detectado que cambiaste tu correo se te envío un correo para verificarlo, si no lo haces la próxima vez que quieras iniciar sesión no podrás ingresar";
        }

        res.render("profile",{
            user:req.session.user,
            msg:{
                content:msg,
                type:"info"
            }
        });
    }catch(ex){
        console.log(ex);
        res.render("profile",{
            msg:{
                content:"Error al actualizar el usuario, revisa los datos ",
                type:"error"
            }
        });
    }
});

/// Export module
module.exports=router;
