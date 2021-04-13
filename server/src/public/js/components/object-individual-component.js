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
        }
    }
});
function initMap(){
    map = new google.maps.Map(document.getElementById("map"), {
        center: { lat: 21.5017081, lng: -104.8715752 },
        zoom: 12,
    });
    app.init();
}