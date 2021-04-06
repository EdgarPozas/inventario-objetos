/// Import model
const Objects=require("../models/object");

module.exports={
    /// Get all
    all:async function(){
        try{
            let objects=await Objects.find({});
            return {
                status:200,
                objects:objects,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                objects:[],
                msg:ex
            };
        }
    },

    /// Get by Id
    getById:async function(id){
        try{
            let object=await Objects.findById(id).exec();

            if(!object)
                throw Error("Objects not found");

            return {
                status:200,
                object:object,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                object:undefined,
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
                functionality,
                tags,
                urlImage,
                urlSound,
                price,
                sharedBy,
                positions,
                createdBy
            }=obj;
    
            let object=new Objects();
            object.name=name;
            object.description=description;
            object.functionality=functionality;
            object.tags=tags;
            object.urlImage=urlImage;
            object.urlSound=urlSound;
            object.price=price;
            object.sharedBy=sharedBy;
            object.positions=positions;
            object.createdBy=createdBy;

            await object.save();

            return {
                status:200,
                object:object,
                msg:"ok"
            };
        }catch(ex){
            return {
                status:400,
                object:undefined,
                msg:ex
            };
        }
    },

    /// Update
    update:async function(id,obj){
        try{
            let {
                name,
                description,
                functionality,
                tags,
                urlImage,
                urlSound,
                price,
                sharedBy,
                positions,
                createdBy
            }=obj;
    
            let object=await Objects.findById(id).exec();
            if(!object)
                throw Error("Objects not found");

            object.name=name;
            object.description=description;
            object.functionality=functionality;
            object.tags=tags;
            object.urlImage=urlImage;
            object.urlSound=urlSound;
            object.price=price;
            object.sharedBy=sharedBy;
            object.positions=positions;
            object.createdBy=createdBy;

            await object.save();

            return {
                status:200,
                object:object,
                msg:`Objects ${id} updated`
            };
        }catch(ex){
            return {
                status:400,
                object:undefined,
                msg:ex
            };
        }
    },

    /// Delete
    delete:async function(id){
        try{
            await Objects.deleteOne({
                _id:id
            });
            return {
                status:200,
                msg:`object ${id} deleted`
            };
        }catch(ex){
            return {
                status:400,
                msg:ex
            };
        }
    }
}
