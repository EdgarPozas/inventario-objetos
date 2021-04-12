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

/// Import position
const Position=require("../src/models/position");

/// Import utils
const {createUser,createRoom,createPosition,getUserExisting,getRoomExisting}=require("./utils");

/// Clean position
// beforeEach(async function() {
//     await Position.deleteMany({})
// });

/// Creating suit test
describe("Position",()=>{

    it("GET /api/position show all positions",async ()=>{
        try{
            let res=await chai.request(app).get("/api/position");
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
            res.body.positions.should.be.a("array");
        }catch(ex){
            console.log(ex)
        }
    });

    it("GET /api/position/id/:id show position by id",async ()=>{
        try{
            let user=await getUserExisting();
            let room=await getRoomExisting();
            let position=await createPosition(user,room);
            let res=await chai.request(app).get(`/api/position/id/${position.position._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });

    it("POST /api/position create position",async ()=>{    
        try{
            let user=await getUserExisting();
            let room=await getRoomExisting();
            let body={
                latitude:faker.address.latitude(),
                longitude:faker.address.longitude(),
                altitude:faker.address.latitude(),
                room:room._id,
                createdBy:user._id
            };
            let res=await chai.request(app).post(`/api/position`)
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    
    it("PUT /api/position/:id update position",async ()=>{
        try{
            let user=await getUserExisting();
            let room=await getRoomExisting();
            let position=await createPosition(user,room);
            let body={
                latitude:faker.address.latitude(),
                longitude:faker.address.longitude(),
                altitude:faker.address.latitude(),
                room:room._id,
            };
            let res=await chai.request(app).put(`/api/position/${position.position._id}`)
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    
    it("DELETE /api/position/:id delete position",async ()=>{
        try{
            let user=await getUserExisting();
            let room=await getRoomExisting();
            let position=await createPosition(user,room);
            let res=await chai.request(app).delete(`/api/position/${position.position._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
});
