/// Initialize dependencies
const mongoose=require('mongoose');
const fs=require("fs");
const path=require("path");
const util = require('util');
const readFile = util.promisify(fs.readFile);

/// Read credentials file and connect to the database
async function initDataBase(){
    const data=await readFile(path.join(__dirname,"../../credentials.txt"),"utf8");
    const lines=data.split("\r\n");
    const password=lines[0];
    const dataBaseName=lines[1];
    mongoose.connect(`mongodb+srv://edgar:${password}@cluster0.uldfg.mongodb.net/${dataBaseName}?retryWrites=true&w=majority`, {useNewUrlParser: true, useUnifiedTopology: true});
}

initDataBase();
