/// Import model
const Position=require("../models/position");

module.exports={
    /// Get all
    all:async function(){
        try{
            let positions=await Position.find({});
            return {
                status:200,
                positions:positions,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                positions:[],
                msg:ex
            };
        }
    },

    /// Get by Id
    getById:async function(id){
        try{
            let position=await Position.findById(id).exec();

            if(!position)
                throw Error("Position not found");

            return {
                status:200,
                position:position,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                position:undefined,
                msg:ex
            };
        }
    },

    /// Create
    create:async function(obj){
        try{
            let {
                latitude,
                longitude,
                altitude,
                room,
                createdBy
            }=obj;
    
            let position=new Position();
            position.latitude=latitude;
            position.longitude=longitude;
            position.altitude=altitude;
            position.room=room;
            position.createdBy=createdBy;

            await position.save();

            return {
                status:200,
                position:position,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                position:undefined,
                msg:ex
            };
        }
    },

    /// Update
    update:async function(id,obj){
        try{
            let {
                latitude,
                longitude,
                altitude,
                room
            }=obj;

            let position=await Position.findById(id).exec();
            if(!position)
                throw Error("Position not found");

            position.latitude=latitude;
            position.longitude=longitude;
            position.altitude=altitude;
            position.room=room;

            await position.save();

            return {
                status:200,
                position:position,
                msg:`Position ${id} updated`
            };
        }catch(ex){
            return {
                status:400,
                position:undefined,
                msg:ex
            };
        }
    },

    /// Delete
    delete:async function(id){
        try{
            await Position.deleteOne({
                _id:id
            });
            return {
                status:200,
                msg:`Position ${id} deleted`
            };
        }catch(ex){
            return {
                status:400,
                msg:ex
            };
        }
    }
}
