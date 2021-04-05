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

/// Create the app
const app=express();
/// Defining the port
const port=process.env.PORT||3000;

/// Defining configurations
app.set("view engine","pug");
app.set("views",path.join(__dirname,"src/views"));
app.use(express.static(path.join(__dirname,"./src/public")));
app.use(express.urlencoded({extended: true}));

/// Register controllers
app.use("/",homeRouter);

/// Register API
app.use("/api/user",userApi);
app.use("/api/object",objectApi);
app.use("/api/room",roomApi);
app.use("/api/similar",similarApi);

/// Starting the server
app.listen(port);
console.log(`Server started at port ${port}`);

module.exports=app;
