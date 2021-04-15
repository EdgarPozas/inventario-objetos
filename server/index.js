/// Initialize dependencies
require("./src/models/database");
const express=require("express");
const path=require("path");

/// Import routers
const homeRouter=require("./src/routers/home-router");

/// Import API
const userApi=require("./src/api/user-api");
const objectApi=require("./src/api/object-api");
const roomApi=require("./src/api/room-api");
const similarApi=require("./src/api/similar-api");
const emailApi=require("./src/api/email-api");
const positionApi=require("./src/api/position-api");
const fileApi=require("./src/api/file-api");

/// Create the app
const app=express();
/// Defining the port
const port=process.env.PORT||3000;

/// Defining configurations
app.set("view engine","pug");
app.set("views",path.join(__dirname,"src/views"));
app.use(express.static(path.join(__dirname,"./src/public")));
app.use(express.urlencoded({extended: true}));
app.use(express.json())

/// Register controllers
app.use("/",homeRouter);

/// Register API
app.use("/api/user",userApi);
app.use("/api/object",objectApi);
app.use("/api/room",roomApi);
app.use("/api/similar",similarApi);
app.use("/api/email",emailApi);
app.use("/api/position",positionApi);
app.use("/api/file",fileApi);

/// Route GET /about
app.use((req,res,next)=>{
    res.render("notfound");
});

/// Starting the server
app.listen(port);
console.log(`Server started at port ${port}`);

module.exports=app;
