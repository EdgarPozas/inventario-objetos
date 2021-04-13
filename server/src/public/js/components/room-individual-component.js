const app=new Vue({
    el:"#room-individual-component",
    data:{
        filterSelected:0,
        tableMode:false,
        objects:[],
        room:""
    },
    mounted(){
        this.objects=JSON.parse($("#room-objects").attr("data"));
        this.room=JSON.parse($("#room-room").attr("data"));
        this.loadChart("roomsChart",
            this.objects,
            "Objetos agregados por día",
            "Gráfica de objetos agregados por día desde que se registró el espacio");
    },
    methods:{
        select:function(i){
            this.filterSelected=i;
        },
        loadChart:function(id,values,label,title){
            var ctx = document.getElementById(id).getContext('2d');
            
            let startDate=new Date(this.room.createdAt);
            let days=new Date(Date.now()-startDate).getDate();
            let labels=[];
            let data=[];
            let backgroundColor=[];
            let borderColor=[];

            for(let i=0;i<days;i++){
                let newDate=startDate.addDays(i);
                labels.push(dateToString(newDate));
                backgroundColor.push('rgba(54, 162, 235, 0.2)');
                borderColor.push('rgba(54, 162, 235, 1)');
                let amount=0;
                values.forEach(x=>{
                    x.positions.forEach(y=>{
                        let dateCreated=new Date(y.createdAt);
                        if(
                            dateCreated.getDate()==newDate.getDate() && 
                            dateCreated.getMonth()==newDate.getMonth() &&
                            dateCreated.getFullYear()==newDate.getFullYear()){
                                amount++;
                            }
                    });
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
