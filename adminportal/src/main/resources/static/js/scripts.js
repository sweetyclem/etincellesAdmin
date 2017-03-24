$(document).ready(
		function() {
			$("#usersTable").DataTable(
					{
						"lengthMenu" : [ [ 5, 10, 15, 20, -1 ],
								[ 5, 10, 15, 20, "All" ] ],
						"ordering" : true,
						stateSave : true,
						colReorder : true,
						responsive : true,
						buttons : [ 'copy', 'excel', 'pdf' ]
					});
			
			$('.forgetPass').on('click', function (){
				/*<![CDATA[*/
			    var path = /*[[@{/}]]*/'forgetPassword';
			    /*]]>*/
				
				var id=$(this).attr('id');
				
				bootbox.confirm({
					message: "Supprimer l'article ?",
					buttons: {
						cancel: {
							label:'<i class="fa fa-times"></i> Annuler'
						},
						confirm: {
							label:'<i class="fa fa-check"></i> Confirmer'
						}
					},
					callback: function(confirmed) {
						if(confirmed) {
							$.post(path, {'id':id}, function(res) {
								location.reload();
							});
						}
					}
				});
			});
	});