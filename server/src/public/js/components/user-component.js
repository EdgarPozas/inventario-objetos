const app=new Vue({
    el:"#user-component",
    data:{
        firstName:"",
        lastName:"",
        email:"",
        filterSelected:"",
        useFilter:true,
        active:true,
        verified:true,
        tableMode:false,
        usersOriginal:[],
        users:[],
        waitingResponse:false,
    },
    mounted(){
        // console.log($("#user-data").attr("data"));
        this.usersOriginal=JSON.parse($("#user-data").attr("data"));
        this.filter();
    },
    methods:{
        filter:function(){
            Object.assign(this.users,this.usersOriginal);
            if(!this.useFilter)
                return;
            if(this.firstName!="")
                this.users=this.users.filter(x=>x.firstName.toLowerCase().includes(this.firstName.toLowerCase()));
            if(this.lastName!="")
                this.users=this.users.filter(x=>x.lastName.toLowerCase().includes(this.lastName.toLowerCase()));
            if(this.email!="")
                this.users=this.users.filter(x=>x.email.toLowerCase().includes(this.email.toLowerCase()));
            this.users=this.users.filter(x=>x.active==this.active);
            this.users=this.users.filter(x=>x.verified==this.verified);
        },
        removeFilters:function(){
            this.useFilter=! this.useFilter;
            this.filter();
        },
        clearFilters:function(){
            this.reset();
            Object.assign(this.users,this.usersOriginal);
        },
        select:function(i){
            this.filterSelected=i;
        },
        reset:function(){
            this.firstName="";
            this.lastName="";
            this.email="";
            this.active=true;
            this.verified=true;
        },
        makeReport:async function(){
            try{
                this.waitingResponse=true;
                let result=await axios.post("/report",{
                    filters:[
                        {
                            name:"Nombre",
                            filter: this.firstName
                        },
                        {
                            name:"Apellido",
                            filter: this.lastName
                        },
                        {
                            name:"Correo",
                            filter: this.email
                        },
                        {
                            name:"Activo",
                            filter: this.active
                        },
                        {
                            name:"Verificado",
                            filter: this.verified
                        }
                    ],
                    data:this.users
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
