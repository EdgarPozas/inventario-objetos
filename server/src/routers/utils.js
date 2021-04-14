/// Import dependencies
const fs = require('fs');
const pdf = require('html-pdf');

module.exports={
    
    createPDF:function(res,values,template){
        
        var html = fs.readFileSync('src/public/templates/'+template, 'utf8');

        for(let i=0;i<values.length;i++){
            html=html.replaceAll(values[i].key,values[i].value);
        }

        let fileName="report_"+Date.now()+".pdf";
        const options = { format: 'Letter' };
        pdf.create(html, options).toFile(`src/public/pdfs/${fileName}`, function(err, result) {
            if (err) {
                return res.json({
                    status:400,
                    msg:err
                });
            }
            return res.json({
                status:200,
                msg:"Report created",
                url:"http://localhost:3000/pdfs/"+fileName
            });
        });
    }
}