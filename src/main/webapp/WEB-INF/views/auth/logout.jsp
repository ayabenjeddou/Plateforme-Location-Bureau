<%@page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Logout</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body {
            background: linear-gradient(135deg, #7C3AED, #A78BFA);
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Poppins', sans-serif;
        }

        .card-logout {
            background: white;
            padding: 40px;
            border-radius: 20px;
            text-align: center;
            box-shadow: 0 25px 50px rgba(0,0,0,0.2);
        }

        .icon {
            font-size: 60px;
            color: #7C3AED;
            margin-bottom: 20px;
        }

        .btn-home {
            margin-top: 20px;
            background: #7C3AED;
            color: white;
            padding: 10px 20px;
            border-radius: 10px;
            text-decoration: none;
            display: inline-block;
        }

        .btn-home:hover {
            background: #5B21B6;
        }
    </style>
</head>

<body>

<div class="card-logout">
    <div class="icon">👋</div>

    <h3>You have been logged out</h3>
    <p>Merci pour votre visite</p>

    <a href="${pageContext.request.contextPath}/login" class="btn-home">
        Login again
    </a>
</div>

</body>
</html>