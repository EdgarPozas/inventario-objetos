/// Initialize dependencies
const express=require("express");
const app=express();
/// Defining a router
const router=express.Router();
/// Import function
const emailFunctions=require("../functions/email-functions");
/// Import function
const userFunctions=require("../functions/user-functions");

/// Route POST /api/email/recovery
/// Sends an email to verify yout account
router.post("/recovery",async (req,res)=>{
    try{
        let {
            email
        }=req.body;

        let userValues=await userFunctions.getByEmail(email);
        if(userValues.status!=200)
            throw Error(userValues.msg)

        let url=`https://inventario-objetos.herokuapp.com/users/recovery/${userValues.user._id}`
        let values=await emailFunctions.send({
            to:email,
            subject:"Recuperar contraseña | Inventario - Objetos",
            html:`
            <h1>¡Hola ${userValues.user.firstName} ${userValues.user.lastName}!</h1>
            <p>Hemos recibido la solicitud de recuperar tu contraseña, si tu fuiste quien lo solicitó da selecciona el siguiente enlace para ir a restablecer tu contraseña <a href="${url}">${url}</p>
            `
        });
        res.json(values);
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
