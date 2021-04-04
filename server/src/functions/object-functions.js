/// Initialize dependencies
const express=require("express");
const bcrypt=require("bcrypt");
/// Import model
const Object=require("../models/object");

module.exports={
    /// Get all
    all:async function(){
        try{
            let objects=await Object.find({});
            return {
                status:200,
                objects:objects,
                msg:"ok"
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                objects:[],
                msg:"Error finding objects"
            };
        }
    },

    /// Get by Id
    getById:async function(id){
        try{
            let object=await Object.findById(id).exec();
            return {
                status:200,
                object:object,
                msg:"ok"
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                object:undefined,
                msg:"Error finding object"
            };
        }
    },

    /// Create
    create:async function(owner,obj){
        let {
            name,
            price,
            latitude,
            longitude
        }=obj;

        let object=new Object();
        object.name=name;
        object.price=price;
        object.latitude=latitude;
        object.longitude=longitude;
        object.owner=owner._id;

        try{
            await object.save();
            return {
                status:200,
                object:object,
                msg:"ok"
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                object:undefined,
                msg:"Error creating object"
            };
        }
    },

    /// Update
    update:async function(id,obj){
        let {
            name,
            price,
            latitude,
            longitude
        }=obj;

        try{

            let object=await Object.findById(id).exec();
            if(!object)
                throw Error("Object not found");

            object.name=name;
            object.price=price;
            object.latitude=latitude;
            object.longitude=longitude;
            object.owner=owner._id;
            await object.save();

            return {
                status:200,
                object:object,
                msg:`Object ${id} updated`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                object:undefined,
                msg:"Error updating object"
            };
        }
    },

    /// Delete
    delete:async function(id){
        try{
            await Object.deleteOne({
                _id:id
            });
            return {
                status:200,
                object:object,
                msg:`Object ${id} deleted`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                object:undefined,
                msg:"Error deleting object"
            };
        }
    }
}
