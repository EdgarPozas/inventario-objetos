let updateConfirmed=false;
const appProfile = new Vue({
    el: '#profileForm',
    data: {
        firstName:"",
        lastName:"",
        email:"",
        password:"*******************",
        confirmPassword:"*******************"
    },
    mounted(){
        this.firstName=$("[name='firstName']").attr("data");
		this.lastName=$("[name='lastName']").attr("data");
		this.email=$("[name='email']").attr("data");
    },
    methods:{
        sendForm:function(e)
		{
            if(updateConfirmed){
                console.log("send");
                return;
            }
			if(this.password!=this.confirmPassword)
			{
				$("#modal-check-data").modal("toggle");
				e.preventDefault();
                return;
			}

            $("#modal-update-profile").modal("toggle");
            e.preventDefault();
		}
    }
});

$("#btn-profile-yes").click(()=>
{
	updateConfirmed=true;
	$("#profileForm").submit();
});
