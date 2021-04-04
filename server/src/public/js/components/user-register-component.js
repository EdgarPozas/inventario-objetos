const appRegister = new Vue({
    el: '#registerModal',
    data: {
        password:"",
        confirmPassword:""
    },
    methods:{
        sendForm:function(e)
		{
			if(this.password!=this.confirmPassword)
			{
				$("#modal-check-data").modal("toggle");
				e.preventDefault();
			}
		}
    }
})
