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

/// Creating suit test
describe("User",()=>{

    it("POST /user  create user",(done)=>{
        let body={
            firstName:faker.name.firstName(),
            lastName:faker.name.lastName(),
            email:faker.internet.email(),
            password:faker.internet.password(),
        };

        chai.request(app)
            .post("/user")
            .send(body)
            .end((err,res)=>{
                if(err)
                    return;
                res.should.have.status(200);
                done();
            });
    });

    it("GET /user show all users",(done)=>{
        chai.request(app)
            .get("/user")
            .end((err,res)=>{
                if(err)
                   return;
                res.should.have.status(200);
                res.body.users.should.be.a("array");
                done();
            });
    });

    it("GET /user/:id show user",(done)=>{
        let id="6068aee2d36f4328f4a3c2b0";

        chai.request(app)
            .get(`/user/${id}`)
            .end((err,res)=>{
                if(err)
                   return;
                res.should.have.status(200);
                res.body.should.have.property("user");
                done();
            });
    });

    it("PUT /user/:id update user",(done)=>{
        let body={
            firstName:faker.name.firstName(),
            lastName:faker.name.lastName(),
            email:faker.internet.email(),
            password:faker.internet.password(),
        };
        let id="6068aee2d36f4328f4a3c2b0";

        chai.request(app)
            .put(`/user/${id}`)
            .send(body)
            .end((err,res)=>{
                if(err)
                    return;
                res.should.have.status(200);
                done();
            });
    });

    it("DELETE /user/:id delete user",(done)=>{

        let id="6068aee2d36f4328f4a3c2b0";

        chai.request(app)
            .delete(`/user/${id}`)
            .end((err,res)=>{
                if(err)
                    return;
                res.should.have.status(200);
                done();
            });
    });
});
