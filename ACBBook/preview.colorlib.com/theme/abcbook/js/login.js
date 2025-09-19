$(document).ready(function() {

    $('#btn-login').click(function() {

        var email = $("#email").val()
        var password = $("#password").val()

        $.ajax({
            url: "http://localhost:9090/auth/sign-in",
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify({
                email: email,
                password: password
            }),
            success: function (response) {
                if (response.code === 200) {
                    // Save token to localStorage
                    localStorage.setItem("token", response.data);
                    console.log("Login successful! Token saved: ", response.data);
                } else {
                    console.error("Login failed: " + response.message);
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.error("AJAX Error: ", textStatus, errorThrown);
            }
        });
    })
})