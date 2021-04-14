/// Initialize dependencies
const express=require("express");
const app=express();
const bcrypt=require("bcrypt");

/// Defining a router
const router=express.Router();
/// Models
const User=require("../models/user");
const Objects=require("../models/object");
const Room=require("../models/room");
const Position=require("../models/position");
/// Functions
const userFunctions=require("../functions/user-functions");
const {createPDF}=require("./utils");

/// Route GET /
router.get("/",async (req,res)=>{
    res.redirect("/objects");
});

/// Route GET /users
router.get("/users",async (req,res)=>{
    try{
        let users=await User.find({});
        res.render("users",{users});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /users/:id
router.get("/users/:id",async (req,res)=>{
    try{
        let {id}=req.params;
        let user=await User.findById(id);
        let objects=await Objects.find({createdBy:id}).populate("sharedBy").populate("positions");
        let rooms=await Room.find({createdBy:id});
        res.render("users-individual",{user,objects,rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /objects
router.get("/objects",async (req,res)=>{
    try{
        let objects=await Objects.find({}).populate("createdBy").populate("sharedBy").populate("positions");
        let users=await User.find({});
        let rooms=await Room.find({});
        res.render("objects",{objects,users,rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /objects/:id
router.get("/objects/:id",async (req,res)=>{
    try{
        let {id}=req.params;
        let object=await Objects.findById({_id:id}).populate("createdBy").populate("sharedBy").populate("positions");
        let users=await User.find({});
        let rooms=await Room.find({});
        res.render("objects-individual",{object,users,rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /rooms
router.get("/rooms",async (req,res)=>{
    try{
        let rooms=await Room.find({});
        res.render("rooms",{rooms});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /rooms/:id
router.get("/rooms/:id",async (req,res)=>{
    try{
        let {id}=req.params;
        let room=await Room.findById({_id:id}).populate("createdBy");
        let positions=await Position.find({room:id},"_id");
        let arrayPositions=[];
        for(let i=0;i<positions.length;i++){
            arrayPositions.push(positions[i]._id);
        }
        let objects=await Objects.aggregate([
            { $project: { lastPosition: { $arrayElemAt: ['$positions', -1] } } },
            { $match: { lastPosition: {$in:arrayPositions} } }
        ]);
        let arrayObjects=[];
        for(let i=0;i<objects.length;i++){
            arrayObjects.push(objects[i]._id+"");
        }
        let objectsAux=await Objects.find({_id:{$in:arrayObjects}}).populate("sharedBy").populate("positions");
        res.render("rooms-individual",{room,objects:objectsAux});
    }catch(ex){
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /user/verify/:id
router.get("/users/verify/:id",async (req,res)=>{
    try{
        let values=await userFunctions.verify(req.params.id);
        if(values.status!=200)
            throw Error(values.msg);
        res.render("verify");
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
});

/// Route GET /users/verify/:id
router.get("/users/recovery/:id",async (req,res)=>{
    try{
        let user=await User.findById(req.params.id);
        res.render("recovery",{user});
    }catch(ex){
        console.log(ex);
        res.json({
            status:400,
            msg:ex
        });
    }
    
});

/// Route POST /users/recovery/:id
router.post("/users/recovery/:id",async (req,res)=>{
    try{
        let {
            email,
            password,
        }=req.body;
        
        let user=await User.findOne({
            _id:req.params.id,
            email:email
        }).exec();

        if(!user)
            throw Error("User not found");

        user.password=await bcrypt.hash(password, 10);
        await user.save();

        res.render("recovery", {
            status:200,
            user:user,
        });
    }catch(ex){
        console.log(ex);
        let user=await User.findById(req.params.id);
        res.render("recovery", {
            status:400,
            user:user,
        });
    }
});

/// Route POST /report
router.post("/report/:type",async (req,res)=>{
    
    let {
        filters,
        data
    }=req.body;
    let type=req.params.type;

    let values=[]
    let template="";

    if(type=="user"){
        template="report-user.html";

        values=[
            {
                key:"#NAME#",
                value:"Reporte de todos los usuarios"
            },
            {
                key:"#HOUR#",
                value:new Date()
            },
            {
                key:"#LEYEND#",
                value:"Cantidad de registros: <strong>"+data[0].users.length+"</strong>"
            },
        ];

        let filtersBody="";
        for(let i=0;i<filters.length;i++){
            filtersBody+="<tr>"
            filtersBody+="<td>"+filters[i].name+"</td>";
            filtersBody+="<td>"+(filters[i].filter==""?"N/A":filters[i].filter)+"</td>";
            filtersBody+="</tr>"
        }

        values.push({
            key:"#FILTERSTABLEBODY#",
            value:filtersBody
        });

        
        let tableBody="";
    
        for(let i=0;i<data[0].users.length;i++){
            tableBody+="<tr>"
            tableBody+="<td>"+(i+1)+"</td>";
            tableBody+="<td>"+data[0].users[i].firstName+"</td>";
            tableBody+="<td>"+data[0].users[i].lastName+"</td>";
            tableBody+="<td>"+data[0].users[i].email+"</td>";
            tableBody+="<td>"+data[0].users[i].active+"</td>";
            tableBody+="<td>"+data[0].users[i].verified+"</td>";
            tableBody+="<td>"+data[0].users[i].createdAt+"</td>";
            tableBody+="</tr>"
        }

        values.push({
            key:"#TABLEBODY#",
            value:tableBody
        });

    } else if(type=="user-individual"){
        template="report-user-individual.html";

        values=[
            {
                key:"#NAME#",
                value:"Reporte del miembro - "+data[0].user.firstName+" "+data[0].user.lastName
            },
            {
                key:"#HOUR#",
                value:new Date()
            },
        ];
        
        let tableBody="";
        tableBody+="<tr>"
        tableBody+="<td>"+data[0].user.firstName+"</td>";
        tableBody+="<td>"+data[0].user.lastName+"</td>";
        tableBody+="<td>"+data[0].user.email+"</td>";
        tableBody+="<td>"+data[0].user.active+"</td>";
        tableBody+="<td>"+data[0].user.verified+"</td>";
        tableBody+="<td>"+data[0].user.createdAt+"</td>";
        tableBody+="</tr>"
       
        values.push({
            key:"#TABLEUSERBODY#",
            value:tableBody
        });

        let tableBodyObjects="";
        for(let i=0;i<data[1].objects.length;i++){
            tableBodyObjects+="<tr>"
            tableBodyObjects+="<td>"+(i+1)+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].name+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].description+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].functionality+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].price+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].tags+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].sharedBy.map(x=>x.firstName+" "+x.lastName)+"</td>";
            let room=data[1].objects[i].positions.reverse()[0].room;
            let roomAux=data[2].rooms.filter(x=>x._id==room+"")[0];
            tableBodyObjects+="<td>"+(roomAux?roomAux.name:"N/A")+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].createdAt+"</td>";
            tableBodyObjects+="</tr>"
        }

        values.push({
            key:"#TABLEOBJECTSBODY#",
            value:tableBodyObjects
        });

        let tableBodyRoom="";
        for(let i=0;i<data[2].rooms.length;i++){
            tableBodyRoom+="<tr>"
            tableBodyRoom+="<td>"+(i+1)+"</td>";
            tableBodyRoom+="<td>"+data[2].rooms[i].name+"</td>";
            tableBodyRoom+="<td>"+data[2].rooms[i].description+"</td>";
            tableBodyRoom+="<td>"+data[2].rooms[i].active+"</td>";
            tableBodyRoom+="<td>"+data[2].rooms[i].createdAt+"</td>";
            tableBodyRoom+="</tr>"
        }

        values.push({
            key:"#TABLEROOMSBODY#",
            value:tableBodyRoom
        });
    } else if(type=="room"){
        template="report-room.html";

        values=[
            {
                key:"#NAME#",
                value:"Reporte de todos los espacios"
            },
            {
                key:"#HOUR#",
                value:new Date()
            },
            {
                key:"#LEYEND#",
                value:"Cantidad de registros: <strong>"+data[0].rooms.length+"</strong>"
            },
        ];

        let filtersBody="";
        for(let i=0;i<filters.length;i++){
            filtersBody+="<tr>"
            filtersBody+="<td>"+filters[i].name+"</td>";
            filtersBody+="<td>"+(filters[i].filter==""?"N/A":filters[i].filter)+"</td>";
            filtersBody+="</tr>"
        }

        values.push({
            key:"#FILTERSTABLEBODY#",
            value:filtersBody
        });

        
        let tableBody="";
    
        for(let i=0;i<data[0].rooms.length;i++){
            tableBody+="<tr>"
            tableBody+="<td>"+(i+1)+"</td>";
            tableBody+="<td>"+data[0].rooms[i].name+"</td>";
            tableBody+="<td>"+data[0].rooms[i].description+"</td>";
            tableBody+="<td>"+data[0].rooms[i].active+"</td>";
            tableBody+="<td>"+data[0].rooms[i].createdAt+"</td>";
            tableBody+="</tr>"
        }

        values.push({
            key:"#TABLEBODY#",
            value:tableBody
        });

    } else if(type=="room-individual"){
        template="report-room-individual.html";

        values=[
            {
                key:"#NAME#",
                value:"Reporte del espacio - "+data[0].room.name
            },
            {
                key:"#HOUR#",
                value:new Date()
            },
        ];
        
        let tableBody="";
        tableBody+="<tr>"
        tableBody+="<td>"+data[0].room.name+"</td>";
        tableBody+="<td>"+data[0].room.description+"</td>";
        tableBody+="<td>"+data[0].room.active+"</td>";
        tableBody+="<td>"+data[0].room.createdAt+"</td>";
        tableBody+="</tr>"
       
        values.push({
            key:"#TABLEUSERBODY#",
            value:tableBody
        });

        let tableBodyObjects="";
        for(let i=0;i<data[1].objects.length;i++){
            tableBodyObjects+="<tr>"
            tableBodyObjects+="<td>"+(i+1)+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].name+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].description+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].functionality+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].price+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].tags+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].sharedBy.map(x=>x.firstName+" "+x.lastName)+"</td>";
            let room=data[1].objects[i].positions.reverse()[0].room;
            let roomAux=data[0].room;
            tableBodyObjects+="<td>"+(roomAux?roomAux.name:"N/A")+"</td>";
            tableBodyObjects+="<td>"+data[1].objects[i].createdAt+"</td>";
            tableBodyObjects+="</tr>"
        }

        values.push({
            key:"#TABLEOBJECTSBODY#",
            value:tableBodyObjects
        });
    } else if(type=="object"){
        template="report-object.html";

        values=[
            {
                key:"#NAME#",
                value:"Reporte de todos los objetos"
            },
            {
                key:"#HOUR#",
                value:new Date()
            },
            {
                key:"#LEYEND#",
                value:"Cantidad de registros: <strong>"+data[0].objects.length+"</strong>"
            },
        ];

        let filtersBody="";
        for(let i=0;i<filters.length;i++){
            filtersBody+="<tr>"
            filtersBody+="<td>"+filters[i].name+"</td>";
            filtersBody+="<td>"+(filters[i].filter==""?"N/A":filters[i].filter)+"</td>";
            filtersBody+="</tr>"
        }

        values.push({
            key:"#FILTERSTABLEBODY#",
            value:filtersBody
        });

        
        let tableBody="";
    
        for(let i=0;i<data[0].objects.length;i++){
            tableBody+="<tr>"
            tableBody+="<td>"+(i+1)+"</td>";
            tableBody+="<td>"+data[0].objects[i].name+"</td>";
            tableBody+="<td>"+data[0].objects[i].description+"</td>";
            tableBody+="<td>"+data[0].objects[i].functionality+"</td>";
            tableBody+="<td>"+data[0].objects[i].price+"</td>";
            tableBody+="<td>"+data[0].objects[i].tags+"</td>";
            tableBody+="<td>"+data[0].objects[i].sharedBy.map(x=>x.firstName+" "+x.lastName)+"</td>";
            let room=data[0].objects[i].positions.reverse()[0].room;
            let roomAux=data[1].rooms.filter(x=>x._id==room+"")[0];
            tableBody+="<td>"+(roomAux?roomAux.name:"N/A")+"</td>";
            tableBody+="<td>"+data[0].objects[i].createdAt+"</td>";
            tableBody+="</tr>"
        }

        values.push({
            key:"#TABLEBODY#",
            value:tableBody
        });

    } else if(type=="object-individual"){
        template="report-object-individual.html";

        values=[
            {
                key:"#NAME#",
                value:"Reporte del objeto - "+data[0].object.name
            },
            {
                key:"#HOUR#",
                value:new Date()
            },
        ];
        
        let tableBody="";
        tableBody+="<tr>"
        tableBody+="<td>"+data[0].object.name+"</td>";
        tableBody+="<td>"+data[0].object.description+"</td>";
        tableBody+="<td>"+data[0].object.functionality+"</td>";
        tableBody+="<td>"+data[0].object.price+"</td>";
        tableBody+="<td>"+data[0].object.tags+"</td>";
        tableBody+="<td>"+data[0].object.sharedBy.map(x=>x.firstName+" "+x.lastName)+"</td>";
        let room=data[0].object.positions.reverse()[0].room;
        let roomAux=data[1].rooms.filter(x=>x._id==room+"")[0];
        tableBody+="<td>"+(roomAux?roomAux.name:"N/A")+"</td>";
        tableBody+="<td>"+data[0].object.createdAt+"</td>";
        tableBody+="</tr>"
       
        values.push({
            key:"#TABLEUSERBODY#",
            value:tableBody
        });

        let tableBodyPositions="";
        let size=data[0].object.positions.length;
        for(let i=0;i<size;i++){
            tableBodyPositions+="<tr>"
            tableBodyPositions+="<td>"+(size-i)+"</td>";
            let position=data[0].object.positions[i]
            let roomAux=data[1].rooms.filter(x=>x._id==position.room+"")[0];
            tableBodyPositions+="<td>"+(roomAux?roomAux.name:"N/A")+"</td>";
            let user=data[2].users.filter(x=>x._id==position.createdBy+"")[0];
            tableBodyPositions+="<td>"+(user.firstName+" "+user.lastName)+"</td>";
            tableBodyPositions+="<td>"+position.createdAt+"</td>";
            tableBodyPositions+="</tr>"
        }

        values.push({
            key:"#TABLEPOSITIONSBODY#",
            value:tableBodyPositions
        });

        let amounts=[];
        let days=[1,7,30,365];
        const dates=(date)=> new Date(new Date()-new Date(date))

        for(let e=0;e<days.length;e++){
            let amount=data[0].object.positions.filter(x=>dates(x.createdAt).getDate()<days[e]).length;
            amounts.push(amount);
        }
       
        let tableBodyMoved="";
        tableBodyMoved+="<tr>"
        tableBodyMoved+="<td>Día</td>";
        tableBodyMoved+="<td>"+amounts[0]+"</td>";
        tableBodyMoved+="</tr>"
        tableBodyMoved+="<tr>"
        tableBodyMoved+="<td>Semana</td>";
        tableBodyMoved+="<td>"+amounts[1]+"</td>";
        tableBodyMoved+="</tr>"
        tableBodyMoved+="<tr>"
        tableBodyMoved+="<td>Mes</td>";
        tableBodyMoved+="<td>"+amounts[2]+"</td>";
        tableBodyMoved+="</tr>"
        tableBodyMoved+="<tr>"
        tableBodyMoved+="<td>Año</td>";
        tableBodyMoved+="<td>"+amounts[3]+"</td>";
        tableBodyMoved+="</tr>"
       
        values.push({
            key:"#TABLEMOVEDSBODY#",
            value:tableBodyMoved
        });
    } 
    
    
    createPDF(res,values,template);
});


/// Route GET /about
router.use((req,res,next)=>{
    res.render("notfound");
});


/// Export module
module.exports=router;
