const app=new Vue({
    el:"#object-component",
    data:{
        name:"",
        description:"",
        functionality:"",
        priceMin:0,
        priceMax:0,
        tag:"",
        tags:[],
        userShared:"",
        room:"",
        usersAvailable:[],
        usersShared:[],
        tableMode:true,
        objectsOriginal:[],
        objects:[],
        rooms:[]
    },
    mounted(){
        this.objectsOriginal=JSON.parse($("#object-data").attr("data"));
        this.usersAvailable=JSON.parse($("#users-data").attr("data"));
        this.rooms=JSON.parse($("#rooms-data").attr("data"));
        this.filter();
    },
    methods:{
        filter:function(){
            Object.assign(this.objects,this.objectsOriginal);
            if(this.name!="")
                this.objects=this.objects.filter(x=>x.name.toLowerCase().includes(this.name.toLowerCase()));
            if(this.description!="")
                this.objects=this.objects.filter(x=>x.description.toLowerCase().includes(this.description.toLowerCase()));
            if(this.functionality!="")
                this.objects=this.objects.filter(x=>x.functionality.toLowerCase().includes(this.functionality.toLowerCase()));
            if(this.priceMin!=0){
                this.objects=this.objects.filter(x=> x.price>=this.priceMin);
            }
            if(this.priceMax!=0){
                this.objects=this.objects.filter(x=> x.price<=this.priceMax);
            }
            if(this.tags.length>0){
                let tmpObjects=[];
                for(let i=0;i<this.objects.length;i++){
                    let tags=this.objects[i].tags.map(x=>x.toLowerCase());
                    let myTags=this.tags.map(x=>x.toLowerCase());
                    let tagsSelected=[];
                    for(let e=0;e<myTags.length;e++){
                        for(let k=0;k<tags.length;k++){
                            if(myTags[e]==tags[k] && !tagsSelected.includes(myTags[e])){
                                tagsSelected.push(myTags[e])
                            }
                        }    
                    }
                    if(counter>=myTags.length)
                        tmpObjects.push(this.objects[i]);
                }
                this.objects=tmpObjects;
            }
            if(this.usersShared.length>0){
                let tmpObjects=[];
                for(let i=0;i<this.objects.length;i++){
                    let usersShared=this.objects[i].sharedBy.map(x=>x._id);
                    let myUsers=this.usersShared.map(x=>x._id);
                    let usersSelected=[];
                    for(let e=0;e<myUsers.length;e++){
                        for(let k=0;k<usersShared.length;k++){
                            if(myUsers[e]==usersShared[k] && !usersSelected.includes(myUsers[e])){
                                usersSelected.push(myUsers[e]);
                            }
                        }    
                    }
                    if(usersSelected.length>=myUsers.length)
                        tmpObjects.push(this.objects[i]);
                }
                this.objects=tmpObjects;
            }
            // if(this.room!="")
            //     this.objects=this.objects.filter(x=>x.functionality.toLowerCase().includes(this.functionality.toLowerCase()));
        },
        addTag:function(){
            this.tags.push(this.tag);
            this.tag="";
            this.filter();
        },
        removeTag:function(i){
            this.tags.splice(i,1);
            this.filter();
        },
        addShared:function(){
            this.usersAvailable.splice(this.usersAvailable.indexOf(this.userShared),1)
            this.usersShared.push(this.userShared);
            this.filter();
            this.userShared="";
        },
        removeShared:function(i){
            this.usersAvailable.push(this.usersShared[i])
            this.usersShared.splice(i,1);
            this.filter();
        },
        clearFilters:function(){
            this.reset();
            Object.assign(this.objects,this.objectsOriginal);
        },
        reset:function(){
            this.name="";
            this.description="";
            this.functionality="";
            this.price="";
            this.tag="";
            this.tags=[];
            this.userShared="";
            this.usersShared=[];
            this.room="";
        }
    }
});
