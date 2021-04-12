/// Initialize dependencies
const faker=require("faker");
let chai = require('chai');
let chaiHttp = require('chai-http');
const expect = require('chai').expect;
let should = chai.should();
/// Server test
chai.use(chaiHttp);

/// Importing the app
const app=require("../index");

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

module.exports={
    createUser:function (){
        return new Promise((resolve,reject)=>{
            let body={
                firstName:faker.name.firstName(),
                lastName:faker.name.lastName(),
                email:faker.internet.email(),
                password:"123",
            };

            chai.request(app)
                .post("/api/user")
                .send(body)
                .end((err,res)=>{
                    if(err)
                        reject(err);
                    resolve(res.body);
                });
        });
    },
    getUserExisting:function (){
        return new Promise((resolve,reject)=>{
            chai.request(app)
                .get("/api/user")
                .end((err,res)=>{
                    if(err)
                        reject(err);
                    
                    let users=res.body.users;
                    let user=users[getRandomInt(0,users.length)];
                    resolve(user);
                });
        });
    },
    createRoom:function (user){
        return new Promise((resolve,reject)=>{
            let body={
                name:faker.commerce.product(),
                description:faker.commerce.productDescription(),
                id:user._id
            };

            chai.request(app)
                .post("/api/room")
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body)
                .end((err,res)=>{
                    if(err)
                        reject(err);
                    resolve(res.body);
                });
        });
    },
    getRoomExisting:function (){
        return new Promise((resolve,reject)=>{
            chai.request(app)
                .get("/api/room")
                .end((err,res)=>{
                    if(err)
                        reject(err);
                    
                    let rooms=res.body.rooms;
                    let room=rooms[getRandomInt(0,rooms.length)];
                    resolve(room);
                });
        });
    },
    createObject:function (users,positions){
        return new Promise((resolve,reject)=>{
            let body={
                name:faker.commerce.product(),
                description:faker.commerce.productDescription(),
                function:faker.commerce.department(),
                tags:[],
                urlImage:faker.internet.url(),
                urlSound:faker.internet.url(),
                price:getRandomInt(100,1000),
                sharedBy:[],
                position:positions[getRandomInt(0,positions.length)]._id,
                createdBy:users[getRandomInt(0,users.length)]._id,
            };

            for(let i=0;i<getRandomInt(1,10);i++){
                body.tags.push("tag"+i);
                body.sharedBy.push(users[getRandomInt(0,users.length)]._id);
            } 

            chai.request(app)
                .post("/api/object")
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body)
                .end((err,res)=>{
                    if(err)
                        reject(err);
                    resolve(res.body);
                });
        });
    },
    getObjectExisting:function (){
        return new Promise((resolve,reject)=>{
            chai.request(app)
                .get("/api/object")
                .end((err,res)=>{
                    if(err)
                        reject(err);
                    
                    let objects=res.body.objects;
                    let object_=objects[getRandomInt(0,objects.length)];
                    resolve(object_);
                });
        });
    },
    createPosition:function (user,room){
        return new Promise((resolve,reject)=>{
            let body={
                latitude:faker.address.latitude(),
                longitude:faker.address.longitude(),
                altitude:faker.address.latitude(),
                room:room._id,
                createdBy:user._id
            };

            chai.request(app)
                .post("/api/position")
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body)
                .end((err,res)=>{
                    if(err)
                        reject(err);
                    resolve(res.body);
                });
        });
    },
}