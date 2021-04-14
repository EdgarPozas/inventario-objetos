/// Initialize dependencies
const express=require("express");
const fs=require("fs");
const path=require("path");
const util = require('util');
const readFile = util.promisify(fs.readFile);
const AWS = require('aws-sdk');

module.exports={

    /// Upload file
    upload:async function(file){

        const fileCredentials=await readFile(path.join(__dirname,"../../credentials.txt"),"utf8");
        const lines=fileCredentials.split("\n");

        const s3 = new AWS.S3({
            accessKeyId: lines[3].replace("\r",""),
            secretAccessKey: lines[4].replace("\r","")
        });

        const params = {
            Bucket: 'inventario-objetos',
            Key: file.originalname,
            Body: file.buffer
        };
        
        return new Promise((resolve,reject)=>{
            s3.upload (params, function (err, data) {
                if (err) {
                    reject({
                        status:400,
                        url:'',
                        msg:err
                    });
                } 
                resolve({
                    status:200,
                    url:data.Location,
                    msg:"File uploaded"
                });
            });
        });
    },
}
