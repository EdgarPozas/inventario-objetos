/// Import model
const Room=require("../models/room");
const Position=require("../models/position");
const Objects=require("../models/object");

module.exports={
    /// Get all
    all:async function(){
        try{
            let rooms=await Room.find({});
            return {
                status:200,
                rooms:rooms,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                rooms:[],
                msg:ex
            };
        }
    },

    /// Get by Id
    getById:async function(id){
        try{
            let room=await Room.findById(id).exec();

            if(!room)
                throw Error("Room not found");

            return {
                status:200,
                room:room,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                room:undefined,
                msg:ex
            };
        }
    },

    /// Create
    create:async function(obj){
        try{
            let {
                name,
                description,
                createdBy
            }=obj;
    
            let room=new Room();
            room.name=name;
            room.description=description;
            room.createdBy=createdBy;

            await room.save();

            return {
                status:200,
                room:room,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                room:undefined,
                msg:ex
            };
        }
    },

    /// Update
    update:async function(id,obj){
        try{
            let {
                name,
                description
            }=obj;

            let room=await Room.findById(id).exec();
            if(!room)
                throw Error("Room not found");

            room.name=name;
            room.description=description;

            await room.save();

            return {
                status:200,
                room:room,
                msg:`Room ${id} updated`
            };
        }catch(ex){
            return {
                status:400,
                room:undefined,
                msg:ex
            };
        }
    },

    /// Delete
    delete:async function(id){
        try{
           
            let room=await Room.findById(id).exec();
            if(!room)
                throw Error("Room not found");

            room.active=false;

            await room.save();

            return {
                status:200,
                msg:`Room ${id} deleted`
            };
        }catch(ex){
            return {
                status:400,
                msg:ex
            };
        }
    }
}
