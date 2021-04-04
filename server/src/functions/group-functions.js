/// Initialize dependencies
const express=require("express");
const bcrypt=require("bcrypt");
/// Import models
const Group=require("../models/group");
const User=require("../models/user");

module.exports={
    /// Get all
    all:async function(id){
        try{
            let groups=await Group.find({});
            return {
                status:200,
                groups:groups,
                msg:"ok"
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                groups:[],
                msg:"Error finding groups"
            };
        }
    },

    /// Get by Id
    getById:async function(id){
        try{
            let group=await Group.findById(id).exec();
            return {
                status:200,
                group:group,
                msg:"ok"
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                group:undefined,
                msg:"Error finding group"
            };
        }
    },

    /// Create
    create:async function(id,obj){
        let {
            name,
        }=obj;

        let group=new Group();
        group.name=name;
        group.owner=id;
        group.users.push(id);

        try{
            let user=await User.findById(id).exec();
            if(!user)
                throw Error("User not found");

            await group.save();

            user.groups.push(group._id);
            await user.save();

            return {
                status:200,
                group:group,
                msg:"ok"
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                group:undefined,
                msg:"Error creating group"
            };
        }
    },

    /// Update
    update:async function(id,obj){
        let {
            name,
        }=obj;

        try{

            let group=await Group.findById(id).exec();
            if(!group)
                throw Error("Group not found");

            group.name=name;
            await group.save();

            return {
                status:200,
                group:group,
                msg:`Group ${id} updated`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                group:undefined,
                msg:"Error updating group"
            };
        }
    },

    /// Add users
    addUsers:async function(id,users){

        try{
            let group=await Group.findById(id).exec();
            if(!group)
                throw Error("Group not found");

            for (var i = 0; i < users.length; i++) {
                group.users.push(users[i]._id);
            }

            await group.save();

            return {
                status:200,
                group:group,
                msg:`Users added to group ${id}`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                group:undefined,
                msg:"Error updating users from group"
            };
        }
    },

    /// Add users
    removeUsers:async function(id,users){

        try{
            let group=await Group.findById(id).exec();
            if(!group)
                throw Error("Group not found");

            for (var i = 0; i < users.length; i++) {
                group.users.pull(users[i]._id);
            }

            await group.save();

            return {
                status:200,
                group:group,
                msg:`Users added to group ${id}`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                group:undefined,
                msg:"Error removing users from group"
            };
        }
    },

    /// Add objects
    addObjects:async function(id,objects){

        try{
            let group=await Group.findById(id).exec();
            if(!group)
                throw Error("Group not found");

            for (var i = 0; i < objects.length; i++) {
                group.objects.push(objects[i]._id);
            }

            await group.save();

            return {
                status:200,
                group:group,
                msg:`Objects added to group ${id}`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                group:undefined,
                msg:"Error adding objects to group"
            };
        }
    },

    /// Add objects
    removeObjects:async function(id,objects){

        try{
            let group=await Group.findById(id).exec();
            if(!group)
                throw Error("Group not found");

            for (var i = 0; i < objects.length; i++) {
                group.objects.pull(users[i]._id);
            }

            await group.save();

            return {
                status:200,
                group:group,
                msg:`Objects remove of group ${id}`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                group:undefined,
                msg:"Error removing objects group"
            };
        }
    },

    /// Delete
    delete:async function(id){
        try{
            await Group.deleteOne({
                _id:id
            });
            return {
                status:200,
                msg:`Group ${id} deleted`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                msg:"Error deleting group"
            };
        }
    }
}
