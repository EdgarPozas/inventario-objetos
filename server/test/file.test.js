/// Initialize dependencies
const faker=require("faker");
let chai = require('chai');
let chaiHttp = require('chai-http');
const expect = require('chai').expect;
let should = chai.should();
const path=require("path");
/// Server test
chai.use(chaiHttp);

/// Importing the app
const app=require("../index");

/// Import readFile
const fs=require("fs");
const util = require('util');
const readFile = util.promisify(fs.readFile);

/// Creating suit test
describe("Files",()=>{

    // it("POST /api/file upload file",async ()=>{    
    //     try{
    //         let file= await readFile(path.join(__dirname,'imagen.jpg'));

    //         let res=await chai.request(app).post(`/api/file`)
    //             .set('content-type', 'application/x-www-form-urlencoded')
    //             .attach('image', file,'imagen.jpg')
    //             .attach('audio', file,'imagen2.jpg')

    //         res.should.not.be.a("Error");
    //         res.should.have.status(200);
    //         res.body.status.should.to.equal(200);

    //     }catch(ex){
    //         console.log(ex)
    //     }
    // });
    
});
