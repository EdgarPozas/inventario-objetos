/// Export module
module.exports=function(req,res,next){
    if(req.session.user)
        next();
    res.redirect("/");
}
