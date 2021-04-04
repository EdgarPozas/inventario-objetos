/// Initialize dependencies
require("./src/models/database");
const express=require("express");
const bodyParser=require("body-parser");
const session = require('express-session');
const path=require("path");

/// Import routers
const homeRouter=require("./src/router/home-router");
const userRouter=require("./src/router/user-router");
const groupRouter=require("./src/router/group-router");

/// Import API
const userApi=require("./src/api/user-api");
const groupApi=require("./src/api/group-api");

/// Create the app
const app=express();
/// Defining the port
const port=process.env.PORT||3000;

/// Defining configurations
app.set("view engine","pug");
app.set("views",path.join(__dirname,"src/views"));
app.use(express.static(path.join(__dirname,"./src/public")));

app.use(session({
  secret: 'keyboard cat',
  resave: false,
  saveUninitialized: true,
  cookie: { secure: true }
}));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.text());
app.use(bodyParser.json({ type: 'application/json'}));

/// Register controllers
app.use("/",homeRouter);
app.use("/user",userRouter);
app.use("/group",groupRouter);

/// Register API
app.use("/api/user",userApi);
app.use("/api/group",groupApi);

/// Starting the server
app.listen(port);
console.log(`Server started at port ${port}`);

module.exports=app;
