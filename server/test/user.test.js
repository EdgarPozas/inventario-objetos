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

/// Import user
const User=require("../src/models/user");

/// Import utils
const {createUser}=require("./utils");

/// Clean users
beforeEach(async function() {
    await User.deleteMany({})
});

/// Creating suit test
describe("User",()=>{

    it("GET /api/user show all users",async ()=>{
        try{
            let res=await chai.request(app).get("/api/user");
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
            res.body.users.should.be.a("array");
        }catch(ex){
            console.log(ex)
        }
    });

    it("GET /api/user/:id show user by id",async ()=>{
        try{
            let user=await createUser();
            let res=await chai.request(app).get(`/api/user/id/${user.user._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });

    it("GET /api/user/email/:email show user by email",async ()=>{
        try{
            let user=await createUser();
            let res=await chai.request(app).get(`/api/user/email/${user.user.email}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    

    it("POST /api/user/login login",async ()=>{
        try{
            let user=await createUser();
            let body={
                email:user.user.email,
                password:"123",
            };
            let res=await chai.request(app)
                .post(`/api/user/login`)
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });

    it("POST /api/user create user",async ()=>{    
        try{
            let body={
                firstName:faker.name.firstName(),
                lastName:faker.name.lastName(),
                email:faker.internet.email(),
                password:"123"
            };
            let res=await chai.request(app).post(`/api/user`)
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    
    it("PUT /api/user/:id update user",async ()=>{
        try{
            let user=await createUser();
            let body={
                firstName:user.user.firstName,
                lastName:user.user.lastName,
                email:user.user.email,
                password:"123",
            };
            let res=await chai.request(app).put(`/api/user/${user.user._id}`)
                .set('content-type', 'application/x-www-form-urlencoded')
                .send(body);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
    

    it("GET /api/user/verify/:id verify user",async ()=>{
        try{
            let user=await createUser();
            let res=await chai.request(app).get(`/api/user/verify/${user.user._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });

    it("DELETE /api/user/:id delete user",async ()=>{
        try{
            let user=await createUser();
            let res=await chai.request(app).delete(`/api/user/${user.user._id}`);
            res.should.not.be.a("Error");
            res.should.have.status(200);
            res.body.status.should.to.equal(200);
        }catch(ex){
            console.log(ex)
        }
    });
});
