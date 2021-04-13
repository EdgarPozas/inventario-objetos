const app=new Vue({
    el:"#room-individual-component",
    data:{
        filterSelected:0,
        tableMode:false,
        objects:[],
    },
    mounted(){
        this.objects=JSON.parse($("#room-objects").attr("data"));
    },
    methods:{
        select:function(i){
            this.filterSelected=i;
        },
    }
});
