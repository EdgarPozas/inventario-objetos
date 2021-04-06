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

/// Import object
const Objects=require("../src/models/object");

/// Import utils
const {createUser,createRoom,createObject}=require("./utils");

/// Clean object
beforeEach(async function() {
    await Objects.deleteMany({})
});

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min)) + min;
}

/// Creating suit test
describe("Objects",()=>{

    it("GET /api/object show all objects",async ()=>{
        try{
            let res=await chai.request(app).get("/api/object");
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
            res.body.objects.should.be.a("array");
        }catch(ex){
            console.log(ex)
        }
    });

    it("GET /api/object/:id show object by id",async ()=>{
        try{
            let users=[];
            let rooms=[];
            for(let i=0;i<5;i++){
                let user=await createUser();
                users.push(user.user);
                let room=await createRoom(user.user);
                rooms.push(room.room);
            }
            let object=await createObject(users,rooms);
            let res=await chai.request(app).get(`/api/object/id/${object.object._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });

    it("POST /api/object create object",async ()=>{    
        try{
            let users=[];
            let rooms=[];
            for(let i=0;i<5;i++){
                let user=await createUser();
                users.push(user.user);
                let room=await createRoom(user.user);
                rooms.push(room.room);
            }

            let body={
                name:faker.commerce.product(),
                description:faker.commerce.productDescription(),
                function:faker.commerce.department(),
                tags:[],
                urlImage:faker.internet.url(),
                urlSound:faker.internet.url(),
                price:getRandomInt(100,1000),
                sharedBy:users,
                positions:[],
                createdBy:users[getRandomInt(0,users.length)],
            };

            for(let i=0;i<getRandomInt(1,10);i++){
                body.tags.push("tag"+i);
            } 

            for(let i=0;i<getRandomInt(1,10);i++){
                body.positions.push({
                    latitude:faker.datatype.float(),
                    longitude:faker.datatype.float(),
                    altitude:faker.datatype.float(),
                    room:rooms[getRandomInt(0,rooms.length)],
                    updatedBy:users[getRandomInt(0,users.length)],
                });
            } 

            let res=await chai.request(app)
                .post("/api/object")
                .set('content-type', 'text/json')
                .send(body)
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    
    it("PUT /api/object/:id update object",async ()=>{
        try{
            let users=[];
            let rooms=[];
            for(let i=0;i<5;i++){
                let user=await createUser();
                users.push(user.user);
                let room=await createRoom(user.user);
                rooms.push(room.room);
            }

            let body={
                name:faker.commerce.product(),
                description:faker.commerce.productDescription(),
                function:faker.commerce.department(),
                tags:[],
                urlImage:faker.internet.url(),
                urlSound:faker.internet.url(),
                price:getRandomInt(100,1000),
                sharedBy:users,
                positions:[],
                createdBy:users[getRandomInt(0,users.length)],
            };

            for(let i=0;i<getRandomInt(1,10);i++){
                body.tags.push("tag"+i);
            } 

            for(let i=0;i<getRandomInt(1,10);i++){
                body.positions.push({
                    latitude:faker.datatype.float(),
                    longitude:faker.datatype.float(),
                    altitude:faker.datatype.float(),
                    room:rooms[getRandomInt(0,rooms.length)],
                    updatedBy:users[getRandomInt(0,users.length)],
                });
            } 
            users=[];
            rooms=[];
            for(let i=0;i<5;i++){
                let user=await createUser();
                users.push(user.user);
                let room=await createRoom(user.user);
                rooms.push(room.room);
            }
            let object=await createObject(users,rooms);
            let res=await chai.request(app).put(`/api/object/${object.object._id}`)
                .set('content-type', 'text/json')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    
    it("DELETE /api/object/:id delete object",async ()=>{
        try{
            let users=[];
            let rooms=[];
            for(let i=0;i<5;i++){
                let user=await createUser();
                users.push(user.user);
                let room=await createRoom(user.user);
                rooms.push(room.room);
            }
            let object=await createObject(users,rooms);
            let res=await chai.request(app).delete(`/api/object/${object.object._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
});
