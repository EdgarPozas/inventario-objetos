const appAllGroup = new Vue({
	el: '#allGroups',
	data:
	{
		user:[],
		groups:[],
		auxGroup:undefined
	},
	mounted()
	{
		this.user=JSON.parse($("[name='user']").attr("data"));
		this.groups=this.user.groups;
	},
	methods:
	{
		getAllGroups:async function(){
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
		},
		deleteGroup:async function(){
			try{
                let values=await axios.delete(server+"/group/"+this.auxGroup._id);
                if(values.data.status!=200)
                    throw Error(values.data.msg);

                appAllGroup.groups.pull(this.auxGroup);
            }catch(ex){
                console.log(ex);
                $("#modal-group-delete-error").modal("toggle");
            }
		}
	}
});

$("#btn-confirm-delete-group").click(()=>{
	$("#modal-group-delete").modal("toggle");
	appAllGroup.deleteGroup();
});
