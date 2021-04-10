/// Initialize dependencies
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
                msg:ex
            };
        }
    },

    /// Get by Id
    getById:async function(id){
        try{
            let user=await User.findById(id).exec();

            if(!user)
                throw Error("User not found");

            return {
                status:200,
                user:user,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:ex
            };
        }
    },

    /// Get by Email
    getByEmail:async function(email){
        try{
            let user=await User.findOne({
                email:email
            }).exec();

            if(!user)
                throw Error("User not found");

            return {
                status:200,
                user:user,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:ex
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
            return {
                status:400,
                user:undefined,
                msg:ex
            };
        }
    },

    /// Create
    create:async function(obj){
        try{
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
                msg:ex
            };
        }
    },

    /// Update
    update:async function(id,obj){
        try{
            let {
                firstName,
                lastName,
                email,
                password,
                active
            }=obj;

            let user=await User.findById(id).exec();
            if(!user)
                throw Error("User not found");

            user.verified=email==user.email;
            user.firstName=firstName;
            user.lastName=lastName;
            user.email=email;
            user.active=active;
            let result=await bcrypt.compare(password, user.password);
            if(!result){
                user.password=await bcrypt.hash(password, 10);
            }
            
            await user.save();

            return {
                status:200,
                user:user,
                msg:`User ${id} updated`
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:ex
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
            await user.save();

            return {
                status:200,
                user:user,
                msg:`User ${id} account verified`
            };
        }catch(ex){
            return {
                status:400,
                user:undefined,
                msg:ex
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
                msg:`User ${id} deleted`
            };
        }catch(ex){
            return {
                status:400,
                msg:ex
            };
        }
    }
}
