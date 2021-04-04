const appRecovery = new Vue({
    el: '#recoveryForm',
    data: {
        changePassword:"",
        changeConfirmPassword:""
    },
    methods:{
        sendFormChange:function(e)
		{
			if(this.changePassword!=this.changeConfirmPassword)
			{
				$("#modal-check-data").modal("toggle");
				e.preventDefault();
			}
		}
    }
})
