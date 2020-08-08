
<div class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand">
            <img src="${pageContext.request.contextPath}/media/images/green.svg" width="70" height="70" alt="" class="d-inline-block align-middle mr-2">
        </a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav mr-auto">
                <li class="nav-item active">
                    <a href="${pageContext.request.contextPath}/nouvellevente" class="nav-item nav-link active">Vendre un article</a>
                </li>
            </ul>
            <ul class="navbar-nav ml-auto">
                <c:choose>
                    <c:when test="${user != null}">

                        <li class="nav-item active">
                            <a href="${pageContext.request.contextPath}/profil" class="nav-item nav-link">Profil</a>
                        </li>

                        <li class="nav-item active">
                            <a href="${pageContext.request.contextPath}/logout" class="nav-item nav-link">Logout</a>
                        </li>

                    </c:when>
                    <c:otherwise>
                        <li class="nav-item active">
                            <a href="${pageContext.request.contextPath}/login" class="nav-item nav-link">Se connecter</a>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </nav>
</div>










