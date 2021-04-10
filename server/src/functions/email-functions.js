/// Initialize dependencies
const express=require("express");
const sgMail = require('@sendgrid/mail')
const fs=require("fs");
const path=require("path");
const util = require('util');
const readFile = util.promisify(fs.readFile);

module.exports={

    /// Send email
    send:async function(data){
        const file=await readFile(path.join(__dirname,"../../credentials.txt"),"utf8");
        const lines=file.split("\r\n");
        const api=lines[2];

        sgMail.setApiKey(api);

        const msg = {
            from: 'edgar730a@gmail.com',
            to: data.to,
            subject: data.subject,
            html: data.html,
        }

        try{
            await sgMail.send(msg);
            return {
                status:200,
                msg:"Message sent"
            }
        }catch(ex){
            return{
                status:400,
                msg:ex
            }
        }
    },
}
