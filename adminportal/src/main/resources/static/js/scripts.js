$(function () {
	
	/*var last_valid_selection = null;
    $('#skillSelect').change(function(event) {
      if ($(this).val().length > 4) {
        alert('Vous devez choisir 4 compétences maximum');
        $(this).removeAttr("selected");
        $(this).val(last_valid_selection);
      } else {
        last_valid_selection = $(this).val();
      }
    });*/
	
	$("#txtNewPassword").keyup(function() {
	var password = $("#txtNewPassword").val();
	var confirmPassword = $("#txtConfirmPassword").val();
	
	if(password == "" && confirmPassword =="") {
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	} else {
		if(password != confirmPassword) {
			$("#checkPasswordMatch").html("Les mots de passe ne correspondent pas");
			$("#updateUserInfoButton").prop('disabled', true);
		} else {
			$("#checkPasswordMatch").html("Les mots de passe correspondent");
			$("#updateUserInfoButton").prop('disabled', false);
		}
	}
	});

	$("#txtNewPassword").on('input', function() {
	var password = $("#txtNewPassword").val();
	var confirmPassword = $("#txtConfirmPassword").val();
	
	if(password == "" && confirmPassword =="") {
		$("#checkPasswordMatch").html("");
		$("#updateUserInfoButton").prop('disabled', false);
	} else {
		if(password != confirmPassword) {
			$("#checkPasswordMatch").html("Les mots de passe ne correspondent pas");
			$("#updateUserInfoButton").prop('disabled', true);
		} else {
			$("#checkPasswordMatch").html("Les mots de passe correspondent");
			$("#updateUserInfoButton").prop('disabled', false);
		}
	}
	});
	
	$("#txtConfirmPassword").keyup(function() {
		var password = $("#txtNewPassword").val();
		var confirmPassword = $("#txtConfirmPassword").val();
		
		if(password == "" && confirmPassword =="") {
			$("#checkPasswordMatch").html("");
			$("#updateUserInfoButton").prop('disabled', false);
		} else {
			if(password != confirmPassword) {
				$("#checkPasswordMatch").html("Les mots de passe ne correspondent pas");
				$("#updateUserInfoButton").prop('disabled', true);
			} else {
				$("#checkPasswordMatch").html("Les mots de passe correspondent");
				$("#updateUserInfoButton").prop('disabled', false);
			}
		}
		});

	$("#txtConfirmPassword").on('input', function() {
		var password = $("#txtNewPassword").val();
		var confirmPassword = $("#txtConfirmPassword").val();
		
		if(password == "" && confirmPassword =="") {
			$("#checkPasswordMatch").html("");
			$("#updateUserInfoButton").prop('disabled', false);
		} else {
			if(password != confirmPassword) {
				$("#checkPasswordMatch").html("Les mots de passe ne correspondent pas");
				$("#updateUserInfoButton").prop('disabled', true);
			} else {
				$("#checkPasswordMatch").html("Les mots de passe correspondent");
				$("#updateUserInfoButton").prop('disabled', false);
			}
		}
		});
	
	$("#email1").keyup(function() {
		var email1 = $("#email1").val();
		var email2 = $("#email2").val();
		
		if(email1 == "" && email2 =="") {
			$("#checkemailMatch").html("");
			$("#contact-submit").prop('disabled', false);
		} else {
			if(email1 != email2) {
				$("#checkemailMatch").html("Les emails ne correspondent pas");
				$("#contact-submit").prop('disabled', true);
			} else {
				$("#checkemailMatch").html("Les emails correspondent");
				$("#contact-submit").prop('disabled', false);
			}
		}
		});

	$("#email1").on('input', function() {
		var email1 = $("#email1").val();
		var email2 = $("#email2").val();
		
		if(email1 == "" && email2 =="") {
			$("#checkemailMatch").html("");
			$("#contact-submit").prop('disabled', false);
		} else {
			if(email1 != email2) {
				$("#checkemailMatch").html("Les emails ne correspondent pas");
				$("#contact-submit").prop('disabled', true);
			} else {
				$("#checkemailMatch").html("Les emails correspondent");
				$("#contact-submit").prop('disabled', false);
			}
		}
		});
		
		$("#email2").keyup(function() {
			var email1 = $("#email1").val();
			var email2 = $("#email2").val();
			
			if(email1 == "" && email2 =="") {
				$("#checkemailMatch").html("");
				$("#contact-submit").prop('disabled', false);
			} else {
				if(email1 != email2) {
					$("#checkemailMatch").html("Les emails ne correspondent pas");
					$("#contact-submit").prop('disabled', true);
				} else {
					$("#checkemailMatch").html("Les emails correspondent");
					$("#contact-submit").prop('disabled', false);
				}
			}
			});

		$("#email2").on('input', function() {
			var email1 = $("#email1").val();
			var email2 = $("#email2").val();
			
			if(email1 == "" && email2 =="") {
				$("#checkemailMatch").html("");
				$("#contact-submit").prop('disabled', false);
			} else {
				if(email1 != email2) {
					$("#checkemailMatch").html("Les emails ne correspondent pas");
					$("#contact-submit").prop('disabled', true);
				} else {
					$("#checkemailMatch").html("Les emails correspondent");
					$("#contact-submit").prop('disabled', false);
				}
			}
			});
	
	});