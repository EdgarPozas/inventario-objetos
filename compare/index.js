const Jimp = require("jimp");


async function compare(){

	const img1 = await Jimp.read("./reloj/IMG_20210415_150012.jpg");
	const img2 = await Jimp.read("./no_reloj/IMG_20210415_150727.jpg");

	console.log("Images compared \n=========================================");
	console.log(`hash (base 64) ${img1.hash()}`);
	console.log(`hash (binary)  ${img2.hash()}\n`);
    console.log(`distance       ${Jimp.distance(img1, img2)}`);
	console.log(`diff.percent   ${Jimp.diff(img1, img2).percent}\n`);
}

compare();