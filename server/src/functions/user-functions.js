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
            console.log(ex);
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
            console.log(ex);
            return {
                status:400,
                user:undefined,
                msg:"Error finding user"
            };
        }
    },

    /// Login
    login:async function(email,password){
        try{

            let user=await User.findOne({
                email:email
            }).exec();

            if(!user)
                throw Error("User not found");

            let result=await bcrypt.compare(password, user.password);
            if(!result)
                throw Error("Login incorrect");

            return {
                status:200,
                user:user,
                msg:"ok"
            };
        }catch(ex){
            console.log(ex);
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
            console.log(ex);
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
            console.log(ex);
            return {
                status:400,
                user:undefined,
                msg:"Error updating user"
            };
        }
    },

    /// Verify
    verify:async function(id){
        try{
            let user=await User.findById(id).exec();
            if(!user)
                throw Error("User not found");
            user.verified=true;
            user.save();

            return {
                status:200,
                user:user,
                msg:`User ${id} account verified`
            };
        }catch(ex){
            console.log(ex);
            return {
                status:400,
                user:undefined,
                msg:"Error verifing account user"
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
            console.log(ex);
            return {
                status:400,
                user:undefined,
                msg:"Error deleting user"
            };
        }
    }
}
