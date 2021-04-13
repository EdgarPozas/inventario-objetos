let map;
const app=new Vue({
    el:"#object-individual-component",
    data:{
        filterSelected:0,
        tableMode:false,
        rooms:[],
        object:"",
    },
    mounted(){
        this.rooms=JSON.parse($("#object-rooms").attr("data"));
        this.object=JSON.parse($("#object-object").attr("data"));
        this.loadChart("timesMovedDay",1,"Veces que se ha movido","Gráfica de las veces que se ha movido el objeto por día");
        this.loadChart("timesMovedWeek",7,"Veces que se ha movido","Gráfica de las veces que se ha movido el objeto por semana");
        this.loadChart("timesMovedMonth",30,"Veces que se ha movido","Gráfica de las veces que se ha movido el objeto por mes");
        this.loadChart("timesMovedYear",365,"Veces que se ha movido","Gráfica de las veces que se ha movido el objeto por año");
    },
    methods:{
        init:function(){
            let positions=this.object.positions.reverse();
            let size=positions.length;
            for(let i=0;i<positions.length;i++){
                let position=positions[i];
                const pos = { lat: position.latitude, lng: position.longitude };
                if(i==0){
                    const marker = new google.maps.Marker({
                        position: pos,
                        map: map,
                        label: { color: '#fff', fontWeight: 'bold', fontSize: '14px', text: (size-i)+""},
                        zIndex:size,
                        icon:"https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|0045DD"
                      });
                }else{
                    const marker = new google.maps.Marker({
                        position: pos,
                        map: map,
                        label: { color: '#fff', fontWeight: 'bold', fontSize: '14px', text: (size-i)+""},
                        icon:"https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|DD2342",
                        zIndex:1,
                      });
                }
            }
        },
        select:function(i){
            this.filterSelected=i;
        },
        selectPosition:function(i){
            let position=this.object.positions[i];
            map.setZoom(20);
            const pos = { lat: position.latitude, lng: position.longitude };
            map.panTo(pos);
        },
        loadChart:function(id,days,label,title){
            var ctx = document.getElementById(id).getContext('2d');

            const dates=(date)=> new Date(new Date()-new Date(date));
            
            let startDate=new Date().addDays(-days);
            let labels=[];
            let data=[];
            let backgroundColor=[];
            let borderColor=[];

            for(let i=1;i<=days;i++){
                let newDate=startDate.addDays(i);
                labels.push(dateToString(newDate));
                backgroundColor.push('rgba(54, 162, 235, 0.2)');
                borderColor.push('rgba(54, 162, 235, 1)');
                let amount=0;
                this.object.positions.forEach(x=>{
                    let dateCreated=new Date(x.createdAt);
                    if(
                        dateCreated.getDate()==newDate.getDate() && 
                        dateCreated.getMonth()==newDate.getMonth() &&
                        dateCreated.getFullYear()==newDate.getFullYear()){
                            amount++;
                        }
                });
                data.push(amount);
            }
            
            var myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: label,
                        data: data,
                        backgroundColor: backgroundColor,
                        borderColor: borderColor,
                        borderWidth: 1
                    }]
                },
                options: {
                    title: {
                        display: true,
                        text: title
                    },
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        },
    }
});
function initMap(){
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 21.5017081, lng: -104.8715752 },
        zoom: 12,
    });
    app.init();
}