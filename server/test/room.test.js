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

/// Import room
const Room=require("../src/models/room");

/// Import utils
const {createUser,createRoom,getUserExisting}=require("./utils");

/// Clean room
// beforeEach(async function() {
//     await Room.deleteMany({})
// });

/// Creating suit test
describe("Room",()=>{

    it("GET /api/room show all rooms",async ()=>{
        try{
            let res=await chai.request(app).get("/api/room");
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
            res.body.rooms.should.be.a("array");
        }catch(ex){
            console.log(ex)
        }
    });

    it("GET /api/room/id/:id show room by id",async ()=>{
        try{
            let user=await getUserExisting();
            let room=await createRoom(user);
            let res=await chai.request(app).get(`/api/room/id/${room.room._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });

    it("POST /api/room create room",async ()=>{    
        try{
            let user=await getUserExisting();
            let body={
                name:faker.commerce.product(),
                description:faker.commerce.productDescription(),
                id: user._id
            };
            let res=await chai.request(app).post(`/api/room`)
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    
    it("PUT /api/room/:id update room",async ()=>{
        try{
            let user=await getUserExisting();
            let room=await createRoom(user);
            let body={
                name:faker.commerce.product(),
                description:faker.commerce.productDescription(),
            };
            let res=await chai.request(app).put(`/api/room/${room.room._id}`)
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    
    it("DELETE /api/room/:id delete room",async ()=>{
        try{
            let user=await getUserExisting();
            let room=await createRoom(user);
            let res=await chai.request(app).delete(`/api/room/${room.room._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
});
