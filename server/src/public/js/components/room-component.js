const app=new Vue({
    el:"#room-component",
    data:{
        name:"",
        description:"",
        active:true,
        tableMode:false,
        useFilter:true,
        filterSelected:0,
        roomsOriginal:[],
        rooms:[],
        waitingResponse:false,
    },
    mounted(){
        this.roomsOriginal=JSON.parse($("#room-data").attr("data"));
        this.filter();
    },
    methods:{
        filter:function(){
            Object.assign(this.rooms,this.roomsOriginal);
            if(!this.useFilter)
                return;
            if(this.name!="")
                this.rooms=this.rooms.filter(x=>x.name.toLowerCase().includes(this.name.toLowerCase()));
            if(this.description!="")
                this.rooms=this.rooms.filter(x=>x.description.toLowerCase().includes(this.description.toLowerCase()));
            this.rooms=this.rooms.filter(x=>x.active==this.active);
        },
        removeFilters:function(){
            this.useFilter=! this.useFilter;
            this.filter();
        },
        clearFilters:function(){
            this.reset();
            Object.assign(this.rooms,this.roomsOriginal);
        },
        select:function(i){
            this.filterSelected=i;
        },
        reset:function(){
            this.name="";
            this.description="";
            this.active=true;
        },
        makeReport:async function(){
            try{
                this.waitingResponse=true;
                let result=await axios.post("/report/room",{
                    filters:[
                        {
                            name:"Nombre",
                            filter: this.name
                        },
                        {
                            name:"Descripción",
                            filter: this.description
                        },
                        {
                            name:"Activo",
                            filter: this.active
                        },
                    ],
                    data:[
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
