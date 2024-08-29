<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


            <html lang="en">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>User Detail id: = ${id}</title>

                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">


                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>


                <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>

                <style>
                    .btn-small {
                        width: 150px;
                        /* Độ rộng của nút */
                        padding: 10px;
                        /* Khoảng cách bên trong */
                        font-size: 16px;
                        /* Kích thước chữ */
                    }
                </style>
            </head>

            <body>
                <div class="container mt-5">
                    <div class="row">
                        <div class="col-12 mx-auto">
                            <div class="d-flex justify-content-between">
                                <h3>Delete User Detail ${id}</h3>


                            </div>

                            <hr />
                            <div class="card" style="width: 60%;">
                                <div class="card-header">
                                    Cảnh báo
                                </div>
                                <div class="alert alert-warning" role="alert">
                                    Bạn muốn xoá chứ ?

                                </div>
                                <form:form method="post" action="/admin/user/delete" modelAttribute="newUser">
                                    <div class="mb-3" style="display: none">
                                        <label class="form-label">Id:</label>
                                        <form:input value="${id}" type="text" class="form-control" path="id" />
                                    </div>
                                    <!-- <a href="/admin/user/${user.id}" button class="btn btn-danger btn-small">YES</a> -->
                                    <button class="btn btn-danger">Confirm</button>
                                </form:form>
                            </div>

                        </div>

                    </div>
            </body>

            </html>