/// Import dependencies
const fs = require('fs');
const pdf = require('html-pdf');
const fileFunctions = require('../functions/file-functions');
/// Functions
const fileFunctons=require("../functions/file-functions");

module.exports={
    
    createPDF:function(res,values,template){
        
        var html = fs.readFileSync('src/public/templates/'+template, 'utf8');

        for(let i=0;i<values.length;i++){
            html=html.replaceAll(values[i].key,values[i].value);
        }

        
        const options = { format: 'Letter' };
    
        pdf.create(html, options).toBuffer(async function(err, buffer) {
            if (err) {
                return res.json({
                    status:400,
                    msg:err
                });
            }
            try{
                const fileReport={
                    originalname:"report_"+Date.now()+".pdf",
                    buffer:buffer
                }

                let values=await fileFunctions.upload(fileReport);
                if(values.status!=200)
                    throw Error(values.msg)
                    
                return res.json({
                    status:200,
                    msg:"Report created",
                    url:values.url
                });
            }catch(ex){
                console.log(ex);
                return res.json({
                    status:400,
                    msg:ex,
                    url:""
                });
            }
        });
    }
}