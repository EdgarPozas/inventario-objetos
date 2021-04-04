/// Initialize dependencies
const express=require("express");
const bcrypt=require("bcrypt");
/// Import model
const User=require("../models/user");

module.exports={
    /// Get all
    all:async function(){
        try{
            let users=await User.find({});
            return {
                status:200,
                users:users,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                users:[],
                msg:"Error finding users"
            };
        }
    },

    /// Get by Id
    get:async function(id){
        try{
            let user=await User.findById(id).exec();
            return {
                status:200,
                user:user,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:"Error finding user"
            };
        }
    },

    /// Create
    create:async function(obj){
        let {
            firstName,
            lastName,
            email,
            password,
        }=obj;

        let user=new User();
        user.firstName=firstName;
        user.lastName=lastName;
        user.email=email;
        user.password=await bcrypt.hash(password, 10);

        try{
            await user.save();
            return {
                status:200,
                user:user,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:"Error creating user"
            };
        }
    },

    /// Update
    update:async function(id,obj){
        let {
            firstName,
            lastName,
            email,
            password,
        }=obj;

        try{
            await User.updateOne({
                firstName:firstName,
                lastName:lastName,
                email:email,
                password:password,
            },{
                _id:id
            });
            return {
                status:200,
                user:user,
                msg:`User ${id} updated`
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:"Error updating user"
            };
        }
    },

    /// Delete
    delete:async function(id){
        try{
            await User.deleteOne({
                _id:id
            });
            return {
                status:200,
                user:user,
                msg:`User ${id} deleted`
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:"Error deleting user"
            };
        }
    }
}
