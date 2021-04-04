let server="http://localhost:3000"
const appAddGroup = new Vue({
    el: '#addGroupModal',
    data: {
        id:"",
        name:"",
    },
    mounted(){
        this.id=$("[name='id']").attr("data");
    },
    methods:{
        sendForm:async function(){
            try{
                let values=await axios.post(server+"/group",{
                    id:this.id,
                    name:this.name
                });
                if(values.data.status!=200)
                    throw Error(values.data.msg);
                appAllGroup.groups.push(values.data.group);
                $("#modal-group-created").modal("toggle");
            }catch(ex){
                console.log(ex);
                $("#modal-group-error").modal("toggle");
            }
		}
    }
})
