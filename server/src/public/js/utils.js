Date.prototype.addDays = function(days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
}
Date.prototype.addMonths = function(months) {
    var date = new Date(this.valueOf());
    date.setDate(date.getMonth() + months);
    return date;
}
Date.prototype.addYear = function(years) {
    var date = new Date(this.valueOf());
    date.setDate(date.getFullYear() + years);
    return date;
}
function dateToString(date){
    return date.getDate()+"/"+months[date.getMonth()]+"/"+date.getFullYear();
}
let months=["Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"];