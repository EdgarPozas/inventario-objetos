
const app=new Vue({
    el:"#user-individual-component",
    data:{
        filterSelected:0,
        tableMode:false,
        objects:[],
        rooms:[],
        user:"",
        waitingResponse:false,
    },
    mounted(){
        this.objects=JSON.parse($("#user-objects").attr("data"));
        this.rooms=JSON.parse($("#user-rooms").attr("data"));
        this.user=JSON.parse($("#user-user").attr("data"));
        this.loadChart("objectsChart",this.objects,'Objetos registrados por día','Gráfica de objetos registrados por día desde que se registró el miembro');
        this.loadChart("roomsChart",this.rooms,'Espacios registrados por día','Gráfica de espacios registrados por día desde que se registró el miembro');
    },
    methods:{
        select:function(i){
            this.filterSelected=i;
        },
        loadChart:function(id,values,label,title){
            var ctx = document.getElementById(id).getContext('2d');
            
            let startDate=new Date(this.user.createdAt);
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
        makeReport:async function(){
            try{
                this.waitingResponse=true;
                let result=await axios.post("/report/user-individual",{
                    data:[
                        {
                            user:this.user
                        },
                        {
                            objects:this.objects
                        },
                        {
                            rooms:this.rooms
                        }
                    ]
                });
                this.waitingResponse=false;
                if(result.data.status!=200)
                    throw Error(result.data.msg);
                window.open(result.data.url);
            }catch(ex){
                console.log(ex);
            }
        }
    }
});
