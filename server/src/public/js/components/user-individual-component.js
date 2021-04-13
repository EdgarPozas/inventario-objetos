const app=new Vue({
    el:"#user-individual-component",
    data:{
        filterSelected:0,
        tableMode:false,
        objects:[],
        rooms:[]
    },
    mounted(){
        this.objects=JSON.parse($("#user-objects").attr("data"));
        this.rooms=JSON.parse($("#user-rooms").attr("data"));
    },
    methods:{
        select:function(i){
            this.filterSelected=i;
        },
    }
});
